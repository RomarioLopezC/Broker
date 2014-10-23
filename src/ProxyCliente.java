import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Romario
 */
public class ProxyCliente {

    private ArrayList<Candidatos> candidatos;
    private String stringDatosServicio;
    private static final int COMANDOENVIADO = 1;
    private static final int TERMINO = 2;
    private static int STATE = 0;

    /**
     * @return the candidatos
     */
    public ArrayList<Candidatos> getCandidatos() {
        return candidatos;
    }

    /**
     * @return the datosServicio
     */
    public String getString_DatosDeServicio() {
        return stringDatosServicio;
    }

    /**
     * @return the STATE
     */
    public static int getSTATE() {
        return STATE;
    }

    /**
     * @param candidatos the candidatos to set
     */
    public void setCandidatos(ArrayList<Candidatos> candidatos) {
        this.candidatos = candidatos;
    }

    /**
     * @param datosServicio the datosServicio to set
     */
    public void setString_DatosDeServicio(String datosServicio) {
        this.stringDatosServicio = datosServicio;
    }

    /**
     * @param aSTATE the STATE to set
     */
    public static void setSTATE(int aSTATE) {
        STATE = aSTATE;
    }

    public void enviarSolicitud(ArrayList<Candidatos> candidatos, String host, String port) {
        this.setCandidatos(candidatos);
        String nombreBroker = host;
        int puertoBroker = Integer.parseInt(port);
        try (
                Socket brokerSocket = new Socket(nombreBroker, puertoBroker);
                PrintWriter aBroker = new PrintWriter(brokerSocket.getOutputStream(), true);
                BufferedReader deBroker = new BufferedReader(
                        new InputStreamReader(brokerSocket.getInputStream()));) {
            
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromBroker;
            String deCliente;

            while ((fromBroker = deBroker.readLine()) != null) {
                System.out.println("**Broker: " + fromBroker);
                if (fromBroker.toLowerCase().contains("terminar")) {
                    //lo ponemos en 0, para permitir que siga la ejecución.
                    setSTATE(0);
                    break;
                }
                if (getSTATE() != COMANDOENVIADO) {
                    System.out.print(">>");
                    if ((deCliente = stdIn.readLine()) != null) {
                        if (deCliente.toLowerCase().contains("enviar")) {
                            empaquetarDatos();
                            deCliente += "," + getString_DatosDeServicio();
                            aBroker.println(deCliente);
                        } else {
                            aBroker.println(deCliente);                            
                        }
                        setSTATE(COMANDOENVIADO);
                    }
                }else{
                    if (getSTATE() == TERMINO){
                        System.out.println("\n\n***Se cerrará la aplicación***\n\n");
                        System.exit(0);
                    }
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("No se conoce la ip: " + nombreBroker);
        } catch (IOException e) {
            System.err.println("No se pudo conectar al Broker, asegurate de que este corriendo "
                    + nombreBroker);
        }

    }

    public void empaquetarDatos() {
        setString_DatosDeServicio("");

        for (Candidatos cand : getCandidatos()) {
            //empaquetamos los datos, en string.
            String cadena = cand.getId() + "&" + cand.getNombre() + "&" + cand.getVotos() + "%";
            setString_DatosDeServicio(getString_DatosDeServicio() + cadena);
        }

        //System.out.println(datosServicio);
    }
}
