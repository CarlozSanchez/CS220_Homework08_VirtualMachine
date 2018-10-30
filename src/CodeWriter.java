// Programer: Carlos Sanchez
// Class: CS220 MW 3:30pm - 5:20pm
// Lst Update: 10/16/2018
// Version 1.0


import java.io.PrintWriter;


/**
 * CodeWriter.java - This writes the appropriate Arithmetic or Push/Push operation
 * to destination file.
 */
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
     * DESCRIPTION: Informs the code writer that the translation of a new VM file is started.
     * @param fileName The file to set
     */
    private void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /***
     * DESCRIPTION: Writes the assembly code that is the translation of the given arithmetic command.
     * PRECONDITION: argument should contain a valid command: "add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not
     * POSTCONDITION: the appropriate method is executed for given command.
     * @param command the command to execute.
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
     * POSTCONDITION: The file will be appended with the commands for:  go to SP, decrement SP address then go to new
     * address, Pop value and Store in D, decrement current address, update M with sum of D + M
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
     * POSTCONDITION: The file will be appended with the commands for: go to SP, decrement SP address then go to new
     * address, Pop value and Store in D, decrement current address, update M with difference of M - D
     */
    private void writeSub()
    {
        doublePopOperation();
        printWriter.println("M=M-D");    // update memory with the difference of second Pop - firstPop
    }

    /**
     * DESCRIPTION: Performs a Negation command by popping a single value from stack, then performs a bit wise negation
     * on the value, finally the result is pushed into stack.
     * PRECONDITION: none.
     * POSTCONDITION: The file will be appended with the commands for: go to SP, go to SP address - 1, update M
     * with bit-wise negation of M
     */
    private void writeNeg()
    {
        singlePopOperation();
        printWriter.println("M=-M");      // Pop value, negate value, push result
    }

     /**
     * DESCRIPTION: Performs an Equality operation by popping two values from stack, comparing both value then push the
      * result(true,false) back into stack.
     * PRECONDITION: none.
     * POSTCONDITION: The file will be appended with the commands for: go to SP, decrement SP address then go to new
      * address, Pop value and Store in D, decrement current address, Conver D to negative number, update D with sum of
      * D+M, Set M to TRUE, go to SKIP address, jump if D == 0, go to SP, go to SP address -1, Set M to FALSE. ("skip)"
      * is written.
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
     * DESCRIPTION: Performs a Greater then operation by popping two values from stack, comparing first value bigger then
     * second value, then storing the result(true,false) in stack.
     * PRECONDITION: none.
     * POSTCONDITION: The file will be appended with the commands for: go to SP, decrement SP address then go to new
     * address, Pop value and Store in D, decrement current address, update D with difference of M - D, Set M to TRUE,
     * go to SKIP address, jump if D > 0, go to SP, go to SP address -1, Set M to FALSE. "(SKIP)" is written.
     */
    private void writeGt()
    {
        doublePopOperation();

        printWriter.println("D=M-D"); // Calculate greater then of M > D

        finish_GT_LT();
    }

    /**
     * DESCRIPTION: Performs a Less then operation by popping two values from stack, comparing second  value bigger then
     * first value, then storing the result(true,false) in stack.
     * PRECONDITION:
     * POSTCONDITION: The file will be appended with the commands for: go to SP, decrement SP address then go to new
     * address, Pop value and Store in D, decrement current address, update D with difference of D - M, Set M to TRUE,
     * go to SKIP address, jump if D < 0, go to SP, go to SP address - 1, Set M to FALSE. "(SKIP)" is written.
     */
    private void writeLt()
    {
        doublePopOperation();

        printWriter.println("D=D-M");   // Calculate less then of D < M

        finish_GT_LT();
    }

    /**
     * DESCRIPTION: Performs the rest of a less then or greater then operation by pushing into stack the result(true,false)
     * of a previous LT or GT comparison.
     * PRECONDITION: writeLT() or writeGt() should invoke this method to finish the last steps of it operation.
     * POSTCONDITION: The file will be appended with the commands for: Set M to TRUE, go to SKIP address, jump if D < 0,
     * go to SP, go to SP address - 1, Set M to FALSE. "(SKIP)" is written.
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
     * DESCRIPTION: Performs an AND operation by popping two values from stack, performing an bit wise AND operation on
     * both values, then pushing the results back into stack.
     * PRECONDITION: none.
     * POSTCONDITION: The file will be appended with the commands for:  go to SP, decrement SP address then go to new
     * address, Pop value and Store in D, decrement current address, update M with result of D & M
     */
    private void writeAnd()
    {
        doublePopOperation();
        printWriter.println("M=D&M");
    }

    /**
     * DESCRIPTION: Performs an OR operation by popping two values from stack, performing a bit wise OR operation on
     * both values, then pushing the result back into stack.
     * PRECONDITION: none.
     * POSTCONDITION: The file will be appended with the commands for:  go to SP, decrement SP address then go to new
     * address, Pop value and Store in D, decrement current address, update M with result of D | M
     */
    private void writeOr()
    {
        doublePopOperation();
        printWriter.println("M=D|M");

    }

    /**
     * DESCRIPTION: Performs a NOT operation by popping two values from stack, performing a bit wise NOT operation on
     * both values, then pushing the result back into stack.
     * PRECONDITION: none.
     * POSTCONDITION: The file will be appended with the commands for: go to SP, go to SP address - 1, update M
     * with bit-wise not of M
     */
    private void writeNot()
    {
        singlePopOperation();
        printWriter.println("M=!M");
    }

    /***
     * DESCRIPTION: Writes the assembly code that is the translation of the given command, where command is either
     * C_PUSH or C_POP
     * PRECONDITION: commands, segment, index must be valid arguments.
     * @param command The command to operate, either "push" or "pop" command.
     * @param segment The segment of memory to operate on, "static", 'local", "constant"
     * @param index The index of the segment to push from, or pop too.
     */
    public void writePushPop(String command, String segment, int index)
    {

        // Prints the current operation as comment to file.
        printWriter.printf("// %s %s %s\n", command, segment, index);

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
     * DESCRIPTION: Writes to file The Operation to Pop value from stack and store in D.
     * PRECONDITION: none.
     * POSTCONDITION: The file will be appended with the commands for: Go to index, Store literal value in D,
     * @param segment
     * @param index
     */
    private void loadDfromStack(String segment, int index)
    {
        switch (segment)
        {
            case "constant":
                loadDwithAddress(index);
                break;

            case "local":
                loadDwithAddress(index);
                printWriter.println("@LCL");
                goToSegmentIndex_LoadDwithMemory();
                break;

            case "argument":
                loadDwithAddress(index);
                printWriter.println("@ARG");
                goToSegmentIndex_LoadDwithMemory();
                break;

            case "this":
                loadDwithAddress(index);
                printWriter.println("@THIS");
                goToSegmentIndex_LoadDwithMemory();
                break;

            case "that":
                loadDwithAddress(index);
                printWriter.println("@THAT");
                goToSegmentIndex_LoadDwithMemory();
                break;

            case "temp":
                loadDwithAddress(index);
                printWriter.println("@THAT");
                printWriter.println("A=A+1");
                goToSegmentIndex_LoadDwithMemory();
                break;

            case "static":
                printWriter.println("@" + fileName + "." + index);
                printWriter.println("D=M");
                break;

            default:
                System.out.println("unknown segment \"" + segment + "\" in loadDfromStack");
                break;
        }
    }

    /**
     * DESCRIPTION: helper method used by loadDfromStack().
     * @param address
     */
    private void loadDwithAddress(int address)
    {
        printWriter.println("@" + address);
        printWriter.println("D=A");
    }

    /**
     * DESCRIPTION: helper method used by loadDfromStack().
     */
    private void goToSegmentIndex_LoadDwithMemory()
    {
        printWriter.println("A=A+D");
        printWriter.println("D=M");
    }

    /**
     * DESCRIPTION: Pushes the value stored in D to stack.
     * PRECONDITION: The file was previously appended with a command to Store the desired push value in D.
     * POSTCONDITION: The file will be appended with the commands for: go to SP, increment SP address, go to SP address -1 ,
     * update M withe value in D.
     */
    private void writePush()
    {
        // increment SP address
        printWriter.println("@SP");
        printWriter.println("M=M+1");

        // update top of stack with value stored in D
        printWriter.println("A=M-1");
        printWriter.println("M=D");
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





    /***
     * DESCRIPTION: Closes the output file.
     */
    public void close()
    {
        this.printWriter.close();
    }
}
