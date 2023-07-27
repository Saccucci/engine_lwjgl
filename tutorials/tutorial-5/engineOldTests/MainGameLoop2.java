package engineOldTests;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;

/**
 * Esta classe contém o método main e é usada para testar a engine.
 */
public class MainGameLoop2 {


	/**
	 * Carrega os dados de posição para dois triângulos (que juntos formam um quadrado)
	 * em um VAO. Esse VAO é então renderizado na tela a cada quadro.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		// Modelo que está sendo desenhado (no caso um retangulo)
		//Declaração de um array de floats chamado vertices que contém as coordenadas dos vértices de dois triângulos, formando um quadrado na tela. Cada conjunto de três floats representa as coordenadas x, y e z de um vértice.
		float[] vertices = {
				// Left bottom triangle
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				// Right top triangle
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f
		};
		
		// Cria um modelo (instância de RawModel) carregando os dados de vértices do array vertices usando o objeto loader.
		RawModel model = loader.loadToVAO(vertices);

		while (!Display.isCloseRequested()) {
			// game logic
			// Limpa a tela
			renderer.prepare();
			// renderiza o Objeto passado como parâmetro
			renderer.render(model);
			DisplayManager.updateDisplay();
		}

		// Limpa o Buffer /Memória alocada para os VAOs e VBOs
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}