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



		// ---- Critical Section ------------------------------------
		buf = buf+msg;  // appends new frame to the twisted pair
		//-----------------------------------------------------------


	}
	/*
	 * Recving from twisted pair
	 */
	public String recv() throws InterruptedException
	{
		String msgs;  // for returning string in twisted pair


		// ---- Critical Section ------------------------------------
		msgs = buf;
		buf = "";
		//-----------------------------------------------------------


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
