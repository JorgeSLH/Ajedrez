package modelo;

import java.util.ArrayList;
import java.util.List;

public class Peon extends Pieza {
    public Peon(String color) {
        super(color);
    }

    @Override
    public boolean movimientoValido(int filaInicio, int colInicio, int filaDestino, int colDestino, Tablero tablero) {
        int direccion = color.equals("blanco") ? 1 : -1;

        // Caso 1: Movimiento simple (1 casilla hacia adelante)
        if (colInicio == colDestino) {
            if ((filaDestino - filaInicio) == direccion) {
                // Verificar que la casilla destino esté vacía
                return tablero.obtenerPieza(filaDestino, colDestino) == null;
            }
        }

        // Caso 2: Primer movimiento (2 casillas hacia adelante)
        if (colInicio == colDestino && !haSidoMovida()) {
            // Puede mover 2 casillas solo si ambas están vacías
            if ((filaDestino - filaInicio) == 2 * direccion) {
                if (tablero.obtenerPieza(filaDestino, colDestino) == null &&
                        tablero.obtenerPieza(filaDestino - direccion, colInicio) == null) {
                    return true;
                }
            }
        }

        // Caso 3: Captura en diagonal
        if (Math.abs(colDestino - colInicio) == 1 && (filaDestino - filaInicio) == direccion) {
            Pieza piezaDestino = tablero.obtenerPieza(filaDestino, colDestino);
            if (piezaDestino != null && !piezaDestino.getColor().equals(this.color)) {
                return true; // Captura válida si hay una pieza oponente en el destino
            }
        }

        return false;
    }

    @Override
    public void marcarComoMovida() {
        super.marcarComoMovida(); // Llamada al método de la clase base
    }

    @Override
    public boolean haSidoMovida() {
        return super.haSidoMovida(); // Retorna el estado del atributo 'movido' de la clase base
    }

}
