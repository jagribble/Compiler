import java.util.ArrayList;

/**
 * Created by G on 24/02/2016.
 */
public class Lex {
    ArrayList<Token> tokens = new ArrayList<Token>();

    public ArrayList<Token> splitString(String s){
        s = s.replaceAll("\\s","");
        int x=0;
        String value = "";
        int type = 0;
        boolean newToken = true;
        for (;x<s.length();){
            if(!Character.isWhitespace(s.charAt(x)) && (type == Character.getType(s.charAt(x))||newToken)){
                if(((type==22&& Character.getType(s.charAt(x))==22)||(type==21&& Character.getType(s.charAt(x))==21))){
                    if(!value.equals("")){
                        lexicalAnalyser(value);
                        newToken = true;
                        type=0;

                        value = "";
                    }
                }
                else{

                if(Character.getType(s.charAt(x))==20){ //if there is a '-' check if it a minus or a negative sign
                    //20 is the value for '-'
                    if(type!=9){
                            value +=s.charAt((x));
                            s = s.substring(1);
                            type=9;//9 is the type value for an integer
                            continue;
                    }
                }
                newToken = false;
                type = Character.getType(s.charAt(x));
                value += s.charAt(x);
                s = s.substring(1);
                }
            }
            else{
                if(!value.equals("")){
                    lexicalAnalyser(value);
                    newToken = true;
                    value = "";
                }
            }
        }
        if (!value.equals("")){
            lexicalAnalyser(value);
        }
        return tokens;
    }

    public String getTokenValue(int i,String s){
        int start = i;
        String value = "";
        for (;i<s.length();i++){
            if(Character.isDigit(s.charAt(i))||Character.getType(s.charAt(i))==20){
                value += s.charAt(i);
            }
            else{
                if(Character.isLetter(s.charAt(i))){
                    System.out.println("YOU CAN NOT ENTER LETTERS INTO THIS COMPILER");
                }
                System.exit(0);
                break;
            }
        }
        return value;
    }

    public boolean checkIfValid(String s){
        if (s.length()>=2){
            return false;
        }
        else{
            return true;
        }
    }

    public String getPreviousValues(){
        String values = "";
        for(int x=0;x<tokens.size();x++){
            values+=tokens.get(x).getVal();
        }
        return values;
    }

    public ArrayList<Token> lexicalAnalyser(String s) {
            switch (s.toCharArray()[0]) {
                case '+':
                    if(checkIfValid(s)) {
                        tokens.add(new Token("+", TokenType.PLUS));
                    }
                    else{
                        System.out.println(s+" can not go here "+getPreviousValues());
                        System.exit(0);
                    }
                    break;
                case '-':
                    if(checkIfValid(s)) {
                        tokens.add(new Token("-", TokenType.MINUS));
                    }
                    else{
                        String value = getTokenValue(0,s);
                        tokens.add(new Token(value,TokenType.INTEGER));
                    }
                    break;
                case '/':
                    if(checkIfValid(s)) {
                        tokens.add(new Token("/", TokenType.DIVIDE));
                    }
                    else{
                        System.out.println(s+" can not go here "+getPreviousValues());
                        System.exit(0);
                    }
                    break;
                case '*':
                    if(checkIfValid(s)) {
                        tokens.add(new Token("*", TokenType.MULTIPLY));
                    }
                    else{
                        System.out.println(s+" can not go here "+getPreviousValues());
                        System.exit(0);
                    }
                    break;
                case '=':
                    if(checkIfValid(s)) {
                        tokens.add(new Token("=", TokenType.EQUALS));
                    }
                    else{
                        System.out.println(s+" can not go here "+getPreviousValues());
                        System.exit(0);
                    }
                    break;
                case '(':
                    if(checkIfValid(s)) {
                        tokens.add(new Token("(", TokenType.FPRAM));
                    }
                    else{
                        System.out.println(s+" can not go here "+getPreviousValues());
                        System.exit(0);
                    }
                    break;
                case ')':
                    if(checkIfValid(s)) {
                        tokens.add(new Token(")", TokenType.LPRAM));
                    }
                    else{
                        System.out.println(s+" can not go here "+getPreviousValues());
                        System.exit(0);
                    }
                    break;
                default:
                    //if it is not a operator it must be an INTEGER as it is just a calculator
                    String value = getTokenValue(0,s);
                    tokens.add(new Token(value,TokenType.INTEGER));
                    break;
            }
        return tokens;
    }
}
