/* 
 * Class: WriterThread
 * Description:  Writes to a TwistedPairVer2 object.
 */
public class WriterThread extends Thread
{
	char id;
	TwistedPairVer2 tp;
	int threadId;
	String [] msgs;

	// Object constructed with an id and reference to TwistedPairVer2 object	
	public WriterThread(char id, TwistedPairVer2 tp, String[] msgs)
	{
		this.id = id;
		this.tp = tp;
		this.msgs = msgs;
    }
	
	// Method run by the thread which writes to the TwistedPairVer2 object until
	// all messages sent.
	public void run()
	{
		int i;
		System.out.println("Starting Writer Thread "+id+"("+this.getId()+")"); System.out.flush();
		for (i=0 ; msgs[i] != null; i++)
		{
			try
			{
				System.out.printf("Writer %c (%d): xmitting >%s<\n",id,getId(),msgs[i]); System.out.flush();
				tp.xmit(msgs[i]);
			}
			catch (InterruptedException ex) { break; }
		}
		System.out.println("Writer Thread "+id+"("+this.getId()+") Terminated"); System.out.flush();
	}
}
