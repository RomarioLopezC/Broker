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
 * @author Lalo
 */
public class Bitacoras {

    private static FileWriter broker, proxyServidor, proxyCliente;

    public static void escribirBitacoraBroker(String aEscribir) {
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

    public static void escribirBitacoraProxyServidor(String aEscribir) {
        try {
            proxyServidor = new FileWriter("bitacoraProxyServidor.txt", true);
            BufferedWriter bw = new BufferedWriter(proxyServidor);
            bw.write(aEscribir);
            bw.newLine();
            bw.close();

            proxyServidor.close();
        } catch (IOException ex) {
        }
    }

    public static void escribirBitacoraProxyCliente(String aEscribir) {
        try {
            proxyCliente = new FileWriter("bitacoraProxyCliente.txt", true);
            BufferedWriter bw = new BufferedWriter(proxyCliente);
            bw.write(aEscribir);
            bw.newLine();
            bw.close();
            proxyCliente.close();
        } catch (IOException ex) {
        }
    }
}
