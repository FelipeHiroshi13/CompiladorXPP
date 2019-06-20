 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import compilador.AnalisadorSintatico;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static source.Error.telaPrincipal;
import static tokens.Id.reserved;

/**
 *
 * @author Felipe Hiroshi
 */
public class Parser
{
    private int position = 0;
    private List<Token> tokenList;
    public static Token lToken;
    public SymbolTable<STEntry> globalST;
    public SymbolTable<STEntry> currentST;
    public Map<String, SymbolTable> classesSTMap;
    public Map<String, String> classesMapping; 
    private void initSymbolTable()
    {
        Token t;
        
        t = new Token(Names.CLASS);
        globalST.add(new STEntry(t, "class", true));
        t = new Token(Names.EXTENDS);
        globalST.add(new STEntry(t, "extends", true));
        t = new Token(Names.INT);
        globalST.add(new STEntry(t, "int", true));
        t = new Token(Names.STRING);
        globalST.add(new STEntry(t, "string", true));
        t = new Token(Names.BREAK);
        globalST.add(new STEntry(t, "break", true));
        t = new Token(Names.PRINT);
        globalST.add(new STEntry(t, "print", true));
        t = new Token(Names.READ);
        globalST.add(new STEntry(t, "read", true));
        t = new Token(Names.RETURN);
        globalST.add(new STEntry(t, "return", true));
        t = new Token(Names.SUPER);
        globalST.add(new STEntry(t, "super", true));
        t = new Token(Names.IF);
        globalST.add(new STEntry(t, "if", true));
        t = new Token(Names.ELSE);
        globalST.add(new STEntry(t, "else", true));
        t = new Token(Names.FOR);
        globalST.add(new STEntry(t, "for", true));
        t = new Token(Names.NEW);
        globalST.add(new STEntry(t, "new", true));
        t = new Token(Names.CONSTRUCTOR);
        globalST.add(new STEntry(t, "constructor", true));
        t = new Token(Names.TYPE);
        globalST.add(new STEntry(t, "type", true));
    }
    
    public Parser(List tokenList)
    {
        this.tokenList = tokenList;
        globalST = new SymbolTable<STEntry>();
        classesSTMap = new HashMap<>();
        classesMapping = new HashMap<>();
        initSymbolTable();
        currentST = globalST;
    }
    
    private void AddToST(Token lToken, boolean reserved) throws Error
    {
        boolean addedSuccesfully;
        STEntry stEntry = new STEntry(lToken, lToken.getLexeme(), reserved);
        addedSuccesfully = currentST.add(stEntry);
        if(!addedSuccesfully)
        {
            telaPrincipal.jSetTextAreaConsole("Erro Semantico");
        }
    }
    
    private void RemoveST(SymbolTable symbolTable)
    {
        currentST = symbolTable.parent;
        symbolTable.clear();
    }
    
    private SymbolTable CreateNewST(Token lToken, boolean reserved)
    { 
        SymbolTable newSymbolTable = new SymbolTable<>();
        AddToST(lToken, reserved);
        newSymbolTable.parent = currentST;
        currentST = newSymbolTable;
        return newSymbolTable;
    }
    
    public void VerifyAttribute(String classLexeme, String attributeLexeme)
    {
        SymbolTable classSymbolTable;
        classSymbolTable = classesSTMap.get(classesMapping.get(classLexeme));
        System.out.println(classesMapping.get(classLexeme));
        if(classSymbolTable == null)
        {
            telaPrincipal.jSetTextAreaConsole("Classe nao definida");
        }
        else
        {
            if(classSymbolTable.get(attributeLexeme) == null)
            {
                telaPrincipal.jSetTextAreaConsole("Atributo " + attributeLexeme +  " nao existente na classe " + classLexeme);
            }
        }
    }
    
    private void advance()
    {
        position++;
        lToken = tokenList.get(position);
    }
    
    private void back(){
        position--;
        lToken = tokenList.get(position);
    }
    
    private void match (Names tokenCodigo)
    {
        if(lToken.getName() == tokenCodigo)
        {
            advance();
        }
        else
        {
            throw new Error("(Erro Sintático)Identificador esperado: " + AnalisadorSintatico.debug.get(tokenCodigo) + "\nNo lugar de: " + AnalisadorSintatico.debug.get(lToken.getName()));
        }
    }
    
