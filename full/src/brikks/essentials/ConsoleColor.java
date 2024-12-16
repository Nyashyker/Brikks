package brikks.essentials;

public class ConsoleColor extends Color {
    private final String color;

    public ConsoleColor(String color) {
        this.color = color;
    }

    @Override
    public String get() {
        return this.color;
    }
}
