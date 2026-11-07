package pl.easyban;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

/** GUI ustawień (domyślnie klawisz N). */
public class ConfigScreen extends Screen {
    private static final int PW = 320;          // szerokość panelu
    private static final int PH = 252;          // wysokość panelu
    private static final int PAD = 12;
    private static final int INNER = PW - PAD * 2;
    private static final int PREVIEW_W = 240;
    private static final int GAP = 10;

    private final EasyBanConfig cfg = EasyBanConfig.get();
    private int px, py;
    private boolean showPreview;

    public ConfigScreen() {
        super(Text.literal("EasyBan"));
    }

    @Override
    protected void init() {
        showPreview = width >= PW + GAP + PREVIEW_W + 20;
        int total = showPreview ? PW + GAP + PREVIEW_W : PW;
        px = (width - total) / 2;
        py = Math.max(4, (height - PH) / 2);

        int x = px + PAD;
        int half = (INNER - 8) / 2;
        int x2 = x + half + 8;

        addField(x, py + 62, INNER, cfg.reason, s -> cfg.reason = s);
        addField(x, py + 96, half, cfg.bannedBy, s -> cfg.bannedBy = s);
        addField(x2, py + 96, half, cfg.duration, s -> cfg.duration = s);
        addField(x, py + 130, 190, cfg.extra, s -> cfg.extra = s);
        addField(x + 198, py + 130, INNER - 198, cfg.banId, s -> cfg.banId = s);

        addDrawableChild(new FlatButton(x, py + 162, half, 20, FlatButton.Style.SWITCH,
                () -> Text.literal("Włącz mod"), () -> cfg.enabled,
                b -> cfg.enabled = !cfg.enabled));

        addDrawableChild(new FlatButton(x2, py + 162, half, 20, FlatButton.Style.SWITCH,
                () -> Text.literal("Rozłącz z serwera"), () -> cfg.disconnect,
                b -> cfg.disconnect = !cfg.disconnect));

        addDrawableChild(new FlatButton(x, py + 186, half, 20, FlatButton.Style.NORMAL,
                () -> Text.literal("Język bana: " + (cfg.polish ? "Polski" : "English")), null,
                b -> cfg.polish = !cfg.polish));

        addDrawableChild(new FlatButton(x2, py + 186, half, 20, FlatButton.Style.NORMAL,
                () -> Text.literal("Zbanuj mnie (test)"), null,
                b -> EasyBanClient.triggerBan(client)));

        addDrawableChild(new FlatButton(x, py + 214, INNER, 20, FlatButton.Style.PRIMARY,
                () -> Text.literal("Zapisz i zamknij"), null,
                b -> close()));
    }

    private void addField(int x, int y, int w, String value, Consumer<String> setter) {
        TextFieldWidget f = new TextFieldWidget(textRenderer, x, y, w, 18, Text.empty());
        f.setMaxLength(120);
        f.setText(value == null ? "" : value);
        f.setChangedListener(setter);
        addDrawableChild(f);
    }

    private void label(DrawContext ctx, String text, int x, int y) {
        ctx.drawTextWithShadow(textRenderer, text, x, y, Theme.MUTED);
    }

    @Override
    public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.renderBackground(ctx, mouseX, mouseY, delta);

        Theme.card(ctx, px, py, PW, PH);

        // Nagłówek
        MatrixStack m = ctx.getMatrices();
        m.push();
        m.translate((float) (px + PAD), (float) (py + 10), 0f);
        m.scale(2f, 2f, 1f);
        ctx.drawTextWithShadow(textRenderer, "Easy", 0, 0, Theme.TEXT);
        ctx.drawTextWithShadow(textRenderer, "Ban", textRenderer.getWidth("Easy"), 0, Theme.ACCENT);
        m.pop();
        ctx.drawTextWithShadow(textRenderer, "Fałszywy ban widoczny tylko dla Ciebie", px + PAD, py + 30, Theme.MUTED);
        Theme.rect(ctx, px + PAD, py + 43, INNER, 1, Theme.PANEL_BORDER);

        // Podpisy pól
        int x = px + PAD;
        int half = (INNER - 8) / 2;
        int x2 = x + half + 8;
        label(ctx, "Powód bana", x, py + 52);
        label(ctx, "Zbanowany przez (nick)", x, py + 86);
        label(ctx, "Czas (np. 7 dni, perm)", x2, py + 86);
        label(ctx, "Dodatkowa linijka (np. link)", x, py + 120);
        label(ctx, "Ban ID", x + 198, py + 120);

        Theme.rect(ctx, px + PAD, py + 157, INNER, 1, Theme.PANEL_BORDER);

        // Podpowiedź o klawiszach
        Text hint = Text.literal("GUI: ").append(EasyBanClient.openGuiKey.getBoundKeyLocalizedText())
                .append(Text.literal("   •   Ban: "))
                .append(EasyBanClient.banKey.getBoundKeyLocalizedText());
        ctx.drawCenteredTextWithShadow(textRenderer, hint, px + PW / 2, py + PH - 12, Theme.MUTED);

        if (showPreview) {
            drawPreview(ctx);
        }
    }

    private void drawPreview(DrawContext ctx) {
        int x = px + PW + GAP;
        Theme.card(ctx, x, py, PREVIEW_W, PH);
        ctx.drawTextWithShadow(textRenderer, "Podgląd", x + PAD, py + 12, Theme.ACCENT);
        ctx.drawTextWithShadow(textRenderer, "Tak zobaczysz ekran bana", x + PAD, py + 24, Theme.MUTED);
        Theme.rect(ctx, x + PAD, py + 36, PREVIEW_W - PAD * 2, 1, Theme.PANEL_BORDER);

        int y = py + 46;
        int maxY = py + PH - 14;
        for (Text l : BanMessage.build(cfg)) {
            List<OrderedText> parts = textRenderer.wrapLines(l, PREVIEW_W - PAD * 2);
            if (parts.isEmpty()) {
                y += textRenderer.fontHeight;
                continue;
            }
            for (OrderedText p : parts) {
                if (y > maxY) return;
                ctx.drawTextWithShadow(textRenderer, p, x + PAD, y, Theme.TEXT);
                y += textRenderer.fontHeight + 1;
            }
        }
    }

    @Override
    public void removed() {
        EasyBanConfig.save();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
