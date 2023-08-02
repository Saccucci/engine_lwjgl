package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

public class MasterRenderer {
	
    // Declaração de um objeto StaticShader e um objeto Renderer
    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);
    
    // Mapa para armazenar uma lista de entidades (Entity) associadas a cada modelo texturizado (TexturedModel)
    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

    // Método para renderizar todas as entidades no mundo virtual
    public void render(Light sun, Camera camera) {
        // Preparar o renderer para iniciar o processo de renderização
        renderer.prepare();
        
        // Iniciar o shader estático (StaticShader) para processar as entidades
        shader.start();
        
        // Carregar a luz do sol (sun) e a matriz de visualização da câmera (camera) no shader
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        
        // Renderizar todas as entidades presentes no mapa "entities"
        renderer.render(entities);
        
        // Parar o shader após renderização
        shader.stop();
        
        // Limpar a lista de entidades após renderização para que esteja vazia na próxima iteração
        entities.clear();
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
    }

}
