package fr.smartsoft.sz.ai.assistant.rag.pgvector;

import java.util.Locale;

public final class PgVectorFormat {

    private PgVectorFormat() {
    }

    public static String toLiteral(float[] vector) {
        if (vector == null || vector.length == 0) {
            throw new IllegalArgumentException("vector must not be empty");
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(String.format(Locale.ROOT, "%.8f", vector[i]));
        }
        sb.append(']');
        return sb.toString();
    }
}