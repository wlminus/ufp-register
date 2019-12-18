package com.wlminus.ufp.domain;

import com.wlminus.ufp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PassStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PassStatus.class);
        PassStatus passStatus1 = new PassStatus();
        passStatus1.setId(UUID.randomUUID());
        PassStatus passStatus2 = new PassStatus();
        passStatus2.setId(passStatus1.getId());
        assertThat(passStatus1).isEqualTo(passStatus2);
        passStatus2.setId(UUID.randomUUID());
        assertThat(passStatus1).isNotEqualTo(passStatus2);
        passStatus1.setId(null);
        assertThat(passStatus1).isNotEqualTo(passStatus2);
    }
}
