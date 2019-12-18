package com.wlminus.ufp.domain;

import com.wlminus.ufp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RequirePairTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequirePair.class);
        RequirePair requirePair1 = new RequirePair();
        requirePair1.setId(UUID.randomUUID());
        RequirePair requirePair2 = new RequirePair();
        requirePair2.setId(requirePair1.getId());
        assertThat(requirePair1).isEqualTo(requirePair2);
        requirePair2.setId(UUID.randomUUID());
        assertThat(requirePair1).isNotEqualTo(requirePair2);
        requirePair1.setId(null);
        assertThat(requirePair1).isNotEqualTo(requirePair2);
    }
}
