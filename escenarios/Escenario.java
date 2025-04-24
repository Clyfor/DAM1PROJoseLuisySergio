package escenarios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Clase que representa un escenario del juego.
 * Esta clase se encarga de generar el mapa del escenario a partir de un
 * archivo,
 * inicializar las paredes, colocar la entrada y salida, y mover al jugador.
 */
public class Escenario {
    private char[][] mapa;
    private int posX, posY;
    private int salidaX, salidaY;
    private final Random random = new Random();
    private int filas;
    private int columnas;

    /**
     * Genera el mapa desde un archivo de texto con formato específico.
     *
     * @param rutaArchivo ruta al archivo del escenario
     * @throws IOException si hay errores al leer el archivo
     */
    public void generarDesdeArchivo(String rutaArchivo) throws IOException {
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea = lector.readLine();
            if (linea == null) {
                throw new IOException("Archivo vacío.");
            }

            String[] dimensiones = linea.split(",");
            columnas = Integer.parseInt(dimensiones[0].trim()) + 2;
            filas = Integer.parseInt(dimensiones[1].trim()) + 2;
            mapa = new char[filas][columnas];

            inicializarConParedes();

            int y = 1;
            while ((linea = lector.readLine()) != null && y < filas - 1) {
                procesarLinea(linea, y++);
            }
        }

        colocarEntradaSalida();
    }

    /**
     * Procesa una línea del archivo y actualiza el mapa.
     *
     * @param linea línea del archivo
     * @param y     fila actual en el mapa
     */
    private void procesarLinea(String linea, int y) {
        String[] instrucciones = linea.split(",");
        int x = 1;
        for (String inst : instrucciones) {
            inst = inst.trim();
            if (inst.length() < 2) {
                continue;
            }

            String numeroStr = inst.substring(0, inst.length() - 1);
            char tipo = inst.charAt(inst.length() - 1);

            int cantidad;
            try {
                cantidad = Integer.parseInt(numeroStr);
            } catch (NumberFormatException e) {
                System.err.println("Error al convertir número: " + numeroStr + " en línea: " + linea);
                continue;
            }

            char simbolo = obtenerSimbolo(tipo, linea);
            for (int i = 0; i < cantidad && x < columnas - 1; i++) {
                mapa[y][x++] = simbolo;
            }
        }
    }

    /**
     * Obtiene el símbolo correspondiente al tipo de celda.
     *
     * @param tipo  tipo de celda
     * @param linea línea del archivo
     * @return símbolo correspondiente
     */
    private char obtenerSimbolo(char tipo, String linea) {
        return switch (tipo) {
            case 'O' -> 'X'; // Obstáculo
            case 'E' -> ' '; // Espacio
            default -> {
                System.err.println("Tipo de celda desconocido: " + tipo + " en línea: " + linea);
                yield ' ';
            }
        };
    }

    /**
     * Inicializa todo el mapa con paredes exteriores ('#') y espacios interiores ('
     * ').
     */
    private void inicializarConParedes() {
        for (int y = 0; y < filas; y++) {
            for (int x = 0; x < columnas; x++) {
                mapa[y][x] = (y == 0 || y == filas - 1 || x == 0 || x == columnas - 1) ? '#' : ' ';
            }
        }
    }

    /**
     * Coloca aleatoriamente la entrada (E) en la parte superior o inferior del
     * mapa,
     * y la salida (S) en el lado opuesto.
     */
    private void colocarEntradaSalida() {
        boolean arriba = random.nextBoolean();
        int xCentro = columnas / 2;

        if (arriba) {
            mapa[0][xCentro] = 'E';
            mapa[filas - 1][xCentro] = 'S';
            posX = xCentro;
            posY = 0;
            salidaX = xCentro;
            salidaY = filas - 1;
        } else {
            mapa[filas - 1][xCentro] = 'E';
            mapa[0][xCentro] = 'S';
            posX = xCentro;
            posY = filas - 1;
            salidaX = xCentro;
            salidaY = 0;
        }
    }

    /**
     * Mueve al jugador en la dirección dada (W, A, S, D) si es una celda válida
     * (espacio o salida).
     *
     * @param direccion dirección de movimiento
     * @return true si el movimiento fue exitoso, false si hubo colisión
     */
    public boolean moverJugador(char direccion) {
        int nuevaX = posX;
        int nuevaY = posY;

        switch (Character.toUpperCase(direccion)) {
            case 'W' -> nuevaY--;
            case 'A' -> nuevaX--;
            case 'S' -> nuevaY++;
            case 'D' -> nuevaX++;
            default -> {
                return false; // Dirección no válida
            }
        }

        if (nuevaX < 0 || nuevaX >= columnas || nuevaY < 0 || nuevaY >= filas) {
            return false; // Fuera de los límites
        }

        char destino = mapa[nuevaY][nuevaX];
        if (destino == ' ' || destino == 'S') {
            posX = nuevaX;
            posY = nuevaY;
            return true; // Movimiento exitoso
        }

        return false; // Colisión con obstáculo o pared
    }

    /**
     * Getter para el mapa del escenario.
     *
     * @return Devuelve el mapa del escenario.
     */
    public char[][] getMapa() {
        return mapa;
    }

    /**
     * Getter para la posición X del jugador.
     *
     * @return Devuelve la posición X del jugador.
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Getter para la posición Y del jugador.
     *
     * @return Devuelve la posición Y del jugador.
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Verifica si el jugador ha llegado a la salida.
     *
     * @param x coordenada X del jugador
     * @param y coordenada Y del jugador
     * @return true si el jugador ha llegado a la salida, false en caso contrario
     */
    public boolean esSalida(int x, int y) {
        return x == salidaX && y == salidaY;
    }
}
