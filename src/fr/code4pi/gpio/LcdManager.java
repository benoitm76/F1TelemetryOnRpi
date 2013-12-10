package fr.code4pi.gpio;

import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.Pin;

import fr.code4pi.telemetry.F1Data;
import fr.code4pi.tools.Utils;

/**
 * Class to manage lcd.
 * 
 * @author Benoit Mouquet
 * 
 */
public class LcdManager {

	private GpioLcdDisplay lcd;
	private char[][] lcdString;
	private F1Data f1data;
	private Thread screenThread;
	private boolean stopThread;

	/**
	 * Number of rows of the lcd.
	 */
	public final static int LCD_ROWS = 2;
	/**
	 * Number of columns of the lcd.
	 */
	public final static int LCD_COLUMNS = 16;
	/**
	 * Refresh time of information of lcd.
	 */
	public final static int LCD_REFRESH_TIME = 200;

	/**
	 * Constructor of lcd manager.
	 * 
	 * @param rsPin
	 *            RS pin of lcd.
	 * @param strobePin
	 *            Strobe pin of lcd.
	 * @param dataBit1
	 *            Pin bit 1 data of lcd.
	 * @param dataBit2
	 *            Pin bit 2 data of lcd.
	 * @param dataBit3
	 *            Pin bit 3 data of lcd.
	 * @param dataBit4
	 *            Pin bit 4 data of lcd.
	 * @param data
	 *            Reference of f1 data.
	 */
	public LcdManager(Pin rsPin, Pin strobePin, Pin dataBit1, Pin dataBit2,
			Pin dataBit3, Pin dataBit4, F1Data data) {
		lcd = new GpioLcdDisplay(LCD_ROWS, LCD_COLUMNS, rsPin, strobePin,
				dataBit1, dataBit2, dataBit3, dataBit4);
		lcdString = new char[LCD_ROWS][LCD_COLUMNS];
		this.f1data = data;
	}

	/**
	 * Start screen refresh.
	 */
	public void startRefresh() {
		if (screenThread == null || !screenThread.isAlive()) {
			screenThread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (!stopThread) {
						try {
							lcdWrite(f1data.getSpeed() + "km/h " + " G : "
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
			screenThread.start();
		}
	}

	/**
	 * Write data on lcd without clear of screen.
	 * 
	 * @param string
	 *            String to show on screen
	 */
	public void lcdWrite(String string) {
		for (int i = 0; i < string.length() && i < LCD_COLUMNS * LCD_ROWS; i++) {
			if (string.charAt(i) != lcdString[(int) (i / LCD_COLUMNS)][i
					% LCD_COLUMNS]) {
				lcdString[(int) (i / LCD_COLUMNS)][i % LCD_COLUMNS] = string
						.charAt(i);
				lcd.write((int) (i / LCD_COLUMNS), i % LCD_COLUMNS,
						string.charAt(i));
			}
		}
		if (string.length() < LCD_COLUMNS * LCD_ROWS) {
			for (int i = string.length(); i < LCD_COLUMNS * LCD_ROWS; i++) {
				if (lcdString[(int) (i / LCD_COLUMNS)][i % LCD_COLUMNS] != '\u0000') {
					lcdString[(int) (i / LCD_COLUMNS)][i % LCD_COLUMNS] = '\u0000';
					lcd.write((int) (i / LCD_COLUMNS), i % LCD_COLUMNS, ' ');
				}
			}
		}
	}

	/**
	 * Write data a row on the lcd without clear of screen.
	 * 
	 * @param string
	 *            String to show on screen
	 * @param row
	 *            Row number
	 */
	public void lcdWrite(String string, int row) {
		if (row < LCD_ROWS) {
			for (int i = 0; i < string.length() && i < LCD_COLUMNS; i++) {
				if (string.charAt(i) != lcdString[row][i]) {
					lcdString[row][i] = string.charAt(i);
					lcd.write(row, i, string.charAt(i));
				}
			}
			if (string.length() < LCD_COLUMNS) {
				for (int i = string.length(); i < LCD_COLUMNS; i++) {
					if (lcdString[row][i] != '\u0000') {
						lcdString[row][i] = '\u0000';
						lcd.write(row, i, ' ');
					}
				}
			}
		}
	}

	/**
	 * Stop screen refresh at the end.
	 */
	public void finishThread() {
		if (screenThread != null && screenThread.isAlive()) {
			this.stopThread = true;
			try {
				screenThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lcd.clear();
		}
	}
}
