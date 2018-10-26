import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Stream;

enum CommandType{C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION, C_RETURN, C_CALL};

public class Parser
{

    private File file;
    private Scanner inputStream;

    private String rawLine;
    private String cleanLine;
    private int lineNumber;


    /***
     * FULL CONSTRUCTOR: opens input file/stream and prepares to parse.
     * PRECONDITION: provided file is ASM file.
     * POSTCONDITION: if file can't be opened, ends program w/ error message.
     * @param fileName The file to open.
     */
    public  Parser(String fileName) throws IOException
    {
        file = new File(fileName);
        inputStream = new Scanner(file);
        resetFields();
        lineNumber = 0;
    }

    private void resetFields()
    {
        rawLine = "";
        cleanLine = "";
    }

    /***
     * DESCRIPTION: Are there more commands in the input?
     * POSTCONDITON: returns true if more commands, else close stream.
     * @return return boolean true if more commands, else false.
     */
    public boolean hasMoreCommands()

    {
        if(inputStream.hasNextLine())
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
        resetFields();
        rawLine = inputStream.nextLine();
        cleanLine();
        parseCommand();
        lineNumber++;
    }

    private void parseCommand()
    {

    }

    /***
     * DESCRIPTION: Returns the first arg. of the current command. In the case of C_ARITHMETIC, the command itself
     * (add, sub, etc>) is returned. Should not be called in the current command is C_RETURN.
     * @param argument
     */
    public void arg1(String argument)
    {

    }

    /***
     * DESCRIPTION: Returns the second argument of the current command. Should be called only if the current
     * command is C_PUSH, C_POP, C_FUNCTION, or C_CALL.
     * @param argument
     */
    public void arg2(String argument)
    {

    }

    /***
     * METHOD: cleans rawLine by removing whitespace and comments.
     * PRECONDITION: advance() is called so rawLine has string to clean.
     * POSTCONDITION: cleanLine is updated with contents of rawLine without comments or whitespace.
     */
    private void cleanLine()
    {
        String rawLine = this.rawLine;

        String line = rawLine.replaceAll(" ", "");
        line = line.replaceAll("\t", "");

        int commentLocation = line.indexOf("//");
        if (commentLocation != -1)
        {
            line = line.substring(0, commentLocation);
        }

        cleanLine = line;
    }
}
