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
     * @return devolverá true si existe y false en caso de que no exista
     */
    public static boolean configuracionExiste() {
        File archivo = new File(nombreArchivo);
        return archivo.exists();
    }
/**
 * Método que crea el archivo de configuración
 * @throws IOException lanza una excepción si no se puede crear el archivo
 */
    public static void crearArchivoConfig() throws IOException {
        File archivo2 = new File(nombreArchivo);
        if (archivo2.createNewFile()) {
            System.out.println("Archivo de configuracion creado");
        } else {
            System.out.println("No se ha podido crear el archivo de configuracion");
        }
    }
}
