package com.parkmobile.server.controller;

import com.parkmobile.server.domain.Agent;
import com.parkmobile.server.domain.Parkings;
import com.parkmobile.server.domain.Session;
import com.parkmobile.server.web.rest.AgentResource;
import com.parkmobile.server.web.rest.SessionResource;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("backend/Agent")
@Component
@AllArgsConstructor
public class AgentCint {
    @Autowired
    AgentResource agentResource;
@Autowired
SessionResource sessionService ;
    @PostMapping("/addAgentusers")
    public ResponseEntity<Agent> addParkingWithStartedSession(@RequestBody Map<String, String> request) throws URISyntaxException {
        return agentResource.createAgent(request);
    }
//    @GetMapping("/session-status/{matricule}")
//    public ResponseEntity<String> checkSessionStatus( @PathVariable String matricule) {
//
//        return agentResource.checkSessionStatus(matricule);
//    }

    @GetMapping("/{number}")
    public List<Parkings> getParkingsByNumber(@PathVariable String number) {
    return  agentResource.getParkingsByNumber(number);
    }
    @GetMapping("/sessions/{matricule}")
    public ResponseEntity<List<Session>> getSessionsByMatricule(@PathVariable String matricule) {
        List<Session> sessions = sessionService.findSessionsByMatricule(matricule);

        if (sessions.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(sessions);
        }
    }

}
