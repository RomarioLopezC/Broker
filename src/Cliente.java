import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Romario
 */
public class Cliente {

    private ArrayList<Candidatos> candidatos = new ArrayList();
    private int id = 4;
    private ProxyCliente proxyCliente;

    /**
     * @return the candidatos
     */
    public ArrayList<Candidatos> getCandidatos() {
        return candidatos;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the proxyCliente
     */
    public ProxyCliente getProxyCliente() {
        return proxyCliente;
    }

    /**
     * @param candidatos the candidatos to set
     */
    public void setCandidatos(ArrayList<Candidatos> candidatos) {
        this.candidatos = candidatos;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param proxyCliente the proxyCliente to set
     */
    public void setProxyCliente(ProxyCliente proxyCliente) {
        this.proxyCliente = proxyCliente;
    }

    public void inicializarCandidatos() {
        getCandidatos().add(new Candidatos(1, "romario lopez", 0));
        getCandidatos().add(new Candidatos(2, "alejandro sumarraga", 0));
        getCandidatos().add(new Candidatos(3, "eduardo canche", 0));
    }

    public void iniciarTarea(String ip, String puerto) {

        Scanner scn = new Scanner(System.in);

        //creamos un nuevo proxyCliente.
        setProxyCliente(new ProxyCliente());

        //mostramos las posibles opciones que el usuario tiene:
        mostrarOpciones();

        System.out.print(">> ");
        String clienteDice = scn.nextLine().toLowerCase();
        do {
            switch (clienteDice) {
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
                    mostrarOpciones();
                    break;
                case "5":
                    solicitarServicio(ip, puerto);
                    break;
                case "cls":
                    limpiarConsola();
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
            System.out.println("\nIngrese opcion");
            System.out.print(">> ");
        } while (!(clienteDice = scn.nextLine().toLowerCase()).contains("salir"));
    }

    public void votar() {

        System.out.println("Ingresa el nombre del candidato");

        Scanner scn = new Scanner(System.in);
        String nombreCandidato = scn.nextLine().toLowerCase();

        boolean encontrado = false;

        for (Candidatos candidato : getCandidatos()) {
            if (candidato.getNombre().equals(nombreCandidato)) {
                candidato.incrementarVotos();
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("Candidato no encontrado");
        }
    }

    public void verVotos() {
        for (Candidatos candidato : getCandidatos()) {
            System.out.println("Id: " + candidato.getId() + ">>" + candidato.getNombre()
                    + " >> " + candidato.getVotos());
        }
    }

    public void agregarCandidato() {

        System.out.println("Ingresa el nombre");

        Scanner scn = new Scanner(System.in);
        String nombre = scn.nextLine();

        boolean existeCandidato = false;

        for (Candidatos cand : getCandidatos()) {
            if (cand.getNombre().equals(nombre)) {
                existeCandidato = true;
            }
        }
        if (!existeCandidato) {
            getCandidatos().add(new Candidatos(getId(), nombre, 0));
            setId(getId() + 1);
        } else {
            System.out.println("Ya existe un candidato con ese nombre.");
        }

    }

    public void mostrarOpciones() {
        System.out.println("\n\n****MODULO DE VOTOS****"
                + "\nIngrese el numero de lo que desea realizar:"
                + "\n1.- Votar"
                + "\n2.- Ver Votos"
                + "\n3.- Agregar Candidato "
                + "\n4.- Ver opciones"
                + "\n5.- Escribir comando");
    }

    public void limpiarConsola() {
        try {
            System.console().flush();
            //Runtime.getRuntime().exec("cls");

        } catch (final Exception e) {
            //  Handle any exceptions.
            System.out.println("Error Contacte al administrador del programa.");
        }
    }

    public void solicitarServicio(String ip, String puerto) {
        getProxyCliente().enviarSolicitud(getCandidatos(), ip, puerto);
        System.out.println("\n\n****EL SERVICIO HA SIDO ENVIADO****");
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
        cliente.iniciarTarea(args[0], args[1]);
    }//fin main.
}//fin clase.
