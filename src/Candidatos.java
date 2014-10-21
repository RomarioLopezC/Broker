/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Romario
 */
public class Candidatos {
    String nombre;
    int votos;
    int id;
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
        this.votos ++;
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
        return id + "|" + nombre + "|" + votos;
    } 
    
    
    
}
