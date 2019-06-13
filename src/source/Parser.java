 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.util.List;

/**
 *
 * @author Felipe Hiroshi
 */
public class Parser {
    private int position = 0;
    private List<Token> tokenList;
    private Token lToken;
    public Parser(List tokenList){
        this.tokenList = tokenList;
    }
    
    private void advance(){
        position++;
        lToken = tokenList.get(position);
    }
    
    private void match (Names tokenCodigo){
        if(lToken.getName() == tokenCodigo)
            advance();
        else
            throw new SyntaxError("Identificador esperado");
    }
    
     private void match (Names tokenName, Names tokenAtributo){
        if(lToken.getAttribute()== tokenAtributo)
            advance();
        else
            throw new SyntaxError("Identificador esperado");
    }
    
    public void program() throws SyntaxError
    {
        lToken = tokenList.get(0);
        if(lToken.getName() == Names.CLASS)
        {
            classList();               
        }
        else if(lToken.getName() != Names.EOF)
        {
            throw new SyntaxError("Classe mal definida");
        }
    }
    
    public void classList() 
    {
        if (lToken.getName() == Names.CLASS)
        {
            classDecl();
            classListLinha();
        }
        else
            throw new SyntaxError("Classe mal definida");
        
    }
    
    public void classListLinha()
    {
         if(lToken.getName() == Names.CLASS)
        {
            classList();               
        } else
            throw new SyntaxError("Classe mal definida");
    }
    
    public void classDecl() 
    {
        if (lToken.getName() == Names.CLASS)
        {            
            advance();
            match(Names.ID);
            classDeclLinha();
        }
        else
            throw new SyntaxError("Classe mal definida");
        
    }
    
    public void classDeclLinha()
    {
        if (lToken.getName() == Names.EXTENDS)
        {
            advance();
            match(Names.ID);
        }
        classBody();

    }
    
    public void classBody()
    {
        if(lToken.getAttribute()== Names.CHE){
            System.out.println("oioi");
            advance();
            varDeclListOpt();
            constructDeclListOpt();
            methodDeclListOpt();
            match(Names.SEP, Names.CHD);
        }
        else
           throw new SyntaxError("Inicio do bloco da classe nao definido");
            
    }
    public void varDeclListOpt()
    {
        if(lToken.isVariableType(lToken))
        {
            varDeclList();
        }
        else
            throw new SyntaxError("Tipo da variavel mal definido");
    }
    public void varDeclList()
    {
        if(lToken.isVariableType(lToken))
        {
            varDecl();
            varDeclListinha();
        }
        else
            throw new SyntaxError("Tipo da variavel mal definido");
    }
    
    public void varDeclListinha()
    {
        if(lToken.isVariableType(lToken))
        {
            varDecl();
            varDeclListinha();
        } else
            throw new SyntaxError("Tipo da variavel mal definido");
    }
    
    public void varDecl()
    {
        if(lToken.isVariableType(lToken))
        {
            advance();
            varDeclLinha();
        }
    }
    
    public void varDeclLinha()
    {
        if(lToken.isVariableType(lToken))
        {
            advance();
            if (lToken.getAttribute()== Names.COE) 
            {
                advance();
                match(Names.SEP, Names.COD);
            }
            else
                throw new SyntaxError("Colchetes nao aberto");
        }
        else
            throw new SyntaxError("Tipo da variavel mal definido");
        
        match(Names.ID);
        varDeclOpt();
    }
    
    public void varDeclOpt()
    {
        if (lToken.getAttribute()== Names.VIR)
        {
            advance();
            match(Names.ID);
            varDeclOpt();
        }
        else
              throw new SyntaxError("Variavel seguinte de mesmo tipo nao definida");
    }

    public void type()
    {
        if(lToken.isVariableType(lToken))
        {
            advance();
        }
        else
            throw new SyntaxError("Tipo de variavel nao existente");
            
    }
    public void constructDeclListOpt()
    {
        if (lToken.getName() == Names.CONSTRUCTOR)
            constructDeclList();
        else
             throw new SyntaxError("Construtor nao definido");
    }
    public void constructDeclList() // Esse metodo e o de baixo sao realmente necessarios ?
    {
        if (lToken.getName() == Names.CONSTRUCTOR)
        {
            constructDecl();
            constructDeclListLinha();
        }
        else
            throw new SyntaxError("Construtor nao definido");
        
    }
    
