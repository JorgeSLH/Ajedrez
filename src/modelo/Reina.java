package modelo;

import java.util.ArrayList;
import java.util.List;

public class Reina extends Pieza {
    public Reina(String color) {
        super(color);
    }

    @Override
    public boolean movimientoValido(int filaInicio, int colInicio, int filaDestino, int colDestino, Tablero tablero) {
        int diferenciaFilas = Math.abs(filaDestino - filaInicio);
        int diferenciaColumnas = Math.abs(colDestino - colInicio);

        // Movimiento válido: diagonal, horizontal o vertical
        if (!(diferenciaFilas == diferenciaColumnas || filaInicio == filaDestino || colInicio == colDestino)) {
            return false;
        }

        // Verificar si hay piezas bloqueando el camino
        if (!caminoLibre(filaInicio, colInicio, filaDestino, colDestino, tablero)) {
            return false;
        }

        // Verificar si el destino está vacío o tiene una pieza del color contrario
        Pieza piezaEnDestino = tablero.obtenerPieza(filaDestino, colDestino);
        return piezaEnDestino == null || !piezaEnDestino.getColor().equals(this.getColor());
    }

    /**
     * Verifica que el camino esté libre entre la posición inicial y final.
     *
     * @param filaInicio Fila inicial.
     * @param colInicio  Columna inicial.
     * @param filaDestino Fila destino.
     * @param colDestino  Columna destino.
     * @param tablero     Tablero actual.
     * @return True si el camino está libre, False si hay piezas bloqueando.
     */
    private boolean caminoLibre(int filaInicio, int colInicio, int filaDestino, int colDestino, Tablero tablero) {
        int incrementoFila = Integer.signum(filaDestino - filaInicio); // Incremento en filas
        int incrementoColumna = Integer.signum(colDestino - colInicio); // Incremento en columnas

        int filaActual = filaInicio + incrementoFila;
        int columnaActual = colInicio + incrementoColumna;

        while (filaActual != filaDestino || columnaActual != colDestino) {
            if (tablero.obtenerPieza(filaActual, columnaActual) != null) {
                return false; // Hay una pieza bloqueando el camino
            }
            filaActual += incrementoFila;
            columnaActual += incrementoColumna;
        }

        return true; // El camino está libre
    }

}
