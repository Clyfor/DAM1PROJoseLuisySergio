import java.io.IOException;
import GestionConfiguracion.ArchivoConfiguracion;
import GestionDirectorios.ArchivoDirectorios;

/**
 * Clase principal de la aplicación.
 * 
 * @author Jose Luis Moreno Torés y Sergio Ramírez Pla
 * @version GPL v_3
 * @date 2023-10-01
 * @description Esta clase se encarga de crear el archivo de configuración y los directorios necesarios para la aplicación.
 */
public class App {
    public static void main(String[] args) throws IOException {
        try {
            if(!ArchivoConfiguracion.configuracionExiste()){
                ArchivoConfiguracion.crearArchivoConfig();
            } else {
                System.out.println("El archivo de configuración ya existe!");
            }
            ArchivoDirectorios.crearDirectorios();
        } catch (IOException e) {
            System.out.println("Error al crear el archivo de configuración " +
                    "\nMensaje de la excepción: " + e.getMessage());
        }
    }
}
