package fr.code4pi.gpio;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import fr.code4pi.thread.LedBlinking;

/**
 * Class to manage Led.
 * 
 * @author Benoit Mouquet
 * 
 */
public class LedManager {

	private GpioPinDigitalOutput pin1, pin2, pin3;
	private LedBlinking ledBlinking;
	private int rpmForPin1 = RPM_FOR_PIN1;
	private int rpmForPin2 = RPM_FOR_PIN2;
	private int rpmForPin3 = RPM_FOR_PIN3;
	private int rpmForBlinking = RPM_FOR_BLINKING;

	/**
	 * Limit when led of pin 1 turn on.
	 */
	public final static int RPM_FOR_PIN1 = 1785;
	/**
	 * Limit when led of pin 2 turn on.
	 */
	public final static int RPM_FOR_PIN2 = 1825;
	/**
	 * Limit when led of pin 3 turn on.
	 */
	public final static int RPM_FOR_PIN3 = 1865;
	/**
	 * Limit when all led blinking.
	 */
	public final static int RPM_FOR_BLINKING = 1880;

	/**
	 * Constructor of class.
	 * 
	 * @param pin1
	 *            Pin for first line of led.
	 * @param pin2
	 *            Pin for second line of led.
	 * @param pin3
	 *            Pin for third line of led.
	 */
	public LedManager(Pin pin1, Pin pin2, Pin pin3) {
		GpioController gpio = GpioFactory.getInstance();
		this.pin1 = gpio.provisionDigitalOutputPin(pin1, "MyLED", PinState.LOW);

		this.pin2 = gpio.provisionDigitalOutputPin(pin2, "MyLED", PinState.LOW);

		this.pin3 = gpio.provisionDigitalOutputPin(pin3, "MyLED", PinState.LOW);

		List<GpioPinDigitalOutput> gpioLed = new ArrayList<GpioPinDigitalOutput>();
		gpioLed.add(this.pin1);
		gpioLed.add(this.pin2);
		gpioLed.add(this.pin3);

		ledBlinking = new LedBlinking(gpioLed);
	}

	/**
	 * Set custom rpm limits.
	 * 
	 * @param rpmForPin1
	 *            Rpm start limit for first leds.
	 * @param rpmForPin2
	 *            Rpm start limit for second leds.
	 * @param rpmForPin3
	 *            Rpm start limit for third leds.
	 * @param rpmForBlinking
	 *            Rpm start limit for blinking.
	 */
	public void setCustomLimit(int rpmForPin1, int rpmForPin2, int rpmForPin3,
			int rpmForBlinking) {
		this.rpmForPin1 = rpmForPin1;
		this.rpmForPin2 = rpmForPin2;
		this.rpmForPin3 = rpmForPin3;
		this.rpmForBlinking = rpmForBlinking;
	}

	/**
	 * @param rpm
	 */
	public void updateLed(double rpm) {
		if (rpm >= rpmForPin1 && rpm < rpmForPin2) {
			stopBlinking();
			pin1.high();
			pin2.low();
			pin3.low();
		} else if (rpm >= rpmForPin2 && rpm < rpmForPin3) {
			stopBlinking();
			pin1.high();
			pin2.high();
			pin3.low();
		} else if (rpm >= rpmForPin3 && rpm < rpmForBlinking) {
			stopBlinking();
			pin1.high();
			pin2.high();
			pin3.high();
		} else if (rpm >= rpmForBlinking) {
			if (!ledBlinking.isAlive()) {
				ledBlinking.startThread();
			}

		} else {
			stopBlinking();
			pin1.low();
			pin2.low();
			pin3.low();
		}
	}

	/**
	 * Method call at the end to turn off led and stop blinking.
	 */
	public void finishThread() {
		stopBlinking();
		pin1.low();
		pin2.low();
		pin3.low();
	}

	/**
	 * Stop led blinking.
	 */
	private void stopBlinking() {
		if (ledBlinking.isAlive()) {
			ledBlinking.stopThread();
			try {
				ledBlinking.waitForThread();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method to test led blinking.
	 * 
	 * @param duration
	 *            Duration of blinking in millisecond
	 */
	public void testLeds(int duration) {
		ledBlinking.startThread();
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopBlinking();
	}

	/**
	 * @return the rpmForPin1
	 */
	public int getRpmForPin1() {
		return rpmForPin1;
	}

	/**
	 * @param rpmForPin1
	 *            the rpmForPin1 to set
	 */
	public void setRpmForPin1(int rpmForPin1) {
		this.rpmForPin1 = rpmForPin1;
	}

	/**
	 * @return the rpmForPin2
	 */
	public int getRpmForPin2() {
		return rpmForPin2;
	}

	/**
	 * @param rpmForPin2
	 *            the rpmForPin2 to set
	 */
	public void setRpmForPin2(int rpmForPin2) {
		this.rpmForPin2 = rpmForPin2;
	}

	/**
	 * @return the rpmForPin3
	 */
	public int getRpmForPin3() {
		return rpmForPin3;
	}

	/**
	 * @param rpmForPin3
	 *            the rpmForPin3 to set
	 */
	public void setRpmForPin3(int rpmForPin3) {
		this.rpmForPin3 = rpmForPin3;
	}

	/**
	 * @return the rpmForBlinking
	 */
	public int getRpmForBlinking() {
		return rpmForBlinking;
	}

	/**
	 * @param rpmForBlinking
	 *            the rpmForBlinking to set
	 */
	public void setRpmForBlinking(int rpmForBlinking) {
		this.rpmForBlinking = rpmForBlinking;
	}

}
