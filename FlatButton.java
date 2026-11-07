package pl.easyban;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/** Płaski, ciemny przycisk: zwykły, główny (czerwony) albo przełącznik z suwakiem. */
public class FlatButton extends ButtonWidget {
    public enum Style { PRIMARY, NORMAL, SWITCH }

    private final Style style;
    private final Supplier<Text> label;
    private final BooleanSupplier on;

    public FlatButton(int x, int y, int w, int h, Style style, Supplier<Text> label,
                      BooleanSupplier on, ButtonWidget.PressAction action) {
        super(x, y, w, h, label.get(), action, DEFAULT_NARRATION_SUPPLIER);
        this.style = style;
        this.label = label;
        this.on = on;
    }

    @Override
    public void renderWidget(DrawContext ctx, int mouseX, int mouseY, float delta) {
        setMessage(label.get());
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        int x = getX(), y = getY(), w = getWidth(), h = getHeight();
        boolean hover = isHovered();

        int fill;
        int border;
        if (style == Style.PRIMARY) {
            fill = hover ? Theme.ACCENT_HOVER : Theme.ACCENT;
            border = Theme.ACCENT;
        } else {
            fill = hover ? Theme.BTN_HOVER : Theme.BTN;
            border = hover ? Theme.MUTED : Theme.BTN_BORDER;
        }
        Theme.box(ctx, x, y, w, h, fill, border);

        int textY = y + (h - tr.fontHeight) / 2 + 1;
        if (style == Style.SWITCH) {
            ctx.drawTextWithShadow(tr, getMessage(), x + 8, textY, Theme.TEXT);
            boolean state = on != null && on.getAsBoolean();
            int tw = 22, th = 10;
            int tx = x + w - 8 - tw;
            int ty = y + (h - th) / 2;
            Theme.rect(ctx, tx, ty, tw, th, state ? Theme.GREEN : Theme.OFF);
            int kx = state ? tx + tw - th + 1 : tx + 1;
            Theme.rect(ctx, kx, ty + 1, th - 2, th - 2, 0xFFFFFFFF);
        } else {
            ctx.drawCenteredTextWithShadow(tr, getMessage(), x + w / 2, textY, Theme.TEXT);
        }
    }
}
