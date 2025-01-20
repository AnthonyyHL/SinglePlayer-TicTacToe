/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.singleplayertictactoe.model;

/**
 *
 * @author samir
 */
public class Player {
    private int wins;
    private int loses;
    private int draws;
    private char turn;

    /**
     * Constructor que inicializa un nuevo jugador
     * @param turn símbolo del jugador ('X' u 'O')
     */
    public Player(char turn) {
        this.turn = turn;
        this.wins = 0;
        this.loses = 0;
        this.draws = 0;
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

    
    public int getWins() {
        return wins;
    }

    
    public int getLoses() {
        return loses;
    }

    
    public int getDraws() {
        return draws;
    }
}