package fr.code4pi.thread;

import java.util.List;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * Class which permit led blinking.
 * 
 * @author Benoit Mouquet
 * 
 */
public class LedBlinking {
	private BlinkRunnable runnable;
	private Thread t;

	/**
	 * Constructor of class.
	 * 
	 * @param listLed
	 *            List of led to blink
	 */
	public LedBlinking(List<GpioPinDigitalOutput> listLed) {
		this.runnable = new BlinkRunnable(listLed);
	}

	/**
	 * Start blinking led.
	 */
	public void startThread() {
		t = new Thread(runnable);
		t.start();
	}

	/**
	 * Stop blinking led.
	 */
	public void stopThread() {
		runnable.stopBlinking();
	}

	/**
	 * Wait the end of the thread (to avoid blinking problems).
	 * 
	 * @throws InterruptedException
	 */
	public void waitForThread() throws InterruptedException {
		t.join();
	}

	/**
	 * Check if blinking is running.
	 * 
	 * @return if blinking is running.
	 */
	public boolean isAlive() {
		if(t != null)
			return t.isAlive();
		else
			return false;
	}

}