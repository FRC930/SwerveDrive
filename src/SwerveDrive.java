//import edu.wpi.first.wpilibj.SpeedController;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

public class SwerveDrive {

	/*
	 * Team 930 SwerveDrive
	 * 
	 * Heading of 0 degrees is straight forward to the robot. Interface for
	 * other objects are the INTERFACE METHODS, and private calculation methods
	 * are the CALC METHODS.
	 * 
	 * For the field centric utilization of the code, you must calibrate the
	 * robot so that it faces perpendicular to the baseline of the field.
	 * Heading zero is that way
	 */

	// DECLARATIONS OF VARIABLES AND OTHER THINGS THE CODE MIGHT FIND USEFUL
	final double UPDATE_TIME = .005; // time between updates to the robot, in
										// seconds
	double timeSinceLastUpdate; // time since the last update, in seconds

	// SpeedController TR, TL, BR, BL; - if possible to implement

	private boolean isFieldcentric; // are we doin' field centric calculations?
	Joystick translationStick, headingStick; // field centric sticks to control
												// the robot
	private double width, length; // length and width of the robot

	private double strafe, forward, rotationCW, Rx, Ry; // basic input variables
	private double topX, bottomX, rightY, leftY; // intermediate values for the
													// wheel velocity components

	private double topRightSpeed, topLeftSpeed, bottomLeftSpeed,
			bottomRightSpeed; // speed variables for the wheels
	private double topRightAngle, topLeftAngle, bottomLeftAngle,
			bottomRightAngle; // angle variables for the wheels

	private double heading, lastHeading;

	// CONSTRUCTOR - eventually get them all to accept passed speedcontrollers
	public SwerveDrive(double length, double width) {
		this.width = width;
		this.length = length;
		this.isFieldcentric = false;
		this.lastHeading = 0;
		this.timeSinceLastUpdate = UPDATE_TIME;

	}

	public SwerveDrive(double length, double width, boolean fieldcentric) {
		this.width = width;
		this.length = length;
		this.isFieldcentric = fieldcentric;
		this.lastHeading = 0;
		this.timeSinceLastUpdate = UPDATE_TIME;

	}

	// INTERFACE METHODS
	// update swerve for raw robocentic values
	public void updateSwerve(double forwardIn, double strafeIn,
			double rotationCWIn) {

		this.forward = forwardIn;
		this.strafe = strafeIn;
		this.rotationCW = rotationCWIn;

		this.calc();
		this.output();

	}

	// update swerve for field centric from joysticks
	public void updateSwerve(Joystick translationStickIn,
			Joystick headingStickIn) {

		// long cycleStart = System.currentTimeMillis();
		// while(timeSinceLastUpdate < UPDATE_TIME){ timeSinceLast = }

		this.translationStick = translationStickIn;
		this.headingStick = headingStickIn;

		this.calc();
		this.output();

	}

	public void setStrafe(double strafeIn) {
		strafe = strafeIn;
	}

	public void setForward(double forwardIn) {
		forward = forwardIn;
	}

	public void setRotation(double rotationIn) { // rotation measured clockwise
													// and from North
		rotationCW = 2 * Math.PI * rotationIn;
	}

	public void switchCentricity() { // switch from robo to field and vicea
										// versa while in code
		isFieldcentric = !isFieldcentric;
	}

	// CALC METHODS
	private void convertFieldToRobocentric() {

		// calculate the current heading
		heading = 90 - Math.atan2(headingStick.getX(), headingStick.getY())
				* 180 / Math.PI;

		// defining the velocity vector in polar to convert to robocentric
		double magnitudeV = Math.sqrt(Math.pow(translationStick.getX(), 2)
				+ Math.pow(translationStick.getY(), 2));
		double phiV = 90
				- Math.atan2(translationStick.getX(), translationStick.getY())
				* 180 / Math.PI;

		// do the conversion so the robocentic code can handle the rest
		strafe = magnitudeV * Math.sin(phiV - lastHeading);
		forward = magnitudeV * Math.cos(phiV - lastHeading);
		rotationCW = (heading - lastHeading) / UPDATE_TIME;

		lastHeading = heading;
	}

