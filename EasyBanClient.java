package pl.easyban;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class EasyBanClient implements ClientModInitializer {
    public static KeyBinding openGuiKey;
    public static KeyBinding banKey;

    @Override
    public void onInitializeClient() {
        EasyBanConfig.load();

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.easyban.open_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.categories.easyban"));
        banKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.easyban.ban", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_ALT, "key.categories.easyban"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ConfigScreen());
                }
            }
            while (banKey.wasPressed()) {
                if (EasyBanConfig.get().enabled) {
                    triggerBan(client);
                }
            }
        });
    }

    /** Pokazuje fałszywy ban – tylko lokalnie, serwer o niczym nie wie. */
    public static void triggerBan(MinecraftClient client) {
        EasyBanConfig cfg = EasyBanConfig.get();
        List<Text> lines = BanMessage.build(cfg);

        if (cfg.disconnect && client.getNetworkHandler() != null && !client.isInSingleplayer()) {
            // Zamyka połączenie po stronie klienta -> pojawia się zwykły ekran rozłączenia z naszym tekstem
            client.getNetworkHandler().getConnection().disconnect(BanMessage.join(lines));
        } else {
            // Sam ekran bana nałożony na grę, bez wychodzenia z serwera / świata
            client.setScreen(new BanScreen(client.currentScreen, lines, cfg.polish));
        }
    }
}
