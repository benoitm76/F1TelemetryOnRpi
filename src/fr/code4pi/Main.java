package fr.code4pi;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.pi4j.io.gpio.RaspiPin;

import fr.code4pi.gpio.LcdManager;
import fr.code4pi.gpio.LedManager;
import fr.code4pi.telemetry.F1Data;
import fr.code4pi.tools.Utils;

public class Main {

	private LedManager led;
	private LcdManager lcd;
	private F1Data curData;
	private DatagramSocket localDatagramSocket;

	public static void main(String[] args) {
		new Main();

	}

	public Main() {

		led = new LedManager(RaspiPin.GPIO_07, RaspiPin.GPIO_09,
				RaspiPin.GPIO_08);

		curData = new F1Data();

		lcd = new LcdManager(RaspiPin.GPIO_11, // LCD RS pin
				RaspiPin.GPIO_10, // LCD strobe pin
				RaspiPin.GPIO_06, // LCD data bit 1
				RaspiPin.GPIO_05, // LCD data bit 2
				RaspiPin.GPIO_04, // LCD data bit 3
				RaspiPin.GPIO_01, curData); // LCD data bit 4

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