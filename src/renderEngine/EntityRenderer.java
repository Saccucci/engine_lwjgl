package renderEngine;

import java.util.List;
import java.util.Map;

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

public class EntityRenderer {

	private StaticShader shader;

	// Construtor da classe Renderer
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start(); // Inicia o shader
		shader.loadProjectionMatrix(projectionMatrix); // Carrega a matriz de projeção para o shader
		shader.stop(); // Para o shader
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
}
