package renderEngine;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/** O codigo é responsalvel por carregar e gerenciar as geometrias para a rendereização,
ele cria os VAOs e os VBOs armazena e renderiza os dados de vertices.
o codigo é responsalvel por  carregar os dados da geometria no VAO e,
quando a aplicação é encerrada, ele se encarrega de limpar a memória de vídeo alocada para os VAOs e VBOs. */

public class Loader {

	//Declaração de uma lista de Integer vaos para armazenar os IDs dos VAOs (Vertex Array Objects) criados durante a execução do programa. 
    // Os VAOs representam geometria carregada em memória de vídeo para renderização.
	private List<Integer> vaos = new ArrayList<Integer>();
	//Declaração de uma lista de Integer vbos para armazenar os IDs dos VBOs (Vertex Buffer Objects) criados durante a execução do programa. 
    // Os VBOs representam buffers que armazenam os dados dos vértices da geometria carregada.
	private List<Integer> vbos = new ArrayList<Integer>();

	//Declaração do método público loadToVAO(float[] positions) responsável por carregar os dados de geometria em um VAO e retornar um objeto RawModel representando o modelo carregado.
	public RawModel loadToVAO(float[] positions) {
		//Cria um novo VAO e obtém seu ID chamando o método createVAO(). Este novo VAO será usado para armazenar os dados do modelo.
		int vaoID = createVAO();
		//Armazena os dados de posição dos vértices (representados por positions) no atributo 0 do VAO.
		storeDataInAttributeList(0, positions);
		//Desvincula o VAO após carregar os dados. Isso é uma boa prática para evitar modificar acidentalmente o VAO enquanto não estiver sendo usado.
		unbindVAO();
		//Retorna um novo objeto RawModel com o ID do VAO criado e o número de vértices do modelo (calculado dividindo o comprimento do array positions por 3, pois cada vértice possui três coordenadas x, y, z).
		return new RawModel(vaoID, positions.length / 3);
	}

	// Declaração do método público cleanUp() que será chamado para liberar a memória alocada para os VAOs e VBOs quando o jogo ou aplicação for encerrado.
	public void cleanUp() {
		// Iteração pelos IDs dos VAOs armazenados na lista vaos
		for (int vao : vaos) {
			//Deleta o VAO correspondente usando o ID vao com a função glDeleteVertexArrays(). Isso libera a memória de vídeo associada ao VAO.
			GL30.glDeleteVertexArrays(vao);
		}
		//Iteração pelos IDs dos VBOs armazenados na lista vbos.
		for (int vbo : vbos) {
			// Deleta o VBO correspondente usando o ID vbo com a função glDeleteBuffers(). Isso libera a memória de vídeo associada ao VBO.
			GL15.glDeleteBuffers(vbo);
		}
	}

	//Declaração do método privado createVAO() que cria um novo VAO, retorna seu ID e o torna o VAO ativo.
	private int createVAO() {
		//Gera o ID do VAO usando glGenVertexArrays() e armazena-o em vaoID.
		int vaoID = GL30.glGenVertexArrays();
		// Adiciona o ID do VAO à lista vaos.
		vaos.add(vaoID);
		//Ativa o VAO, tornando-o o VAO ativo, permitindo que ele seja configurado.
		GL30.glBindVertexArray(vaoID);
		//Retorna o ID do VAO recém-criado.
		return vaoID;
	}

	//Declaração do método privado storeDataInAttributeList(int attributeNumber, float[] data) que armazena os dados de geometria no atributo especificado (usando attributeNumber) do VAO ativo.
	private void storeDataInAttributeList(int attributeNumber, float[] data) {
		// Gera o ID do VBO usando glGenBuffers() e armazena-o em vboID.
		int vboID = GL15.glGenBuffers();
		//Adiciona o ID do VBO à lista vbos
		vbos.add(vboID);
		//Ativa o VBO, tornando-o o VBO ativo, permitindo que ele seja configurado.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		//Converte o array de floats data em um FloatBuffer usando o método storeDataInFloatBuffer(data).
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		//Armazena os dados do buffer de vértices no VBO ativo usando glBufferData(). O último parâmetro GL15.GL_STATIC_DRAW indica que os dados não serão alterados com frequência.
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		//Especifica o atributo de índice attributeNumber no VAO onde os dados do VBO serão armazenados. 
        // Neste caso, o atributo 0 contém as posições dos vértices. 
        // O segundo parâmetro 3 indica que cada vértice possui três coordenadas (x, y, z). 
        // O terceiro parâmetro GL11.GL_FLOAT indica que os dados são de tipo float. 
        // O quarto parâmetro false indica que os dados não estão normalizados. 
        // O quinto parâmetro 0 indica o deslocamento inicial dos dados no buffer.
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
		// Desativa o VBO, tornando-o inativo. É uma boa prática desativar o VBO após configurá-lo.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	//Declaração do método privado unbindVAO() que desvincula o VAO ativo.
	private void unbindVAO() {
		// Desativa o VAO, tornando-o inativo. É uma boa prática desativar o VAO após configurá-lo.
		GL30.glBindVertexArray(0);
	}

	//Declaração do método privado storeDataInFloatBuffer(float[] data) que converte um array de floats em um FloatBuffer e retorna o FloatBuffer resultante.
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		//Cria um FloatBuffer vazio com o tamanho igual ao comprimento do array de floats data.
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		//Adiciona os dados do array de floats data ao FloatBuffer.
		buffer.put(data);
		//Prepara o FloatBuffer para leitura, colocando o ponteiro de leitura no início do buffer.
		buffer.flip();
		//Retorna o FloatBuffer contendo os dados dos vértices no formato adequado para serem carregados em um VBO.
		return buffer;
	}

}
