package tokens;

import source.Names;
import source.ScannerToken;
import source.Token;
import source.TokenDefine;

public class IntergerLiteral extends Token implements TokenDefine{
	private int posicaoInicial;
        private int posicaoFinal;
        private String input;
	
	@Override
	public boolean tokenDefine(String input, ScannerToken scannerToken, Token token) {
		if(isNoDefineToken(scannerToken.posicao, token)) {
			return false;
		}else {
			this.input = input;
			if(isNegative(input, scannerToken, token))
				return true;
			else if(isPositive(input, scannerToken, token))
				return true;
			else
				return false;
		}
	}
	
	private boolean isNegative(String input, ScannerToken scannerToken, Token token) {
		 if(input.charAt(scannerToken.posicao) == '-'){
	            if(isNoEnded(scannerToken.posicao+1) && isDigit(scannerToken.posicao+1, input)){
	                posicaoInicial = scannerToken.posicao;
	                scannerToken.posicao += 2;
	                while(isNoEnded(scannerToken.posicao ) && isDigit(scannerToken.posicao, input))
	                    scannerToken.posicao++;
	                isErrorLexico(scannerToken);
	                posicaoFinal = scannerToken.posicao;
	                token.setName(Names.INTEGER_LITERAL);
	                token.setLexema(input.substring(posicaoInicial, posicaoFinal));
	                return true;
	            }
		 }
		 return false;
	}
	
	private boolean isPositive(String input, ScannerToken scannerToken, Token token) {
		if(getName() != Names.INTEGER_LITERAL){
            if(isDigit(scannerToken.posicao, input)){
                posicaoInicial = scannerToken.posicao;
                scannerToken.posicao++;
                while(isNoEnded(scannerToken.posicao) && isDigit(scannerToken.posicao, input))
                    scannerToken.posicao++;
                isErrorLexico(scannerToken);
                posicaoFinal = scannerToken.posicao;
                token.setName(Names.INTEGER_LITERAL);
                token.setLexema(input.substring(posicaoInicial, posicaoFinal));
                return true;
            }
        }
		return false;
	}

    @Override
    public boolean isErrorLexico(ScannerToken scannerToken) {
        if(isNoEnded(scannerToken.posicao) && (isLetter(scannerToken.posicao, input))){
	    setLexicalError();
            return true;
        }
        return false;
    }


}
