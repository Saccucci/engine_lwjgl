package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;

public class Loader {

    // Lista de IDs de Vertex Array Objects (VAOs), Vertex Buffer Objects (VBOs) e texturas para limpeza posterior.
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    // Carrega um modelo 3D para um Vertex Array Object (VAO) e retorna um objeto RawModel que contém o ID do VAO criado e o número de índices no modelo.
    public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices) {
        // Cria um novo VAO e retorna seu ID. O VAO será usado para armazenar dados geométricos do modelo 3D.
        int vaoID = createVAO();
        // Cria um index buffer e o associa ao VAO atual. O index buffer contém os índices que conectam os vértices para formar triângulos.
        bindIndicesBuffer(indices);
        // Armazena os dados de posição dos vértices no atributo 0 do VAO. Os dados são armazenados em um VBO (Vertex Buffer Object) específico.
        storeDataInAttributeList(0, 3, positions);
        // Armazena os dados de coordenadas de textura no atributo 1 do VAO. Os dados são armazenados em um VBO específico.
        storeDataInAttributeList(1, 2, textureCoords);
        // Desfaz o vínculo com o VAO atual após terminar de armazenar os dados.
        unbindVAO();
        // Retorna um novo objeto RawModel, que contém o ID do VAO criado e o número de índices no modelo.
        return new RawModel(vaoID, indices.length);
    }

    // Carrega uma textura a partir de um arquivo e retorna o ID da textura.
    public int loadTexture(String fileName) {
        Texture texture = null;
        try {
            // Carrega a textura usando a biblioteca Slick-Util.
            texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Obtém o ID da textura carregada e adiciona-o à lista de texturas rastreadas pela classe.
        int textureID = texture.getTextureID();
        textures.add(textureID);
        // Retorna o ID da textura para uso posterior.
        return textureID;
    }

    // Limpa e deleta todos os VAOs, VBOs e texturas criadas pela classe.
    public void cleanUp() {
        // Itera sobre todos os VAOs criados e deleta-os da memória de vídeo (GPU).
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        // Itera sobre todos os VBOs criados e deleta-os da memória de vídeo (GPU). Isso também inclui o index buffer criado anteriormente.
        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        // Itera sobre todas as texturas carregadas e deleta-as da memória de vídeo (GPU).
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    // Cria um novo Vertex Array Object (VAO), retorna seu ID e o vincula como o VAO ativo para armazenamento de dados geométricos.
    private int createVAO() {
        // Gera um ID para um novo VAO usando glGenVertexArrays() do OpenGL.
        int vaoID = GL30.glGenVertexArrays();
        // Adiciona o ID do novo VAO à lista de VAOs rastreados pela classe.
        vaos.add(vaoID);
        // Vincula o novo VAO, tornando-o o VAO ativo que será usado para armazenar e renderizar dados geométricos.
        GL30.glBindVertexArray(vaoID);
        // Retorna o ID do VAO criado.
        return vaoID;
    }

    // Armazena dados em um atributo de um VAO, como as posições dos vértices ou as coordenadas de textura.
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        // Gera um ID para um novo VBO usando glGenBuffers() do OpenGL.
        int vboID = GL15.glGenBuffers();
        // Adiciona o ID do novo VBO à lista de VBOs rastreados pela classe.
        vbos.add(vboID);
        // Vincula o novo VBO, tornando-o o VBO ativo que será usado para armazenar os dados do atributo.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        // Converte os dados de posição do array de float para um FloatBuffer para que possam ser armazenados no VBO.
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        // Armazena os dados no VBO usando glBufferData(). GL_STATIC_DRAW indica que os dados não serão modificados após serem armazenados.
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        // Define como os dados do VBO serão interpretados pelos shaders. Neste caso, os dados serão interpretados como atributos de vértice com 3 valores de ponto flutuante (x, y, z).
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        // Desfaz o vínculo com o VBO após terminar de armazenar os dados.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    // Desfaz o vínculo com o VAO atual, indicando o término do armazenamento de dados.
    private void unbindVAO() {
        // Desfaz o vínculo com o VAO atual após terminar de armazenar os dados.
        GL30.glBindVertexArray(0);
    }

    // Cria um novo Vertex Buffer Object (VBO) para o index buffer, que contém os índices para desenhar os triângulos do modelo.
    private void bindIndicesBuffer(int[] indices) {
        // Gera um ID para um novo VBO que será usado como index buffer.
        int vboId = GL15.glGenBuffers();
        // Adiciona o ID do novo VBO à lista de VBOs rastreados pela classe.
        vbos.add(vboId);
        // Vincula o novo VBO como o index buffer ativo para o VAO atual.
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        // Converte os índices do array de int para um IntBuffer para que possam ser armazenados no VBO.
        IntBuffer buffer = storeDataInIntBuffer(indices);
        // Armazena os índices no index buffer usando glBufferData(). GL_STATIC_DRAW indica que os dados não serão modificados após serem armazenados.
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    // Cria e retorna um novo IntBuffer com os dados de um array de int.
    private IntBuffer storeDataInIntBuffer(int[] data) {
        // Cria um novo IntBuffer com tamanho igual ao comprimento do array data. O IntBuffer é um buffer especializado que contém dados do tipo int.
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        // Insere o array data no IntBuffer. O método put() copia os elementos do array para o buffer.
        buffer.put(data);
        // Prepara o IntBuffer para leitura. O buffer é colocado no modo de leitura, onde o ponteiro é posicionado no início dos dados inseridos.
        buffer.flip();
        // Retorna o IntBuffer pronto para ser utilizado. Agora, o buffer contém os dados do array data e está pronto para ser armazenado no Vertex Buffer Object (VBO).
        return buffer;
    }

    // Cria e retorna um novo FloatBuffer com os dados de um array de float.
    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        // Cria um novo FloatBuffer com tamanho igual ao comprimento do array data. O FloatBuffer é um buffer especializado que contém dados do tipo float.
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        // Insere o array data no FloatBuffer. O método put() copia os elementos do array para o buffer.
        buffer.put(data);
        // Prepara o FloatBuffer para leitura. O buffer é colocado no modo de leitura, onde o ponteiro é posicionado no início dos dados inseridos.
        buffer.flip();
        // Retorna o FloatBuffer pronto para ser utilizado. Agora, o buffer contém os dados do array data e está pronto para ser armazenado no Vertex Buffer Object (VBO).
        return buffer;
    }

}