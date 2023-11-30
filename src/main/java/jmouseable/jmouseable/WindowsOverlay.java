package jmouseable.jmouseable;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import jmouseable.jmouseable.Grid.GridBuilder;
import jmouseable.jmouseable.WindowsMouse.CursorPositionAndSize;

import java.util.Objects;

public class WindowsOverlay {

    private static final int indicatorEdgeThreshold = 100; // in pixels
    private static final int indicatorSize = 16;

    private static WinDef.HWND indicatorWindowHwnd;
    private static GridWindow gridWindow;
    private static boolean showingIndicator;
    private static String currentIndicatorHexColor;
    private static boolean showingGrid;
    private static Grid currentGrid;

    public static GridBuilder gridFittingActiveWindow(GridBuilder grid,
                                                      double windowWidthPercent,
                                                      double windowHeightPercent) {
        WinDef.HWND foregroundWindow = User32.INSTANCE.GetForegroundWindow();
        // https://stackoverflow.com/a/65605845
        WinDef.RECT excludeShadow = windowRectExcludingShadow(foregroundWindow);
        int windowWidth = excludeShadow.right - excludeShadow.left;
        int windowHeight = excludeShadow.bottom - excludeShadow.top;
        int gridWidth = (int) (windowWidth * windowWidthPercent);
        int gridHeight = (int) (windowHeight * windowHeightPercent);
        return grid.x(excludeShadow.left + (windowWidth - gridWidth) / 2)
                   .y(excludeShadow.top + (windowHeight - gridHeight) / 2)
                   .width(gridWidth)
                   .height(gridHeight);
    }

    private static WinDef.RECT windowRectExcludingShadow(WinDef.HWND hwnd) {
        // On Windows 10+, DwmGetWindowAttribute() returns the extended frame bounds excluding shadow.
        WinDef.RECT rect = new WinDef.RECT();
        Dwmapi.INSTANCE.DwmGetWindowAttribute(hwnd, Dwmapi.DWMWA_EXTENDED_FRAME_BOUNDS,
                rect, rect.size());
        return rect;
    }

    public static void setTopmost() {
        if (indicatorWindowHwnd != null)
            setWindowTopmost(indicatorWindowHwnd);
        if (gridWindow != null)
            setWindowTopmost(gridWindow.hwnd);
    }

    private static void setWindowTopmost(WinDef.HWND hwnd) {
        User32.INSTANCE.SetWindowPos(hwnd, ExtendedUser32.HWND_TOPMOST, 0, 0, 0, 0,
                WinUser.SWP_NOMOVE | WinUser.SWP_NOSIZE);
    }

    private record GridWindow(WinDef.HWND hwnd) {

    }

    private static int bestIndicatorX(int mouseX, int cursorWidth, int monitorLeft,
                                      int monitorRight) {
        mouseX = Math.min(monitorRight, Math.max(monitorLeft, mouseX));
        boolean isNearLeftEdge = mouseX <= (monitorLeft + indicatorEdgeThreshold);
        boolean isNearRightEdge = mouseX >= (monitorRight - indicatorEdgeThreshold);
        if (isNearRightEdge)
            return mouseX - indicatorSize;
        return mouseX + cursorWidth / 2;
    }

    private static int bestIndicatorY(int mouseY, int cursorHeight, int monitorTop,
                                      int monitorBottom) {
        mouseY = Math.min(monitorBottom, Math.max(monitorTop, mouseY));
        boolean isNearBottomEdge = mouseY >= (monitorBottom - indicatorEdgeThreshold);
        boolean isNearTopEdge = mouseY <= (monitorTop + indicatorEdgeThreshold);
        if (isNearBottomEdge)
            return mouseY - indicatorSize;
        return mouseY + cursorHeight / 2;
    }

    private static void createIndicatorWindow() {
        CursorPositionAndSize cursorPositionAndSize =
                WindowsMouse.cursorPositionAndSize();
        WinUser.MONITORINFO monitorInfo =
                WindowsMonitor.activeMonitorInfo(cursorPositionAndSize.position());
        indicatorWindowHwnd = createWindow("Indicator",
                bestIndicatorX(cursorPositionAndSize.position().x,
                        cursorPositionAndSize.width(), monitorInfo.rcMonitor.left,
                        monitorInfo.rcMonitor.right),
                bestIndicatorY(cursorPositionAndSize.position().y,
                        cursorPositionAndSize.height(), monitorInfo.rcMonitor.top,
                        monitorInfo.rcMonitor.bottom), indicatorSize, indicatorSize,
                WindowsOverlay::indicatorWindowCallback);
    }

    private static void createGridWindow(int x, int y, int width, int height) {
        WinDef.HWND hwnd = createWindow("Grid", x, y, width, height,
                WindowsOverlay::gridWindowCallback);
        gridWindow = new GridWindow(hwnd);
    }

