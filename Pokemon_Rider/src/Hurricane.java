import java.util.Random;

public class Hurricane {

	private int numFrames;		// Keeps track of how many frames are in the animation sequence
	private long duration;		// duration to play the animation over (in milliseconds)
	private long starttime;		// keep track of starting time of animation
	private int x, y;
	private int speed;
	
	private boolean loopIt;		// determine whether animation should loop or not
	private boolean starting;	// Flag to indicate that you are starting animation from frame zero
	private boolean stopped;	// Flag to indicate if the animation has stopped.
	private boolean visibility;	// Flag to determine if the images should be visible or not
	
	private EZImage[] frames;	// Hold all the animation frames
	private EZGroup group;		// Holds the EZGroup
	
	// Constructor accepts the following parameters
	// filename - contains an array of filenames of images to read.
	// dur - duration over which animation frames should play.
	// posX, posY - location of the animated object.
	Hurricane(String[] filenames, long dur, int posX, int posY) {
		duration = dur;
		
		// Make an EZgroup to gather up all the individual EZimages.
		group = EZ.addGroup();
		numFrames = filenames.length;
		
		// Make an array to hold the EZImages
		frames = new EZImage[numFrames];
		
		// Load each image
		for(int i = 0; i < numFrames; i++){
			frames[i] = EZ.addImage(filenames[i], 0, 0);

			frames[i].hide();
			group.addElement(frames[i]);
		}
		
		setLoop(true);
		starting = true;
		stopped = false;
		visibility = true;
		rando();
	}
	public void rando() {
		Random randomGenerator = new Random();
		
		// Generate a random number using that multiple to calculate the position on the screen.
		int ranY = randomGenerator.nextInt(1000)+450;
		setPosition(2000, ranY);

		speed = randomGenerator.nextInt(15) + 1;
	}
	
	public int getX() {
		return group.getXCenter();
	}

	public int getY() {
		return group.getYCenter();
	}

	// Set the position of the ship
	public void setPosition(int posx, int posy) {
		x = posx;
		y = posy;
		setPersonImagePosition(x, y);
	}

	private void setPersonImagePosition(int posx, int posy) {
		group.translateTo(posx, posy);
	}

	public void move() {
		x = x - speed;
		setPosition(x, y);
		if (x < - 100) {
			rando();
		}
	}

	public float getXCenter() {
		return group.getXCenter();
	}

	public float getYCenter() {
		return group.getYCenter();
	}
	
	public void moveback(int step) {
		x = x + step;
		setPersonImagePosition(x,y);
		
	}

	void translateTo(int posX, int posY) {
		group.translateTo(posX,posY);
}

	void rotateTo(double angle){
		group.rotateTo(angle);
	}
	
	void scaleTo(double scale){
		group.scaleTo(scale);
	}
	void setLoop(boolean loop){
		loopIt = loop;
	}
	void restart(){
		starting = true;
	}
	
	void stop(){
		stopped = true;
	}
	
	// Hide each animation frame
	void hide(){
		visibility = false;
		for(int i =0; i < numFrames; i++) frames[i].hide();
	}
	
	void show() {
		visibility = true;
	}
	boolean go(){
		if (stopped) return false;
		
		// If the animation is starting for the first time save the starttime
		if (starting){
			starttime = System.currentTimeMillis();
			starting = false;
		}

		// If the duration for the animation is exceeded and if looping is enabled
		// then restart the animation from the beginning.
		if ((System.currentTimeMillis() - starttime) > duration) {
			if (loopIt) {
				restart();
				return true;
			}
			
			// Otherwise there is no more animation to play so stop.
			return false;
		}
		
		// Based on the current frame and elapsed time figure out what frame to show.
		float normTime = (float) (System.currentTimeMillis() - starttime)/ (float) duration;

		int currentFrame = (int) (((float) numFrames) *  normTime);
		if (currentFrame > numFrames-1) currentFrame = numFrames-1;
		
		// Hide all the frames first
		for (int i=0; i < numFrames; i++) {
			if (i != currentFrame) frames[i].hide();
		}
		
		// Then show all the frame you want to see.
		if (visibility)
			frames[currentFrame].show();
		else 
			frames[currentFrame].hide();
		return true;

	}
	public boolean isInside(int posx, int posy) {
		return group.isPointInElement(posx, posy);
	}
		


		
	}
