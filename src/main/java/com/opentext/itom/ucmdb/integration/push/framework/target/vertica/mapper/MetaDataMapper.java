package com.opentext.itom.ucmdb.integration.push.framework.target.vertica.mapper;

import com.opentext.itom.ucmdb.integration.push.repo.model.TableMeta;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface MetaDataMapper {
    void createTable();

    void insertMetaData(Map<String, Object> objects);

    void updateMetaData(Map<String, Object> objects);

    boolean checkExist(@Param("key") String key);

    void dropTableIfExist();

    @MapKey("key")
    Map<String, Object> getAll();
}
