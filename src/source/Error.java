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
public class Error extends RuntimeException
{
    private String msg;
    public static TelaPrincipal telaPrincipal;
    
    
    public static boolean syntaxError = false;
    public Error()
    {
        msg = "Unexpected";
    }
    
    
    public Error(String str)
    {
        
        super(str);
        msg = str;
        syntaxError = true;
        telaPrincipal.jSetTextAreaConsole("Erro na Linha " + (Token.getLine()) + ": " + msg);
    }
    
    public String toString()
    {
        return msg;
    }
    
}
