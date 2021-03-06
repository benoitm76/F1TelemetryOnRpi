package fr.code4pi;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.pi4j.io.gpio.RaspiPin;

import fr.code4pi.gpio.LcdManager;
import fr.code4pi.gpio.LcdParameter;
import fr.code4pi.gpio.LedManager;
import fr.code4pi.gpio.LedParameter;
import fr.code4pi.telemetry.F1Data;
import fr.code4pi.tools.F1TelemetryProperties;
import fr.code4pi.tools.Utils;

/**
 * Main class of F1TelemetryOnRpi.
 * 
 * @author Benoit Mouquet
 * 
 */
public class Main {

	private LedManager led;
	private LcdManager lcd;
	private boolean lcdStatus = false, ledStatus = false;
	private F1Data curData;
	private DatagramSocket localDatagramSocket;
	private boolean closureInProgress = false;
	
	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();

	}

	/**
	 * Constructor of the app.
	 */
	public Main() {
		
		System.out.println("Application startup : F1 telemetry on RPI");
		System.out.println("Version : " + getClass().getPackage().getImplementationVersion());
		System.out.println("Author : Benoit Mouquet");
		System.out.println("Website : http://code4pi.fr");

		F1TelemetryProperties properties = new F1TelemetryProperties(
				"config.properties");

		lcdStatus = properties.getBooleanProperties(LcdParameter.LCD_ON);
		ledStatus = properties.getBooleanProperties(LedParameter.LED_ON);

		if (ledStatus) {
			// Load GPIO pin configuration for led from properties file
			led = new LedManager(properties.getGpioPinFor(
					LedParameter.LED_PARAM_PIN_FIRST_LEDS, RaspiPin.GPIO_07),
					properties.getGpioPinFor(
							LedParameter.LED_PARAM_PIN_SECOND_LEDS,
							RaspiPin.GPIO_09), properties.getGpioPinFor(
							LedParameter.LED_PARAM_PIN_THIRD_LEDS,
							RaspiPin.GPIO_08));

			// Load custom limits for led from properties file
			led.setCustomLimit(properties.getIntProperties(
					LedParameter.LED_PARAM_RPM_FOR_PIN1,
					LedManager.RPM_FOR_PIN1), properties.getIntProperties(
					LedParameter.LED_PARAM_RPM_FOR_PIN2,
					LedManager.RPM_FOR_PIN2), properties.getIntProperties(
					LedParameter.LED_PARAM_RPM_FOR_PIN3,
					LedManager.RPM_FOR_PIN3), properties.getIntProperties(
					LedParameter.LED_PARAM_RPM_FOR_BLINKING,
					LedManager.RPM_FOR_BLINKING));

			// Test leds
			led.testLeds(2000);
		}

		curData = new F1Data();

		if (lcdStatus) {

			// Load GPIO pin configuration for lcd from properties file
			lcd = new LcdManager(properties.getGpioPinFor(
					LcdParameter.LCD_PARAM_PIN_RS, RaspiPin.GPIO_11),
					properties.getGpioPinFor(LcdParameter.LCD_PARAM_PIN_STROBE,
							RaspiPin.GPIO_10),
					properties.getGpioPinFor(LcdParameter.LCD_PARAM_PIN_BIT_1,
							RaspiPin.GPIO_06),
					properties.getGpioPinFor(LcdParameter.LCD_PARAM_PIN_BIT_2,
							RaspiPin.GPIO_05),
					properties.getGpioPinFor(LcdParameter.LCD_PARAM_PIN_BIT_3,
							RaspiPin.GPIO_04),
					properties.getGpioPinFor(LcdParameter.LCD_PARAM_PIN_BIT_4,
							RaspiPin.GPIO_01), curData);

			// Load custom refresh time from properties file
			lcd.setCustomRefreshTime(properties.getIntProperties(
					LcdParameter.LCD_PARAM_REFRESH_TIME,
					LcdManager.LCD_DEFAULT_REFRESH_TIME));		
			
			try {
				lcd.lcdWrite("F1telemetryonRPI", 0);
				lcd.lcdWrite("Version : " + getClass().getPackage().getImplementationVersion(), 1);
				Thread.sleep(2000);
				lcd.lcdWrite("Author :", 0);
				lcd.lcdWrite("Benoit Mouquet", 1);
				Thread.sleep(1000);
				lcd.lcdWrite("Website :", 0);
				lcd.lcdWrite("code4pi.fr", 1);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Execute when leave application (ctrl + c)
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				// Notify that we close the application
				closureInProgress = true;
				// Close the socket if it is open
				if (localDatagramSocket != null
						&& !localDatagramSocket.isClosed()) {
					localDatagramSocket.close();
				}
				// End all thread
				if (ledStatus) {
					led.finishThread();
				}
				if (lcdStatus) {
					lcd.finishThread();
				}
			}
		});

		byte[] arrayOfByte = new byte[256];
		try {
			localDatagramSocket = new DatagramSocket(20777);
			if (lcdStatus) {
				lcd.startRefresh();
			}
			System.out.println("Ok");
			// Infinity loop
			while (true) {
				DatagramPacket localDatagramPacket = new DatagramPacket(
						arrayOfByte, arrayOfByte.length);
				try {
					// Get datagram packet
					localDatagramSocket.receive(localDatagramPacket);
					// Get data
					byte[] localObject1 = localDatagramPacket.getData();
					// Extract double from data
					double[] datas = (double[]) Utils
							.byteToDouble(localObject1);

					// Update the F1 data object
					curData.updateDataWithDoubleArray(datas);

					if (ledStatus) {
						// Update led status
						led.updateLed(curData.getEngineRpm());
					}
				} catch (Exception e) {
					// Display error message only if we not leave the
					// application
					if (!closureInProgress) {
						e.printStackTrace();
					}
				}
			}

		} catch (SocketException localSocketException1) {
			System.err
					.println("Cannot create socket. Are you trying to run two instances listening to the same port?");
			System.exit(1);
		}

	}

}
