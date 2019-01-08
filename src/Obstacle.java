import java.util.Random;

public class Obstacle {

	private EZImage obstaclePicture;
	private EZGroup group;
	private int x, y;
	private int speed;
	private int maxScreenX, maxScreenY;

	// Constructor for obstacle.
	public Obstacle(String filename, int maxX, int maxY) {
		group = EZ.addGroup();
		
		obstaclePicture = EZ.addImage(filename, 0, 0);
		obstaclePicture.scaleBy(0.2);
		
		group.addElement(obstaclePicture);
		
		maxScreenX = maxY;
		maxScreenY = 700;
		rando();
	}

	// Initialize the obstacle by randomizing its stating position on the screen.
	public void rando() {
		Random randomGenerator = new Random();
		
		int spd = 10;
		speed = spd;
		
		// Generate a random number using that multiple to calculate the position on the screen.
		int ranY = randomGenerator.nextInt(1100)+450;
		setPosition(2000, ranY);
	}

	// Accessor function to get the X and Y position of the obstacle.
	public int getX() {
		return group.getXCenter();
	}

	public int getY() {
		return group.getYCenter();
	}

	// Set the position of the obstacle.
	public void setPosition(int posx, int posy) {
		x = posx;
		y = posy;
		setObstacleImagePosition(x, y);
	}

	// Set the image position of the obstacle.
	private void setObstacleImagePosition(int posx, int posy) {
		group.translateTo(posx, posy);
	}

	// Move the obstacle.
	public void move() {
		x = x - speed;
		
		setPosition(x, y);
		if (x < -100) {
			rando();
		}
	}

	// Check to see if a point is inside an obstacle.
	public boolean isInside(int posx, int posy) {
		return group.isPointInElement(posx, posy);
	}
}