import java.util.concurrent.Semaphore;

/*
 * Class to simulate twisted pair wires
 * Student: 
 * Student Number: 
 *
 */
public class TwistedPairVer2
{
	private static int tpNumbers = 1000;
	private int tpId;   // identifier for twisted pair
	private final int maxBufLen = 60;  // Maximum size of string to represent xmission accross twisted pair
	private String buf;   // String to represent a twisted pair xmission, when empty it references an Empty String ""
	private Semaphore blockedReaders = new Semaphore(0);
	private Semaphore blockedWriters = new Semaphore(0);
	private int blockedReaderCount = 0;
	private int blockedWriterCount = 1;
	/**
	 * Constructor
	 */
	public TwistedPairVer2()
	{
		tpId = TwistedPairVer2.tpNumbers++;  // Unique identifier
		buf = "";
	}

	/*---------------------------------------------
	 * Methods and attributes for recving/xmitting across the twisted pair
	 -----------------------------------------------*/
	// Semaphore and flags for recving/xmitting across the twisted pair



	/*
	 * Xmitting across twisted pair
	 */
	public void xmit(String msg) throws InterruptedException
	{
		// Can we xmit?

		while(msg.length() > (maxBufLen - buf.length()) )
		{  
			try
			{
				this.blockedWriterCount++;
				blockedWriters.acquire();
				this.blockedWriterCount--;
			}
			catch (InterruptedException ex)
			{
				this.logMsg("terminated"); //log that an interrupt occurred at this level
				throw ex; //This needs to be handled further up (in the worker method) so we re-throw the error
			}
		}

		// ---- Critical Section ------------------------------------
		buf = buf+msg;  // appends new frame to the twisted pair
		//-----------------------------------------------------------
		if (this.blockedReaderCount > 0)
		{
			this.blockedReaders.release();
		}

	}
	/*
	 * Recving from twisted pair
	 */
	public String recv() throws InterruptedException
	{
		String msgs;  // for returning string in twisted pair
		
		/*
		  	While the buffer contains the empty string, there is nothing to be received ("consumed")
		  	therefore, the recv method waits (releasing the monitor) until the xmit method ("producer")
		  	sends a notification that something has been added to the buffer (in other words, something 
		  	has been "produced") 
		 */
		
		while (buf == "")
		{	
			try
			{
				this.blockedReaderCount++;
				blockedReaders.acquire();
				this.blockedReaderCount--;
			}
			catch (InterruptedException ex)
			{
				this.logMsg("terminated"); //log that an interrupt occurred at this level
				throw ex; //This needs to be handled further up (in the worker method) so we re-throw the error
			}
		}

		// ---- Critical Section ------------------------------------
		msgs = buf;
		buf = "";
		//-----------------------------------------------------------	
		
		for(int i=0;i<this.blockedWriterCount;i++)
		{
			this.blockedWriters.release();
		}
		
		return(msgs);
	}
	
	// twisted pair identifier getter
	public int getTwistedPairId()  { return tpId; }
	
	// For logging messages
	
	private void logMsg(String msg)
	{
		System.out.println("TwistedPair ("+tpId+", "+Thread.currentThread().getId()+") "+msg);
		System.out.flush();
	}
}
