package com.github.nds842.sqlfirst.base;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MiscUtils {

    public static final String UTF_8 = "utf-8";

    private MiscUtils() {
    }

    public static String prepareNameString(String nameString) {
        nameString = underscores(nameString).replaceAll("\\W", "_").toLowerCase();
        nameString = Arrays.stream(nameString.split("_")).map(StringUtils::capitalize).collect(Collectors.joining());
        nameString = StringUtils.uncapitalize(nameString);
        return nameString;
    }

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
        return result.toString().toUpperCase();
    }


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

    public static String getLastWordAfterDot(String x) {
        return x.contains(".") ? StringUtils.substringAfterLast(x, ".") : x;
    }

    public static String escape(String query) {
        return "\"" + query.replace("\"", "\\\"").replace("\n","\"+\n        \" ") + "\"";
    }
}