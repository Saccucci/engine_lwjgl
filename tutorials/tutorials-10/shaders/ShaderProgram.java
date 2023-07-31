package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram {
	
	// ID do programa de shader.
	private int programID;
	// ID do shader de vértices.
	private int vertexShaderID;
	//ID do shader de fragmentos.
	private int fragmentShaderID;
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	//O construtor recebe os nomes de arquivos dos shaders de vértices e fragmentos como parâmetros.
	public ShaderProgram(String vertexFile,String fragmentFile){
		/**
		 * Carrega e compila o shader de vértices.
		 * Chama o método privado loadShader com o nome do arquivo do shader de vértices (vertexFile)
		 * e a constante GL20.GL_VERTEX_SHADER para indicar que se trata de um shader de vértices.
		 * O método loadShader retornará o ID do shader de vértices, que é armazenado no atributo vertexShaderID.
		 */
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		/**Carrega e compila o shader de fragmentos.
		 * Chama o método privado loadShader com o nome do arquivo do shader de fragmentos (fragmentFile)
		 *  e a constante GL20.GL_FRAGMENT_SHADER para indicar que se trata de um shader de fragmentos.
		 * O método loadShader retornará o ID do shader de fragmentos, que é armazenado no atributo fragmentShaderID. 
		*/
		fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		/** Cria um programa de shader (programID) que representa um conjunto de shaders.
		 * Utiliza a função GL20.glCreateProgram() para criar o programa vazio, e o ID do programa
 		 * é armazenado no atributo programID. 
		*/
		programID = GL20.glCreateProgram();
		/** Anexa o shader de vértices previamente compilado ao programa de shader.	
		 * Usa a função GL20.glAttachShader() para vincular o shader de vértices ao programa,
		 *  passando os IDs do programa e do shader de vértices como argumentos. 
		*/
		GL20.glAttachShader(programID, vertexShaderID);
		/**Anexa o shader de fragmentos previamente compilado ao programa de shader.
		 * Usa a função GL20.glAttachShader() para vincular o shader de fragmentos ao programa,
		 * passando os IDs do programa e do shader de fragmentos como argumentos. 
		*/
		GL20.glAttachShader(programID, fragmentShaderID);
		/**  Realiza a vinculação dos atributos do Vertex Array Object (VAO) aos atributos do shader.
		 * Chama o método bindAttributes(), que é um método abstrato declarado na classe e deve ser implementado nas subclasses. 
		*/
		bindAttributes();
		/** Realiza a linkagem do programa de shader.
		* Usa a função GL20.glLinkProgram() para vincular os shaders e criar um executável do programa. 
		*/
		
		GL20.glLinkProgram(programID);
		/** Valida o programa de shader.
		 * Usa a função GL20.glValidateProgram() para validar o programa, 
		 * arantindo que todos os shaders estejam corretamente vinculados. 
		*/
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}

	// O método getAllUniformLocations é declarado como abstrato e deve ser implementado nas subclasses.
	// Ele é responsável por obter todas as localizações das variáveis uniformes no shader.
	protected abstract void getAllUniformLocations();

	// O método getUniformLocation é usado para obter a localização (ID) de uma variável uniforme no shader pelo seu nome.
	protected int getUniformLocation (String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	public void start(){
		// Ativa o programa de shader identificado pelo programID. Isso significa que o programa de shader será usado durante o processo de renderização e substituirá os shaders previamente ativos.
		GL20.glUseProgram(programID);
	}
	
	public void stop(){
		// Desativa qualquer programa de shader atualmente ativo. Isso restaura o estado padrão do OpenGL, onde não há programa de shader em uso.
		GL20.glUseProgram(0);
	}
	
	public void cleanUp(){
		//Chama o método stop() para garantir que nenhum programa de shader esteja ativo antes de realizar a limpeza.
		stop();
		// Desvincula o shader de vértices (vertexShaderID) do programa de shader (programID).
		GL20.glDetachShader(programID, vertexShaderID);
		//Desvincula o shader de fragmentos (fragmentShaderID) do programa de shader (programID).
		GL20.glDetachShader(programID, fragmentShaderID);
		//Deleta o shader de vértices (vertexShaderID) da memória do OpenGL.
		GL20.glDeleteShader(vertexShaderID);
		//Deleta o shader de fragmentos (fragmentShaderID) da memória do OpenGL.
		GL20.glDeleteShader(fragmentShaderID);
		//Deleta o programa de shader (programID) da memória do OpenGL.
		GL20.glDeleteProgram(programID);
	}
	
	/** 
	 * Esse método é declarado como abstrato e é destinado a ser implementado nas subclasses.
	 * Ele será responsável por vincular os atributos dos shaders aos atributos dos vértices do modelo renderizado.
	 * Por exemplo, associar as posições dos vértices do modelo ao atributo "in_Position" no shader de vértices.
	 */
	protected abstract void bindAttributes();

	// Função para carregar um valor de tipo float para uma variável uniforme no shader.
	// Parâmetros:
	//   - location: O endereço da variável uniforme no shader onde o valor float será carregado.
	//   - value: O valor float que será carregado na variável uniforme.
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	// Função para carregar um vetor de tipo Vector3f (provavelmente uma classe que representa um vetor 3D)
	// para uma variável uniforme no shader.
	// Parâmetros:
	//   - location: O endereço da variável uniforme no shader onde os valores do vetor serão carregados.
	//   - vector: O objeto Vector3f contendo os valores x, y e z do vetor a ser carregado.
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	// Função para carregar um valor booleano (true ou false) para uma variável uniforme no shader.
	// Os shaders geralmente trabalham com valores float, então um valor booleano é convertido para 0 (false) ou 1 (true).
	// Parâmetros:
	//   - location: O endereço da variável uniforme no shader onde o valor booleano será carregado.
	//   - value: O valor booleano a ser carregado (true ou false).
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if (value) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}

	// Função para carregar uma matriz 4x4 (Matrix4f) para uma variável uniforme no shader.
	// Parâmetros:
	//   - location: O endereço da variável uniforme no shader onde a matriz será carregada.
	//   - matrix: O objeto Matrix4f contendo os elementos da matriz 4x4 a ser carregada.
	protected void loadMatrix(int location, Matrix4f matrix) {
		// Armazena a matriz no buffer para enviá-la ao shader.
		matrix.store(matrixBuffer);
		// Prepara o buffer para ser lido, definindo a posição para o início do buffer e a limitação para o fim dos dados.
		matrixBuffer.flip();
		// Envia a matriz (buffer) para a variável uniforme no shader.
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}

	protected void bindAttribute(int attribute, String variableName){
		/**
		 * Vincula um atributo do VAO (Vertex Array Object) a um atributo do shader usando seu nome.
		 * Isso permite que o OpenGL saiba como os dados dos atributos do VAO estão conectados aos atributos do shader durante a renderização.
		 */
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	// O método loadShader é uma função auxiliar para carregar e compilar um shader a partir de um arquivo.
	private static int loadShader(String file, int type){
		// Cria um objeto StringBuilder chamado shaderSource. O StringBuilder será usado para armazenar o código-fonte do shader lido do arquivo.
		StringBuilder shaderSource = new StringBuilder();
		try{
			//Cria um objeto BufferedReader, que será usado para ler o arquivo de shader. O BufferedReader é utilizado para ler o arquivo linha por linha.
			BufferedReader reader = new BufferedReader(new FileReader(file));
			//Cria uma variável line para armazenar cada linha lida do arquivo.
			String line;
			//Lê cada linha do arquivo usando reader.readLine() até que não haja mais linhas a serem lidas (null é retornado). O bloco de código dentro do loop é executado para cada linha lida.
			while((line = reader.readLine())!=null){
				/**
				 * Adiciona a linha lida ao objeto StringBuilder shaderSource, seguido por // e uma quebra de linha.
				 * Isso é feito para que cada linha do shader tenha um comentário
				 * // no final, evitando problemas de compilação caso a última linha não termine com uma quebra de linha.
				 */
				shaderSource.append(line).append("//\n");
			}
			//Fecha o BufferedReader após terminar a leitura do arquivo. Isso libera os recursos associados ao arquivo.
			reader.close();

		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}

		//Cria um shader (vertex ou fragment shader) do tipo especificado (GL20.GL_VERTEX_SHADER ou GL20.GL_FRAGMENT_SHADER) usando glCreateShader() do OpenGL. Esse método retorna o ID do shader criado.
		int shaderID = GL20.glCreateShader(type);
		//Carrega o código-fonte do shader no shader recém-criado. shaderSource contém todo o código-fonte do shader, incluindo os comentários adicionados anteriormente.
		GL20.glShaderSource(shaderID, shaderSource);
		//Compila o shader usando o código-fonte carregado. Após essa chamada, o shader estará pronto para uso.
		GL20.glCompileShader(shaderID);

		// Verifica se a compilação do shader foi bem-sucedida. glGetShaderi() retorna o status da compilação do shader.
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;
	}

}
