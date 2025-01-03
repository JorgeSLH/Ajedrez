package modelo;

import java.util.ArrayList;
import java.util.List;

public class Alfil extends Pieza {
    public Alfil(String color) {
        super(color);
    }

    @Override
    public boolean movimientoValido(int filaInicio, int colInicio, int filaDestino, int colDestino, Tablero tablero) {
        int difFila = Math.abs(filaDestino - filaInicio);
        int difCol = Math.abs(colDestino - colInicio);

        // Validar movimiento diagonal
        if (difFila != difCol) {
            return false;
        }

        int incrementoFila = (filaDestino > filaInicio) ? 1 : -1;
        int incrementoCol = (colDestino > colInicio) ? 1 : -1;

        int filaActual = filaInicio + incrementoFila;
        int colActual = colInicio + incrementoCol;

        while (filaActual != filaDestino || colActual != colDestino) {
            if (tablero.obtenerPieza(filaActual, colActual) != null) {
                return false; // Camino bloqueado
            }
            filaActual += incrementoFila;
            colActual += incrementoCol;
        }

        Pieza piezaDestino = tablero.obtenerPieza(filaDestino, colDestino);
        return piezaDestino == null || !piezaDestino.getColor().equals(this.getColor());
    }

}
