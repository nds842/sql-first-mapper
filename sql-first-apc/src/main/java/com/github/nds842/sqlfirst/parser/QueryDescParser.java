package com.github.nds842.sqlfirst.parser;

import com.github.nds842.sqlfirst.base.QueryDesc;

public interface QueryDescParser {

    QueryDesc parse(String queryText);

}
