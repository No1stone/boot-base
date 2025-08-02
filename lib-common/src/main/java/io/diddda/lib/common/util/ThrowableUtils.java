package io.diddda.lib.common.util;

public class ThrowableUtils {

    public static <T extends Throwable> T findCauseByType(Throwable throwable, Class<T> targetExceptionType, int maxDepth) {
        Throwable cause = throwable.getCause();
        int depth = 0;
        while (cause != null && depth < maxDepth) {
            if (targetExceptionType.isInstance(cause)) {
                return targetExceptionType.cast(cause);
            }
            cause = cause.getCause();
            depth++;
        }
        return null;
    }
}