import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Romario
 */
public class Cliente {

    ArrayList<Candidatos> candidatos = new ArrayList();
    int id = 4;
    ProxyCliente proxyCliente;

    public void principal(String host, String port) {
        Scanner scn = new Scanner(System.in);

        proxyCliente = new ProxyCliente();

        String clienteDice;
        System.out.println("\n\n****MODULO DE VOTOS****"
                + "\n\nIngrese el el numero de lo que desea realizar:"
                + "\n1.- Votar"
                + "\n2.- Ver Votos"
                + "\n3.- Agregar Candidato "
                + "\n4.- Ver opciones"
                + "\n5.- Escribir comando "
                + "\nEscriba salir para terminar");

        System.out.print(">> ");
        while (!(clienteDice = scn.nextLine().toLowerCase()).contains("salir")) {

            String opcion = clienteDice;

            switch (opcion) {
                case "1":
                    votar();
                    break;
                case "2":
                    verVotos();
                    break;
                case "3":
                    agregarCandidato();
                    break;
                case "4":
                    System.out.println("\n\n****MODULO DE VOTOS****"
                            + "\nIngrese el numero de lo que desea realizar:"
                            + "\n1.- Votar"
                            + "\n2.- Ver Votos"
                            + "\n3.- Agregar Candidato "
                            + "\n4.- Ver opciones"
                            + "\n5.- Escribir comando");
                    break;
                case "5":
                    //clienteDice= scn.nextLine().toLowerCase();
                    proxyCliente.Connect(candidatos, host, port);
                    System.out.println("\n\n****MODULO DE VOTOS****");
                    break;
                case "cls":
                    clearConsole();
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
            System.out.println("\nIngrese opcion");
            System.out.print(">> ");
        }
        //System.out.println(clienteDice);

        //proxy.peticionDeServicio(null, numeros);
    }

    public static void clearConsole() {
        try {
            Runtime.getRuntime().exec("cls");
            
        } catch (final Exception e) {
            //  Handle any exceptions.
            System.out.println("Error Contacte al administrador del programa.");
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(
                    "Uso: java Client <host name> <port number>");
            System.err.println(
                    "Ejemplo: java Client 192.168.123.123 4444");
            System.exit(1);
        }
        Cliente cliente = new Cliente();
        cliente.inicializarCandidatos();
        cliente.principal(args[0], args[1]);
    }

    public void inicializarCandidatos() {
        candidatos.add(new Candidatos(1, "romario"));
        candidatos.add(new Candidatos(2, "alejandro"));
        candidatos.add(new Candidatos(3, "eduardo"));
    }

    public void agregarCandidato() {
        Scanner scn = new Scanner(System.in);
        System.out.println("Ingresa el nombre");
        candidatos.add(new Candidatos(id, scn.nextLine()));
        id++;
    }

    public void verVotos() {
        for (Candidatos cand : candidatos) {
            System.out.println("Id: " + cand.getId() + "  " + cand.getNombre() + " >> " + cand.getVotos());
        }
    }

    public void votar() {
        boolean encontrado = false;
        Scanner scn = new Scanner(System.in);
        System.out.println("Ingresa el nombre del candidato");
        String a = scn.nextLine().toLowerCase();
        
        for (Candidatos cand : candidatos) {
            if (cand.getNombre().equals(a)) {
                cand.incrementarVotos();
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("Candidato no encontrado");
        }
    }
}
