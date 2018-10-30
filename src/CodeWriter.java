// Programer: Carlos Sanchez
// Class: CS220 MW 3:30pm - 5:20pm
// Lst Update: 10/16/2018
// Version 1.0


import java.io.PrintWriter;

public class CodeWriter
{

    // keep track of labelCounter variable here
    // private final static int STATIC_START = 16;
    //private final static int HEAP_START = 2048;
    //private final static int LOCAL_START = 0;
    //private final static int STACK_START = 256;

    // private File file;
    private PrintWriter printWriter;

    private int labelCounter;

    String fileName;


    /***
     * DESCRIPTION: Opens the output file/stream and gets ready to write into it.
     * PRECONDITION
     */
    CodeWriter(PrintWriter outputFile)
    {
        this.printWriter = outputFile;
        this.labelCounter = 0;
        // initializePointers();
    }

    /***
     * DESCRIPTION: Informs the code writer that the tranlation of a new VM file is started.
     * @param fileName The file to set
     */
    private void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /***
     * DESCRIPTION: Writes the assembly code that is the translation of the given arithmetic command.
     * @param command
     */
    public void writeArithmetic(String command)
    {

        printWriter.println("//" + command);
        switch (command)
        {
            case "add":
                writeAdd();
                break;

            case "sub":
                writeSub();
                break;

            case "neg":
                writeNeg();
                break;

            case "eq":
                writeEq();
                break;

            case "gt":
                writeGt();
                break;

            case "lt":
                writeLt();
                break;

            case "and":
                writeAnd();
                break;

            case "or":
                writeOr();
                break;

            case "not":
                writeNot();
                break;

            default:
                System.out.println("Unkown command");
                break;
        }

        printWriter.println();
    }

    /**
     * DESCRIPTION: Performs the first steps of a single pop operation by going to the top of stack.
     * PRECONDITION: none.
     * POSTCONDITION: Writes to file the assembly code for A-register to hold top of stack address.
     */
    private void singlePopOperation()
    {
        printWriter.println("@SP");         // go to Stack Pointer
        printWriter.println("A=M-1");       // go to Top of Stack
    }

    /**
     * DESCRIPTION: Performs the first steps of a double pop operation by going to SP, decrement SP address and go to
     * new SP address, Store contents of 1st Pop value into D, go to location of 2nd value to be popped.
     * PRECONDITION: none.
     * POSTCONDITION: Writes to file the assembly code that will go to address
     * of top of stack. Pop off value at top of stack, store it in D. Then
     * decrement the stack pointer.
     *
     */
    private void doublePopOperation()
    {
        printWriter.println("@SP");         // go to Stack Pointer
        printWriter.println("AM=M-1");      // decrement sp & go to new address
        printWriter.println("D=M");         // store value of first pop
        printWriter.println("A=A-1");       // go to current address - 1
    }

    /**
     * DESCRIPTION: Performs an Addition command by popping 2 values from stack, adding them, then pushing result into stack.
     * PRECONDITION: none
     * POSTCONDITION: Writes to file the assembly code for going to top of stack, pop first value and store in D, decrement
     * address by 1, pop second value and add with first value, finally push the result into stack.
     */
    private void writeAdd()
    {
        doublePopOperation();
        printWriter.println("M=D+M");       // update memory with D + M
    }


    /**
     * DESCRIPTION: Performs an Subtraction command by popping 2 values from stack, then subtracts the first popped value
     * from the second popped value, finally the result is pushed back into stack.
     * PRECONDITION: none
     * POSTCONDITION: Writes to file the assembly code for going to top of stack, pop first value and store in D, decrement
     * address by 1, pop second value , subtract first value from second value, finally push the result into stack.
     */
    private void writeSub()
    {
        doublePopOperation();
        printWriter.println("M=M-D");    // update memory with the difference of second Pop - firstPop
    }

    /**
     * DESCRIPTION:
     * PRECONDITION:
     * POSTCONDITION:
     */
    private void writeNeg()
    {
        singlePopOperation();
        printWriter.println("M=-M");      // Pop value, negate value, push result
    }

     /**
     * DESCRIPTION:
     * PRECONDITION:
     * POSTCONDITION:
     */
    private void writeEq()
    {
        doublePopOperation();

        printWriter.println("D=-D");
        printWriter.println("D=D+M");   // Calculate equality of D == M

        printWriter.println("M=-1");    // set top of stack to true

        printWriter.println("@skip" + labelCounter);
        printWriter.println("D;JEQ");

        printWriter.println("@SP");       // Go to SP
        printWriter.println("A=M-1");   // Go to top of Stack
        printWriter.println("M=0");     // set top of stack to false

        printWriter.println("(skip" + labelCounter + ")");

        labelCounter++;
    }

    /**
     * DESCRIPTION:
     * PRECONDITION:
     * POSTCONDITION:
     */
    private void writeGt()
    {
        doublePopOperation();

        printWriter.println("D=M-D"); // Calculate greater then of M > D

        finish_GT_LT();
    }

    /**
     * DESCRIPTION:
     * PRECONDITION:
     * POSTCONDITION:
     */
    private void writeLt()
    {
        doublePopOperation();

        printWriter.println("D=D-M");   // Calculate less then of D < M

        finish_GT_LT();
    }

