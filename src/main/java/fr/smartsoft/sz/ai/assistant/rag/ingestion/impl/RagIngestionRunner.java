package fr.smartsoft.sz.ai.assistant.rag.ingestion.impl;

import fr.smartsoft.sz.ai.assistant.rag.ingestion.RagIngestionService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class RagIngestionRunner {

    private static final Logger log = LoggerFactory.getLogger(RagIngestionRunner.class);
    @Bean
    @Profile("rag-ingest")
    ApplicationRunner runRagIngestion(RagIngestionService ingestionService) {
        return args -> {
            Integer count = ingestionService.ingestAll().block();
            log.info("Indexed {} chunks into rag_chunks", count);
        };

    }

}
