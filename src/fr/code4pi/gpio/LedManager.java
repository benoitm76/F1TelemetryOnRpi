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
	
	/**
	 * Limit when led of pin 1 turn on.
	 */
	public final static int RPM_FOR_PIN1 = 1780;
	/**
	 * Limit when led of pin 2 turn on.
	 */
	public final static int RPM_FOR_PIN2 = 1830;
	/**
	 * Limit when led of pin 3 turn on.
	 */
	public final static int RPM_FOR_PIN3 = 1870;
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
	 * @param rpm
	 */
	public void updateLed(double rpm) {
		if (rpm >= RPM_FOR_PIN1 && rpm < RPM_FOR_PIN2) {
			stopBlinking();
			pin1.high();
			pin2.low();
			pin3.low();
		} else if (rpm >= RPM_FOR_PIN2 && rpm < RPM_FOR_PIN3) {
			stopBlinking();
			pin1.high();
			pin2.high();
			pin3.low();
		} else if (rpm >= RPM_FOR_PIN3 && rpm < RPM_FOR_BLINKING) {
			stopBlinking();
			pin1.high();
			pin2.high();
			pin3.high();
		} else if (rpm >= RPM_FOR_BLINKING) {
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
	private void stopBlinking()
	{
		if (ledBlinking.isAlive()) {
			ledBlinking.stopThread();
			try {
				ledBlinking.waitForThread();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
