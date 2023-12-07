package jmouseable.jmouseable;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;

import java.util.HashSet;
import java.util.Set;

public class WindowsScreen {
    public static Set<Screen> findScreens() {
        Set<Screen> screens = new HashSet<>();
        User32.INSTANCE.EnumDisplayMonitors(null, null, new WinUser.MONITORENUMPROC() {
            @Override
            public int apply(WinUser.HMONITOR hMonitor, WinDef.HDC hdcMonitor,
                             WinDef.RECT lprcMonitor, WinDef.LPARAM dwData) {
                screens.add(new Screen(new Rectangle(lprcMonitor.left, lprcMonitor.top,
                        lprcMonitor.right - lprcMonitor.left,
                        lprcMonitor.bottom - lprcMonitor.top), findScreenDpi(hMonitor)));
                return 1;
            }
        }, null);
        return screens;
    }

    public static Screen findActiveScreen(
            WinDef.POINT mousePosition) {
        WinUser.HMONITOR hMonitor = User32.INSTANCE.MonitorFromPoint(
                new WinDef.POINT.ByValue(mousePosition.getPointer()),
                WinUser.MONITOR_DEFAULTTONEAREST);
        WinUser.MONITORINFO monitorInfo = new WinUser.MONITORINFO();
        User32.INSTANCE.GetMonitorInfo(hMonitor, monitorInfo);
        int dpi = findScreenDpi(hMonitor);
        return new Screen(
                new Rectangle(monitorInfo.rcMonitor.left, monitorInfo.rcMonitor.top,
                        monitorInfo.rcMonitor.right - monitorInfo.rcMonitor.left,
                        monitorInfo.rcMonitor.bottom - monitorInfo.rcMonitor.top), dpi);
    }

    private static int findScreenDpi(WinUser.HMONITOR hMonitor) {
        IntByReference dpiX = new IntByReference();
        IntByReference dpiY = new IntByReference();
        Shcore.INSTANCE.GetDpiForMonitor(hMonitor,
                new Shcore.MONITOR_DPI_TYPE(Shcore.MONITOR_DPI_TYPE.MDT_EFFECTIVE_DPI),
                dpiX, dpiY);
        return dpiX.getValue();
    }

}