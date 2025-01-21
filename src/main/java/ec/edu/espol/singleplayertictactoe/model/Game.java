/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.singleplayertictactoe.model;

import ec.edu.espol.singleplayertictactoe.constants.GameState;
import ec.edu.espol.singleplayertictactoe.constants.GameTurns;

/**
 *
 * @author samir
 */
public class Game {
    // Constantes del juego
    private static final int BOARD_SIZE = 3;
    private static final char EMPTY_CELL = ' ';
    
    // Estado del juego
    private final char[][] board;
    private final Player human;
    private final Player ai;
    private final Player currentTurn;

    /**
     * Constructor que inicializa un nuevo juego.
     * Crea el tablero vacío y configura los jugadores según las preferencias seleccionadas.
     */
    public Game() {
        this.board = initializeBoard();
        
        // Configurar jugadores basados en el símbolo seleccionado
        char humanSymbol = GameState.getSelectedSymbol();
        char aiSymbol = (humanSymbol == GameTurns.X_TURNS) ? GameTurns.O_TURNS : GameTurns.X_TURNS;
        
        this.human = new Player(humanSymbol);
        this.ai = new Player(aiSymbol);
        this.currentTurn = GameState.doesHumanStart() ? human : ai;
    }

    
    private char[][] initializeBoard() {
        char[][] newBoard = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                newBoard[i][j] = EMPTY_CELL;
            }
        }
        return newBoard;
    }

    
    public char[][] getBoard() {
        return board;
    }

    /**
     * Encuentra el mejor movimiento posible para la IA
     * @return array con las coordenadas [fila, columna] del mejor movimiento
     * @throws IllegalStateException si el jugador IA no está inicializado
     */
    public int[] findBestMove() {
        if (ai == null) {
            throw new IllegalStateException("Jugador IA no inicializado");
        }
        GameTree gameTree = new GameTree(board, ai.getTurn());
        return gameTree.findBestMove();
    }

    
    public String getGameStatus() {
        if (checkWinner(human.getTurn())) {
            return "¡Has ganado!";
        } else if (checkWinner(ai.getTurn())) {
            return "¡La IA ha ganado!";
        } else if (isBoardFull()) {
            return "¡Empate!";
        }
        return "En curso";
    }

    
    private boolean checkWinner(char symbol) {
        // Verificar filas y columnas
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (checkLine(i, symbol) || checkColumn(i, symbol)) {
                return true;
            }
        }
        
        // Verificar diagonales
        return checkDiagonals(symbol);
    }

    /**
     * Verifica una línea horizontal
     */
    private boolean checkLine(int row, char symbol) {
        return board[row][0] == symbol && 
               board[row][1] == symbol && 
               board[row][2] == symbol;
    }

    /**
     * Verifica una columna
     */
    private boolean checkColumn(int col, char symbol) {
        return board[0][col] == symbol && 
               board[1][col] == symbol && 
               board[2][col] == symbol;
    }

    /**
     * Verifica ambas diagonales
     */
    private boolean checkDiagonals(char symbol) {
        return (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
               (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
    }

    
    private boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY_CELL) {
                    return false;
                }
            }
        }
        return true;
    }
}