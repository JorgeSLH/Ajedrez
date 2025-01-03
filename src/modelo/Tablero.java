package modelo;

import java.util.ArrayList;
import java.util.List;
import modelo.*;
public class Tablero {
    private Pieza[][] casillas;

    public Tablero() {
        casillas = new Pieza[8][8];
        inicializarPiezas();
    }

    private void inicializarPiezas() {
        // Inicializa piezas blancas
        casillas[0][0] = new Torre("blanco");
        casillas[0][1] = new Caballo("blanco");
        casillas[0][2] = new Alfil("blanco");
        casillas[0][3] = new Reina("blanco");
        casillas[0][4] = new Rey("blanco");
        casillas[0][5] = new Alfil("blanco");
        casillas[0][6] = new Caballo("blanco");
        casillas[0][7] = new Torre("blanco");
        for (int i = 0; i < 8; i++) {
            casillas[1][i] = new Peon("blanco");
        }

        // Inicializa piezas negras
        casillas[7][0] = new Torre("negro");
        casillas[7][1] = new Caballo("negro");
        casillas[7][2] = new Alfil("negro");
        casillas[7][3] = new Reina("negro");
        casillas[7][4] = new Rey("negro");
        casillas[7][5] = new Alfil("negro");
        casillas[7][6] = new Caballo("negro");
        casillas[7][7] = new Torre("negro");
        for (int i = 0; i < 8; i++) {
            casillas[6][i] = new Peon("negro");
        }
    }

    public boolean moverPieza(int filaInicio, int columnaInicio, int filaDestino, int columnaDestino) {
        Pieza pieza = casillas[filaInicio][columnaInicio];
        if (pieza == null) {
            System.out.println("No hay pieza en la posición inicial.");
            return false;
        }

        if (pieza.movimientoValido(filaInicio, columnaInicio, filaDestino, columnaDestino, this)) {
            casillas[filaDestino][columnaDestino] = pieza;
            casillas[filaInicio][columnaInicio] = null;
            pieza.marcarComoMovida();
            return true;
        } else {
            System.out.println("Movimiento inválido.");
            return false;
        }
    }

    public Pieza obtenerPieza(int fila, int columna) {
        return casillas[fila][columna];
    }

    public List<Pieza> todasLasPiezas() {
        List<Pieza> piezas = new ArrayList<>();
        for (int fila = 0; fila < casillas.length; fila++) {
            for (int col = 0; col < casillas[fila].length; col++) {
                Pieza pieza = casillas[fila][col];
                if (pieza != null) { // Ignorar casillas vacías
                    piezas.add(pieza);
                }
            }
        }
        return piezas;
    }

    public List<Pieza> obtenerPiezasDelOponente(String colorRey) {
        List<Pieza> piezasOponente = new ArrayList<>();
        for (Pieza pieza : todasLasPiezas()) { // Método que obtenga todas las piezas del tablero
            if (!pieza.getColor().equals(colorRey)) {
                piezasOponente.add(pieza);
            }
        }
        return piezasOponente;
    }

    public boolean casillaBajoAtaque(int fila, int columna, String color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pieza pieza = casillas[i][j];
                if (pieza != null && !pieza.getColor().equals(color)) {
                    // Si la pieza es enemiga, comprobar si puede atacar la casilla
                    if (pieza.movimientoValido(i, j, fila, columna, this)) {
                        return true; // Si una pieza enemiga puede atacar la casilla, devolver verdadero
                    }
                }
            }
        }
        return false; // Si no se encuentra ninguna pieza enemiga que pueda atacar
    }


    public Rey encontrarReyBlanco() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (casillas[i][j] instanceof Rey && casillas[i][j].getColor().equals("blanco")) {
                    return (Rey) casillas[i][j];
                }
            }
        }
        return null;
    }

    public Rey encontrarReyNegro() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (casillas[i][j] instanceof Rey && casillas[i][j].getColor().equals("negro")) {
                    return (Rey) casillas[i][j];
                }
            }
        }
        return null;
    }
}

