package pl.easyban;

import net.minecraft.client.gui.DrawContext;

/** Kolory i proste funkcje rysujące wspólne dla wszystkich ekranów moda. */
public final class Theme {
    private Theme() {}

    public static final int ACCENT = 0xFFE5484D;
    public static final int ACCENT_HOVER = 0xFFF26065;
    public static final int PANEL = 0xF5101319;
    public static final int PANEL_BORDER = 0xFF2B303B;
    public static final int BTN = 0xFF232733;
    public static final int BTN_HOVER = 0xFF2F3544;
    public static final int BTN_BORDER = 0xFF3A4150;
    public static final int TEXT = 0xFFFFFFFF;
    public static final int MUTED = 0xFF8B93A5;
    public static final int GREEN = 0xFF3FB950;
    public static final int OFF = 0xFF4A5160;

    public static void rect(DrawContext c, int x, int y, int w, int h, int color) {
        c.fill(x, y, x + w, y + h, color);
    }

    /** Wypełniony prostokąt z 1px obramowaniem. */
    public static void box(DrawContext c, int x, int y, int w, int h, int fill, int border) {
        rect(c, x, y, w, h, fill);
        rect(c, x, y, w, 1, border);
        rect(c, x, y + h - 1, w, 1, border);
        rect(c, x, y, 1, h, border);
        rect(c, x + w - 1, y, 1, h, border);
    }

    /** Ciemna karta z cieniem i czerwonym paskiem u góry. */
    public static void card(DrawContext c, int x, int y, int w, int h) {
        rect(c, x + 3, y + 3, w, h, 0x66000000);
        box(c, x, y, w, h, PANEL, PANEL_BORDER);
        rect(c, x, y, w, 2, ACCENT);
    }
}
