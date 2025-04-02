package escenarios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Escenario {
    public static final int ancho = 80;  // 80 columnas
    public static final int alto = 40;   // 40 filas

    /**
     * Crea un escenario con obstáculos generados aleatoriamente y lo guarda en un archivo.
     * @param nombreArchivo Nombre del archivo donde se guardará el escenario.
     * @throws IOException
     */
    public static void generarEscenarioLaberinto(String nombreArchivo) throws IOException {
        Random random = new Random();
        try (BufferedWriter escritura = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (int i = 0; i < alto; i++) {
                char[] linea = new char[ancho];
                for (int j = 0; j < ancho; j++) {
                    linea[j] = (random.nextDouble() < 0.2) ? 'X' : ' ';
                }
                escritura.write(linea);
                escritura.newLine();
            }
            System.out.println("Escenario (laberinto) generado en " + nombreArchivo);
        }
    }

    /**
     * Carga el escenario desde un archivo y lo almacena en una matriz de caracteres.
     * @param nombreArchivo Nombre del archivo del escenario.
     * @return Matriz de caracteres que representa el escenario.
     * @throws IOException Si las dimensiones del archivo no son correctas o hay error de lectura.
     */
    public static char[][] cargarMatriz(String nombreArchivo) throws IOException {
        char[][] matriz = new char[alto][ancho];
        String linea="";
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            for (int i = 0; i < alto; i++) {
                linea = reader.readLine();
                if (linea == null || linea.length() != ancho) {
                    throw new IOException("El escenario no tiene las dimensiones correctas en la línea " + (i+1));
                }
                matriz[i] = linea.toCharArray();
            }
        }
        return matriz;
    }

    /**
     * Muestra el escenario en consola.
     * @param matriz Matriz que representa el escenario.
     */
    public static void mostrarEscenario(char[][] matriz) {
        for (int i = 0; i < alto; i++) {
            System.out.println(matriz[i]);
        }
    }
}