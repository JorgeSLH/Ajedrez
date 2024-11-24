package modelo;

import java.util.ArrayList;
import java.util.List;

public class Caballo extends Pieza {
    public Caballo(String color) {
        super(color);
    }

    @Override
    public boolean movimientoValido(int filaInicio, int colInicio, int filaDestino, int colDestino, Tablero tablero) {
        int difFila = Math.abs(filaDestino - filaInicio);
        int difCol = Math.abs(colDestino - colInicio);

        // Validar movimiento en "L"
        if (!((difFila == 2 && difCol == 1) || (difFila == 1 && difCol == 2))) {
            return false;
        }

        // Validar el destino: debe estar vacío o contener una pieza del color contrario
        Pieza piezaDestino = tablero.obtenerPieza(filaDestino, colDestino);
        return piezaDestino == null || !piezaDestino.getColor().equals(this.getColor());
    }

}
