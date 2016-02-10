import java.net.Socket;

public class FtpServerClient implements Runnable
{
    private Socket	s;
    
    public FtpServerClient(Socket s)
    {
	this.s = s;
    }

    public void run()
    {
	byte	b[] = new byte[255];
	String	str;

	str = "";
	try
	    {
		s.getInputStream().read(b);
		str = new String(b);
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
	    }
	System.out.println(s);
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
	else
	    this.processRequest(str);
    }

    public void processUSER(String str)
    {
	System.out.println("USER NYI");
    }

    public void processPASS(String str)
    {
	System.out.println("PASS NYI");
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

    public void processQUIT(String str)
    {
	System.out.println("QUIT NYI");
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

    public void processRequest(String str)
    {
	System.out.println(" NYI");
    }
}
