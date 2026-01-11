package com.ai._ojo.service;

import com.ai._ojo.ai.ContractChatAssistant;
import org.junit.jupiter.api.Test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractChatServiceTest {

    // Helper: create instance without invoking constructor and inject the assistant mock
    private ContractChatService createServiceWithAssistant(ContractChatAssistant assistant) throws Exception {
        // Obtain Unsafe
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        // Allocate instance without running constructor
        Object instance = unsafe.allocateInstance(ContractChatService.class);

        // Inject the private final field 'assistant'
        Field assistantField = ContractChatService.class.getDeclaredField("assistant");
        assistantField.setAccessible(true);

        assistantField.set(instance, assistant);

        return (ContractChatService) instance;
    }

    @Test
    void ask_delegatesQuestionAndReturnsAssistantResponse() throws Exception {
        // Given
        ContractChatAssistant assistant = mock(ContractChatAssistant.class);
        when(assistant.chat("Hello")).thenReturn("Hi there");

        ContractChatService service = createServiceWithAssistant(assistant);

        // When
        String out = service.ask("Hello");

        // Then
        assertEquals("Hi there", out);
        verify(assistant, times(1)).chat("Hello");
    }

    @Test
    void ask_forwardsNullQuestionToAssistantAndPropagatesNullResponse() throws Exception {
        // Given
        ContractChatAssistant assistant = mock(ContractChatAssistant.class);
        when(assistant.chat((String) null)).thenReturn(null);

        ContractChatService service = createServiceWithAssistant(assistant);

        // When
        String out = service.ask(null);

        // Then
        assertNull(out);
        // verify that exactly null was passed to the assistant
        verify(assistant).chat((String) isNull());
    }

    @Test
    void ask_propagatesRuntimeExceptionFromAssistant() throws Exception {
        // Given
        ContractChatAssistant assistant = mock(ContractChatAssistant.class);
        when(assistant.chat(anyString())).thenThrow(new RuntimeException("assistant failed"));

        ContractChatService service = createServiceWithAssistant(assistant);

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.ask("q"));
        assertEquals("assistant failed", ex.getMessage());
        verify(assistant).chat("q");
    }

    @Test
    void concurrentAsk_multipleThreads_noRaceOrExceptions() throws Exception {
        // Given
        ContractChatAssistant assistant = mock(ContractChatAssistant.class);
        when(assistant.chat(anyString())).thenAnswer(invocation -> {
            // small delay to increase contention
            Thread.sleep(5);
            return "pong";
        });

        ContractChatService service = createServiceWithAssistant(assistant);

        int threads = 8;
        int callsPerThread = 25;
        int total = threads * callsPerThread;

        ExecutorService exec = Executors.newFixedThreadPool(threads);
        try {
            List<Callable<String>> tasks = new ArrayList<>();
            for (int i = 0; i < total; i++) {
                final String payload = "q-" + i;
                tasks.add(() -> service.ask(payload));
            }

            List<Future<String>> futures = exec.invokeAll(tasks);
            for (Future<String> f : futures) {
                String r = f.get();
                assertEquals("pong", r);
            }

            // Then: ensure assistant was invoked expected number of times
            verify(assistant, times(total)).chat(anyString());
        } finally {
            exec.shutdownNow();
        }
    }
}

