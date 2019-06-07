package compilador;

import java.util.List;
import source.Parser;
import source.Token;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 201719050368
 */
public class AnalisadorSintatico {
    private List<Token> tokenList;
    public AnalisadorSintatico(List tokenList){
        this.tokenList = tokenList;
    }
    public void inicia(){
        Parser parser = new Parser(tokenList);
        parser.imprime();
        parser.program();
    }
    
  
    
}
