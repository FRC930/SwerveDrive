import edu.wpi.first.wpilibj.Joystick;

public class DriveMain {

	public static void main(String[] args) {
	
		Joystick stick = new Joystick(0);
		
		SwerveDrive d1 = new SwerveDrive();
		d1.updateSwerve(stick.getY(), stick.getX(), stick.getZ()); //for,strafe,rotCW
	}
}