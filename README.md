[CZ verze]([url](https://github.com/sepeca/winter_house_3D/blob/main/README_CZ.md)) 
# ❄️ Winter Scene 3D
An interactive 3D simulation of a winter landscape featuring dynamic snowfall and snow accumulation. The project was built in Java using the LWJGL (OpenGL) library as a semester project (University of Hradec Králové 2025/2026).
<img width="800" height="419" alt="2026-05-1716-19-38-ezgif com-video-to-gif-converter (2)" src="https://github.com/user-attachments/assets/57e9d491-4550-4093-a6d8-6d7a2de36144" />
<img width="684" height="360" alt="2026-05-1716-20-25-ezgif com-cut" src="https://github.com/user-attachments/assets/ccff1def-0210-425b-989b-8999933d6eba" />
## ✨ Key Features

* **Dynamic Particle System:** Real-time simulation of up to 5,000 falling snowflakes.
* **Snow Accumulation and Physics:** Snow realistically accumulates on the ground and on the roof of the house model. It features a recursive algorithm to simulate small avalanches if the snow layer becomes too steep.
* **Procedural Noise (Noise Map):** To achieve an organic look, the snow cover is generated using a noise map that adds minor bumps (bump mapping) and natural sparkle to the surface.
* **VBO Optimization:** Rendering the massive terrain grid (600x600 cells, hundreds of thousands of vertices) is fully hardware-accelerated using Vertex Buffer Objects (VBO), ensuring a stable and high framerate (FPS).
* **Free Camera:** Free movement through the scene (First-Person) using the keyboard and mouse.
* **Texturing:** Includes a full spherical Skybox and 2D texture mapping on 3D objects.

## 🛠 Technologies Used

* **Java 21**
* **LWJGL 3** (Lightweight Java Game Library) - bindings for OpenGL, GLFW (window and input management), and STB (image loading).
* **JOML** (Java OpenGL Math Library) - calculations for transformation and projection matrices.
* **Maven** - dependency management and project building.

## 🎮 Controls

The following controls are available when the application is running:

* **W, S, A, D** - Camera movement (forward, backward, strafe)
* **Space / Left Shift** - Camera movement up / down
* **Mouse** - Look around the scene
* **Arrows (← → ↑ ↓)** - Move the snow cloud in space
* **I / O** - Change the maximum number of falling snowflakes
* **K / L** - Change the size (radius) of the snow cloud
* **ESC** - Exit application

## 🚀 How to Run the Project

### Using an IDE (IntelliJ IDEA / Eclipse)
1. Clone the repository.
2. Open the project and reload Maven dependencies (Load Maven Changes).
3. Run the main class `org.Sergei_Suprunov.App`
