import BrokerAPI.API_BROKER;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Lalo
 */
public class ProxyServidor {

    Servidor servidor;
    ArrayList<String> cand;
    ArrayList<Servers> servicios;

    API_BROKER API;

    public ProxyServidor() {
        this.servidor = new Servidor();
        this.cand = new ArrayList<>();
        servicios = new ArrayList<>();
        //inicializarServicios();

        API = new API_BROKER();
    }

    public void conectarServidor() {
        
        
        while (true) {
            try (
                    
                    ServerSocket serverSocket = new ServerSocket(4444);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter aBroker = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader deBroker = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));) {

                /*Aquí contestamos al broker, para decirle que ha sido
                 conectado con el proxyServidor y servidor.*/
                Bitacoras.escribirBitacoraProxyServidor("Broker conectado: " + clientSocket.getInetAddress());
                System.out.println("Broker conectado: " + clientSocket.getInetAddress());
                aBroker.println("Te has conectado al servidor satisfactoriamente");
                /*El servidor solo pinta, por eso entre el proxyServidor y Servidor,
                 se conectan mediante Objetos.*/
                String inputLine;

                // Inicia Conversación con el broker.
                inputLine = deBroker.readLine();
                
                try {
                    String respuestaDeServidor = "";

                    if ((inputLine.contains("pastel"))) {

                        cand = desEmpaquetarDatos(inputLine);
                        //Aquí llamamos al servidor para que pinte,
                        //y éste a su vez contesta :
                        respuestaDeServidor = servidor.graficarPastel(cand);
                        //la respuesta la imprimimos en la consola del broker:
                        aBroker.println(respuestaDeServidor);

                    } else if ((inputLine.contains("barras"))) {
                        cand = desEmpaquetarDatos(inputLine);

                        respuestaDeServidor = servidor.graficarBarras(cand);
                        aBroker.println(respuestaDeServidor);

                    } else {
                        aBroker.println("Terminar; No se encuentra ese servicio, "
                                + "contacte al administrador");

                    }

                } catch (NoClassDefFoundError e) {
                    System.out.println(e.getLocalizedMessage());
                }

            } catch (IOException e) {
                Bitacoras.escribirBitacoraProxyServidor("Exception caught when trying to listen on port "
                        + 4444 + " or listening for a connection" + e.getMessage());
                System.out.println("Exception caught when trying to listen on port "
                        + 4444 + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }

    private ArrayList<String> desEmpaquetarDatos(String cadenaDatos) {

        //separamos el String que tiene toda la información:
        String datos = cadenaDatos.split(",")[1];

        /*en infoCandidatos tendremos la información de los candidatos
         pero cada casilla es de esta forma:
         infoCandidatos[0] = "1&eduardo canche&5"
         primero el id, luego el nombre, luego los votos.*/
        String[] infoCandidatos = datos.split("%");

        ArrayList<String> candidatos = new ArrayList<>();
        StringTokenizer tokenCandidatos;

        for (String candidato : infoCandidatos) {
            tokenCandidatos = new StringTokenizer(candidato, "&");

            //obtiene el id:
            candidatos.add(tokenCandidatos.nextToken());

            //obtiene el nombre:
            candidatos.add(tokenCandidatos.nextToken());

            //obtiene los votos:
            candidatos.add(tokenCandidatos.nextToken());
        }

        return candidatos;
    }

    public static void main(String[] args) {
        ProxyServidor proxyServidor = new ProxyServidor();
        API_BROKER.conectar("127.0.0.1", "4445", "127.0.0.1", "4444", "barras,pastel");
        proxyServidor.conectarServidor();
        /*necesita ipBroker, puertoBroker,puertoServidor.*/
    }
}
