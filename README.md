# Lector de Partidas de Ajedrez en formato PGN

## Descripción
Esta aplicación permite visualizar partidas de ajedrez en formato PGN (Portable Game Notation) a través de una interfaz gráfica. 
El programa muestra un tablero de ajedrez y permite cargar partidas para visualizarlas movimiento a movimiento.

## Cómo ejecutar la aplicación
1. Ejecute el archivo ChessGUI.java ubicado en el paquete vista
2. Se abrirá una ventana con el tablero de ajedrez y los controles

## Funcionalidades

### Área de texto PGN
- En el lado derecho de la ventana hay un área de texto donde puede pegar la notación PGN de una partida
- El formato PGN debe incluir los movimientos numerados, por ejemplo:
  1. e4 e5
  2. Nf3 Nc6
  3. Bb5
  etc.

### Botones de control
- **Cargar PGN**: Carga la partida desde el área de texto
- **Avanzar Movimiento**: Ejecuta el siguiente movimiento de la partida
- **Reiniciar**: Vuelve el tablero a su posición inicial

### Cómo usar la aplicación
1. Copie una partida en formato PGN
2. Pegue el texto en el área de la derecha
3. Presione "Cargar PGN"
4. Use el botón "Avanzar Movimiento" para ver la partida paso a paso
5. Si desea comenzar de nuevo, presione "Reiniciar"

### Funcionalidades soportadas
- Movimientos básicos de todas las piezas
- Capturas de piezas
- Validación de movimientos legales
- Alternancia automática de turnos entre blancas y negras

### Notas importantes
- La partida solo puede avanzar hacia adelante
- No se pueden realizar movimientos manualmente
- Los movimientos inválidos serán ignorados
- Cuando la partida termine, se mostrará un mensaje
