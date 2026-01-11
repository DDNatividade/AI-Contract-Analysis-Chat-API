package com.ai._ojo.controller;

import com.ai._ojo.domain.ContractAnalysis;
import com.ai._ojo.dto.ChatRequest;
import com.ai._ojo.dto.ChatResponse;
import com.ai._ojo.pdfextractor.PdfExtractor;
import com.ai._ojo.service.ContractAnalysisService;
import com.ai._ojo.service.ContractChatService;
import com.ai._ojo.service.ContractChatServiceFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractAnalysisService analysisService;
    private final ContractChatServiceFactory chatServiceFactory;
    private ContractChatService chatService;

    public ContractController(
            ContractAnalysisService analysisService,
            ContractChatServiceFactory chatServiceFactory
    ) {
        this.analysisService = analysisService;
        this.chatServiceFactory = chatServiceFactory;
    }


    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ContractAnalysis analyze(@RequestPart("file") MultipartFile file) throws IOException {

        // 1. Convertimos el archivo subido en bytes
        byte[] bytes = file.getBytes();

        // 2. Extraemos el texto (Llamada síncrona)
        String contractText = PdfExtractor.extractText(bytes);

        // 3. Inicializamos el servicio de chat con el texto del contrato
        // Esto permite que el usuario haga preguntas sobre este contrato después
        this.chatService = chatServiceFactory.create(contractText);

        // 4. Analizamos con la IA (Llamada síncrona)
        // Spring esperará aquí hasta que la IA responda
        return analysisService.analyzeContract(contractText);
    }



    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {

        if (chatService == null) {
            throw new IllegalStateException(
                    "Chat service not initialized. Call /analyze first."
            );
        }

        String answer = chatService.ask(request.getQuestion());
        return new ChatResponse(answer);
    }
}
