package tokens;

import source.Names;
import source.ScannerToken;
import source.Token;
import source.TokenDefine;

public class Sep extends Token implements TokenDefine{
    @Override
    public boolean tokenDefine(String input, ScannerToken scannerToken, Token token) {
            if(isNoDefineToken(scannerToken.posicao, token)) {
                    return false;
            }else {
                return adavanceSep(input.charAt(scannerToken.posicao), scannerToken, token);
            }
    }
 
    private boolean adavanceSep(char chr, ScannerToken scannerToken, Token token){
        if(chr == ')' || chr == '(' || chr ==  '{' || chr == '}' || chr == ';' || chr == ',' || chr == '.' || chr == '[' || chr == ']' || chr == ':'){
            token.setName(Names.SEP);
            scannerToken.posicao++;
        }
        switch(chr){
            case '(':
               token.setAttribute(Names.PE);
               return true;
            case ')':
               token.setAttribute(Names.PD);
               return true;
            case '{':
               token.setAttribute(Names.CHE);
               return true;
            case '}':
               token.setAttribute(Names.CHD);
               return true;
            case ';':
               token.setAttribute(Names.POINTV);
               return true;
            case ',':
               token.setAttribute(Names.VIR);
               return true;
            case '.':
               token.setAttribute(Names.POINT);
               return true;
            case '[':
               token.setAttribute(Names.COE);
               return true;
            case ']':
               token.setAttribute(Names.COD);
               return true;
            case ':':
               token.setAttribute(Names.TWOPOINTS);
               return true;
           }
     	return false;
    }

    @Override
    public boolean isErrorLexico(ScannerToken scannerToken) {
        return true;
    }
}
