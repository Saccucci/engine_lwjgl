package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

public class MasterRenderer {

    private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
    private Matrix4f projectionMatrix;
	
    // Declaração de um objeto StaticShader e um objeto Renderer
    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();
    
    // Mapa para armazenar uma lista de entidades (Entity) associadas a cada modelo texturizado (TexturedModel)
    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();
    
    public MasterRenderer(){
        // ativa o culling/ocultação de faces
		GL11.glEnable(GL11.GL_CULL_FACE);
		// este comando esconde as faces // o Padrão é (GL11.GL_BACK) para esconder as faces traseiras/ocultas porém (GL_FRONT) foi o parametro que funcionou para esta finalidade
		GL11.glCullFace(GL11.GL_BACK);
        // GL11.glFrontFace(GL11.GL_CW);
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    // Método para renderizar todas as entidades no mundo virtual
    public void render(Light sun, Camera camera) {
        // Preparar o renderer para iniciar o processo de renderização
        prepare();
        
        // Iniciar o shader estático (StaticShader) para processar as entidades
        shader.start();
        
        // Carregar a luz do sol (sun) e a matriz de visualização da câmera (camera) no shader
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        
        // Renderizar todas as entidades presentes no mapa "entities"
        renderer.render(entities);
        
        // Parar o shader após renderização
        shader.stop();
        terrainShader.start();
        terrainShader.loadLight(sun);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
        // Limpar a lista de entidades após renderização para que esteja vazia na próxima iteração
        entities.clear();
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    // Método para processar uma entidade individual e adicioná-la à lista correta no mapa "entities"
    public void processEntity(Entity entity) {
        // Obter o modelo texturizado (TexturedModel) associado à entidade
        TexturedModel entityModel = entity.getModel();
        
        // Verificar se já existe uma lista de entidades para o modelo texturizado no mapa "entities"
        List<Entity> batch = entities.get(entityModel);
        
        // Se a lista já existe, adicionar a entidade a ela
        if (batch != null) {
            batch.add(entity);
        } else {
            // Se a lista ainda não existe, criar uma nova lista e adicioná-la ao mapa "entities" junto com a entidade
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    } 

	// Método para limpar recursos após o uso (no caso, limpar o shader)
	public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
    }

    // Prepara a tela para renderização
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Ativa o teste de profundidade
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Limpa o buffer de cores e de profundidade
		GL11.glClearColor(0.4f, 0.2f, 0.2f, 1); // Define a cor de fundo da tela (verde escuro)
	}

    // Cria a matriz de projeção
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight(); // Calcula a proporção de aspecto
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio); // Calcula a escala no eixo Y
		float x_scale = y_scale / aspectRatio; // Calcula a escala no eixo X
		float frustum_length = FAR_PLANE - NEAR_PLANE; // Calcula o comprimento do frustum

		projectionMatrix = new Matrix4f(); // Cria uma nova matriz 4x4 (matriz de projeção)
		projectionMatrix.m00 = x_scale; // Define o valor de escala no eixo X
		projectionMatrix.m11 = y_scale; // Define o valor de escala no eixo Y
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length); // Define a componente (2,2) da matriz
		projectionMatrix.m23 = -1; // Define a componente (2,3) da matriz
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length); // Define a componente (3,2) da matriz
		projectionMatrix.m33 = 0; // Define a componente (3,3) da matriz
	}

}
