import java.net.ServerSocket;
import java.net.Socket;

public class FtpServer
{
    private ServerSocket ss;

    public FtpServer(int port) throws Exception
    {
	this.ss = new ServerSocket(port);
    }

    public void launch() throws Exception
    {
	Socket	s;

	s = this.ss.accept();
	new Thread(new FtpServerClient(s)).start();
    }
    
    public static void main(String args[]) throws Exception
    {
	new FtpServer(Integer.parseInt(args[0])).launch();
    }
}
