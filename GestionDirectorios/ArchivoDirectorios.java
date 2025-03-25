package GestionDirectorios;

import java.io.File;

/**
 * Clase que se encarga de la creación de los directorios
 */
public class ArchivoDirectorios {

   /**
    *  Un Array de Strings que contiene los nombres de los directorios que Miguel ha mandado crear
    */
    private static final String[] Directorios = {"jugadores", "escenarios", "partidas" };

   /**
    * Método que crea los directorios donde si no existen los crea
    * @throws IOException lanza una excepción si no se puede crear el archivo    
    */
    public static void crearDirectorios(){

        for (String nombreDirectorio : Directorios){

            File directorio = new File(nombreDirectorio);

            //Si el directorio no existe...
            if(!directorio.exists()){

                //Comando para intentar crear la carpeta (en caso de que no exista)
                if(directorio.mkdir()){

                    System.out.println("Directorio "+nombreDirectorio+" creado");

                }else{
                    //La carpeta ya existe
                    System.out.println("No se pudo crear el directorio "+nombreDirectorio);

                }
            }
        }
    }
}
