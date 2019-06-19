 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import compilador.AnalisadorSintatico;
import java.util.List;

/**
 *
 * @author Felipe Hiroshi
 */
public class Parser
{
    private int position = 0;
    private List<Token> tokenList;
    private Token lToken;
    public SymbolTable<STEntry> globalST;
    
    private void initSymbolTable()
    {
        Token t;
        
        t = new Token();
        t.setName(Names.CLASS);
        globalST.add(new STEntry(t, "class", true));
        t = new Token();
        t.setName(Names.EXTENDS);
        globalST.add(new STEntry(t, "extends", true));
        t = new Token();
        t.setName(Names.CONSTRUCTOR);
        //CONTINUA COM AS DEMAIS PALAVRAS RESERVADAS
    }
    
    public Parser(List tokenList)
    {
        this.tokenList = tokenList;
        globalST = new SymbolTable<STEntry>();
       
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
            throw new SyntaxError("Identificador esperado: " + AnalisadorSintatico.debug.get(tokenCodigo) + "\nNo lugar de: " + AnalisadorSintatico.debug.get(lToken.getName()));
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
            throw new SyntaxError("Esperava o atributo " + AnalisadorSintatico.debug.get(tokenAtributo) + "\nNo lugar de: " +  AnalisadorSintatico.debug.get(lToken.getAttribute()));
        }
    }
    
    public void program() throws SyntaxError
    {
        lToken = tokenList.get(0);
        if(lToken.getAttribute()== Names.CLASS)
        {
            classList();               
        }
        else if (lToken.getName() != Names.EOF)
        {
            throw new SyntaxError("Classe mal definida");
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
            throw new SyntaxError("classList mal definida");
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
            match(Names.ID, Names.ID);
            classDeclLinha();
        }
        else
        {
            throw new SyntaxError("Classe mal definida");
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
            throw new SyntaxError("varDecl mal definido.");
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
           throw new SyntaxError("Inicio do bloco da classe nao definido");
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
            throw new SyntaxError("varDecl mal definido.");
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
            throw new SyntaxError("varDecl mal definido.");
        }
    }
    
    public void varDeclLinha()
    {
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
        varDeclOpt();
        if(lToken.getAttribute() != Names.PE){
            match(Names.SEP, Names.POINTV);
        }
        else{
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
            throw new SyntaxError("Tipo de variavel nao existente");
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
            throw new SyntaxError("constructDeclList mal definido");
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
            match(Names.ID, Names.CONSTRUCTOR);
            methodBody();
        }
        else
        {
            throw new SyntaxError("constructDecl mal definido");
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
            throw new SyntaxError("methodDeclList mal definido");
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
            throw new SyntaxError("methodDecl mal definido");
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
            match(Names.ID);
            methodBody();
        }
        else
        {
            throw new SyntaxError("methodDeclLinha mal definido");
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
        }
        else
        {
            throw new SyntaxError("methodBody mal definido");
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
            throw new SyntaxError("paramList mal definido");
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
            throw new SyntaxError("param mal definido");
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
            throw new SyntaxError("paramLinha mal definido");
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
            throw new SyntaxError("statements mal definido");
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
            throw new SyntaxError("Statment mal definido");
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
            throw new SyntaxError("atribStatment mal definido");
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
            throw new SyntaxError("atribStatLinha mal definido");
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
            throw new SyntaxError("printStatment mal definido");
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
            throw new SyntaxError("readStatment mal definido");
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
            throw new SyntaxError("returnStatment mal definido");
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
            throw new SyntaxError("superStatment mal definido");
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
            throw new SyntaxError("ifStatment mal definido");
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
            throw new SyntaxError("forStatment mal definido");
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
            throw new SyntaxError("Identificador inválido");
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
            throw new SyntaxError("Expressão mal definida");
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
            throw new SyntaxError("allocExpression mal definido.");
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
            throw new SyntaxError("numExpression mal definido.");
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
            throw new SyntaxError("Termo mal definido.");
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
            throw new SyntaxError("UnaryExpression mal definido.");
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
            throw new SyntaxError("Fator mal definido.");
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
            throw new SyntaxError("Argumento inválido.");
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
