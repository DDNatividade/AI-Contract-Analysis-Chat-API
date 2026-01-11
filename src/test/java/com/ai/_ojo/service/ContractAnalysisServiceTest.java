package com.ai._ojo.service;

import com.ai._ojo.ai.ContractAnalysisAssistant;
import com.ai._ojo.domain.ContractAnalysis;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractAnalysisServiceTest {

//    @Test
//    void delegatesAndReturnsSameInstance() {
//        // Given
//        ContractAnalysisAssistant assistant = mock(ContractAnalysisAssistant.class);
//        ContractAnalysis expected = new ContractAnalysis();
//        when(assistant.analyze("contract body")).thenReturn(expected);
//        ContractAnalysisService service = new ContractAnalysisService();
//        service.setAssistant(assistant);
//
//        // When
//        ContractAnalysis actual = service.analyzeContract("contract body");
//
//        // Then
//        assertSame(expected, actual, "El servicio debería devolver exactamente la instancia devuelta por el assistant");
//        verify(assistant, times(1)).analyze("contract body");
//    }
//
//    @Test
//    void propagatesRuntimeExceptionFromAssistant() {
//        // Given
//        ContractAnalysisAssistant assistant = mock(ContractAnalysisAssistant.class);
//        when(assistant.analyze(anyString())).thenThrow(new RuntimeException("boom"));
//        ContractAnalysisService service = new ContractAnalysisService();
//        service.setAssistant(assistant);
//
//        // When / Then
//        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.analyzeContract("x"));
//        assertEquals("boom", ex.getMessage());
//        verify(assistant, times(1)).analyze("x");
//    }
//
//    @Test
//    void forwardsNullContractTextToAssistant() {
//        // Given
//        ContractAnalysisAssistant assistant = mock(ContractAnalysisAssistant.class);
//        when(assistant.analyze((String) null)).thenReturn(null);
//        ContractAnalysisService service = new ContractAnalysisService();
//        service.setAssistant(assistant);
//
//        // When
//        ContractAnalysis result = service.analyzeContract(null);
//
//        // Then
//        assertNull(result, "Si el assistant devuelve null, el servicio debe propagar null");
//        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
//        verify(assistant).analyze(captor.capture());
//        assertNull(captor.getValue(), "El servicio debe pasar exactamente null al assistant");
//    }
//
//    @Test
//    void propagatesNullReturnedByAssistant() {
//        // Given
//        ContractAnalysisAssistant assistant = mock(ContractAnalysisAssistant.class);
//        when(assistant.analyze("any")).thenReturn(null);
//        ContractAnalysisService service = new ContractAnalysisService();
//        service.setAssistant(assistant);
//
//        // When
//        ContractAnalysis out = service.analyzeContract("any");
//
//        // Then
//        assertNull(out, "El servicio no debe fabricar un objeto por defecto si el assistant devuelve null");
//        verify(assistant).analyze("any");
//    }
//
//    @Test
//    void forwardsLargeInput_entireStringPassed() {
//        // Given
//        ContractAnalysisAssistant assistant = mock(ContractAnalysisAssistant.class);
//        ContractAnalysis expected = new ContractAnalysis();
//        when(assistant.analyze(anyString())).thenReturn(expected);
//        ContractAnalysisService service = new ContractAnalysisService();
//        service.setAssistant(assistant);
//
//        StringBuilder sb = new StringBuilder(100_000);
//        for (int i = 0; i < 100_000; i++) sb.append('a');
//        String large = sb.toString();
//
//        // When
//        ContractAnalysis actual = service.analyzeContract(large);
//
//        // Then
//        assertSame(expected, actual);
//        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
//        verify(assistant).analyze(captor.capture());
//        assertEquals(100_000, captor.getValue().length(), "El servicio debe reenviar la cadena completa al assistant");
//    }
//
//    @Test
//    void constructorWithNullAssistant_throwsOnInvocation() {
//        // Given
//        ContractAnalysisService service = new ContractAnalysisService();
//        // intentionally do not set assistant (null)
//
//        // When / Then
//        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.analyzeContract("x"),
//                "Si no se inyecta el assistant, la invocación debería lanzar IllegalStateException en tiempo de ejecución");
//        assertTrue(ex.getMessage().contains("No ContractAnalysisAssistant"));
//    }
//
//    @Test
//    void concurrentCalls_allInvocationsReceivedWithoutExceptions() throws Exception {
//        // Given
//        ContractAnalysisAssistant assistant = mock(ContractAnalysisAssistant.class);
//        when(assistant.analyze(anyString())).thenAnswer(invocation -> new ContractAnalysis());
//        ContractAnalysisService service = new ContractAnalysisService();
//        service.setAssistant(assistant);
//
//        int threads = 8;
//        int callsPerThread = 25;
//        int total = threads * callsPerThread;
//        ExecutorService exec = Executors.newFixedThreadPool(threads);
//        try {
//            List<Callable<Void>> tasks = new ArrayList<>();
//            for (int i = 0; i < total; i++) {
//                final String payload = "c-" + i;
//                tasks.add(() -> {
//                    service.analyzeContract(payload);
//                    return null;
//                });
//            }
//
//            List<Future<Void>> futures = exec.invokeAll(tasks);
//            for (Future<Void> f : futures) {
//                f.get(); // fallará si la llamada lanzó una excepción
//            }
//
//            // Then
//            verify(assistant, times(total)).analyze(anyString());
//        } finally {
//            exec.shutdownNow();
//        }
//    }
}
