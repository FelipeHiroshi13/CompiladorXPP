/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;
//
//import java.io.FileReader;

import java.util.Scanner;
import source.Error;
import source.Parser;

//import java.util.Scanner;

/**
 * Trabalho Compilador
 * Alunos: Felipe Hiroshi Baron, Thiago Luiz Alves Targino, Walter do Espirito Santo Souza
 */
public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TelaPrincipal tela = new TelaPrincipal();
        Error.telaPrincipal = tela;
        tela.setVisible(true);   
    }
    
}    
       
