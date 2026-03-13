CREATE
EXTENSION IF NOT EXISTS vector;


CREATE TABLE rag_chunks
(
    id          BIGSERIAL PRIMARY KEY,
    doc_id      VARCHAR(128) NOT NULL,
    doc_title   VARCHAR(512) NOT NULL,
    chunk_id    VARCHAR(128) NOT NULL UNIQUE,
    content     TEXT         NOT NULL,
    embedding   vector(768) NOT NULL,
    source_type VARCHAR(32)  NOT NULL,
    source_url  TEXT,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_rag_chunks_doc_id ON rag_chunks (doc_id);
CREATE INDEX idx_rag_chunks_source_type ON rag_chunks (source_type);

-- ivfflat is common for pgvector ANN search
CREATE INDEX idx_rag_chunks_embedding_ivfflat
    ON rag_chunks USING ivfflat (embedding vector_cosine_ops)
    WITH (lists = 100);