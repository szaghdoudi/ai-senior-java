package fr.smartsoft.sz.ai.assistant.support;

public final class Exceptions {

    private Exceptions() {
    }

    public static Throwable rootCause(Throwable t) {
        if (t == null) {
            return null;
        }

        Throwable result = t;
        while (result.getCause() != null && result.getCause() != result) {
            result = result.getCause();
        }
        return result;
    }

    public static String rootMessage(Throwable t) {
        Throwable root = rootCause(t);
        return root == null ? null : root.getMessage();
    }
}