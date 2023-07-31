package models;

import textures.ModelTexture;

// Define a classe TexturedModel
public class TexturedModel {

    // Declaração de atributos da classe
    private RawModel rawModel; // Representa o modelo 3D não texturizado (malha) carregado no VAO
    private ModelTexture texture; // Representa a textura que será aplicada ao modelo 3D

    // Construtor da classe TexturedModel
    public TexturedModel(RawModel model, ModelTexture texture) {
        this.rawModel = model; // Inicializa o atributo rawModel com o modelo passado como parâmetro
        this.texture = texture; // Inicializa o atributo texture com a textura passada como parâmetro
    }

    // Método para obter o modelo 3D não texturizado (malha) associado a este TexturedModel
    public RawModel getRawModel() {
        return rawModel; // Retorna o modelo 3D não texturizado (malha) associado a este TexturedModel
    }

    // Método para obter a textura associada a este TexturedModel
    public ModelTexture getTexture() {
        return texture; // Retorna a textura associada a este TexturedModel
    }
}
