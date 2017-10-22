package com.github.nds842.sqlfirst.base;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility class
 */
public class MiscUtils {

    public static final String UTF_8 = "utf-8";

    private MiscUtils() {
    }

    /**
     * Prepare camelcase name
     *
     * @param nameString string to convert to camel case
     * @return camelcase string
     */
    public static String prepareNameString(String nameString) {
        nameString = underscores(nameString).replaceAll("\\W", "_").toLowerCase();
        nameString = Arrays.stream(nameString.split("_")).map(StringUtils::capitalize).collect(Collectors.joining());
        nameString = StringUtils.uncapitalize(nameString);
        return nameString;
    }

    /**
     * Transform input string to underscores, i.e. some.strIng will be converted to SOME_STR_ING
     *
     * @param input string to convert
     * @return underscores string
     */
    public static String underscores(String input) {
        input = input.replace(".", "_");
        if (input.toUpperCase().equals(input)) {
            return input;
        }
        String lowerCase = input.toLowerCase();
        StringBuilder result = new StringBuilder();
        boolean isUpper = false;
        for (int i = 0; i < lowerCase.length(); i++) {
            char charAt = input.charAt(i);
            if (lowerCase.charAt(i) != charAt && i > 0) {
                if (!isUpper) {
                    result.append("_");
                }
                isUpper = true;
            } else {
                isUpper = false;
            }
            result.append(charAt);
        }
        return result.toString().replace("__", "_").toUpperCase();
    }


    /**
     * Create Javadoc from input string
     *
     * @param query   string to create javadoc from
     * @param padding number of space symbols to pad from the left
     * @return prepared javadoc string
     */
    public static String prepareJavadoc(String query, int padding) {
        String pad = StringUtils.rightPad("", padding);
        StringBuilder sb = new StringBuilder(pad + "/**" + "\n");
        String[] spl = query.split("\n");
        for (String string : spl) {
            string = string.replace("*/", "* /").replaceAll("\t", "    ");
            sb.append(pad).append(" * ").append(string).append("\n");
        }
        sb.append(pad).append(" */");
        return sb.toString();

    }

    /**
     * Split by dot and get last word
     *
     * @param stringWithDots string to get last word from
     * @return word after last dot
     */
    public static String getLastWordAfterDot(String stringWithDots) {
        if (StringUtils.isBlank(stringWithDots)){
            return stringWithDots;
        }
        return stringWithDots.contains(".") ? StringUtils.substringAfterLast(stringWithDots, ".") : stringWithDots;
    }
}