package controlador;

import modelo.*;
import vista.ChessGUI;

import java.util.List;

public class Controlador {
    private Tablero tableroActual;  // Modelo
    private ChessGUI interfaz;        // Vista
    private LectorPGN lectorPGN;   // Lector PGN
    private List<String> listaMovimientos; // Lista de movimientos procesados
    private int posicionMovimiento; // Índice del movimiento actual
    private String colorTurno;     // Turno actual

    public Controlador(Tablero tablero, ChessGUI vista, LectorPGN lector) {
        this.tableroActual = tablero;
        this.interfaz = vista;
        this.lectorPGN = lector;
        this.posicionMovimiento = 0;
        this.colorTurno = "negro";
    }

    public void cargarMovimientos(String textoPGN) {
        lectorPGN.procesarPGN(textoPGN);
        listaMovimientos = lectorPGN.obtenerMovimientos();
        posicionMovimiento = 0;
    }

    public Tablero obtenerTablero() {
        return tableroActual;
    }

    public boolean realizarMovimiento() {
        if (listaMovimientos == null || posicionMovimiento >= listaMovimientos.size()) {
            return false;
        }

        String movimiento = listaMovimientos.get(posicionMovimiento);
        System.out.println("Realizando movimiento: " + movimiento);

        if (ejecutarMovimiento(movimiento)) {
            posicionMovimiento++;
            interfaz.actualizarTablero();
            return true;
        } else {
            System.err.println("Movimiento inválido: " + movimiento);
            return false;
        }
    }

    public void reiniciarJuego() {
        tableroActual = new Tablero();
        posicionMovimiento = 0;
        listaMovimientos = null;
        colorTurno = "negro";
        interfaz.actualizarTablero();
    }

