package fr.code4pi;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.pi4j.io.gpio.RaspiPin;

import fr.code4pi.gpio.LcdManager;
import fr.code4pi.gpio.LedManager;
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
	private F1Data curData;
	private DatagramSocket localDatagramSocket;

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

		F1TelemetryProperties properties = new F1TelemetryProperties(
				"config.properties");

		led = new LedManager(properties.getGpioPinFor(
				LedManager.LED_PIN_FIRST_LEDS, RaspiPin.GPIO_07),
				properties.getGpioPinFor(LedManager.LED_PIN_SECOND_LEDS,
						RaspiPin.GPIO_09), properties.getGpioPinFor(
						LedManager.LED_PIN_THIRD_LEDS, RaspiPin.GPIO_08));

		led.setCustomLimit(properties.getIntProperties("rpm_for_pin_1",
				LedManager.RPM_FOR_PIN1), properties.getIntProperties(
				"rpm_for_pin_2", LedManager.RPM_FOR_PIN2), properties
				.getIntProperties("rpm_for_pin_3", LedManager.RPM_FOR_PIN3),
				properties.getIntProperties("rpm_for_blinking",
						LedManager.RPM_FOR_BLINKING));

		curData = new F1Data();

		lcd = new LcdManager(properties.getGpioPinFor(LcdManager.LCD_PIN_RS,
				RaspiPin.GPIO_11), properties.getGpioPinFor(
				LcdManager.LCD_PIN_STROBE, RaspiPin.GPIO_10),
				properties.getGpioPinFor(LcdManager.LCD_PIN_BIT_1,
						RaspiPin.GPIO_06), properties.getGpioPinFor(
						LcdManager.LCD_PIN_BIT_2, RaspiPin.GPIO_05),
				properties.getGpioPinFor(LcdManager.LCD_PIN_BIT_3,
						RaspiPin.GPIO_04), properties.getGpioPinFor(
						LcdManager.LCD_PIN_BIT_4, RaspiPin.GPIO_01), curData);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (localDatagramSocket != null
						&& !localDatagramSocket.isClosed()) {
					localDatagramSocket.close();
				}
				led.finishThread();
				lcd.finishThread();
			}
		});

		byte[] arrayOfByte = new byte[256];
		try {
			localDatagramSocket = new DatagramSocket(20777);
			lcd.startRefresh();
			System.out.println("Ok");
			while (true) {
				DatagramPacket localDatagramPacket = new DatagramPacket(
						arrayOfByte, arrayOfByte.length);
				try {
					localDatagramSocket.receive(localDatagramPacket);
					byte[] localObject1 = localDatagramPacket.getData();
					double[] datas = (double[]) Utils
							.byteToDouble(localObject1);

					curData.updateDataWithDoubleArray(datas);

					led.updateLed(curData.getEngineRpm());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (SocketException localSocketException1) {
			System.err
					.println("Cannot create socket. Are you trying to run two instances listening to the same port?");
			System.exit(1);
		}

	}

}
