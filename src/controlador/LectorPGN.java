package controlador;

import java.util.ArrayList;
import java.util.List;

public class LectorPGN {

    private List<String> listaMovimientos; // Lista de movimientos procesados

    public LectorPGN() {
        this.listaMovimientos = new ArrayList<>();
    }

    /**
     * Procesa los movimientos desde una cadena de texto en formato PGN.
     *
     * @param textoPGN Texto completo en formato PGN.
     */
    public void procesarPGN(String textoPGN) {
        listaMovimientos.clear();

        // Eliminar líneas que contienen metadatos (entre corchetes)
        String[] lineas = textoPGN.split("\n");
        StringBuilder textoMovimientos = new StringBuilder();
        for (String linea : lineas) {
            if (!linea.startsWith("[")) { // Ignorar líneas que comienzan con "["
                textoMovimientos.append(linea).append(" ");
            }
        }

        // Eliminar números de turnos y separar movimientos
        String textoLimpio = textoMovimientos.toString()
                .replaceAll("\\d+\\.\\s*", "") // Eliminar números de turnos
                .replaceAll("\\s+", " ")      // Eliminar espacios extra
                .trim();

        String[] movimientosSeparados = textoLimpio.split("\\s+");

        for (String movimiento : movimientosSeparados) {
            // Ignorar resultados del juego (e.g., "1-0", "0-1", "1/2-1/2")
            if (movimiento.matches("1-0|0-1|1/2-1/2")) {
                continue;
            }
            // Verificar y agregar movimientos de enroque
            if (movimiento.equals("O-O") || movimiento.equals("O-O-O")) {
                listaMovimientos.add(movimiento); // Enroque corto o largo
            } else {
                listaMovimientos.add(movimiento);
            }
        }
    }


    /**
     * Devuelve la lista de movimientos procesados.
     *
     * @return Lista de movimientos en orden.
     */
    public List<String> obtenerMovimientos() {
        return listaMovimientos;
    }
}
