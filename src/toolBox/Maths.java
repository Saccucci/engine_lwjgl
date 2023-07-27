// Importação das classes necessárias para a implementação da função
package toolBox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Maths {
    
    // Função para criar uma matriz de transformação 4x4 (usada para transformar vértices no espaço do mundo)
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, Float rz, float scale) {
        // Criação da matriz 4x4
        Matrix4f matrix = new Matrix4f();
        
        // Define a matriz como uma matriz identidade (inicialmente, sem transformações)
        matrix.setIdentity();
        
        // Aplica a transformação de translação à matriz
        Matrix4f.translate(translation, matrix, matrix);
        
        // Aplica a transformação de rotação em torno do eixo X à matriz, utilizando o ângulo rx em radianos
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        
        // Aplica a transformação de rotação em torno do eixo Y à matriz, utilizando o ângulo ry em radianos
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        
        // Aplica a transformação de rotação em torno do eixo Z à matriz, utilizando o ângulo rz em radianos
        // Obs: rz é um Float opcional, permitindo que a rotação em torno do eixo Z seja ignorada caso seja nulo
        if (rz != null) {
            Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        }
        
        // Aplica a transformação de escala à matriz, utilizando a escala no eixo X, Y e Z, sendo todas iguais (scale)
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        
        // Retorna a matriz de transformação resultante
        return matrix;
    }
}
