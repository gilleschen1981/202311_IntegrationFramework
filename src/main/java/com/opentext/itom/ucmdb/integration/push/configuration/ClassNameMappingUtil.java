package com.opentext.itom.ucmdb.integration.push.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ClassNameMappingUtil {
    private static final Logger log = LoggerFactory.getLogger(ClassNameMappingUtil.class);
    public static final String CLASS_MAPPING_FILE_PATH = "data/classmapping.json";
    ClassMapping classMapping;

    public void init(){
        File mappingFile = new File(CLASS_MAPPING_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();
        try {
            classMapping = mapper.readValue(mappingFile, ClassMapping.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getGraphQLInnerClassName(String classname){
        if(classMapping == null){
            init();
        }
        String rlt = classMapping.getClassnameMapping().get(classname);
        if(rlt == null){
            log.error("[GraphQLMapping]Mapping missing for class: " + classname);
        }
        return rlt;
    }

    public String getGraphQLRootClassName(String classname){
        return classname + "s";
    }

}
