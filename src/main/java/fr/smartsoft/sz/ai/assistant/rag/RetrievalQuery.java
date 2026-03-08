package fr.smartsoft.sz.ai.assistant.rag;

public record RetrievalQuery(
        String query,
        int topK
) {
}