    private boolean ejecutarMovimiento(String movimiento) {
        try {
            if (movimiento.equals("O-O")) {
                return procesarEnroque("corto");
            } else if (movimiento.equals("O-O-O")) {
                return procesarEnroque("largo");
            } else if (movimiento.contains("x")) {
                return procesarCaptura(movimiento);
            } else if (movimiento.length() == 2) {
                return moverPeon(movimiento);
            } else if (movimiento.length() > 2) {
                char pieza = movimiento.charAt(0);
                String destino = movimiento.substring(1);
                return moverConDestino(pieza, destino);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean procesarEnroque(String tipo) {
        if (colorTurno.equals("blanco")) {
            if (!tableroActual.encontrarReyBlanco().haSidoMovida()) {
                if (tipo.equals("corto")) {
                    return ejecutarEnroque("blanco", 7, 4, 7, 6, 7, 7, 7, 5); // Enroque corto
                } else if (tipo.equals("largo")) {
                    return ejecutarEnroque("blanco", 7, 4, 7, 2, 7, 0, 7, 3); // Enroque largo
                }
            }
        } else if (colorTurno.equals("negro")) {
            if (!tableroActual.encontrarReyNegro().haSidoMovida()) {
                if (tipo.equals("corto")) {
                    return ejecutarEnroque("negro", 0, 4, 0, 6, 0, 7, 0, 5); // Enroque corto
                } else if (tipo.equals("largo")) {
                    return ejecutarEnroque("negro", 0, 4, 0, 2, 0, 0, 0, 3); // Enroque largo
                }
            }
        }
        return false;
    }



    private boolean ejecutarEnroque(String color, int filaRey, int colReyOrigen, int filaReyDestino, int colReyDestino, int filaTorreOrigen, int colTorreOrigen, int filaTorreDestino, int colTorreDestino) {
        // Verificar si las casillas entre el rey y la torre están libres
        if (!verificarEspaciosLibres(filaRey, colReyOrigen, colTorreOrigen)) {
            System.out.println("Movimiento inválido: Casillas entre rey y torre no están libres.");
            return false;
        }

        // Verificar si las casillas están bajo ataque (incluyendo las casillas por donde el rey pasará)
        if (verificarCasillasBajoAtaque(filaRey, colReyOrigen, colReyDestino, color)) {
            System.out.println("Movimiento inválido: Casillas bajo ataque.");
            return false;
        }

        // Verificar que la torre no haya sido movida
        Pieza torre = tableroActual.obtenerPieza(filaTorreOrigen, colTorreOrigen);
        if (torre == null || torre.haSidoMovida()) {
            System.out.println("Movimiento inválido: La torre ha sido movida.");
            return false;
        }

        // Mover el rey
        tableroActual.moverPieza(filaRey, colReyOrigen, filaReyDestino, colReyDestino);

        // Mover la torre
        tableroActual.moverPieza(filaTorreOrigen, colTorreOrigen, filaTorreDestino, colTorreDestino);

        alternarColorTurno(); // Cambiar el turno de color
        return true; // El enroque se realiza con éxito
    }



    private boolean verificarEspaciosLibres(int fila, int colRey, int colTorre) {
        // Verificar que las columnas estén ordenadas de menor a mayor (izquierda a derecha) o mayor a menor (derecha a izquierda)
        int colInicio = Math.min(colRey, colTorre);
        int colFin = Math.max(colRey, colTorre);

        // Recorrer todas las casillas entre el rey y la torre
        for (int col = colInicio + 1; col < colFin; col++) {
            Pieza pieza = tableroActual.obtenerPieza(fila, col);
            if (pieza != null) {
                // Si encontramos una pieza en el camino, no se puede realizar el enroque
                System.out.println("Movimiento inválido: Hay una pieza en el camino en la columna " + col);
                return false;
            }
        }

        // Si no encontramos piezas en el camino, la verificación es exitosa
        return true;
    }


    private boolean verificarCasillasBajoAtaque(int fila, int colInicio, int colFin, String color) {
        int minCol = Math.min(colInicio, colFin);
        int maxCol = Math.max(colInicio, colFin);

        // Comprobar las casillas entre colInicio y colFin, incluyendo las posiciones inicial y final
        for (int col = minCol; col <= maxCol; col++) {
            if (tableroActual.casillaBajoAtaque(fila, col, color)) {
                System.out.println("Movimiento inválido: Casilla " + fila + "," + col + " bajo ataque.");
                return true; // La casilla está bajo ataque
            }
        }
        return false;
    }


    private boolean procesarCaptura(String movimiento) {
        char pieza = Character.isUpperCase(movimiento.charAt(0)) ? movimiento.charAt(0) : 'P';
        String destino = movimiento.substring(movimiento.length() - 2);
        int filaDestino = convertirFila(destino.charAt(1));
        int columnaDestino = convertirColumna(destino.charAt(0));
        char pistaOrigen = movimiento.length() > 3 ? movimiento.charAt(1) : 0;

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Pieza piezaActual = tableroActual.obtenerPieza(fila, columna);
                if (piezaActual != null && piezaActual.getColor().equals(colorTurno) && getCaracterPieza(piezaActual) == pieza) {
                    if (pistaOrigen != 0 && pistaOrigen != 'x') {
                        if (Character.isDigit(pistaOrigen) && fila != convertirFila(pistaOrigen)) continue;
                        if (Character.isLetter(pistaOrigen) && columna != convertirColumna(pistaOrigen)) continue;
                    }

                    if (piezaActual.movimientoValido(fila, columna, filaDestino, columnaDestino, tableroActual)) {
                        tableroActual.moverPieza(fila, columna, filaDestino, columnaDestino);
                        alternarColorTurno();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean moverPeon(String destino) {
        int filaDestino = convertirFila(destino.charAt(1));
        int columnaDestino = convertirColumna(destino.charAt(0));

        for (int fila = 0; fila < 8; fila++) {
            Pieza pieza = tableroActual.obtenerPieza(fila, columnaDestino);
            if (pieza instanceof Peon && pieza.getColor().equals(colorTurno)) {
                if (pieza.movimientoValido(fila, columnaDestino, filaDestino, columnaDestino, tableroActual)) {
                    tableroActual.moverPieza(fila, columnaDestino, filaDestino, columnaDestino);
                    alternarColorTurno();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean moverConDestino(char pieza, String destino) {
        int filaDestino = convertirFila(destino.charAt(1));
        int columnaDestino = convertirColumna(destino.charAt(0));

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Pieza piezaActual = tableroActual.obtenerPieza(fila, columna);
                if (piezaActual != null && getCaracterPieza(piezaActual) == pieza && piezaActual.getColor().equals(colorTurno)) {
                    if (piezaActual.movimientoValido(fila, columna, filaDestino, columnaDestino, tableroActual)) {
                        tableroActual.moverPieza(fila, columna, filaDestino, columnaDestino);
                        alternarColorTurno();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int convertirColumna(char letraColumna) {
        return letraColumna - 'a';
    }

    private int convertirFila(char numeroFila) {
        return 8 - (numeroFila - '0');
    }

    private char getCaracterPieza(Pieza pieza) {
        if (pieza instanceof Rey) return 'K';
        if (pieza instanceof Reina) return 'Q';
        if (pieza instanceof Torre) return 'R';
        if (pieza instanceof Alfil) return 'B';
        if (pieza instanceof Caballo) return 'N';
        if (pieza instanceof Peon) return 'P';
        return '?';
    }

    private void alternarColorTurno() {
        colorTurno = colorTurno.equals("negro") ? "blanco" : "negro";
    }
}
