package renderEngine;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import toolbox.Maths;

import entities.Entity;

public class Renderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	
	// Construtor da classe Renderer
	public Renderer(StaticShader shader){
		createProjectionMatrix(); // Chama o método para criar a matriz de projeção
		shader.start(); // Inicia o shader
		shader.loadProjectionMatrix(projectionMatrix); // Carrega a matriz de projeção para o shader
		shader.stop(); // Para o shader
	}

	// Prepara a tela para renderização
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Ativa o teste de profundidade
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Limpa o buffer de cores e de profundidade
		GL11.glClearColor(0, 0.3f, 0.0f, 1); // Define a cor de fundo da tela (verde escuro)
	}

	// Renderiza uma entidade na tela
	public void render(Entity entity, StaticShader shader) {
		TexturedModel model = entity.getModel(); // Obtém o modelo texturizado da entidade
		RawModel rawModel = model.getRawModel(); // Obtém o modelo bruto do modelo texturizado
		GL30.glBindVertexArray(rawModel.getVaoID()); // Ativa o VAO (Vertex Array Object) do modelo
		GL20.glEnableVertexAttribArray(0); // Ativa o atributo de posição do VAO (índice 0)
		GL20.glEnableVertexAttribArray(1); // Ativa o atributo de coordenadas de textura do VAO (índice 1)
		GL20.glEnableVertexAttribArray(2); // Ativa o atributo de coordenadas de textura do VAO (índice 2)
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				entity.getPosition(), // Posição da entidade
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), // Rotação da entidade nos eixos X, Y e Z
				entity.getScale() // Escala da entidade
		);
		shader.loadTransformationMatrix(transformationMatrix); // Carrega a matriz de transformação para o shader
		GL13.glActiveTexture(GL13.GL_TEXTURE0); // Ativa a textura no índice 0 (GL_TEXTURE0)
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID()); // Associa a textura ao modelo
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // Renderiza o modelo
		GL20.glDisableVertexAttribArray(0); // Desativa o atributo de posição do VAO (índice 0)
		GL20.glDisableVertexAttribArray(1); // Desativa o atributo de coordenadas de textura do VAO (índice 1)
		GL20.glDisableVertexAttribArray(2); // Desativa o atributo de coordenadas de textura do VAO (índice 2)
		GL30.glBindVertexArray(0); // Desativa o VAO
	}
	
	// Cria a matriz de projeção
	private void createProjectionMatrix(){
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight(); // Calcula a proporção de aspecto
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio); // Calcula a escala no eixo Y
		float x_scale = y_scale / aspectRatio; // Calcula a escala no eixo X
		float frustum_length = FAR_PLANE - NEAR_PLANE; // Calcula o comprimento do frustum

		projectionMatrix = new Matrix4f(); // Cria uma nova matriz 4x4 (matriz de projeção)
		projectionMatrix.m00 = x_scale; // Define o valor de escala no eixo X
		projectionMatrix.m11 = y_scale; // Define o valor de escala no eixo Y
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length); // Define a componente (2,2) da matriz
		projectionMatrix.m23 = -1; // Define a componente (2,3) da matriz
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length); // Define a componente (3,2) da matriz
		projectionMatrix.m33 = 0; // Define a componente (3,3) da matriz
	}
}
