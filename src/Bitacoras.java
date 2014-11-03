
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Romario
 */
public class Bitacoras {
    private static FileWriter broker,proxyServidor,proxyCliente;
    
    
    public static void escribirBitacoraBroker(String aEscribir){
        try {
            broker = new FileWriter("bitacoraBroker.txt", true);
            BufferedWriter bw = new BufferedWriter(broker);
            bw.write(aEscribir);
            bw.newLine();
            bw.close();
            broker.close();
        } catch (IOException ex) {
        }
    }
    
    public static void escribirBitacoraProxyServidor(String aEscribir){
        try {
            proxyServidor = new FileWriter("bitacoraProxyServidor.txt", true);
            proxyServidor.write(aEscribir);
            proxyServidor.close();
        } catch (IOException ex) {
        }
    }
    
    public static void escribirBitacoraProxyCliente(String aEscribir){
        try {
            proxyCliente = new FileWriter("bitacoraProxyCliente.txt", true);
            proxyCliente.write(aEscribir);
            proxyCliente.close();
        } catch (IOException ex) {
        }
    }
    
    
    public static void main(String[] args) {
        escribirBitacoraBroker("HOLA");
        escribirBitacoraBroker("ADIOS");
    }
    
    
}
