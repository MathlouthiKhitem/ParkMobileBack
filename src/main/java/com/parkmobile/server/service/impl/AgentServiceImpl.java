package com.parkmobile.server.service.impl;

import com.parkmobile.server.domain.Agent;
import com.parkmobile.server.repository.AgentRepository;
import com.parkmobile.server.service.AgentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Agent}.
 */
@Service
public class AgentServiceImpl implements AgentService {

    private final Logger log = LoggerFactory.getLogger(AgentServiceImpl.class);

    private final AgentRepository agentRepository;

    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Override
    public Agent save(Agent agent) {
        log.debug("Request to save Agent : {}", agent);
        return agentRepository.save(agent);
    }

    @Override
    public Agent update(Agent agent) {
        log.debug("Request to update Agent : {}", agent);
        // no save call needed as we have no fields that can be updated
        return agent;
    }

    @Override
    public Optional<Agent> partialUpdate(Agent agent) {
        log.debug("Request to partially update Agent : {}", agent);

        return agentRepository
            .findById(agent.getId())
            .map(existingAgent -> {
                return existingAgent;
            })// .map(agentRepository::save)
        ;
    }

    @Override
    public List<Agent> findAll() {
        log.debug("Request to get all Agents");
        return agentRepository.findAll();
    }

    @Override
    public Optional<Agent> findOne(String id) {
        log.debug("Request to get Agent : {}", id);
        return agentRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Agent : {}", id);
        agentRepository.deleteById(id);
    }
}
