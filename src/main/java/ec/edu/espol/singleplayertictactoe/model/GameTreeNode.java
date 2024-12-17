package ec.edu.espol.singleplayertictactoe.model;

import ec.edu.espol.singleplayertictactoe.constants.GameTurns;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameTreeNode {
    private int utility;
    private List<GameTree> children;
    private Player playerTurn;
    private Player opponentTurn;
    private char[][] board;
    private int row;
    private int column;

    public GameTreeNode(char[][] board, Player playerTurn, Player opponentTurn) {
        this.board = new char[3][3];
        for (int i = 0 ; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.board[i][j] = board[i][j];
            }
        }
        children = new LinkedList<>();
        this.playerTurn = playerTurn;
        this.opponentTurn = opponentTurn;
        this.row = -1;
        this.column = -1;
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    public List<GameTree> getChildren() {
        return children;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public Player getOpponentTurn() {
        return opponentTurn;
    }

    public char[][] getBoard() {
        return board;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.column;
    }

    public void setLastMove(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public void addChild(GameTree child) {
        getChildren().add(child);
    }

    public boolean isTerminalState() {
        return (hasWon(board, playerTurn, opponentTurn) || hasWon(board, opponentTurn, playerTurn) || isBoardFull(board, playerTurn, opponentTurn));
    }

    public boolean isHorizontalWin(char[][] board, Player player, int i) {
        return board[i][0] == player.getTurn() &&
                board[i][0] == board[i][1] &&
                board[i][0] == board[i][2];
    }

    public boolean isVerticalWin(char[][] board, Player player, int i) {
        return board[0][i] == player.getTurn() &&
                board[0][i] == board[1][i] &&
                board[0][i] == board[2][i];
    }

    public boolean isStraightDiagonalWin(char[][] board, Player player) {
        return board[0][0] == player.getTurn() &&
                board[0][0] == board[1][1] &&
                board[0][0] == board[2][2];
    }

    public boolean isInverseDiagonalWin(char[][] board, Player player) {
        return board[0][2] == player.getTurn() &&
                board[0][2] == board[1][1] &&
                board[0][2] == board[2][0];
    }

    public boolean hasWon(char[][] board, Player player, Player playerAgainst) {
        for (int i = 0; i < 3; i++) {
            if (
                    isHorizontalWin(board, player, i) ||
                    isVerticalWin(board, player, i) ||
                    isStraightDiagonalWin(board, player) ||
                    isInverseDiagonalWin(board, player)
            ) {
                player.updateWins();
                playerAgainst.updateLoses();
                return true;
            }
        }
        return false;
    }

    public boolean isBoardFull(char[][] board, Player player, Player playerAgainst) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        player.updateDraws();
        playerAgainst.updateDraws();
        return true;
    }

    public void calculateUtility() {
        int playerPossibilities = calculatePossibilities(playerTurn);
        int opponentPossibilities = calculatePossibilities(opponentTurn);

        this.utility = playerPossibilities - opponentPossibilities;
    }

    public int calculatePossibilities(Player player) {
        char opponentTurn = (player.getTurn() == GameTurns.X_TURNS) ?
                            GameTurns.O_TURNS :
                            GameTurns.X_TURNS;
        int possibilities = 0;

        // Checking Rows
        for (int i = 0; i < 3; i++) {
            if (
                board[i][0] != opponentTurn &&
                board[i][0] != board[i][1] &&
                board[i][0] != board[i][2]
            ) { possibilities++; }
        }

        // Checking Columns
        for (int j = 0; j < 3; j++) {
            if (
                board[0][j] != opponentTurn &&
                board[0][j] != board[1][j] &&
                board[0][j] != board[2][j]
            ) { possibilities++; }
        }

        // Checking Straight Diagonal
        for (int j = 0; j < 3; j++) {
            if (
                board[0][0] != opponentTurn &&
                board[0][0] != board[1][1] &&
                board[0][0] != board[2][2]
            ) { possibilities++; }
        }

        // Checking Inverse Diagonal
        for (int j = 0; j < 3; j++) {
            if (
                board[0][2] != opponentTurn &&
                board[0][2] != board[1][1] &&
                board[0][2] != board[2][0]
            ) { possibilities++; }
        }

        return possibilities;
    }

    public List<int[]> getPossibleMoves() {
        List<int[]> cellAvailableCoords = new ArrayList<>();

        char playerTurn = getPlayerTurn().getTurn();
        char opponentTurn = getOpponentTurn().getTurn();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (
                    board[i][j] != playerTurn &&
                    board[i][j] != opponentTurn
                ) { cellAvailableCoords.add(new int[]{i, j}); }
            }
        }

        return cellAvailableCoords;
    }

    public char[][] copyBoard() {
        char[][] newBoard = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newBoard[i][j] = getBoard()[i][j];
            }
        }
        return newBoard;
    }
}
