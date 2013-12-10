package fr.code4pi.telemetry;

/**
 * Class to store useful data from the game
 * 
 * @author Benoit Mouquet
 *
 */
public class F1Data {
	
	private int gear;
	private int speed;
	private double time;
	private double engineRpm;
	private int position;
	
	/**
	 * Update data with array of double from the game.
	 * 
	 * @param arrayOfDouble The array of double
	 */
	public void updateDataWithDoubleArray(double[] arrayOfDouble)
	{
		engineRpm = arrayOfDouble[37];
		speed = (int) (arrayOfDouble[7] * 3.6); // Multiply by 3.6 to get in km/h
		gear = (int) arrayOfDouble[33];
		time = arrayOfDouble[1];
		position = (int) arrayOfDouble[39];
	}

	/**
	 * @return the gear
	 */
	public int getGear() {
		return gear;
	}

	/**
	 * @param gear the gear to set
	 */
	public void setGear(int gear) {
		this.gear = gear;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * @return the time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(double time) {
		this.time = time;
	}

	/**
	 * @return the engineRpm
	 */
	public double getEngineRpm() {
		return engineRpm;
	}

	/**
	 * @param engineRpm the engineRpm to set
	 */
	public void setEngineRpm(double engineRpm) {
		this.engineRpm = engineRpm;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}
}
