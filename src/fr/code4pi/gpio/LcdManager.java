package fr.code4pi.gpio;

import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.Pin;

import fr.code4pi.telemetry.F1Data;
import fr.code4pi.tools.Utils;

public class LcdManager {

	private GpioLcdDisplay lcd;
	private char[][] lcdString;
	private F1Data f1data;
	private Thread screenThread;
	private boolean stopThread;

	public final static int LCD_ROWS = 2;
	public final static int LCD_COLUMNS = 16;
	public final static int LCD_REFRESH_TIME = 200;

	public LcdManager(Pin rsPin, Pin strobePin, Pin dataBit1, Pin dataBit2,
			Pin dataBit3, Pin dataBit4, F1Data data) {
		lcd = new GpioLcdDisplay(LCD_ROWS, LCD_COLUMNS, rsPin, // LCD RS pin
				strobePin, // LCD strobe pin
				dataBit1, // LCD data bit 1
				dataBit2, // LCD data bit 2
				dataBit3, // LCD data bit 3
				dataBit4); // LCD data bit 4
		lcdString = new char[2][16];
		this.f1data = data;

		screenThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!stopThread) {
					try {
						lcdWrite(
								f1data.getSpeed() + "km/h " + " G : "
										+ f1data.getGear(), 0);
						lcdWrite(Utils.doubleToTime(f1data.getTime()) + " "
								+ "P : " + f1data.getPosition(), 1);
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		});
	}
	
	public void startRefresh()
	{
		screenThread.start();
	}

	public void lcdWrite(String string) {
		for (int i = 0; i < string.length() && i < 32; i++) {
			if (string.charAt(i) != lcdString[(int) (i / 16)][i % 16]) {
				lcdString[(int) (i / 16)][i % 16] = string.charAt(i);
				lcd.write((int) (i / 16), i % 16, string.charAt(i));
			}
		}
		if (string.length() < 32) {
			for (int i = string.length(); i < 32; i++) {
				if (lcdString[(int) (i / 16)][i % 16] != '\u0000') {
					lcdString[(int) (i / 16)][i % 16] = '\u0000';
					lcd.write((int) (i / 16), i % 16, ' ');
				}
			}
		}
	}

	public void lcdWrite(String string, int row) {
		for (int i = 0; i < string.length() && i < 16; i++) {
			if (string.charAt(i) != lcdString[row][i]) {
				lcdString[row][i] = string.charAt(i);
				lcd.write(row, i, string.charAt(i));
			}
		}
		if (string.length() < 16) {
			for (int i = string.length(); i < 16; i++) {
				if (lcdString[row][i] != '\u0000') {
					lcdString[row][i] = '\u0000';
					lcd.write(row, i, ' ');
				}
			}
		}
	}
	
	public void finishThread()
	{
		this.stopThread = true;
		try {
			screenThread.join();
			System.out.println("Fermeture thread LCD");
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		lcd.clear();
	}
}
