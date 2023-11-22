package com.opentext.itom.ucmdb.integration.push;

import com.opentext.itom.ucmdb.client.UCMDBClient;
import com.opentext.itom.ucmdb.client.rest.wrapper.ClassModelClassWrapper;
import com.opentext.itom.ucmdb.integration.push.configuration.ClassmodelConfigRepo;
import com.opentext.itom.ucmdb.integration.push.configuration.SimpleTopology;
import com.opentext.itom.ucmdb.integration.push.framework.target.PushClient;
import com.opentext.itom.ucmdb.integration.push.repo.PushRepository;
import com.opentext.itom.ucmdb.integration.push.repo.model.ClassModelWrappeConverter;
import com.opentext.itom.ucmdb.integration.push.repo.model.ClassTypeMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.ModelConverter;
import com.opentext.itom.ucmdb.integration.push.repo.model.TargetMeta;
import com.opentext.itom.ucmdb.integration.push.repo.pesistence.CICachePersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class PushInitializer implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(PushInitializer.class);
    @Autowired
    ClassmodelConfigRepo classmodelConfigRepo;
    @Autowired
    PushRepository pushRepo;

    @Autowired
    @Qualifier("rest")
    private UCMDBClient ucmdbClient;

    @Autowired
    @Qualifier("graphql")
    private UCMDBClient graphqlClient;

    @Autowired
    PushClient pushClient;
    @Autowired
    PushService pushService;
    @Autowired
    CICachePersistenceService cacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("[Phase 1/3 Init]Start init.");
        // load configuration
        classmodelConfigRepo.init();
        cacheService.loadCICache();

        // test ucmdb connection
        log.info("[Init]Connecting source UCMDB RESTAPI.");
        boolean connected = ucmdbClient.testConnection();
        if (!connected){
            log.error("[Init]Failed to connect to UCMDB.");
            return;
        }
        log.info("[Init]Source UCMDB RESTAPI, connected.");

        log.info("[Init]Connecting source UCMDB GraphQL.");
        connected = graphqlClient.testConnection();
        if (!connected){
            log.error("[Init]Failed to connect to CMSGateway.");
            return;
        }
        log.info("[Init]Source UCMDB GraphQL, connected.");

        // test target system connection
        log.info("[Init]Connecting push target.");
        connected = pushClient.testConnection();
        if(!connected){
            log.error("[Init]Failed to connect to target system.");
            return;
        }
        log.info("[Init]Target System, connected.");

        // load classmodel from ucmdb
        if(classmodelConfigRepo.isOverride()){
            for(SimpleTopology simpleTopology : classmodelConfigRepo.getClassmodelConf().getSimpleTopoList()){
                loopClassConf(simpleTopology);
            }
        } else{
            log.error("[Init]Unimplemented.");
            System.exit(1);
        }
        log.info("[Init]Load classmodel detail from source UCMDB, success. Number of ClassType: " + pushRepo.getClassTypeMetaMap().size());

        // init the push service
        pushService.init();
        log.info("[Phase 1/3 Init]Init finished");
        log.info("[Phase 2/3 FirstPush]Start first push.");
        // load target system cache
        TargetMeta targetMeta = pushClient.loadTargetMeta();
        if(targetMeta == null){
            // init target system if first time
            targetMeta = pushClient.initTargetSystem(ModelConverter.convertClassMetaMap2TableMetaMap(pushRepo.getClassTypeMetaMap()));
            // start the first push
            pushService.firstPush();
        } else {
            log.info("[Phase 2/3 FirstPush]Previous push context exist, no need of first push.");
            pushRepo.setLastSuccessPushTimestamp(targetMeta.getLastUpdateTimestamp());
            pushService.setFirstPushFinished(true);
        }
        pushRepo.setTargetMeta(targetMeta);
        log.info("[Phase 2/3 FirstPush]First push finished.");
        pushService.setFirstPushFinished(true);
    }

    private void loopClassConf(SimpleTopology simpleTopology) {
        if(simpleTopology == null){
            return;
        }
        ClassTypeMeta classTypeMeta = ClassModelWrappeConverter.convertClassWrapper2Meta(ucmdbClient.getClassDefByName(simpleTopology.getClassname()));
        pushRepo.getClassTypeMetaMap().put(simpleTopology.getClassname(), classTypeMeta);

        // add relation
        if(simpleTopology.getRelated() != null){
            for(String relationname : simpleTopology.getRelated().keySet()){
                ClassModelClassWrapper classWrapper = ucmdbClient.getClassDefByName(relationname);
                for(SimpleTopology child : simpleTopology.getRelated().get(relationname)){
                    ClassTypeMeta relationMeta = ClassModelWrappeConverter.convertRelationWrapper2Meta(classWrapper);
                    relationMeta.setEnd1Class(simpleTopology.getClassname());
                    relationMeta.setEnd2Class(child.getClassname());
                    pushRepo.getClassTypeMetaMap().put(classmodelConfigRepo.getRelationKey(relationname, simpleTopology.getClassname(), child.getClassname()), classTypeMeta);
                    // add child class
                    loopClassConf(child);
                }
            }
        }


    }
}
