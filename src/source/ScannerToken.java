/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import tokens.Comments;
import tokens.Id;
import tokens.IntergerLiteral;
import tokens.Op;
import tokens.Sep;
import tokens.TokenString;
import tokens.WhiteSpace;
/**
 *
 * @author felipe
 */
//TODO OS CASOS 
//TODO RECONHECER PALAVRAS RESERVADAS
//TODO STRING
public class ScannerToken {
    public int posicao;
    public boolean erroLexico = false;
    public  String input;


    WhiteSpace whiteSpace = new WhiteSpace();;
    Comments comments = new Comments();
    Id id = new Id();
    IntergerLiteral intergerLiteral = new IntergerLiteral();
    Sep sep = new Sep();
    Op op = new Op();
    TokenString string = new TokenString();
   
    public ScannerToken(String input){
        this.input = input;
    }
    
    public Token nextToken()
    {   
        Token token = new Token();
        token.setLength(input.length());
        System.out.println(input.length());
        whiteSpace.isWhiteSpace(input, this);
        token.setLine(whiteSpace.getLineCount());
        
        string.tokenDefine(input, this, token);
        comments.tokenDefine(input, this, token);
        intergerLiteral.tokenDefine(input, this, token);
        op.tokenDefine(input, this, token);
        sep.tokenDefine(input, this, token);
        id.tokenDefine(input, this, token);
        isEof(posicao, token);
        return token;
    }
    
    private boolean isEof(int posicao, Token token){
        if(!token.isDefined(token))
            if(posicao == token.getLength()){
                token.setName(Names.EOF);
                return true;
            }
        return false;
    }
    
}
