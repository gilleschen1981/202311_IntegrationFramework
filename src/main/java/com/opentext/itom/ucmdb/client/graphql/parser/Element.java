package com.opentext.itom.ucmdb.client.graphql.parser;

import com.opentext.itom.ucmdb.client.graphql.SingleTopoQuery;

import java.util.ArrayList;
import java.util.List;

public class Element {
    private String elementName;

    private boolean isRoot;
    private Filter filter;

    private Pagination pagination;

    private List<Element> subElementList;

    private List<String> attributes;

    public Element(String elementName, boolean isRoot) {
        this.elementName = elementName;
        this.isRoot = isRoot;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Element> getSubElementList() {
        if(subElementList == null){
            subElementList = new ArrayList<>();
        }
        return subElementList;
    }

    public void setSubElementList(List<Element> subElementList) {
        this.subElementList = subElementList;
    }

    public List<String> getAttributes() {
        if(attributes == null){
            attributes = new ArrayList<>();
        }
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public String generateCountPayload() {
        StringBuilder rlt = new StringBuilder();
        rlt.append(getElementName());

        if(filter != null){
            rlt.append(SingleTopoQuery.PAYLOAD_OPENING_PARENTHESE);
            rlt.append(SingleTopoQuery.PAYLOAD_KEYWORD_FILTER).append(SingleTopoQuery.PAYLOAD_COLON).append(filter.generatePayload());
            rlt.append(SingleTopoQuery.PAYLOAD_CLOSING_PARENTHESE);
        }
        rlt.append("\n").append(SingleTopoQuery.PAYLOAD_OPENING_BRACE).append("\n");
        rlt.append(SingleTopoQuery.PAYLOAD_KEYWORD_COUNT).append(SingleTopoQuery.PAYLOAD_OPENING_PARENTHESE);
        rlt.append(SingleTopoQuery.PAYLOAD_KEYWORD_FILED).append(SingleTopoQuery.PAYLOAD_COLON).append(SingleTopoQuery.PAYLOAD_ATTRIBUTE_GLOBALID);
        rlt.append(SingleTopoQuery.PAYLOAD_CLOSING_PARENTHESE).append("\n");
        rlt.append(SingleTopoQuery.PAYLOAD_CLOSING_BRACE).append("\n");
        return rlt.toString();
    }

    public String generateBatchQueryPayload() {
        StringBuilder rlt = new StringBuilder();
        rlt.append(elementName);
        if(filter != null || pagination != null){
            rlt.append(SingleTopoQuery.PAYLOAD_OPENING_PARENTHESE);
            if(pagination != null){
                rlt.append(getPagination().generatePayload()).append(SingleTopoQuery.PAYLOAD_COMMA);
            }
            if(filter != null){
                rlt.append(SingleTopoQuery.PAYLOAD_KEYWORD_FILTER).append(SingleTopoQuery.PAYLOAD_COLON).append(filter.generatePayload());
            }
            rlt.append(SingleTopoQuery.PAYLOAD_CLOSING_PARENTHESE);
        }
        rlt.append("\n").append(SingleTopoQuery.PAYLOAD_OPENING_BRACE).append("\n");
        for(String attr : attributes){
            rlt.append(attr).append("\n");
        }
        for(Element subElement : getSubElementList()){
            rlt.append(subElement.generateBatchQueryPayload());
        }
        rlt.append(SingleTopoQuery.PAYLOAD_CLOSING_BRACE).append("\n");
        return rlt.toString();
    }
}
