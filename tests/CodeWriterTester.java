import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


public class CodeWriterTester
{
    String fileName = "BasicTest";
    PrintWriter printWriter;

    CodeWriter codeWriter;
    Parser parser;

    @Before
    public void setup()
    {
      //  fileName = "EqualityTest.asm";
        try
        {
            parser = new Parser(fileName + ".vm");
        }

        catch(FileNotFoundException e)
        {
            System.out.println("Unable to create file: " + fileName);
            System.exit(0);
        }

        try
        {
            printWriter = new PrintWriter(new FileOutputStream(fileName + ".asm"));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Unable to create file: " + fileName + ".asm");
            System.exit(0);
        }

        codeWriter = new CodeWriter(printWriter);
    }

    @Test
    public void testWrite()
    {
        while(parser.hasMoreCommands())
        {
            parser.advance();

            if(parser.commandType() == CommandType.C_PUSH || parser.commandType() == CommandType.C_POP)
            {
                int index = Integer.parseInt(parser.arg2());
                codeWriter.writePushPop(parser.command(), parser.arg1(), index);
            }

            if(parser.commandType() == CommandType.C_ARITHMETIC)
            {
                codeWriter.writeArithmetic(parser.command());
            }
        }

        codeWriter.close();
    }
}
