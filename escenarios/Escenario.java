package escenarios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Representa un escenario 2D de 40x10 con paredes, obstáculos y espacios vacíos.
 */
public class Escenario {
    private static final int FILAS = 12;   // 10 filas internas + 2 filas de paredes
    private static final int COLUMNAS = 42; // 40 columnas internas + 2 columnas de paredes
    private char[][] mapa = new char[FILAS][COLUMNAS];
    private int posX, posY;
    private int salidaX, salidaY;
    private Random random = new Random();

    /**
     * Genera el escenario desde un archivo de instrucciones.
     * @param rutaArchivo Ruta del archivo de instrucciones.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public void generarDesdeArchivo(String rutaArchivo) throws IOException {
        inicializarConParedes();

        BufferedReader lectorArchivos = null;
        try {
            lectorArchivos = new BufferedReader(new FileReader(rutaArchivo));
            String linea, instruccion;
            int cantidad;
            char tipo, simbolo;
            String[] instrucciones;
            int filaY = 1;  // Primera fila interna
            int columnaX;

            while ((linea = lectorArchivos.readLine()) != null) {
                instrucciones = linea.split(",");
                columnaX = 1;  // Primera columna interna

                for (int iInstruccion = 0; iInstruccion < instrucciones.length; iInstruccion++) {
                    instruccion = instrucciones[iInstruccion].trim();
                    cantidad = Integer.parseInt(instruccion.substring(0, instruccion.length() - 1));
                    tipo = instruccion.charAt(instruccion.length() - 1);
                    simbolo = (tipo == 'O') ? 'X' : ' ';

                    for (int i = 0; i < cantidad; i++) {
                        if (columnaX >= COLUMNAS - 1) {
                            columnaX = 1;
                            filaY++;
                            if (filaY >= FILAS - 1) break;
                        }
                        mapa[filaY][columnaX] = simbolo;
                        columnaX++;
                    }
                    if (filaY >= FILAS - 1) break;
                }
                filaY++;
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo (Excepcion Entrada/Salida de archivos): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al leer el archivo: (Excepción generica)" + e.getMessage());
        } finally {
            if (lectorArchivos != null) {
                try {
                    lectorArchivos.close();
                } catch (IOException e2) {
                    System.out.println("Error al cerrar el archivo: " + e2.getMessage());
                }
            }
        }
        colocarEntradaSalida();
    }

    /**
     * Inicializa el mapa colocando paredes ('#') en los bordes y espacios vacíos en el interior.
     */
    private void inicializarConParedes() {
        for (int y = 0; y < FILAS; y++) {
            for (int x = 0; x < COLUMNAS; x++) {
                mapa[y][x] = (y == 0 || y == FILAS - 1 || x == 0 || x == COLUMNAS - 1) ? '#' : ' ';
            }
        }
    }

    /**
     * Coloca la entrada (E) y la salida (S) en la pared superior o inferior, de forma aleatoria.
     */
    private void colocarEntradaSalida() {
        boolean empiezaArriba = random.nextBoolean();

        mapa[0][COLUMNAS / 2] = ' ';
        mapa[FILAS - 1][COLUMNAS / 2] = ' ';

        if (empiezaArriba) {
            mapa[0][COLUMNAS / 2] = 'E';
            posX = COLUMNAS / 2;
            posY = 0;
            mapa[FILAS - 1][COLUMNAS / 2] = 'S';
            salidaX = COLUMNAS / 2;
            salidaY = FILAS - 1;
        } else {
            mapa[FILAS - 1][COLUMNAS / 2] = 'E';
            posX = COLUMNAS / 2;
            posY = FILAS - 1;
            mapa[0][COLUMNAS / 2] = 'S';
            salidaX = COLUMNAS / 2;
            salidaY = 0;
        }
    }

    /**
     * Imprime el mapa usando el siguiente estilo:
     * 'P' para el jugador, '#' para paredes, 'X' para obstáculos,
     * 'E' para la entrada y 'S' para la salida.
     */
    public void mostrarMapa() {
        char casilla = ' ';
        for (int y = 0; y < FILAS; y++) {
            for (int x = 0; x < COLUMNAS; x++) {
                if (y == posY && x == posX) {
                    System.out.print("[P]");
                } else {
                    casilla = mapa[y][x];
                    if (casilla == '#') {
                        System.out.print("[#]");
                    } else if (casilla == 'X') {
                        System.out.print("[X]");
                    } else if (casilla == 'E') {
                        System.out.print("[E]");
                    } else if (casilla == 'S') {
                        System.out.print("[S]");
                    } else {
                        System.out.print("[ ]");
                    }
                }
            }
            System.out.println();
        }
    }

    // Getters y Setters

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public char[][] getMapa() {
        return mapa;
    }

    public void setPosicion(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    /**
     * Verifica si la posición indicada corresponde a la salida.
     * @param x Coordenada X.
     * @param y Coordenada Y.
     * @return true si es la salida, false en caso contrario.
     */
    public boolean esSalida(int x, int y) {
        return x == salidaX && y == salidaY;
    }
}
