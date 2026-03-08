package fr.smartsoft.sz.ai.assistant.rag.impl;

import fr.smartsoft.sz.ai.assistant.rag.RetrievalQuery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRetrievalServiceTest {

    private final InMemoryRetrievalService service = new InMemoryRetrievalService();

    @Test
    void shouldReturnTopKRankedChunks() {
        var result = service.retrieve(new RetrievalQuery("timeout provider retries", 2)).block();

        assertNotNull(result);
        assertTrue(result.size() <= 2);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).score() >= result.get(result.size() - 1).score());
    }

    @Test
    void shouldReturnEmptyForBlankQuestion() {
        var result = service.retrieve(new RetrievalQuery("   ", 3)).block();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
