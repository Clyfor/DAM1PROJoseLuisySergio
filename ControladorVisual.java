import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import escenarios.Escenario;

public class ControladorVisual extends Application {

    private static final int TILE_SIZE = 32;
    private GridPane grid = new GridPane();
    private Escenario escenario = new Escenario();
    private Image tileset;
    private int altura = 1000;
    private int anchura = 600;
    private Stage stage; // Definir el Stage aquí

    private String nombreUsuario = "";
    private int pantallasCompletadas = 0;
    private int colisionesConPared = 0;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage; // Inicializar el Stage
        pedirNombreUsuario();
    }

    private void pedirNombreUsuario() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nombre de usuario");
        dialog.setHeaderText("Bienvenido al juego de la mazmorra");
        dialog.setContentText("Por favor, introduce tu nombre:");

        dialog.showAndWait().ifPresent(nombre -> {
            nombreUsuario = nombre;
            mostrarPantallaInicio();
        });
    }

    private void mostrarPantallaInicio() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.CENTER);

        String rutaFondo = "file:/C:/Users/Maria/Desktop/TRABAJO3EVA_ABRIR_SIEMPRE_ESTE/TrabajoConJavaFX/DAM1PROJoseLuisySergio/escenarios/fondo.jpg";

        try {
            Image fondo = new Image(rutaFondo);
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
            System.out.println("Error al cargar el fondo desde la ruta absoluta.");
            e.printStackTrace();
        }

        Button botonIniciar = new Button("Iniciar juego");
        botonIniciar.setOnAction(e -> seleccionarEscenario());

        contenedor.getChildren().addAll(botonIniciar);

        Scene escenaInicio = new Scene(contenedor, altura, anchura);
        stage.setTitle("Mazmorra");
        stage.setScene(escenaInicio);
        stage.show();
    }

    private void seleccionarEscenario() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.CENTER);

        String rutaFondoSeleccion = "file:/C:/Users/Maria/Desktop/TRABAJO3EVA_ABRIR_SIEMPRE_ESTE/TrabajoConJavaFX/DAM1PROJoseLuisySergio/escenarios/seleccion.jpg";

        try {
            Image fondoSeleccion = new Image(rutaFondoSeleccion);
            if (fondoSeleccion == null) {
                System.out.println("No se pudo cargar la imagen de fondo de selección.");
            }
            BackgroundImage backgroundImage = new BackgroundImage(
                fondoSeleccion,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
            );
            contenedor.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.out.println("Error al cargar el fondo de selección.");
            e.printStackTrace();
        }

        Button escenario1 = new Button("Escenario 1");
        escenario1.setOnAction(e -> iniciarEscenario("escenario_1.txt"));

        Button escenario2 = new Button("Escenario 2");
        escenario2.setOnAction(e -> iniciarEscenario("escenario_2.txt"));

        Button escenario3 = new Button("Escenario 3");
        escenario3.setOnAction(e -> iniciarEscenario("escenario_3.txt"));

        Button botonEstadisticas = new Button("Ver estadísticas");
        botonEstadisticas.setOnAction(e -> mostrarEstadisticas());

        contenedor.getChildren().addAll(new Button("Selecciona un escenario"), escenario1, escenario2, escenario3, botonEstadisticas);

        Scene escenaSeleccion = new Scene(contenedor, altura, anchura);
        stage.setScene(escenaSeleccion);
    }

    private void iniciarEscenario(String rutaEscenario) {
        try {
            escenario.generarDesdeArchivo("escenarios/" + rutaEscenario);
            String rutaTileset = "file:/C:/Users/Maria/Desktop/TRABAJO3EVA_ABRIR_SIEMPRE_ESTE/TrabajoConJavaFX/DAM1PROJoseLuisySergio/escenarios/casillero.png";
            tileset = new Image(rutaTileset);
            grid = new GridPane();
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
    
                // Crear ImageView para las celdas (casillas)
                ImageView imageViewCasilla = new ImageView(tileset);
                imageViewCasilla.setFitWidth(TILE_SIZE);
                imageViewCasilla.setFitHeight(TILE_SIZE);
    
                // Dependiendo de la celda, se asigna una porción del tileset a la casilla
                char simbolo = mapa[y][x];
                switch (simbolo) {
                    case '#' -> {
                        // Pared: x=0, y=32 (en el tileset, de tamaño 32x32)
                        imageViewCasilla.setViewport(new javafx.geometry.Rectangle2D(0, 32, TILE_SIZE, TILE_SIZE)); // Pared
                    }
                    case 'X' -> {
                        // Obstáculo: x=32, y=32
                        imageViewCasilla.setViewport(new javafx.geometry.Rectangle2D(32, 32, TILE_SIZE, TILE_SIZE)); // Obstáculo
                    }
                    case ' ' -> {
                        // Espacio libre: x=64, y=32
                        imageViewCasilla.setViewport(new javafx.geometry.Rectangle2D(64, 32, TILE_SIZE, TILE_SIZE)); // Espacio
                    }
                    case 'E' -> {
                        // Entrada: x=96, y=0
                        imageViewCasilla.setViewport(new javafx.geometry.Rectangle2D(96, 0, TILE_SIZE, TILE_SIZE));  // Entrada
                    }
                    case 'S' -> {
                        // Salida: x=128, y=0
                        imageViewCasilla.setViewport(new javafx.geometry.Rectangle2D(128, 0, TILE_SIZE, TILE_SIZE)); // Salida
                    }
                    default -> {
                        // Si no hay un tipo definido, poner una celda por defecto
                        imageViewCasilla.setViewport(new javafx.geometry.Rectangle2D(0, 0, TILE_SIZE, TILE_SIZE)); // Valor por defecto
                    }
                }
    
                // Crear ImageView para el personaje
                ImageView imageViewPersonaje = new ImageView(tileset);
                imageViewPersonaje.setFitWidth(TILE_SIZE);
                imageViewPersonaje.setFitHeight(TILE_SIZE);
    
                // Si la posición actual coincide con la del jugador, mostrar al personaje
                if (escenario.getPosX() == x && escenario.getPosY() == y) {
                    // Asume que el sprite del personaje está en (96, 64) en el tileset
                    imageViewPersonaje.setViewport(new javafx.geometry.Rectangle2D(96, 64, TILE_SIZE, TILE_SIZE));  // Personaje
                } else {
                    // Si no es el personaje, hacer transparente el ImageView del personaje
                    imageViewPersonaje.setOpacity(0);
                }
    
                // Añadir las celdas y el personaje a la celda (el personaje está sobrepuesto)
                celda.getChildren().add(imageViewCasilla);   // Celdas en el fondo
                celda.getChildren().add(imageViewPersonaje); // Personaje encima de las celdas
    
                // Añadir la celda al grid
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
        }

        if (haChocado) colisionesConPared++;

        if (escenario.getMapa()[escenario.getPosY()][escenario.getPosX()] == 'S') {
            pantallasCompletadas++;
            mostrarFelicidades();
        }

        renderizarMapa();
    }

    private void mostrarFelicidades() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Felicidades!");
        alert.setHeaderText("¡Has llegado a la salida!");
        alert.setContentText("¡Bien hecho, ahora puedes elegir otro escenario!");
        alert.showAndWait().ifPresent(response -> seleccionarEscenario());
    }

    private void mostrarEstadisticas() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Estadísticas del jugador");
        alert.setHeaderText("Datos de: " + nombreUsuario);
        alert.setContentText("Pantallas completadas: " + pantallasCompletadas +
                "\nColisiones con paredes: " + colisionesConPared);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
