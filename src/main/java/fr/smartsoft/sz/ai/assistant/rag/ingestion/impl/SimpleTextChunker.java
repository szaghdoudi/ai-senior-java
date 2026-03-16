package fr.smartsoft.sz.ai.assistant.rag.ingestion.impl;

import fr.smartsoft.sz.ai.assistant.rag.ingestion.ChunkedDocument;
import fr.smartsoft.sz.ai.assistant.rag.ingestion.RagDocument;
import fr.smartsoft.sz.ai.assistant.rag.ingestion.TextChunker;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class SimpleTextChunker implements TextChunker {
    private static final int MAX_CHARS = 800;

    @Override
    public List<ChunkedDocument> chunk(RagDocument document) {
        if (document == null || !StringUtils.hasText(document.content())) {
            return List.of();
        }

        List<String> rawBlocks = splitParagraphs(document.content());
        List<ChunkedDocument> chunks = new ArrayList<>();
        int index = 0;
        for (String block : rawBlocks) {
            if (block.length() <= MAX_CHARS) {
                chunks.add(toChunk(document, index++, block));
                continue;
            }
            for (String part : splitLargeBlock(block, MAX_CHARS)) {
                chunks.add(toChunk(document, index++, part));
            }
        }
        return chunks;
    }

    private List<String> splitParagraphs(String content) {
        String[] parts = content.split("\\R\\s*\\R");
        List<String> blocks = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isBlank()) {
                blocks.add(trimmed);
            }
        }
        return blocks;
    }

    private List<String> splitLargeBlock(String block, int maxChars) {
        List<String> out = new ArrayList<>();
        int start = 0;
        while (start < block.length()) {
            int end = Math.min(start + maxChars, block.length());
            out.add(block.substring(start, end).trim());
            start = end;
        }
        return out;
    }

    private ChunkedDocument toChunk(RagDocument document, int index, String block) {
        return new ChunkedDocument(
                document.docId(),
                document.title(),
                document.docId() + "#chunk-" + index,
                block.trim(),
                document.sourceType(),
                document.sourceUrl()
        );
    }


}
