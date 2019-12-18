package com.wlminus.ufp.domain;

import com.wlminus.ufp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentRegisterStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentRegisterStatus.class);
        StudentRegisterStatus studentRegisterStatus1 = new StudentRegisterStatus();
        studentRegisterStatus1.setId(UUID.randomUUID());
        StudentRegisterStatus studentRegisterStatus2 = new StudentRegisterStatus();
        studentRegisterStatus2.setId(studentRegisterStatus1.getId());
        assertThat(studentRegisterStatus1).isEqualTo(studentRegisterStatus2);
        studentRegisterStatus2.setId(UUID.randomUUID());
        assertThat(studentRegisterStatus1).isNotEqualTo(studentRegisterStatus2);
        studentRegisterStatus1.setId(null);
        assertThat(studentRegisterStatus1).isNotEqualTo(studentRegisterStatus2);
    }
}
