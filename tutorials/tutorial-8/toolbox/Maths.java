package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths {

    // Cria uma matriz de transformação com base nos parâmetros de posição, rotação e escala
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
            float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        
        // Inicializa a matriz como uma matriz identidade (todos os elementos são 0 exceto a diagonal principal, que é 1)
        matrix.setIdentity();
        
        // Realiza uma translação na matriz usando os valores de posição (translation) passados como parâmetros
        Matrix4f.translate(translation, matrix, matrix);
        
        // Realiza rotações na matriz nos eixos X, Y e Z, usando os ângulos passados como parâmetros
        // As rotações são realizadas em relação ao sistema de coordenadas da matriz (local)
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        
        // Realiza uma escala na matriz, ajustando os valores nos eixos X, Y e Z, usando o valor passado como parâmetro (scale)
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        
        // Retorna a matriz de transformação final
        return matrix;
    }

    // Cria uma matriz de visualização (view matrix) com base nos parâmetros da câmera
    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        
        // Inicializa a matriz de visualização como uma matriz identidade
        viewMatrix.setIdentity();
        
        // Realiza rotações inversas (negativas) na matriz de visualização para simular a rotação da câmera
        // As rotações são realizadas nos eixos X e Y, usando os ângulos de pitch e yaw da câmera
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        
        // Obtém a posição da câmera
        Vector3f cameraPos = camera.getPosition();
        
        // Cria um vetor com a posição negativa da câmera (inverte os valores dos eixos para simular o movimento da câmera)
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        
        // Realiza uma translação inversa (negativa) na matriz de visualização para simular o movimento da câmera
        // Move a cena na direção oposta à posição da câmera
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        
        // Retorna a matriz de visualização final
        return viewMatrix;
    }
}
