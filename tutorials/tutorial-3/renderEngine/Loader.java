package renderEngine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


public class Loader {

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();

	public RawModel loadToVAO(float[] positions, int[] indices) {
		//Cria um novo VAO e retorna seu ID. O VAO será usado para armazenar dados geométricos do modelo 3D.
		int vaoID = createVAO();
		// Cria um index buffer e o associa ao VAO atual. O index buffer contém os índices que conectam os vértices para formar triângulos.
		bindIndicesBuffer(indices);
		// Armazena os dados de posição dos vértices no atributo 0 do VAO. Os dados são armazenados em um VBO (Vertex Buffer Object) específico.
		storeDataInAttributeList(0, positions);
		//Desfaz o vínculo com o VAO atual após terminar de armazenar os dados.
		unbindVAO();
		//Retorna um novo objeto RawModel, que contém o ID do VAO criado e o número de índices no modelo.
		return new RawModel(vaoID, indices.length);
	}


	public void cleanUp() {
		//Itera sobre todos os VAOs criados e deleta-os da memória de vídeo (GPU).
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		//Itera sobre todos os VBOs criados e deleta-os da memória de vídeo (GPU). Isso também inclui o index buffer criado anteriormente.
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
	}


	private int createVAO() {
		//Gera um ID para um novo VAO usando glGenVertexArrays() do OpenGL.
		int vaoID = GL30.glGenVertexArrays();
		//Adiciona o ID do novo VAO à lista de VAOs rastreados pela classe.
		vaos.add(vaoID);
		//Vincula o novo VAO, tornando-o o VAO ativo que será usado para armazenar e renderizar dados geométricos.
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	
	private void storeDataInAttributeList(int attributeNumber, float[] data) {
		//Gera um ID para um novo VBO usando glGenBuffers() do OpenGL.
		int vboID = GL15.glGenBuffers();
		//Adiciona o ID do novo VBO à lista de VBOs rastreados pela classe.
		vbos.add(vboID);
		//Vincula o novo VBO, tornando-o o VBO ativo que será usado para armazenar os dados do atributo.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		//Converte os dados de posição do array de float para um FloatBuffer para que possam ser armazenados no VBO.
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		//Armazena os dados no VBO usando glBufferData(). GL_STATIC_DRAW indica que os dados não serão modificados após serem armazenados.
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		//Define como os dados do VBO serão interpretados pelos shaders. Neste caso, os dados serão interpretados como atributos de vértice com 3 valores de ponto flutuante (x, y, z).
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
		//Desfaz o vínculo com o VBO após terminar de armazenar os dados.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	
	private void unbindVAO() {
		//Desfaz o vínculo com o VAO atual após terminar de armazenar os dados.
		GL30.glBindVertexArray(0);
	}

	
	private void bindIndicesBuffer(int[] indices) {
		// Gera um ID para um novo VBO que será usado como index buffer.
		int vboId = GL15.glGenBuffers();
		//Adiciona o ID do novo VBO à lista de VBOs rastreados pela classe.
		vbos.add(vboId);
		//Vincula o novo VBO como o index buffer ativo para o VAO atual.
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
		//Converte os índices do array de int para um IntBuffer para que possam ser armazenados no VBO.
		IntBuffer buffer = storeDataInIntBuffer(indices);
		//Armazena os índices no index buffer usando glBufferData(). GL_STATIC_DRAW indica que os dados não serão modificados após serem armazenados.
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}


	private IntBuffer storeDataInIntBuffer(int[] data) {
		//Cria um novo IntBuffer com tamanho igual ao comprimento do array data. O IntBuffer é um buffer especializado que contém dados do tipo int.
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		//: Insere o array data no IntBuffer. O método put() copia os elementos do array para o buffer.
		buffer.put(data);
		// Prepara o IntBuffer para leitura. O buffer é colocado no modo de leitura, onde o ponteiro é posicionado no início dos dados inseridos.
		buffer.flip();
		//Retorna o IntBuffer pronto para ser utilizado. Agora, o buffer contém os dados do array data e está pronto para ser armazenado no Vertex Buffer Object (VBO).
		return buffer;
	}


	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		//Cria um novo FloatBuffer com tamanho igual ao comprimento do array data. O FloatBuffer é um buffer especializado que contém dados do tipo float.
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		//Insere o array data no FloatBuffer. O método put() copia os elementos do array para o buffer.
		buffer.put(data);
		//Prepara o FloatBuffer para leitura. O buffer é colocado no modo de leitura, onde o ponteiro é posicionado no início dos dados inseridos.
		buffer.flip();
		//Retorna o FloatBuffer pronto para ser utilizado. Agora, o buffer contém os dados do array data e está pronto para ser armazenado no Vertex Buffer Object (VBO).
		return buffer;
	}

}