    private static WinDef.HWND createWindow(String windowName, int windowX, int windowY,
                                            int windowWidth, int windowHeight,
                                            WinUser.WindowProc windowCallback) {
        WinUser.WNDCLASSEX wClass = new WinUser.WNDCLASSEX();
        wClass.hbrBackground = null;
        wClass.lpszClassName = "JMouseable" + windowName + "ClassName";
        wClass.lpfnWndProc = windowCallback;
        WinDef.ATOM registerClassExResult = User32.INSTANCE.RegisterClassEx(wClass);
        WinDef.HWND hwnd = User32.INSTANCE.CreateWindowEx(
                User32.WS_EX_TOPMOST | ExtendedUser32.WS_EX_TOOLWINDOW | ExtendedUser32.WS_EX_NOACTIVATE
                | ExtendedUser32.WS_EX_LAYERED | ExtendedUser32.WS_EX_TRANSPARENT,
                wClass.lpszClassName, "JMouseable" + windowName + "WindowName",
                WinUser.WS_POPUP, windowX, windowY, windowWidth, windowHeight, null, null,
                wClass.hInstance, null);
        User32.INSTANCE.SetLayeredWindowAttributes(hwnd, 0, (byte) 0,
                WinUser.LWA_COLORKEY);
        User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_SHOWNORMAL);
        return hwnd;
    }

    private static WinDef.LRESULT indicatorWindowCallback(WinDef.HWND hwnd, int uMsg,
                                                          WinDef.WPARAM wParam,
                                                          WinDef.LPARAM lParam) {
        switch (uMsg) {
            case WinUser.WM_PAINT:
                ExtendedUser32.PAINTSTRUCT ps = new ExtendedUser32.PAINTSTRUCT();
                WinDef.HDC hdc = ExtendedUser32.INSTANCE.BeginPaint(hwnd, ps);
                clearWindow(hdc, ps.rcPaint);
                if (showingIndicator) {
                    WinDef.HBRUSH hbrBackground = ExtendedGDI32.INSTANCE.CreateSolidBrush(
                            hexColorStringToInt(currentIndicatorHexColor));
                    ExtendedUser32.INSTANCE.FillRect(hdc, ps.rcPaint, hbrBackground);
                    GDI32.INSTANCE.DeleteObject(hbrBackground);
                }
                ExtendedUser32.INSTANCE.EndPaint(hwnd, ps);
                break;
        }
        return User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam);
    }

    private static WinDef.LRESULT gridWindowCallback(WinDef.HWND hwnd, int uMsg,
                                                     WinDef.WPARAM wParam,
                                                     WinDef.LPARAM lParam) {
        switch (uMsg) {
            case WinUser.WM_PAINT:
                ExtendedUser32.PAINTSTRUCT ps = new ExtendedUser32.PAINTSTRUCT();
                WinDef.HDC hdc = ExtendedUser32.INSTANCE.BeginPaint(hwnd, ps);
                // If not cleared, the previous drawings will be painted.
                clearWindow(hdc, ps.rcPaint);
                if (showingGrid)
                    paintGrid(hdc, ps.rcPaint);
                ExtendedUser32.INSTANCE.EndPaint(hwnd, ps);
                break;
        }
        return User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam);
    }

    private static void clearWindow(WinDef.HDC hdc, WinDef.RECT windowRect) {
        WinDef.HBRUSH hbrBackground = ExtendedGDI32.INSTANCE.CreateSolidBrush(0);
        ExtendedUser32.INSTANCE.FillRect(hdc, windowRect, hbrBackground);
        GDI32.INSTANCE.DeleteObject(hbrBackground);
    }

    private static void paintGrid(WinDef.HDC hdc, WinDef.RECT windowRect) {
        int snapRowCount = currentGrid.snapRowCount();
        int snapColumnCount = currentGrid.snapColumnCount();
        int cellWidth = currentGrid.width() / snapRowCount;
        int cellHeight = currentGrid.height() / snapColumnCount;
        int[] polyCounts = new int[snapRowCount + 1 + snapColumnCount + 1];
        WinDef.POINT[] points =
                (WinDef.POINT[]) new WinDef.POINT().toArray(polyCounts.length * 2);
        int lineThickness = currentGrid.lineThickness();
        // Vertical lines
        for (int lineIndex = 0; lineIndex <= snapColumnCount; lineIndex++) {
            int x = lineIndex == snapColumnCount ? windowRect.right :
                    lineIndex * cellWidth;
            if (x == windowRect.left)
                x += lineThickness / 2;
            else if (x == windowRect.right)
                x -= lineThickness / 2 + lineThickness % 2;
            points[2 * lineIndex].x = x;
            points[2 * lineIndex].y = 0;
            points[2 * lineIndex + 1].x = x;
            points[2 * lineIndex + 1].y = currentGrid.height();
            polyCounts[lineIndex] = 2;
        }
        // Horizontal lines
        int polyCountsOffset = snapColumnCount + 1;
        int pointsOffset = 2 * polyCountsOffset;
        for (int lineIndex = 0; lineIndex <= snapRowCount; lineIndex++) {
            int y = lineIndex == snapRowCount ? windowRect.bottom :
                    lineIndex * cellHeight;
            if (y == windowRect.top)
                y += lineThickness / 2;
            else if (y == windowRect.bottom)
                y -= lineThickness / 2 + lineThickness % 2;
            points[pointsOffset + 2 * lineIndex].x = 0;
            points[pointsOffset + 2 * lineIndex].y = y;
            points[pointsOffset + 2 * lineIndex + 1].x = currentGrid.width();
            points[pointsOffset + 2 * lineIndex + 1].y = y;
            polyCounts[polyCountsOffset + lineIndex] = 2;
        }
        String lineColor = currentGrid.lineHexColor();
        WinUser.HPEN gridPen =
                ExtendedGDI32.INSTANCE.CreatePen(ExtendedGDI32.PS_SOLID, lineThickness,
                        hexColorStringToInt(lineColor));
        if (gridPen == null)
            throw new IllegalStateException("Unable to create grid pen");
        WinNT.HANDLE oldPen = GDI32.INSTANCE.SelectObject(hdc, gridPen);
        boolean polyPolylineResult = ExtendedGDI32.INSTANCE.PolyPolyline(hdc, points, polyCounts,
                polyCounts.length);
        if (!polyPolylineResult) {
            int lastError = Native.getLastError();
            throw new IllegalStateException(
                    "PolyPolyline failed with error code " + lastError);
        }
        GDI32.INSTANCE.SelectObject(hdc, oldPen);
        GDI32.INSTANCE.DeleteObject(gridPen);
    }

    private static int hexColorStringToInt(String hexColor) {
        if (hexColor.startsWith("#"))
            hexColor = hexColor.substring(1);
        int colorInt = Integer.parseUnsignedInt(hexColor, 16);
        // In COLORREF, the order is 0x00BBGGRR, so we need to reorder the components.
        int red = (colorInt >> 16) & 0xFF;
        int green = (colorInt >> 8) & 0xFF;
        int blue = colorInt & 0xFF;
        return (blue << 16) | (green << 8) | red;
    }

    public static void setIndicatorColor(String hexColor) {
        Objects.requireNonNull(hexColor);
        if (showingIndicator && currentIndicatorHexColor != null &&
            currentIndicatorHexColor.equals(hexColor))
            return;
        currentIndicatorHexColor = hexColor;
        if (indicatorWindowHwnd == null)
            createIndicatorWindow();
        showingIndicator = true;
        requestWindowRepaint(indicatorWindowHwnd);
    }

    public static void hideIndicator() {
        if (!showingIndicator)
            return;
        showingIndicator = false;
        requestWindowRepaint(indicatorWindowHwnd);
    }

    public static void setGrid(Grid grid) {
        Objects.requireNonNull(grid);
        if (showingGrid && currentGrid != null && currentGrid.equals(grid))
            return;
        Grid oldGrid = currentGrid;
        currentGrid = grid;
        if (gridWindow == null)
            createGridWindow(currentGrid.x(), currentGrid.y(), currentGrid.width(),
                    currentGrid.height());
        else {
            if (grid.x() != oldGrid.x() || grid.y() != oldGrid.y() ||
                grid.width() != oldGrid.width() || grid.height() != oldGrid.height()) {
                User32.INSTANCE.SetWindowPos(gridWindow.hwnd(),
                        ExtendedUser32.HWND_TOPMOST, grid.x(), grid.y(), grid.width(),
                        grid.height(), 0);
            }
        }
        showingGrid = true;
        requestWindowRepaint(gridWindow.hwnd);
    }

    public static void hideGrid() {
        if (!showingGrid)
            return;
        showingGrid = false;
        requestWindowRepaint(gridWindow.hwnd);
    }

    private static void requestWindowRepaint(WinDef.HWND hwnd) {
        User32.INSTANCE.InvalidateRect(hwnd, null, true);
        User32.INSTANCE.UpdateWindow(hwnd);
    }

    static void mouseMoved(WinDef.POINT mousePosition) {
        WinUser.MONITORINFO monitorInfo = WindowsMonitor.activeMonitorInfo(mousePosition);
        CursorPositionAndSize cursorPositionAndSize =
                WindowsMouse.cursorPositionAndSize();
        User32.INSTANCE.MoveWindow(indicatorWindowHwnd,
                bestIndicatorX(mousePosition.x, cursorPositionAndSize.width(),
                        monitorInfo.rcMonitor.left, monitorInfo.rcMonitor.right),
                bestIndicatorY(mousePosition.y, cursorPositionAndSize.height(),
                        monitorInfo.rcMonitor.top, monitorInfo.rcMonitor.bottom),
                indicatorSize, indicatorSize, false);
    }

}