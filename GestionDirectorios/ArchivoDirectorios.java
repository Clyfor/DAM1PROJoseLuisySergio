package GestionDirectorios;

import java.io.File;

//Clase ArchivoDirectorios
public class ArchivoDirectorios {

    //Creamos un array de Strings estatico con los nombres de nuestros directorios
    private static final String[] Directorios = {"jugadores", "escenarios", "partidas" };

    /* 
     * Creamos la funcion que recorre nuestro array de Strings(Directorios) y los va creando
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
