package engineOldTests;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;

/**
 * Esta classe Serve Para Testar a Engine
 */
public class MainGameLoop {

	/**
	 * Cria um display e atualiza continuamente até o usuário fechar
	 * @param args
	 */
	public static void main(String[] args) {
		// Cria o Display
		DisplayManager.createDisplay();

		// Enquanto a requisição de Fechar o Display/Jogo não for acionada
		while (!Display.isCloseRequested()) {

			// Lógica do Game

			// Renderização
			DisplayManager.updateDisplay();
		}

		// Fecha o Display
		DisplayManager.closeDisplay();
	}

}