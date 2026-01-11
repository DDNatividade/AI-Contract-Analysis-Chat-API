package com.ai._ojo.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ContractChatAssistant {

    @SystemMessage("""
        You are a contract analysis assistant.

        Rules:
        - Answer ONLY using the information contained in the contract.
        - If the answer is not specified, say: "The contract does not specify this."
        - Do NOT provide legal advice.
        - Be concise and factual.
        """)
    @UserMessage("""
        Question:
        {{question}}
        """)
    String chat(String question);
}
