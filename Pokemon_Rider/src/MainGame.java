import java.awt.Color;

public class MainGame {
	static final int PERSON_SURFING = 1;
	static final int GAME_OVER = 2;
	static int FOOD_COUNTER = 1000;
	static int POTION_COUNTER = 1000;

	public static void main(String[] args) {
		//load window, images, and sound
		EZ.initialize(1000, 720);
		EZImage background0 = EZ.addImage("Towny.png", 500, 90);
		EZImage background00 = EZ.addImage("Towny.png", 1500, 90);
		EZImage background1 = EZ.addImage("test3.png", 500, 420);
		EZImage background2 = EZ.addImage("test3.png", 1500, 420);
		EZSound PokemonTheme = EZ.addSound("PokemonTheme.wav");
		EZSound Surf = EZ.addSound("Bubble.wav");
		EZSound Potion = EZ.addSound("Potion.wav");
		EZSound Berry = EZ.addSound("Berry.wav");
		EZSound Eat = EZ.addSound("Shark.wav");
		EZSound Hit = EZ.addSound("Hit.wav");
		EZSound GameOver = EZ.addSound("GameOver.wav");

		// Color of the text
		Color c = new Color(255, 214, 51);
		Color d = new Color(0, 172, 230);
		int fontsize = 50;

		//create array for animations
		String[] swim = { "swim1.png", "swim2.png" };
		String[] Sharp = { "Sharp.png", "Sharp2.png", "Sharp3.png", "Sharp4.png", "Sharp5.png", "Sharp6.png" };
		String[] bird = { "Spearow.png", "Spearow2.png", "Spearow3.png", "Spearow4.png" };
		
		//creating array for obstacles and hurricane
		Bird spearow[] = new Bird[2];
		Hurricane shark[] = new Hurricane[3];
		Person myPerson = new Person(swim, 600, 1000, 720, Surf);
		Obstacle berry[] = new Obstacle[5];
		Obstacle potion[] = new Obstacle[5];

		for (int i = 0; i < 2; i++) {
			spearow[i] = new Bird(bird, 600, 500, 360);
		}
		
		//add text for berry and potion health
		EZ.addRectangle(135, 50, 270, 150, d, true);
		EZText text = EZ.addText(120, 30, "Berry: ", c, fontsize);
		EZText text1 = EZ.addText(120, 80, "Potion: ", c, fontsize);
		EZText text2 = EZ.addText(230, 25, "100", c, fontsize);
		EZText text3 = EZ.addText(230, 80, "100", c, fontsize);
		
		text.setFont("Pokemon Solid.ttf");
		text1.setFont("Pokemon Solid.ttf");
		text2.setFont("Pokemon Solid.ttf");
		text3.setFont("Pokemon Solid.ttf");

		//assigning pictures to array
		for (int f = 0; f < 3; f++) {
			shark[f] = new Hurricane(Sharp, 600, 500, 720);
		}
		for (int i = 0; i < 5; i++) {
			berry[i] = new Obstacle("Berry.png", 500, 720);
		}
		for (int i = 0; i < 5; i++) {
			potion[i] = new Obstacle("Potion.png", 500, 720);
		}
	
		
		PokemonTheme.play();
		
		int personState = PERSON_SURFING;
		
		while (personState == PERSON_SURFING) {
			//create scrolling background
			moveSky(background00, background0, background1, background2);
			repeatSky(background00, background0, background1, background2);
			
			//decrease potion and berry health when moving
			POTION_COUNTER -= 0.000000001;
			FOOD_COUNTER -= 0.0000000001;
			
			//show counter in game 
			text2.setMsg(Integer.toString(FOOD_COUNTER / 10));
			text3.setMsg(Integer.toString(POTION_COUNTER / 10));
			
			// Steer the person
			myPerson.go();
			myPerson.ControlIt(Surf);

			// Move spearow
			for (int x = 0; x < 2; x++) {
				spearow[x].go();
				spearow[x].move();
			}
			
			//Move sharks
			for (int i = 0; i < 3; i++) {
				shark[i].go();
				shark[i].move();
			}
			
			//Move berries and potions 
			for (int i = 0; i < 5; i++) {
				berry[i].move();
				potion[i].move();
			}
			
			//checking to see if berries and potions go into person or shark
			checkBerriesAndPotions(berry, potion, shark, myPerson, Berry, Potion, Eat, Hit);
			EZ.refreshScreen();
			if (FOOD_COUNTER < 10 || POTION_COUNTER < 10) {
				personState = GAME_OVER;
				PokemonTheme.stop();
				GameOver.play();
				EZ.addImage("GameOver.jpg", 500, 360);
			}
		}
	}
	
	//function for checking to see if berries and potions are in person or shark
	private static void checkBerriesAndPotions(Obstacle[] berries, Obstacle[] potions, Hurricane[] sharks, Person myPerson, EZSound Berry, EZSound Potion, EZSound Eat, EZSound Hit) {
		for (int i = 0; i < 5; i++) {
			if (myPerson.isInside(berries[i].getX(), berries[i].getY())) {
				if (FOOD_COUNTER < 800) {
					FOOD_COUNTER += 195;
				}
				Berry.play();
				berries[i].rando();
			}
			
			if (myPerson.isInside(potions[i].getX(), potions[i].getY())) {
				if (POTION_COUNTER < 800) {
					POTION_COUNTER += 200;
				}
				Potion.play();
				potions[i].rando();
			}
			
			for (int j = 0; j < 3; j++) {
				if (sharks[j].isInside(berries[i].getX(), berries[i].getY())) {
					Eat.play();
					berries[i].rando();
				}
				
				if (sharks[j].isInside(potions[i].getX(), potions[i].getY())) {
					Eat.play();
					potions[i].rando();
				}
				
				if (myPerson.isInside(sharks[j].getX(), sharks[j].getY())) {
					if (POTION_COUNTER < 800 && POTION_COUNTER > 400) {
						POTION_COUNTER -= 400;
					}
					if (FOOD_COUNTER < 800 && FOOD_COUNTER > 400) {
						FOOD_COUNTER -= 400;
					}
					Hit.play();
					sharks[j].rando();
				}
			}
		}
	}

	//setting up scrolling background
	public static void moveSky(EZImage background00, EZImage background0, EZImage background1, EZImage background2) {
		background00.moveForward(-0.4);
		background0.moveForward(-0.4);
		background1.moveForward(-7);
		background2.moveForward(-7);
	}
	
	//reset background position to show seamless background
	public static void repeatSky(EZImage background00, EZImage background0, EZImage background1, EZImage background2) {
		if (background1.getXCenter() < -500) {
			background1.moveForward(2000);
		}
		if (background2.getXCenter() < -500) {
			background2.moveForward(2000);
		}
		if (background0.getXCenter() < -500) {
			background0.moveForward(2000);
		}
		if (background00.getXCenter() < -500) {
			background00.moveForward(2000);
		}

	}

}