     public void constructDeclListLinha()
    {
        if (lToken.getAttribute()== Names.CONSTRUCTOR)
        {
            constructDecl();
            constructDeclListLinha();
        }
        else
            throw new SyntaxError("Construtor nao definido");
    }
     
    public void constructDecl()
    {
        if (lToken.getName() == Names.CONSTRUCTOR)
        {
            match(Names.CONSTRUCTOR);
            methodBody();
        }
        else
             throw new SyntaxError("Construtor nao definido");
        
    }
    public void methodDeclListOpt()
    {
         if(lToken.isVariableType(lToken))
         {
             methodDeclList();
         }
         else   
            throw new SyntaxError("Tipo de variavel nao definido");
    }
    public void methodDeclList()
    {
        if(lToken.isVariableType(lToken))
        {
            methodDecl();
            methodDeclListLinha();
        } 
        else   
            throw new SyntaxError("Tipo de variavel nao definido");
    }
    
    public void methodDeclListLinha()
    {
        if(lToken.isVariableType(lToken))
        {
            methodDecl();
            methodDeclListLinha();
        } 
        else   
            throw new SyntaxError("Tipo de variavel nao definido");
    }
    
    public void methodDecl()
    {
        if(lToken.isVariableType(lToken))
        {
            type();
            methodDeclLinha();
        }
        else   
            throw new SyntaxError("Tipo de variavel nao definido");
    }
    public void methodDeclLinha()
    {
        if (lToken.getAttribute()== Names.COE) 
        {
            advance();
            match(Names.SEP, Names.COD);
        }
        match(Names.ID);
        methodBody();
    }
    public void methodBody()
    {
        if (lToken.getAttribute() == Names.PE) 
        {
            match(Names.SEP, Names.PE);
            paramListOpt();
            match(Names.SEP, Names.PD);
            match(Names.SEP, Names.COE);
            statementsOpt();
            match(Names.SEP, Names.COD);
        }
        else   
            throw new SyntaxError("Parenteses nao aberto");
    }
    public void paramListOpt()
    {
        if ((lToken.isVariableType(lToken)))
            paramList();
        else
            throw new SyntaxError("Tipo de variavel nao definido");

    }
    public void paramList()
    {
        if ((lToken.isVariableType(lToken)))
        {
            param();
            paramListLinha();
        }
        else
            throw new SyntaxError("Tipo de variavel nao definido");
    }
    
     public void paramListLinha()
    {
        if (lToken.getAttribute() == Names.VIR)
        {
            match(Names.SEP, Names.VIR);
            param();
            paramListLinha();
        }
        else
            throw new SyntaxError("Ausencia do divisor de parametros");
              
    }
    public void param()
    {
        if ((lToken.isVariableType(lToken)))
        {
            type();
            paramLinha();   
        }
        else
            throw new SyntaxError("Tipo de variavel nao definido");

    }
    
    public void paramLinha()
    {
        if (lToken.getAttribute() == Names.COE)
        {
            advance();
            match(Names.SEP, Names.COD);
        }
        match(Names.ID);
    }
    
    public void statementsOpt()
    {
        if (lToken.getName() == Names.ID || lToken.getAttribute() == Names.POINTV)
        {
            statements();
        }
        else
            throw new SyntaxError();
    }
    public void statements()
    {
        
        if (lToken.getName() == Names.ID || lToken.getAttribute() == Names.POINTV)
        {
            statement();
            statementsLinha();
        }
    }
    
