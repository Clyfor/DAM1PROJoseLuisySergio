import GestionConfiguracion.ArchivoConfiguracion;
import GestionDirectorios.ArchivoDirectorios;
import javafx.application.Application;

/**
 * Clase principal de la aplicación.
 * Esta clase se encarga de crear el archivo de configuración y los directorios
 * necesarios para la aplicación.
 */
public class App {
    public static void main(String[] args) {
        try {
            if (!ArchivoConfiguracion.configuracionExiste()) {
                ArchivoConfiguracion.crearArchivoConfig();
            } else {
                System.out.println("El archivo de configuración ya existe!");
            }

            ArchivoDirectorios.crearDirectorios();

            Application.launch(ControladorVisual.class, args);

        } catch (Exception e) {
            System.out.println("Error al iniciar la aplicación: " + e.getMessage());
        }
    }
}
