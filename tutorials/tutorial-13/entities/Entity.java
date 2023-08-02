// Importando classes necessárias para o funcionamento da entidade
package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

// Definindo a classe da entidade
public class Entity {

    // Atributos da entidade
    private TexturedModel model; // Modelo texturizado da entidade
    private Vector3f position; // Posição da entidade no espaço 3D (coordenadas x, y, z)
    private float rotX, rotY, rotZ; // Rotação da entidade em torno dos eixos x, y, z
    private float scale; // Escala da entidade

    // Construtor da entidade que recebe todos os parâmetros para inicializar seus atributos
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model; // Define o modelo da entidade
        this.position = position; // Define a posição inicial da entidade
        this.rotX = rotX; // Define a rotação inicial em torno do eixo x
        this.rotY = rotY; // Define a rotação inicial em torno do eixo y
        this.rotZ = rotZ; // Define a rotação inicial em torno do eixo z
        this.scale = scale; // Define a escala inicial da entidade
    }

    // Método para incrementar a posição da entidade em um deslocamento específico
    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx; // Incrementa a coordenada x da posição da entidade
        this.position.y += dy; // Incrementa a coordenada y da posição da entidade
        this.position.z += dz; // Incrementa a coordenada z da posição da entidade
    }

    // Método para incrementar a rotação da entidade em valores específicos
    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx; // Incrementa a rotação em torno do eixo x
        this.rotY += dy; // Incrementa a rotação em torno do eixo y
        this.rotZ += dz; // Incrementa a rotação em torno do eixo z
    }

    // Métodos de acesso aos atributos da entidade

    public TexturedModel getModel() {
        return model; // Retorna o modelo texturizado da entidade
    }

    public void setModel(TexturedModel model) {
        this.model = model; // Define um novo modelo texturizado para a entidade
    }

    public Vector3f getPosition() {
        return position; // Retorna a posição da entidade no espaço 3D
    }

    public void setPosition(Vector3f position) {
        this.position = position; // Define uma nova posição para a entidade
    }

    public float getRotX() {
        return rotX; // Retorna a rotação atual em torno do eixo x
    }

    public void setRotX(float rotX) {
        this.rotX = rotX; // Define um novo valor de rotação em torno do eixo x
    }

    public float getRotY() {
        return rotY; // Retorna a rotação atual em torno do eixo y
    }

    public void setRotY(float rotY) {
        this.rotY = rotY; // Define um novo valor de rotação em torno do eixo y
    }

    public float getRotZ() {
        return rotZ; // Retorna a rotação atual em torno do eixo z
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ; // Define um novo valor de rotação em torno do eixo z
    }

    public float getScale() {
        return scale; // Retorna a escala atual da entidade
    }

    public void setScale(float scale) {
        this.scale = scale; // Define um novo valor de escala para a entidade
    }
}
