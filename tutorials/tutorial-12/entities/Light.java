package entities;

import org.lwjgl.util.vector.Vector3f;

// Classe Light representa uma fonte de luz no ambiente 3D
public class Light {

    // Propriedades privadas que armazenam a posição e a cor da luz
    private Vector3f position;
    private Vector3f colour;

    // Construtor da classe Light
    // Recebe a posição (x, y, z) e a cor (r, g, b) da luz como parâmetros
    public Light(Vector3f position, Vector3f colour) {
        // Atribui a posição e a cor recebidas às propriedades da classe
        this.position = position;
        this.colour = colour;
    }
    
    // Método para obter a posição da luz
    // Retorna o vetor de posição (x, y, z) da luz
    public Vector3f getPosition() {
        return position;
    }

    // Método para definir a posição da luz
    // Recebe um vetor de posição (x, y, z) como parâmetro e atualiza a posição da luz
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    // Método para obter a cor da luz
    // Retorna o vetor de cor (r, g, b) da luz
    public Vector3f getColour() {
        return colour;
    }
    
    // Método para definir a cor da luz
    // Recebe um vetor de cor (r, g, b) como parâmetro e atualiza a cor da luz
    public void setColour(Vector3f colour) {
        this.colour = colour;
    }
}

