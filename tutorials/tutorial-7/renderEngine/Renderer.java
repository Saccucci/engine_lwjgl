package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import models.TexturedModel;

public class Renderer {

	// Prepara a cena para renderização
	public void prepare() {
		// Define a cor de fundo da tela para vermelho (RGBA: 1, 0, 0, 1). Esta chamada especifica a cor que será usada para limpar o buffer de cor durante o início de cada frame.
		GL11.glClearColor(1, 0, 0, 1);
		// Limpa o buffer de cor da tela com a cor especificada na chamada anterior, ou seja, preenche toda a tela com a cor vermelha.
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

	// Renderiza um modelo 3D texturizado
	public void render(TexturedModel texturedModel) {
		// Obtém o objeto RawModel associado ao modelo 3D texturizado
		RawModel model = texturedModel.getRawModel();
		// Define o VAO (Vertex Array Object) a ser utilizado para a renderização. O VAO armazena as informações de vértices do modelo 3D que será renderizado.
		GL30.glBindVertexArray(model.getVaoID());
		// Habilita o atributo de vértice no VAO. Neste caso, o atributo de índice 0 contém as posições dos vértices do modelo 3D.
		GL20.glEnableVertexAttribArray(0);
		// Habilita o atributo de coordenada de textura no VAO. Neste caso, o atributo de índice 1 contém as coordenadas de textura dos vértices do modelo 3D.
		GL20.glEnableVertexAttribArray(1);
		// Ativa a unidade de textura GL_TEXTURE0. Em OpenGL, várias unidades de textura podem ser usadas simultaneamente, e esta linha define a unidade de textura que será usada para a textura do modelo 3D.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// Vincula a textura do objeto TexturedModel à unidade de textura GL_TEXTURE0. Isso permite que a textura seja usada durante a renderização.
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
		// Renderiza o modelo 3D. Neste caso, a função glDrawElements é usada para renderizar os vértices do VAO de acordo com os índices armazenados. 
		// Os parâmetros são:
		// GL11.GL_TRIANGLES: Tipo de primitiva a ser renderizada. Neste caso, triângulos individuais são renderizados.
		// model.getVertexCount(): O número de vértices a serem renderizados.
		// GL11.GL_UNSIGNED_INT: O tipo de dados dos índices armazenados no VAO.
		// 0: O deslocamento inicial nos índices do VAO. Neste caso, o rendering começa no primeiro índice (índice 0).
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		// Desabilita o atributo de vértice após a renderização, para evitar conflitos com outros processos de renderização.
		GL20.glDisableVertexAttribArray(0);
		// Desabilita o atributo de coordenada de textura após a renderização, para evitar conflitos com outros processos de renderização.
		GL20.glDisableVertexAttribArray(1);
		// Desfaz o vínculo com o VAO após a renderização.
		GL30.glBindVertexArray(0);
	}
}