package com.opentext.itom.ucmdb.integration.push.repo.model;

import java.util.ArrayList;
import java.util.List;

public class TableMeta {
    public static final String TABLE_PREFIX = "cmdb_";
    public static final String TABLE_SURFIX = "";
    public final static String TABLE_SEPARATOR = "_";

    private final String className;
    private List<TableColumnMeta> columnList;

    public TableMeta(String tableName) {
        this.className = tableName;
        columnList = new ArrayList<>();
    }

    public String getClassName() {
        return className;
    }

    public String getTableName() {
        return TABLE_PREFIX + className + TABLE_SURFIX;
    }

    public List<TableColumnMeta> getColumnList() {
        if(columnList == null){
            columnList = new ArrayList<>();
        }
        return columnList;
    }

    public List<String> getColumnListForTable() {
        List<String> rlt = new ArrayList<>();
        for(TableColumnMeta columnMeta : getColumnList()){
            rlt.add(ModelConverter.convertColumnMeta2TableType(columnMeta));
        }
        return rlt;
    }

    public static String generateRelationTableName(String className, String end1Class, String end2Class) {
        return className + TABLE_SEPARATOR + end1Class + TABLE_SEPARATOR + end2Class;
    }
}
