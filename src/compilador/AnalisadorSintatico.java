package compilador;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import source.Names;
import source.Parser;
import source.Token;
import source.Error;
import static source.Error.syntaxError;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 201719050368
 */
public class AnalisadorSintatico {
    private List<Token> tokenList;
    String msg = "Léxico Ok"
            + "\nSintatico Ok";
            
    public AnalisadorSintatico(List tokenList){
        this.tokenList = tokenList;
    }
    public void inicia(TelaPrincipal telaPrincipal){
        StringWriter sw = new StringWriter();
        String erroSemantico = "";
        Parser parser = new Parser(tokenList);
        parser.program();
        if(!parser.ErroSemantico){
            erroSemantico = "\nSemantico OK";
        }
        telaPrincipal.jSetTextAreaConsole(msg + erroSemantico);
    }
    
    public static Map<Names, String> debug;
    static 
    {
        debug = new HashMap<>();
        debug.put(Names.CLASS, "class");
        debug.put(Names.EXTENDS, "extends");
        debug.put(Names.INT, "int");
        debug.put(Names.STRING, "string");
        debug.put(Names.BREAK, "break");
        debug.put(Names.PRINT, "print");
        debug.put(Names.READ, "read");
        debug.put(Names.RETURN, "return");
        debug.put(Names.SUPER, "super");
        debug.put(Names.IF, "if");
        debug.put(Names.ELSE, "else");
        debug.put(Names.FOR, "for");
        debug.put(Names.NEW, "new");
        debug.put(Names.CONSTRUCTOR, "constructor");
        debug.put(Names.TYPE, "type");
        debug.put(Names.ID, "id");
        debug.put(Names.CHE, "{");
        debug.put(Names.CHD, "}");
        debug.put(Names.COE, "[");
        debug.put(Names.COD, "]");
        debug.put(Names.PE, "(");
        debug.put(Names.PD, ")");
        debug.put(Names.POINT, ".");
        debug.put(Names.POINTV, ";");
        debug.put(Names.TWOPOINTS, ":");
        debug.put(Names.TYPE, "type");
        debug.put(Names.DIF, "!=");
        debug.put(Names.EQ, "=");
        debug.put(Names.EQUALS, "==");
        debug.put(Names.DIV, "/");
        debug.put(Names.MULT, "*");
        debug.put(Names.MOD, "%");
        debug.put(Names.PLUS, "+");
        debug.put(Names.MINUS, "-");
        debug.put(Names.ELSE, "else");
        debug.put(Names.GE, ">=");
        debug.put(Names.GT, ">");
        debug.put(Names.IF, "if");
        debug.put(Names.INTEGER_LITERAL, "inteiro");
        debug.put(Names.LE, "<=");
        debug.put(Names.LT, "<");
        debug.put(Names.RELOP, "relop");
        debug.put(Names.SEP, "separador");
        debug.put(Names.STRINGLITERAL, "string literal");
        debug.put(Names.UNDEF, "undef");
        debug.put(Names.VIR, ",");    
    }
    
}
