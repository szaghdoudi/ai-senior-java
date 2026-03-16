package fr.smartsoft.sz.ai.assistant.rag.impl;

import fr.smartsoft.sz.ai.assistant.rag.EmbeddingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
@Component
public class SimpleEmbeddedProvider implements EmbeddingProvider {

    private final int dimension;

    public SimpleEmbeddedProvider(@Value("${rag.embedding.dim}") int dimension) {
        this.dimension = dimension;
    }

    @Override
    public Mono<float[]> embed(String text) {
        if (!StringUtils.hasText(text)) {
            return Mono.just(new float[0]);
        }
        //Placeholder embedding for MVP wiring
        float[] vec = new float[dimension];
        int i = 0;
        for (char c : text.toCharArray()) {
            vec[i % vec.length] += c;
            i++;
        }
        return Mono.just(vec);
    }
}
