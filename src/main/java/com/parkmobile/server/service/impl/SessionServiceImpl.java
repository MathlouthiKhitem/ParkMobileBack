package com.parkmobile.server.service.impl;

import com.parkmobile.server.domain.Session;
import com.parkmobile.server.repository.SessionRepository;
import com.parkmobile.server.service.SessionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Session}.
 */
@Service
public class SessionServiceImpl implements SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);

    private final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session save(Session session) {
        log.debug("Request to save Session : {}", session);
        return sessionRepository.save(session);
    }

    @Override
    public Session update(Session session) {
        log.debug("Request to update Session : {}", session);
        return sessionRepository.save(session);
    }

    @Override
    public Optional<Session> partialUpdate(Session session) {
        log.debug("Request to partially update Session : {}", session);

        return sessionRepository
            .findById(session.getId())
            .map(existingSession -> {
                if (session.getStartTime() != null) {
                    existingSession.setStartTime(session.getStartTime());
                }
                if (session.getEndTime() != null) {
                    existingSession.setEndTime(session.getEndTime());
                }
                if (session.getLocation() != null) {
                    existingSession.setLocation(session.getLocation());
                }

                return existingSession;
            })
            .map(sessionRepository::save);
    }

    @Override
    public List<Session> findAll() {
        log.debug("Request to get all Sessions");
        return sessionRepository.findAll();
    }

    @Override
    public Optional<Session> findOne(String id) {
        log.debug("Request to get Session : {}", id);
        return sessionRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Session : {}", id);
        sessionRepository.deleteById(id);
    }
}
