package io.barrowisp.craftastrophe.util;

public class StringUtils {

    /**
     * Encapsulate the string argument with quotation marks
     * @param single whether to use single instead of double quotation marks
     */
    public static String quote(String s, boolean single) {
        char quote = single ? '\'' : '\"';
        return quote + s + quote;
    }

    /**
     * <p>Encapsulate the string representation of an object with single
     * or double quotation marks depending on the object type.</p>
     * <ul>
     *     <li>String - double quotation marks</li>
     *     <li>Number - no quotation marks</li>
     *     <li>Other - single quotation marks</li>
     * </ul>
     */
    public static String smartQuote(Object s) {
        String value = String.valueOf(s);
        return s instanceof Number ? value : quote(value, !(s instanceof String));
    }
}
