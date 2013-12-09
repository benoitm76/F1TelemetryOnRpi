package fr.code4pi.thread;

import java.util.List;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * Runnable for led blinking.
 * 
 * @author Benoit Mouquet
 * 
 */
public class BlinkRunnable implements Runnable {

	private boolean stopThread;
	private List<GpioPinDigitalOutput> listLed;
	private boolean ledStatus = true;

	/**
	 * Class constructor.
	 * 
	 * @param listLed
	 *            List of led
	 */
	public BlinkRunnable(List<GpioPinDigitalOutput> listLed) {
		this.listLed = listLed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (!stopThread) {
			for (GpioPinDigitalOutput led : listLed) {
				if (ledStatus) {
					led.high();
				} else {
					led.low();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ledStatus = !ledStatus;
		}
		stopThread = false;
	}

	/**
	 * Stop loop of blinking.
	 */
	public synchronized void stopBlinking() {
		this.stopThread = true;
	}

}