    public void statementsLinha()
    {
        if (lToken.getName() == Names.ID || lToken.getAttribute() == Names.POINTV)
        {
            statement();
        }
    }
   
     
    public void statement()    
    {
        if(lToken.isVariableType(lToken))
            varDeclList();  
        else if(lToken.getName() == Names.ID)
            atribStat();
        else if(lToken.getName() == Names.PRINT)
            printStat();
        else if(lToken.getName() == Names.ID)
        	readStat();
        else if(lToken.getName() == Names.RETURN)
        	returnStat();
        else if(lToken.getName() == Names.SUPER)
        	superStat();
        else if(lToken.getName() == Names.IF)
        	ifStat();
        else if(lToken.getName() == Names.FOR)
        	forStat();
        else if(lToken.getName() == Names.BREAK)
        {
        	advance();
        	match(Names.SEP, Names.POINTV);
        }
        else
        	match(Names.SEP, Names.POINTV);
        
   
    }
    public void atribStat()
    {
        if (lToken.getName() == Names.ID)
        {
            lValue();
            match(Names.RELOP, Names.EQUALS);
            atribStatLinha();
        }
        else
            throw new SyntaxError("Atribuicao de declaracao invalida");

    }
    public void atribStatLinha()
    {
        if (lToken.getAttribute() == Names.PLUS)
        {
            expression();
        }
        else if (lToken.getName()== Names.NEW)
        {
            allocExpression();
        }
        else
            throw new SyntaxError("Token invalido");
    }
    public void printStat()
    {
        if (lToken.getName() == Names.PRINT)
        {
            advance();
            expression();
        }
        else
            throw new SyntaxError("Comando nao reconhecido");
    }
    public void readStat()
    {
        if (lToken.getName() == Names.READ)
        {
            advance();
            lValue();
        }
        else
            throw new SyntaxError("Comando nao reconhecido");
    }
    public void returnStat()
    {
        if (lToken.getName() == Names.RETURN)
        {
            advance();
            expression();
        }
        else
            throw new SyntaxError("Comando nao reconhecido");
    }
    public void superStat()
    {
        if (lToken.getName() == Names.SUPER)
        {
            match(Names.SEP, Names.PE);
            argListOpt();
            match(Names.SEP, Names.PD);
        }
        else
            throw new SyntaxError("Comando nao reconhecido");
    }
    public void ifStat()
    {
        if (lToken.getName() == Names.IF)
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
            throw new SyntaxError("Comando nao reconhecido");
    }
    public void ifStatLinha()
    {
    	 if (lToken.getName() == Names.ELSE)
         {
    		 match(Names.SEP,Names.CHE);
    		 statements();
    		 match(Names.SEP,Names.CHD);
         }
    }
    public void forStat()
    {
    	if (lToken.getName() == Names.FOR)
        {
    		advance();
    		match(Names.SEP,Names.PE);
    		atribStatOpt();
    		match(Names.SEP,Names.POINTV);
    		expressionOpt();
    		match(Names.SEP,Names.POINTV);
    		atribStatOpt();
    		match(Names.SEP,Names.PD);
    		match(Names.SEP,Names.CHE);
    		statements();
    		match(Names.SEP,Names.CHD);
        }
    }
    public void atribStatOpt()
    {
    	if (lToken.getName() == Names.ID)
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
    	if (lToken.getName() == Names.ID)
        {
    		lValueLinha();
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
    }
     public void expressionLinha()
    {
    	 //TODO:Verificar se eh relop
    	 advance();
    	 numExpression();
    }
    public void allocExpression()
    {
    	if (lToken.getName() == Names.NEW)
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
    }
    public void numExpression()
    {
    	if (lToken.getAttribute() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
    		term();
    		numExpressionLinha();
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
    		advance();
    		unaryExpression();
    		termLinha();
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
    }
    public void factor()
    {
    	if (lToken.getName() == Names.INTEGER_LITERAL)
        {
    		advance();
        }
    	//TODO STRING LITERAL
    	else if (lToken.getName() == Names.STRING)
        {
    		advance();
        }
    	else if (lToken.getName() == Names.ID)
        {
    		advance();
    		lValue();
        }
    	else
    	{
    		match(Names.SEP,Names.PE);
    		expression();
    		match(Names.SEP,Names.PD);
    	}
    }
    public void argListOpt()
    {
    	if (lToken.getName() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
    		advance();
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
    }
    public void argListLinha()
    {
    	if (lToken.getName() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
    		advance();
    		argList();
    		match(Names.SEP,Names.VIR);
        }
    }

    
    private Token nextToken(int i){
       return tokenList.get(i);
    }
}
