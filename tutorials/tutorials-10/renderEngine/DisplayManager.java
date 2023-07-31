package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
	Essa classe contém todos os métodos necessários para configurar, 
	manter e fechar uma exibição (display) usando o LWJGL.**
 */
public class DisplayManager {

	private static final int WIDTH = 1280;					// Largura Tela
	private static final int HEIGHT = 720;					// Altura Tela
	private static final int FPS_CAP = 60;					// FPS (Quadros Por Segundo)
	private static final String TITLE = "Titulo Display"; 	// Título

	/**
		Cria uma janela de exibição na qual podemos renderizar nosso jogo.
		As dimensões da janela são determinadas definindo o modo de exibição.
		Ao usar "glViewport", dizemos ao OpenGL em qual parte da janela queremos renderizar nosso jogo.
		Indicamos que queremos usar toda a janela.
	 */
	public static void createDisplay() {
		// Versão que desejamos usar do OpenGL
		// Versao(principalVersão,versãoSecundária).compativelComVersõesFuturas().UtilizarAsFuncionalidadesMaisModernasNãoSuportaObsoletos()
		ContextAttribs attribs = new ContextAttribs(3, 2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			// Local onde a Janela Inicia
			Display.setLocation(0, 0); 
			// Define o modo de exibição da janela com base nas constantes WIDTH e HEIGHT, que foram definidas anteriormente.
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT)); 
			 // Cria a janela de exibição com o formato de pixel especificado pelo objeto PixelFormat e os atributos do contexto OpenGL especificados pelo objeto attribs.
			Display.create(new PixelFormat(), attribs);
			 //Define o título da janela de exibição com o valor da constante TITLE.
			Display.setTitle(TITLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		// Define a região de exibição OpenGL (viewport) para abranger toda a janela, com base nas constantes WIDTH e HEIGHT.
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}

	/**
	Este método é usado para atualizar a tela no final de cada quadro (frame). 
	Quando configuramos um processo de renderização, este método exibirá o que quer que tenhamos renderizando na tela. 
	O método "sync" é utilizado aqui para limitar a taxa de quadros (frame rate). 
	Sem isso, o computador tentaria executar o jogo o mais rápido possível, realizando mais trabalho do que o necessário.
	*/
	
	public static void updateDisplay() {
		// Limita a taxa de quadros (FPS) para o valor especificado em FPS_CAP, garantindo que o jogo não seja executado muito rápido.
		Display.sync(FPS_CAP); 
		// Atualiza a janela de exibição para refletir o que foi renderizado no quadro atual.
		Display.update();
	}

	/**
	Isso fecha a janela quando o jogo é encerrado.
	 */
	public static void closeDisplay() {
		// Fecha e libera os recursos da janela de exibição. Isso encerra a exibição do jogo e permite que o programa seja finalizado corretamente.
		Display.destroy();
	}

}
