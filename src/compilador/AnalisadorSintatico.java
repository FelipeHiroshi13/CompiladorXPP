package compilador;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import source.Parser;
import source.Token;
import source.SyntaxError;
import static source.SyntaxError.syntaxError;

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
    public StringWriter inicia(){
        StringWriter sw = new StringWriter();
        Parser parser = new Parser(tokenList);
        parser.program();
        
        
        return sw;
    }
    
  
    
}
