# API Contract — AI Dev Assistant

## Endpoint: Ask
**POST** `/api/ai/ask`

### Request (JSON)
```json
{
  "question": "string",
  "context": {
    "project": "optional string",
    "service": "optional string",
    "environment": "optional string"
  },
  "options": {
    "useRag": true,
    "topK": 5,
    "temperature": 0.2,
    "maxTokens": 800
  }
}
```
### Response (JSON)
```json
{
  "answer": "string",
  "citations": [
    {
      "docId": "string",
      "docTitle": "string",
      "chunkId": "string",
      "score": 0.0,
      "sourceType": "CONFLUENCE|GIT|PDF|MARKDOWN|LOG",
      "sourceUrl": "optional string"
    }
  ],
  "meta": {
    "requestId": "string",
    "model": "string",
    "provider": "string",
    "ragUsed": true,
    "retrieval": {
      "topK": 5
    },
    "timingsMs": {
      "retrieval": 0,
      "llm": 0,
      "total": 0
    },
    "cost": {
      "inputTokens": 0,
      "outputTokens": 0,
      "totalTokens": 0,
      "estimatedUsd": 0.0
    }
  }
}
```
### Error response (JSON)
```json
{
  "error": {
    "code": "INVALID_REQUEST|RATE_LIMITED|PROVIDER_DOWN|SECURITY_BLOCKED",
    "message": "string",
    "requestId": "string"
  }
}

