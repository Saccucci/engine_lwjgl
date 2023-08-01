package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
// Importações das classes necessárias
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop11 {

    public static void main(String[] args) {

        // Cria a janela de exibição
        DisplayManager.createDisplay();

        // Inicializa o loader, responsável por carregar os dados dos modelos e texturas
        Loader loader = new Loader();

        // Inicializa o shader estático, que é responsável por renderizar os modelos na tela
        StaticShader shader = new StaticShader();

        // Inicializa o renderizador, que é responsável por renderizar os objetos na tela
        Renderer renderer = new Renderer(shader);

        // Carrega os dados do modelo para o VAO (Vertex Array Object)
        // responsável por ler e interpretar o arquivo ".obj" (neste caso stall.obj) contendo as informações do modelo 3D. 
        // O resultado é uma estrutura de dados RawModel que contém as informações do modelo.
        RawModel model = OBJLoader.loadObjModel("dragon", loader);

        // Cria um modelo texturizado, que inclui o modelo e sua textura associada
        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("image")));

        // Cria uma entidade, que é uma instância do modelo texturizado em uma posição específica do mundo
        Entity entity = new Entity(staticModel, new Vector3f(0, -5, -50), 0, 0, 0, 1);

        // Cria uma luz, que será usada para iluminar o modelo
        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));

        // Cria uma câmera, que representa a visão do jogador no mundo 3D
        Camera camera = new Camera();

        // Loop principal do jogo, que executa até que a janela de exibição seja fechada
        while (!Display.isCloseRequested()) {
            // Rotaciona a entidade
            entity.increaseRotation(0, 1, 0);
            // Move a câmera
            camera.move();

            // Prepara o renderizador para a próxima renderização
            renderer.prepare();

            // Inicia o shader estático para renderizar o modelo
            shader.start();

            // Carrega a luz no shader para iluminar o modelo
            shader.loadLight(light);
            // Carrega a matriz de visão da câmera no shader
            shader.loadViewMatrix(camera);
            // Renderiza a entidade
            renderer.render(entity, shader);
            // Para o shader após renderização
            shader.stop();

            // Atualiza a janela de exibição
            DisplayManager.updateDisplay();
        }

        // Limpa o shader após o término do loop principal
        shader.cleanUp();
        // Limpa o loader após o término do loop principal
        loader.cleanUp();
        // Fecha a janela de exibição após o término do loop principal
        DisplayManager.closeDisplay();
    }
}
