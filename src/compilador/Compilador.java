/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;
//
//import java.io.FileReader;

import java.util.Scanner;
import source.SyntaxError;

//import java.util.Scanner;

/**
 *
 * @author 201719050198
 */
public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TelaPrincipal tela = new TelaPrincipal();
        SyntaxError.telaPrincipal = tela;
        tela.setVisible(true);   
    }
    
}    
       
