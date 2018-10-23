import java.util.ArrayList;

/**
 * Created by G on 29/02/2016.
 *
 * grammer rules
 * s -> =E
 * E -> T| T+E |T-E|T*E|T/E
 * T -> (E)|Integer+T|Integer-T|Integer*T|Integer/T|Integer
 */
public class Parser {
    String error ="";
    boolean t1Running = false;
    ArrayList<Token> tokens = new ArrayList<Token>();
    ArrayList<Token> infix = new ArrayList<Token>();
    int nextPointer = 0;



    public void setTokens(ArrayList<Token> tokens){
        this.tokens = tokens;
    }

    public ArrayList<Token> getInfix(){
        return infix;
    }

    public boolean term(TokenType tokenType){
        if(nextPointer>=tokens.size()){
            return false;
        }
        if ( tokens.get(nextPointer).getType()==tokenType ){
            if(!infix.contains(tokens.get(nextPointer))){
                infix.add(tokens.get(nextPointer));
            }
            nextPointer++;
            return true;
        }
        else{
            nextPointer++;
            return false;
        }
    }


    public boolean start(){
        return term(TokenType.EQUALS) && Expression();
    }

    public boolean Expression(){
        int savedPointer = nextPointer;
        boolean value = E1();
        if(!value ){
            nextPointer = savedPointer;
            value = E2();
            if(!value){
                nextPointer = savedPointer;
                value = E3();
                if(!value){
                    nextPointer = savedPointer;
                    value = E4();
                    if(!value) {
                        nextPointer = savedPointer;
                        value = E5();

                    }
                }
            }
        }
        if(!value){
            error += "Error found at "+ tokens.get(savedPointer-1);
        }
        return value;
    }


    public boolean E5(){
        return T();
    }

    public boolean E2(){
        return T() && term(TokenType.PLUS) && Expression();
    }

    public boolean E3(){
        return T() && term(TokenType.MINUS) && Expression();
    }

    public boolean E4(){
        return T() && term(TokenType.MULTIPLY) && Expression();
    }

    public boolean E1(){
        return T() && term(TokenType.DIVIDE) && Expression();
    }



    public boolean T(){
        int savedPointer = nextPointer;
        boolean value = false;

        value = T1();
        if (!value){
            nextPointer = savedPointer;
            value = T2();
            if(!value){
                nextPointer = savedPointer;
                value = T3();
                if(!value){
                    nextPointer = savedPointer;
                    value = T4();
                    if(!value){
                        nextPointer = savedPointer;
                        value = T5();
                        if(!value){
                            nextPointer = savedPointer;
                            value = T6();
                            t1Running = false;
                        }
                    }
                }
            }
        }
        if(!value){
            error += "Error found at "+ tokens.get(savedPointer-1);
        }
        return value;
    }

    public boolean T6(){
        return term(TokenType.INTEGER);
    }

    public boolean T2(){
        return term(TokenType.INTEGER) && term(TokenType.PLUS) && T();
    }

    public boolean T3(){
        return term(TokenType.INTEGER) && term(TokenType.MINUS) &&T();
    }

    public boolean T4(){
        return term(TokenType.INTEGER) && term(TokenType.MULTIPLY) &&T();
    }

    public boolean T5(){
        return term(TokenType.INTEGER) && term(TokenType.DIVIDE) &&T();
    }

    public boolean T1(){
        t1Running = true;
        return term(TokenType.FPRAM) && Expression() && term(TokenType.LPRAM);
    }

}
