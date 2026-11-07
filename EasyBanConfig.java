package pl.easyban;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

/** Ustawienia zapisywane w config/easyban.json */
public class EasyBanConfig {
    public boolean enabled = true;      // czy Prawy Alt w ogóle działa
    public boolean disconnect = true;   // czy rozłączać z serwera (realistyczny ban)
    public boolean polish = true;       // język komunikatu bana

    public String reason = "Cheating";
    public String bannedBy = "Admin";
    public String duration = "perm";    // "perm" albo np. "7 dni"
    public String extra = "";           // dodatkowa linijka, np. link do odwołania
    public String banId = "";           // opcjonalne ID bana

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static EasyBanConfig instance = new EasyBanConfig();

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("easyban.json");
    }

    public static EasyBanConfig get() {
        return instance;
    }

    public static void load() {
        try {
            Path p = path();
            if (Files.exists(p)) {
                EasyBanConfig loaded = GSON.fromJson(Files.readString(p), EasyBanConfig.class);
                if (loaded != null) instance = loaded;
            } else {
                save();
            }
        } catch (Exception e) {
            System.err.println("[EasyBan] Nie udało się wczytać configu: " + e);
        }
    }

    public static void save() {
        try {
            Files.writeString(path(), GSON.toJson(instance));
        } catch (Exception e) {
            System.err.println("[EasyBan] Nie udało się zapisać configu: " + e);
        }
    }
}
