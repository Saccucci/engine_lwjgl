package models;

// Declaração da classe RawModel.
public class RawModel {

	// Declaração de uma variável de instância vaoID, que armazena o ID do VAO (Vertex Array Object) que contém os dados do modelo.
	private int vaoID;
	
	// Declaração de uma variável de instância vertexCount, que armazena o número de vértices no modelo.
	private int vertexCount;

	// Declaração do construtor da classe RawModel. O construtor é chamado quando um novo objeto RawModel é criado. Recebe dois argumentos: vaoID e vertexCount.
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	// Declaração do método público getVaoID(), que permite que outras classes obtenham o valor do atributo vaoID.
	public int getVaoID() {
		return vaoID;
	}
	
	// Declaração do método público getVertexCount(), que permite que outras classes obtenham o valor do atributo vertexCount.
	public int getVertexCount() {
		return vertexCount;
	}

}
