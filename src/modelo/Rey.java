package modelo;

import java.util.ArrayList;
import java.util.List;

public class Rey extends Pieza {
    public Rey(String color) {
        super(color);
    }

    @Override
    public void marcarComoMovida() {
        super.marcarComoMovida(); // Llamada al método de la clase base (Pieza)
    }

    public boolean estaMovido() {
        return super.haSidoMovida(); // Retorna el estado del atributo 'movido' de la clase base
    }

    @Override
    public boolean movimientoValido(int filaInicio, int columnaInicio, int filaDestino, int columnaDestino, Tablero tablero) {
        int diferenciaFilas = Math.abs(filaDestino - filaInicio);
        int diferenciaColumnas = Math.abs(columnaDestino - columnaInicio);

        // Enroque (movimiento especial)
        if (!estaMovido() && diferenciaFilas == 0 && diferenciaColumnas == 2) {
            Pieza torre = tablero.obtenerPieza(filaInicio, columnaInicio == 0 ? 0 : 7);
            if (torre != null && torre instanceof Torre && !torre.haSidoMovida()) {
                int pasoColumna = columnaInicio < columnaDestino ? 1 : -1;
                for (int c = columnaInicio + pasoColumna; c != columnaDestino; c += pasoColumna) {
                    if (tablero.obtenerPieza(filaInicio, c) != null) {
                        return false; // Hay una pieza bloqueando el enroque
                    }
                }
                return true;
            }
        }

        // Movimiento normal del Rey (una casilla en cualquier dirección)
        return diferenciaFilas <= 1 && diferenciaColumnas <= 1;
    }

}
