//import edu.wpi.first.wpilibj.Joystick;

public class DriveMain {

	public static void main(String[] args) {

		//Joystick stick = new Joystick(0);

		SwerveDrive d1 = new SwerveDrive(36, 24);
		
		long last = System.currentTimeMillis();
		for (int t = 0; t < 1000; t++) {
			d1.updateSwerve(.5, -.2, 1); // for,strafe,rotCW
		}
		long diff = (System.currentTimeMillis() - last);
		System.out.println("\n step = " + diff);
	}
}