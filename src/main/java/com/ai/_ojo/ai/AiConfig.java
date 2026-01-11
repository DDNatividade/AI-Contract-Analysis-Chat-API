package com.ai._ojo.ai;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    @ConditionalOnProperty(name = "OPENAI_API_KEY", matchIfMissing = false)
    public ContractAnalysisAssistant contractAnalysisAssistant() {

        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY environment variable is not set");
        }

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4o")
                .temperature(0.0)
                .strictTools(true)
                .build();

        return AiServices.builder(ContractAnalysisAssistant.class)
                .chatModel(chatModel)
                .build();
    }
}
