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
 * @author felipe hiroshi
 */
public class Comments extends Token implements TokenDefine{
    private boolean mult = false;
    private boolean div = false;
    
    @Override
    public boolean tokenDefine(String input, ScannerToken scannerToken, Token token){
        if(isNoDefineToken(scannerToken.posicao, token)){
            return false;
        }
        if(input.charAt(scannerToken.posicao) == '/')
        {
            isLine(input, scannerToken, token);
            isBlock(input, scannerToken, token);
        }
        return true;
    }
    
    public boolean isLine(String input, ScannerToken scannerToken, Token token){
        if(isNoEnded(scannerToken.posicao+1) && input.charAt(scannerToken.posicao+1) == '/'){
            scannerToken.posicao += 2;
            while(isNoEnded(scannerToken.posicao)){
                if(input.charAt(scannerToken.posicao) == '\n')
                   break;
                scannerToken.posicao ++;
            }
            token.setName(Names.COMMENTS);
            return true;
        }
        return false;
    }
    
    
    public boolean isBlock(String input, ScannerToken scannerToken, Token token){
        if(isNoEnded(scannerToken.posicao+1) && input.charAt(scannerToken.posicao+1) == '*')
        {
            scannerToken.posicao += 2;
            while(isNoEnded(scannerToken.posicao)){
                if(input.charAt(scannerToken.posicao) == '*'){
                    mult = true;
                    scannerToken.posicao++;
                    if(input.charAt(scannerToken.posicao ) == '/' && mult == true){
                        scannerToken.posicao++;
                        div = true;
                        break;
                    }
                }
                scannerToken.posicao++;
            }
            isErrorLexico(scannerToken);
            if(div == true)
                token.setName(Names.COMMENTS);
            return true;
        }
        return false;
        
    }

    @Override
    public boolean isErrorLexico(ScannerToken scannerToken) {
        if(div != true){
            setLexicalError();
            return true;
        }
        return false;
    }
}
