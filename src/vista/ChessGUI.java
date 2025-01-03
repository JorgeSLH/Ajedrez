package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelo.*;
import controlador.*;

public class ChessGUI extends JFrame {
    private JPanel panelTablero;
    private JButton btnAvanzarMovimiento;
    private JButton btnReiniciar;
    private JButton btnCargarPgn;
    private JTextArea areaTextoPgn;
    private JLabel[][] casillas;

    private Controlador controlador; // Controlador para manejar la lógica

    public ChessGUI() {
        setTitle("Ajedrez");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Crear el modelo y el controlador
        Tablero tablero = new Tablero();
        LectorPGN lectorPGN = new LectorPGN();
        controlador = new Controlador(tablero, this, lectorPGN);

        inicializarVista();

        setVisible(true);
    }

    // Inicializa los componentes de la vista
    private void inicializarVista() {
        // Crear el tablero visual
        panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(8, 8));
        casillas = new JLabel[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JLabel casilla = new JLabel();
                casilla.setHorizontalAlignment(SwingConstants.CENTER);
                casilla.setOpaque(true);
                // Cambiar colores a un estilo madera
                if ((i + j) % 2 == 0) {
                    casilla.setBackground(new Color(222, 184, 135)); // Color madera claro
                } else {
                    casilla.setBackground(new Color(139, 69, 19)); // Color madera oscuro
                }
                casilla.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                casillas[i][j] = casilla;
                panelTablero.add(casilla);
            }
        }

        actualizarTablero();

        // Área de texto para el PGN
        areaTextoPgn = new JTextArea(5, 20);
        areaTextoPgn.setLineWrap(true);
        areaTextoPgn.setWrapStyleWord(true);
        JScrollPane pgnScrollPane = new JScrollPane(areaTextoPgn);

        // Botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());

        // Cambiar el color de fondo de los botones
        btnCargarPgn = new JButton("Cargar PGN");
        btnCargarPgn.setBackground(new Color(210, 180, 140)); // Color madera
        btnCargarPgn.setForeground(Color.BLACK);
        btnCargarPgn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarMovimientos();
            }
        });

        btnAvanzarMovimiento = new JButton("Avanzar Movimiento");
        btnAvanzarMovimiento.setBackground(new Color(210, 180, 140)); // Color madera
        btnAvanzarMovimiento.setForeground(Color.BLACK);
        btnAvanzarMovimiento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                avanzarMovimiento();
            }
        });

        btnReiniciar = new JButton("Reiniciar");
        btnReiniciar.setBackground(new Color(210, 180, 140)); // Color madera
        btnReiniciar.setForeground(Color.BLACK);
        btnReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarPartida();
            }
        });

        panelBotones.add(btnCargarPgn);
        panelBotones.add(btnAvanzarMovimiento);
        panelBotones.add(btnReiniciar);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.add(panelTablero, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);
        add(pgnScrollPane, BorderLayout.EAST);
    }

    // Actualiza la vista del tablero según el estado actual
    public void actualizarTablero() {
        Tablero tablero = controlador.obtenerTablero(); // Obtener el estado actual del tablero
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pieza pieza = tablero.obtenerPieza(i, j); // Obtener la pieza en la posición (i, j)
                if (pieza != null) {
                    String rutaImagen = getRutaImagenPieza(pieza); // Obtener la ruta de la imagen
                    if (rutaImagen != null) {
                        // Cargar y escalar la imagen
                        ImageIcon icono = new ImageIcon(rutaImagen);
                        Image imagenEscalada = icono.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                        casillas[i][j].setIcon(new ImageIcon(imagenEscalada));
                        casillas[i][j].setText(""); // Asegurarse de que no haya texto
                    }
                } else {
                    casillas[i][j].setIcon(null); // Limpiar el icono si no hay pieza
                    casillas[i][j].setText(""); // Limpiar el texto
                }
            }
        }
    }



    // Devuelve un símbolo unicode o texto representando la pieza
    private String getRutaImagenPieza(Pieza pieza) {  // Renombrado para reflejar que devuelve una ruta
        String color = pieza.getColor();
        if (pieza instanceof Rey) return color.equals("blanco") ? "src/Recursos/Rey.png" : "src/Recursos/ReyNegro.png";
        if (pieza instanceof Reina) return color.equals("blanco") ? "src/Recursos/Reina.png" : "src/Recursos/ReinaNegra.png";
        if (pieza instanceof Torre) return color.equals("blanco") ? "src/Recursos/Torre.png" : "src/Recursos/TorreNegra.png";
        if (pieza instanceof Alfil) return color.equals("blanco") ? "src/Recursos/Alfil.png" : "src/Recursos/AlfilNegro.png";
        if (pieza instanceof Caballo) return color.equals("blanco") ? "src/Recursos/Caballo.png" : "src/Recursos/CaballoNegro.png";
        if (pieza instanceof Peon) return color.equals("blanco") ? "src/Recursos/Ficha Peon.png" : "src/Recursos/Ficha PeonNegra.png";
        return null; // Si no es una pieza válida, devuelve null
    }


    // Lógica para cargar movimientos desde el texto del PGN
    private void cargarMovimientos() {
        String pgnTexto = areaTextoPgn.getText().trim();
        if (pgnTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce texto PGN.");
            return;
        }

        controlador.cargarMovimientos(pgnTexto); // Pasar los movimientos al controlador
        JOptionPane.showMessageDialog(this, "Movimientos cargados correctamente.");
    }

    // Lógica para avanzar un movimiento
    private void avanzarMovimiento() {
        if (!controlador.realizarMovimiento()) {
            JOptionPane.showMessageDialog(this, "No hay más movimientos por avanzar.");
        }
    }

    // Reinicia la partida al estado inicial
    private void reiniciarPartida() {
        controlador.reiniciarJuego();
        actualizarTablero();
        areaTextoPgn.setText("");
    }

    public static void main(String[] args) {
        new ChessGUI();
    }
}