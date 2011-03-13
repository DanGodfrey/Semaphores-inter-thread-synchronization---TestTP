/* 
 * Class: ReaderThread
 * Description:  Reads from a TwistedPairVer2 object.
 */
public class ReaderThread extends Thread
{
	TwistedPairVer2 tp;
	int id;
	
	// Object constructed with an id and reference to TwistedPairVer2 object
	public ReaderThread(int id, TwistedPairVer2 tp)
	{
		this.id = id;
		this.tp = tp;
    }
	
	// Method run by the thread which reads from the TwistedPairVer2 object until
	// thread is cancelled.
	public void run()
	{		
		String msg;
		System.out.println("Starting Reader Thread "+id+"("+this.getId()+")"); System.out.flush();
		while(true)
		{
			try
			{
				msg = tp.recv();
				System.out.printf("Reader %d (%d): received >%s<\n",id,getId(),msg); System.out.flush();
			}
			catch (InterruptedException ex) { break; }
			if(this.isInterrupted()) break;  // break out of loop and terminate the thread.
		}
		System.out.println("REader Thread "+id+"("+this.getId()+") Terminated"); System.out.flush();
	}
}
