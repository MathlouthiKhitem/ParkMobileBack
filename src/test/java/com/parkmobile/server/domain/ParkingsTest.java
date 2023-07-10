package com.parkmobile.server.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.parkmobile.server.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParkingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parkings.class);
        Parkings parkings1 = new Parkings();
        parkings1.setId("id1");
        Parkings parkings2 = new Parkings();
        parkings2.setId(parkings1.getId());
        assertThat(parkings1).isEqualTo(parkings2);
        parkings2.setId("id2");
        assertThat(parkings1).isNotEqualTo(parkings2);
        parkings1.setId(null);
        assertThat(parkings1).isNotEqualTo(parkings2);
    }
}
