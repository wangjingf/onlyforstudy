package compiler.expr;

import compiler.expr.ast.ASTNode;
import compiler.expr.ast.Program;
import io.study.helper.IoHelper;
import junit.framework.TestCase;

import java.io.*;
import java.net.URL;
import java.util.Map;

public class ExprParserTest extends TestCase {
    String  getFileContent(String fileName){
        String path = "D:\\wjf\\workspace\\onlyforstudy\\src\\test\\java\\compiler\\expr\\pascal\\"+fileName;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(path)));
            String s = null;
            while ((s=reader.readLine())!=null){
                sb.append(s+"\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoHelper.safeClose(reader);
        }
        return sb.toString();
    }
    Program parseProgram(String fileName){
        String content = getFileContent("part1.pas");
        System.out.println(content);
        ExprParser parser = new ExprParser(content);
        return (Program) parser.program();
    }
    public void testProgramParse(){
        ASTNode astNode = parseProgram("part1.pas");
        System.out.println(astNode);
    }
    public void testInterpreter(){
        Program program = parseProgram("part1.pas");
        Interpreter interpreter = new Interpreter();
        interpreter.visit(program);
        System.out.println(interpreter.getScope().getAllVars());
    }
}
