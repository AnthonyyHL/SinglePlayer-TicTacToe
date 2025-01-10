/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.singleplayertictactoe.constants;

/**
 *
 * @author samir
 */
public class GameState {
    private static char selectedSymbol;
    private static boolean humanStarts;
    
    public static void setSelectedSymbol(char symbol) {
        selectedSymbol = symbol;
    }
    
    public static char getSelectedSymbol() {
        return selectedSymbol;
    }
    
    public static void setHumanStarts(boolean starts) {
        humanStarts = starts;
    }
    
    public static boolean doesHumanStart() {
        return humanStarts;
    }
    
}
