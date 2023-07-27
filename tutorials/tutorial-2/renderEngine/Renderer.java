package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

	//Declaração do método prepare(). Este método é chamado a cada quadro (frame) antes de qualquer renderização ser realizada. 
    // Ele basicamente limpa a tela de tudo o que foi renderizado no último quadro usando o método glClear(). 
    // O método glClearColor() determina a cor que será usada para limpar a tela. Neste exemplo, ele torna a tela inteira vermelha no início de cada quadro.
	public void prepare() {
		// Define a cor de limpeza da tela como vermelho puro (RGBA: 1, 0, 0, 1).
		GL11.glClearColor(1, 0, 0, 1);
		//Executa a limpeza da tela usando a cor definida anteriormente.
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

	//Declaração do método render(RawModel model). Este método é responsável por renderizar um modelo na tela.
	public void render(RawModel model) {
		// Ativa o VAO (Vertex Array Object) do modelo especificado, permitindo que ele seja renderizado.
		GL30.glBindVertexArray(model.getVaoID());
		//Ativa o atributo de VAO de índice 0, que contém os dados de posição dos vértices do modelo. Isso permite que a posição dos vértices seja enviada para a GPU para ser processada na etapa de renderização.
		GL20.glEnableVertexAttribArray(0);
		//Renderiza o VAO na tela usando a função glDrawArrays(). Neste caso, está renderizando triângulos, 
		// e o parâmetro model.getVertexCount() especifica o número de vértices que serão renderizados.
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		//Desativa o atributo de VAO de índice 0 após a renderização, para evitar conflitos com outros processos de renderização.
		GL20.glDisableVertexAttribArray(0);
		//Desativa o VAO após a renderização, limpando o estado para evitar comportamentos inesperados em outros processos de renderização.
		GL30.glBindVertexArray(0);
	}

}

/**GL11: Esta classe pertence ao LWJGL e fornece funções de ligação para a API OpenGL 1.1,
que é uma versão mais antiga do OpenGL. Ela oferece recursos básicos de renderização,
como desenho de formas simples e configurações de estado básicas.

GL20: Essa classe também faz parte do LWJGL e fornece funções de ligação para a API OpenGL 2.0,
que é uma versão mais recente do OpenGL. O OpenGL 2.0 introduziu recursos mais avançados,
como sombreadores programáveis, o que permite maior flexibilidade no processo de renderização.

GL30: Outra classe do LWJGL, que fornece funções de ligação para a API OpenGL 3.0 e versões posteriores.
O OpenGL 3.0 introduziu mudanças significativas na forma como o pipeline de renderização é gerenciado,
promovendo o uso de funcionalidades mais modernas e de alto desempenho.

As principais diferenças entre as diferentes versões do OpenGL são a disponibilidade de recursos
e a forma como o pipeline de renderização é gerenciado. À medida que o OpenGL evoluiu, foram adicionados recursos mais avançados e flexíveis,
permitindo gráficos mais realistas e eficientes. Portanto, dependendo dos recursos que você precisa e do nível de compatibilidade com sistemas
mais antigos, você pode escolher a versão do OpenGL que melhor se adapte às suas necessidades. */