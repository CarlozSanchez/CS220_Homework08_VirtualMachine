// Programer: Carlos Sanchez
// Class: CS220 MW 3:30pm - 5:20pm
// Lst Update: 10/16/2018
// Version 1.0


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

enum CommandType
{
    C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION, C_RETURN, C_CALL
}

public class Parser
{
    private final static String DELIMETER = " ";
    private final static String[] ARITHMETIC_BOOLEAN_COMMANDS = {"add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"};
    // private final static String[] MEMORY_ACCESS_COMMANDS = {"push", "pop"};
    // private final static String[] PROGRAM_FLOW_COMMANDS = {"label", "goto", "if-goto"};
    // private final static String[] FUNCTION_CALLING_COMMANDS = {"function", "call", "return"};


    HashMap<String, CommandType> myHashMap;


    private File file;
    private Scanner inputStream;
    private int lineNumber;

    private String nextCommandLine;
    private CommandType commandType;
    private String command, arg1, arg2;


    /***
     * DESCRIPTION: opens input file/stream and prepares to parse.
     * PRECONDITION: provided file is ASM file.
     * POSTCONDITION: if file can't be opened, ends program w/ error message.
     * @param fileName The file to open.
     */
    public Parser(String fileName) throws FileNotFoundException
    {
        file = new File(fileName);
        inputStream = new Scanner(file);
        initializeSets();
        lineNumber = 0;
        resetFields();
    }


    /***
     * DESCRIPTION: This initializes the hashmap containing a KV pair of command String with their appropriate CommandType.
     * PRECONDITION: none.
     * POSTCONDITION: hashmap will be initialized and updated with all required commands.
     */
    private void initializeSets()
    {
        // arithmeticBooleanSet = new HashSet<String>();
        myHashMap = new HashMap<String, CommandType>();

        for (String str : ARITHMETIC_BOOLEAN_COMMANDS)
        {
            //arithmeticBooleanSet.add(str);
            myHashMap.put(str, CommandType.C_ARITHMETIC);
        }

        // Memory Access Commands
        myHashMap.put("push", CommandType.C_PUSH);
        myHashMap.put("pop", CommandType.C_POP);

        // Program Flow Commands
        myHashMap.put("label", CommandType.C_LABEL);
        myHashMap.put("goto", CommandType.C_GOTO);
        myHashMap.put("if-goto", CommandType.C_IF);

        // Function Calling Commands
        myHashMap.put("function", CommandType.C_FUNCTION);
        myHashMap.put("call", CommandType.C_CALL);
        myHashMap.put("return", CommandType.C_RETURN);
    }


    /***
     * DESCRIPTION: Helper method that resets commandType to null. resets nextCommandLine, arg1, arg2 to empty string.
     * PRECONDITION: this should be automatically called by advane()
     * POSTCONDITION: commandType set null.  nextCommandLine, arg1, arg2 are set to empty string.
     */
    private void resetFields()
    {
        nextCommandLine = "";
        commandType = null;
        command = arg1 = arg2 = "";
    }


    /***
     * DESCRIPTION: Are there more commands in the input?
     * POSTCONDITON: returns true if more commands, else close stream.
     * @return return boolean true if more commands, else false.
     */
    public boolean hasMoreCommands()

    {
        if (inputStream.hasNextLine())
        {
            return true;
        }
        else
        {
            inputStream.close();
            return false;
        }
    }

    /***
     * DESCRIPTION: Reads the next command from the input and makes it the current command. Should be called only if
     * hasMoreCommands is true. Initially there is no current command.
     */
    public void advance()
    {
        lineNumber++;
        resetFields();
        nextCommandLine = inputStream.nextLine();
        parseCommand();
    }


    /***
     * DESCRIPTION: Parses the commandType, arg1 , arg2 from nextCommandLine.
     * PRECONDITION: advance() was called prior to invoking this method.
     * POSTCONDITION: commandType, arg1, arg2 should be updated if valid arguments where found in nextCommandLine.
     * otherwise the fields will be either null or empty string.
     */
    private void parseCommand()
    {
        String[] command = nextCommandLine.split(DELIMETER);

        this.commandType = myHashMap.get(command[0]);
        this.command = command[0];

        if (command.length == 3)
        {
            this.arg1 = command[1];
            this.arg2 = command[2];
        }
    }

    /**
     * DESCRIPTION: Returns one of the following CommandType, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF-GOTO, C_FUNCTION,
     * C_CALL, C_RETURN .
     * PRECONDITION: advance must have been called prior in order for commandType field to be updated.
     * POSTCONDITION: commandType is updated to command type found in current line, if no matching command type is
     * recognized the field will be null.
     * @return a String representing the current command type in String form..
     */
    public CommandType commandType()
    {
        return this.commandType;
    }


    /**
     * DESCRIPTION: Returns the command type of this current line. either "push", "pop", "label", "goto", "if-goto",
     * "function", "call", "return".
     * PRECONDITION: advance must have been called prior in order for commandType field to be updated.
     * POSTCONDITION: command is updated to command type found in current line, if no matching command type is
     * recognized the field will be empty String.
     * @return a String representing the current command type in String form..
     */
    public String command()
    {
        return this.command;
    }

    /***
     * DESCRIPTION: Returns the first arg. of the current command. In the case of C_ARITHMETIC, the command itself
     * (add, sub, etc>) is returned. Should not be called in the current command is C_RETURN.
     * PRECONDITION: advance() has been called to update class variable arg1.
     * POSTCONDITION: The argument contained in arg1 is returned.
     * @return String containing the first argument.
     */
    public String arg1()
    {
        return this.arg1;
    }

    /***
     * DESCRIPTION: Returns the second argument of the current command. Should be called only if the current
     * command is C_PUSH, C_POP, C_FUNCTION, or C_CALL.
     * PRECONDITION: advance() has been called to update class variable arg1.
     * POSTCONDITION: The argument contained in arg2 is returned.
     * @return String containing the second argument.
     */
    public String arg2()
    {
        return this.arg2;
    }


    /***
     * DESCRIPTION: this describes the current status of Parser Class Including File Name, Line Number, Next Command Line,
     * Command Type, Argument 1, Argument 2.
     * PRECONDITION: none
     * POSTCONDITION: A String describing the status of Parser Class is returned.
     * @return String describing the status of Parser Class.
     */
    public String toString()
    {
        int firstBuffer = -16;
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%" + firstBuffer + "s" + "%s\n", "File:", file.getName()));
        sb.append(String.format("%" + firstBuffer + "s" + "%s\n", "Line Number:", lineNumber));
        sb.append(String.format("%" + firstBuffer + "s" + "%s\n", "Next Command:", nextCommandLine));
        sb.append(String.format("%" + firstBuffer + "s" + "%s\n", "Command Type:", commandType));
        sb.append(String.format("%" + firstBuffer + "s" + "%s\n", "Command: ", command));
        sb.append(String.format("%" + firstBuffer + "s" + "%s\n", "Argument 1:", arg1));
        sb.append(String.format("%" + firstBuffer + "s" + "%s\n", "Argument 2:", arg2));

        return sb.toString();
    }
}
