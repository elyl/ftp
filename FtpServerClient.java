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

	try
	    {
		s.getInputStream().read(b);
		System.out.println(new String(b));
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
	    }
    }
}
