package escenarios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Escenario {
    private static final int FILAS = 12;
    private static final int COLUMNAS = 42;
    private char[][] mapa = new char[FILAS][COLUMNAS];
    private int posX, posY;
    private int salidaX, salidaY;
    private Random random = new Random();

    public void generarDesdeArchivo(String rutaArchivo) throws IOException {
        inicializarConParedes();

        BufferedReader lectorArchivos = null;
        try {
            lectorArchivos = new BufferedReader(new FileReader(rutaArchivo));
            String linea, instruccion;
            int cantidad;
            char tipo, simbolo;
            String[] instrucciones;
            int y = 1;

            while ((linea = lectorArchivos.readLine()) != null) {
                instrucciones = linea.split(",");
                int x = 1;

                for (String inst : instrucciones) {
                    instruccion = inst.trim();
                    cantidad = Integer.parseInt(instruccion.substring(0, instruccion.length() - 1));
                    tipo = instruccion.charAt(instruccion.length() - 1);
                    simbolo = (tipo == 'O') ? 'X' : ' ';

                    for (int i = 0; i < cantidad; i++) {
                        if (x >= COLUMNAS - 1) {
                            x = 1;
                            y++;
                            if (y >= FILAS - 1) break;
                        }
                        mapa[y][x] = simbolo;
                        x++;
                    }
                    if (y >= FILAS - 1) break;
                }
                y++;
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

    private void inicializarConParedes() {
        for (int y = 0; y < FILAS; y++) {
            for (int x = 0; x < COLUMNAS; x++) {
                mapa[y][x] = (y == 0 || y == FILAS - 1 || x == 0 || x == COLUMNAS - 1) ? '#' : ' ';
            }
        }
    }

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

    public void mostrarMapa() {
        for (int y = 0; y < FILAS; y++) {
            for (int x = 0; x < COLUMNAS; x++) {
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
        if (nuevaX >= 0 && nuevaX < COLUMNAS && nuevaY >= 0 && nuevaY < FILAS) {
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
    
    
    
    

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosicion(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public boolean esSalida(int x, int y) {
        return x == salidaX && y == salidaY;
    }
}
