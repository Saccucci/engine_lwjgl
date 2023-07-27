package shaders;

import org.lwjgl.util.vector.Matrix4f;

/**
 * A classe StaticShader estende a classe ShaderProgram. Ela representa um shader estático que será usado para renderizar objetos.
 */
public class StaticShader extends ShaderProgram {

    // Declaração de uma constante chamada VERTEX_FILE, que é uma string que contém o caminho para o arquivo do shader de vértices.
    private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
    // Declaração de uma constante chamada VERTEX_FILE, que é uma string que contém o caminho para o arquivo do shader de fragmentos.
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
    // declara uma variável de instância privada chamada location_tranformationMatrix. Essa variável será usada para armazenar a localização da variável uniforme "tranformationMatrix" no shader. Como é uma variável de instância, cada objeto da classe StaticShader terá sua própria cópia dela.
	private int location_tranformationMatrix;

    /**
     * O construtor da classe StaticShader. É chamado sempre que um objeto da classe StaticShader é criado. Neste caso,
     * o construtor invoca o construtor da superclasse ShaderProgram e passa os caminhos dos arquivos de shader de vértices e fragmentos.
     */
    public StaticShader() {
        // Chama o construtor da superclasse ShaderProgram, passando os caminhos dos arquivos de shader de vértices e fragmentos como argumentos. Isso inicializa a classe ShaderProgram com os shaders corretos.
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     *  Um método protegido (protected) chamado bindAttributes(), que é uma sobrescrita do método da superclasse ShaderProgram.
     *  Esse método é responsável por vincular os atributos dos shaders aos atributos dos vértices do modelo renderizado.
     */
    @Override
    protected void bindAttributes() {
        /**
         *  Chama o método bindAttribute da superclasse ShaderProgram para vincular o atributo do VAO (Vertex Array Object)
         *  com índice 0 (representando as posições dos vértices) ao atributo "position" no shader. 
         *  Esse método foi definido na classe ShaderProgram e permite que os atributos do VAO sejam associados aos atributos dos shaders.
         */
        super.bindAttribute(0, "position");

        /**
         *  Chama o método bindAttribute da superclasse ShaderProgram para vincular o atributo do VAO (Vertex Array Object)
         *  com índice 1 (representando as coordenadas de textura dos vértices) ao atributo "textureCoords" no shader. 
         *  Esse método foi definido na classe ShaderProgram e permite que os atributos do VAO sejam associados aos atributos dos shaders.
         *  Isso é comum em shaders que utilizam mapeamento de textura.
         */
        super.bindAttribute(1, "textureCoords");
    }

    /**
     * Um método protegido (protected) chamado getAllUniformLocation(), que é uma sobrescrita do método da superclasse ShaderProgram.
     * Esse método é responsável por obter a localização das variáveis uniformes (constantes) definidas nos shaders.
     */
    @Override
    protected void getAllUniformLocation() {
        // Obtém a localização da variável uniforme "tranformationMatrix" no shader e armazena no atributo "location_tranformationMatrix".
        location_tranformationMatrix = super.getUniformLocation("tranformationMatrix");
    }

    /**
     * Um método público chamado loadTransformationMatrix(), que é usado para carregar uma matriz de transformação (Matrix4f) no shader.
     * Essa matriz será usada para transformar os vértices do modelo antes da renderização.
     */
    public void loadTransformationMatrix(Matrix4f matrix) {
        // Chama o método loadMatrix da superclasse ShaderProgram para carregar a matriz de transformação na variável uniforme "tranformationMatrix" do shader.
        super.loadMatrix(location_tranformationMatrix, matrix);
    }

}
