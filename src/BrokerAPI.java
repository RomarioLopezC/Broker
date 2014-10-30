
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
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
public class BrokerAPI {
    
    public void conectar(String ipBroker,String puertoBroker,String ipServidor,
            String puertoProxyServidor, String servicios){
        int portBroker = Integer.parseInt(puertoBroker);
        int portProxyServidor = Integer.parseInt(puertoProxyServidor);
        
        LinkedList<String> serviciosSeparados = desEmpaquetarServicios(servicios);

          try (
                    Socket kkSocket = new Socket(ipBroker, portBroker);
                    PrintWriter aBroker = new PrintWriter(kkSocket.getOutputStream(), true);
                    BufferedReader deBroker = new BufferedReader(
                            new InputStreamReader(kkSocket.getInputStream()));) {
                BufferedReader stdIn
                        = new BufferedReader(new InputStreamReader(System.in));
                String fromBroker = null;

                //aBroker.println(input);
                fromBroker = deBroker.readLine();
                System.out.println("Broker: " + fromBroker);
                
                
                /*Registramos los servicios en el broker.*/
                registrarServicios(ipServidor, portProxyServidor, serviciosSeparados, aBroker);
                
                
                
                //aBroker.println(input);
                fromBroker = deBroker.readLine();
                //outClient.println(fromServer);

            } catch (UnknownHostException e) {
                System.err.println("Don't know about Broker " + ipBroker);
                System.exit(1);
            } catch (IOException e) {
                System.out.println("Terminar, Broker Caído.");
            }
        
    }
    /**Esta función desempaqueta los servicios que vienen de la forma:
     * "barras,pastel".
     */
    private LinkedList<String> desEmpaquetarServicios(String servicios){
        StringTokenizer tokens = new StringTokenizer(servicios,",");
        LinkedList<String> serviciosSeparados = new LinkedList<>();
        
        while(tokens.hasMoreTokens()){
            serviciosSeparados.add(tokens.nextToken());
        }
        
        return serviciosSeparados;
    }
    
    
    private void registrarServicios(String ipServidor, int puertoSevidor, 
            LinkedList<String> servicios, PrintWriter aBroker){
        
        String cad = "agregarServ "+ ipServidor+" "+puertoSevidor+" ";
        for(String servicio : servicios){
            cad+=servicio;
            aBroker.println(cad);
        }
        
        
        
    }
    
    
    
    
}
