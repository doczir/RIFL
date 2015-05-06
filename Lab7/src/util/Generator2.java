package util;

import java.util.Random;
import java.util.function.IntSupplier;

public class Generator2 implements IntSupplier {

	private static final int TYPE = 1;
	
	private static Generator2 instance = null;
	
	private Random random = null;
	
	
	public static Generator2 getInstance() {
		if (instance == null) {
			instance = new Generator2();
		}
		
		return instance;
	}
	
	private Generator2() {
		random = new Random();
	}

	@Override
	public int getAsInt() {
		if (TYPE == 0) {
			// Folytonos normális eloszlás
			double res = (random.nextGaussian()+10)*100;
			if (res < 0.0d) {
				return 0;
			}
			
			return (int) res;
		} else {
			// Diszkrét egyenletes eloszlás
			return random.nextInt(1000);
		}
	}
	
}
