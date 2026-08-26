package com.unithon.meetroute.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 내부 오류가 발생했습니다."),

    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "SHOP4001", "해당 매장을 찾을 수 없습니다."),
    PRIORITY_NOT_FOUND(HttpStatus.NOT_FOUND, "PRIORITY4001", "아직 계산된 우선순위가 없습니다."),
    AI_BRIEFING_FAILED(HttpStatus.BAD_GATEWAY, "SHOP5001", "AI 브리핑 생성에 실패했습니다."),

    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER4001", "이미 가입된 이메일입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "USER4002", "이메일 또는 비밀번호가 올바르지 않습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "로그인이 필요합니다."),
    SALES_ACTIVITY_NOT_FOUND(HttpStatus.NOT_FOUND, "SALES4001", "방문 기록을 찾을 수 없습니다. 먼저 방문을 기록해주세요."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKMARK4001", "북마크한 매장이 아닙니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
