import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;

public class FtpServer
{
    private ServerSocket	ss;
    private Map<String, String>	users;
    private String		basePwd;

    public FtpServer(int port, String basePwd) throws Exception
    {
	this.ss = new ServerSocket(port);
	this.users = new HashMap<String, String>();
	this.users.put("toto", "toto");
	this.basePwd = basePwd;
    }

    public void launch() throws Exception
    {
	Socket	s;

	while (true)
	    {
		s = this.ss.accept();
		new Thread(new FtpServerClient(s, this, this.basePwd)).start();
	    }
    }

    public String getUser(String user)
    {
	return (this.users.get(user));
    }
    
    public static void main(String args[]) throws Exception
    {
	if (args.length < 2)
	    System.out.println("Usage : <port> <pwd>");
	else
	    new FtpServer(Integer.parseInt(args[0]), args[1]).launch();
    }
}
