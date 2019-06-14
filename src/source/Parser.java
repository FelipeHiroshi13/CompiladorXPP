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
            throw new SyntaxError("Identificador esperado: " + tokenCodigo + "\nMas foi o: " + lToken.getName());
    }
    
     private void match (Names tokenName, Names tokenAtributo){
        if(lToken.getAttribute()== tokenAtributo)
            advance();
        else
            throw new SyntaxError("Esperava o atributo " + tokenAtributo + "\nMas foi o " +  lToken.getAttribute());
    }
    
    public void program() throws SyntaxError
    {
        lToken = tokenList.get(0);
        System.out.println("--->" + lToken.getName());
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
            match(Names.ID);
            classDeclLinha();
        }
        else
            throw new SyntaxError("Classe mal definida");
        
    }
    
    public void classDeclLinha()
    {
        if (lToken.getAttribute() == Names.EXTENDS)
        {
            advance();
            match(Names.ID);
        }
        classBody();

    }
    
    public void classBody()
    {
        if(lToken.getAttribute()== Names.CHE){
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
    }
    public void varDeclList()
    {
        varDecl();
        if(lToken.isVariableType(lToken))
        {
            varDeclListinha();
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
        type();
        varDeclLinha();   
    }
    
    public void varDeclLinha()
    {
     
        if (lToken.getAttribute()== Names.COE) 
        {
            advance();
            match(Names.SEP, Names.COD);
        }
        
        match(Names.ID);
        varDeclOpt();
        if(lToken.getAttribute() != Names.PE)
            match(Names.SEP, Names.POINTV);
        else
            methodBody();
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
            System.out.println(lToken.getName());
            System.out.println(lToken.getAttribute());
            advance();
        }
        else
            throw new SyntaxError("Tipo de variavel nao existente");
       
            
    }
    public void constructDeclListOpt()
    {
      
        if (lToken.getAttribute() == Names.CONSTRUCTOR)
            constructDeclList();
    }
    public void constructDeclList() // Esse metodo e o de baixo sao realmente necessarios ?
    {
       
        constructDecl();
        if (lToken.getAttribute()== Names.CONSTRUCTOR)
        {
            constructDeclListLinha();
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
            System.out.println("=====>" + lToken.getAttribute());
            match(Names.SEP, Names.CHE);
            System.out.println("=====>" + lToken.getAttribute());
            statementsOpt();
            System.out.println("===>" + lToken.getName());
            match(Names.SEP, Names.CHD);
        }
     
    }
    public void paramListOpt()
    {
        if ((lToken.isVariableType(lToken)))
            paramList();
      

    }
    public void paramList()
    {
        if ((lToken.isVariableType(lToken)))
        {
            param();
            paramListLinha();
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
            System.out.println("oiiiiiiii");
            statements();
        }
        else
            ;
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
        System.out.println("77777" + lToken.getAttribute());
        if(lToken.isVariableType(lToken))
            varDeclList();  
        else if(lToken.getAttribute() == Names.ID)
            atribStat();
        else if(lToken.getAttribute() == Names.PRINT)
            printStat();
        else if(lToken.getAttribute() == Names.READ)
        	readStat();
        else if(lToken.getAttribute() == Names.RETURN)
        	returnStat();
        else if(lToken.getAttribute() == Names.SUPER)
        	superStat();
        else if(lToken.getAttribute() == Names.IF)
        	ifStat();
        else if(lToken.getAttribute() == Names.FOR)
                forStat();
        	
        else if(lToken.getAttribute() == Names.BREAK)
        {
        	advance();
        	match(Names.SEP, Names.POINTV);
        }
        else
        	match(Names.SEP, Names.POINTV);
        
   
    }
    public void atribStat()
    {
        if (lToken.getAttribute() == Names.ID)
        {
            lValue();
            match(Names.RELOP, Names.EQ);
            atribStatLinha();
        }
       

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
       
    }
    public void printStat()
    {
        if (lToken.getAttribute() == Names.PRINT)
        {
            advance();
            expression();
        }
       
    }
    public void readStat()
    {
        if (lToken.getAttribute() == Names.READ)
        {
            System.out.println("oioioi" + lToken.getName());
            advance();
            lValue();
        }
     
    }
    public void returnStat()
    {
        if (lToken.getAttribute() == Names.RETURN)
        {
            advance();
            expression();
        }
   
    }
    public void superStat()
    {
        if (lToken.getAttribute() == Names.SUPER)
        {
            match(Names.SEP, Names.PE);
            argListOpt();
            match(Names.SEP, Names.PD);
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
      
    }
    public void ifStatLinha()
    {
    	 if (lToken.getAttribute() == Names.ELSE)
         {
    		 match(Names.SEP,Names.CHE);
    		 statements();
    		 match(Names.SEP,Names.CHD);
         }
    }
    public void forStat()
    {
    	if (lToken.getAttribute() == Names.FOR)
        {
                System.out.println("ooioioi");
    		advance();
    		match(Names.SEP,Names.PE);
    		atribStatOpt();
    		match(Names.SEP,Names.POINTV);
    		expressionOpt();
                System.out.println("---->");
                System.out.println("--=>LEGAL");
    		match(Names.SEP,Names.POINTV);
    		atribStatOpt();
    		match(Names.SEP,Names.PD);
    		match(Names.SEP,Names.CHE);
    		statements();
    		match(Names.SEP,Names.CHD);
                System.out.println("LEGAL");
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
    	if (lToken.getName() == Names.ID)
        {
                match(Names.ID);
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
                System.out.println("olaaaa");
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
