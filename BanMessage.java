package pl.easyban;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Składa tekst fałszywego bana z ustawień. */
public final class BanMessage {
    private BanMessage() {}

    public static boolean isPermanent(String duration) {
        if (duration == null) return true;
        String d = duration.trim().toLowerCase(Locale.ROOT);
        return d.isEmpty() || d.startsWith("perm") || d.equals("na zawsze") || d.equals("forever");
    }

    private static boolean has(String s) {
        return s != null && !s.isBlank();
    }

    private static Text line(String label, String value) {
        return Text.literal(label).formatted(Formatting.GRAY)
                .append(Text.literal(value).formatted(Formatting.WHITE));
    }

    public static List<Text> build(EasyBanConfig c) {
        boolean pl = c.polish;
        boolean perm = isPermanent(c.duration);
        List<Text> out = new ArrayList<>();

        String title;
        if (pl) title = perm ? "Zostałeś permanentnie zbanowany na tym serwerze!" : "Zostałeś zbanowany na tym serwerze!";
        else title = perm ? "You are permanently banned from this server!" : "You are banned from this server!";
        out.add(Text.literal(title).formatted(Formatting.RED));
        out.add(Text.empty());

        String reason = has(c.reason) ? c.reason : (pl ? "Nie podano" : "No reason given");
        out.add(line(pl ? "Powód: " : "Reason: ", reason));

        if (has(c.bannedBy)) {
            out.add(line(pl ? "Zbanowany przez: " : "Banned by: ", c.bannedBy));
        }

        String time = perm ? (pl ? "Permanentnie" : "Permanent") : c.duration;
        out.add(line(pl ? "Czas: " : "Duration: ", time));

        if (has(c.banId)) {
            out.add(line("Ban ID: ", c.banId));
        }

        if (has(c.extra)) {
            out.add(Text.empty());
            out.add(Text.literal(c.extra).formatted(Formatting.YELLOW));
        }
        return out;
    }

    /** Skleja linie w jeden tekst z \n (do ekranu rozłączenia). */
    public static Text join(List<Text> lines) {
        MutableText out = Text.empty();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) out.append(Text.literal("\n"));
            out.append(lines.get(i));
        }
        return out;
    }
}
