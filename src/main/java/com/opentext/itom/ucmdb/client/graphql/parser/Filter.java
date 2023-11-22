package com.opentext.itom.ucmdb.client.graphql.parser;

import com.opentext.itom.ucmdb.client.graphql.SingleTopoQuery;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private boolean compositeFilter;

    private String logicOperator;

    private List<Filter> subFilters;

    private String attributeName;

    private String operator;

    private String value;

    public Filter(String attributeName, String operator, String value) {
        this.attributeName = attributeName;
        this.operator = operator;
        this.value = value;
        compositeFilter = false;
        logicOperator = "";
        subFilters = null;
    }

    public Filter() {
        compositeFilter = true;
    }

    public Filter and(List<Filter> filters) {
        Filter rlt = new Filter();
        rlt.setLogicOperator(SingleTopoQuery.PAYLOAD_KEYWORD_AND);
        rlt.getSubFilters().add(this);
        rlt.getSubFilters().addAll(filters);
        return rlt;
    }

    public List<Filter> getSubFilters() {
        if(subFilters == null){
            subFilters = new ArrayList<Filter>();
        }
        return subFilters;
    }

    public void setLogicOperator(String logicOperator) {
        this.logicOperator = logicOperator;
    }

    public String generatePayload() {
        String rlt = null;
        if(compositeFilter){
            rlt = generateCompositeFilterPayload();
        } else{
            rlt = generateSimpleFilterPayload();
        }
        return rlt;
    }

    private String generateSimpleFilterPayload() {
        String rlt = SingleTopoQuery.PAYLOAD_OPENING_BRACE + attributeName + SingleTopoQuery.PAYLOAD_COLON
                + SingleTopoQuery.PAYLOAD_OPENING_BRACE + operator + SingleTopoQuery.PAYLOAD_COLON
                + value + SingleTopoQuery.PAYLOAD_CLOSING_BRACE + SingleTopoQuery.PAYLOAD_CLOSING_BRACE;
        return rlt;
    }

    private String generateCompositeFilterPayload() {
        StringBuilder rlt = new StringBuilder();
        rlt.append(SingleTopoQuery.PAYLOAD_OPENING_BRACE).append(logicOperator)
                .append(SingleTopoQuery.PAYLOAD_COLON).append(SingleTopoQuery.PAYLOAD_OPENING_BRACKET);
        for(Filter filter : subFilters){
            rlt.append(filter.generatePayload());
        }
        rlt.append(SingleTopoQuery.PAYLOAD_CLOSING_BRACKET).append(SingleTopoQuery.PAYLOAD_CLOSING_BRACE);
        return rlt.toString();
    }
}
