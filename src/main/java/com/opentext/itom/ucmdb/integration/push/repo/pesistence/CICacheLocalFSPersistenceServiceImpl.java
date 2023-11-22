package com.opentext.itom.ucmdb.integration.push.repo.pesistence;

import com.opentext.itom.ucmdb.integration.push.repo.PushRepository;
import com.opentext.itom.ucmdb.integration.push.repo.PushedCICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashSet;
import java.util.Set;


@Component
@Primary
public class CICacheLocalFSPersistenceServiceImpl implements CICachePersistenceService{
    private static final Logger log = LoggerFactory.getLogger(CICacheLocalFSPersistenceServiceImpl.class);
    public final static String CACHE_FILE_PATH = "data/cicache.dat";
    public final static String SEPARATOR = "\t";

    @Autowired
    PushRepository pushRepository;
    @Override
    public void storeCICache() {
        log.info("[Cache]Store cache.");
        if(!pushRepository.getPushedCICache().isDirty()){
            log.info("[Cache]No change in cache.");
            return;
        }
        File file = new File(CACHE_FILE_PATH);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter writer = null;
        try {
            PushedCICache ciCache = pushRepository.getPushedCICache();
            writer = new BufferedWriter(new FileWriter(file));
            String title = "Time: " + pushRepository.getLastSuccessPushTimestamp() + ". Cached CI Number: " + ciCache.getIdTypeMap().size();
            writer.write(title);
            writer.newLine();
            StringBuilder typeStatistics = new StringBuilder();
            for(String ciType : ciCache.getTypeIdMap().keySet()){
                typeStatistics.append(ciType).append("-").append(ciCache.getTypeIdMap().get(ciType).size()).append(", ");
            }
            writer.write(typeStatistics.toString());
            writer.newLine();
            for(String ciType : ciCache.getTypeIdMap().keySet()){
                writer.write(ciType + SEPARATOR + ciCache.getTypeIdMap().get(ciType).size());
                writer.newLine();
                for(String id : ciCache.getTypeIdMap().get(ciType)){
                    writer.write(id + SEPARATOR + ciCache.getTimestampMap().get(id) + SEPARATOR + ciCache.getCiIdMapping().get(id));
                    writer.newLine();
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            }
            log.info("[Cache]Store cache finished.");
        }
        pushRepository.getPushedCICache().setDirty(false);
    }

    @Override
    public void loadCICache() {
        log.info("[Cache]Load cache.");
        File file = new File(CACHE_FILE_PATH);
        if(!file.exists()){
            return;
        }
        PushedCICache ciCache = pushRepository.getPushedCICache();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String title = reader.readLine();
            if(title == null){
                return;
            }
            pushRepository.setLastSuccessPushTimestamp(Long.valueOf(title.substring(6, 19)));
            reader.readLine();
            String tempString;
            while((tempString = reader.readLine()) != null){
                String[] splits = tempString.split(SEPARATOR);
                int count = Integer.valueOf(splits[1]);
                String ciType = splits[0];
                for(int i = 0; i < count; i++){
                    tempString = reader.readLine();
                    splits = tempString.split(SEPARATOR);
                    long timestamp = Long.valueOf(splits[1]);
                    // idMap
                    ciCache.getCiIdMapping().put(splits[0], splits[2]);
                    // typeMap
                    ciCache.getIdTypeMap().put(splits[0], ciType);
                    Set<String> idSet = ciCache.getTypeIdMap().getOrDefault(ciType, new HashSet<String>());
                    if(!idSet.contains(splits[0])){
                        idSet.add(splits[0]);
                    }
                    ciCache.getTypeIdMap().put(ciType, idSet);
                    // timeMap
                    ciCache.getTimestampMap().put(splits[0], timestamp);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            log.info("[Cache]Load cache finished.");
        }
    }
}
