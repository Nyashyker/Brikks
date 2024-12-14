package brikks.essentials;

public class ASCIIColor extends Color {
    private final char color;


    public ASCIIColor(final char color) {
        this.color = color;
    }


    @Override
    public String get() {
        return String.valueOf(this.color);
    }

    public char getChar() {
        return this.color;
    }
}
