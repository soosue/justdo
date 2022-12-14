package com.project.justdo.service;

import com.project.justdo.domain.DayOff;
import com.project.justdo.domain.DayOffApproval;
import com.project.justdo.domain.Member;
import com.project.justdo.domain.repository.DayOffApprovalRepository;
import com.project.justdo.domain.repository.DayOffRepository;
import com.project.justdo.domain.repository.MemberRepository;
import com.project.justdo.service.dto.DayOffApplicationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DayOffApprovalServiceTest {
    @Autowired
    private DayOffApplicationService dayOffApplicationService;

    @Autowired
    private DayOffApprovalService dayOffApprovalService;

    @Autowired
    private DayOffApprovalRepository dayOffApprovalRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DayOffRepository dayOffRepository;

    @BeforeEach
    void init() {
        Member 베니스 = new Member("베니스");
        memberRepository.save(베니스);
        memberRepository.save(new Member("아메리"));
        memberRepository.save(new Member("케이크"));
        memberRepository.save(new Member("아이스"));

        dayOffRepository.save(new DayOff(2022, 15, 베니스));
    }

    @DisplayName("연차신청서는 승인하거나 거절 할 수 있다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void 연차신청서_승인과_거절(boolean state) {
        DayOffApplicationDto dto = new DayOffApplicationDto(
                memberRepository.findByName("베니스").getId(), "2022-09-05", "2022-09-09",
                List.of(memberRepository.findByName("아메리").getId(), memberRepository.findByName("케이크").getId()),
                "010-1234-5678",
                memberRepository.findByName("아이스").getId(),
                "개인 사유로 연차를 신청합니다."
        );

        Long dayOffApplicationId = dayOffApplicationService.registerDayOffApplication(dto);
        Member member = memberRepository.findById(memberRepository.findByName("아메리").getId()).get();


        Long dayOffApprovalId = dayOffApprovalService.save(dayOffApplicationId, member, state);

        DayOffApproval dayOffApproval = dayOffApprovalRepository.findById(dayOffApprovalId).get();

        assertThat(dayOffApproval.isApproved()).isEqualTo(state);
    }

}
