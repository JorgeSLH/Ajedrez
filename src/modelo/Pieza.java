package modelo;

import java.util.List;

public abstract class Pieza {
    protected String color;
    protected boolean movido;
    protected String imagen; // Nueva propiedad para la imagen

    public Pieza(String color) {
        this.color = color;
        this.movido = false;
        this.imagen = ""; // Inicializamos como cadena vacía
    }


    public String getColor() {
        return color;
    }

    public boolean haSidoMovida() {
        return movido;
    }

    // Método que marca la pieza como movida
    public void marcarComoMovida() {
        this.movido = true;
    }

    // Método abstracto para validar movimientos
    public abstract boolean movimientoValido(int filaInicio, int colInicio, int filaDestino, int colDestino, Tablero tablero);

    // Métodos para gestionar la imagen
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
