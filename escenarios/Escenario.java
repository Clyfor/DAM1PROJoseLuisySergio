package escenarios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Clase que representa un escenario del juego.
 * Esta clase se encarga de generar el mapa del escenario a partir de un archivo,
 * inicializar las paredes, colocar la entrada y salida, mostrar el mapa y mover al jugador.
 */
public class Escenario {
    private char[][] mapa;
    private int posX, posY;
    private int salidaX, salidaY;
    private Random random = new Random();
    private int filas;
    private int columnas;

    /**
     * Genera el mapa del escenario a partir de un archivo.
     *
     * @param rutaArchivo La ruta del archivo que contiene la descripción del escenario.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public void generarDesdeArchivo(String rutaArchivo) throws IOException {
        BufferedReader lectorArchivos = null;
        String primeraLinea = null;
        try {
            lectorArchivos = new BufferedReader(new FileReader(rutaArchivo));
            primeraLinea = lectorArchivos.readLine();
            if (primeraLinea != null) {
                String[] dimensiones = primeraLinea.split(",");
                if (dimensiones.length == 2) {
                    columnas = Integer.parseInt(dimensiones[0].trim()) + 2; // Incluir paredes
                    filas = Integer.parseInt(dimensiones[1].trim()) + 2; // Incluir paredes
                    System.out.println("Dimensiones leídas: " + (columnas - 2) + "x" + (filas - 2));
                } else {
                    throw new IOException("Formato de dimensiones incorrecto en el archivo.");
                }
            } else {
                throw new IOException("El archivo está vacío o no contiene dimensiones.");
            }

            mapa = new char[filas][columnas];
            inicializarConParedes();

            String linea, instruccion;
            int cantidad;
            char tipo, simbolo;
            String[] instrucciones;
            int y = 1;
            int x = 0;

            while ((linea = lectorArchivos.readLine()) != null) {
                instrucciones = linea.split(",");
                x = 1;

                for (String inst : instrucciones) {
                    instruccion = inst.trim();
                    cantidad = Integer.parseInt(instruccion.substring(0, instruccion.length() - 1));
                    tipo = instruccion.charAt(instruccion.length() - 1);
                    simbolo = (tipo == 'O') ? 'X' : ' ';

                    for (int i = 0; i < cantidad; i++) {
                        if (x >= columnas - 1) {
                            x = 1;
                            y++;
                            if (y >= filas - 1) break;
                        }
                        mapa[y][x] = simbolo;
                        x++;
                    }
                    if (y >= filas - 1) break;
                }
                y++;
                if (y >= filas - 1) break;
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
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
     * Inicializa el mapa con paredes en los bordes.
     */
    private void inicializarConParedes() {
        for (int y = 0; y < filas; y++) {
            for (int x = 0; x < columnas; x++) {
                mapa[y][x] = (y == 0 || y == filas - 1 || x == 0 || x == columnas - 1) ? '#' : ' ';
            }
        }
    }

    /**
     * Coloca la entrada y la salida en el mapa de manera aleatoria.
     */
    private void colocarEntradaSalida() {
        boolean empiezaArriba = random.nextBoolean();

        mapa[0][columnas / 2] = ' ';
        mapa[filas - 1][columnas / 2] = ' ';

        if (empiezaArriba) {
            mapa[0][columnas / 2] = 'E';
            posX = columnas / 2;
            posY = 0;
            mapa[filas - 1][columnas / 2] = 'S';
            salidaX = columnas / 2;
            salidaY = filas - 1;
        } else {
            mapa[filas - 1][columnas / 2] = 'E';
            posX = columnas / 2;
            posY = filas - 1;
            mapa[0][columnas / 2] = 'S';
            salidaX = columnas / 2;
            salidaY = 0;
        }
    }

    /**
     * Muestra el mapa en la consola.
     */
    public void mostrarMapa() {
        for (int y = 0; y < filas; y++) {
            for (int x = 0; x < columnas; x++) {
                if (y == posY && x == posX) {
                    System.out.print("[P]");
                } else {
                    char casilla = mapa[y][x];
                    switch (casilla) {
                        case '#': System.out.print("[#]"); break;
                        case 'X': System.out.print("[X]"); break;
                        case 'E': System.out.print("[E]"); break;
                        case 'S': System.out.print("[S]"); break;
                        default: System.out.print("[ ]"); break;
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * Mueve al jugador en la dirección especificada.
     *
     * @param direccion La dirección en la que mover al jugador ('W', 'A', 'S', 'D').
     */
    public void moverJugador(char direccion) {
        int nuevaX = posX;
        int nuevaY = posY;

        switch (Character.toUpperCase(direccion)) {
            case 'W': nuevaY--; break; // Mover hacia arriba
            case 'A': nuevaX--; break; // Mover hacia izquierda
            case 'S': nuevaY++; break; // Mover hacia abajo
            case 'D': nuevaX++; break; // Mover hacia derecha
            default:
                System.out.println("Dirección inválida.");
                return;
        }

        // Verificar si la nueva posición está dentro de los límites del mapa
        if (nuevaX >= 0 && nuevaX < columnas && nuevaY >= 0 && nuevaY < filas) {
            char destino = mapa[nuevaY][nuevaX];

            if (destino == ' ' || destino == 'S') { // Si es un espacio vacío o la salida
                setPosicion(nuevaX, nuevaY); // Mover al jugador
                if (destino == 'S') {
                    System.out.println("¡Has llegado a la salida! 🎉");
                }
            } else {
                System.out.println("No puedes moverte ahí. Hay una pared o un obstáculo.");
            }
        } else {
            System.out.println("Movimiento fuera de los límites del mapa.");
        }
    }

    /**
     * Obtiene la posición X del jugador.
     *
     * @return La posición X del jugador.
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Obtiene la posición Y del jugador.
     *
     * @return La posición Y del jugador.
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Establece la posición del jugador.
     *
     * @param x La nueva posición X del jugador.
     * @param y La nueva posición Y del jugador.
     */
    public void setPosicion(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    /**
     * Verifica si la posición dada es la salida.
     *
     * @param x La posición X a verificar.
     * @param y La posición Y a verificar.
     * @return true si la posición es la salida, false en caso contrario.
     */
    public boolean esSalida(int x, int y) {
        return x == salidaX && y == salidaY;
    }
}
