// Importação das bibliotecas necessárias
package entities;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

// Definição da classe Camera
public class Camera {
	
	// Declaração dos atributos da câmera
	private Vector3f position = new Vector3f(0,0,0); // Vetor que armazena a posição da câmera no espaço 3D
	private float pitch; // Angulação em torno do eixo x (inclinção da câmera para cima ou para baixo)
	private float yaw;   // Angulação em torno do eixo y (rotação da câmera para a esquerda ou para a direita)
	private float roll;  // Angulação em torno do eixo z (inclinação lateral da câmera)

	// Construtor padrão da classe Camera
	public Camera(){}

	// Método responsável por movimentar a câmera com base nas teclas pressionadas
	public void move(){
		// Verifica se a tecla W está pressionada
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.z -= 0.02f; // Move a câmera para trás no eixo z (diminui a posição no eixo z)
		}
		// Verifica se a tecla S está pressionada
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.z += 0.02f; // Move a câmera para frente no eixo z (aumenta a posição no eixo z)
		}
		// Verifica se a tecla D está pressionada
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.x += 0.02f; // Move a câmera para a direita no eixo x (aumenta a posição no eixo x)
		}
		// Verifica se a tecla A está pressionada
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x -= 0.02f; // Move a câmera para a esquerda no eixo x (diminui a posição no eixo x)
		}
	}

	// Método para obter a posição atual da câmera
	public Vector3f getPosition() {
		return position;
	}

	// Método para obter a inclinação da câmera (pitch)
	public float getPitch() {
		return pitch;
	}

	// Método para obter a rotação da câmera (yaw)
	public float getYaw() {
		return yaw;
	}

	// Método para obter a inclinação lateral da câmera (roll)
	public float getRoll() {
		return roll;
	}
}
