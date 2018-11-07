// FileName: CodeWriter.java
// Programer: Carlos Sanchez
// Class: CS220 MW 3:30pm - 5:20pm
// Lst Update: 10/31/2018
// Version 1.0


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;


/**
 * CodeWriter.java - This writes the appropriate Arithmetic or Push/Push operation to destination file. The commands
 * fall into 2 categories of an Arithmetic commands that consist on a single phrase (ex. add, not, eql) or a PushPop
 * commands that consists of 3 arguments ex(push constant 4, pop static 25).
 */
public class CodeWriter
{
    String fileName;
    private PrintWriter printWriter;
    private int labelCounter;


    /***
     * DESCRIPTION: Opens the output file/stream and gets ready to write into it.
     * PRECONDITION
     */
    CodeWriter(String fileName) throws FileNotFoundException
    {
        this.fileName = fileName;
        this.printWriter = new PrintWriter(new FileOutputStream(fileName));
        this.labelCounter = 0;
    }

    /***
     * DESCRIPTION: Informs the code writer that the translation of a new VM file is started.
     * @param fileName The file to set
     */
    private void setFileName(String fileName) throws FileNotFoundException
    {
        this.fileName = fileName;
        this.printWriter = new PrintWriter(new FileOutputStream(fileName));
        this.labelCounter = 0;
    }

