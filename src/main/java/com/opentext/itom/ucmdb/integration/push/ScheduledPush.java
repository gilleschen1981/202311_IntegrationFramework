package com.opentext.itom.ucmdb.integration.push;

import com.opentext.itom.ucmdb.integration.push.configuration.SimpleTopology;
import com.opentext.itom.ucmdb.integration.push.configuration.ClassmodelConfigRepo;
import com.opentext.itom.ucmdb.integration.push.framework.AppConfig;
import com.opentext.itom.ucmdb.integration.push.framework.PushStatus;
import com.opentext.itom.ucmdb.integration.push.repo.PushRepository;
import com.opentext.itom.ucmdb.integration.push.repo.pesistence.CICachePersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class ScheduledPush {
    private static final Logger log = LoggerFactory.getLogger(ScheduledPush.class);
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Autowired
    PushRepository pushRepository;
    @Autowired
    ClassmodelConfigRepo classmodelConfigRepo;
    @Autowired
    CICachePersistenceService cacheService;
    @Autowired
    private AppConfig appConfig;
    int scheduledPushCount = 0;

    public void scheduleTask() {
        threadPoolTaskScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                scheduledPushCount ++;
                // do delta push
                log.info("[ScheduledPush]Scheduled push started, count: " + scheduledPushCount);
                pushRepository.setCurrentPushStartTimestamp(System.currentTimeMillis());
                // push CI
                for(SimpleTopology simpleTopology : classmodelConfigRepo.getClassmodelConf().getSimpleTopoList()){
                    // update PushStatus
                    pushRepository.getPushStatusMap().put(simpleTopology, new PushStatus());
                    pushRepository.pushPushQueue(simpleTopology);
                }

                finishPush();
            }
        }, appConfig.getPushScheduleInterval());
    }

    public void finishPush() {
        // wait until all push finish
        while(!pushRepository.isPushFinished()){
            log.info("[FinishPush]Wait for first push to finish, size of waiting queue: " + pushRepository.getPushQueueSize());
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long finishTime = System.currentTimeMillis();
        log.info("[FinishPush]Push finished. Start time: " + pushRepository.getCurrentPushStartTimestamp()
                + ". End time: " + finishTime + ". Total CI: " + pushRepository.getCurrentPushCICount()
                + ". Total Relation: " + pushRepository.getCurrentPushRelationCount()
                + ". Rate of CI/millisecond: "
                + (pushRepository.getCurrentPushCICount() + pushRepository.getCurrentPushRelationCount())/((finishTime - pushRepository.getCurrentPushStartTimestamp())/1000));

        pushRepository.setLastSuccessPushTimestamp(pushRepository.getCurrentPushStartTimestamp());
        pushRepository.setCurrentPushStartTimestamp(0L);
        cacheService.storeCICache();
    }


}
