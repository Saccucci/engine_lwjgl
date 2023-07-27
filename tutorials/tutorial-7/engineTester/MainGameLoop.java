package engineTester;

import org.lwjgl.opengl.Display;

import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	/**
	 * Carrega os dados de posição para dois triângulos (que juntos formam um quadrilátero)
	 * em um VAO. Esse VAO é então renderizado na tela a cada quadro.
	 * @param args
	 */
	public static void main(String[] args) {
		
		DisplayManager.createDisplay(); // Cria a janela de exibição para o jogo
		Loader loader = new Loader(); // Cria um objeto Loader, usado para carregar dados na GPU
		Renderer renderer = new Renderer(); // Cria um objeto Renderer, usado para renderizar objetos na tela
		StaticShader shader = new StaticShader(); // Instancia um shader estático

		// Define as coordenadas dos vértices que compõem dois triângulos, formando um quadrilátero
		float[] vertices = {
			-0.5f, 0.5f, 0f, // v0
			-0.5f, -0.5f, 0f, // v1
			0.5f, -0.5f, 0f, // v2
			0.5f, 0.5f, 0f // v3
		};

		// Define a ordem dos vértices para formar os triângulos
		int[] indices = {
			0, 1, 3, // top left triangle (v0, v1, v3)
			3, 1, 2 // bottom right triangle (v3, v1, v2)
		};

		// Define as coordenadas de textura para mapear a textura sobre o modelo
		float[] textureCoords = {
			0, 0, // V0
			0, 1, // V1
			1, 1, // V2
			1, 0 // V3
		};
		
		// Carrega os dados de vértices, texturas e índices em um VAO usando o objeto loader
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);

		// Carrega a textura do modelo a partir de um arquivo de imagem
		ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
		
		// Cria um modelo texturizado com o modelo Raw e a textura carregada
		TexturedModel texturedModel = new TexturedModel(model, texture);

		while (!Display.isCloseRequested()) {
			// Lógica do jogo (pode ser a atualização de entidades, eventos, etc.)

			renderer.prepare(); // Prepara a tela para a renderização
			shader.start(); // Ativa o shader estático para a renderização
			renderer.render(texturedModel); // Renderiza o modelo texturizado na tela
			shader.stop(); // Desativa o shader estático após a renderização
			DisplayManager.updateDisplay(); // Atualiza a janela de exibição para mostrar as mudanças
			
			// O loop continua até que o usuário feche a janela de exibição
		}

		shader.cleanUp(); // Libera os recursos alocados para o shader
		loader.cleanUp(); // Libera os recursos alocados para o loader
		DisplayManager.closeDisplay(); // Fecha a janela de exibição e encerra o programa
	}

}
