package GestionDirectorios;

import java.io.File;

/**
 * Clase que se encarga de la creación de los directorios
 */
public class ArchivoDirectorios {
    private static final String[] Directorios = { "jugadores", "escenarios", "partidas" };

    /**
     * Método que crea los directorios si no existen
     */
    public static void crearDirectorios() {
        for (String nombre : Directorios) {
            File dir = new File(nombre);
            if (!dir.exists()) {
                if (dir.mkdir()) {
                    System.out.println("Directorio " + nombre + " creado");
                } else {
                    System.out.println("No se pudo crear el directorio " + nombre);
                }
            }
        }
    }
}
