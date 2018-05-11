package com.appirio.service.resourcefactory;

import com.appirio.service.challengefeeder.dao.ChallengeFeederDAO;
import com.appirio.service.challengefeeder.dao.ChallengeDetailsFeederDAO;
import com.appirio.service.challengefeeder.manager.ChallengeDetailFeederManager;
import com.appirio.service.challengefeeder.manager.ChallengeFeederManager;
import com.appirio.service.challengefeeder.resources.ChallengeDetailsFeederResource;
import com.appirio.service.challengefeeder.resources.ChallengeFeederResource;
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

    /**
     * Simple constructor
     * 
     * @param jestClient the jest client
     */
    public ChallengeDetailsFeederFactory(JestClient jestClient) {
        this.jestClient = jestClient;
    }

    /**
     * Get ChallengeFeederResource object
     *
     * @return ChallengeDetailsFeederResource the challenge details feeder resource
     * @throws SupplyException exception for supply server
     */
    @Override
    public ChallengeDetailsFeederResource getResourceInstance() throws SupplyException {
        final ChallengeDetailFeederManager challengeManager = new ChallengeDetailFeederManager(jestClient, DAOFactory.getInstance().createDAO(ChallengeDetailsFeederDAO.class));

        return new ChallengeDetailsFeederResource(challengeManager);
    }
}
