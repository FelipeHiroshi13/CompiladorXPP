/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokens;

import source.Names;
import source.ScannerToken;
import source.Token;
import source.TokenDefine;

/**
 *
 * @author felipe
 */
public class Op extends Token implements TokenDefine{
    private String input;
    
    @Override
    public boolean tokenDefine(String input, ScannerToken scannerToken, Token token) {
        boolean case1;
        if(isNoDefineToken(scannerToken.posicao, token))
            return false;
        else{
            this.input = input;
            case1 = advanceOp(input.charAt(scannerToken.posicao), scannerToken, token);
            if(!case1)
               return caseDif(input, scannerToken, token);
            else
                return case1;
        }
    }
    
    private boolean advanceOp(char operation, ScannerToken scannerToken, Token token){
        if(operation == '+' || operation == '-'||operation == '*' ||operation == '/'|| operation == '%'){
            token.setName(Names.OP);
            scannerToken.posicao++;
        }
        switch (operation)
        {
            case '+':
                token.setAttribute(Names.PLUS);
                return true;
            case '-':
                token.setAttribute(Names.MINUS);
                return true;
            case '*':
                token.setAttribute(Names.MULT);
                return true;
            case '/':
                token.setAttribute(Names.DIV);
                return true;
            case '%':
                token.setAttribute(Names.MOD);
                return true;
        }
        return false;
    }

    private boolean caseDif(String input, ScannerToken scannerToken, Token token){
        int posicao = scannerToken.posicao;
        char chr = input.charAt(posicao);
        if(chr == '>')
        {
            token.setName(Names.RELOP);
            token.setAttribute(Names.GT);
            scannerToken.posicao++;
            if(scannerToken.posicao < input.length()){
                //isErrorLexico(scannerToken);
                if(input.charAt(scannerToken.posicao) ==  '='){
                    token.setAttribute(Names.GE);
                    scannerToken.posicao++;
                }
            }
               
        }
        if(chr == '<')
        {
            token.setName(Names.RELOP);
            token.setAttribute(Names.LT);
            scannerToken.posicao++;
            if(scannerToken.posicao < input.length()){
                //isErrorLexico(scannerToken);
                if(input.charAt(scannerToken.posicao) ==  '='){
                    token.setAttribute(Names.LE);
                    scannerToken.posicao++;
                }
            }
               
        }
        if(chr == '='){
            token.setName(Names.RELOP);
            token.setAttribute(Names.EQ);
            scannerToken.posicao++;
            if(scannerToken.posicao <input.length()){  
                //isErrorLexico(scannerToken);
                if(input.charAt(scannerToken.posicao) == '='){
                    token.setName(Names.OP);
                    token.setAttribute(Names.EQUALS);
                    scannerToken.posicao++;
                }
            }
              
            
        }
        if(chr == '!'){
            if(scannerToken.posicao+1 < input.length()){
                scannerToken.posicao++;
                //isErrorLexico(scannerToken);
                if(input.charAt(scannerToken.posicao) ==  '='){
                    token.setName(Names.OP);
                    token.setAttribute(Names.DIF);
                    scannerToken.posicao++;
                }
            }
            
        }
        return true;   
    }
    
    @Override
    public boolean isErrorLexico(ScannerToken scannerToken) {
        //System.out.println(input.charAt(scannerToken.posicao));
        if(input.charAt(scannerToken.posicao) != '=' && input.charAt(scannerToken.posicao) != ' '){
            setLexicalError();
            return true;
        }
        return false;
    }
}
