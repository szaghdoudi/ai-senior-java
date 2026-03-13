package fr.smartsoft.sz.ai.assistant.rag.impl;

import fr.smartsoft.sz.ai.assistant.rag.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultRetrievalServiceTest {

    @Mock
    private EmbeddingProvider embeddingProvider;

    @Mock
    private VectorStore vectorStore;

    private DefaultRetrievalService service;

    @BeforeEach
    void setUp() {
        service = new DefaultRetrievalService(embeddingProvider, vectorStore);
    }

    @Test
    void shouldEmbedSearchAndMapToRetrievedChunks() {
        // given
        String question = "timeout provider retries";
        float[] emb = new float[]{0.1f, 0.2f, 0.3f};

        List<VectorSearchMatch> matches = List.of(
                new VectorSearchMatch(
                        "rb-001",
                        "LLM provider down / timeouts",
                        "rb-001-c1",
                        "verify timeout and retries",
                        0.91,
                        SourceType.MARKDOWN,
                        "docs/runbooks/RB-001-llm-provider-down.md"
                )
        );

        when(embeddingProvider.embed(question)).thenReturn(Mono.just(emb));
        when(vectorStore.search(emb, 3)).thenReturn(Mono.just(matches));

        // when
        List<RetrievedChunk> out = service.retrieve(new RetrievalQuery(question, 3)).block();

        // then
        assertNotNull(out);
        assertEquals(1, out.size());

        RetrievedChunk c = out.getFirst();
        assertEquals("rb-001", c.docId());
        assertEquals("rb-001-c1", c.chunkId());
        assertEquals(0.91, c.score(), 1e-9);
        assertEquals(SourceType.MARKDOWN, c.sourceType());

        verify(embeddingProvider, times(1)).embed(question);
        verify(vectorStore, times(1)).search(emb, 3);
    }

    @Test
    void shouldReturnEmptyAndSkipDependenciesWhenQuestionIsBlank() {
        // when
        List<RetrievedChunk> out = service.retrieve(new RetrievalQuery("   ", 5)).block();

        // then
        assertNotNull(out);
        assertTrue(out.isEmpty());
        verifyNoInteractions(embeddingProvider, vectorStore);
    }

    @Test
    void shouldClampTopKToMin1() {
        // given
        float[] emb = new float[]{1f};
        when(embeddingProvider.embed("q")).thenReturn(Mono.just(emb));
        when(vectorStore.search(any(), anyInt())).thenReturn(Mono.just(List.of()));

        // when
        service.retrieve(new RetrievalQuery("q", 0)).block();

        // then
        ArgumentCaptor<Integer> kCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(vectorStore).search(eq(emb), kCaptor.capture());
        assertEquals(1, kCaptor.getValue());
    }

    @Test
    void shouldClampTopKToMax10() {
        // given
        float[] emb = new float[]{1f};
        when(embeddingProvider.embed("q")).thenReturn(Mono.just(emb));
        when(vectorStore.search(any(), anyInt())).thenReturn(Mono.just(List.of()));

        // when
        service.retrieve(new RetrievalQuery("q", 99)).block();

        // then
        ArgumentCaptor<Integer> kCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(vectorStore).search(eq(emb), kCaptor.capture());
        assertEquals(10, kCaptor.getValue());
    }
}
