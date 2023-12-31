package com.parkmobile.server.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.parkmobile.server.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agent.class);
        Agent agent1 = new Agent();
        agent1.setId("id1");
        Agent agent2 = new Agent();
        agent2.setId(agent1.getId());
        assertThat(agent1).isEqualTo(agent2);
        agent2.setId("id2");
        assertThat(agent1).isNotEqualTo(agent2);
        agent1.setId(null);
        assertThat(agent1).isNotEqualTo(agent2);
    }
}
