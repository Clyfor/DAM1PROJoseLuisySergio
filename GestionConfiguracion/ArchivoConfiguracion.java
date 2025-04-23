package GestionConfiguracion;

import java.io.File;
import java.io.IOException;

/**
 * Clase que se encarga de la creación del archivo de configuración
 */
public class ArchivoConfiguracion {
    private static final String nombreArchivo = "configuracion.txt";

    /**
     * Método que comprueba si el archivo de configuración existe
     */
    public static boolean configuracionExiste() {
        return new File(nombreArchivo).exists();
    }

    /**
     * Método que crea el archivo de configuración
     */
    public static void crearArchivoConfig() throws IOException {
        File archivo = new File(nombreArchivo);
        if (archivo.createNewFile()) {
            System.out.println("Archivo de configuracion creado");
        } else {
            System.out.println("No se ha podido crear el archivo de configuracion");
        }
    }
}
