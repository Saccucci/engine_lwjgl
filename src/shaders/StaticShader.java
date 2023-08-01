package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {
	
	// Caminho para o arquivo contendo o código-fonte do vertex shader
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	// Caminho para o arquivo contendo o código-fonte do fragment shader
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	// Localização das variáveis uniformes no shader
	private int location_transformationMatrix; // Matriz de transformação
	private int location_projectionMatrix; // Matriz de projeção
	private int location_viewMatrix; // Matriz de visualização (câmera)
	private int location_lightPosition; // Posição da luz no espaço 3D
	private int location_lightColour; // Cor da luz
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE); // Chama o construtor da classe ShaderProgram com os caminhos dos shaders
	}

	@Override
	protected void bindAttributes() {
		// Vincula os atributos de posição e coordenadas de textura nos VAOs ao shader
		super.bindAttribute(0, "position"); // Vincula o atributo de posição do VAO ao atributo "position" do shader
		super.bindAttribute(1, "textureCoordinates"); // Vincula o atributo de coordenadas de textura do VAO ao atributo "textureCoordinates" do shader
		super.bindAttribute(2, "normal"); // Vincula o atributo de normais do VAO ao atributo "normal" do shader
	}

	@Override
	protected void getAllUniformLocations() {
		// Obtém a localização (ID) das variáveis uniformes no shader
		location_transformationMatrix = super.getUniformLocation("transformationMatrix"); // Obtém a localização da variável "transformationMatrix" no shader
		location_projectionMatrix = super.getUniformLocation("projectionMatrix"); // Obtém a localização da variável "projectionMatrix" no shader
		location_viewMatrix = super.getUniformLocation("viewMatrix"); // Obtém a localização da variável "viewMatrix" no shader
		location_lightPosition = super.getUniformLocation("lightPosition"); // Obtém a localização da variável "lightPosition" no shader
		location_lightColour = super.getUniformLocation("lightColour"); // Obtém a localização da variável "lightColour" no shader
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		// Carrega a matriz de transformação no shader usando a localização obtida anteriormente
		super.loadMatrix(location_transformationMatrix, matrix); // Carrega a matriz 4x4 de transformação no shader, representando a posição e orientação do objeto no espaço 3D
	}

	public void loadLight(Light light) {
		super.loadVector(location_lightPosition, light.getPosition()); // Carrega a posição da luz no espaço 3D no shader
		super.loadVector(location_lightColour, light.getColour()); // Carrega a cor da luz no shader
	}
	
	public void loadViewMatrix(Camera camera){
		// Cria a matriz de visualização com base nas informações da câmera e carrega-a no shader
		Matrix4f viewMatrix = Maths.createViewMatrix(camera); // Cria uma matriz 4x4 que representa a visão da câmera no mundo 3D
		super.loadMatrix(location_viewMatrix, viewMatrix); // Carrega a matriz de visualização no shader, definindo a posição e orientação da câmera
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		// Carrega a matriz de projeção no shader usando a localização obtida anteriormente
		super.loadMatrix(location_projectionMatrix, projection); // Carrega a matriz de projeção no shader, definindo a perspectiva da cena
	}

}
