// Programer: Carlos Sanchez
// Class: CS220 MW 3:30pm - 5:20pm
// Lst Update: 10/29/2018
// Version 1.0


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class VirtualMachinePart1
{
    // ALGORITHM:
    // get input file name
    // create output file name and stream

    // create symbol table
    // do first pass to build symbol table (no output yet!)
    // do second pass to output translated ASM to HACK code

    // print out "done" message to user
    // close output file stream
    public static void main(String[] args)
    {
        String inputFileName, outputFileName;
        PrintWriter outputFile = null; //keep compiler happy

        //SymbolTable symbolTable;
        int romAddress, ramAddress;

        //get input file name from command line or console input
        if(args.length == 1) {
            System.out.println("command line arg = " + args[0]);
            inputFileName = args[0];
        }
        else
        {
            Scanner keyboard = new Scanner(System.in);

            System.out.println("Please enter assembly file name you would like to assemble.");
            System.out.println("Don't forget the .vm extension: ");
            inputFileName = keyboard.nextLine();

            keyboard.close();
        }

        outputFileName = inputFileName.substring(0,inputFileName.lastIndexOf('.')) + ".asm";

        try {
            outputFile = new PrintWriter(new FileOutputStream(outputFileName));
        } catch (FileNotFoundException ex) {
            System.err.println("Could not open output file " + outputFileName);
            System.err.println("Run program again, make sure you have write permissions, etc.");
            System.exit(0);
        }

        // TODO: finish driver as algorithm describes
        writeASM(inputFileName, outputFile, outputFileName);
    }

    /***
     * ALGORITHM:
     * Create new parser with file to parse.
     * While parser has more commands.
     *  advance parser
     *  if parser command is C_Push or C_POP
     *      write pushPop command to output file
     *  if parser command is C_Arithmetic
     *      write Arithmetic command to output file
     *
     *  close file and update user.
     *
     * @param fileName
     * @param outputFile
     * @param outputFileName
     */
    private static void writeASM(String fileName, PrintWriter outputFile, String outputFileName)
    {
        Parser parser = null;
        CodeWriter codeWriter;

        try
        {
            parser = new Parser(fileName);

        }
        catch(FileNotFoundException e)
        {
            System.out.println("Unable to open file" + fileName);
            System.err.println("Run program again, check file name.");
            System.exit(0);
        }

        codeWriter = new CodeWriter(outputFile);

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

        System.out.println("Process complete, file was saved as " +  outputFileName);
    }
}
