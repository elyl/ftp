import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;

public class FtpServer
{
    private ServerSocket	ss;
    private Map<String, String>	users;

    public FtpServer(int port) throws Exception
    {
	this.ss = new ServerSocket(port);
	this.users = new HashMap<String, String>();
	this.users.put("toto", "toto");
    }

    public void launch() throws Exception
    {
	Socket	s;

	while (true)
	    {
		s = this.ss.accept();
		new Thread(new FtpServerClient(s, this)).start();
	    }
    }

    public String getUser(String user)
    {
	return (users.get(user));
    }
    
    public static void main(String args[]) throws Exception
    {
	new FtpServer(Integer.parseInt(args[0])).launch();
    }
}
