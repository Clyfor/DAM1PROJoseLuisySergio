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

import escenarios.Escenario;

/**
 * Clase encargada de gestionar la lógica del juego.
 * Gestiona la carga de usuarios, escenarios y la interacción con el jugador.
 * 
 * @author Jose Luis Moreno Torés y Sergio Ramírez Pla
 * @version GPL v_3
 * @date 20/04/2025
 */
public class Controlador {
    private static final String ARCHIVO_USUARIOS = "usuarios.bin";
    private static final String CARPETA_ESCENARIOS = "escenarios/";

    /**
     * Carga los usuarios desde el archivo binario.
     * 
     * @return un mapa de usuarios donde la clave es el nombre de usuario y el valor es el correo electrónico.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> cargarUsuarios() {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ARCHIVO_USUARIOS))) {
            return (Map<String, String>) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    /**
     * Guarda la lista de usuarios en el archivo binario.
     * 
     * @param usuarios El mapa de usuarios a guardar.
     */
    public static void guardarUsuarios(Map<String, String> usuarios) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            salida.writeObject(usuarios);
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios.");
        }
    }

    /**
     * Obtiene los nombres de escenarios disponibles en la carpeta de escenarios.
     * 
     * @return Un arreglo de Strings con los identificadores de escenarios válidos.
     */
    public static String[] obtenerEscenariosDisponibles() {
        File carpetaEscenarios = new File(CARPETA_ESCENARIOS);
        File[] archivosEscenarios = carpetaEscenarios.listFiles((dir, name) -> name.endsWith(".txt"));

        if (archivosEscenarios == null || archivosEscenarios.length == 0) {
            return new String[0];
        }

        String[] nombres = new String[archivosEscenarios.length];
        for (int i = 0; i < archivosEscenarios.length; i++) {
            nombres[i] = archivosEscenarios[i].getName()
                    .replace("escenario_", "")
                    .replace(".txt", "");
        }
        return nombres;
    }

    /**
     * Carga un escenario específico por su número.
     * 
     * @param numEscenario número del escenario como String
     * @return objeto Escenario cargado desde archivo
     * @throws IOException si no se puede leer el archivo
     */
    public static Escenario cargarEscenario(String numEscenario) throws IOException {
        String ruta = CARPETA_ESCENARIOS + "escenario_" + numEscenario + ".txt";
        Escenario escenario = new Escenario();
        escenario.generarDesdeArchivo(ruta);
        return escenario;
    }
}
