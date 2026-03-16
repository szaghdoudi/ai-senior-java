package fr.smartsoft.sz.ai.assistant.rag.ingestion.impl;

import fr.smartsoft.sz.ai.assistant.rag.SourceType;
import fr.smartsoft.sz.ai.assistant.rag.ingestion.DocumentLoader;
import fr.smartsoft.sz.ai.assistant.rag.ingestion.RagDocument;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class MarkdownDocumentLoader implements DocumentLoader {
    private final Path docsRoot = Path.of("docs");

    @Override
    public Flux<RagDocument> loadAll() {
        return Flux.defer(() -> {
            try (Stream<Path> paths = Files.walk(docsRoot)) {
                var documents = paths
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".md"))
                        .map(this::toDocument)
                        .toList();
                return Flux.fromIterable(documents);
            } catch (IOException e) {
                return Flux.error(new IllegalArgumentException("Failed to load markdown documents", e));
            }

        });
    }

    private RagDocument toDocument(Path path) {
        try {
            String content = Files.readString(path);
            String relative = docsRoot.relativize(path).toString().replace('\\', '/');
            String title = extractTitle(content, path);
            return new RagDocument(
                    relative,
                    title,
                    content,
                    SourceType.MARKDOWN,
                    "docs/" + relative
            );

        } catch (IOException e) {
            throw new IllegalStateException("failed to read document: " + path, e);
        }
    }

    private String extractTitle(String content, Path path) {
        return content.lines()
                .filter(line -> line.startsWith("# "))
                .map(line -> line.substring(2).trim())
                .findFirst()
                .orElse(path.getFileName().toString());
    }
}
