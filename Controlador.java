
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Clase encargada de gestionar la lógica del juego.
 * 
 * @author Jose Luis Moreno Torés y Sergio Ramírez Pla
 * @version GPL v_3
 * @date 01/04/2025
 * @description Gestiona la carga de usuarios, escenarios y la interacción con
 *              el jugador.
 */
public class Controlador {
    private static final String ARCHIVO_USUARIOS = "usuarios.bin";
    private static final String CARPETA_ESCENARIOS = "escenarios/"; // Ruta donde están los escenarios

    /**
     * Método que inicia el flujo de la aplicación.
     */
    public static void ejecutar() {
        Scanner teclado = new Scanner(System.in);
        Map<String, String> usuarios = cargarUsuarios();
        String correo = "";
        String numEscenario = "";

        // Interacción con el usuario
        System.out.print("Ingrese su nombre de usuario: ");
        String usuario = teclado.nextLine();

        if (usuarios.containsKey(usuario)) {
            System.out.println("Usuario encontrado. Correo: " + usuarios.get(usuario));
        } else {
            System.out.print("Usuario no encontrado. Ingrese su correo: ");
            correo = teclado.nextLine();
            usuarios.put(usuario, correo);
            guardarUsuarios(usuarios);
            System.out.println("Usuario registrado con éxito.");
        }

        // Listar los escenarios disponibles
        mostrarEscenariosDisponibles();

        // Pedir al usuario el número del escenario que quiere ver
        System.out.print("Ingrese el número de escenario que desea ver: ");
        numEscenario = teclado.nextLine();
        mostrarEscenario(numEscenario);
        teclado.close();
    }

    /**
     * Carga los usuarios desde el archivo binario.
     * 
     * @return un mapa de usuarios donde la clave es el nombre de usuario y el valor
     *         es el correo electrónico.
     */
    @SuppressWarnings("unchecked") // Suprimimos la advertencia
    private static Map<String, String> cargarUsuarios() {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ARCHIVO_USUARIOS))) {
            return (Map<String, String>) entrada.readObject();// nos devuelve una advertencia de tipo unchecked, pero no
                                                              // afecta al funcionamiento del programa
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    /**
     * Guarda la lista de usuarios en el archivo binario.
     * 
     * @param usuarios El mapa de usuarios a guardar.
     */
    private static void guardarUsuarios(Map<String, String> usuarios) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            salida.writeObject(usuarios);
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios.");
        }
    }

    /**
     * Muestra todos los archivos de escenarios disponibles en la carpeta escenarios
     */
    private static void mostrarEscenariosDisponibles() {
        File carpetaEscenarios = new File(CARPETA_ESCENARIOS);
        String numEscenario = "";
        File[] archivosEscenarios = carpetaEscenarios.listFiles((dir, name) -> name.endsWith(".txt"));

        if (archivosEscenarios != null && archivosEscenarios.length > 0) {
            System.out.println("Escenarios disponibles:");
            for (int i = 0; i < archivosEscenarios.length; i++) {
                // Extrae solo el número del archivo sin la extensión ".txt"
                numEscenario = archivosEscenarios[i].getName().replace("escenario_", "").replace(".txt", "");
                System.out.println(i + 1 + ". Escenario " + numEscenario);
            }
        } else {
            System.out.println("No hay escenarios disponibles.");
        }
    }

    /**
     * Muestra el escenario solicitado por el usuario.
     * 
     * @param numEscenario El número del escenario a mostrar.
     */
    private static void mostrarEscenario(String numEscenario) {
        String rutaEscenario = CARPETA_ESCENARIOS + "escenario_" + numEscenario + ".txt";

        try (BufferedReader br = new BufferedReader(new FileReader(rutaEscenario))) {
            br.lines().forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error al cargar el escenario: " + e.getMessage());
        }
    }
}
