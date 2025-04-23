import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    private void pedirNombreUsuario() {
        Map<String, String> usuarios = cargarUsuarios();

        TextInputDialog textoPantallaInicial = new TextInputDialog();
        textoPantallaInicial.setTitle("Nombre de usuario");
        textoPantallaInicial.setHeaderText("Bienvenido al juego de la mazmorra");
        textoPantallaInicial.setContentText("Por favor, introduce tu nombre:");

        textoPantallaInicial.showAndWait().ifPresent(nombre -> {
            nombreUsuario = nombre;
            if (!usuarios.containsKey(nombreUsuario)) {
                pedirCorreoUsuario();
            } else {
                seleccionarPersonaje();
            }
        });
    }

    private void pedirCorreoUsuario() {
        TextInputDialog textoPantallaInicial2 = new TextInputDialog();
        textoPantallaInicial2.setTitle("Correo electrónico");
        textoPantallaInicial2.setHeaderText("Bienvenido al juego de la mazmorra");
        textoPantallaInicial2.setContentText("Por favor, introduce tu correo electrónico:");

        textoPantallaInicial2.showAndWait().ifPresent(correo -> {
            correoUsuario = correo;
            guardarUsuario(nombreUsuario, correoUsuario);
            seleccionarPersonaje();
        });
    }

    private void seleccionarPersonaje() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.CENTER);

        Label cartelSeleccionPersonaje = new Label("Selecciona tu personaje:");
        cartelSeleccionPersonaje.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        String rutaFondo = "escenarios/fondo.jpg";

        try {
            Image fondo = new Image("file:" + rutaFondo);
            if (fondo == null) {
                System.out.println("No se pudo cargar la imagen de fondo.");
            }
            BackgroundImage imagenFondo = new BackgroundImage(
                fondo,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
            );
            contenedor.setBackground(new Background(imagenFondo));
        } catch (Exception e) {
            System.out.println("Error al cargar el fondo desde la ruta relativa.");
            e.printStackTrace();
        }

        Button botonClerigo = new Button("Clérigo");
        botonClerigo.setOnAction(e -> {
            personajeSeleccionado = "clerigo";
            mostrarPantallaInicio();
        });

        Button botonMago = new Button("Mago");
        botonMago.setOnAction(e -> {
            personajeSeleccionado = "mago";
            mostrarPantallaInicio();
        });

        Button botonGuerrero = new Button("Guerrero");
        botonGuerrero.setOnAction(e -> {
            personajeSeleccionado = "guerrero";
            mostrarPantallaInicio();
        });

        contenedor.getChildren().addAll(cartelSeleccionPersonaje, botonClerigo, botonMago, botonGuerrero);

        Scene escenaSeleccionPersonaje = new Scene(contenedor, altura, anchura);
        stage.setTitle("Selecciona tu personaje");
        stage.setScene(escenaSeleccionPersonaje);
        stage.show();
    }

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

    private void guardarUsuario(String nombre, String correo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_USUARIOS, true))) {
            writer.write(nombre + "," + correo);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
        }
    }

    private void mostrarPantallaInicio() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.CENTER);

        String rutaFondo = "escenarios/fondo.jpg";

        try {
            Image fondo = new Image("file:" + rutaFondo);
            if (fondo == null) {
                System.out.println("No se pudo cargar la imagen de fondo.");
            }
            BackgroundImage backgroundImage = new BackgroundImage(
                fondo,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
            );
            contenedor.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.out.println("Error al cargar el fondo desde la ruta relativa.");
            e.printStackTrace();
        }

        Button botonIniciar = new Button("Iniciar juego");
        botonIniciar.setOnAction(e -> seleccionarEscenario());

        Button botonEstadisticas = new Button("Ver estadísticas");
        botonEstadisticas.setOnAction(e -> mostrarEstadisticas());

        Button botonSalir = new Button("Salir");
        botonSalir.setOnAction(e -> System.exit(0));

        contenedor.getChildren().addAll(botonIniciar, botonEstadisticas, botonSalir);

        Scene escenaInicio = new Scene(contenedor, altura, anchura);
        stage.setTitle("Mazmorra");
        stage.setScene(escenaInicio);
        stage.show();
    }

    private void seleccionarEscenario() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.CENTER);

        String rutaFondoSeleccion = "escenarios/seleccion.jpg";

        try {
            Image fondoSeleccion = new Image("file:" + rutaFondoSeleccion);
            if (fondoSeleccion == null) {
                System.out.println("No se pudo cargar la imagen de fondo de selección.");
            }
            BackgroundImage imagenFondo = new BackgroundImage(
                fondoSeleccion,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
            );
            contenedor.setBackground(new Background(imagenFondo));
        } catch (Exception e) {
            System.out.println("Error al cargar el fondo de selección.");
            e.printStackTrace();
        }

        Button escenario1 = new Button("Escenario 1");
        escenario1.setOnAction(e -> iniciarEscenario("escenarios/escenario_1.txt"));

        Button escenario2 = new Button("Escenario 2");
        escenario2.setOnAction(e -> iniciarEscenario("escenarios/escenario_2.txt"));

        Button escenario3 = new Button("Escenario 3");
        escenario3.setOnAction(e -> iniciarEscenario("escenarios/escenario_3.txt"));

        Button botonVolver = new Button("Volver");
        botonVolver.setOnAction(e -> mostrarPantallaInicio());

        contenedor.getChildren().addAll(new Button("Selecciona un escenario"), escenario1, escenario2, escenario3, botonVolver);

        Scene escenaSeleccion = new Scene(contenedor, altura, anchura);
        stage.setScene(escenaSeleccion);
    }

    private void iniciarEscenario(String rutaEscenario) {
        try {
            escenario.generarDesdeArchivo(rutaEscenario);
            String rutaCasillero = "file:escenarios/casillero.png";
            tileset = new Image(rutaCasillero);
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

    private void renderizarMapa() {
        grid.getChildren().clear();
        char[][] mapa = escenario.getMapa();

        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                StackPane celda = new StackPane();

                ImageView imageViewCasilla = new ImageView(tileset);
                imageViewCasilla.setFitWidth(TILE_SIZE);
                imageViewCasilla.setFitHeight(TILE_SIZE);

                char simbolo = mapa[y][x];
                switch (simbolo) {
                    case '#' -> imageViewCasilla.setViewport(new Rectangle2D(0, 32, TILE_SIZE, TILE_SIZE));
                    case 'X' -> imageViewCasilla.setViewport(new Rectangle2D(32, 32, TILE_SIZE, TILE_SIZE));
                    case ' ' -> imageViewCasilla.setViewport(new Rectangle2D(64, 32, TILE_SIZE, TILE_SIZE));
                    case 'E' -> imageViewCasilla.setViewport(new Rectangle2D(96, 0, TILE_SIZE, TILE_SIZE));
                    case 'S' -> imageViewCasilla.setViewport(new Rectangle2D(128, 0, TILE_SIZE, TILE_SIZE));
                    default -> imageViewCasilla.setViewport(new Rectangle2D(0, 0, TILE_SIZE, TILE_SIZE));
                }

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

                celda.getChildren().addAll(imageViewCasilla, imageViewPersonaje);
                grid.add(celda, x, y);
            }
        }
    }

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

    private void mostrarMensajeColision() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("¡Cuidado!");
        alert.setHeaderText(null);
        alert.setContentText("¡Ouch! Has perdido una vida." + "❤".repeat(vidas));
        alert.showAndWait();
    }

    private void mostrarMensajeMuerto() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Oh no!");
        alert.setHeaderText(null);
        alert.setContentText("☠ ¡Has muerto! Vuelve a intentarlo.");
        alert.showAndWait();
    }

    private void mostrarFelicidades() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Felicidades!");
        alert.setHeaderText("🎉 ¡Has llegado a la salida! 🎉");
        alert.setContentText("✨ ¡Bien hecho, ahora puedes elegir otro escenario! ✨");
        alert.showAndWait().ifPresent(response -> seleccionarEscenario());
    }

    private void mostrarEstadisticas() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Estadísticas del jugador");
        alert.setHeaderText("Datos de: " + nombreUsuario);

        VBox contenido = new VBox(10);
        contenido.setAlignment(Pos.CENTER);

        ImageView imageViewPersonaje = new ImageView(tileset);
        imageViewPersonaje.setFitWidth(TILE_SIZE * 2);
        imageViewPersonaje.setFitHeight(TILE_SIZE * 2);
        switch (personajeSeleccionado) {
            case "clerigo" -> imageViewPersonaje.setViewport(new Rectangle2D(0, 96, TILE_SIZE, TILE_SIZE));
            case "mago" -> imageViewPersonaje.setViewport(new Rectangle2D(96, 96, TILE_SIZE, TILE_SIZE));
            case "guerrero" -> imageViewPersonaje.setViewport(new Rectangle2D(96, 64, TILE_SIZE, TILE_SIZE));
        }

        Label estadisticasLabel = new Label("🥇 Pantallas completadas: " + pantallasCompletadas + "\n" +
                                             "👻 Has muerto: " + vecesMuerto+" veces!");

        contenido.getChildren().addAll(imageViewPersonaje, estadisticasLabel);
        alert.getDialogPane().setContent(contenido);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
