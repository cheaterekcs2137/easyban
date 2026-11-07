# EasyBan 2.0 (Fabric 1.21.4)

Kliencki mod – fałszywy ban widoczny tylko dla Ciebie.

- **N** – ustawienia (powód, kto zbanował, czas, ban ID, dodatkowa linijka, język, włącz/wyłącz mod) z podglądem na żywo
- **Prawy Alt** – pokazuje ban
- Klawisze można zmienić w Opcje > Sterowanie > EasyBan
- Config: `config/easyban.json`

## Budowanie
Potrzebne: Java 21 + Gradle 8.12+ (albo GitHub Actions z `.github/workflows/build.yml`).

    gradle build

Gotowy plik: `build/libs/easyban-2.0.0.jar` -> wrzuć do folderu `mods`.
Wymaga: Minecraft 1.21.4, Fabric Loader 0.16+, Fabric API.
