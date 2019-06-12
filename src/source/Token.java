package source;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author biancadantas
 */
public class Token 
{
    private  Names name;
    private  Names attribute;
    private  String lexeme;
    private static int length;
    private static int line =1;
    private static boolean erroLexico;
    
    public Token(){
        this.name = Names.UNDEF;
        this.attribute = Names.UNDEF;
        this.lexeme = null;
        erroLexico = false;
    }
    
    public void setName(Names name){
        this.name = name;
    }
    
    public void setAttribute(Names atributte){
        this.attribute = atributte;
    }
    
    public void setLexema(String lexeme){
        this.lexeme = lexeme;
    }
    
    public void setLength(int length){
        this.length = length;
    }
    
    public void setLine(int line){
        this.line = line;
    }
    
    public void setLexicalError(){
        erroLexico = true;
    }
    
    public Names getName(){
        return name;
    }
    
    public Names getAttribute(){
        return attribute;
    }
    
    public String getLexeme(){
        return lexeme;
    }
    
    public int getLength(){
        return length;
    }
    
    public int getLine(){
        return line;
    }
    
    public boolean getLexicalError(){
        return erroLexico;
    }
    
    public StringWriter erroLexico(){
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter (sw);
        out.println("Erro Léxico na linha " + ++line);
        return sw;
    }
    
    public boolean isDefined(Token token){
        return token.getName() != Names.UNDEF;
    }
    
    public boolean isNoEnded(int posicao){
        return posicao < length;
    }
    
    public boolean isNoDefineToken(int posicao, Token token) {
    	return (isDefined(token) || !isNoEnded(posicao));
    }
    
    public boolean isLetter(int posicao, String input){
        return Character.isLetter(input.charAt(posicao));
    }
    
    public boolean isDigit(int posicao, String input){ 
        return Character.isDigit(input.charAt(posicao));
    }
    
    public boolean isVariableType(Token lToken)
    {
        if(lToken.getName() == Names.ID || lToken.getName() == Names.STRING || lToken.getName() == Names.INT)
            return true;
        else
            return false;
    }

    
}
