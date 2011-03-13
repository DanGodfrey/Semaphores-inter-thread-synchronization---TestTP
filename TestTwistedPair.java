
class TestTwistedPair 
{
	static String [] tstMessages0 =
	{
		"one-------------------------->", "two----------------------->", 
		"three------------------------>", "four---------------------->", 
		"five------------------------->", null
	};
	static String [] tstMessages1 =
	{
	    "un<<<<<<<<<<<<<<<<<<<<<<<<<<<", "deux<<<<<<<<<<<<<<<<<<<<<<<", 
	    "trois<<<<<<<<<<<<<<<<<<<<<<<<", "quatre<<<<<<<<<<<<<<<<<<<<<", 
	    "cinq<<<<<<<<<<<<<<<<<<<<<<<<<", null
	};
	static String [] tstMessages2 =
	{
	    "uno@@@@@@@@@@@@@@@@@@@@@@@@@@", "dos@@@@@@@@@@@@@@@@@@@@@@@@", 
	    "tres@@@@@@@@@@@@@@@@@@@@@@@@@", "Cuatro@@@@@@@@@@@@@@@@@@@@@", 
	    "Cinco@@@@@@@@@@@@@@@@@@@@@@@@@", null
	};
	static String [] tstMessages3 =
	{
	    "yi===========================<", "er=======================<", 
	    "san==========================<", "si=======================<", 
	    "wu===========================<", null
	};
	public static void main(String[] args)
	{
		int i;
		Thread [] threadReference = new Thread[8];
		TwistedPairVer2 twPair = new TwistedPairVer2();
		System.out.println("Starting the threads");
		// Create reader threads
		for(i=0; i<4; i++)
			threadReference[i] = new ReaderThread(i,twPair);
		// Create writer threads
		threadReference[4] = new WriterThread('a',twPair,tstMessages0);
		threadReference[5] = new WriterThread('b',twPair,tstMessages1);
		threadReference[6] = new WriterThread('c',twPair,tstMessages2);
		threadReference[7] = new WriterThread('d',twPair,tstMessages3);
		// Start the threads
		for(i=0 ; i<8 ; i++) threadReference[i].start();

		// Sleep a while
		try { Thread.sleep(5000); } 
	    catch (InterruptedException e) { System.out.println("Sleep interrupted");}
		   
		// Terminate threads
		for(i = 0 ; i < 4 ; i++) threadReference[i].interrupt();
		// Wait on all threads
		System.out.println("Waiting for threads to terminate"); System.out.flush();
		try { for(i = 0 ; i < 8 ; i++) threadReference[i].join(); }
		catch(InterruptedException e) { }
	}

}