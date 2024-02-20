package com.opentext.itom.ucmdb.integration.push.framework.source;

import com.opentext.itom.ucmdb.client.graphql.SingleTopoQuery;
import com.opentext.itom.ucmdb.client.graphql.parser.Element;
import com.opentext.itom.ucmdb.client.graphql.parser.Filter;
import com.opentext.itom.ucmdb.client.graphql.parser.Pagination;
import com.opentext.itom.ucmdb.integration.push.configuration.ClassNameMappingUtil;
import com.opentext.itom.ucmdb.integration.push.configuration.SimpleTopology;
import com.opentext.itom.ucmdb.integration.push.framework.AppConfig;
import com.opentext.itom.ucmdb.integration.push.repo.PushRepository;
import com.opentext.itom.ucmdb.integration.push.repo.model.AttributeMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.ClassTypeMeta;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GraphQLQueryBuilder {
    private static final Logger log = LoggerFactory.getLogger(GraphQLQueryBuilder.class);
    public static final String OP_GREATER_THAN = "gt";
    public static final String OP_LITTLE_THAN = "lt";

    static PushRepository pushRepository;
    @Autowired
    PushRepository autoPushRepository;
    static AppConfig appConfig;
    @Autowired
    AppConfig autoAppConfig;

    static ClassNameMappingUtil classNameMappingUtil;
    @Autowired
    ClassNameMappingUtil autoClassNameMappingUtil;

    @PostConstruct
    public void init(){
        pushRepository = this.autoPushRepository;
        appConfig = this.autoAppConfig;
        classNameMappingUtil = this.autoClassNameMappingUtil;
    }
    public static SingleTopoQuery buildSingleTopoQuery(SimpleTopology simpleTopology) {
        SingleTopoQuery rlt = new SingleTopoQuery();
        Element rootElement = loopElement(simpleTopology);
        rootElement.setRoot(true);
        rootElement.setElementName(classNameMappingUtil.getGraphQLRootClassName(simpleTopology.getClassname()));

        // build pagination
        rootElement.setPagination(new Pagination(appConfig.getPushBatchSize(), 0));
        rootElement.setFilter(buildDefaultTimestampFilter());

        // build attribute

        rlt.setElement(rootElement);
        return rlt;
    }

    private static Element loopElement(SimpleTopology simpleTopology) {
        Element rlt = new Element(classNameMappingUtil.getGraphQLInnerClassName(simpleTopology.getClassname()), false);
        if(simpleTopology.getAttributes().contains(SimpleTopology.CLASSMODEL_ATTRNAME_ALL)){
            ClassTypeMeta classTypeMeta = pushRepository.getClassTypeMetaMap().get(simpleTopology.getClassname());
            if(classTypeMeta == null){
                log.error("[GraphQL]ClassTypeMeta missing, classname = " + simpleTopology.getClassname());
            } else {
                for (AttributeMeta attributeMeta : classTypeMeta.getAttributeMetaList()) {
                    rlt.getAttributes().add(attributeMeta.getAttrName());
                }
            }
        } else{
            rlt.setAttributes(List.copyOf(simpleTopology.getAttributes()));
        }

        // build subelement
        if(simpleTopology.getRelated() != null){
            List<Element> subElementList = new ArrayList<>();
            for(String relationName : simpleTopology.getRelated().keySet()){
                for(SimpleTopology child : simpleTopology.getRelated().get(relationName)){
                    Element subElement = loopElement(child);
                    subElementList.add(subElement);
                }
            }
            rlt.setSubElementList(subElementList);
        } else{
            rlt.setFilter(buildDefaultTimestampFilter());
        }
        return rlt;
    }

    private static Filter buildDefaultTimestampFilter() {
        Filter rlt = new Filter(SimpleTopology.CLASSMODEL_ATTRNAME_LASTACCESSTIME, OP_GREATER_THAN, String.valueOf(pushRepository.getLastSuccessPushTimestamp()))
                .and(Arrays.asList(new Filter(SimpleTopology.CLASSMODEL_ATTRNAME_LASTACCESSTIME, OP_LITTLE_THAN, String.valueOf(pushRepository.getCurrentPushStartTimestamp()))));
        return rlt;
    }

}
