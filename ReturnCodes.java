public class ReturnCodes
{
    public static final String TRANSFER_START = "125 Data connection already open; starting file transfer";
    public static final String COMMAND_OK = "200 Command OK";
    public static final String CONNECTION_ESTABLISHED = "220 Service ready";
    public static final String GOODBYE = "221 Goodbye";
    public static final String DIRECTORY_CHANGED = "250 Directory changed";
    public static final String PWD = "257 Current directory is ";
    public static final String USER_OK = "331 User name OK, password required";
    public static final String TRANSFER_OK = "226 Transfer complete";
    public static final String LOGGED_IN = "230 User logged in";
    public static final String NO_DATA = "425 Can't open data connection";
    public static final String SYNTAX_ERROR = "500 Syntax Error";
    public static final String SYNTAX_ERROR_PARAMETER = "501 Syntax error in parameters";
    public static final String USER_KO = "503 Must use USER first";
    public static final String NOT_LOGGED_IN = "530 Not logged in";
    public static final String ACCESS_DENIED = "550 Access denied";
    public static final String FILE_NOT_FOUND = "550 File not found";
}
