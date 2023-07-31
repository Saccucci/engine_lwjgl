package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJLoader {
    
    // Função responsável por carregar um modelo OBJ e retornar um RawModel.
    public static RawModel loadObjModel(String fileName, Loader loader) {
        // Inicializa o FileReader para ler o arquivo .obj.
        FileReader fr = null;
        try {
            fr = new FileReader(new File("res/" + fileName + ".obj"));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file! ");
            e.printStackTrace();
        }

        // Inicializa o BufferedReader para ler o conteúdo do arquivo linha por linha.
        BufferedReader reader = new BufferedReader(fr);
        String line;

        // Listas para armazenar informações do modelo.
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();

        // Arrays para armazenar os dados dos vértices, texturas e normais do modelo.
        float[] verticesArray = null;
        float[] normalArray = null;
        float[] textureArray = null;
        int[] indicesArray = null;

        try {
            // Primeiro, processa as linhas que contêm informações de vértices, texturas e normais.
            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");

                if (line.startsWith("v ")) {
                    // Processa um vértice (x, y, z) e o adiciona à lista de vértices.
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    // Processa uma coordenada de textura (u, v) e a adiciona à lista de texturas.
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    // Processa um vetor normal (x, y, z) e o adiciona à lista de normais.
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    // Quando encontra uma face (triângulo), inicializa os arrays para texturas e normais
                    // e interrompe o loop para começar a processar as faces.
                    textureArray = new float[vertices.size() * 2];
                    normalArray = new float[vertices.size() * 3];
                    break;
                }
            }

            // Agora, processa as linhas que contêm informações sobre as faces do modelo.
            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[3].split("/");
                String[] vertex3 = currentLine[2].split("/");

                // Processa cada vértice da face e adiciona seus índices aos arrays de índices,
                // bem como atualiza os arrays de texturas e normais correspondentes.
                processVertex(vertex1, indices, textures, normals, textureArray, normalArray);
                processVertex(vertex2, indices, textures, normals, textureArray, normalArray);
                processVertex(vertex3, indices, textures, normals, textureArray, normalArray);
                line = reader.readLine();
            }

            // Fecha o BufferedReader após processar o arquivo.
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Converte as listas de vértices e índices em arrays.
        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            // Transforma as coordenadas dos vértices em um único array.
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            // Transforma a lista de índices em um único array.
            indicesArray[i] = indices.get(i);
        }

        // Retorna o RawModel criado a partir dos arrays de vértices, texturas e índices.
        return loader.loadToVAO(verticesArray, textureArray, indicesArray);
    }

    // Função auxiliar para processar um vértice de uma face e atualizar as listas de texturas e normais.
    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures,
    List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        // Converte o índice do vértice da string para um número inteiro e subtrai 1, pois os índices em OBJ começam em 1
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        // Adiciona o índice do vértice à lista de índices
        indices.add(currentVertexPointer);
        
        // Obtém as coordenadas de textura com base no índice fornecido e as armazena no textureArray
        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        // Atribui a coordenada s do vértice atual no textureArray
        textureArray[currentVertexPointer * 2] = currentTex.x;
        // Atribui a coordenada t (1 - y) do vértice atual no textureArray (a textura é invertida no eixo y no OpenGL)
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
        
        // Obtém as coordenadas da normal com base no índice fornecido e as armazena no normalsArray
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        // Atribui o componente x da normal do vértice atual no normalsArray
        normalsArray[currentVertexPointer * 3] = currentNorm.x;
        // Atribui o componente y da normal do vértice atual no normalsArray
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        // Atribui o componente z da normal do vértice atual no normalsArray
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
    }

}
