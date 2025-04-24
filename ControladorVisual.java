import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import escenarios.Escenario;
import java.util.Optional;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControladorVisual extends Application {

    private static final int TILE_SIZE = 32;
    private GridPane grid = new GridPane();
    private Escenario escenario = new Escenario();
    private Image tileset;
    private int altura = 1000;
    private int anchura = 600;
    private Stage stage;

    private String nombreUsuario = "";
    private String correoUsuario = "";
    private int pantallasCompletadas = 0;
    private int vecesMuerto = 0;
    private int vidas = 3;
    private String personajeSeleccionado = "guerrero";

    private static final String ARCHIVO_USUARIOS = "usuarios.dat";

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        pedirNombreUsuario();
    }

    /**
     * Pide el nombre de usuario al jugador.
     */
    private void pedirNombreUsuario() {
        Map<String, String> usuarios = cargarUsuarios();
        solicitarTexto("Nombre de usuario", "Bienvenido al juego de la mazmorra", "Por favor, introduce tu nombre:")
                .ifPresent(nombre -> {
                    nombreUsuario = nombre;
                    if (!usuarios.containsKey(nombreUsuario)) {
                        pedirCorreoUsuario();
                    } else {
                        seleccionarPersonaje();
                    }
                });
    }

    /**
     * Pide el correo electrónico del usuario.
     */
    private void pedirCorreoUsuario() {
        solicitarTexto("Correo electrónico", "Bienvenido al juego de la mazmorra",
                "Por favor, introduce tu correo electrónico:")
                .ifPresent(correo -> {
                    correoUsuario = correo;
                    guardarUsuario(nombreUsuario, correoUsuario);
                    seleccionarPersonaje();
                });
    }

    /**
     * Muestra la pantalla de selección de personaje y permite al jugador elegir
     * uno.
     */
    private void seleccionarPersonaje() {
        VBox contenedor = crearContenedorConFondo("escenarios/fondo.jpg");
        contenedor.getChildren().addAll(
                crearLabel("Selecciona tu personaje:", 18),
                crearBotonPersonaje("Clérigo", "clerigo"),
                crearBotonPersonaje("Mago", "mago"),
                crearBotonPersonaje("Guerrero", "guerrero"));
        mostrarEscena(contenedor, "Selecciona tu personaje");
    }

    /**
     * Carga los usuarios desde un archivo de texto.
     * 
     * @return un mapa de usuarios donde la clave es el nombre de usuario y el valor
     *         es el correo electrónico.
     */
    private Map<String, String> cargarUsuarios() {
        Map<String, String> usuarios = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_USUARIOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    usuarios.put(partes[0].trim(), partes[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    /**
     * Guarda un nuevo usuario en el archivo de texto.
     * 
     * @param nombre nombre de usuario
     * @param correo correo electrónico del usuario
     */
    private void guardarUsuario(String nombre, String correo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_USUARIOS, true))) {
            writer.write(nombre + "," + correo);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
        }
    }

    /**
     * Muestra la pantalla de inicio del juego con opciones para iniciar el juego,
     * ver estadísticas o salir de él.
     */
    private void mostrarPantallaInicio() {
        VBox contenedor = crearContenedorConFondo("escenarios/fondo.jpg");
        contenedor.getChildren().addAll(
                crearBotonAccion("Iniciar juego", this::seleccionarEscenario),
                crearBotonAccion("Ver estadísticas", this::mostrarEstadisticas),
                crearBotonAccion("Salir", () -> System.exit(0)));
        mostrarEscena(contenedor, "Mazmorra");
    }

    /**
     * Muestra la pantalla de selección de escenario y permite al jugador elegir
     * uno.
     */
    private void seleccionarEscenario() {
        VBox contenedor = crearContenedorConFondo("escenarios/seleccion.jpg");

        // Creamos la etiqueta con estilo personalizado
        Label etiquetaSeleccion = new Label("Selecciona un escenario");
        etiquetaSeleccion.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #FF5733;"); // Cambia
                                                                                                           // #FF5733 al
                                                                                                           // color que
                                                                                                           // prefieras

        contenedor.getChildren().addAll(
                etiquetaSeleccion,
                crearBotonAccion("Escenario 1", () -> iniciarEscenario("escenarios/escenario_1.txt")),
                crearBotonAccion("Escenario 2", () -> iniciarEscenario("escenarios/escenario_2.txt")),
                crearBotonAccion("Escenario 3", () -> iniciarEscenario("escenarios/escenario_3.txt")),
                crearBotonAccion("Volver", this::mostrarPantallaInicio));

        mostrarEscena(contenedor, "Selecciona un escenario");
    }

    /**
     * Inicia el escenario seleccionado por el jugador.
     * 
     * @param rutaEscenario ruta del archivo del escenario a cargar tras haber sido
     *                      seleccionado por el jugador.
     */
    private void iniciarEscenario(String rutaEscenario) {
        try {
            escenario.generarDesdeArchivo(rutaEscenario);
            tileset = new Image("file:escenarios/casillero.png");
            grid = new GridPane();
            vidas = 3;
            renderizarMapa();
            Scene scene = new Scene(grid);
            stage.setScene(scene);
            stage.show();
            scene.setOnKeyPressed(this::moverJugador);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Renderiza el mapa del escenario en la cuadrícula de la interfaz gráfica.
     */
    private void renderizarMapa() {
        grid.getChildren().clear();
        char[][] mapa = escenario.getMapa();

        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                StackPane celda = new StackPane();
                ImageView imageViewCasilla = crearImagenCasilla(mapa[y][x]);
                ImageView imageViewPersonaje = crearImagenPersonaje(x, y);
                celda.getChildren().addAll(imageViewCasilla, imageViewPersonaje);
                grid.add(celda, x, y);
            }
        }
    }

    /**
     * Mueve al jugador en la dirección indicada por la tecla presionada.
     * 
     * @param event el evento de teclado que indica la dirección del movimiento.
     */
    private void moverJugador(KeyEvent event) {
        boolean haChocado = false;
        switch (event.getCode()) {
            case W -> haChocado = !escenario.moverJugador('W');
            case A -> haChocado = !escenario.moverJugador('A');
            case S -> haChocado = !escenario.moverJugador('S');
            case D -> haChocado = !escenario.moverJugador('D');
            case Q -> {
                seleccionarEscenario();
                return;
            }
        }

        if (haChocado) {
            vidas--;
            mostrarMensajeColision();
            if (vidas <= 0) {
                vecesMuerto++;
                mostrarMensajeMuerto();
                seleccionarEscenario();
            }
        }

        if (escenario.getMapa()[escenario.getPosY()][escenario.getPosX()] == 'S') {
            pantallasCompletadas++;
            mostrarFelicidades();
        }

        renderizarMapa();
    }

    /**
     * Muestra un mensaje de advertencia al jugador cuando colisiona con un
     * obstáculo.
     */
    private void mostrarMensajeColision() {
        mostrarAlerta(Alert.AlertType.WARNING, "¡Cuidado!", null, "¡Ouch! Has perdido una vida." + "❤".repeat(vidas));
    }

    /**
     * Muestra un mensaje de advertencia al jugador cuando muere.
     */
    private void mostrarMensajeMuerto() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "¡Oh no!", null, "☠ ¡Has muerto! Vuelve a intentarlo.");
    }

    /**
     * Muestra un mensaje de felicitación al jugador cuando llega a la salida.
     */
    private void mostrarFelicidades() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "¡Felicidades!", "🎉 ¡Has llegado a la salida! 🎉",
                "✨ ¡Bien hecho, ahora puedes elegir otro escenario! ✨")
                .ifPresent(response -> seleccionarEscenario());
    }

    /**
     * 
     * Muestra las estadísticas del jugador en una ventana emergente.
     * Incluye el número de pantallas completadas y el número de veces que ha
     * muerto.
     * También muestra la imagen del personaje seleccionado.
     */
    private void mostrarEstadisticas() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Estadísticas del jugador");
        alert.setHeaderText("Datos de: " + nombreUsuario);

        VBox contenido = new VBox(10);
        contenido.setAlignment(Pos.CENTER);

        ImageView imageViewPersonaje = crearImagenPersonajeEstadisticas();
        Label estadisticasLabel = new Label("🥇 Pantallas completadas: " + pantallasCompletadas + "\n" +
                "👻 Has muerto: " + vecesMuerto + " veces!");

        contenido.getChildren().addAll(imageViewPersonaje, estadisticasLabel);
        alert.getDialogPane().setContent(contenido);

        alert.showAndWait();
    }

    /**
     * Crea un contenedor con fondo a partir de una ruta de imagen.
     * 
     * @param rutaFondo ruta de la imagen de fondo
     * @return el contenedor con el fondo aplicado
     */
    private VBox crearContenedorConFondo(String rutaFondo) {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.CENTER);
        try {
            Image fondo = new Image("file:" + rutaFondo);
            BackgroundImage imagenFondo = new BackgroundImage(
                    fondo,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, false));
            contenedor.setBackground(new Background(imagenFondo));
        } catch (Exception e) {
            System.out.println("Error al cargar el fondo desde la ruta relativa.");
            e.printStackTrace();
        }
        return contenedor;
    }

    /**
     * Crea un Label con un texto y un tamaño de fuente específicos.
     * 
     * @param texto        el texto a mostrar en el Label
     * @param tamañoFuente el tamaño de fuente del texto
     * @return devuelve el Label creado
     */
    private Label crearLabel(String texto, int tamañoFuente) {
        Label label = new Label(texto);
        label.setStyle("-fx-font-size: " + tamañoFuente + "px; -fx-font-weight: bold; -fx-text-fill: white;");
        return label;
    }

    /**
     * Crea un botón para seleccionar un personaje.
     * 
     * @param texto     el texto que se mostrará en el botón
     * @param personaje el nombre del personaje asociado al botón
     * @return el botón creado
     */
    private Button crearBotonPersonaje(String texto, String personaje) {
        Button boton = new Button(texto);
        boton.setOnAction(e -> {
            personajeSeleccionado = personaje;
            mostrarPantallaInicio();
        });
        return boton;
    }

    /**
     * Crea un botón con un texto y una acción asociada.
     * 
     * @param texto  el texto que se mostrará en el botón
     * @param accion la acción que se ejecutará al hacer clic en el botón
     * @return el botón creado
     */
    private Button crearBotonAccion(String texto, Runnable accion) {
        Button boton = new Button(texto);
        boton.setOnAction(e -> accion.run());
        return boton;
    }

    /**
     * Crea una imagen de casilla a partir de un símbolo.
     * 
     * @param simbolo el símbolo que representa la casilla en el juego
     * @return la imagen de la casilla creada
     */
    private ImageView crearImagenCasilla(char simbolo) {
        ImageView imageViewCasilla = new ImageView(tileset);
        imageViewCasilla.setFitWidth(TILE_SIZE);
        imageViewCasilla.setFitHeight(TILE_SIZE);
        switch (simbolo) {
            case '#' -> imageViewCasilla.setViewport(new Rectangle2D(0, 32, TILE_SIZE, TILE_SIZE));
            case 'X' -> imageViewCasilla.setViewport(new Rectangle2D(32, 32, TILE_SIZE, TILE_SIZE));
            case ' ' -> imageViewCasilla.setViewport(new Rectangle2D(64, 32, TILE_SIZE, TILE_SIZE));
            case 'E' -> imageViewCasilla.setViewport(new Rectangle2D(96, 0, TILE_SIZE, TILE_SIZE));
            case 'S' -> imageViewCasilla.setViewport(new Rectangle2D(128, 0, TILE_SIZE, TILE_SIZE));
            default -> imageViewCasilla.setViewport(new Rectangle2D(0, 0, TILE_SIZE, TILE_SIZE));
        }
        return imageViewCasilla;
    }

    /**
     * Crea una imagen del personaje en la posición especificada.
     * 
     * @param x posicion x del personaje
     * @param y posicion y del personaje
     * @return la imagen del personaje creada
     */
    private ImageView crearImagenPersonaje(int x, int y) {
        ImageView imageViewPersonaje = new ImageView(tileset);
        imageViewPersonaje.setFitWidth(TILE_SIZE);
        imageViewPersonaje.setFitHeight(TILE_SIZE);
        if (escenario.getPosX() == x && escenario.getPosY() == y) {
            switch (personajeSeleccionado) {
                case "clerigo" -> imageViewPersonaje.setViewport(new Rectangle2D(0, 96, TILE_SIZE, TILE_SIZE));
                case "mago" -> imageViewPersonaje.setViewport(new Rectangle2D(96, 96, TILE_SIZE, TILE_SIZE));
                case "guerrero" -> imageViewPersonaje.setViewport(new Rectangle2D(96, 64, TILE_SIZE, TILE_SIZE));
            }
        } else {
            imageViewPersonaje.setOpacity(0);
        }
        return imageViewPersonaje;
    }

    /**
     * Crea una imagen del personaje para mostrar en las estadísticas.
     * 
     * @return la imagen del personaje creada
     */
    private ImageView crearImagenPersonajeEstadisticas() {
        ImageView imageViewPersonaje = new ImageView(tileset);
        imageViewPersonaje.setFitWidth(TILE_SIZE * 2);
        imageViewPersonaje.setFitHeight(TILE_SIZE * 2);
        switch (personajeSeleccionado) {
            case "clerigo" -> imageViewPersonaje.setViewport(new Rectangle2D(0, 96, TILE_SIZE, TILE_SIZE));
            case "mago" -> imageViewPersonaje.setViewport(new Rectangle2D(96, 96, TILE_SIZE, TILE_SIZE));
            case "guerrero" -> imageViewPersonaje.setViewport(new Rectangle2D(96, 64, TILE_SIZE, TILE_SIZE));
        }
        return imageViewPersonaje;
    }

    /**
     * Muestra una escena con un contenedor y un título.
     * 
     * @param contenedor el contenedor que contiene los elementos de la escena
     * @param titulo     el título de la ventana
     */
    private void mostrarEscena(VBox contenedor, String titulo) {
        Scene escena = new Scene(contenedor, altura, anchura);
        stage.setTitle(titulo);
        stage.setScene(escena);
        stage.show();
    }

    /**
     * Solicita un texto al usuario mediante un cuadro de diálogo.
     * 
     * @param titulo     el título del cuadro de diálogo
     * @param encabezado el encabezado del cuadro de diálogo
     * @param contenido  el contenido del cuadro de diálogo
     * @return
     */
    private Optional<String> solicitarTexto(String titulo, String encabezado, String contenido) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText(encabezado);
        dialog.setContentText(contenido);
        return dialog.showAndWait();
    }

    /**
     * Muestra una alerta al usuario con un tipo, título, encabezado y contenido
     * 
     * @param tipo       tipo de alerta
     * @param titulo     título de la alerta
     * @param encabezado encabezado de la alerta
     * @param contenido  contenido de la alerta
     * @return opcional con el tipo de botón presionado
     */
    private Optional<ButtonType> mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado,
            String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        return alert.showAndWait();
    }

    /**
     * Método principal que inicia la aplicación.
     * 
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
