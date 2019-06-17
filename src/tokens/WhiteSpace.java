
package tokens;

import source.ScannerToken;

/**
 *
 * @author felipe
 */
public class WhiteSpace{
    private int lineCount;
    public void isWhiteSpace(String input, ScannerToken scannerToken){
        while(scannerToken.posicao < input.length() && ((input.charAt(scannerToken.posicao) == ' ' || input.charAt(scannerToken.posicao) == '\n') || input.charAt(scannerToken.posicao) ==  '\t')){
           if(input.charAt(scannerToken.posicao) == '\n')
               lineCount++;
           scannerToken.posicao++;
       }      
    }
    
    public int getLineCount() {
        return lineCount;
    }
}
