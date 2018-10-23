import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by G on 29/02/2016.
 */
public class CodeGen {
    int pointer = 0;
    ArrayList<Token> infix = new ArrayList<Token>();
    ArrayList<Token> operatorArray = new ArrayList<Token>();
    ArrayList<Token> postFix = new ArrayList<Token>();
    Stack<Token> operatorStack = new Stack<Token>();

    public void setInfixTokens(ArrayList<Token> infixTokens){
        this.infix = infixTokens;
    }

    public ArrayList<Token> sortOperators(ArrayList<Token> addArray,int start){
        ArrayList<Token> correctOperatorArray = new ArrayList<Token>();
        int[] operatorOrder = new int[addArray.size()-1-start];
        for(int x=start+1;x<addArray.size()-1;x++){
            switch(addArray.get(x).getVal()){
                case "/":
                    operatorOrder[x-(start+1)]=0;
                    break;
                case "*":
                    operatorOrder[x-(start+1)]=1;
                    break;
                case "+":
                    operatorOrder[x-(start+1)]=2;
                    break;
                case "-":
                    operatorOrder[x-(start+1)]=3;
                    break;
            }
        }
        for (int x = 0; x < ( operatorOrder.length - 1 ); x++) {
            for (int y = 0; y < operatorOrder.length - x - 1; y++) {
                if (operatorOrder[y] < operatorOrder[y+1]) /* For descending order use < */
                {
                    Token swap = addArray.get(y);
                    int swapInt = operatorOrder[y];
                    addArray.set(y,addArray.get(y+1));
                    operatorOrder[y]   = operatorOrder[y+1];
                    addArray.set(y+1,swap);
                    operatorOrder[y+1] = swapInt;
                }
            }
        }
        return addArray;
    }

    public boolean checkOperator(Token token1,Token token2){
        int token1Val = 0;
        switch (token1.getVal()){
            case "/":
                token1Val=4;
                break;
            case "*":
                token1Val=3;
                break;
            case "+":
                token1Val=2;
                break;
            case "-":
                token1Val=1;
                break;
            case "(":
                token1Val=0;
                break;
            case "=":
                token1Val = -1;
                break;
        }
        int token2Val = 0;
        switch (token2.getVal()){
            case "/":
                token2Val=4;
                break;
            case "*":
                token2Val=3;
                break;
            case "+":
                token2Val=2;
                break;
            case "-":
                token2Val=1;
                break;
            case "=":
                token2Val = -1;
                break;

        }
        if(token1Val>=token2Val){
            return true;
        }
        else{
            if(token1.getType()==TokenType.FPRAM){
                return true;
            }
            else {
                return false;
            }
            }
    }

    public void loopStack(Token token1){
        if(!operatorStack.isEmpty()){
            if(checkOperator(token1,operatorStack.peek())) {
                operatorStack.push(token1);
            }
                else{
                    if(operatorStack.peek().getType()==TokenType.FPRAM){
                        operatorStack.push(token1);
                    }
                else{
                        postFix.add(operatorStack.pop());
                        loopStack(token1);
                    }
                }
        }
        else{
            operatorStack.push(token1);
        }
    }

    public void addTillFpram(){
        if(!operatorStack.isEmpty()&&operatorStack.peek().getType()!=TokenType.FPRAM){
            postFix.add(operatorStack.pop());
            addTillFpram();
        }
        else if(!operatorStack.isEmpty()&&operatorStack.peek().getType()==TokenType.FPRAM){
            operatorStack.pop();
        }

    }

    public ArrayList<Token> postFix(){

        for(int x=0;x<infix.size();x++){
            Token token = infix.get(x);
            if(token.getType()==TokenType.INTEGER){
                postFix.add(token);
            }
            else{
                if(operatorStack.isEmpty()){
                    operatorStack.push(token);
                }
                else{
                    if(token.getType()!=TokenType.LPRAM){
                        if(operatorStack.peek().getType()==TokenType.FPRAM){
                            operatorStack.push(token);
                        }
                        else{
                            loopStack(token);
                        }
                    }
                    else{
                        addTillFpram();
                    }
                }
            }
        }
        addTillFpram();
        return postFix;
    }



