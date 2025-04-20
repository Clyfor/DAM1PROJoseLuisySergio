import GestionConfiguracion.ArchivoConfiguracion;
import GestionDirectorios.ArchivoDirectorios;

/**
 * Clase principal de la aplicación.
 * 
 * @author Jose Luis Moreno Torés y Sergio Ramírez Pla
 * @version GPL v_3
 * @date 20/04/2025
 * @description Esta clase se encarga de crear el archivo de configuración y los directorios necesarios para la aplicación.
 */

 public class App {
    public static void main(String[] args) {
        try {
            // Verifica si el archivo de configuración existe
            if (!ArchivoConfiguracion.configuracionExiste()) {
                ArchivoConfiguracion.crearArchivoConfig();  // Si no existe, lo crea
            } else {
                System.out.println("El archivo de configuración ya existe!");
            }

            // Crea los directorios necesarios para el funcionamiento de la aplicación
            ArchivoDirectorios.crearDirectorios();

            // Llama al controlador para ejecutar la lógica del juego
            Controlador.ejecutar();

        } catch (Exception e) {
            System.out.println("Error al iniciar la aplicación: " + e.getMessage());
        }
    }
}
