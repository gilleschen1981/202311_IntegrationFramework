package com.opentext.itom.ucmdb.integration.push;

import com.opentext.itom.ucmdb.client.graphql.UCMDBGraphQLClient;
import com.opentext.itom.ucmdb.integration.push.configuration.SimpleTopology;
import com.opentext.itom.ucmdb.integration.push.framework.AppConfig;
import com.opentext.itom.ucmdb.integration.push.framework.PushRunner;
import com.opentext.itom.ucmdb.integration.push.framework.PushStatus;
import com.opentext.itom.ucmdb.integration.push.repo.PushRepository;
import com.opentext.itom.ucmdb.integration.push.configuration.ClassmodelConfigRepo;
import com.opentext.itom.ucmdb.integration.push.framework.target.PushClient;
import com.opentext.itom.ucmdb.integration.push.repo.pesistence.CICachePersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class PushService {
    private static final Logger log = LoggerFactory.getLogger(PushService.class);
    @Autowired
    private PushRepository pushRepository;
    @Autowired
    private ClassmodelConfigRepo classmodelConfigRepo;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private UCMDBGraphQLClient graphQLClient;
    @Autowired
    private PushClient pushClient;
    @Autowired
    ScheduledPush scheduledPush;
    @Autowired
    CICachePersistenceService cacheService;

    public void init(){
        log.info("[Init]Initialize push service. Number of push thread: " + appConfig.getPushThreadPoolSize());
        // start thread pool
        for(int i = 0; i < appConfig.getPushThreadPoolSize(); i++){
            taskExecutor.execute(new PushRunner(pushRepository, graphQLClient, pushClient, appConfig));
        }
    }
    public void firstPush() {
        pushRepository.setCurrentPushStartTimestamp(System.currentTimeMillis());
        // push CI
        for(SimpleTopology simpleTopology : classmodelConfigRepo.getClassmodelConf().getSimpleTopoList()){
            // update PushStatus
            pushRepository.getPushStatusMap().put(simpleTopology, new PushStatus());
            pushRepository.pushPushQueue(simpleTopology);
        }
        scheduledPush.finishPush();
        try {
            Thread.sleep(appConfig.getPushScheduleInterval());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // start schedule push
        scheduledPush.scheduleTask();
    }

}
