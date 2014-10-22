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
    ArrayList<Candidatos> cand;

    public ProxyServidor() {
        this.servidor = new Servidor();
        this.cand = new ArrayList<>();
    }

    public void conectarServidor() {
        try (
                ServerSocket serverSocket = new ServerSocket(4444);
                Socket clientSocket = serverSocket.accept();
                PrintWriter aBroker = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader deBroker = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));) {

            System.out.println("Broker conectado: " + clientSocket.getInetAddress());
            aBroker.println("Te has conectado al servidor satisfactoriamente");
            String inputLine, outputLine;

            // Initiate conversation with client
            inputLine = deBroker.readLine();
            System.out.println("Broker: " + inputLine);
            if ((inputLine.contains("pastel"))) {
                cand = unpackData(inputLine);
                aBroker.println(servidor.letrasNumeros(cand));
            } else if ((inputLine.contains("barras"))) {
                cand = unpackData(inputLine);
                aBroker.println(servidor.letrasNumeros(cand));
            } else if ((inputLine.contains("tabla"))) {
                cand = unpackData(inputLine);
                aBroker.println(servidor.letrasNumeros(cand));
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + 4444 + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    private ArrayList<Candidatos> unpackData(String cadenaDatos) {
        ArrayList<Candidatos> candidatos = new ArrayList<>();

        String datos = cadenaDatos.split(",")[1];
        String[] cadCandidatos = datos.split("%");

        StringTokenizer tokenCandidatos;

        for (String candidato : cadCandidatos) {
            tokenCandidatos = new StringTokenizer(candidato, "&");
            candidatos.add(new Candidatos(Integer.parseInt(tokenCandidatos.nextToken()),
                    tokenCandidatos.nextToken(),
                    Integer.parseInt(tokenCandidatos.nextToken())));
        }

        return candidatos;
    }

    public static void main(String[] args) {
        ProxyServidor proxyServidor = new ProxyServidor();
        proxyServidor.conectarServidor();
    }
}
