//import edu.wpi.first.wpilibj.Joystick;

public class DriveMain {

	public static void main(String[] args) {

		//Joystick stick = new Joystick(0);

		SwerveDrive d1 = new SwerveDrive(36, 24);
		//while (stick.getTrigger() == true) {
			d1.updateSwerve(.5, -.2, 1); // for,strafe,rotCW
		//}
	}
}