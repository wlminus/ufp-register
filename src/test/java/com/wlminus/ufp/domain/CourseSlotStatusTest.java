package com.wlminus.ufp.domain;

import com.wlminus.ufp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CourseSlotStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseSlotStatus.class);
        CourseSlotStatus courseSlotStatus1 = new CourseSlotStatus();
        courseSlotStatus1.setId(UUID.randomUUID());
        CourseSlotStatus courseSlotStatus2 = new CourseSlotStatus();
        courseSlotStatus2.setId(courseSlotStatus1.getId());
        assertThat(courseSlotStatus1).isEqualTo(courseSlotStatus2);
        courseSlotStatus2.setId(UUID.randomUUID());
        assertThat(courseSlotStatus1).isNotEqualTo(courseSlotStatus2);
        courseSlotStatus1.setId(null);
        assertThat(courseSlotStatus1).isNotEqualTo(courseSlotStatus2);
    }
}
