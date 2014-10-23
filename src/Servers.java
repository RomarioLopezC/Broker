

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Romario
 */
public class Servers {
    String ip;
    int port;
    private boolean estaActivo;

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
    String servicio;
    public Servers(String servicio, String ip, int port){
        this.ip = ip;
        this.port = port;
        this.servicio=servicio.toLowerCase();
        estaActivo = true;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return " >> "+this.ip + " : " + this.port;//To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the estaActivo
     */
    public boolean isEstaActivo() {
        return estaActivo;
    }

    /**
     * @param estaActivo the estaActivo to set
     */
    public void setEstaActivo(boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
}
