package jugadores;

public class Jugador {
    private int fila;
    private int columna;
    private char[][] escenario;
/**
 * Constructor de la clase Jugador
 * @param escenario añade el escenario al jugador
 * @param filaInicial  añade la fila inicial al jugador
 * @param columnaInicial añade la columna inicial al jugador
 */
    public Jugador(char[][] escenario, int filaInicial, int columnaInicial) {
        this.escenario = escenario;
        this.fila = filaInicial;
        this.columna = columnaInicial;
    }
/**
 * Método que se encargará de mover al jugador
 * @return devuelve la fila
 */
    public int getFila() {
        return fila;
    }
/**
 * Método que se encargará de mover al jugador
 * @return devolverá la columna
 */
    public int getColumna() {
        return columna;
    }

}