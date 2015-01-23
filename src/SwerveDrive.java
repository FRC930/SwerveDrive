import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveDrive {

	/**
	 * Team 930 SwerveDrive
	 * 
	 * Heading of 0 degrees is straight forward to the robot. Interface for
	 * other objects are the INTERFACE METHODS, and private calculation methods
	 * are the CALC METHODS.
	 * 
	 */

	double width, length;

	private double strafe, forward, rotationCW, Rx, Ry;
	private double topX, bottomX, rightY, leftY;
	private double topRightSpeed, topLeftSpeed, bottomLeftSpeed,
			bottomRightSpeed;
	private double topRightAngle, topLeftAngle, bottomLeftAngle,
			bottomRightAngle;

	// CONSTRUCTOR

	public SwerveDrive(double length, double width, SpeedController TR,
			SpeedController TL, SpeedController BL, SpeedController BR) {
		this.width = width;
		this.length = length;
	}

	// INTERFACE METHODS
	public void updateSwerve(double forwardIn, double strafeIn,
			double rotationIn) {
		forward = forwardIn;
		strafe = strafeIn;
		rotationCW = rotationIn;// * 2 * Math.PI;

		this.Calc();
		// System.out output
		// System.out.println(topRightSpeed + "\n" + topLeftSpeed + "\n"
		// + bottomLeftSpeed + "\n" + bottomRightSpeed + "\n");
		// System.out.println(topRightAngle + "\n" + topLeftAngle + "\n"
		// + bottomLeftAngle + "\n" + bottomRightAngle + "\n");

		// SmartDashboard output
		SmartDashboard.putNumber("Top Right Speed", topRightSpeed);
		SmartDashboard.putNumber("Top Left Speed", topRightSpeed);
		SmartDashboard.putNumber("Bottom Left Speed", topRightSpeed);
		SmartDashboard.putNumber("Bottom Right Speed", topRightSpeed);

		SmartDashboard.putNumber("Top Right Angle", topRightAngle);
		SmartDashboard.putNumber("Top Left Angle", topRightAngle);
		SmartDashboard.putNumber("Bottom Left Angle", topRightAngle);
		SmartDashboard.putNumber("Bottom Right Angle", topRightAngle);
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

	// CALC METHODS
	private void normalizeTranslation() {
		if (Math.pow(forward, 2) + Math.pow(strafe, 2) > 1) {
			forward = Math.sqrt(Math.pow(forward, 2)
					/ (Math.pow(forward, 2) + Math.pow(strafe, 2)));
			strafe = Math.sqrt(Math.pow(strafe, 2)
					/ (Math.pow(forward, 2) + Math.pow(strafe, 2)));
		}

		// double max;
		// max = topRightSpeed;
		// if (topLeftSpeed > max) {
		// max = topLeftSpeed;
		// }
		// if (bottomLeftSpeed > max) {
		// max = bottomLeftSpeed;
		// }
		// if (bottomRightSpeed > max) {
		// max = bottomRightSpeed;
		// }
		// if (max > 1) {
		// topRightSpeed /= max;
		// topLeftSpeed /= max;
		// bottomLeftSpeed /= max;
		// bottomRightSpeed /= max;
		// }
	}

	private void setRotationVectors() {
		Rx = rotationCW * LENGTH / 2;
		Ry = rotationCW * WIDTH / 2;
	}

	private void setIntermediates() {
		topX = strafe + Rx;
		rightY = forward - Ry;
		bottomX = strafe - Rx;
		leftY = strafe + Ry;

		System.out.println(bottomX + "\n" + topX + "\n" + rightY + "\n" + leftY
				+ "\n");
	}

	private void setWheelSpeeds() {
		topRightSpeed = Math.sqrt(Math.pow(topX, 2) + Math.pow(rightY, 2));
		topLeftSpeed = Math.sqrt(Math.pow(topX, 2) + Math.pow(leftY, 2));
		bottomLeftSpeed = Math.sqrt(Math.pow(bottomX, 2) + Math.pow(leftY, 2));
		bottomRightSpeed = Math
				.sqrt(Math.pow(bottomX, 2) + Math.pow(rightY, 2));

	}

	private void setWheelAngles() {
		topRightAngle = 90 - Math.atan2(topX, rightY) * 180 / Math.PI;
		topLeftAngle = 90 - Math.atan2(topX, leftY) * 180 / Math.PI;
		bottomLeftAngle = 90 - Math.atan2(bottomX, leftY) * 180 / Math.PI;
		bottomRightAngle = 90 - Math.atan2(bottomX, rightY) * 180 / Math.PI;
	}

	private void Calc() {
		this.normalizeTranslation();
		this.setRotationVectors();
		this.setIntermediates();
		this.setWheelSpeeds();
		this.setWheelAngles();
	}
}