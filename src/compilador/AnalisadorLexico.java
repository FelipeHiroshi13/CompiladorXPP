/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import source.ScannerToken;
import source.Token;
import source.Names;
import source.SyntaxError;
/**
 *
 * @author felipe
 */
public class AnalisadorLexico {
    final private String input;
    public boolean erroLexico = false;
    public List<Token> tokenList = new ArrayList<>();
    private SyntaxError syntaxError;
    public AnalisadorLexico(String input){
        this.input = input;
    }
    
    public StringWriter Tokens(){
        ScannerToken scannerToken = new ScannerToken(input);
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter (sw);
        Token token;
        
        do{
            token = scannerToken.nextToken();
            if(token.getName() == Names.UNDEF || token.getLexicalError() == true)
                return token.erroLexico();
            addTokenList(tokenList, token);
        }while(token.getName() != Names.EOF);
        AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(tokenList);
        sw = analisadorSintatico.inicia();
        return sw;
    }
    
     private void addTokenList(List tokenList, Token token){
        if(token.getName() != Names.COMMENTS)
            tokenList.add(token);
    }
    
}