    /***
     * DESCRIPTION: Closes the output file.
     */
    public void close()
    {
        this.printWriter.close();
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
     * DESCRIPTION: Writes the first steps of a single pop operation by going to the top of stack.
     * PRECONDITION: none.
     * POSTCONDITION: Writes to file the assembly code for A-register to hold top of stack address.
     */
    private void singlePopOperation()
    {
        printWriter.println("@SP");         // go to Stack Pointer
        printWriter.println("A=M-1");       // go to Top of Stack
    }

    /**
     * DESCRIPTION: Writes the first steps of a double pop operation by going to SP, decrement SP address and go to
     * new SP address, Store contents of 1st Pop value into D, go to location of 2nd value to be popped.
     * PRECONDITION: none.
     * POSTCONDITION: Writes to file the assembly code that will go to address
     * of top of stack. Pop off value at top of stack, store it in D. Then
     * decrement the stack pointer.
     */
    private void doublePopOperation()
    {
        printWriter.println("@SP");         // go to Stack Pointer
        printWriter.println("AM=M-1");      // decrement sp & go to new address
        printWriter.println("D=M");         // store value of first pop
        printWriter.println("A=A-1");       // go to current address - 1
    }

    /**
     * DESCRIPTION: Writes an Addition command by popping 2 values from stack, adding them, then pushing result into stack.
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
     * DESCRIPTION: Writes an Subtraction command by popping 2 values from stack, then subtracts the first popped value
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
     * DESCRIPTION: Writes a Negation command by popping a single value from stack, then writes a bit wise negation
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
     * DESCRIPTION: Writes an Equality operation by popping two values from stack, comparing both value then push the
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
     * DESCRIPTION: Writes a Greater then operation by popping two values from stack, comparing first value bigger then
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
     * DESCRIPTION: Writes a Less then operation by popping two values from stack, comparing second  value bigger then
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
     * DESCRIPTION: Writes the rest of a less then or greater then operation by pushing into stack the result(true,false)
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
     * DESCRIPTION: Writes an AND operation by popping two values from stack, performing an bit wise AND operation on
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
     * DESCRIPTION: Writes an OR operation by popping two values from stack, performing a bit wise OR operation on
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
     * DESCRIPTION: Writes a NOT operation by popping two values from stack, performing a bit wise NOT operation on
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
       // printWriter.printf("// %s %s %s\n", command, segment, index);
        printWriter.println("// " + command + " " + segment + " " + index);

        switch (command)
        {
            case "push":
                writePush(segment, index);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// PUSH METHODS //////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * DESCRIPTION: Writes the assembly instructions for "push segment i", where segment is the memory segment to push
     * into, and i represents the specific index of said segment to push into.
     * PRECONDITION: segment must be one of the following commands: "constant", "local", "argument", "this", "that",
     * "temp", "static".
     * POSTCONDITION: The file will be appended with the commands for: Go to segment index, Store literal value in D,
     *
     * @param segment The segment of memory to access.
     * @param index   The specific index in memory segment to access.
     */
    private void writePush(String segment, int index)
    {
        switch (segment)
        {
            case "constant":
                pushCONSTANT(index);
                break;

            case "local":
                pushLCL(index);
                break;

            case "argument":
                pushARG(index);
                break;

            case "this":
                pushTHIS(index);
                break;

            case "that":
                pushTHAT(index);
                break;

            case "temp":
                pushTEMP(index);
                break;

            case "static":
                pushSTATIC(index);
                break;

            case "pointer":
                pushPOINTER(index);
                break;

            default:
                System.out.println("unknown segment \"" + segment + "\" in loadDfromStack");
                break;
        }
    }

    /**
     * DESCRIPTION: Writes the assembly commands for pushing a "costant" value into stack.
     * PRECONDITION: index is a none negative number within memory range(0-max)
     * POSTCONDTION: The file will be appended with the commands: @+index, D=A,
     * @SP, M=M+1, A=M-1, M=D
     * @param index the index of memory segment CONSTANT we want to push from.
     */
    private void pushCONSTANT(int index)
    {
        loadD(index);                           // @ + index, D=A
        pushDintoStack();                       // @SP, M=M+1, A=M-1, M=D
    }

    /**
     * DESCRIPTION: Writes the assembly commands for pushing a "local" value into stack.
     * PRECONDITION: index is a none negative number within memory range(0-max)
     * POSTCONDTION: The file will be appended with the commands: @+index, D=A,
     * @LCL, A=M+D, D=M, @SP, M=M+1, A=M-1, M=D
     * @param index the index of memory segment LCL we want to push from.
     */
    private void pushLCL(int index)
    {
        loadD(index);                           // @+index, D=A
        printWriter.println("@LCL");            // @LCL
        dereferenceSegment();                   // A=M+D, D=M
        pushDintoStack();                       // @SP, M=M+1, A=M-1, M=D
    }

    /**
     * DESCRIPTION: Writes the assembly commands for pushing an "argument" value into stack.
     * PRECONDITION: index is a none negative number within memory range(0-max)
     * POSTCONDTION: The file will be appended with the commands: @+index, D=A,
     * @ARG, A=M+D, D=M, @SP, M=M+1, A=M-1, M=D
     * @param index the index of memory segment ARG we want to push from.
     */
    private void pushARG(int index)
    {
        loadD(index);                           // @ + index, D=A
        printWriter.println("@ARG");            // @ARG
        dereferenceSegment();                   // A=M+D, D=M
        pushDintoStack();                       // @SP, M=M+1, A=M-1, M=D
    }

    /**
     * DESCRIPTION: Writes the assembly commands for pushing a "this" value into stack.
     * PRECONDITION: index is a none negative number within memory range(0-max)
     * POSTCONDTION: The file will be appended with the commands: @+index, D=A,
     * @THIS, A=M+D, D=M, @SP, M=M+1, A=M-1, M=D
     * @param index the index of memory segment THIS we want to push from.
     */
    private void pushTHIS(int index)
    {
        loadD(index);                           // @ + index, D=A
        printWriter.println("@THIS");           // @THIS
        dereferenceSegment();                   // A=M+D, D=M
        pushDintoStack();                       // @SP, M=M+1, A=M-1, M=D
    }

    /**
     * DESCRIPTION: Writes the assembly commands for pushing a "that" value into stack.
     * PRECONDITION: index is a none negative number within memory range(0-max)
     * POSTCONDTION: The file will be appended with the commands: @+index, D=A,
     * @THAT, A=M+D, D=M, @SP, M=M+1, A=M-1, M=D
     * @param index the index of memory segment THAT we want to push from.
     */
    private void pushTHAT(int index)
    {
        loadD(index);                           // @ + index, D=A
        printWriter.println("@THAT");           // @THAT
        dereferenceSegment();                   // A=M+D, D=M
        pushDintoStack();                       // @SP, M=M+1, A=M-1, M=D
    }

    /**
     * DESCRIPTION: Writes the assembly commands for pushing a "temp" value into stack.
     * PRECONDITION: index is a none negative number within memory range(0-max)
     * POSTCONDTION: The file will be appended with the commands: @+index, D=A,
     * @THAT, A=A+1, A=A+D, D=M, @SP, M=M+1, A=M-1, M=D
     * @param index the index of memory segment TEMP we want to push from.
     */
    private void pushTEMP(int index)
    {
        loadD(index);                           // @ + index, D=A
        printWriter.println("@THAT");           // @THAT
        printWriter.println("A=A+1");           // A=A+1
        printWriter.println("A=A+D");           // A=A+D
        printWriter.println("D=M");             // D=M
        pushDintoStack();                       // @SP, M=M+1, A=M-1, M=D
    }

    /**
     * DESCRIPTION: Writes the assembly commands for pushing a "static" value into stack.
     * PRECONDITION: index is a none negative number within memory range(0-max)
     * POSTCONDTION: The file will be appended with the commands: @Codewriter.i,
     * D=M, @SP, M=M+1, A=M-1, M=D
     * @param index the index of memory segment STATIC we want to push from.
     */
    private void pushSTATIC(int index)
    {
        String temp = "@" + fileName + "." + index;

        printWriter.println(temp);              // @Codewriter.i
        printWriter.println("D=M");             // D=M
        pushDintoStack();                       // @SP, M=M+1, A=M-1, M=D
    }

    /**
     * DESCRIPTION: Writes the assembly commands for pushing a "pointer" value into stack.
     * PRECONDITION: index is a none negative number within memory range(0-max)
     * POSTCONDTION: The file will be appended with the commands: @+index, D=A, @THIS, A=A+D, D=M, @SP, M=M+1, A=M-1, M=D
     *
     * @param index the index of memory segment POINTER we want to push from.
     */
    private void pushPOINTER(int index)
    {
        loadD(index);                           // @+index, D=A
        printWriter.println("@3");              // @THIS
        printWriter.println("A=A+D");           // A=A+D
        printWriter.println("D=M");             // D=M
        pushDintoStack();                       // @SP, M=M+1, A=M-1, M=D
    }

    ///////////////////////////////////////// PUSH HELPER METHODS //////////////////////////////////////////////////////

    /**
     * DESCRIPTION: helper method used by push"Segment" methods to load a value into D.
     * POSTCONDTION: The file will be appended with the commands: @address, D=A
     *
     * @param address The value to assign D to.
     */
    private void loadD(int address)
    {
        printWriter.println("@" + address);
        printWriter.println("D=A");
    }

    /**
     * DESCRIPTION: helper method used by loadDfromStack().
     * POSTCONDTION: The file will be appended with the commands: A=M+D, D=M
     */
    private void dereferenceSegment()
    {
        printWriter.println("A=M+D");
        printWriter.println("D=M");
    }

    /**
     * DESCRIPTION: Pushes the value stored in D to stack.
     * PRECONDITION: The file was previously appended with a command to Store the desired push value in D.
     * POSTCONDITION: The file will be appended with the commands for: go to SP, increment SP address, go to SP address -1 ,
     * update M withe value in D.
     */
    private void pushDintoStack()
    {
        // increment SP address
        printWriter.println("@SP");
        printWriter.println("M=M+1");

        // update top of stack with value stored in D
        printWriter.println("A=M-1");
        printWriter.println("M=D");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// POP METHODS //////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /** pointer implementation
     * push pointer 0, this
     * pop pointer 1, that
     */


    /**
     * DESCRIPTION: Writes the assembly instructions for "pop segment i", where segment i is the memory segment to pop
     * to, and i represents the specific index of said segment to pop to.
     * PRECONDITION: segment must be one of the following commands: "constant", "local", "argument", "this", "that",
     * "temp", "static", "pointer".
     * POSTCONDITION: The file will be appended with the commands for: Pop top most value of stack into segmente i.
     *
     * @param segment The segment of memory to access.
     * @param index   The specific index in memory segment to access.
     */
    private void writePop(String segment, int index)
    {
        switch (segment)
        {
            case "constant":
                // don't write anything
                break;

            case "local":
                popLCL(index);
                break;

            case "argument":
                popARG(index);
                break;

            case "this":
                popTHIS(index);
                break;

            case "that":
                popTHAT(index);
                break;

            case "temp":
                popTEMP(index);
                break;

            case "static":
                popSTATIC(index);
                break;

            case "pointer":
                popPOINTER(index);
                break;

            default:
                System.out.println("unknown segment \"" + segment + "\" in loadDfromStack");
                break;
        }
    }


    /**
     * DESCRIPTION: Pops values from stack into LCL[index].
     * PRECONDITION: none.
     * POSTCONDITION:   Writes to file the assembly code for: @+index, D=a, @LCL, D=D+M, @R13, M=D, @SP, AM=M-1, D=m,
     * @R13, A=M, M=D
     * @param index The LCL[index] to pop value too.

     */
    private void popLCL(int index)
    {
        loadDwith(index);                       // @ + index, D=A

        printWriter.println("@LCL");            // @LCL
        printWriter.println("D=D+M");           // D = address of LCL[i]

        storeDestinationAddressInTemp();        // @R13, M = D
        popValueToDestinationAddress();         // @SP, AM=M-1, D=M, @R13, A=M, M=D
    }

    /**
     * DESCRIPTION: Pops values from stack into ARG[index].
     * PRECONDITION: none.
     * POSTCONDITION:   Writes to file the assembly code for: @+index, D=A, @ARG, D=D+M, @R13, M=D, @SP, AM=M-1, D=m,
     * @R13, A=M, M=D
     * @param index The ARG[index] to pop value too.
     */
    private void popARG(int index)
    {
        loadDwith(index);                       // @ + index, D=A

        printWriter.println("@ARG");            //@ARG
        printWriter.println("D=D+M");           // D = address of ARG[i]

        storeDestinationAddressInTemp();        // @R13, M = D
        popValueToDestinationAddress();         // @SP, AM=M-1, D=M, @R13, A=M, M=D
    }

    /**
     * DESCRIPTION: Pops values from stack into THIS[index].
     * PRECONDITION: none.
     * POSTCONDITION:   Writes to file the assembly code for: @+index, D=A, @THIS, D=D+M, @R13, M=D, @SP, AM=M-1, D=m,
     * @R13, A=M, M=D
     * @param index The THIS[index] to pop value too.
     */
    private void popTHIS(int index)
    {
        loadDwith(index);                       // @ + index, D=A

        printWriter.println("@THIS");           // @THIS
        printWriter.println("D=D+M");           // D = address of THIS[i]

        storeDestinationAddressInTemp();        // @R13, M = D
        popValueToDestinationAddress();         // @SP, AM=M-1, D=M, @R13, A=M, M=D
    }

    /**
     * DESCRIPTION: Pops values from stack into THAT[index].
     * PRECONDITION: none.
     * POSTCONDITION:   Writes to file the assembly code for: @+index, D=A, @THAT, D=D+M, @R13, M=D, @SP, AM=M-1, D=m,
     * @R13, A=M, M=D
     * @param index The THAT[index] to pop value too.
     */
    private void popTHAT(int index)
    {
        loadDwith(index);                       // @ + index, D=A

        printWriter.println("@THAT");           // @THAT
        printWriter.println("D=D+M");           // D = address of THAT[i]

        storeDestinationAddressInTemp();        // @R13, M = D
        popValueToDestinationAddress();         // @SP, AM=M-1, D=M, @R13, A=M, M=D
    }

    /**
     * DESCRIPTION: Pops values from stack into TEMP[index].
     * PRECONDITION: none.
     * POSTCONDITION: Writes to file the assembly code for: @+index, D=A, @THAT, A=A+1, D=D+M, @R13, M=D, @SP, AM=M-1, D=m,
     * @R13, A=M, M=D
     * @param index The THAT[index] to pop value too.
     */
    private void popTEMP(int index)
    {
        loadDwith(index);                       // @ + index, D=A

        printWriter.println("@THAT");           // @THAT
        printWriter.println("A=A+1");           // A=A+1
        printWriter.println("D=D+A");           // D=D+A

        storeDestinationAddressInTemp();        // @R13, M = D
        popValueToDestinationAddress();         // @SP, AM=M-1, D=M, @R13, A=M, M=D
    }

    /**
     * DESCRIPTION:Pops values from stack into STATIC[index].
     * PRECONDITION: none.
     * POSTCONDITION: Writes to file the assembly code for: @SP, AM=M=1, D=M, @CodeWriter.i, M=D
     *
     * @param index The STATIC[index] to pop value too.
     */
    private void popSTATIC(int index)
    {
        String str = "@" + fileName + "." + index;

        printWriter.println("@SP");             // go to Stack Pointer
        printWriter.println("AM=M-1");          // decrement SP, go to new Top of Stack address
        printWriter.println("D=M");             //  Pop M to D
        printWriter.println(str);               // @filename.i
        printWriter.println("M=D");             // M=D
    }

    /**
     * DESCRIPTION: Pops values from stack into POINTER[index].
     * PRECONDITION: none.
     * POSTCONDITION: Writes to file the assembly code for: @+index, D=A, @3, D=D+A, @R13, M=D, @SP, AM=M-1, D=m,
     *
     * @param index The POINTER[index] to pop value too.
     */
    private void popPOINTER(int index)
    {
        loadDwith(index);                       // @ + index, D=A

        printWriter.println("@3");              // @This
        printWriter.println("D=D+A");           // D=D+A

        storeDestinationAddressInTemp();        // @R13, M = D
        popValueToDestinationAddress();         // @SP, AM=M-1, D=M, @R13, A=M, M=D
    }

    /////////////////////////////////////////// POP HELPER METHODS /////////////////////////////////////////////////////
    /**
     * DESCRIPTION: Helper Method used by pop methods to write the assembly commands for loading D with a literal value.
     * PRECONDITION: None.
     * POSTCONDITION: Writes to file the assembly code for: @+index, D=A.
     *
     * @param value The value to load D with
     */
    private void loadDwith(int value)
    {
        printWriter.println("@" + value);   // @index
        printWriter.println("D=A");         // store index as value in D
    }


    /**
     * DESCRIPTION: Helper Method used by pop methods to write the assembly commands for storing the value in D to R13.
     * PRECONDITION: None.
     * POSTCONDITION: Writes to file the assembly code for: @R13, M=D
     */
    private void storeDestinationAddressInTemp()
    {
        printWriter.println("@R13");        // Go to temp R13
        printWriter.println("M=D");         // Store destination address
    }

    /**
     * DESCRIPTION: Helper Method used by pop methods to write the assembly commands for poping top most value in stack
     * to the destination address stored in R13.
     * PRECONDITION: None.
     * POSTCONDITION: Writes to file the assembly code for: @R13, M=D
     */
    private void popValueToDestinationAddress()
    {
        printWriter.println("@SP");         // go to Stack Pointer
        printWriter.println("AM=M-1");      // decrement SP, go to new Top of Stack address
        printWriter.println("D=M");         //  Pop M to D

        printWriter.println("@R13");        // Go to tempR13
        printWriter.println("A=M");         // go to segment[i]
        printWriter.println("M=D");         // Store the value of D into memory, segment[i] = D
    }
}