     private void match (Names tokenName, Names tokenAtributo)
     {
        if(lToken.getAttribute()== tokenAtributo)
        {
            advance();
        }
        else
        {
            throw new Error("(Erro Sintatico) Esperava o atributo " + AnalisadorSintatico.debug.get(tokenAtributo) + "\nNo lugar de: " +  AnalisadorSintatico.debug.get(lToken.getAttribute()));
        }
    }
    
    public void program() throws Error
    {
        lToken = tokenList.get(0);
        if(lToken.getAttribute()== Names.CLASS)
        {
            classList();               
        }
        else if (lToken.getName() != Names.EOF)
        {
            throw new Error("Classe mal definida");
        }
    }
    
    public void classList() 
    {
        if (lToken.getAttribute() == Names.CLASS)
        {
            classDecl();
            classListLinha();
        }
        else
        {
            throw new Error("classList mal definida");
        }
    }
    
    public void classListLinha()
    {
        if(lToken.getAttribute() == Names.CLASS)
        {
            classList();               
        } 
    }
    
    public void classDecl() 
    {
        if (lToken.getAttribute() == Names.CLASS)
        {            
            advance();
            SymbolTable newSymbolTable = CreateNewST(lToken, false);
            classesSTMap.put(lToken.getLexeme(), newSymbolTable);
            match(Names.ID, Names.ID);
            classDeclLinha();
        }
        else
        {
            throw new Error("Classe mal definida");
        }
    }
    
    public void classDeclLinha()
    {
        if (lToken.getAttribute() == Names.EXTENDS)
        {
            advance();
            match(Names.ID, Names.ID);
        }
        if(lToken.getAttribute()== Names.CHE)
        {
            classBody();
        }
        else
        {
            throw new Error("varDecl mal definido.");
        }
    }
    
    public void classBody()
    {
        if(lToken.getAttribute()== Names.CHE)
        {
            advance();
            varDeclListOpt();
            constructDeclListOpt();
            methodDeclListOpt();
            match(Names.SEP, Names.CHD);
        }
        else
        {
           throw new Error("Inicio do bloco da classe nao definido");
        }
    }
    
    public void varDeclListOpt()
    {
        if(lToken.isVariableType(lToken))
        {
            varDeclList();
        }
    }
    
    public void varDeclList()
    {
        if(lToken.isVariableType(lToken))
        {
            varDecl();
            if(lToken.isVariableType(lToken))
            {
                varDeclListinha();
            }
        }
        else
        {
            throw new Error("varDecl mal definido.");
        }
    }
    
    public void varDeclListinha()
    {   
        if(lToken.isVariableType(lToken))
        {
            varDecl();
            varDeclListinha();
        }
    }
    
    public void varDecl()
    {
        if(lToken.isVariableType(lToken))
        {
            type();
            varDeclLinha();
        }
        else
        {
            throw new Error("varDecl mal definido.");
        }
    }
    
    public void varDeclLinha()
    {
        boolean method = true;
        if (lToken.getAttribute()== Names.COE) 
        {
            advance();
            match(Names.SEP, Names.COD);
        }
        if(lToken.getAttribute() == Names.EQ){
            back();
            atribStat();
            return;
        }
        
        
        
        match(Names.ID, Names.ID);
        
        if (lToken.getAttribute()== Names.VIR){
            varDeclOpt();
            method = false;
        }
        if(lToken.getAttribute() != Names.PE){
            System.out.println(tokenList.get(position-2).getAttribute());
            if(tokenList.get(position-2).getAttribute() == Names.ID)
                classesMapping.put( tokenList.get(position-1).getLexeme() , tokenList.get(position-2).getLexeme());
            AddToST(tokenList.get(position-1), false);
            match(Names.SEP, Names.POINTV);
        }
        else if(method){
            CreateNewST(tokenList.get(position-1), false);
            methodBody();
        }
        
        
        
    }
    
    public void varDeclOpt()
    {
        if (lToken.getAttribute()== Names.VIR)
        {
            advance();
            match(Names.ID);
            varDeclOpt();
        }
    }

    public void type()
    {
        if(lToken.isVariableType(lToken))
        {
            
            advance();
        }
        else
        {
            throw new Error("Tipo de variavel nao existente");
        }
    }
    
