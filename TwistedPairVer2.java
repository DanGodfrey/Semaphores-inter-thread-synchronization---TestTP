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
	private Semaphore blockedReaders = new Semaphore(0); //Semaphore for synchronizing consumers
	private Semaphore blockedWriters = new Semaphore(0); //Semaphore for synchronizing producers
	private int blockedReaderCount = 0; //count of the number of currently blocked consumers
	private int blockedWriterCount = 0; //count of the number of currently blocked consumers
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
		
		/*
		   Check if msg length is greater then max buffer.
		   If msg length is greater then max buffer it can
		   NEVER be sent. If we attempt to send it as usual,
		   the writer will be stuck in an infinite loop where
		   it is constantly acquiring a permit. 
		   
		   To avoid this, we print an error and terminate the 
		   method early in the case that the msg is > then 
		   the max buffer.
		 */
		
		if (msg.length() > maxBufLen)
		{
			this.logMsg("Message exceeds max buffer. Message not sent");
			return;
		}
		
		/*
		 	While there is no room in the buffer for the message, 
		 	attempt to acquire a permit from the writers semaphore.
		 	If a permit is not available, the writer will block
		 	until one is released in the recv method. 
		 	
		 	A counter is incremented before attempting to acquire 
		 	a permit and decremented after is has been acquired. 
		 	This allows us to maintain a count of blocked writers.
		 */
		
		while(msg.length() > (maxBufLen - buf.length()))
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
		
		//If one to many readers are blocked, release a SINGLE permit from the readers semaphore after executing the CS
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
	 	While there is nothing to read from the buffer (buffer 
	 	is the empty string), attempt to acquire a permit from 
	 	the readers semaphore. If a permit is not available, 
	 	the reader will block until one is released in the xmit 
	 	method. 
	 	
	 	A counter is incremented before attempting to acquire 
	 	a permit and decremented after is has been acquired. 
	 	This allows us to maintain a count of blocked readers.
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
		
		
		//Release ALL permits in the writers semaphore after executing the CS
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
