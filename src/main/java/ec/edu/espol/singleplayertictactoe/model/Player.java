package ec.edu.espol.singleplayertictactoe.model;

public class Player {
    private int wins;
    private int loses;
    private int draws;
    private char turn;

    public Player(char turn) {
        this.turn = turn;
    }

    public void updateWins() {
        this.wins++;
    }

    public void updateLoses() {
        this.loses++;
    }

    public void updateDraws() {
        this.draws++;
    }

    public char getTurn() {
        return this.turn;
    }

    public void setTurn(char turn) {
        this.turn = turn;
    }
}
