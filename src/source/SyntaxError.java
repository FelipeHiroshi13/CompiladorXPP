/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

/**
 *
 * @author 201719050163
 */
public class SyntaxError extends RuntimeException
{
    private String msg;
    
    public SyntaxError()
    {
        msg = "Unexpected";
    }
    
    
    public SyntaxError(String str)
    {
        super(str);
        msg = str;
    }
    
    public String toString()
    {
        return msg;
    }
    
}
