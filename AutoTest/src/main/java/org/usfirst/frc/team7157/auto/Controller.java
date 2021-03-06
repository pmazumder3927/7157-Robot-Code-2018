
package org.usfirst.frc.team7157.auto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;

/**
 * This class stores the int sent back from the Driver Station and uses it to check for rising or falling edges
 */
public class Controller extends Joystick {

	public static class Xbox {
		public static int A = 1;
		public static int B = 2;
		public static int X = 3;
		public static int Y = 4;
		public static int LeftBumper = 5;
		public static int RightBumper = 6;
		public static int Back = 7;
		public static int Start = 8;
		public static int LeftClick = 9;
		public static int RightClick = 10;

		public static int LeftX = 0;
		public static int LeftY = 1;
		public static int LeftTrigger = 2;
		public static int RightTrigger = 3;
		public static int RightX = 4;
		public static int RightY = 5;
	}

	/*
	 * The Driver Station sends back an int(32 bits) for buttons
	 * Shifting 1 left (button - 1) times and ANDing it with
	 * int sent from the Driver Station will either give you
	 * 0 or a number not zero if it is true
	 */
	private int oldButtons;
	private int currentButtons;
	private int axisCount, povCount;
	private double[] oldAxis;
	private double[] currentAxis;
	private int[] oldPOV;
	private int[] currentPOV;
	private boolean record, play;
	private InputRecorder recorder;
	private InputPlayer player;
	private String _suffix = "";

	public Controller(int port) {
		super(port);
		axisCount = DriverStation.getInstance().getStickAxisCount(port);
		povCount = DriverStation.getInstance().getStickPOVCount(port);
		oldAxis = new double[axisCount];
		currentAxis = new double[axisCount];
		oldPOV = new int[povCount];
		currentPOV = new int[povCount];
		record = false;
		play = false;
		recorder = null;
		player = null;
	}

	/**
	 * Only works if update() is called in each iteration
	 *
	 * @param button
	 *            Joystick button ID
	 * @return
	 * 		Falling edge state of the button
	 */
	public boolean getFallingEdge(int button) {
		boolean oldVal = getButtonState(button, oldButtons);
		boolean currentVal = getButtonState(button, currentButtons);
		if (oldVal == true && currentVal == false) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Only works if update() is called in each iteration
	 *
	 * @param button
	 *            Joystick button ID
	 * @return
	 * 		Rising edge state of the button
	 */
	public boolean getRisingEdge(int button) {
		boolean oldVal = getButtonState(button, oldButtons);
		boolean currentVal = getButtonState(button, currentButtons);
		if (oldVal == false && currentVal == true) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getRisingEdge(int axis, double threshold) {
		if (axis <= axisCount) {
			boolean oldVal = oldAxis[axis] > threshold;
			boolean currentVal = currentAxis[axis] > threshold;
			if (oldVal == false && currentVal == true) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public boolean getFallingEdge(int axis, double threshold) {
		if (axis <= axisCount) {
			boolean oldVal = oldAxis[axis] > threshold;
			boolean currentVal = currentAxis[axis] > threshold;
			if (oldVal == true && currentVal == false) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * This method needs to be called for each iteration of the teleop loop
	 */
	public void update() {
		if (play) {
			if (player.update() != 0) {
				stopPlay();
				return;
			}
			currentButtons = player.getButtons();
			currentAxis = player.getAxis();
			currentPOV = player.getPOV();
		} else {
			oldButtons = currentButtons;
			currentButtons = DriverStation.getInstance().getStickButtons(getPort());
			if (axisCount != DriverStation.getInstance().getStickAxisCount(getPort())) {
				axisCount = DriverStation.getInstance().getStickAxisCount(getPort());
				oldAxis = new double[axisCount];
				currentAxis = new double[axisCount];
			}
			if (povCount != DriverStation.getInstance().getStickPOVCount(getPort())) {
				povCount = DriverStation.getInstance().getStickPOVCount(getPort());
				oldPOV = new int[povCount];
				currentPOV = new int[povCount];
			}
			oldAxis = currentAxis;
			for (int i = 0; i < axisCount; i++) {
				currentAxis[i] = DriverStation.getInstance().getStickAxis(getPort(), i);
			}

			oldPOV = currentPOV;
			for (int i = 0; i < povCount; i++) {
				currentPOV[i] = DriverStation.getInstance().getStickPOV(getPort(), i);
			}
			if (record) {
				recorder.record(currentButtons, currentAxis, currentPOV);
			}
		}
	}

	public void setRecord(String filename) {
		record = true;
		recorder = new InputRecorder(filename + _suffix);
	}

	public void stopRecord() {
		record = false;
		recorder.stop();
	}

	public void setPlay(String filename) {
		play = true;
		player = new InputPlayer(filename + _suffix);
	}

	public void stopPlay() {
		play = false;
	}

	public static class InputPlayer {

		private Scanner scanner;
		private int buttons;
		private double[] axis;
		private int[] POV;

		public InputPlayer(String filename) {
			try {
				scanner = new Scanner(new File("/home/lvuser/" + filename + ".csv"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			}
			scanner.useDelimiter(",|\\n");
		}

		public int update() {
			if (scanner.hasNext()) {
				buttons = scanner.nextInt();
				int axisLength = scanner.nextInt();
				axis = new double[axisLength];
				for (int i = 0; i < axisLength; i++) {
					axis[i] = scanner.nextDouble();
				}
				int povLength = scanner.nextInt();
				POV = new int[povLength];
				for (int i = 0; i < povLength; i++) {
					POV[i] = scanner.nextInt();
				}
			} else {
				return -1;
			}
			return 0;
		}

		public int getButtons() {
			return buttons;
		}

		public double[] getAxis() {
			return axis;
		}

		public int[] getPOV() {
			return POV;
		}

	}

	public static class InputRecorder {

		private FileWriter writer;

		public InputRecorder(String filename) {
			try {
				writer = new FileWriter("/home/lvuser/" + filename + ".csv");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void record(int currentButtons, double[] currentAxis, int[] currentPOV) {
			try {
				String buttons = String.valueOf(currentButtons);
				writer.append(buttons);
				writer.append("," + String.valueOf(currentAxis.length));
				for (int i = 0; i < currentAxis.length; i++) {
					writer.append("," + String.valueOf(currentAxis[i]));
				}
				writer.append("," + String.valueOf(currentPOV.length));
				for (int i = 0; i < currentPOV.length; i++) {
					writer.append("," + String.valueOf(currentPOV[i]));
				}
				writer.append("\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void stop() {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean getRawButton(int button) {
		return getButtonState(button, currentButtons);
	}

	@Override
	public double getRawAxis(int axis) {
		if (axis < axisCount && axis > -1) {
			return currentAxis[axis];
		}
		return 0;
	}

	@Override
	public int getPOV(int pov) {
		if (pov < povCount && pov > -1) {
			return currentPOV[pov];
		}
		return -1;
	}
	
	public boolean getButtonState(int button, int state) {
		return ((0x1 << (button - 1)) & state) != 0;
	}
	
	public void setSuffix(String suffix) {
		_suffix = suffix;
	}
}