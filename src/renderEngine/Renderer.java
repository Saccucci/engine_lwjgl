package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

public class Renderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;

	private Matrix4f projectionMatrix;
	private StaticShader shader;

	// Construtor da classe Renderer
	public Renderer(StaticShader shader) {
		this.shader = shader;
		// ativa o culling/ocultação de faces
		GL11.glEnable(GL11.GL_CULL_FACE);
		// este comando esconde as faces // o Padrão é (GL11.GL_BACK) para esconder as faces traseiras/ocultas porém (GL_FRONT) foi o parametro que funcionou para esta finalidade
		GL11.glCullFace(GL11.GL_FRONT);
		createProjectionMatrix(); // Chama o método para criar a matriz de projeção
		shader.start(); // Inicia o shader
		shader.loadProjectionMatrix(projectionMatrix); // Carrega a matriz de projeção para o shader
		shader.stop(); // Para o shader
	}

	// Prepara a tela para renderização
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Ativa o teste de profundidade
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Limpa o buffer de cores e de profundidade
		GL11.glClearColor(0.4f, 0.2f, 0.2f, 1); // Define a cor de fundo da tela (verde escuro)
	}

	// Método para renderizar uma lista de entidades associadas a modelos texturizados
	public void render(Map<TexturedModel, List<Entity>> entities) {
		// Iterar sobre cada modelo texturizado na lista de entidades
		for (TexturedModel model : entities.keySet()) {
			// Preparar o modelo texturizado antes de renderizar
			prepareTexturedModel(model);

			// Obter a lista de entidades associadas a este modelo texturizado
			List<Entity> batch = entities.get(model);

			// Iterar sobre cada entidade na lista
			for (Entity entity : batch) {
				// Preparar a instância da entidade antes de renderizar
				prepareInstance(entity);

				// Desenhar os elementos do modelo texturizado no contexto OpenGL
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				// Esta linha é responsável por renderizar o modelo texturizado utilizando triângulos,
				// usando o número de vértices obtidos a partir do modelo raw e um buffer de elementos
				// não assinados (Unsigned Int) para a renderização.
			}

			// Finalizar o modelo texturizado após renderizar todas as entidades associadas a ele
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel(); // Obtém o modelo bruto do modelo texturizado
		GL30.glBindVertexArray(rawModel.getVaoID()); // Ativa o VAO (Vertex Array Object) do modelo
		GL20.glEnableVertexAttribArray(0); // Ativa o atributo de posição do VAO (índice 0)
		GL20.glEnableVertexAttribArray(1); // Ativa o atributo de coordenadas de textura do VAO (índice 1)
		GL20.glEnableVertexAttribArray(2); // Ativa o atributo de coordenadas de textura do VAO (índice 2)
		ModelTexture texture = model.getTexture(); // Obtém a textura do modelo
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity()); // Carrega as variáveis de brilho (shine) para o shader
		GL13.glActiveTexture(GL13.GL_TEXTURE0); // Ativa a textura no índice 0 (GL_TEXTURE0)
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID()); // Associa a textura ao modelo
	}

	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0); // Desativa o atributo de posição do VAO (índice 0)
		GL20.glDisableVertexAttribArray(1); // Desativa o atributo de coordenadas de textura do VAO (índice 1)
		GL20.glDisableVertexAttribArray(2); // Desativa o atributo de coordenadas de textura do VAO (índice 2)
		GL30.glBindVertexArray(0); // Desativa o VAO
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), // Posição da entidade
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), // Rotação da entidade nos eixos X, Y e Z
				entity.getScale() // Escala da entidade
		);
		shader.loadTransformationMatrix(transformationMatrix); // Carrega a matriz de transformação para o shader
	}

	// Cria a matriz de projeção
	private void createProjectionMatrix() {
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
