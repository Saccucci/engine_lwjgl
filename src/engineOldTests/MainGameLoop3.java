package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;

public class MainGameLoop3 {


	/**
		Carrega os dados de posição para dois triângulos (que juntos formam um quadrilátero)
		em um VAO. Esse VAO é então renderizado na tela a cada quadro.
	 * @param args
	 */
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		// Define um array vertices que contém as coordenadas dos vértices de dois triângulos que juntos formam um quadrilátero.
		float[] vertices = {
				-0.5f, 0.5f, 0f,//v0
				-0.5f, -0.5f, 0f,//v1
				0.5f, -0.5f, 0f,//v2
				0.5f, 0.5f, 0f,//v3
		};
		
		// Define um array indices que define a ordem dos vértices para formar os triângulos. 
		// Esses índices referenciam os vértices definidos no array vertices.
		int[] indices = {
				0,1,3,//top left triangle (v0, v1, v3)
				3,1,2//bottom right triangle (v3, v1, v2)
		};
		
		//  Carrega os dados de vértices e índices em um VAO usando o objeto loader. 
		// O model é um objeto RawModel que contém os dados necessários para renderizar o quadrilátero na tela.
		RawModel model = loader.loadToVAO(vertices, indices);

		while (!Display.isCloseRequested()) {
			// game logic
			renderer.prepare();
			renderer.render(model);
			DisplayManager.updateDisplay();
		}

		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}