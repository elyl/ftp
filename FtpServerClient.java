import java.net.Socket;
import java.io.PrintWriter;
import java.io.File;

public class FtpServerClient implements Runnable
{
    private PrintWriter	out;
    private Socket	s;
    private FtpServer	server;
    private String	user;
    private boolean	logged_in;
    
    public FtpServerClient(Socket s, FtpServer server) throws Exception
    {
	this.s = s;
	this.user = null;
	this.logged_in = false;
	this.server = server;
	out = new PrintWriter(s.getOutputStream(), true);
    }

    public void run()
    {
	byte	b[] = new byte[255];
	String	str;

	try
	    {
		s.getInputStream().read(b);
		str = new String(b);
		System.out.println(s);
		this.processRequest(str);
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
	    }
    }

    public void processUSER(String str)
    {
	String	params[];
	
	params = str.split(" ");
	System.out.println(params.length);
	if (params.length >= 2)
	    {
		this.user = params[1];
		out.println(ReturnCodes.USER_OK);
	    }
	else
	    out.println(ReturnCodes.SYNTAX_ERROR);
    }

    public void processPASS(String str)
    {
	String params[];
	
	if (this.user != null)
	    {
		params = str.split(" ");
		if (params.length >= 2)
		    {
			if (this.server.getUser(this.user).equals(params[1]))
			    {
				out.println(ReturnCodes.LOGGED_IN);
				this.logged_in = true;
				return;
			    }
			else
			    out.println(ReturnCodes.NOT_LOGGED_IN);
		    }
		else
		    out.println(ReturnCodes.SYNTAX_ERROR);
		this.user = null;
	    }
	else
	    out.println(ReturnCodes.USER_KO);
    }

    public void processRETR(String str)
    {
	System.out.println("RETR NYI");
    }

    public void processSTOR(String str)
    {
	System.out.println("STOR NYI");
    }

    public void processLIST(String str)
    {
	System.out.println("LIST NYI");
    }

    public void processQUIT(String str) throws Exception
    {
	s.close();
    }

    public void processPWD(String str)
    {
	System.out.println("PWD NYI");
    }

    public void processCWD(String str)
    {
	System.out.println("CWD NYI");
    }

    public void processCDUP(String str)
    {
	System.out.println("CDUP NYI");
    }

    public void processRequest(String str) throws Exception
    {
	if (str.startsWith("USER"))
	    this.processUSER(str);
	else if (str.startsWith("PASS"))
	    this.processPASS(str);
	else if (str.startsWith("RETR"))
	    this.processRETR(str);
	else if (str.startsWith("STOR"))
	    this.processSTOR(str);
	else if (str.startsWith("LIST"))
	    this.processLIST(str);
	else if (str.startsWith("QUIT"))
	    this.processQUIT(str);
	else if (str.startsWith("PWD"))
	    this.processPWD(str);
	else if (str.startsWith("CWD"))
	    this.processCWD(str);
	else if (str.startsWith("CDUP"))
	    this.processCDUP(str);
    }
}
