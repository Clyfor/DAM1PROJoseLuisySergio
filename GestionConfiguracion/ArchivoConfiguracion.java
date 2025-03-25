package GestionConfiguracion;

import java.io.File;
import java.io.IOException;

public class ArchivoConfiguracion {
    private static final String nombreArchivo = "configuracion.txt";

    public static boolean configuracionExiste() {
        File archivo = new File(nombreArchivo);
        return archivo.exists();
    }

    public static void crearArchivoConfig() throws IOException {
        File archivo2 = new File(nombreArchivo);
        if (archivo2.createNewFile()) {
            System.out.println("\nArchivo de configuracion creado");
        } else {
            System.out.println("\nNo se ha podido crear el archivo de configuracion");
        }
    }
}
