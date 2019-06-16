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
            throw new SyntaxError("Identificador esperado: " + AnalisadorSintatico.debug.get(tokenCodigo) + "\nNo lugar de: " + AnalisadorSintatico.debug.get(lToken.getName()));
    }
    
     private void match (Names tokenName, Names tokenAtributo){
        if(lToken.getAttribute()== tokenAtributo)
            advance();
        else
            throw new SyntaxError("Esperava o atributo " + AnalisadorSintatico.debug.get(tokenAtributo) + "\nNo lugar de: " +  AnalisadorSintatico.debug.get(lToken.getAttribute()));
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
            match(Names.ID, Names.ID);
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
            match(Names.ID, Names.ID);
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
        
        match(Names.ID, Names.ID);
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
        match(Names.ID, Names.ID);
    }
    
    public void statementsOpt()
    {
        if (lToken.IsStatementType(lToken))
        {
            System.out.println("oiiiiiiii");
            statements();
        }
        else
            ;
    }
    public void statements()
    {
     
        if (lToken.IsStatementType(lToken))
        {
            statement();
            statementsLinha();
        }
    }
    
    public void statementsLinha()
    {
        if (lToken.IsStatementType(lToken))
        {
            statement();
        }
    }
   
     
    public void statement()    
    {
        System.out.println("77777" + lToken.getAttribute());
        if(lToken.isVariableType(lToken))
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
            System.out.println("PRINT CORNO" + lToken.getAttribute());
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
        	ifStat();
        else if(lToken.getAttribute() == Names.FOR)
                forStat();
        	
        else if(lToken.getAttribute() == Names.BREAK)
        {
        	advance();
        	match(Names.SEP, Names.POINTV);
        }
        
         else if(lToken.getAttribute() == Names.POINTV)
        	match(Names.SEP, Names.POINTV);
         else 
                throw new SyntaxError("Statement mal formado");
   
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
        if (lToken.getAttribute() == Names.PLUS || lToken.getAttribute() == Names.MINUS)
        {
            expression();
        }
        else if (lToken.getName()== Names.NEW || lToken.isVariableType(lToken))
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
            advance();
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
                System.out.println("oioioi 1 " + lToken.getAttribute());
    		match(Names.SEP,Names.PE);
                System.out.println("oioioi 2 " + lToken.getAttribute());
    		atribStatOpt();
                System.out.println("oioioi 3 " + lToken.getAttribute());
                
//                advance();
    		
                match(Names.SEP,Names.POINTV);
                System.out.println("oioioi 4 " + lToken.getAttribute());
    		expressionOpt();
                System.out.println("oioioi 5 " + lToken.getAttribute());
    		match(Names.SEP,Names.POINTV);
                System.out.println("oioioi 6 " + lToken.getAttribute());
    		atribStatOpt();
                System.out.println("oioioi 7 " + lToken.getAttribute());
    		match(Names.SEP,Names.PD);
    		match(Names.SEP,Names.CHE);
                System.out.println("oioioi 8 " + lToken.getAttribute());
    		statements();
                System.out.println("oioioi 9 " + lToken.getAttribute());
    		match(Names.SEP,Names.CHD);
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
                System.out.println("olaaaa");
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
    }
     public void expressionLinha()
    {
    	 //TODO:Verificar se eh relop
        if(lToken.getName() == Names.RELOP)
        {
    	 advance();
    	 numExpression();
        }
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
                
//    		advance();
//                System.out.println(lToken.getName());
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
    	else if (lToken.getAttribute()== Names.ID)
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
        }else{
            throw new SyntaxError("Argumento Inválido");
        }
    }
    public void argListLinha()
    {
        System.out.println("perra" + lToken.getAttribute());
    	if (lToken.getAttribute() == Names.VIR)
        {
                System.out.println("perrwqwqwa");
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