    /**
     * DESCRIPTION:
     * PRECONDITION:
     * POSTCONDITION:
     */
    private void finish_GT_LT()
    {
        printWriter.println("M=-1");    // set top of stack to true

        printWriter.println("@skip" + labelCounter);    // goTo Skip
        printWriter.println("D;JGT");                   // jump to skip if D > M

        printWriter.println("@SP");     // Go to SP
        printWriter.println("A=M-1");   // Go to top of Stack
        printWriter.println("M=0");     // set top of stack to false

        printWriter.println("(skip" + labelCounter + ")");  // (skip.n) label

        labelCounter++;
    }

    /**
     * DESCRIPTION:
     * PRECONDITION:
     * POSTCONDITION:
     */
    private void writeAnd()
    {
        doublePopOperation();
        printWriter.println("M=D&M");
    }

    /**
     * DESCRIPTION:
     * PRECONDITION:
     * POSTCONDITION:
     */
    private void writeOr()
    {
        doublePopOperation();
        printWriter.println("M=D|M");

    }

    /**
     * DESCRIPTION:
     * PRECONDITION:
     * POSTCONDITION:
     */
    private void writeNot()
    {
        singlePopOperation();
        printWriter.println("M=!M");
    }

    /***
     * DESCRIPTION: Writes the assembly code that is the translation of the given command, where command is either
     * C_PUSH or C_POP
     * @param command
     * @param segment
     * @param index
     */
    public void writePushPop(String command, String segment, int index)
    {

        // Prints comments to file
        printWriter.printf("// %s %s %s\n", command, segment, index);

        //System.out.println("The command: " + command);
        //System.out.println("The segment " + segment);


        switch (command)
        {
            case "push":
                loadDfromStack(segment, index);
                writePush();
                break;

            case "pop":
                writePop(segment, index);
                break;

            default:
                System.out.println("Not a push or pop command");
                break;
        }

        printWriter.println();
    }


    /**
     * DESCRIPTION:
     * PRECONDITION:
     * POSTCONDITION:
     * @param segment
     * @param index
     */
    private void loadDfromStack(String segment, int index)
    {
        printWriter.println("@" + index);
        printWriter.println("D=A");

        writeAtSegement(segment, index);       // @segment EX. @THAT, @THIS, @LCL

        //
        if (!segment.equals("constant"))
        {
            // D = segment[i]
            printWriter.println("A=M+D");
            printWriter.println("D=M");
        }

    }

    /**
     * DESCRIPTION:
     * PRECONDITION:
     * POSTCONDITION:
     */
    private void writePush()
    {
        // increment sp
        printWriter.println("@SP");
        printWriter.println("M=M+1");

        // store value in D at top of stack
        printWriter.println("A=M-1");
        printWriter.println("M=D");
    }

    /**
     * DESCRIPTION:
     * PRECONDITION:
     * POSTCONDITION:
     * @param segment
     * @param index
     */
    private void writeAtSegement(String segment, int index)
    {
        switch (segment)
        {
            case "constant":
                // don't write anything
                break;

            case "local":
                printWriter.println("@LCL");
                break;

            case "argument":
                printWriter.println("@ARG");
                break;

            case "this":
                printWriter.println("@THIS");
                break;

            case "that":
                printWriter.println("@THAT");
                break;

            case "temp":
                //printWriter.println("@TEMP");
                printWriter.println("@THAT");
                printWriter.println("A=A+1");
                break;

            case "static":
                // Load D from static segment
                // printWriter.println("@")
                printWriter.println("@" + fileName + "." + index);

                break;

            default:
                System.out.println("unknown segment \"" + segment + "\" in loadDfromStack");
                break;
        }
    }


    /**
     * DESCRIPTION: Performs the steps for Popping a value from the stack to the desired segment[index].
     * PRECONDITION:
     * POSTCONDITION:
     * @param segment
     * @param index
     */
    private void writePop(String segment, int index)
    {

        printWriter.println("@" + index);   // @index
        printWriter.println("D=A");         // store index as value in D

        writeAtSegement(segment, index);    // @segment EX. @THAT, @THIS, @LCL
        printWriter.println("D=D+M");       // D =  segment[i]

        printWriter.println("@R13");        // Go to temp R13
        printWriter.println("M=D");         // Store destination address

        printWriter.println("@SP");         // go to Stack Pointer
        printWriter.println("AM=M-1");      // decrement SP, go to new Top of Stack address
        printWriter.println("D=M");         //  Pop M to D

        printWriter.println("@R13");        // Go to tempR13
        printWriter.println("A=M");         // go to segment[i]
        printWriter.println("M=D");         // Store the value of D into memory, segment[i] = D
    }


    /***
     * DESCRIPTION: Closes the output file.
     */
    public void close()
    {
        this.printWriter.close();
    }
//
//    private void initializePointers()
//    {
//        // Initialize Stack Pointer
//        printWriter.println("// Initializing SP");
//        printWriter.println("@" + STACK_START);
//        printWriter.println("D=A");
//        printWriter.println("@SP");
//        printWriter.println("M=D");
//
//        // Initialize Static Pointer
//
////        // Initialize Local Pointer
////        printWriter.println("@LCL");
////        printWriter.println("M=" + 0);
//
//        printWriter.println();
//    }
}
