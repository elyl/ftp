import java.net.Socket;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.nio.file.Path;

public class FtpServerClient implements Runnable
{
    private PrintWriter		out;
    private Socket		s;
    private FtpServer		server;
    private String		user;
    private boolean		logged_in;
    private boolean		running;
    private String		pwd;
    private PrintWriter		dataOut;
    private BufferedReader	dataIn;
    private Socket		sout;
    
    public FtpServerClient(Socket s, FtpServer server) throws Exception
    {
	this.s = s;
	this.user = null;
	this.logged_in = false;
	this.server = server;
	this.running = true;
	this.setPwd("/home/elyl");
	out = new PrintWriter(s.getOutputStream(), true);
	out.println(ReturnCodes.CONNECTION_ESTABLISHED);
    }

    public void run()
    {
	byte	b[] = new byte[255];
	String	str;

	while (this.running)
	    {
		try
		    {
			Arrays.fill(b, (byte)0);
			s.getInputStream().read(b);
			str = new String(b);
			this.processRequest(str);
		    }
		catch (Exception e)
		    {
			e.printStackTrace();
		    }
	    }
    }

    public void processUSER(String str)
    {
	String	params[];
	
	params = str.split(" ");
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
	String	params[];
	String	pass;
	
	if (this.user != null)
	    {
		params = str.split(" ");
		if (params.length >= 2)
		    {
			pass = this.server.getUser(this.user);
			if (pass != null && pass.equals(params[1]))
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

    public void processRETR(String str) throws Exception
    {
	String		params[];
	File		f;
	BufferedReader	bf;
	String		buffer;
	
	if (!this.logged_in)
	    {
		out.println(ReturnCodes.NOT_LOGGED_IN);
		return;
	    }
	if (this.sout == null)
	    {
		out.println(ReturnCodes.NO_DATA);
		return;
	    }
	params = str.split(" ");
	if (params.length < 2)
	    {
		out.println(ReturnCodes.SYNTAX_ERROR);
		return;
	    }
	f = new File(params[1]);
	if (!f.isAbsolute())
	    f = new File(this.pwd + params[1]);
	if (!f.exists())
	    {
		out.println(ReturnCodes.FILE_NOT_FOUND);
		return;
	    }
	if (!f.canRead())
	    {
		out.println(ReturnCodes.ACCESS_DENIED);
		return;
	    }
	out.println(ReturnCodes.TRANSFER_START);
	bf = new BufferedReader(new FileReader(f));
	while ((buffer = bf.readLine()) != null)
	    dataOut.write(buffer);
	out.println(ReturnCodes.TRANSFER_OK);
	this.resetDataStream();
    }

    public void processSTOR(String str) throws Exception
    {
	String		params[];
	String		buffer;
	File		f;
	PrintWriter	pr;
	
	if (!this.logged_in)
	    {
		out.println(ReturnCodes.NOT_LOGGED_IN);
		return;
	    }
	if (this.sout == null)
	    {
		out.println(ReturnCodes.NO_DATA);
		return;
	    }
	params = str.split(" ");
	if (params.length < 2)
	    {
		out.println(ReturnCodes.SYNTAX_ERROR);
		return;
	    }
	f = new File(params[1]);
	if (!f.isAbsolute())
	    f = new File(this.pwd + params[1]);
	f.delete();
	f.createNewFile();
	if (!f.canWrite())
	    {
		out.println(ReturnCodes.ACCESS_DENIED);
		return;
	    }
	pr = new PrintWriter(f);
	out.println(ReturnCodes.TRANSFER_START);
	while ((buffer = dataIn.readLine()) != null)
	    pr.println(buffer);
	pr.close();
	out.println(ReturnCodes.TRANSFER_OK);
	this.resetDataStream();
    }

    public void processLIST(String str)
    {
	if (!this.logged_in)
	    {
		out.println(ReturnCodes.NOT_LOGGED_IN);
		return;
	    }
	System.out.println("LIST NYI");
    }

    public void processQUIT(String str) throws Exception
    {
	this.running = false;
	s.close();
    }

    public void processPWD(String str)
    {
	out.println(ReturnCodes.PWD + this.pwd);
    }

    public void processCWD(String str)
    {
	String	params[];
	File	f;

	params = str.split(" ");	
	if (params.length >= 2)
	    {
		f = new File(params[1]);
		if (!f.isAbsolute())
		    f = new File(this.pwd + params[1]);
		System.out.println(f);
		if (f.exists())
		    {
			this.setPwd(f.toString());
			out.println(ReturnCodes.DIRECTORY_CHANGED);
		    }
		else
		    out.println(ReturnCodes.FILE_NOT_FOUND);
	    }
	else
	    out.println(ReturnCodes.SYNTAX_ERROR);
    }

    public void processSYST(String str)
    {
	out.println("UNIX");
    }

    public void processPORT(String str)
    {
	String	params[];
	String	addr;

	params = str.split(" ")[1].split(",");
	if (params.length < 6)
	    {
		out.println(ReturnCodes.SYNTAX_ERROR);
		return;
	    }
	if (!this.logged_in)
	    {
		out.println(ReturnCodes.NOT_LOGGED_IN);
		return;
	    }
	addr = params[0] + "." + params[1] + "." + params[2] + "." + params[3];
	try
	    {
		this.sout = new Socket(addr, (Integer.parseInt(params[4]) << 8) + Integer.parseInt(params[5]));
		this.dataOut = new PrintWriter(this.sout.getOutputStream(), true);
		this.dataIn = new BufferedReader(new InputStreamReader(this.sout.getInputStream()));
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
		out.println(ReturnCodes.SYNTAX_ERROR_PARAMETER);
		return;
	    }
	out.println(ReturnCodes.COMMAND_OK);
    }

    public void processRequest(String str) throws Exception
    {
	str = str.replace("\n", "");
	str = str.replace("\r", "");
	str = str.trim();
	System.out.println(str);
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
	    this.processCWD(str);
	else if (str.startsWith("SYST"))
	    this.processSYST(str);
	else if (str.startsWith("PORT"))
	    this.processPORT(str);
	else
	    out.println(ReturnCodes.SYNTAX_ERROR);
    }

    private void setPwd(String pwd)
    {
	if (!pwd.endsWith("/"))
	    pwd += "/";
	this.pwd = pwd;
    }

    private void resetDataStream() throws Exception
    {
	this.dataOut = null;
	this.sout.close();
	this.sout = null;
	this.dataIn = null;
    }
}
