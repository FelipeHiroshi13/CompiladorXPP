/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import source.Names;
import source.ScannerToken;
import source.Token;
import source.TokenDefine;

/**
 *
 * @author felipe
 */
public class Id extends Token implements TokenDefine{
    //String reserved [] = {"class", "extends", "int", "string", "break", "print", "read", "return", "super", "if", "else", "for", "new", "constructor"};
    public static Map<String, Names> reserved;
    static 
    {
        reserved = new HashMap<>();
        reserved.put("class", Names.CLASS);
        reserved.put("extends", Names.EXTENDS);
        reserved.put("int", Names.INT);
        reserved.put("string", Names.STRING);
        reserved.put("break", Names.BREAK);
        reserved.put("print", Names.PRINT);
        reserved.put("read", Names.READ);
        reserved.put("return", Names.RETURN);
        reserved.put("super", Names.SUPER);
        reserved.put("if", Names.IF);
        reserved.put("else", Names.ELSE);
        reserved.put("for", Names.FOR);
        reserved.put("new", Names.NEW);
        reserved.put("constructor", Names.CONSTRUCTOR);
        reserved.put("type", Names.TYPE);
        reserved.put("string", Names.STRING);
    }
    public List<String> idList = new ArrayList<String>();
    
    @Override
    public boolean tokenDefine(String input, ScannerToken scannerToken, Token token) {
        if(isNoDefineToken(scannerToken.posicao, token)){
            return false;
        }else{
            
            return advanceId(input, scannerToken, token);
        }
    }
    
    public boolean advanceId(String input, ScannerToken scannerToken, Token token){
        if(isLetter(scannerToken.posicao, input) || input.charAt(scannerToken.posicao) == '_')
        {
            int startPos = scannerToken.posicao;
            scannerToken.posicao++;
            while(isNoEnded(scannerToken.posicao) && (isLetter(scannerToken.posicao, input) || isDigit(scannerToken.posicao, input))){
                scannerToken.posicao++;
            }
            token.setName(Names.ID);
            token.setLexema(input.substring(startPos, scannerToken.posicao));
            idList.add(input.substring(startPos, scannerToken.posicao));
            isReserved(idList.get(idList.size()-1),token);
            return true;
        }else
            return false;
    }
    
    //TODO fazer algo com as palavras reservadas
    public void isReserved(String input, Token lToken){
        if(reserved.containsKey(input))
        {
            lToken.setName(reserved.get(input));
        }
    }

    @Override
    public boolean isErrorLexico(ScannerToken scannerToken) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
