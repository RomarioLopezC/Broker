
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
    public ProxyServidor(){
        this.servidor = new Servidor();
    }
    public void conectarServidor(String input){
    try (
                ServerSocket serverSocket = new ServerSocket(4444);
                Socket clientSocket = serverSocket.accept();
                PrintWriter outBroker = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader inBroker = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));) {
            
            System.out.println("Broker conectado: " + clientSocket.getInetAddress());
            outBroker.println("Te has conectado al servidor satisfactoriamente");
            String inputLine, outputLine;

            
            // Initiate conversation with client
            inputLine = inBroker.readLine();
                System.out.println("Broker: " + inputLine);
                if ((inputLine.equalsIgnoreCase("letras"))) {
                    int[] numeros = unpackData(input);
                    outBroker.println(servidor.letrasNumeros(numeros));
                }

            
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + 4444 + " or listening for a connection");
            System.out.println(e.getMessage());
        }
}

    private int[] unpackData(String input) {
        String[] datos = input.split(",")[1].split("|");
        int[] numeros = new int[datos.length];
        for (int i = 0; i < datos.length; i++) {
            numeros[i] = Integer.parseInt(datos[i]);
        }
        
        return numeros;
    }
}
