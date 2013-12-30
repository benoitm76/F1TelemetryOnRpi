package fr.code4pi.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Class to load properties of the application (GPIO configuration for led and
 * lcd).
 * 
 * @author Benoit Mouquet
 * 
 */
public class F1TelemetryProperties {

	private Properties properties;

	/**
	 * Constructor, load properties file.
	 * 
	 * @param fileName
	 *            File name of properties file
	 */
	public F1TelemetryProperties(String fileName) {

		properties = new Properties();
		FileInputStream input;
		try {
			input = new FileInputStream(fileName);
			properties.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get GPIO pin number for a specific wire.
	 * 
	 * @param param
	 *            Param name
	 * @param defaultPin
	 *            Default pin if not found in file
	 * @return Raspi pin of this param
	 */
	public Pin getGpioPinFor(String param, Pin defaultPin) {
		Pin pin = defaultPin;
		int pinNumber = getIntProperties(param, -1);
		if (pinNumber != -1) {
			switch (pinNumber) {
			case 0:
				pin = RaspiPin.GPIO_00;
				break;
			case 1:
				pin = RaspiPin.GPIO_01;
				break;
			case 2:
				pin = RaspiPin.GPIO_02;
				break;
			case 3:
				pin = RaspiPin.GPIO_03;
				break;
			case 4:
				pin = RaspiPin.GPIO_04;
				break;
			case 5:
				pin = RaspiPin.GPIO_05;
				break;
			case 6:
				pin = RaspiPin.GPIO_06;
				break;
			case 7:
				pin = RaspiPin.GPIO_07;
				break;
			case 8:
				pin = RaspiPin.GPIO_08;
				break;
			case 9:
				pin = RaspiPin.GPIO_09;
				break;
			case 10:
				pin = RaspiPin.GPIO_10;
				break;
			case 11:
				pin = RaspiPin.GPIO_11;
				break;
			case 12:
				pin = RaspiPin.GPIO_12;
				break;
			case 13:
				pin = RaspiPin.GPIO_13;
				break;
			case 14:
				pin = RaspiPin.GPIO_14;
				break;
			case 15:
				pin = RaspiPin.GPIO_15;
				break;
			case 16:
				pin = RaspiPin.GPIO_16;
				break;
			case 17:
				pin = RaspiPin.GPIO_17;
				break;
			case 18:
				pin = RaspiPin.GPIO_18;
				break;
			case 19:
				pin = RaspiPin.GPIO_19;
				break;
			case 20:
				pin = RaspiPin.GPIO_20;
				break;
			}
		}
		return pin;
	}

	/**
	 * Get int value from properties file.
	 * 
	 * @param param
	 *            Param name
	 * @param defaultValue
	 *            Default value if properties not found or not valid
	 * @return int properties
	 */
	public int getIntProperties(String param, int defaultValue) {
		try {
			return Integer.parseInt(properties.getProperty(param));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 * Get boolean value from properties file.
	 * 
	 * @param param
	 *            Param name
	 * @return boolean properties
	 */
	public boolean getBooleanProperties(String param) {
		return "true".equals(properties.getProperty(param));
	}
}
