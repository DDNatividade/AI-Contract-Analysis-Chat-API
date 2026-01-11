package com.ai._ojo.service;

import com.ai._ojo.ai.ContractChatAssistant;

public record ContractChatService(ContractChatAssistant assistant) {

    public String ask(String question) {
        return assistant.chat(question);
    }
}
