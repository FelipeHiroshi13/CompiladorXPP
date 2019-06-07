/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokens;

import source.ScannerToken;

/**
 *
 * @author felipe
 */
public class WhiteSpace {
    private int lineCount;
    public void isWhiteSpace(String input, ScannerToken scannerToken){
       while(scannerToken.posicao < input.length() && (input.charAt(scannerToken.posicao) == ' ' || input.charAt(scannerToken.posicao) == '\n')){
           if(input.charAt(scannerToken.posicao) == '\n')
               lineCount++;
           scannerToken.posicao++;
       }      
    }
    
    public int getLineCount() {
        return lineCount;
    }
}
