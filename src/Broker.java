import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers deCliente Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template deCliente the editor.
 */
/**
 *
 * @author Romario
 */
public class Broker {

    ArrayList<Servers> ListaDeServicios = new ArrayList();

    public void startBroker(String port) {
        ListaDeServicios.add(new Servers("barras", "127.0.0.1", 4444));
        ListaDeServicios.add(new Servers("pastel", "192.168.230.150", 4444));
        ListaDeServicios.add(new Servers("tabla", "192.168.230.151", 4444));
        int portNumber = Integer.parseInt(port);

        while (true) {
            try (
                    ServerSocket brokerSocket = new ServerSocket(portNumber);
                    Socket clientSocket = brokerSocket.accept();
                    PrintWriter aCliente = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader deCliente = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));) {

                System.out.println("Servidor inicializado y corriendo");
                aCliente.println("Ingresar comando");
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                String inputLine;
                while ((inputLine = deCliente.readLine()) != null) {
                    aCliente.println("Comando recibido.");
                    System.out.println("Cliente: " + inputLine);

                    if (inputLine.toLowerCase().contains("enviar")) {
                        aCliente.println("Se procesara la solicitud");
                        servicio(inputLine, aCliente);

                    } else if (inputLine.toLowerCase().contains("agregar")) {
                        regService(inputLine, aCliente);
                    } else {
                        aCliente.println("Terminar Comando no encontrado");
                    }
                }
            } catch (IOException e) {
                System.out.println("Esta ocupado el puerto "
                        + portNumber + " intenta con otro puerto.");
                System.out.println(e.getMessage());
            }
        }

    }

    public void servicio(String input, PrintWriter outClient) {
        String servicio = (input.split(",")[0]).split(" ")[1];
        String datos = input.split(",")[1];

        int servidor = findServer(servicio);
        if (servidor == -1) {
            outClient.println("Terminar Servicio no encontrado\nPresionar Enter para continuar");
        } else {
            String hostName = ListaDeServicios.get(servidor).getIp();
            int portNumber = ListaDeServicios.get(servidor).getPort();

            try (
                    Socket kkSocket = new Socket(hostName, portNumber);
                    PrintWriter server = new PrintWriter(kkSocket.getOutputStream(), true);
                    BufferedReader inServer = new BufferedReader(
                            new InputStreamReader(kkSocket.getInputStream()));) {
                BufferedReader stdIn
                        = new BufferedReader(new InputStreamReader(System.in));
                String fromServer = null;

                server.println(input);
                fromServer = inServer.readLine();
                System.out.println("Server: " + fromServer);
                server.println(input);
                fromServer = inServer.readLine();
                outClient.println(fromServer);

            } catch (UnknownHostException e) {
                System.err.println("Don't know about hostServer " + hostName);
                System.exit(1);
            } catch (IOException e) {
                outClient.println("Terminar Servidor de servicio [" + servicio + "] caido.");
                System.err.println("No se pudo conectar a "
                        + hostName);
            }
        }

    }

    public void regService(String str, PrintWriter out) {
        String[] partida = str.split(" ");
        if (partida.length != 3) {
            out.println("Escribir> agregar IP PORT");
        } else {
            out.println("Servidor agregado> " + partida[1]);
        }
    }

    public int findServer(String servicio) {
        int num = 0;
        for (Servers serv : ListaDeServicios) {
            if (serv.getServicio().equalsIgnoreCase(servicio)) {
                return num;
            }
            num++;
        }
        return -1;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println(
                    "Uso: java Broker <port number>");
            System.err.println(
                    "Ejemplo: java Broker 4444");
            System.exit(1);
        }
        Broker broker = new Broker();
        broker.startBroker(args[0]);
    }
}
