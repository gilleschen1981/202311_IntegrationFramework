package com.opentext.itom.ucmdb.integration.push.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ClassmodelConfigRepo {
    private static final Logger log = LoggerFactory.getLogger(ClassmodelConfigRepo.class);
    public final static String CONFIG_FILE_PATH = "data/classmodel.json";
    public final static String RELATION_SEPARATOR = "\\$";

    @Value("${classmodel.configuration.override}")
    private boolean override;

    private ClassmodelConfig classmodelConf;

    public void init(){
        // initialize, load local configuration file if exist
        File configFile = new File(CONFIG_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();
        try {
            classmodelConf = mapper.readValue(configFile, ClassmodelConfig.class);
            processOObAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("[Init]Load configuration successfully. SimpleTopoCount: " + classmodelConf.getSimpleTopoList().size());
    }

    private void processOObAttributes() {
        for(SimpleTopology simpleTopology : classmodelConf.getSimpleTopoList()){
            simpleTopology.loopProcessOObAttributes();
        }
    }

    public boolean isOverride() {
        return override;
    }

    public ClassmodelConfig getClassmodelConf() {
        return classmodelConf;
    }

//    public Map<String, ClassConf> getClassMap() {
//        Map<String, ClassConf> rlt = new HashMap<String, ClassConf>();
//            for(String classname : classmodelConf.getClassTypeMap().keySet()){
//                ClassConf cl = classmodelConf.getClassTypeMap().get(classname);
//                rlt.put(classname, cl);
//                for(String relationname : cl.getRelated().keySet()){
//                    for(String childname : cl.getRelated().get(relationname).keySet()){
//                        if(!rlt.containsKey(childname)){
//                            rlt.put(childname, cl.getRelated().get(relationname).get(childname));
//                        }
//                    }
//                }
//            }
//        return rlt;
//    }

    public String getRelationKey(String relationname, String parentname, String childname) {
        return relationname + RELATION_SEPARATOR + parentname + RELATION_SEPARATOR + childname;
    }
}
