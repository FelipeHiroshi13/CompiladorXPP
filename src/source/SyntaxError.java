/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.io.PrintWriter;
import java.io.StringWriter;
import compilador.TelaPrincipal;

/**
 *
 * @author 201719050163
 */
public class SyntaxError extends RuntimeException
{
    private String msg;
    public static TelaPrincipal telaPrincipal;
    
    
    public static boolean syntaxError = false;
    public SyntaxError()
    {
        msg = "Unexpected";
    }
    
    
    public SyntaxError(String str)
    {
        
        super(str);
        msg = str;
        syntaxError = true;
        telaPrincipal.jSetTextAreaConsole("Erro Sintatico na Linha " + (Token.getLine()-1) + ": " + msg);
    }
    
    public String toString()
    {
        return msg;
    }
    
}
