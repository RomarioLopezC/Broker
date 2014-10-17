import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Romario
 */
public class ProxyCliente {

    ArrayList<Candidatos> candidatos;
    public String datosServicio;
    private static final int COMANDOENVIADO = 1;
    private static final int TERMINO = 2;
    private static int STATE = 0;
    
    public void sendRequest(ArrayList candidatos,String host, String port) {
        this.candidatos = candidatos;
        String hostNameBroker = host;
        int portNumberBroker = Integer.parseInt(port);
        try (
                Socket kkSocket = new Socket(hostNameBroker, portNumberBroker);
                PrintWriter aBroker = new PrintWriter(kkSocket.getOutputStream(), true);
                BufferedReader deBroker = new BufferedReader(
                        new InputStreamReader(kkSocket.getInputStream()));) {
            BufferedReader stdIn
                    = new BufferedReader(new InputStreamReader(System.in));
            String fromBroker;
            String fromClient;

            while ((fromBroker = deBroker.readLine()) != null) {
                System.out.println("**Broker: " +fromBroker);
                if (fromBroker.toLowerCase().contains("terminar")) {
                    //System.out.println("Broker: " + fromBroker);
                    STATE=0;
                    break;
                }

                //fromClient = clienteDice;
                if(STATE!=COMANDOENVIADO){
                    System.out.print(">>");
                if ((fromClient=stdIn.readLine()) != null) {
                        if (fromClient.toLowerCase().contains("enviar")) {
                            packData();
                            fromClient += "," + datosServicio;
                            aBroker.println(fromClient);
                        } else{
                            aBroker.println(fromClient);
                            //aBroker.println(fromClient);
                        }
                        STATE = COMANDOENVIADO;
                    }
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostNameBroker);
        } catch (IOException e) {
            System.err.println("No se pudo conectar al Broker, asegurate de que este corriendo "
                    + hostNameBroker);
        }

    }

    public void packData() {
        datosServicio = "";

        for (Candidatos cand : candidatos) {
            String dato = cand.getNombre() + "|" + cand.getVotos() + "%";
            datosServicio += dato;
        }

        //System.out.println(datosServicio);
    }
}
