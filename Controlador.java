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

import escenarios.Escenario;

/**
 * Clase encargada de gestionar la lógica del juego.
 * 
 * @author Jose Luis Moreno Torés y Sergio Ramírez Pla
 * @version GPL v_3
 * @date 20/04/2025
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

        // Listar los escenarios disponibles y validar el escenario seleccionado
        File carpetaEscenarios = new File(CARPETA_ESCENARIOS);
        File[] archivosEscenarios = carpetaEscenarios.listFiles((dir, name) -> name.endsWith(".txt"));

        if (archivosEscenarios == null || archivosEscenarios.length == 0) {
            System.out.println("No hay escenarios disponibles. Por favor, crea uno antes de continuar.");
            return;
        }

        System.out.println("Escenarios disponibles:");
        String[] nombresValidos = new String[archivosEscenarios.length];
        for (int i = 0; i < archivosEscenarios.length; i++) {
            String nombre = archivosEscenarios[i].getName().replace("escenario_", "").replace(".txt", "");
            nombresValidos[i] = nombre;
            System.out.println((i + 1) + ". Escenario " + nombre);
        }

        // Solicita un escenario hasta que el usuario escriba uno válido
        while (true) {
            System.out.print("Ingrese el número de escenario que desea ver: ");
            numEscenario = teclado.nextLine().trim();

            boolean encontrado = false;
            for (String nombre : nombresValidos) {
                if (nombre.equals(numEscenario)) {
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                break;
            } else {
                System.out.println("Número de escenario inválido. Por favor, elija uno de la lista.");
            }
        }

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
     * Muestra el escenario solicitado por el usuario.
     * 
     * @param numEscenario El número del escenario a mostrar.
     */
private static void mostrarEscenario(String numEscenario) {
    String rutaEscenario = CARPETA_ESCENARIOS + "escenario_" + numEscenario + ".txt";
    Scanner teclado = new Scanner(System.in);

    try {
        Escenario escenario = new Escenario();
        escenario.generarDesdeArchivo(rutaEscenario);

        while (true) {
            escenario.mostrarMapa();
            System.out.print("Mover (W/A/S/D o Q para salir): ");
            char direccion = teclado.nextLine().toUpperCase().charAt(0);

            if (direccion == 'Q') {
                System.out.println("Saliendo del escenario...");
                break;
            }

            escenario.moverJugador(direccion);

            if (escenario.esSalida(escenario.getPosX(), escenario.getPosY())) {
                escenario.mostrarMapa();
                System.out.println("¡Felicidades, has terminado el escenario!");
                break;
            }
        }

    } catch (IOException e) {
        System.out.println("Error al cargar el escenario: " + e.getMessage());
    }
}

}
