package shaders;

public class StaticShader extends ShaderProgram{
	
	//Declaração de uma constante chamada VERTEX_FILE, que é uma string que contém o caminho para o arquivo do shader de vértices.
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	//Declaração de uma constante chamada VERTEX_FILE, que é uma string que contém o caminho para o arquivo do shader de vértices.
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

	/**
	 * O construtor da classe StaticShader. É chamado sempre que um objeto da classe StaticShader é criado. Neste caso,
	 * o construtor invoca o construtor da superclasse ShaderProgram e passa os caminhos dos arquivos de shader de vértices e fragmentos.
	 */
	public StaticShader() {
		//Chama o construtor da superclasse ShaderProgram, passando os caminhos dos arquivos de shader de vértices e fragmentos como argumentos. Isso inicializa a classe ShaderProgram com os shaders corretos.
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
	}
	
	

}
