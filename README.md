🧠 AI Contract Analysis & Chat API

Version: 1.0.0-RELEASE
Status: ✅ Production Ready
Date: 2026-01-11

A REST API built with Spring Boot that analyzes legal contracts in PDF format using artificial intelligence and allows conversational questions over the analyzed content through a Retrieval Augmented Generation (RAG) approach.


## 🏗️ Application Architecture

### Technology Stack
```
┌─────────────────────────────────────────┐
│ Frontend: Postman (API Testing)        │
└──────────────┬──────────────────────────┘
               ↓ HTTP/REST
┌─────────────────────────────────────────┐
│ Spring Boot 3.5.9                       │
│ - Spring MVC (traditional web)          │
│ - Embedded Tomcat                       │
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
│ - ContractAnalysisAssistant (analysis)  │
│ - ContractChatAssistant (RAG chat)      │
└──────────────┬──────────────────────────┘
               ↓
┌─────────────────────────────────────────┐
│ External Services                       │
│ - OpenAI GPT-4o (analysis)              │
│ - OpenAI text-embedding-3-large (RAG)   │
└─────────────────────────────────────────┘
```

### Data Flow - Contract Analysis
```
PDF File → MultipartFile → byte[] → PDFBox → String (text)
    ↓
LangChain4j → OpenAI GPT-4 → JSON Response
    ↓
ContractAnalysis Object → Client (JSON)
```

### Data Flow - RAG Chat
```
Contract Text → DocumentSplitter → Chunks (500 chars)
    ↓
OpenAI Embeddings → Vector Store (in-memory)
    ↓
User Question → Embedding → Similarity Search → Top 3 Chunks
    ↓
Chunks + Question → GPT-4 → Answer → Client
```

---

## 📂 Project Structure

```
src/main/java/com/ai/_ojo/
├── Application.java                    # Entry point
├── ai/
│   ├── AiConfig.java                   # AI beans configuration
│   ├── ContractAnalysisAssistant.java  # Interface for analysis
│   └── ContractChatAssistant.java      # Interface for chat
├── controller/
│   └── ContractController.java         # REST endpoints
├── service/
│   ├── ContractAnalysisService.java    # Analysis logic
│   ├── ContractChatService.java        # Chat logic
│   └── ContractChatServiceFactory.java # Creates RAG instances
├── domain/
│   ├── ContractAnalysis.java           # Main DTO
│   ├── Parties.java                    # Contract parties
│   ├── KeyDates.java                   # Important dates
│   ├── FinancialTerms.java             # Financial terms
│   ├── KeyClause.java                  # Important clauses
│   ├── ClauseType.java                 # Type enum
│   └── RiskLevel.java                  # Risk enum
├── dto/
│   ├── ChatRequest.java                # Chat request
│   └── ChatResponse.java               # Chat response
└── pdfextractor/
    └── PdfExtractor.java               # PDF text extraction
```

---

## 🔌 API Endpoints

### POST /api/contracts/analyze
**Function:** Analyzes a PDF contract and extracts structured information

**Request:**
```http
POST http://localhost:1505/api/contracts/analyze
Content-Type: multipart/form-data

file: [file.pdf]
```

**Response (200 OK):**
```json
{
  "contractType": "Service Agreement",
  "summary": "This contract establishes...",
  "parties": {
    "partyA": "Company A Inc.",
    "partyB": "Tech Provider Inc."
  },
  "keyDates": {
    "effectiveDate": "2024-01-01",
    "expirationDate": "2026-12-31"
  },
  "duration": "3 years",
  "financialTerms": {
    "totalAmount": 500000.0,
    "currency": "USD",
    "paymentSchedule": "Quarterly"
  },
  "keyClauses": [
    {
      "type": "TERMINATION",
      "summary": "Either party may terminate with 60 days notice",
      "riskLevel": "MEDIUM"
    },
    {
      "type": "LIABILITY",
      "summary": "Liability limited to contract value",
      "riskLevel": "LOW"
    }
  ],
  "specialNotes": [
    "Subject to annual review",
    "Includes 24/7 technical support"
  ]
}
```

### POST /api/contracts/chat
**Function:** Ask questions about the previously analyzed contract

**Request:**
```http
POST http://localhost:1505/api/contracts/chat
Content-Type: application/json

{
  "question": "What are the provider's obligations?"
}
```

**Response (200 OK):**
```json
{
  "answer": "According to the contract, the provider has the following obligations: 1) Provide 24/7 technical support, 2) Maintain a maximum response time of 4 hours for critical incidents, 3) Perform monthly security updates, and 4) Provide quarterly performance reports."
}
```

---

## 🎓 Implemented Technical Concepts

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
// 1. Split document
List<TextSegment> segments = DocumentSplitters.recursive(500, 50)
    .split(document);

// 2. Create embeddings
Embedding embedding = embeddingModel.embed(segment).content();

// 3. Store in vector store
store.add(embedding, segment);

// 4. Search relevant fragments
ContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
    .embeddingStore(store)
    .maxResults(3)
    .build();
```

### 4. PDF Processing
```java
try (PDDocument document = Loader.loadPDF(bytes)) {
    PDFTextStripper stripper = new PDFTextStripper();
    return stripper.getText(document);
}
```

---

## 💰 Estimated OpenAI Costs

### Per contract analysis (GPT-4):
- Input: ~5,000 tokens (10 pages) → $0.15
- Output: ~500 tokens → $0.015
- **Total: ~$0.165 per analysis**

### Per chat question (GPT-4 + embeddings):
- Embedding: ~5,000 tokens → $0.0065
- GPT-4 input: ~2,000 tokens → $0.06
- GPT-4 output: ~200 tokens → $0.003
- **Total: ~$0.07 per question**

### Possible optimizations:
- Use GPT-3.5 for simple analysis: -70% cost
- Analysis cache: avoid re-analyzing same document
- Use gpt-4o-mini for chat: -50% cost

---

## 🚀 How to Run

### 1. Configure API Key
```powershell
$env:OPENAI_API_KEY = "sk-your-openai-api-key"
```

### 2. Start Application
```powershell
cd C:\Users\user\Downloads\1ojoAI
.\iniciar-app.ps1
```

### 3. Verify It's Running
Look for in the logs:
```
Tomcat started on port(s): 1505 (http)
Started Application in 8.234 seconds
```

### 4. Test with Postman
- Import requests from `GUIA_PRUEBAS_POSTMAN.md`
- Upload a PDF to `/analyze`
- Ask questions in `/chat`


---
