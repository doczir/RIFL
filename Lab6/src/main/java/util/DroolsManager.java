package util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.kie.api.KieServices;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;


public class DroolsManager extends Thread {
	private BlockingQueue<Message> queue;
	
	private KieServices ks;
	private KieContainer kContainer;
	private KieSession kSession;
	
	public static long epoch = 0;
	
	private boolean running = true;

	private KieRuntimeLogger logger;
	
	private static DroolsManager instance = null;
	
	
	public static synchronized DroolsManager getInstance() {
		if (instance == null) {
			instance = new DroolsManager();
			instance.start();
		}
		
		return instance;
	}
	

	private DroolsManager() {
		queue = new LinkedBlockingQueue<Message>();
		
		running = true;
		
        // load up the knowledge base
        ks = KieServices.Factory.get();
	    kContainer = ks.getKieClasspathContainer();
    	kSession = kContainer.newKieSession("ksession-rules");
    	
    	logger = ks.getLoggers().newFileLogger(kSession, "logs/myHelloWorld");
    	
    	epoch = System.nanoTime();
	}
	
	@Override
	public void run() {
		super.run();
		
		Message message = null;
		System.out.println("Starting dm");
		while (running) {
			try {
				message = queue.take();
				
				kSession.insert(message);
				kSession.fireAllRules();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		logger.close();
	}
	
	public void insert(Message msg) {
		try {
			queue.put(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;
	}
}