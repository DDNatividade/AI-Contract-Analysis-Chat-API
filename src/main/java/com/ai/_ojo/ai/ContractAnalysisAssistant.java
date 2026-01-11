package com.ai._ojo.ai;

import com.ai._ojo.domain.ContractAnalysis;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ContractAnalysisAssistant {

    @SystemMessage("""
        You are an assistant specialized in analyzing legal contracts.
        Your task is to extract structured information from the provided contract text.

        Rules:
        - Do NOT invent information.
        - If a value is not explicitly stated, return null.
        - Base your analysis ONLY on the provided contract text.
        - Do NOT provide legal advice.
        - Respond ONLY with valid JSON that matches the expected structure.
        """)
    @UserMessage("""
        Analyze the following contract and return the result as JSON.

        Instructions:
        - Provide a concise summary.
        - Identify parties, dates, duration and financial terms.
        - Identify up to 5 important clauses with a risk level.
        - If something is missing, use null.

        Contract text:
        {{contractText}}
        """)
    ContractAnalysis analyze(String contractText);
}
