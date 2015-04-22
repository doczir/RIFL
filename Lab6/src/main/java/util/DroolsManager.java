package util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;


public class DroolsManager extends Thread {
	private BlockingQueue<Message> queue;
	
	private KieServices ks;
	private KieContainer kContainer;
	private KieSession kSession;
	
	private boolean running = true;
	
	private static DroolsManager instance = null;
	
	
	public static synchronized DroolsManager getInstance() {
		if (instance == null) {
			instance = new DroolsManager();
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
	}
	
	@Override
	public void run() {
		super.run();
		
		Message message = null;
		while (running) {
			try {
				message = queue.take();
				
				kSession.insert(message);
				kSession.fireAllRules();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
