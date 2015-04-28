package util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.kie.api.KieServices;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;


public class DroolsManager extends Thread {
	private BlockingQueue<Message> queue;
	
	private KieServices ks;
	private KieContainer kContainer;
	private KieSession kSession;
	
	private long lastTS;
	private boolean running = true;

	private KieRuntimeLogger logger;
	private Map<Message, FactHandle> factHandles;
	
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
		factHandles = new HashMap<Message, FactHandle>();
		
		lastTS = -1L;
		running = true;
	}
	
	@Override
	public void run() {
		super.run();
		
        // load up the knowledge base
        ks = KieServices.Factory.get();
	    kContainer = ks.getKieClasspathContainer();
    	kSession = kContainer.newKieSession("ksession-rules");
    	
    	logger = ks.getLoggers().newFileLogger(kSession, "logs/myHelloWorld");

		
		Message message = null;
		System.out.println("Starting dm");
		while (running) {
			try {
				message = queue.take();
				
				if (factHandles.containsKey(message)) {
					// Ha frissíteni kell az eseményt
					kSession.update(factHandles.get(message), message);
				} else {
					// Ha be kell szúrni egy új eseményt
					factHandles.put(message, kSession.insert(message));
				}

				kSession.fireAllRules();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		logger.close();
	}
	
	public synchronized void insertMessage(Message message) {
		try {
			message.setTimestamp(getUniqueTS());
			
			queue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void updateMessage(Message message) {
		try {
			queue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private long getUniqueTS() {
		long ts = System.currentTimeMillis();
		
		while (ts <= lastTS) {
			ts++;
		}
		
		lastTS = ts;
		
		return ts;
	}
	
	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;
	}
}
