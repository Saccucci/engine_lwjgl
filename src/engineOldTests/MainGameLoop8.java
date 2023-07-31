package engineTester;

// Importações das classes necessárias
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;

public class MainGameLoop8 {

	public static void main(String[] args) {
	
		// Cria a janela de exibição
		DisplayManager.createDisplay();
		
		// Inicializa o loader, responsável por carregar os dados dos modelos e texturas
		Loader loader = new Loader();
		
		// Inicializa o shader estático, que é responsável por renderizar os modelos na tela
		StaticShader shader = new StaticShader();
		
		// Inicializa o renderizador, que é responsável por renderizar os objetos na tela
		Renderer renderer = new Renderer(shader);
		
		// Definição dos vértices que compõem o modelo do objeto
		float[] vertices = {			
			-0.5f, 0.5f, 0,	
			-0.5f, -0.5f, 0,	
			0.5f, -0.5f, 0,	
			0.5f, 0.5f, 0,		
			
			-0.5f, 0.5f, 1,	
			-0.5f, -0.5f, 1,	
			0.5f, -0.5f, 1,	
			0.5f, 0.5f, 1,
			
			0.5f, 0.5f, 0,	
			0.5f, -0.5f, 0,	
			0.5f, -0.5f, 1,	
			0.5f, 0.5f, 1,
			
			-0.5f, 0.5f, 0,	
			-0.5f, -0.5f, 0,	
			-0.5f, -0.5f, 1,	
			-0.5f, 0.5f, 1,
			
			-0.5f, 0.5f, 1,
			-0.5f, 0.5f, 0,
			0.5f, 0.5f, 0,
			0.5f, 0.5f, 1,
			
			-0.5f, -0.5f, 1,
			-0.5f, -0.5f, 0,
			0.5f, -0.5f, 0,
			0.5f, -0.5f, 1	
		};
		
		// Coordenadas de textura dos vértices do modelo
		float[] textureCoords = {
			0, 0,
			0, 1,
			1, 1,
			1, 0,			
			0, 0,
			0, 1,
			1, 1,
			1, 0,			
			0, 0,
			0, 1,
			1, 1,
			1, 0,
			0, 0,
			0, 1,
			1, 1,
			1, 0,
			0, 0,
			0, 1,
			1, 1,
			1, 0,
			0, 0,
			0, 1,
			1, 1,
			1, 0
		};
		
		// indices dos vértices que compõem as faces do modelo
		int[] indices = {
			0, 1, 3,	
			3, 1, 2,	
			4, 5, 7,
			7, 5, 6,
			8, 9, 11,
			11, 9, 10,
			12, 13, 15,
			15, 13, 14,	
			16, 17, 19,
			19, 17, 18,
			20, 21, 23,
			23, 21, 22
		};
		
		// Carrega os dados do modelo para o VAO (Vertex Array Object)
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		
		// Cria um modelo texturizado, que inclui o modelo e sua textura associada
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("image")));
		
		// Cria uma entidade, que é uma instância do modelo texturizado em uma posição específica do mundo
		Entity entity = new Entity(staticModel, new Vector3f(0, 0, -5), 0, 0, 0, 1);
		
		// Cria uma câmera, que representa a visão do jogador no mundo 3D
		Camera camera = new Camera();
		
		// Loop principal do jogo, que executa até que a janela de exibição seja fechada
		while (!Display.isCloseRequested()) {
			// Rotaciona a entidade
			entity.increaseRotation(1, 1, 0);
			// Move a câmera
			camera.move();
			
			// Prepara o renderizador para a próxima renderização
			renderer.prepare();
			
			// Inicia o shader estático para renderizar o modelo
			shader.start();
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
