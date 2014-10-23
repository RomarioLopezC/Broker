/**
 *
 * @author Romario
 */
public class Candidatos {
    private String nombre;
    private int votos;
    private int id;
    
    
    public Candidatos(int id,String nombre, int votos) {
        this.nombre = nombre.toLowerCase();
        this.votos = votos;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void incrementarVotos(){
        this.setVotos(this.getVotos() + 1);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }

    @Override
    public String toString() {
        return getId() + "|" + getNombre() + "|" + getVotos();
    } 
    
    
    
}
