package com.github.nds842.sqlfirst.parser;

import com.github.nds842.sqlfirst.base.ParamDesc;
import com.github.nds842.sqlfirst.base.QueryDesc;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SuffixParser implements QueryDescParser {

    //TODO think of pattern (?!\\B'[^']*)(:\\w+)(?![^']*'\\B)
    private Pattern requestRegexp = Pattern.compile("[:{$]{1}\\b((\\w+)__\\w)\\b");
    private Pattern responseRegexp = Pattern.compile("(?<![:{$])\\b((\\w+)__\\w)\\b");

    @Override
    public QueryDesc parse(String queryText) {

        QueryDesc queryDesc = new QueryDesc();
        queryDesc.setQuery(queryText);
        Set<ParamDesc> reqList = new LinkedHashSet<>();
        Set<ParamDesc> resList = new LinkedHashSet<>();

        Matcher m = requestRegexp.matcher(queryText);

        int n = 0;
        while (m.find()) {
            String s = m.group(1);
            reqList.add(new ParamDesc(n, getType(s), extractName(s), s));
            n++;
        }
        m = responseRegexp.matcher(queryText);
        n = 0;
        while (m.find()) {
            String s = m.group(1);
            resList.add(new ParamDesc(n, getType(s), extractName(s), s));
            n++;
        }

        List<ParamDesc> requestParamList = new ArrayList<>(reqList);
        List<ParamDesc> responseParamList = new ArrayList<>(resList);

        Collections.sort(requestParamList);
        Collections.sort(responseParamList);

        queryDesc.setRequestParamList(requestParamList.isEmpty() ? null : requestParamList);
        queryDesc.setResponseParamList(responseParamList.isEmpty() ? null : responseParamList);

        return queryDesc;
    }

    private String extractName(String s) {
        if (StringUtils.isBlank(s)) {
            throw new RuntimeException("Name must not be blank");
        }
        return StringUtils.substring(s, 0, s.length() - 3);
    }

    private Class<?> getType(String s) {
        String typeIdentifier = StringUtils.substring(s, s.length() - 1);
        Class<?> clazz;
        switch (typeIdentifier) {
            case "l":
                clazz = Long.class;
                break;
            case "s":
                clazz = String.class;
                break;
            case "d":
                clazz = Date.class;
                break;
            case "i":
                clazz = Integer.class;
                break;
            default:
                throw new RuntimeException("unknown type identifier: " + typeIdentifier);
        }
        return clazz;
    }
}
