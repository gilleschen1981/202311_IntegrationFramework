package com.opentext.itom.ucmdb.integration.push.configuration;

import java.util.*;

public class SimpleTopology {

    public static final String CLASSMODEL_ATTRNAME_GLOBALID = "global_id";
    public static final String CLASSMODEL_ATTRNAME_CMDBID = "cmdb_id";
    public static final String CLASSMODEL_ATTRNAME_CLASSTYPE = "root_class";
    public static final String CLASSMODEL_ATTRNAME_LASTACCESSTIME = "root_lastaccesstime";
    public static final String CLASSMODEL_ATTRNAME_ROOT_CONTAINER = "root_container";
    public static final String CLASSMODEL_ATTRNAME_ALL = "all";
    public static final String CLASSMODEL_ATTRNAME_OWNER_TENANT = "owner_tenant";
    public static final String CLASSMODEL_ATTRNAME_CONSUMER_TENANT = "consumer_tenants";
    public static final String CLASSMODEL_ATTRNAME_FAMILY_ICON = "FAMILY_ICON";
    public static final String CLASSMODEL_ATTRNAME_BODY_ICON = "BODY_ICON";
    public static final String CLASSMODEL_ATTRNAME_ACTUAL_DELETION_PERIOD = "root_actualdeletionperiod";
    public static final String CLASSMODEL_ATTRNAME_ACTUAL_DELETION_CANDIDATE_PERIOD = "root_deletioncandidateperiod";
    public static final String CLASSMODEL_ATTRNAME_GLOBAL_ID_SCOPE = "global_id_scope";
    public static final String CLASSMODEL_ATTRNAME_MENU = "MENU";
    public static final String CLASSMODEL_ATTRNAME_CLASSIFICATION = "classification";
    public static final String CLASSMODEL_ATTRNAME_LAYER = "layer";
    public static final String CLASSMODEL_ATTRNAME_MATCHING_RULE = "matching_rules";

    public static final Set<String> oobAttributes = new HashSet<>(Arrays.asList(
            CLASSMODEL_ATTRNAME_GLOBALID, CLASSMODEL_ATTRNAME_CMDBID, CLASSMODEL_ATTRNAME_CLASSTYPE
            ,CLASSMODEL_ATTRNAME_LASTACCESSTIME, CLASSMODEL_ATTRNAME_ROOT_CONTAINER
    ));
    public static final Set<String> excluteAttributes = new HashSet<>(Arrays.asList(
            CLASSMODEL_ATTRNAME_OWNER_TENANT,CLASSMODEL_ATTRNAME_CONSUMER_TENANT
            ,CLASSMODEL_ATTRNAME_FAMILY_ICON, CLASSMODEL_ATTRNAME_BODY_ICON
            ,CLASSMODEL_ATTRNAME_ACTUAL_DELETION_PERIOD, CLASSMODEL_ATTRNAME_ACTUAL_DELETION_CANDIDATE_PERIOD
            ,CLASSMODEL_ATTRNAME_GLOBAL_ID_SCOPE, CLASSMODEL_ATTRNAME_MENU, CLASSMODEL_ATTRNAME_CLASSIFICATION
            , CLASSMODEL_ATTRNAME_LAYER, CLASSMODEL_ATTRNAME_MATCHING_RULE
    ));

    private String classname;
    private Set<String> attributes;
    // <relationname, List of children>
    private Map<String, List<SimpleTopology>> related;

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public Set<String> getAttributes() {
        if(attributes == null){
            attributes = new HashSet<>();
        }
        return attributes;
    }

    public Map<String, List<SimpleTopology>> getRelated() {
        return related;
    }

    public void loopGetAttributesByName(String classname, ClassTypeConfiguration rlt) {
        if(classname == null){
            return;
        }
        if(classname.equals(getClassname())){
            rlt.getAttributes().addAll(getAttributes());
        }
        if(getRelated() != null){
            for(String relationname : getRelated().keySet()){
                for(SimpleTopology child : getRelated().get(relationname)){
                    child.loopGetAttributesByName(classname, rlt);
                }
            }
        }
    }

    public void loopProcessOObAttributes() {
        for(String attr : oobAttributes){
            getAttributes().add(attr);
        }
        for(String attr : excluteAttributes){
            getAttributes().remove(attr);
        }

        if(getRelated()!= null){
            for(String relationname : getRelated().keySet()){
                for(SimpleTopology child : getRelated().get(relationname)){
                    child.loopProcessOObAttributes();
                }
            }
        }

    }
}
