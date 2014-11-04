package BrokerAPI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Lalo
 */
public class BrokerAPI {

    private static final int opcionAgregar = 0;
    private static final int opcionEliminar = 1;
    private static final int opcionActivar = 2;
    private static final int opcionDesactivar = 3;

    /**
     * Esta función será la encargada de registrar a los servidores (con sus
     * servicios) en el Broker correspondiente; deberá verificarse que lo
     * escrito en el archivo de configuración, concuerde con el broker activo,
     * ya que si no concuerda se lanzará un mensaje con la leyenda de que el
     * broker estará caído.
     *
     */
    public void agregarServidores() {

        try {
            FileReader f = new FileReader("ArchivoDeConfiguracion.txt");
            Scanner archivo = new Scanner(f);
            while (archivo.hasNext()) {

                StringTokenizer tokens = new StringTokenizer(archivo.nextLine(), ";");
                StringTokenizer info = new StringTokenizer(tokens.nextToken(), ",");

                conectar(info.nextToken(), info.nextToken(), info.nextToken(),
                        info.nextToken(), tokens.nextToken(), opcionAgregar);
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Error> " + ex.getLocalizedMessage());
        }

    }

    /**
     * Esta función será la encargada de eliminar un servicio, para eso necesita
     * los siguientes argumentos:
     *
     * @param ipBroker, es la ip del broker que estará corriendo y que tiene la
     * información del servidor.
     * @param puertoBroker, es el puerto del broker donde estará escuchando.
     * @param ipServidor, es el ip del servidor a eliminar.
     * @param puertoProxyServidor, es el puerto del servidor donde estará
     * escuchando. (es el que se eliminará).
     * @param servicios, los servicios del servidor que se eliminarán.
     */
    public void eliminarServidor(String ipBroker, String puertoBroker, String ipServidor,
            String puertoProxyServidor, String servicios) {

        //usando la auto-encapsulación:
        conectar(ipBroker, puertoBroker, ipServidor, puertoProxyServidor, servicios, opcionEliminar);

    }

     /**
     * Esta función será la encargada de activar un servicio, para eso necesita
     * los siguientes argumentos:
     *
     * @param ipBroker, es la ip del broker que estará corriendo y que tiene la
     * información del servidor.
     * @param puertoBroker, es el puerto del broker donde estará escuchando.
     * @param ipServidor, es el ip del servidor a activar.
     * @param puertoProxyServidor, es el puerto del servidor donde estará
     * escuchando. (es el que se activará).
     * @param servicios, los servicios del servidor que se activarán.
     */
    public void activarServidor(String ipBroker, String puertoBroker, String ipServidor,
            String puertoProxyServidor, String servicios) {

        //usando la auto-encapsulación:
        conectar(ipBroker, puertoBroker, ipServidor, puertoProxyServidor, servicios, opcionActivar);

    }
    
    /**
     * Esta función será la encargada de desactivar un servicio, para eso necesita
     * los siguientes argumentos:
     *
     * @param ipBroker, es la ip del broker que estará corriendo y que tiene la
     * información del servidor.
     * @param puertoBroker, es el puerto del broker donde estará escuchando.
     * @param ipServidor, es el ip del servidor a desactivar.
     * @param puertoProxyServidor, es el puerto del servidor donde estará
     * escuchando. (es el que se desactivará).
     * @param servicios, los servicios del servidor que se desactivarán.
     */
    public void desactivarServidor(String ipBroker, String puertoBroker, String ipServidor,
            String puertoProxyServidor, String servicios) {

        //usando la auto-encapsulación:
        conectar(ipBroker, puertoBroker, ipServidor, puertoProxyServidor, servicios, opcionDesactivar);

    }
    
    
    
    private void conectar(String ipBroker, String puertoBroker, String ipServidor,
            String puertoProxyServidor, String servicios, int opcion) {
        System.out.println(ipBroker + " " + servicios);
        int portBroker = Integer.parseInt(puertoBroker);
        int portProxyServidor = Integer.parseInt(puertoProxyServidor);

        LinkedList<String> serviciosSeparados = desEmpaquetarServicios(servicios);

        try (
                Socket brokerSocket = new Socket(ipBroker, portBroker);
                PrintWriter aBroker = new PrintWriter(brokerSocket.getOutputStream(), true);
                BufferedReader deBroker = new BufferedReader(
                        new InputStreamReader(brokerSocket.getInputStream()));) {

            String fromBroker = null;
            
            fromBroker = deBroker.readLine();

            switch (opcion) {
                case opcionAgregar:
                    /*Registramos los servicios en el broker.*/
                    System.out.println("Estamos agregando los servicios al broker, a través de la API...");
                    aBroker.println("Estamos agregando los servicios al broker, a través de la API...");
                    registrarServicios(ipServidor, portProxyServidor, serviciosSeparados, aBroker);
                    break;

                case opcionEliminar:
                    //llamamos a la función que eliminará los servicios.
                    System.out.println("Estamos Eliminando los servicios al broker, a través de la API...");
                    aBroker.println("Estamos Eliminando los servicios al broker, a través de la API...");
                    removerServidor(puertoProxyServidor, serviciosSeparados, aBroker);
                    break;
                case opcionActivar:
                    //llamamos a la función que activará algún servicio.
                    System.out.println("Estamos activando los servicios al broker, a través de la API...");
                    aBroker.println("Estamos activando los servicios al broker, a través de la API...");
                    activarUnServicio(puertoProxyServidor, serviciosSeparados, aBroker);
                    break;
                case opcionDesactivar:
                    //llamamos a la función que desactivará algún servicio.
                    System.out.println("Estamos desactivando los servicios al broker, a través de la API...");
                    aBroker.println("Estamos desactivando los servicios al broker, a través de la API...");
                    desactivarUnServicio(puertoProxyServidor, serviciosSeparados, aBroker);
                    break;
            }

            
            fromBroker = deBroker.readLine();
            

        } catch (UnknownHostException e) {
            System.err.println("Don't know about Broker " + ipBroker);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Terminar, Broker Caído.");
        }

    }

    /**
     * Esta función desempaqueta los servicios que vienen de la forma:
     * "barras,pastel".
     */
    private LinkedList<String> desEmpaquetarServicios(String servicios) {
        StringTokenizer tokens = new StringTokenizer(servicios, ",");
        LinkedList<String> serviciosSeparados = new LinkedList<>();

        while (tokens.hasMoreTokens()) {
            serviciosSeparados.add(tokens.nextToken());
        }
        return serviciosSeparados;
    }

    private void mandarInfoParaBroker(String cad, LinkedList<String> servicios, PrintWriter aBroker) {
        String temp = "";
        for (String servicio : servicios) {
            temp = cad;
            temp += servicio;
            aBroker.println(temp);
        }
    }

    private void registrarServicios(String ipServidor, int puertoSevidor,
            LinkedList<String> servicios, PrintWriter aBroker) {

        System.out.println("Se está agregando un servidor, con la API.");

        String cad = "agregarServ " + ipServidor + " " + puertoSevidor + " ";

        mandarInfoParaBroker(cad, servicios, aBroker);
    }

    private void removerServidor(String puertoServidor, LinkedList<String> servicios, PrintWriter aBroker) {
        System.out.println("Se está elimminando un servidor, con la API.");

        String cad = "eliminarServ " + " " + puertoServidor + " ";

        mandarInfoParaBroker(cad, servicios, aBroker);
    }

    private void desactivarUnServicio(String puertoServidor, LinkedList<String> servicios, PrintWriter aBroker) {
        System.out.println("Se está desactivando un servicio, con la API.");

        String cad = "desactivarServ " + " " + puertoServidor + " ";

        mandarInfoParaBroker(cad, servicios, aBroker);
    }

    private void activarUnServicio(String puertoServidor, LinkedList<String> servicios, PrintWriter aBroker) {
        System.out.println("Se está activando un servicio, con la API.");

        String cad = "activarServ " + " " + puertoServidor + " ";

        mandarInfoParaBroker(cad, servicios, aBroker);
    }

}
