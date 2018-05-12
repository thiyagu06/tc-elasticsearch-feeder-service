/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appirio.service.challengefeeder.ChallengeFeederServiceConfiguration;
import com.appirio.service.challengefeeder.dao.ChallengeDetailsFeederDAO;
import com.appirio.service.challengefeeder.dto.ChallengeFeederParam;
import com.appirio.service.challengefeeder.manager.ChallengeDetailFeederManager;
import com.appirio.service.challengefeeder.util.JestClientUtils;
import com.appirio.supply.DAOFactory;
import com.appirio.tech.core.api.v3.TCID;

import de.spinscale.dropwizard.jobs.annotations.DelayStart;
import de.spinscale.dropwizard.jobs.annotations.Every;

/**
 * LoadChangedChallengeDetailJob is used to load the changed challenge details.
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
@DelayStart("15s")
@Every("${com.appirio.service.challengefeeder.job.LoadChangedChallengeDetailJob}")
public class LoadChangedChallengeDetailJob extends BaseJob {
    /**
     * Logger used to log events
     */
    private static final Logger logger = LoggerFactory.getLogger(LoadChangedChallengeDetailJob.class);

    /**
     * The challengeDetailsFeederManager field
     */
    private ChallengeDetailFeederManager challengeDetailsFeederManager;
    
    /**
     * Create LoadChangedChallengeDetailJob
     *
     * @param challengeDetailsFeederManager the challengeDetailsFeederManager to use
     * @param config the config to use
     */
    public LoadChangedChallengeDetailJob(ChallengeDetailFeederManager challengeDetailsFeederManager, ChallengeFeederServiceConfiguration config) {
        this.challengeDetailsFeederManager = challengeDetailsFeederManager;
        this.config = config;
    }
    
    /**
     * Create LoadChangedChallengeDetailJob
     *
     */
    public LoadChangedChallengeDetailJob() {
    }
    
    /**
     * Do job. This methods load the challenge details to elastic services.
     *
     * @param context the context to use
     * @throws JobExecutionException if any error occurs
     */
    @Override
    public void doJob(JobExecutionContext context) throws JobExecutionException {
        RLock lock;
        RedissonClient redisson = null;
        try {
            if (this.challengeDetailsFeederManager == null) {
                this.challengeDetailsFeederManager = new ChallengeDetailFeederManager(JestClientUtils.get(GLOBAL_CONFIGURATION.getJestClientConfiguration()), DAOFactory.getInstance().createDAO(ChallengeDetailsFeederDAO.class));
            }
            if (this.config == null) {
                this.config = GLOBAL_CONFIGURATION;
            }
            Config redissonConfig = new Config();
            redissonConfig.setLockWatchdogTimeout(this.config.getRedissonConfiguration().getLockWatchdogTimeout());
            if (this.config.getRedissonConfiguration().isClusterEnabled()) {
                for (String addr : this.config.getRedissonConfiguration().getNodeAddresses()) {
                    redissonConfig.useClusterServers().addNodeAddress(addr);
                }
               
            } else {
                redissonConfig.useSingleServer().setAddress(this.config.getRedissonConfiguration().getSingleServerAddress());
            }
            
            logger.info("Try to get the lock");
            redisson = Redisson.create(redissonConfig);
            lock = redisson.getLock(config.getRedissonConfiguration().getLoadChangedChallengesJobLockerKeyName());
            if (lock.tryLock()) {
                logger.info("Get the lock for challenge details job successfully");
                try {
                    RMapCache<String, String> mapCache = redisson.getMapCache(config.getRedissonConfiguration().getLoadChangedChallengesJobLastRunTimestampPrefix());

                    String timestamp = mapCache.get(config.getRedissonConfiguration().getLoadChangedChallengesJobLastRunTimestampPrefix());

                    Date lastRunTimestamp = new Date(1L);
                    if (timestamp != null) {
                        lastRunTimestamp = DATE_FORMAT.parse(timestamp);
                    }

                    logger.info("The last run timestamp for challenge details job is:" + timestamp);

                    String currentTime = DATE_FORMAT.format(this.challengeDetailsFeederManager.getTimestamp());

                    List<TCID> totalIds = this.challengeDetailsFeederManager.getChangedChallengeIds(new java.sql.Date(lastRunTimestamp.getTime()));

                    List<Long> ids = new ArrayList<>();
                    for (int i = 0; i < totalIds.size(); ++i) {
                        ids.add(Long.parseLong(totalIds.get(i).getId()));
                    }
                    logger.info("The count of the challenge ids to load:" + ids.size());
                    logger.info("The challenge ids to load:" + ids);

                    int batchSize = this.config.getRedissonConfiguration().getBatchUpdateSize();
                    int to = 0;
                    int from = 0;
                    while (to < ids.size()) {
                        to += (to + batchSize) > ids.size() ? (ids.size() - to) : batchSize;
                        List<Long> sub = ids.subList(from, to);
                        ChallengeFeederParam param = new ChallengeFeederParam();
                        param.setIndex(this.config.getRedissonConfiguration().getChallengesIndex());
                        param.setType(this.config.getRedissonConfiguration().getChallengesType());
                        param.setChallengeIds(sub);
                        try {
                            this.challengeDetailsFeederManager.pushChallengeDetailsFeeder(param);
                        } catch (Exception e) {
                            // ignore all exception
                            e.printStackTrace();
                        }

                        from = to;
                    }

                    logger.info("update last run timestamp for challenge details job is:" + currentTime);
                    mapCache.put(config.getRedissonConfiguration().getLoadChangedChallengesJobLastRunTimestampPrefix(), currentTime);
                } finally {
                    logger.info("release the lock for challenge details job");
                    lock.unlock();
                }
            } else {
                logger.warn("the previous challenge details job is still running");
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            if (redisson != null) {
                redisson.shutdown();
            }
        }
    }
}