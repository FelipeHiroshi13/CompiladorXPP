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
public class TokenString extends Token implements TokenDefine{
    private int posicaoInicial;
    private int posicaoFinal;
    
    @Override
    public boolean tokenDefine(String input, ScannerToken scannerToken, Token token) {
        if(isNoDefineToken(scannerToken.posicao,token))
            return false;
        else{        
            return advanceString(input, scannerToken, token);
        }
    }
    
    private boolean advanceString(String input, ScannerToken scannerToken, Token token){
        if(isNoEnded(scannerToken.posicao) && input.charAt(scannerToken.posicao) == '"'){
            posicaoInicial = scannerToken.posicao+1;
            scannerToken.posicao++;
            while(isNoEnded(scannerToken.posicao)){  
                if(input.charAt(scannerToken.posicao) == '"')
                {
                    posicaoFinal = scannerToken.posicao--;
                    token.setName(Names.STRINGLITERAL);
                    token.setLexema(input.substring(posicaoInicial, posicaoFinal));
                    System.out.println("lexeme: " + getLexeme());
                    scannerToken.posicao += 2;
                    return true;
                }
                scannerToken.posicao++;
            }
            setLexicalError();
        }
        return false;
    }
    
    @Override
    public boolean isErrorLexico(ScannerToken scannerToken) {
        setLexicalError();
        return true;
    }
    
}