    public String generateCode(){

        ArrayList<Token> operators = new ArrayList<Token>();
        String code="";
        int varibleNo = 0;
        Stack<String> stack = new Stack<>();
        for(int x=0;x<postFix.size();x++){
        if(postFix.get(x).getType()!=TokenType.INTEGER){
            Token operator = postFix.get(x);
            switch (operator.getVal()){
                //value 2 is first as it is a stack so the second value should be the first.
                case "+":
                    if (x == 0) {
                        float value1 = Float.parseFloat(stack.pop());
                        float value2 = Float.parseFloat(stack.pop());
                        code += "t"+varibleNo+"="+value2+"+"+value1+";\n";
                    }
                    else{
                        String value1 = stack.pop();

                        String value2 = stack.pop();
                        code += "t"+varibleNo+"="+value2+"+"+value1+";\n";


                    }
                   stack.push("t"+varibleNo);

                    break;
                case "-":
                    if (x == 0) {
                        float value1 = Float.parseFloat(stack.pop());
                        float value2 = Float.parseFloat(stack.pop());
                        code += "t"+varibleNo+"="+value2+"-"+value1+";\n";
                    }
                    else{
                        String value1 = stack.pop();
                        String value2 = stack.pop();
                        code += "t"+varibleNo+"="+value2+"-"+value1+";\n";
                    }
                    stack.push("t"+varibleNo);
                    break;
                case "*":
                    if (x == 0) {
                        float value1 = Float.parseFloat(stack.pop());
                        float value2 = Float.parseFloat(stack.pop());
                        code += "t"+varibleNo+"="+value2+"*"+value1+";\n";
                    }
                    else{
                        String value1 = stack.pop();
                        String value2 = stack.pop();
                        code += "t"+varibleNo+"="+value2+"*"+value1+";\n";
                    }
                    stack.push("t"+varibleNo);
                    break;
                case "/":
                    if (x == 0) {
                        float value1 = Float.parseFloat(stack.pop());
                        float value2 = Float.parseFloat(stack.pop());
                        code += "t"+varibleNo+"="+value2+"/"+value1+";\n";
                    }
                    else{
                        String value1 = stack.pop();
                        String value2 = stack.pop();
                        code += "t"+varibleNo+"="+value2+"/"+value1+";\n";

                    }
                    stack.push("t"+varibleNo);
                    break;
                case "=":
                    String result =  stack.pop();
                    code+= "t"+varibleNo+ " = "+result+";\n";
                    break;
            }
            varibleNo++;
        }
            else{
            stack.push(postFix.get(x).getVal());
        }


    } return code;
    }

    /**
     * gets the number of values there is in that line.
     * If the is one value it will return 0 as it can not find an operator
     * @return values
     */
    public int getValueNo(String s){
        int values=0;
        for(int x=0;x<s.length();x++){
        //!Character.isWhitespace(s.charAt(x))
            if(s.charAt(x)=='+'||s.charAt(x)=='-'||s.charAt(x)=='*'||s.charAt(x)=='/'){
                values++;
            }
        }
        return values;
    }

