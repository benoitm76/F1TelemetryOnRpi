package fr.code4pi.gpio;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import fr.code4pi.thread.LedBlinking;

public class LedManager {

	private GpioPinDigitalOutput bleu, pink, orange;
	private LedBlinking ledBlinking;

	public LedManager(Pin bleuPin, Pin orangePin, Pin pinkPin) {
		GpioController gpio = GpioFactory.getInstance();
		bleu = gpio.provisionDigitalOutputPin(bleuPin, "MyLED", PinState.HIGH);

		pink = gpio.provisionDigitalOutputPin(pinkPin, "MyLED", PinState.HIGH);

		orange = gpio.provisionDigitalOutputPin(orangePin, "MyLED",
				PinState.HIGH);

		List<GpioPinDigitalOutput> gpioLed = new ArrayList<GpioPinDigitalOutput>();
		gpioLed.add(bleu);
		gpioLed.add(pink);
		gpioLed.add(orange);

		ledBlinking = new LedBlinking(gpioLed);
		
		bleu.low();
		orange.low();
		pink.low();
	}

	public void updateLed(double rpm) {
		if (rpm >= 1780 && rpm < 1830) {
			if (ledBlinking.isAlive()) {
				ledBlinking.stopThread();
				try {
					ledBlinking.waitForThread();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			bleu.high();
			orange.low();
			pink.low();
		} else if (rpm >= 1830 && rpm < 1870) {
			if (ledBlinking.isAlive()) {
				ledBlinking.stopThread();
				try {
					ledBlinking.waitForThread();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			bleu.high();
			orange.high();
			pink.low();
		} else if (rpm >= 1870 && rpm < 1880) {
			if (ledBlinking.isAlive()) {
				ledBlinking.stopThread();
				try {
					ledBlinking.waitForThread();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			bleu.high();
			orange.high();
			pink.high();
		} else if (rpm >= 1880) {
			if (!ledBlinking.isAlive()) {
				ledBlinking.startThread();
			}

		} else {
			if (ledBlinking.isAlive()) {
				ledBlinking.stopThread();
				try {
					ledBlinking.waitForThread();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			bleu.low();
			orange.low();
			pink.low();
		}
	}

	public void finishThread() {
		if (ledBlinking.isAlive()) {
			ledBlinking.stopThread();
			try {
				ledBlinking.waitForThread();
				System.out.println("Fermeture thread Led");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		bleu.low();
		orange.low();
		pink.low();
	}

}
