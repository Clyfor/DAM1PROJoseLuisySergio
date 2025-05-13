import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ControladorBaseDeDatos {
    private static final String URL = "jdbc:sqlite:archivosdebasededatos/basedatosjuego.db";

    public ControladorBaseDeDatos() {
        cargarDriverSQLite(); // Forzar registro del driver
        crearTablaSiNoExiste();
    }

    // Asegura que SQLite JDBC se registre

    /**
     * Carga el driver de SQLite.
     * Esto es necesario para que la conexión a la base de datos funcione
     * correctamente.
     */
    private void cargarDriverSQLite() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea la tabla de estadísticas si no existe.
     * Esta tabla almacenará el nombre y la puntuación de los jugadores.
     */
    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS estadisticas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "puntuacion INTEGER NOT NULL);";
        try (Connection conexion = DriverManager.getConnection(URL);
                Statement sentencia = conexion.createStatement()) {
            sentencia.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda las estadísticas del jugador en la base de datos.
     * Si el jugador ya existe, actualiza su puntuación.
     * 
     * @param nombre     nombre del jugador
     * @param puntuacion puntuación del jugador
     */
    public void guardarEstadisticas(String nombre, int puntuacion) {
        String sql = "INSERT OR REPLACE INTO estadisticas (id, nombre, puntuacion) " +
                "VALUES ((SELECT id FROM estadisticas WHERE nombre = ?), ?, ?)";
        try (Connection conexion = DriverManager.getConnection(URL);
                PreparedStatement sentenciaPreparada = conexion.prepareStatement(sql)) {
            sentenciaPreparada.setString(1, nombre);
            sentenciaPreparada.setString(2, nombre);
            sentenciaPreparada.setInt(3, puntuacion);
            sentenciaPreparada.executeUpdate();
            mantenerTop10();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mantiene solo los 10 mejores jugadores en la base de datos.
     * Elimina a los jugadores que no están en el top 10.
     */
    private void mantenerTop10() {
        String sql = "DELETE FROM estadisticas WHERE id NOT IN (" +
                "SELECT id FROM estadisticas ORDER BY puntuacion DESC LIMIT 10);";
        try (Connection conexion = DriverManager.getConnection(URL);
                Statement sentencia = conexion.createStatement()) {
            sentencia.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el top 10 de jugadores.
     * 
     * @return una lista de objetos EstadisticasJugador que representan a los 10
     *         mejores jugadores
     */
    public List<EstadisticasJugador> obtenerTop10() {
        List<EstadisticasJugador> top10 = new ArrayList<>();
        String sql = "SELECT nombre, puntuacion FROM estadisticas ORDER BY puntuacion DESC LIMIT 10";
        try (Connection conexion = DriverManager.getConnection(URL);
                Statement sentencia = conexion.createStatement();
                ResultSet resultado = sentencia.executeQuery(sql)) {
            int rank = 1;
            while (resultado.next()) {
                String nombre = resultado.getString("nombre");
                int puntuacion = resultado.getInt("puntuacion");
                top10.add(new EstadisticasJugador("Top " + rank + ": " + nombre, puntuacion));
                rank++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top10;
    }

    /**
     * Obtiene el Hall of Fame de los jugadores.
     * 
     * @return una lista de objetos EstadisticasJugador que representan a los 3
     *         mejores jugadores
     */
    public List<EstadisticasJugador> obtenerHallOfFame() {
        List<EstadisticasJugador> hallOfFame = new ArrayList<>();
        String sql = "SELECT nombre, puntuacion FROM estadisticas ORDER BY puntuacion DESC LIMIT 3";
        try (Connection conexion = DriverManager.getConnection(URL);
                Statement sentencia = conexion.createStatement();
                ResultSet resultado = sentencia.executeQuery(sql)) {
            int rank = 1;
            while (resultado.next()) {
                String nombre = resultado.getString("nombre");
                int puntuacion = resultado.getInt("puntuacion");
                hallOfFame.add(new EstadisticasJugador("Hall of Fame - " + "Top " + rank + ": " + nombre, puntuacion));
                rank++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hallOfFame;
    }

    /**
     * Clase que representa las estadísticas de un jugador.
     * Contiene el nombre y la puntuación del jugador.
     */

    public static class EstadisticasJugador {
        private String nombre;
        private int puntuacion;

        /**
         * Constructor de la clase EstadisticasJugador.
         * 
         * @param nombre     nombre del jugador
         * @param puntuacion puntuación del jugador
         */
        public EstadisticasJugador(String nombre, int puntuacion) {
            this.nombre = nombre;
            this.puntuacion = puntuacion;
        }
/**
 * Obtiene el nombre del jugador.
 * @return nombre del jugador
 */
        public String getNombre() {
            return nombre;
        }
/**
 * Obtiene la puntuación del jugador.
 * @return puntuación del jugador
 */
        public int getPuntuacion() {
            return puntuacion;
        }
    }
}
