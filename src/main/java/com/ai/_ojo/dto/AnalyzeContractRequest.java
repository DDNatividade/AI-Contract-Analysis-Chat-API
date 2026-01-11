package com.ai._ojo.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class AnalyzeContractRequest {
    private File contractText;

    public File getContractText() {
        return contractText;
    }

    public void setContractText(File contractText) {
        this.contractText = contractText;
    }
}