    public String optimiseCode(String code){
        String optimisedCode = "";
        String lines[] = code.split("\n");
        for(int x=0;x<lines.length-1;x+=1){
            String codeToOptimise = "";
            for(int y=x;y<x+4&&y<lines.length;y++){
                if(lines[y]!=null){
                    codeToOptimise +=lines[y];
                }
            }
            String[] newLines = optimise(codeToOptimise);
            for(int y=x;y-x<newLines.length;y++){
                if(lines[y]!=null){
                        if(newLines[y-x]==null){
                            lines[y] = newLines[y-x];
                        }
                    else{
                            lines[y] = newLines[y-x]+";";
                        }



                }
            }
        }
        for(int x=0;x<lines.length;x++){
            if(lines[x]!=null){
                if(checkForDivZero(lines[x])){
                    String[] split = lines[x].split("=");
                    optimisedCode+="float "+split[0]+"=(float)"+split[1]+"\n";
                }
                else{
                    optimisedCode+="\t\tfloat "+lines[x]+"\n";
                }

            }

        }
        optimisedCode+="\t\tSystem.out.println(\"RESULT -----> \"+"+getLastVar(lines[lines.length-1])+");\n";
        optimisedCode = "public class Output {\n" +
                "\n" +
                "    public static void main(){\n" +
                optimisedCode+
                " System.out.println(\"=============================\");\n"+
                " System.out.println(\"Compiled with Jules' compiler\");\n"+"  }\n" +
                "\n" +
                "}\n";
        System.out.println(optimisedCode);
        try {
            File root = new File("Outputs");
            File sourceFile = new File(root, "Output.java");
            sourceFile.getParentFile().mkdirs();
            FileWriter fwriter = new FileWriter(sourceFile);
            fwriter.write(optimisedCode);
            fwriter.close();

            File output = new File(root,"Output.java");

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            compiler.run(null, null, null, output.getPath());

            // Load and instantiate compiled class.
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
            Class<?> cls = Class.forName("Output", true, classLoader); // Should print "hello".
            Object instance = cls.newInstance();
            instance.getClass().getMethods()[0].invoke(instance,null);
            // Should print "world".

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return optimisedCode;
    }

    public boolean checkForDivZero(String line){
        //if there is a division sysmbol (/) then it gets the value to the right. if it is 0 return true
        String[] division = line.split("/");
        if(division.length==1){
            return false;
        }
        else{
            if(division[1].replaceAll("\\s","").equals("0;")||division[1].replaceAll("\\s","").equals("0.0;")){
                return true;
            }
            else{
                return false;
            }
        }

    }
    public String getLastVar(String line){
        return line.split("=")[0].replaceAll("\\s","");
    }

    public String[] optimise(String code){
        String optimisedCode = "";
        String[] lines = code.split(";");
        for (int x=0;x<lines.length;x++){
            if(lines[x]!=null) {
                boolean changed = false;
                String varibleName = lines[x].split("=")[0];
                String value = lines[x].split("=")[1];
                if (getValueNo(value) == 0 && !isInt(value)) {
                    //if the line has only one value and it is a TAC varible then get the value of the TAC varible then delete that line
                    for (int y = 0; y < lines.length; y++) {
                        if (y != x) {
                            String svaribleName = lines[y].split("=")[0];
                            svaribleName.replaceAll("\\s","");
                            if (svaribleName.contains(value.replaceAll("\\s",""))) {
                                changed = true;
                                value = lines[y].split("=")[1];
                                lines[y] = null;
                                break;
                            }
                        }
                    }
                }
                else if(getValueNo(value)==0&&isInt(value)){
                    //if there is only one value and it is an integer then find that TAC varible in other lines and replace the TAC varible with the value
                    for(int y=0;y<lines.length;y++){
                        if(x!=y){
                            String svalue = lines[y].split("=")[1];
                            if(svalue.contains(varibleName)){
                                svalue.replace(varibleName,value);
                            }

                        }
                    }
                    lines[x] = null; //value put in other TAC statment or value not used so makes line null (to be deleted)
                }
                if (changed) {
                    lines[x] = varibleName + "=" + value+"";
                }

            }
        }

        for(int x=0;x<lines.length;x++){
            if(lines[x]!=null){
                optimisedCode+=lines[x]+";\n";
            }

        }
        return lines;
    }

    public boolean isInt(String s){
        try{
            int i = Integer.parseInt(s);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }

}
