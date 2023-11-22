package com.opentext.itom.ucmdb.integration.push.repo.model;

import com.opentext.itom.ucmdb.client.rest.wrapper.ClassModelAttributeWrapper;
import com.opentext.itom.ucmdb.client.rest.wrapper.ClassModelClassWrapper;
import com.opentext.itom.ucmdb.integration.push.configuration.ClassTypeConfiguration;
import com.opentext.itom.ucmdb.integration.push.configuration.SimpleTopology;
import com.opentext.itom.ucmdb.integration.push.configuration.ClassmodelConfigRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.opentext.itom.ucmdb.integration.push.configuration.SimpleTopology.CLASSMODEL_ATTRNAME_ALL;

@Component
public class ClassModelWrappeConverter {
    static ClassmodelConfigRepo classmodelConf;
    @Autowired
    ClassmodelConfigRepo autoclassmodelConf;

    @PostConstruct
    public void init(){
        classmodelConf = this.autoclassmodelConf;
    }
    public static ClassTypeMeta convertClassWrapper2Meta(ClassModelClassWrapper classWrapper) {
        if(classWrapper == null){
            return null;
        }
        ClassTypeMeta rlt = new ClassTypeMeta(classWrapper.getName());
        ClassTypeConfiguration classConf = classmodelConf.getClassmodelConf().getAggregatedClassConfigurationByName(classWrapper.getName());

        Set<String> attrSet = classConf.getAttributes();
        for(ClassModelAttributeWrapper attr : classWrapper.getAllAttributes()){
            if(attrSet.contains(attr.name) || attrSet.contains(CLASSMODEL_ATTRNAME_ALL)){
                if(SimpleTopology.excluteAttributes.contains(attr.name)){
                    continue;
                }
                int size = 0;
                if(attr.valueSize != null){
                    size = attr.valueSize;
                }
                AttributeMeta attrMeta = new AttributeMeta(attr.name, AttributeTypeEnum.fromString(attr.type), size);
                rlt.getAttributeMetaList().add(attrMeta);
            }
        }
        return rlt;
    }

    public static ClassTypeMeta convertRelationWrapper2Meta(ClassModelClassWrapper classWrapper) {
        if(classWrapper == null){
            return null;
        }
        ClassTypeMeta rlt = new ClassTypeMeta(classWrapper.getName());
        rlt.setRelation(true);
        return rlt;
    }

}
