package me.hyfe.queue.text;

import me.hyfe.queue.text.replacer.Replacer;
import me.hyfe.queue.text.replacer.Subject;

import java.util.regex.Pattern;

public class Text {
    private static final char SECTION_CHAR = '\u00A7';
    private static final char AMPERSAND_CHAR = '&';
    private static final String COLOR_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + SECTION_CHAR + "[0-9A-FK-OR]");

    public static String colorize(String string, Replacer replacer) {
        return string == null ? null : colorize(replacer == null ? string : replacer.apply(new Subject()).applyTo(string));
    }

    public static String colorize(String string) {
        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length - 1; i++) {
            if (charArray[i] == Text.AMPERSAND_CHAR && COLOR_CODES.indexOf(charArray[i + 1]) > -1) {
                charArray[i] = Text.SECTION_CHAR;
                charArray[i + 1] = Character.toLowerCase(charArray[i + 1]);
            }
        }
        return new String(charArray);
    }

    private static String strip(String text) {
        return text == null ? null : STRIP_COLOR_PATTERN.matcher(text).replaceAll("");
    }
}