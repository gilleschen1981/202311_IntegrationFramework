package com.opentext.itom.ucmdb.integration.push.repo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableMeta implements Serializable {
    public static final String TABLE_PREFIX = "cmdb_";
    public static final String TABLE_SURFIX = "";
    public final static String TABLE_SEPARATOR = "_";
    public final static String ATTRIBUTE_GLOBAL_ID = "global_id";
    public final static String ATTRIBUTE_LAST_ACCESS_TIME = "root_lastaccesstime";
    public final static List<TableColumnMeta> MUST_COLUMNS= new ArrayList<TableColumnMeta>() {{
        add(new TableColumnMeta(ATTRIBUTE_GLOBAL_ID, "VARCHAR", 32));
        add(new TableColumnMeta(ATTRIBUTE_LAST_ACCESS_TIME, "DATETIME", 0));
    }};

    private final String className;
    private Set<TableColumnMeta> columns;

    public TableMeta(String tableName) {
        this.className = tableName;
        columns = new HashSet<>();
    }

    public String getClassName() {
        return className;
    }

    public String getTableName() {
        return TABLE_PREFIX + className + TABLE_SURFIX;
    }

    public Set<TableColumnMeta> getColumns() {
        if(columns == null){
            columns = new HashSet<>();
        }
        return columns;
    }

    public List<String> getColumnListForTable() {
        List<String> rlt = new ArrayList<>();
        for(TableColumnMeta columnMeta : getColumns()){
            rlt.add(ModelConverter.convertColumnMeta2TableType(columnMeta));
        }
        return rlt;
    }

    public static String generateRelationTableName(String className, String end1Class, String end2Class) {
        return className + TABLE_SEPARATOR + end1Class + TABLE_SEPARATOR + end2Class;
    }

    public TableColumnMeta getColumnMetaByName(String columnName) {
        if(columnName == null || columnName.isEmpty()){
            return null;
        }
        for(TableColumnMeta tableColumnMeta : getColumns()){
            if(columnName.equals(tableColumnMeta.getColumnName())){
                return  tableColumnMeta;
            }
        }
        return null;
    }
}
