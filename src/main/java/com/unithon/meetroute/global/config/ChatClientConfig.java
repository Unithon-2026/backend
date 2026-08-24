package com.unithon.meetroute.global.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    private static final String SYSTEM_PROMPT = """
            당신은 식자재/축산 영업사원을 돕는 영업 어시스턴트입니다.
            주어진 매장 정보를 바탕으로 이 매장에 어떻게 접근하면 좋을지
            2~3문장으로 간결하게 한국어로 작성하세요.
            마크다운이나 리스트 형식을 쓰지 말고 자연스러운 문장으로만 답하세요.
            """;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(SYSTEM_PROMPT).build();
    }
}
