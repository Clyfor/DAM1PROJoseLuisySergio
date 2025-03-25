import java.io.IOException;

import GestionConfiguracion.ArchivoConfiguracion;
import GestionDirectorios.ArchivoDirectorios;

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
