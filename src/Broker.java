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
 * @author Lalo
 */
public class Broker {

    ArrayList<Servers> ListaDeServicios = new ArrayList();

    public void iniciarBroker(String port) {
        //192.168.230.185
        //ListaDeServicios.add(new Servers("barras", "127.0.0.1", 4444));
        //ListaDeServicios.add(new Servers("pastel", "127.0.0.1", 4444));
        ListaDeServicios.add(new Servers("tabla", "192.168.1.25", 4444));
        int puerto = Integer.parseInt(port);

        while (true) {
            try (
                    ServerSocket brokerSocket = new ServerSocket(puerto);
                    Socket clientSocket = brokerSocket.accept();
                    PrintWriter aCliente = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader deCliente = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));) {

                
                
                Bitacoras.escribirBitacoraBroker("Broker inicializado y corriendo");
                System.out.println("Broker inicializado y corriendo");
                
                aCliente.println("Ingresar comando");
                
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                Bitacoras.escribirBitacoraBroker("Cliente conectado: " + clientSocket.getInetAddress());
               
                String inputLine;
                while ((inputLine = deCliente.readLine()) != null) {
                    aCliente.println("Comando recibido.");
                    Bitacoras.escribirBitacoraBroker("Cliente: " + inputLine);
                    System.out.println("Cliente: " + inputLine);

                    if (inputLine.toLowerCase().contains("graficar")) {
                        aCliente.println("Se procesara la solicitud");
                        procesarServicio(inputLine, aCliente);

                    } else if (inputLine.toLowerCase().contains("agregarserv")) {
                        
                        registrarServicio(inputLine, clientSocket.getInetAddress().toString());

                    } else {
                        aCliente.println("Terminar, Comando NO encontrado");
                    }
                }
            } catch (IOException e) {
                Bitacoras.escribirBitacoraBroker("Esta ocupado el puerto "
                        + puerto + " intenta con otro puerto. " + e.getMessage());
                System.out.println("Esta ocupado el puerto "
                        + puerto + " intenta con otro puerto.");
                System.out.println(e.getMessage());
            }
        }

    }

    public void procesarServicio(String input, PrintWriter outClient) {
        String servicio = (input.split(",")[0]).split(" ")[1];
        String datos = input.split(",")[1];

        int servidor = encontrarServidor(servicio, outClient);
        if (servidor == -1) {
            outClient.println("Terminar, Servicio no encontrado");
        } else {
            //Que la variable servidor sea -2, Significa que si está 
            //el servicio pero está inactivo.
            //Para más información checar funcion Encontrar Servidor
            if (servidor == -2) {
                outClient.println("Terminar, Servicio inactivo");
                return;
            }
            String hostName = ListaDeServicios.get(servidor).getIp();
            int portNumber = ListaDeServicios.get(servidor).getPort();

            try (
                    Socket cliente = new Socket(hostName, portNumber);
                    PrintWriter aProxyServidor = new PrintWriter(cliente.getOutputStream(), true);
                    BufferedReader deProxyServidor = new BufferedReader(
                            new InputStreamReader(cliente.getInputStream()));) {
                BufferedReader stdIn
                        = new BufferedReader(new InputStreamReader(System.in));
                String fromServer = null;

                aProxyServidor.println(input);
                fromServer = deProxyServidor.readLine();
                
                Bitacoras.escribirBitacoraBroker("Server: " + fromServer);
                System.out.println("Server: " + fromServer);
                
                aProxyServidor.println(input);
                fromServer = deProxyServidor.readLine();
                outClient.println(fromServer);

            } catch (UnknownHostException e) {
                
                Bitacoras.escribirBitacoraBroker("Don't know about hostServer " + hostName);
                System.err.println("Don't know about hostServer " + hostName);
                
                System.exit(1);
            } catch (IOException e) {

                outClient.println("Terminar Servidor de servicio [" + ListaDeServicios.get(servidor).getServicio()
                        + "] caido. Cambiando su estado"
                        + " a Inactivo.");

                /*Aquí es donde actualizamos nuestra Base de datos de
                 servidores, ya que si  está siendo ocupado, se lanza una excepción
                 y además seteamos dicho servidor como FALSO; es decir, no se
                 encuentra disponible para dar servicio.*/
                ListaDeServicios.get(servidor).setEstaActivo(false);
                //imprimimos que es imposible conectarnos:
                Bitacoras.escribirBitacoraBroker("No se pudo conectar a "
                        + hostName);
                System.err.println("No se pudo conectar a "
                        + hostName);
            }
        }

    }

    /**
     * Esta función sirve para agregar un servidor, sin embargo no está
     * terminada. (Además no forma parte de la 2a entrega).
     *
     * @param str
     * @param ipServidor
     * @param out
     */
    public void registrarServicio(String str, String ipServidor) {
        String[] partida = str.split(" ");
        
        String servicio = partida[2];
        int puerto = Integer.parseInt(partida[1]);
        ListaDeServicios.add(new Servers(servicio, ipServidor, puerto));

    }

    /**
     * Encuentra el servidor que podrá dar el servicio que se solicita.
     *
     * @param servicio
     * @param outClient
     * @return
     */
    public int encontrarServidor(String servicio, PrintWriter outClient) {
        int num = 0;
        for (Servers serv : ListaDeServicios) {
            /*Si el servicio está, y además está activo el servidor para dar
             el servicio, entonces:*/
            if (serv.getServicio().equalsIgnoreCase(servicio) && serv.estaActivo()) {
                return num;
                /*Si el servicio está, pero el servidor está siendo ocupado
                 entonces: */
            } else if (serv.getServicio().equalsIgnoreCase(servicio) && !serv.estaActivo()) {
                outClient.println("Terminar Servicio encontrado pero está inactivo");
                return -2;
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
        broker.iniciarBroker(args[0]);
    }
}
