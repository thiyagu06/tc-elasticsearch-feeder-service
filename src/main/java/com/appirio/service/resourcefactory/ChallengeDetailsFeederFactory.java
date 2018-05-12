package com.appirio.service.resourcefactory;

import com.appirio.service.challengefeeder.ChallengeFeederServiceConfiguration;
import com.appirio.service.challengefeeder.dao.ChallengeDetailsFeederDAO;
import com.appirio.service.challengefeeder.manager.ChallengeDetailFeederManager;
import com.appirio.service.challengefeeder.resources.ChallengeDetailsFeederResource;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.supply.DAOFactory;
import com.appirio.supply.SupplyException;

import io.searchbox.client.JestClient;

/**
 * Factory for ChallengeFeederResource
 *
 * 
 * @author TCSCODER
 * @version 1.0
 */
public class ChallengeDetailsFeederFactory implements ResourceFactory<ChallengeDetailsFeederResource> {

    /**
     * The jest client
     */
    private final JestClient jestClient;
    
    private final ChallengeFeederServiceConfiguration config;

    /**
     * Simple constructor
     * 
     * @param jestClient the jest client
     */
    public ChallengeDetailsFeederFactory(JestClient jestClient,ChallengeFeederServiceConfiguration config) {
        this.jestClient = jestClient;
        this.config = config;
    }

    /**
     * Get ChallengeDetailsFeederResource object
     *
     * @return ChallengeDetailsFeederResource the challenge details feeder resource
     * @throws SupplyException exception for supply server
     */
    @Override
    public ChallengeDetailsFeederResource getResourceInstance() throws SupplyException {
        final ChallengeDetailFeederManager challengeDetailsManager = new ChallengeDetailFeederManager(jestClient, DAOFactory.getInstance().createDAO(ChallengeDetailsFeederDAO.class));
        challengeDetailsManager.setRootDomain(config.getAuthDomain());
        return new ChallengeDetailsFeederResource(challengeDetailsManager);
    }
}
