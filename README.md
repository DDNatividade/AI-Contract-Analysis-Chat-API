🧠 AI Contract Analysis & Chat API

Versión: 1.0.0-RELEASE
Estado: ✅ Lista para Producción
Fecha: 2026-01-11

Una API REST desarrollada con Spring Boot que analiza contratos legales en formato PDF utilizando inteligencia artificial y permite realizar preguntas conversacionales sobre el contenido analizado mediante un enfoque de Generación Aumentada por Recuperación (RAG).

## 🏗️ Arquitectura de la Aplicación

### Stack Tecnológico
```
┌─────────────────────────────────────────┐
│ Frontend: Postman (API Testing)        │
└──────────────┬──────────────────────────┘
               ↓ HTTP/REST
┌─────────────────────────────────────────┐
│ Spring Boot 3.5.9                       │
│ - Spring MVC (web tradicional)          │
│ - Tomcat embebido                       │
│ - @RestController endpoints             │
└──────────────┬──────────────────────────┘
               ↓
┌─────────────────────────────────────────┐
│ Business Layer                          │
│ - ContractAnalysisService               │
│ - ContractChatService                   │
│ - PdfExtractor                          │
└──────────────┬──────────────────────────┘
               ↓
┌─────────────────────────────────────────┐
│ AI Integration Layer (LangChain4j)      │
│ - ContractAnalysisAssistant (análisis)  │
│ - ContractChatAssistant (chat RAG)      │
└──────────────┬──────────────────────────┘
               ↓
┌─────────────────────────────────────────┐
│ External Services                       │
│ - OpenAI GPT-4o (análisis)              │
│ - OpenAI text-embedding-3-large (RAG)   │
└─────────────────────────────────────────┘
```

### Flujo de Datos - Análisis de Contrato
```
PDF File → MultipartFile → byte[] → PDFBox → String (text)
    ↓
LangChain4j → OpenAI GPT-4 → JSON Response
    ↓
ContractAnalysis Object → Cliente (JSON)
```

### Flujo de Datos - Chat RAG
```
Contract Text → DocumentSplitter → Chunks (500 chars)
    ↓
OpenAI Embeddings → Vector Store (en memoria)
    ↓
User Question → Embedding → Similarity Search → Top 3 Chunks
    ↓
Chunks + Question → GPT-4 → Answer → Cliente
```

---

## 📂 Estructura del Proyecto

```
src/main/java/com/ai/_ojo/
├── Application.java                    # Entry point
├── ai/
│   ├── AiConfig.java                   # Configuración de beans IA
│   ├── ContractAnalysisAssistant.java  # Interface para análisis
│   └── ContractChatAssistant.java      # Interface para chat
├── controller/
│   └── ContractController.java         # REST endpoints
├── service/
│   ├── ContractAnalysisService.java    # Lógica de análisis
│   ├── ContractChatService.java        # Lógica de chat
│   └── ContractChatServiceFactory.java # Crea instancias RAG
├── domain/
│   ├── ContractAnalysis.java           # DTO principal
│   ├── Parties.java                    # Partes del contrato
│   ├── KeyDates.java                   # Fechas importantes
│   ├── FinancialTerms.java             # Términos financieros
│   ├── KeyClause.java                  # Cláusulas importantes
│   ├── ClauseType.java                 # Enum de tipos
│   └── RiskLevel.java                  # Enum de riesgo
├── dto/
│   ├── ChatRequest.java                # Request para chat
│   └── ChatResponse.java               # Response de chat
└── pdfextractor/
    └── PdfExtractor.java               # Extracción de texto PDF
```

---

## 🔌 API Endpoints

### POST /api/contracts/analyze
**Función:** Analiza un contrato PDF y extrae información estructurada

**Request:**
```http
POST http://localhost:1505/api/contracts/analyze
Content-Type: multipart/form-data

file: [archivo.pdf]
```

**Response (200 OK):**
```json
{
  "contractType": "Service Agreement",
  "summary": "Este contrato establece...",
  "parties": {
    "partyA": "Empresa A S.A.",
    "partyB": "Proveedor Tech Inc."
  },
  "keyDates": {
    "effectiveDate": "2024-01-01",
    "expirationDate": "2026-12-31"
  },
  "duration": "3 años",
  "financialTerms": {
    "totalAmount": 500000.0,
    "currency": "USD",
    "paymentSchedule": "Trimestral"
  },
  "keyClauses": [
    {
      "type": "TERMINATION",
      "summary": "Cualquier parte puede terminar con 60 días de aviso",
      "riskLevel": "MEDIUM"
    },
    {
      "type": "LIABILITY",
      "summary": "Responsabilidad limitada al valor del contrato",
      "riskLevel": "LOW"
    }
  ],
  "specialNotes": [
    "Sujeto a revisión anual",
    "Incluye soporte técnico 24/7"
  ]
}
```

### POST /api/contracts/chat
**Función:** Pregunta sobre el contrato previamente analizado

**Request:**
```http
POST http://localhost:1505/api/contracts/chat
Content-Type: application/json

{
  "question": "¿Cuáles son las obligaciones del proveedor?"
}
```

**Response (200 OK):**
```json
{
  "answer": "Según el contrato, el proveedor tiene las siguientes obligaciones: 1) Proporcionar soporte técnico 24/7, 2) Mantener un tiempo de respuesta de máximo 4 horas para incidentes críticos, 3) Realizar actualizaciones de seguridad mensuales, y 4) Proveer reportes trimestrales de desempeño."
}
```

---

## 🎓 Conceptos Técnicos Implementados

### 1. Dependency Injection (Spring)
```java
public ContractController(
    ContractAnalysisService analysisService,
    ContractChatServiceFactory chatServiceFactory
) {
    this.analysisService = analysisService;
    this.chatServiceFactory = chatServiceFactory;
}
```

### 2. LangChain4j AI Services
```java
@UserMessage("""
    Analyze the following contract and return the result as JSON.
    Contract text: {{contractText}}
""")
ContractAnalysis analyze(String contractText);
```

### 3. RAG (Retrieval Augmented Generation)
```java
// 1. Dividir documento
List<TextSegment> segments = DocumentSplitters.recursive(500, 50)
    .split(document);

// 2. Crear embeddings
Embedding embedding = embeddingModel.embed(segment).content();

// 3. Almacenar en vector store
store.add(embedding, segment);

// 4. Buscar fragmentos relevantes
ContentRetriever retriever = EmbeddingStoreContentRet
