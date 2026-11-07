package pl.easyban;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/** Ekran bana nałożony na grę (tryb bez rozłączania z serwera). */
public class BanScreen extends Screen {
    private final Screen parent;
    private final List<Text> lines;
    private final boolean polish;
    private final List<OrderedText> wrapped = new ArrayList<>();
    private int cardX, cardY, cardW, cardH, titleY, textY;

    public BanScreen(Screen parent, List<Text> lines, boolean polish) {
        super(Text.literal(polish ? "Rozłączono z serwerem" : "Disconnected"));
        this.parent = parent;
        this.lines = lines;
        this.polish = polish;
    }

    @Override
    protected void init() {
        cardW = Math.min(width - 20, 380);
        int innerW = cardW - 40;

        wrapped.clear();
        for (Text l : lines) {
            List<OrderedText> parts = textRenderer.wrapLines(l, innerW);
            if (parts.isEmpty()) wrapped.add(OrderedText.EMPTY);
            else wrapped.addAll(parts);
        }
        int lineH = textRenderer.fontHeight + 1;
        int textH = wrapped.size() * lineH;

        cardH = 103 + textH;
        cardX = (width - cardW) / 2;
        cardY = Math.max(4, (height - cardH) / 2);
        titleY = cardY + 19;
        textY = titleY + 30;
        int btnY = textY + textH + 18;

        addDrawableChild(new FlatButton(cardX + cardW / 2 - 60, btnY, 120, 20,
                FlatButton.Style.PRIMARY,
                () -> Text.literal(polish ? "Wróć" : "Back"),
                null,
                b -> close()));
    }

    @Override
    public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.renderBackground(ctx, mouseX, mouseY, delta);
        Theme.rect(ctx, 0, 0, width, height, 0x88000000);
        Theme.card(ctx, cardX, cardY, cardW, cardH);

        MatrixStack m = ctx.getMatrices();
        m.push();
        m.translate((float) (width / 2), (float) titleY, 0f);
        m.scale(2f, 2f, 1f);
        ctx.drawCenteredTextWithShadow(textRenderer, title, 0, 0, Theme.TEXT);
        m.pop();

        int y = textY;
        for (OrderedText l : wrapped) {
            ctx.drawCenteredTextWithShadow(textRenderer, l, width / 2, y, Theme.TEXT);
            y += textRenderer.fontHeight + 1;
        }
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
