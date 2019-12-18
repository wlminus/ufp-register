package com.wlminus.ufp.domain;

import com.wlminus.ufp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SubjectRequireTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubjectRequire.class);
        SubjectRequire subjectRequire1 = new SubjectRequire();
        subjectRequire1.setId(UUID.randomUUID());
        SubjectRequire subjectRequire2 = new SubjectRequire();
        subjectRequire2.setId(subjectRequire1.getId());
        assertThat(subjectRequire1).isEqualTo(subjectRequire2);
        subjectRequire2.setId(UUID.randomUUID());
        assertThat(subjectRequire1).isNotEqualTo(subjectRequire2);
        subjectRequire1.setId(null);
        assertThat(subjectRequire1).isNotEqualTo(subjectRequire2);
    }
}
