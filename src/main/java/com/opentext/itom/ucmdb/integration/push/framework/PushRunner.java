package com.opentext.itom.ucmdb.integration.push.framework;

import com.opentext.itom.ucmdb.client.graphql.SingleTopoQuery;
import com.opentext.itom.ucmdb.client.graphql.UCMDBGraphQLClient;
import com.opentext.itom.ucmdb.integration.push.configuration.SimpleTopology;
import com.opentext.itom.ucmdb.integration.push.framework.source.GraphQLQueryBuilder;
import com.opentext.itom.ucmdb.integration.push.framework.source.GraphQLWrapperConverter;
import com.opentext.itom.ucmdb.integration.push.repo.PushRepository;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;
import com.opentext.itom.ucmdb.integration.push.framework.target.PushClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushRunner implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(PushRunner.class);
    private final PushRepository pushRepository;
    private final UCMDBGraphQLClient graphQLClient;
    private final PushClient pushClient;
    private final AppConfig appConfig;
    private boolean running = true;

    public PushRunner(PushRepository pushRepository, UCMDBGraphQLClient graphQLClient, PushClient pushClient, AppConfig appConfig) {
        this.pushRepository = pushRepository;
        this.graphQLClient = graphQLClient;
        this.pushClient = pushClient;
        this.appConfig = appConfig;
    }

    @Override
    public void run() {
        while(running){
            // get a classmetatype from queue and push
            SimpleTopology simpleTopology = pushRepository.popPushQueue();
            if(simpleTopology == null){
                log.error("[PushRunner]Get empty classtype from queue.");
                return;
            }
            log.info("[PushRunner]Push thread start. RootClass: " + simpleTopology.getClassname());
            PushStatus pushStatus = pushRepository.getPushStatusMap().get(simpleTopology);
            pushStatus.setStatus(PushStatusEnum.RUNNING);

            SingleTopoQuery query = GraphQLQueryBuilder.buildSingleTopoQuery(simpleTopology);
            int elementCount = graphQLClient.countElement(query);
            // batch push
            int batchCount = elementCount/appConfig.getPushBatchSize() + 1;
            log.info("[PushRunner]Total root element to push: " + elementCount + ". Batch number: " + batchCount);
            for(int i = 0; i < batchCount; i++){
                log.info("[PushRunner]Push batch number " + (i));
                // update query pagination
                query.getElement().getPagination().setSkip(i * appConfig.getPushBatchSize());
                // query ci from ucmdb
                CIBatch ciBatch = GraphQLWrapperConverter.convertWrapper2CIBatch(graphQLClient.batchQuery(query),simpleTopology, i+1, pushRepository.getLastSuccessPushTimestamp());
                log.info("[PushRunner]Get " + ciBatch.getBatchStatistics().getRawCICount() + " raw CIs."
                        + ciBatch.getBatchStatistics().getSkippedCICount() + " CIs ignored by cache. "
                        + ciBatch.getBatchStatistics().getPushCICount() + " CIs and "
                        + ciBatch.getBatchStatistics().getPushRelationCount() + " Relations ready for push. "
                        + ciBatch.getStatisticsString());

                if(ciBatch == null || ciBatch.getCiEntityMap().size() <= 0){
                    continue;
                }
                if(log.isDebugEnabled()){
                    // integrity check of cibatch and print log
                    ciBatch.integrityCheck();
                }
                // push to target system
                PushResult pushResult = pushClient.pushBatch(ciBatch);

                // update cache and status
                pushRepository.updateBatchStatus(simpleTopology, ciBatch, pushResult);
                log.info("[PushRunner]Push finished for batch number " + (i+1));
            }
            pushStatus = pushRepository.getPushStatusMap().get(simpleTopology);
            if(pushStatus.getPushedCI() <= 0 && pushStatus.getErrorCI() > 0){
                pushStatus.setStatus(PushStatusEnum.ERROR);
            } else{
                pushStatus.setStatus(PushStatusEnum.FINISHED);
            }
        }
    }

}
