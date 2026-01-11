# AI-Contract-Analysis-Chat-API
A Spring Boot REST API that analyzes legal contracts in PDF format using AI and enables conversational Q&amp;A over the analyzed content using a Retrieval-Augmented Generation (RAG) approach.

🧠 AI Contract Analysis & Chat API

Version: 1.0.0-RELEASE
Status: ✅ Production Ready
Date: 2026-01-11

A Spring Boot REST API that analyzes legal contracts in PDF format using AI and enables conversational Q&A over the analyzed content using a Retrieval-Augmented Generation (RAG) approach.

✅ Final Project Status
Component	Technology	Status
Web Framework	Spring MVC (Servlet stack)	✅ Working
Server	Tomcat 10.1	✅ Port 1505
Threading Model	Thread-per-request	✅ Synchronous
File Upload	MultipartFile	✅ Supported
PDF Extraction	Apache PDFBox 3.0.6	✅ Working
AI Analysis	LangChain4j + GPT-4	✅ Working
RAG Chat	Embeddings + Vector Search	✅ Working
Build	Maven	✅ BUILD SUCCESS
🏗️ Application Architecture
Technology Stack
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
│ - ContractAnalysisAssistant             │
│ - ContractChatAssistant (RAG)           │
└──────────────┬──────────────────────────┘
               ↓
┌─────────────────────────────────────────┐
│ External Services                       │
│ - OpenAI GPT-4o (analysis & chat)       │
│ - OpenAI text-embedding-3-large         │
└─────────────────────────────────────────┘

🔄 Data Flow
Contract Analysis Flow
PDF File → MultipartFile → byte[] → PDFBox → String
    ↓
LangChain4j → OpenAI GPT-4 → JSON Response
    ↓
ContractAnalysis → Client

RAG Chat Flow
Contract Text → Document Splitter (500 chars)
    ↓
OpenAI Embeddings → In-memory Vector Store
    ↓
User Question → Embedding → Similarity Search (Top 3)
    ↓
Context + Question → GPT-4 → Answer

📂 Project Structure
src/main/java/com/ai/_ojo/
├── Application.java
├── ai/
│   ├── AiConfig.java
│   ├── ContractAnalysisAssistant.java
│   └── ContractChatAssistant.java
├── controller/
│   └── ContractController.java
├── service/
│   ├── ContractAnalysisService.java
│   ├── ContractChatService.java
│   └── ContractChatServiceFactory.java
├── domain/
│   ├── ContractAnalysis.java
│   ├── Parties.java
│   ├── KeyDates.java
│   ├── FinancialTerms.java
│   ├── KeyClause.java
│   ├── ClauseType.java
│   └── RiskLevel.java
├── dto/
│   ├── ChatRequest.java
│   └── ChatResponse.java
└── pdfextractor/
    └── PdfExtractor.java

🔌 API Endpoints
POST /api/contracts/analyze

Description:
Uploads and analyzes a contract PDF, extracting structured information.

Request

POST http://localhost:1505/api/contracts/analyze
Content-Type: multipart/form-data

file: contract.pdf


Response (200 OK)

{
  "contractType": "Service Agreement",
  "summary": "This contract establishes...",
  "parties": {
    "partyA": "Company A Ltd.",
    "partyB": "Tech Provider Inc."
  },
  "keyDates": {
    "effectiveDate": "2024-01-01",
    "expirationDate": "2026-12-31"
  },
  "duration": "3 years",
  "financialTerms": {
    "totalAmount": 500000,
    "currency": "USD",
    "paymentSchedule": "Quarterly"
  },
  "keyClauses": [
    {
      "type": "TERMINATION",
      "summary": "Either party may terminate with 60 days notice",
      "riskLevel": "MEDIUM"
    }
  ],
  "specialNotes": [
    "Subject to annual review"
  ]
}

POST /api/contracts/chat

Description:
Ask questions about the previously analyzed contract.

Request

POST http://localhost:1505/api/contracts/chat
Content-Type: application/json

{
  "question": "What are the provider obligations?"
}


Response

{
  "answer": "According to the contract, the provider must offer 24/7 support, ensure a maximum response time of 4 hours for critical incidents, perform monthly security updates, and deliver quarterly performance reports."
}

🎓 Technical Concepts Implemented
Dependency Injection
public ContractController(
    ContractAnalysisService analysisService,
    ContractChatServiceFactory chatServiceFactory
) {
    this.analysisService = analysisService;
    this.chatServiceFactory = chatServiceFactory;
}

LangChain4j AI Services
@UserMessage("""
Analyze the following contract and return the result as JSON.
Contract text: {{contractText}}
""")
ContractAnalysis analyze(String contractText);

RAG Pipeline
List<TextSegment> segments =
    DocumentSplitters.recursive(500, 50).split(document);

PDF Processing
try (PDDocument document = Loader.loadPDF(bytes)) {
    PDFTextStripper stripper = new PDFTextStripper();
    return stripper.getText(document);
}

💰 Estimated OpenAI Costs
Contract Analysis (GPT-4)

Input ~5,000 tokens → $0.15

Output ~500 tokens → $0.015
Total: ~$0.165 per contract

Chat Question (RAG)

Embeddings → ~$0.0065

GPT-4 input/output → ~$0.063
Total: ~$0.07 per question

Optimizations

GPT-4o-mini for chat

Cache analysis results

Avoid re-embedding identical documents

🚀 How to Run
1. Set OpenAI API Key
$env:OPENAI_API_KEY="sk-your-openai-key"

2. Start Application
.\iniciar-app.ps1

3. Verify Startup
Tomcat started on port(s): 1505 (http)
Started Application in X seconds