    public void constructDeclListOpt()
    {
        if (lToken.getAttribute() == Names.CONSTRUCTOR)
        {
            constructDeclList();
        }
    }
    
    public void constructDeclList() // Esse metodo e o de baixo sao realmente necessarios ?
    {
        if (lToken.getAttribute()== Names.CONSTRUCTOR)
        {
            constructDecl();
            constructDeclListLinha();
        }
        else
        {
            throw new Error("constructDeclList mal definido");
        }
    }
    
    public void constructDeclListLinha()
    {
        if (lToken.getAttribute()== Names.CONSTRUCTOR)
        {
            constructDecl();
            constructDeclListLinha();
        }
    }
     
    public void constructDecl()
    {
        if (lToken.getAttribute() == Names.CONSTRUCTOR)
        {
            CreateNewST(lToken, false);
            match(Names.ID, Names.CONSTRUCTOR);
            methodBody();
        }
        else
        {
            throw new Error("constructDecl mal definido");
        }
    }
    
    public void methodDeclListOpt()
    {
         if(lToken.isVariableType(lToken))
         {
             methodDeclList();
         }
    }
    
    public void methodDeclList()
    {
        if(lToken.isVariableType(lToken))
        {
            methodDecl();
            methodDeclListLinha();
        } 
        else
        {
            throw new Error("methodDeclList mal definido");
        }
    }
    
    public void methodDeclListLinha()
    {
        if(lToken.isVariableType(lToken))
        {
            methodDecl();
            methodDeclListLinha();
        } 
       
    }

    public void methodDecl()
    {
        if(lToken.isVariableType(lToken))
        {
            type();
            methodDeclLinha();
        }
        else
        {
            throw new Error("methodDecl mal definido");
        }
    }
    
    public void methodDeclLinha()
    {
        if (lToken.getAttribute()== Names.COE) 
        {
            advance();
            match(Names.SEP, Names.COD);
        }
        if (lToken.getAttribute() == Names.ID)
        {
            CreateNewST(lToken, false);
            match(Names.ID);
            methodBody();
        }
        else
        {
            throw new Error("methodDeclLinha mal definido");
        }
    }
    
    public void methodBody()
    {
        if (lToken.getAttribute() == Names.PE) 
        {
            match(Names.SEP, Names.PE);
            paramListOpt();
            match(Names.SEP, Names.PD);
            match(Names.SEP, Names.CHE);
            statementsOpt();
            match(Names.SEP, Names.CHD);
            RemoveST(currentST);
        }
        else
        {
            throw new Error("methodBody mal definido");
        }
    }
    
    public void paramListOpt()
    {
        if (lToken.isVariableType(lToken))
            paramList();
    }
    
    public void paramList()
    {
        if ((lToken.isVariableType(lToken)))
        {
            param();
            paramListLinha();
        }
        else
        {
            throw new Error("paramList mal definido");
        }
    }
    
    public void paramListLinha()
    {
        if (lToken.getAttribute() == Names.VIR)
        {
            match(Names.SEP, Names.VIR);
            param();
            paramListLinha();
        }
    }
    
    public void param()
    {
        if ((lToken.isVariableType(lToken)))
        {
            type();
            paramLinha();   
        }
        else
        {
            throw new Error("param mal definido");
        }
    }
    
    // ################################ VERIFICAR PARAMLINHA #############################
    public void paramLinha()
    {
        if (lToken.getAttribute() == Names.COE)
        {
            advance();
            match(Names.SEP, Names.COD);
        }
        if (lToken.getAttribute() == Names.ID)
        {
            match(Names.ID, Names.ID);
        }
        else
        {
            throw new Error("paramLinha mal definido");
        }
    }
    
    public void statementsOpt()
    {
        if (lToken.IsStatementType(lToken))
        {
            
            statements();
        }
    }
    
    public void statements()
    {
        if (lToken.IsStatementType(lToken))
        {
            statement();
            statementsLinha();
            
            
        }
        else
        {
            throw new Error("statements mal definido");
        }
    }
    
    public void statementsLinha()
    {
       
        if (lToken.IsStatementType(lToken))
        {
            statements();
        }
    }
   
