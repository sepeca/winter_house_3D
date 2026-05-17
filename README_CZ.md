# ❄️ Zimní Scéna 3D (Winter Scene 3D)
<img width="800" height="419" alt="2026-05-1716-19-38-ezgif com-video-to-gif-converter (2)" src="https://github.com/user-attachments/assets/57e9d491-4550-4093-a6d8-6d7a2de36144" />
<img width="684" height="360" alt="2026-05-1716-20-25-ezgif com-cut" src="https://github.com/user-attachments/assets/ccff1def-0210-425b-989b-8999933d6eba" />

Interaktivní 3D simulace zimní krajiny s dynamickým padáním a hromaděním sněhu. Projekt byl vytvořen v jazyce Java s využitím knihovny LWJGL (OpenGL) jako semestrální práce (UHK 2025/2026).

## ✨ Klíčové vlastnosti

* **Dynamický částicový systém:** Simulace až 5000 padajících sněhových vloček v reálném čase.
* **Akumulace a fyzika sněhu:** Sníh se realisticky hromadí na zemi i na střeše modelu domu. Obsahuje rekurzivní algoritmus pro simulaci drobných sesuvů (lavin), pokud je vrstva sněhu příliš strmá.
* **Procedurální šum (Noise Map):** Pro dosažení organického vzhledu je sněhová pokrývka generována pomocí mapy šumu, která povrchu dodává drobné nerovnosti (Bump mapping) a přirozený třpyt.
* **Optimalizace pomocí VBO:** Vykreslování obrovské sítě terénu (600x600 buněk, statisíce vrcholů) je plně hardwarově akcelerováno pomocí Vertex Buffer Objects (VBO), což zajišťuje stabilní a vysoký počet snímků za sekundu (FPS).
* **Volná kamera:** Volný pohyb scénou (First-Person) pomocí klávesnice a myši.
* **Texturování:** Obsahuje plnohodnotný kulový Skybox a mapování 2D textur na 3D objekty.

## 🛠 Použité technologie

* **Java 21**
* **LWJGL 3** (Lightweight Java Game Library) - vazba na OpenGL, GLFW (správa oken a vstupů) a STB (načítání obrázků).
* **JOML** (Java OpenGL Math Library) - výpočty transformačních a projekčních matic.
* **Maven** - správa závislostí a sestavení projektu.

## 🎮 Ovládání

Při spuštění aplikace je k dispozici následující ovládání:

* **W, S, A, D** - Pohyb kamery (dopředu, dozadu, úkroky)
* **Mezerník / Levý Shift** - Pohyb kamery nahoru / dolů
* **Myš** - Rozhlížení se po scéně
* **Šipky (← → ↑ ↓)** - Pohyb sněžného mraku v prostoru
* **I / O** - Změna maximálního počtu padajících vloček
* **K / L** - Změna velikosti (poloměru) mraku
* **ESC** - Ukončení aplikace

## 🚀 Jak spustit projekt

### Pomocí IDE (IntelliJ IDEA / Eclipse)
1. Naklonujte si repozitář.
2. Otevřete projekt a aktualizujte Maven závislosti (Load Maven Changes).
3. Spusťte hlavní třídu `org.Sergei_Suprunov.App`.

