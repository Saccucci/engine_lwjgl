package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class MainGameLoop13 {

    public static void main(String[] args) {

        // Cria a janela de exibição
        DisplayManager.createDisplay();

        // Inicializa o loader, responsável por carregar os dados dos modelos e texturas
        Loader loader = new Loader();

        // Carrega os dados do modelo para o VAO (Vertex Array Object)
        // responsável por ler e interpretar o arquivo ".obj" contendo as informações do modelo 3D. 
        // O resultado é uma estrutura de dados RawModel que contém as informações do modelo.
        RawModel model = OBJLoader.loadObjModel("Cube", loader);

        // Cria um modelo texturizado, que inclui o modelo e sua textura associada
        TexturedModel cubeModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("image2")));

        // Cria uma luz, que será usada para iluminar o modelo
        Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1)); // posição e cor da Luz

        // Cria uma câmera, que representa a visão do jogador no mundo 3D
        Camera camera = new Camera();

         // Cria uma lista para armazenar todas as entidades (cubos) do jogo
        List<Entity> allCubes = new ArrayList<Entity>();
        // Cria um objeto Random para gerar valores aleatórios
        Random random = new Random();

        // Gera 200 cubos com posições e rotações aleatórias e adiciona-os à lista de entidades
        for(int i = 0; i < 200; i++){
            float x = random.nextFloat() * 100 - 50;
            float y = random.nextFloat() * 100 - 50;
            float z = random.nextFloat() * -300;
            allCubes.add(new Entity(cubeModel, new Vector3f(x, y, z), random.nextFloat() * 180f,
            random.nextFloat() * 180f, 0f, 1f));
        }

        // Inicializa o renderizador mestre (MasterRenderer) responsável por renderizar as entidades na tela
        MasterRenderer renderer = new MasterRenderer();

        // Loop principal do jogo, que executa até que a janela de exibição seja fechada
        while (!Display.isCloseRequested()) {
            // Move a câmera
            camera.move();
            
            for(Entity cube : allCubes){
                // Rotaciona a entidade
                cube.increaseRotation(0, 1, 0);
                
                renderer.processEntity(cube);
            }

            // Renderiza todas as entidades no mundo, considerando a posição da luz e a câmera atual
            renderer.render(light, camera);

            // Atualiza a janela de exibição
            DisplayManager.updateDisplay();
        }

        // Limpa o renderizador mestre após o término do loop principal
        renderer.cleanUp();
        // Limpa o loader após o término do loop principal
        loader.cleanUp();
        // Fecha a janela de exibição após o término do loop principal
        DisplayManager.closeDisplay();
    }
}
