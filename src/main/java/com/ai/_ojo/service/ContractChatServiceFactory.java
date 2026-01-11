package com.ai._ojo.service;

import com.ai._ojo.ai.ContractChatAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ContractChatServiceFactory {

    /**
     * Crea una nueva instancia de ContractChatService para el texto de contrato dado.
     */
    public ContractChatService create(String contractText) {
        // 1. Embeddings
        EmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("text-embedding-3-large")
                .build();

        EmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();

        // 2. Ingesta
        Document document = Document.from(contractText);
        List<TextSegment> segments = DocumentSplitters.recursive(500, 50).split(document);

        for (TextSegment segment : segments) {
            Embedding embedding = embeddingModel.embed(segment).content();
            store.add(embedding, segment);
        }

        // 3. Retriever
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                .build();

        // 4. Modelo de chat
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o")
                .temperature(0.1)
                .build();

        // 5. Asistente
        ContractChatAssistant assistant = AiServices.builder(ContractChatAssistant.class)
                .chatModel(chatModel)
                .contentRetriever(contentRetriever)
                .build();

        return new ContractChatService(assistant);
    }
}