    public void statement()    
    {
        if(lToken.isVariableType(lToken) && (this.nextToken(position + 1).getAttribute() == Names.ID || (this.nextToken(position + 1).getAttribute() == Names.COE && this.nextToken(position + 2).getAttribute() == Names.COD)))
        {
            varDeclList(); 
        }
        else if(lToken.getAttribute() == Names.ID)
        {
            atribStat();
            match(Names.SEP, Names.POINTV);
        }
        else if(lToken.getAttribute() == Names.PRINT)
        {
            printStat();
            match(Names.SEP, Names.POINTV);
        }
        else if(lToken.getAttribute() == Names.READ)
        {
            readStat();
            match(Names.SEP, Names.POINTV);
        }
        else if(lToken.getAttribute() == Names.RETURN)
        {
            returnStat();
            match(Names.SEP, Names.POINTV);
        }
        else if(lToken.getAttribute() == Names.SUPER)
        {
            superStat();
            match(Names.SEP, Names.POINTV);
        }
        else if(lToken.getAttribute() == Names.IF)
        {
      
            ifStat();
        }
        else if(lToken.getAttribute() == Names.FOR)
        {

            forStat();
        }	
        else if(lToken.getAttribute() == Names.BREAK)
        {
            advance();
            match(Names.SEP, Names.POINTV);
        }
        else if(lToken.getAttribute() == Names.POINTV)
        {
            match(Names.SEP, Names.POINTV);
        }
        else
        {
            throw new Error("Statment mal definido");
        }
    }
    
    public void atribStat()
    {
        
        if (lToken.getAttribute() == Names.ID)
        {
            lValue();
            match(Names.RELOP, Names.EQ);
            atribStatLinha();
//            match(Names.SEP, Names.POINTV);
        }
        else
        {
            throw new Error("atribStatment mal definido");
        }
    }
    
    public void atribStatLinha()
    {
        if (lToken.getAttribute() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
            expression();
        }
        else if (lToken.getAttribute()== Names.NEW || lToken.isVariableType(lToken))
        {
            allocExpression();
        }
        else
        {
            throw new Error("atribStatLinha mal definido");
        }
    }
    
    public void printStat()
    {
        if (lToken.getAttribute() == Names.PRINT)
        {
            advance();
            expression();
        }
        else
        {
            throw new Error("printStatment mal definido");
        }
    }
    
    public void readStat()
    {
        if (lToken.getAttribute() == Names.READ)
        {
            advance();
            lValue();
        }
        else
        {
            throw new Error("readStatment mal definido");
        }
     
    }
    
    public void returnStat()
    {
        if (lToken.getAttribute() == Names.RETURN)
        {
            advance();
            expression();
        }
        else
        {
            throw new Error("returnStatment mal definido");
        }
    }
    
    public void superStat()
    {
        if (lToken.getAttribute() == Names.SUPER)
        {
            advance();
            match(Names.SEP, Names.PE);
            argListOpt();
            match(Names.SEP, Names.PD);
        }
        else
        {
            throw new Error("superStatment mal definido");
        }
    }
    
    public void ifStat()
    {
        if (lToken.getAttribute() == Names.IF)
        {
            advance();
            match(Names.SEP, Names.PE);
            expression();
            match(Names.SEP,Names.PD);
            match(Names.SEP,Names.CHE);
            statements();
            match(Names.SEP,Names.CHD);
            ifStatLinha();
        }
        else
        {
            throw new Error("ifStatment mal definido");
        }
    }
    
    public void ifStatLinha()
    {
    	 if (lToken.getAttribute() == Names.ELSE)
         {
                advance();
    		match(Names.SEP,Names.CHE);
    		statements();
    		match(Names.SEP,Names.CHD);
         }
    }
    
    public void forStat()
    {
    	if (lToken.getAttribute() == Names.FOR)
        {
    		advance();
    		match(Names.SEP,Names.PE);
    		atribStatOpt();
//              advance();
                match(Names.SEP,Names.POINTV);
    		expressionOpt();
    		match(Names.SEP,Names.POINTV);
    		atribStatOpt();
    		match(Names.SEP,Names.PD);
    		match(Names.SEP,Names.CHE);
    		statements();
    		match(Names.SEP,Names.CHD);
        }
        else
        {
            throw new Error("forStatment mal definido");
        }
    }
    
