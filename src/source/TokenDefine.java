package source;

public interface TokenDefine {

    public abstract boolean tokenDefine(String input, ScannerToken scannerToken, Token token);
    
    public abstract boolean isErrorLexico(ScannerToken scannerToken);
    
}
