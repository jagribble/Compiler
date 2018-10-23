import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by G on 24/02/2016.
 */
public class Main {

    public static void printTokens(ArrayList<Token> tokens){
        for(int x=0;x<tokens.size();x++){
            Token token = tokens.get(x);
            System.out.println(token.getType()+" : "+token.getVal());
        }
    }


    public static void main(String args[]){
        Lex lex = new Lex();
        System.out.println("YOU NEED TO PUT A '=' infront of the expression");
        System.out.println("Enter a test string");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        ArrayList<Token> tokens=lex.splitString(input);
        Parser parser= new Parser();
        parser.setTokens(tokens);

        if(!parser.start()){
            System.out.println("Grammar not matched");
        }
        else{
            ArrayList<Token> infix = parser.getInfix();
            CodeGen codeGen = new CodeGen();
            codeGen.setInfixTokens(infix);
            System.out.println("INPUT --> "+input);
            printTokens(tokens);
            System.out.println("INFIX -->");
            printTokens(infix);
            System.out.println("POSTFIX -->");
            ArrayList<Token> postFix= codeGen.postFix();
            printTokens(postFix);
            System.out.println("TAC -->");
            String code = codeGen.generateCode();
            System.out.println(code);
            System.out.println("OPTIMIZED -->");
            codeGen.optimiseCode(code);

        }
    }
}