    public void atribStatOpt()
    {
    	if (lToken.getAttribute() == Names.ID)
        {
    		atribStat();
        }
    }
    
    public void expressionOpt()
    {
    	if (lToken.getAttribute() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
    		expression();
        }
    }
    
    public void lValue()
    {
    	if (lToken.getAttribute()== Names.ID)
        {
            
            match(Names.ID);
            lValueLinha();
        }else{
            throw new Error("Identificador inválido");
        }
    }
    
    public void lValueLinha()
    {
    	if (lToken.getAttribute() == Names.COE)
        {
    		advance();
    		expression();
    		match(Names.SEP,Names.COD);
        }
    	lValueComp();
    }
    
    public void lValueComp()
    {
    	if (lToken.getAttribute() == Names.POINT)
        {
    		advance();            
    		match(Names.ID);
                VerifyAttribute(tokenList.get(position-3).getLexeme(), tokenList.get(position-1).getLexeme());
    		lValueCompLinha();
        }
    }
    
    public void lValueCompLinha()
    {
    	if (lToken.getAttribute() == Names.COE)
        {
    		advance();
    		expression();
    		match(Names.SEP,Names.COD);
        }
    	lValueComp();
    }
    
    public void expression()
    {
    	if (lToken.getAttribute() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
    		numExpression();
    		expressionLinha();
        }
        else
        {
            throw new Error("Expressão mal definida");
        }
    }
    
    public void expressionLinha()
    {
        if(lToken.getName() == Names.RELOP)
        {
            advance();
            numExpression();
        }
    }
    
    public void allocExpression()
    {
    	if (lToken.getAttribute()== Names.NEW)
        {
            advance();
            match(Names.ID);
            match(Names.SEP,Names.PE);
            argListOpt();
            match(Names.SEP,Names.PD);
        }
    	else if(lToken.isVariableType(lToken))
    	{
            advance();
            match(Names.SEP,Names.COE);
            expression();
            match(Names.SEP,Names.COD);
    	}
        else
        {
            throw new Error("allocExpression mal definido.");
        }
    }
    
    public void numExpression()
    {
    	if (lToken.getAttribute() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
            term();
            numExpressionLinha();
        }
        else
        {
            throw new Error("numExpression mal definido.");
        }
    }
    
    public void numExpressionLinha()
    {
    	if (lToken.getAttribute() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
            advance();
            term();
        }
    	
    }
    
    public void term()
    {
    	if (lToken.getAttribute() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
            unaryExpression();
            termLinha();
        }
        else
        {
            throw new Error("Termo mal definido.");
        }
    }
    
    public void termLinha()
    {
    	if (lToken.getAttribute() == Names.MULT || lToken.getAttribute() == Names.DIV || lToken.getAttribute() == Names.MOD)
        {
            advance();
            unaryExpression();
        }
    }
    
    public void unaryExpression()
    {
    	if (lToken.getAttribute() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
            advance();
            factor();
        }
        else
        {
            throw new Error("UnaryExpression mal definido.");
        }
    }
    
    public void factor()
    {
    	if (lToken.getName() == Names.INTEGER_LITERAL)
        {
            advance();
        }
    	else if (lToken.getName() == Names.STRINGLITERAL)
        {
            advance();
        }
    	else if (lToken.getAttribute()== Names.ID)
        {
            //advance();
            lValue();
        }
        else if (lToken.getAttribute() == Names.PE)
    	{
            match(Names.SEP,Names.PE);
            expression();
            match(Names.SEP,Names.PD);
    	}
        else
        {
            throw new Error("Fator mal definido.");
        }
    }
    
    public void argListOpt()
    {
    	if (lToken.getAttribute()== Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
    		argList();
        }
    }
    
    public void argList()
    {
    	if (lToken.getAttribute() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
    		expression();
    		argListLinha();
        }
        else
        {
            throw new Error("Argumento inválido.");
        }
    }
    
    public void argListLinha()
    {
    	if (lToken.getAttribute() == Names.VIR)
        {
    		advance();
    		argList();
                argListLinha();
    		//match(Names.SEP,Names.VIR);
        }
    }

    private Token nextToken(int i){
       return tokenList.get(i);
    }
}
