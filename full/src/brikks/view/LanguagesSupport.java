package brikks.view;

public class LanguagesSupport {
    public static boolean isTrue(final String message) {
        return switch (prepareMessage(message)) {
            case "true", "t", "yes", "y", "так", "т", "1" -> true;
            default -> false;
        };
    }

    public static boolean isFalse(final String message) {
        return switch (prepareMessage(message)) {
            case "false", "f", "no", "n", "ні", "н", "0" -> true;
            default -> false;
        };
    }

    private static String prepareMessage(final String message) {
        return message.strip().toLowerCase();
    }
}
