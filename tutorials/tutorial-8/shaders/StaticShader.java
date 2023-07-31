package shaders;

import org.lwjgl.util.vector.Matrix4f;

import toolbox.Maths;

import entities.Camera;

public class StaticShader extends ShaderProgram {
	
	// Caminho para o arquivo contendo o código-fonte do vertex shader
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	// Caminho para o arquivo contendo o código-fonte do fragment shader
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	// Localização das variáveis uniformes no shader
	private int location_transformationMatrix; // Matriz de transformação
	private int location_projectionMatrix; // Matriz de projeção
	private int location_viewMatrix; // Matriz de visualização (câmera)
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE); // Chama o construtor da classe ShaderProgram com os caminhos dos shaders
	}

	@Override
	protected void bindAttributes() {
		// Vincula os atributos de posição e coordenadas de textura nos VAOs ao shader
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
	}

	@Override
	protected void getAllUniformLocations() {
		// Obtém a localização (ID) das variáveis uniformes no shader
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		// Carrega a matriz de transformação no shader usando a localização obtida anteriormente
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		// Cria a matriz de visualização com base nas informações da câmera e carrega-a no shader
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		// Carrega a matriz de projeção no shader usando a localização obtida anteriormente
		super.loadMatrix(location_projectionMatrix, projection);
	}
}
