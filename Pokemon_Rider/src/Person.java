
public class Person {

	private int numFrames;		// Keeps track of how many frames are in the animation sequence
	private long duration;		// duration to play the animation over (in milliseconds)
	private long starttime;		// keep track of starting time of animation
	private int x, y;

	private boolean loopIt;		// determine whether animation should loop or not
	private boolean starting;	// Flag to indicate that you are starting animation from frame zero
	private boolean stopped;	// Flag to indicate if the animation has stopped.
	private boolean visibility;	// Flag to determine if the images should be visible or not
	public boolean Playing;
	
	private EZImage[] frames;	// Hold all the animation frames
	private EZGroup group;		// Holds the EZGroup
	public EZSound surf;
	
	// Constructor accepts the following parameters
	// filename - contains an array of filenames of images to read.
	// dur - duration over which animation frames should play.
	// posX, posY - location of the animated object.
	Person(String[] filenames, long dur, int posX, int posY, EZSound surf) {
		duration = dur;
		surf = EZ.addSound("Bubble.wav");
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
		
		x = posX;
		y = posY;
		setPosition(posX, posY);
		
		setLoop(true);
		starting = true;
		stopped = false;
		visibility = true;
		Playing = true;
	}
	
	public int getX() {
		return group.getXCenter();
	}

	public int getY() {
		return group.getYCenter();
	}

	// Set the position of the person
	public void setPosition(int posx, int posy) {
		x = posx;
		y = posy;
		setPersonImagePosition(x, y);
	}

	private void setPersonImagePosition(int posx, int posy) {
		group.translateTo(posx, posy);
	}

	// Methods for moving the person
	public void moveLeft(int step) {
		setPosition(x - step, y);
		
	}
	public void moveRight(int step) {
		setPosition(x + step, y);
	}

	public void moveUp(int step) {
		if (y >= 400) {
		setPosition(x, y - step);
		}
	}
	
	public void moveDown(int step) {
		if (y <= 1400) {
		setPosition(x, y + step);
		}
	}

	public boolean isInside(int posx, int posy) {
		return group.isPointInElement(posx, posy);
	}
	// Keyboard controls for moving the person.
	public void ControlIt(EZSound surf) {
		if (EZInteraction.isKeyDown('w')) {
			moveUp(14);
		} else if (EZInteraction.isKeyDown('a')) {
			moveLeft(14);
		} else if (EZInteraction.isKeyDown('s')) {
			moveDown(14);
		} else if (EZInteraction.isKeyDown('d')) {
			moveRight(14);
		}
		//make a sound when moving the person
		if(EZInteraction.wasKeyPressed('w')) {
			surf.play();
		}
		if (EZInteraction.wasKeyPressed('a')) {
			surf.play();
		}
		if (EZInteraction.wasKeyPressed('s')) {
			surf.play();
		}
		if (EZInteraction.wasKeyPressed('d')) {
			surf.play();
		}
	}

	void translateTo(int posX, int posY) {
		group.translateTo(posX,posY);
	}
	
	void setLoop(boolean loop){
		loopIt = loop;
	}
	void restart(){
		starting = true;
	}
	
	// Hide each animation frame
	void hide(){
		visibility = false;
		for(int i =0; i < numFrames; i++) frames[i].hide();
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
	}