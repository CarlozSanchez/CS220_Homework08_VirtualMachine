
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;

public class VMTester
{
    Parser parser;
    String fileName;

    @Before
    public void setup()
    {
        fileName = "TestFile.vm";

        try
        {
            parser = new Parser(fileName);
        }
        catch(IOException e)
        {
            System.out.println("Unable to Open " + fileName);
        }

    }

    @Test
    public void testEnumString()
    {
        String str = CommandType.C_ARITHMETIC.toString();

        System.out.println("The word: " + str);
    }

    @Test
    public void test1()
    {
        System.out.println(parser);

        parser.advance();
        System.out.println(parser);

        parser.advance();
        System.out.println(parser);

        parser.advance();
        System.out.println(parser);

        parser.advance();
        System.out.println(parser);

        parser.advance();
        System.out.println(parser);
    }

    @Test
    public void test2()
    {
        while(parser.hasMoreCommands())
        {
            parser.advance();
            System.out.println(parser);
        }
    }

    @Test
    public void testEnum()
    {
        String command = "add";

//        for(ArithmeticBooleanCommands abc : ArithmeticBooleanCommands.values())
//        {
//
//           // System.out.println(abc);
//
//            String str = abc.toString();
//
//            if(str.equals(command))
//            {
//                System.out.println(abc + " == " + command);
//            }
//            else
//            {
//                System.out.println(abc + "!= " + command);
//            }
//        }
    }
}
