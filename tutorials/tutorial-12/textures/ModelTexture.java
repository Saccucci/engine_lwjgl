package textures;

public class ModelTexture {

    // Variável para armazenar o ID da textura.
    private int textureID;

    // Variável que representa o brilho da textura.
    private float shineDamper = 1;

    // Variável que representa o nível de reflexividade da textura.
    private float reflectivity = 0;

    // Construtor da classe ModelTexture que recebe o ID da textura como argumento.
    public ModelTexture(int id) {
        this.textureID = id;
    }

    // Método para obter o ID da textura.
    public int getID() {
        return this.textureID;
    }

    // Método para obter o brilho da textura (shine damper).
    public float getShineDamper() {
        return this.shineDamper;
    }

    // Método para definir o brilho da textura (shine damper).
    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    // Método para obter o nível de reflexividade da textura.
    public float getReflectivity() {
        return this.reflectivity;
    }

    // Método para definir o nível de reflexividade da textura.
    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
