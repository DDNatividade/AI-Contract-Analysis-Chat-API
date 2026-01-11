package com.ai._ojo.service;


import com.ai._ojo.ai.ContractAnalysisAssistant;
import com.ai._ojo.domain.ContractAnalysis;
import org.springframework.stereotype.Service;

@Service
public class ContractAnalysisService {

    private final ContractAnalysisAssistant assistant;

    public ContractAnalysisService(ContractAnalysisAssistant assistant) {
        this.assistant = assistant;
    }

    public ContractAnalysis analyzeContract(String contractText) {
        return assistant.analyze(contractText);
    }
}

