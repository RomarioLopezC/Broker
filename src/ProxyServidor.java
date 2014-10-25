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
 * @author Romario
 */
public class ProxyServidor {

    Servidor servidor;
    ArrayList<String> cand;

    public ProxyServidor() {
        this.servidor = new Servidor();
        this.cand = new ArrayList<>();
    }

    public void conectarServidor() {
        while (true) {
            try (
                    ServerSocket serverSocket = new ServerSocket(4444);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter aBroker = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader deBroker = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));) {

                System.out.println("Broker conectado: " + clientSocket.getInetAddress());
                aBroker.println("Te has conectado al servidor satisfactoriamente");
                String inputLine;

                // Initiate conversation with client
                inputLine = deBroker.readLine();
                System.out.println("Broker: " + inputLine);
                try {
                    String respuestaDeServidor = "";

                    if ((inputLine.contains("pastel"))) {
                        cand = desEmpaquetarDatos(inputLine);

                        respuestaDeServidor = servidor.graficarPastel(cand);
                        aBroker.println(respuestaDeServidor);

                    } else if ((inputLine.contains("barras"))) {
                        cand = desEmpaquetarDatos(inputLine);

                        respuestaDeServidor = servidor.graficarBarras(cand);
                        aBroker.println(respuestaDeServidor);

                    } else {
                        aBroker.println("Terminar No se encuentra ese servicio, "
                                + "contacte al administrador");

                    }

                } catch (NoClassDefFoundError e) {
                    System.out.println(e.getLocalizedMessage());
                }

            } catch (IOException e) {
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
        proxyServidor.conectarServidor();
    }
}
