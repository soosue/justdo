package com.project.justdo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.project.justdo.domain.MemberTexture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DayOffApplicationTest {
    @DisplayName("연차사용자는 연차를 신청할 수 있다.")
    @Test
    void 연차신청() {
        DayOff dayOff = new DayOff(2022, 15, 베니스);

        DayOffApplication dayOffApplication = new DayOffApplication(베니스, dayOff,
                "2022-09-05", "2022-09-09",
                List.of(아메리, 케이크),
                "010-1234-5678",
                아이스,
                "개인 사유로 연차를 신청합니다.");

        dayOffApplication.validate();
    }

    @DisplayName("연차사용자는 남은 연차 이상으로 연차 신청할 수 없다.")
    @Test
    void 연차신청_실패() {
        DayOff dayOff = new DayOff(2022, 4, 베니스);

        DayOffApplication dayOffApplication = new DayOffApplication(베니스, dayOff,
                "2022-09-05", "2022-09-09",
                List.of(아메리, 케이크),
                "010-1234-5678",
                아이스,
                "개인 사유로 연차를 신청합니다.");

        assertThatThrownBy(() ->
                dayOffApplication.validate()
        ).isInstanceOf(IllegalStateException.class);
    }
}
