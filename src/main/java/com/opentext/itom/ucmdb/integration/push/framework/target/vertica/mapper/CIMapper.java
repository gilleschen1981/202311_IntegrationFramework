package com.opentext.itom.ucmdb.integration.push.framework.target.vertica.mapper;

import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CIMapper {
    void createNewTable(@Param("tableName") String tableName, @Param("columns") List<String> columns);

    int countTable(@Param("tableName") String tableName);

    void truncateTable(@Param("tableName") String tableName);

    void deleteTable(@Param("tableName") String tableName);

    void insertCI(@Param("tableName") String tableName, @Param("item")Map<String, String> valueMap);

    boolean ciExist(@Param("tableName") String tableName, @Param("global_id")String globalId, @Param("last_access_time")String lastAccessTime);
}
