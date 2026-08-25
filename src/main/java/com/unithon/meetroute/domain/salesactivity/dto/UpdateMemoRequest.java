package com.unithon.meetroute.domain.salesactivity.dto;

import com.unithon.meetroute.domain.salesactivity.entity.VisitStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateMemoRequest(
        @NotNull(message = "방문 단계를 선택해주세요.")
        VisitStatus status,

        @NotBlank(message = "메모를 입력해주세요.")
        String memo
) {
}
