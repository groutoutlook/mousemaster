package jmouseable.jmouseable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record KeyboardLayout(String name, String identifier) {

    // https://learn.microsoft.com/en-us/windows-hardware/manufacture/desktop/windows-language-pack-default-values?view=windows-11
    private static final Set<KeyboardLayout> keyboardLayouts =
            Set.of(
                new KeyboardLayout("ADLaM", "00140C00"),
                new KeyboardLayout("Albanian", "0000041C"),
                new KeyboardLayout("Arabic (101)", "00000401"),
                new KeyboardLayout("Arabic (102)", "00010401"),
                new KeyboardLayout("Arabic (102) AZERTY", "00020401"),
                new KeyboardLayout("Armenian Eastern (Legacy)", "0000042B"),
                new KeyboardLayout("Armenian Phonetic", "0002042B"),
                new KeyboardLayout("Armenian Typewriter", "0003042B"),
                new KeyboardLayout("Armenian Western (Legacy)", "0001042B"),
                new KeyboardLayout("Assamese - INSCRIPT", "0000044D"),
                new KeyboardLayout("Azerbaijani (Standard)", "0001042C"),
                new KeyboardLayout("Azerbaijani Cyrillic", "0000082C"),
                new KeyboardLayout("Azerbaijani Latin", "0000042C"),
                new KeyboardLayout("Bangla", "00000445"),
                new KeyboardLayout("Bangla - INSCRIPT", "00020445"),
                new KeyboardLayout("Bangla - INSCRIPT (Legacy)", "00010445"),
                new KeyboardLayout("Bashkir", "0000046D"),
                new KeyboardLayout("Belarusian", "00000423"),
                new KeyboardLayout("Belgian (Comma)", "0001080C"),
                new KeyboardLayout("Belgian (Period)", "00000813"),
                new KeyboardLayout("Belgian French", "0000080C"),
                new KeyboardLayout("Bosnian (Cyrillic)", "0000201A"),
                new KeyboardLayout("Buginese", "000B0C00"),
                new KeyboardLayout("Bulgarian", "00030402"),
                new KeyboardLayout("Bulgarian (Latin)", "00010402"),
                new KeyboardLayout("Bulgarian (Phonetic Traditional)", "00040402"),
                new KeyboardLayout("Bulgarian (Phonetic)", "00020402"),
                new KeyboardLayout("Bulgarian (Typewriter)", "00000402"),
                new KeyboardLayout("Canadian French", "00001009"),
                new KeyboardLayout("Canadian French (Legacy)", "00000C0C"),
                new KeyboardLayout("Canadian Multilingual Standard", "00011009"),
                new KeyboardLayout("Central Atlas Tamazight", "0000085F"),
                new KeyboardLayout("Central Kurdish", "00000492"),
                new KeyboardLayout("Cherokee Nation", "0000045C"),
                new KeyboardLayout("Cherokee Phonetic", "0001045C"),
                new KeyboardLayout("Chinese (Simplified) - US", "00000804"),
                new KeyboardLayout("Chinese (Simplified, Singapore) - US", "00001004"),
                new KeyboardLayout("Chinese (Traditional) - US", "00000404"),
                new KeyboardLayout("Chinese (Traditional, Hong Kong S.A.R.) - US", "00000C04"),
                new KeyboardLayout("Chinese (Traditional, Macao S.A.R.) - US", "00001404"),
                new KeyboardLayout("Czech", "00000405"),
                new KeyboardLayout("Czech (QWERTY)", "00010405"),
                new KeyboardLayout("Czech Programmers", "00020405"),
                new KeyboardLayout("Danish", "00000406"),
                new KeyboardLayout("Devanagari - INSCRIPT", "00000439"),
                new KeyboardLayout("Divehi Phonetic", "00000465"),
                new KeyboardLayout("Divehi Typewriter", "00010465"),
                new KeyboardLayout("Dutch", "00000413"),
                new KeyboardLayout("Dzongkha", "00000C51"),
                new KeyboardLayout("English (India)", "00004009"),
                new KeyboardLayout("Estonian", "00000425"),
                new KeyboardLayout("Faeroese", "00000438"),
                new KeyboardLayout("Finnish", "0000040B"),
                new KeyboardLayout("Finnish with Sami", "0001083B"),
                new KeyboardLayout("French", "0000040C"),
                new KeyboardLayout("Futhark", "00120C00"),
                new KeyboardLayout("Georgian (Ergonomic)", "00020437"),
                new KeyboardLayout("Georgian (Legacy)", "00000437"),
                new KeyboardLayout("Georgian (MES)", "00030437"),
                new KeyboardLayout("Georgian (Old Alphabets)", "00040437"),
                new KeyboardLayout("Georgian (QWERTY)", "00010437"),
                new KeyboardLayout("German", "00000407"),
                new KeyboardLayout("German (IBM)", "00010407"),
                new KeyboardLayout("Gothic", "000C0C00"),
                new KeyboardLayout("Greek", "00000408"),
                new KeyboardLayout("Greek (220)", "00010408"),
                new KeyboardLayout("Greek (220) Latin", "00030408"),
                new KeyboardLayout("Greek (319)", "00020408"),
                new KeyboardLayout("Greek (319) Latin", "00040408"),
                new KeyboardLayout("Greek Latin", "00050408"),
                new KeyboardLayout("Greek Polytonic", "00060408"),
                new KeyboardLayout("Greenlandic", "0000046F"),
                new KeyboardLayout("Guarani", "00000474"),
                new KeyboardLayout("Gujarati", "00000447"),
                new KeyboardLayout("Hausa", "00000468"),
                new KeyboardLayout("Hawaiian", "00000475"),
                new KeyboardLayout("Hebrew", "0000040D"),
                new KeyboardLayout("Hebrew (Standard)", "0002040D"),
                new KeyboardLayout("Hindi Traditional", "00010439"),
                new KeyboardLayout("Hungarian", "0000040E"),
                new KeyboardLayout("Hungarian 101-key", "0001040E"),
                new KeyboardLayout("Icelandic", "0000040F"),
                new KeyboardLayout("Igbo", "00000470"),
                new KeyboardLayout("Inuktitut - Latin", "0000085D"),
                new KeyboardLayout("Inuktitut - Naqittaut", "0001045D"),
                new KeyboardLayout("Irish", "00001809"),
                new KeyboardLayout("Italian", "00000410"),
                new KeyboardLayout("Italian (142)", "00010410"),
                new KeyboardLayout("Japanese", "00000411"),
                new KeyboardLayout("Javanese", "00110C00"),
                new KeyboardLayout("Kannada", "0000044B"),
                new KeyboardLayout("Kazakh", "0000043F"),
                new KeyboardLayout("Khmer", "00000453"),
                new KeyboardLayout("Khmer (NIDA)", "00010453"),
                new KeyboardLayout("Korean", "00000412"),
                new KeyboardLayout("Kyrgyz Cyrillic", "00000440"),
                new KeyboardLayout("Lao", "00000454"),
                new KeyboardLayout("Latin American", "0000080A"),
                new KeyboardLayout("Latvian", "00000426"),
                new KeyboardLayout("Latvian (QWERTY)", "00010426"),
                new KeyboardLayout("Latvian (Standard)", "00020426"),
                new KeyboardLayout("Lisu (Basic)", "00070C00"),
                new KeyboardLayout("Lisu (Standard)", "00080C00"),
                new KeyboardLayout("Lithuanian", "00010427"),
                new KeyboardLayout("Lithuanian IBM", "00000427"),
                new KeyboardLayout("Lithuanian Standard", "00020427"),
                new KeyboardLayout("Luxembourgish", "0000046E"),
                new KeyboardLayout("Macedonian", "0000042F"),
                new KeyboardLayout("Macedonian - Standard", "0001042F"),
                new KeyboardLayout("Malayalam", "0000044C"),
                new KeyboardLayout("Maltese 47-Key", "0000043A"),
                new KeyboardLayout("Maltese 48-Key", "0001043A"),
                new KeyboardLayout("Maori", "00000481"),
                new KeyboardLayout("Marathi", "0000044E"),
                new KeyboardLayout("Mongolian (Mongolian Script)", "00000850"),
                new KeyboardLayout("Mongolian Cyrillic", "00000450"),
                new KeyboardLayout("Myanmar (Phonetic order)", "00010C00"),
                new KeyboardLayout("Myanmar (Visual order)", "00130C00"),
                new KeyboardLayout("NZ Aotearoa", "00001409"),
                new KeyboardLayout("Nepali", "00000461"),
                new KeyboardLayout("New Tai Lue", "00020C00"),
                new KeyboardLayout("Norwegian", "00000414"),
                new KeyboardLayout("Norwegian with Sami", "0000043B"),
                new KeyboardLayout("N’Ko", "00090C00"),
                new KeyboardLayout("Odia", "00000448"),
                new KeyboardLayout("Ogham", "00040C00"),
                new KeyboardLayout("Ol Chiki", "000D0C00"),
                new KeyboardLayout("Old Italic", "000F0C00"),
                new KeyboardLayout("Osage", "00150C00"),
                new KeyboardLayout("Osmanya", "000E0C00"),
                new KeyboardLayout("Pashto (Afghanistan)", "00000463"),
                new KeyboardLayout("Persian", "00000429"),
                new KeyboardLayout("Persian (Standard)", "00050429"),
                new KeyboardLayout("Phags-pa", "000A0C00"),
                new KeyboardLayout("Polish (214)", "00010415"),
                new KeyboardLayout("Polish (Programmers)", "00000415"),
                new KeyboardLayout("Portuguese", "00000816"),
                new KeyboardLayout("Portuguese (Brazil ABNT)", "00000416"),
                new KeyboardLayout("Portuguese (Brazil ABNT2)", "00010416"),
                new KeyboardLayout("Punjabi", "00000446"),
                new KeyboardLayout("Romanian (Legacy)", "00000418"),
                new KeyboardLayout("Romanian (Programmers)", "00020418"),
                new KeyboardLayout("Romanian (Standard)", "00010418"),
                new KeyboardLayout("Russian", "00000419"),
                new KeyboardLayout("Russian (Typewriter)", "00010419"),
                new KeyboardLayout("Russian - Mnemonic", "00020419"),
                new KeyboardLayout("Sakha", "00000485"),
                new KeyboardLayout("Sami Extended Finland-Sweden", "0002083B"),
                new KeyboardLayout("Sami Extended Norway", "0001043B"),
                new KeyboardLayout("Scottish Gaelic", "00011809"),
                new KeyboardLayout("Serbian (Cyrillic)", "00000C1A"),
                new KeyboardLayout("Serbian (Latin)", "0000081A"),
                new KeyboardLayout("Sesotho sa Leboa", "0000046C"),
                new KeyboardLayout("Setswana", "00000432"),
                new KeyboardLayout("Sinhala", "0000045B"),
                new KeyboardLayout("Sinhala - Wij 9", "0001045B"),
                new KeyboardLayout("Slovak", "0000041B"),
                new KeyboardLayout("Slovak (QWERTY)", "0001041B"),
                new KeyboardLayout("Slovenian", "00000424"),
                new KeyboardLayout("Sora", "00100C00"),
                new KeyboardLayout("Sorbian Extended", "0001042E"),
                new KeyboardLayout("Sorbian Standard", "0002042E"),
                new KeyboardLayout("Sorbian Standard (Legacy)", "0000042E"),
                new KeyboardLayout("Spanish", "0000040A"),
                new KeyboardLayout("Spanish Variation", "0001040A"),
                new KeyboardLayout("Standard", "0000041A"),
                new KeyboardLayout("Swedish", "0000041D"),
                new KeyboardLayout("Swedish with Sami", "0000083B"),
                new KeyboardLayout("Swiss French", "0000100C"),
                new KeyboardLayout("Swiss German", "00000807"),
                new KeyboardLayout("Syriac", "0000045A"),
                new KeyboardLayout("Syriac Phonetic", "0001045A"),
                new KeyboardLayout("Tai Le", "00030C00"),
                new KeyboardLayout("Tajik", "00000428"),
                new KeyboardLayout("Tamil", "00000449"),
                new KeyboardLayout("Tamil 99", "00020449"),
                new KeyboardLayout("Tamil Anjal", "00030449"),
                new KeyboardLayout("Tatar", "00010444"),
                new KeyboardLayout("Tatar (Legacy)", "00000444"),
                new KeyboardLayout("Telugu", "0000044A"),
                new KeyboardLayout("Thai Kedmanee", "0000041E"),
                new KeyboardLayout("Thai Kedmanee (non-ShiftLock)", "0002041E"),
                new KeyboardLayout("Thai Pattachote", "0001041E"),
                new KeyboardLayout("Thai Pattachote (non-ShiftLock)", "0003041E"),
                new KeyboardLayout("Tibetan (PRC)", "00000451"),
                new KeyboardLayout("Tibetan (PRC) - Updated", "00010451"),
                new KeyboardLayout("Tifinagh (Basic)", "0000105F"),
                new KeyboardLayout("Tifinagh (Extended)", "0001105F"),
                new KeyboardLayout("Traditional Mongolian (Standard)", "00010850"),
                new KeyboardLayout("Turkish F", "0001041F"),
                new KeyboardLayout("Turkish Q", "0000041F"),
                new KeyboardLayout("Turkmen", "00000442"),
                new KeyboardLayout("US", "00000409"),
                new KeyboardLayout("US English Table for IBM Arabic 238_L", "00050409"),
                new KeyboardLayout("Ukrainian", "00000422"),
                new KeyboardLayout("Ukrainian (Enhanced)", "00020422"),
                new KeyboardLayout("United Kingdom", "00000809"),
                new KeyboardLayout("United Kingdom Extended", "00000452"),
                new KeyboardLayout("United States-Dvorak", "00010409"),
                new KeyboardLayout("United States-Dvorak for left hand", "00030409"),
                new KeyboardLayout("United States-Dvorak for right hand", "00040409"),
                new KeyboardLayout("United States-International", "00020409"),
                new KeyboardLayout("Urdu", "00000420"),
                new KeyboardLayout("Uyghur", "00010480"),
                new KeyboardLayout("Uyghur (Legacy)", "00000480"),
                new KeyboardLayout("Uzbek Cyrillic", "00000843"),
                new KeyboardLayout("Vietnamese", "0000042A"),
                new KeyboardLayout("Wolof", "00000488"),
                new KeyboardLayout("Yoruba", "0000046A")
            );

    public static final Map<String, KeyboardLayout> keyboardLayoutByName =
            new HashMap<>();
    public static final Map<String, KeyboardLayout> keyboardLayoutByIdentifier =
            new HashMap<>();

    static {
        for (KeyboardLayout keyboardLayout : keyboardLayouts) {
            keyboardLayoutByName.put(keyboardLayout.name, keyboardLayout);
            keyboardLayoutByIdentifier.put(keyboardLayout.identifier, keyboardLayout);
        }
    }

}