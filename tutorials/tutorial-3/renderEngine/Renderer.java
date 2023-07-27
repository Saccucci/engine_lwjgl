package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

	public void prepare() {
		// Define a cor de fundo da tela para vermelho (RGBA: 1, 0, 0, 1). Esta chamada especifica a cor que será usada para limpar o buffer de cor durante o início de cada frame.
		GL11.glClearColor(1, 0, 0, 1);
		//Limpa o buffer de cor da tela com a cor especificada na chamada anterior, ou seja, preenche toda a tela com a cor vermelha.
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}


	public void render(RawModel model) {
		// Define o VAO (Vertex Array Object) a ser utilizado para a renderização. O VAO armazena as informações de vértices do modelo 3D que será renderizado.
		GL30.glBindVertexArray(model.getVaoID());
		//Habilita o atributo de vértice no VAO. Neste caso, o atributo de índice 0 contém as posições dos vértices do modelo 3D.
		GL20.glEnableVertexAttribArray(0);
		//Renderiza o modelo 3D. Neste caso, a função glDrawElements é usada para renderizar os vértices do VAO de acordo com os índices armazenados. 
		// Os parâmetros são:
		//GL11.GL_TRIANGLES: Tipo de primitiva a ser renderizada. Neste caso, triângulos individuais são renderizados.
		//model.getVertexCount(): O número de vértices a serem renderizados.
		//GL11.GL_UNSIGNED_INT: O tipo de dados dos índices armazenados no VAO.
		//0: O deslocamento inicial nos índices do VAO. Neste caso, o rendering começa no primeiro índice (índice 0).
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		//Desabilita o atributo de vértice após a renderização, para evitar conflitos com outros processos de renderização.
		GL20.glDisableVertexAttribArray(0);
		//Desfaz o vínculo com o VAO após a renderização.
		GL30.glBindVertexArray(0);
	}

}