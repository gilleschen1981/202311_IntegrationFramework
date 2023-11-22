package com.opentext.itom.ucmdb.integration.push.configuration;

import java.util.HashMap;
import java.util.Map;

public class ClassMapping {
    private Map<String, String> classnameMapping;

    public Map<String, String> getClassnameMapping() {
        if(classnameMapping == null){
            classnameMapping = new HashMap<>();
        }
        return classnameMapping;
    }
}
