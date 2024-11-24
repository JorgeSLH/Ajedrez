package modelo;

import java.util.ArrayList;
import java.util.List;

public class Torre extends Pieza {
    public Torre(String color) {
        super(color);
    }

    @Override
    public void marcarComoMovida() {
        super.marcarComoMovida();
    }

    public boolean estaMovida() {
        return super.haSidoMovida();
    }

    @Override
    public boolean movimientoValido(int filaInicio, int columnaInicio, int filaDestino, int columnaDestino, Tablero tablero) {
        int diferenciaFilas = Math.abs(filaDestino - filaInicio);
        int diferenciaColumnas = Math.abs(columnaDestino - columnaInicio);

        // Enroque
        if (!estaMovida() && filaInicio == filaDestino && diferenciaColumnas == 4) {
            int pasoColumna = columnaInicio < columnaDestino ? 1 : -1;
            for (int c = columnaInicio + pasoColumna; c != columnaDestino; c += pasoColumna) {
                if (tablero.obtenerPieza(filaInicio, c) != null) {
                    return false;
                }
            }
            return true;
        }

        // Movimiento normal
        return filaInicio == filaDestino || columnaInicio == columnaDestino;
    }

}