	private void normalizeTranslation() {
		if (Math.pow(forward, 2) + Math.pow(strafe, 2) > 1) {
			forward = Math.sqrt(Math.pow(forward, 2)
					/ (Math.pow(forward, 2) + Math.pow(strafe, 2)));
			strafe = Math.sqrt(Math.pow(strafe, 2)
					/ (Math.pow(forward, 2) + Math.pow(strafe, 2)));
		}
	}

	private void setRotationVectors() {
		Rx = rotationCW * length / 2;
		Ry = rotationCW * width / 2;
	}

	private void setIntermediates() {
		topX = strafe + Rx;
		rightY = forward - Ry;
		bottomX = strafe - Rx;
		leftY = strafe + Ry;

	}

	private void setWheelSpeeds() {
		topRightSpeed = Math.sqrt(Math.pow(topX, 2) + Math.pow(rightY, 2));
		topLeftSpeed = Math.sqrt(Math.pow(topX, 2) + Math.pow(leftY, 2));
		bottomLeftSpeed = Math.sqrt(Math.pow(bottomX, 2) + Math.pow(leftY, 2));
		bottomRightSpeed = Math
				.sqrt(Math.pow(bottomX, 2) + Math.pow(rightY, 2));

		double max;
		max = topRightSpeed;
		if (topLeftSpeed > max) {
			max = topLeftSpeed;
		}
		if (bottomLeftSpeed > max) {
			max = bottomLeftSpeed;
		}
		if (bottomRightSpeed > max) {
			max = bottomRightSpeed;
		}
		if (max > 1) {
			topRightSpeed /= max;
			topLeftSpeed /= max;
			bottomLeftSpeed /= max;
			bottomRightSpeed /= max;
		}
	}

	private void setWheelAngles() {
		topRightAngle = 90 - Math.atan2(topX, rightY) * 180 / Math.PI;
		topLeftAngle = 90 - Math.atan2(topX, leftY) * 180 / Math.PI;
		bottomLeftAngle = 90 - Math.atan2(bottomX, leftY) * 180 / Math.PI;
		bottomRightAngle = 90 - Math.atan2(bottomX, rightY) * 180 / Math.PI;
	}

	private void calc() {
		if (isFieldcentric == true) {
			this.convertFieldToRobocentric();
		}
		this.normalizeTranslation();
		this.setRotationVectors();
		this.setIntermediates();
		this.setWheelSpeeds();
		this.setWheelAngles();
	}

	private void output() {
		// System.out output
		System.out.println(topRightSpeed + "\n" + topLeftSpeed + "\n"
				+ bottomLeftSpeed + "\n" + bottomRightSpeed + "\n");
		System.out.println(topRightAngle + "\n" + topLeftAngle + "\n"
				+ bottomLeftAngle + "\n" + bottomRightAngle + "\n");

		// SmartDashboard output
		// SmartDashboard.putNumber("Top Right Speed", topRightSpeed);
		// SmartDashboard.putNumber("Top Left Speed", topRightSpeed);
		// SmartDashboard.putNumber("Bottom Left Speed", topRightSpeed);
		// SmartDashboard.putNumber("Bottom Right Speed", topRightSpeed);
		//
		// SmartDashboard.putNumber("Top Right Angle", topRightAngle);
		// SmartDashboard.putNumber("Top Left Angle", topRightAngle);
		// SmartDashboard.putNumber("Bottom Left Angle", topRightAngle);
		// SmartDashboard.putNumber("Bottom Right Angle", topRightAngle);

		// Wheel Output
		// TR.set(topRightSpeed);
		// TL.set(topLeftSpeed);
		// BL.set(bottomLeftSpeed);
		// BR.set(bottomRightSpeed);
		//
		//
	}
}