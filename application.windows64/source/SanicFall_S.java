import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import javax.swing.JOptionPane; 
import java.io.IOException; 
import java.io.IOException; 
import javax.swing.JOptionPane; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SanicFall_S extends PApplet {



/*
 * Player presses 'a' or 'd' to move left and right
 * movement slides player across bottom of the screen
 * press SPACEBAR to shoot bullet towards mouse position.
 * SANIC will fall from above. shoot SANICs to avoid prevent
 * them from touching the floor. Game ends if a SANIC reaches
 * the bottom of the screen.
 * killing DOGE will reward player with a certain number of points
 * points are given for shooting down SANICs.
 */
//sanic0.png image size: 150x136
int playerPosition = 370,
     playerMotion = 0,
     playerFireX = -100,
     playerFireY = 0,
     score = 0,
     multi = 1;

byte cooldown = 0;

private java.util.Random rn = new java.util.Random();

Doge doge;

PImage player;

PImage[] sanic = new PImage[5];
boolean[] sanicSpawned = new boolean[5];
int[] sanicX = new int[5];
int[] sanicY = new int[5];
int sanicI = 0;
float sanicTurn = 0;
 
public void setup() {
  System.out.println("Game has started. ");
  player = loadImage(begingame() + ".png");
  
  
  background(5, 255, 100);
  doge = new Doge();
  for (int i = 0; i < 5; i++) 
    sanic[i] = loadImage("pixel.png"); 
    
    textSize(14);
} //end setup()
 
public void draw() {
   
  int prev = score;
  background(5, 255, 100);
   
  playerMovement();
  fill(255, 0, 0);
  image(player, playerPosition, 525); //this is the player
  
  shoot();
  fill(0, 0, 255);
  ellipse(playerFireX, playerFireY, 10, 10); 
  
  sanicI++;
  if (sanicI > 4)
    sanicI = 0;
  sanic();
  
  for (int i = 0; i < sanic.length; i++)
    image(sanic[i], sanicX[i], sanicY[i]);
  
  
  image(doge.leDOG, doge.xpos, 25);
  doge.Disp();
  score += doge.nextDoge(playerFireX, playerFireY);
  
  try {
  if (sanicY[sanicI] > 450) 
   endgame();
  } catch (IOException e) {
    System.out.println("Missing file");
    System.exit(0);
  }
  
  
  
   
   multi = (int) Math.sqrt(score/2) + 1;
   if (multi > 20)
     multi = 20;
     
   fill(0, 102, 153);
   text("Score: " + score + "\nMultiplyer: " + multi, 10, 30);
     
   
   
} //end draw()
 
public void playerMovement() {
  
  if (keyPressed) {
    if (key == 'a' || key == 'A')
       playerMotion -= 2;
    if (key == 'd' || key == 'D')
       playerMotion += 2;
  }
  
   if (playerPosition < 0)
     playerPosition = 0;
   if (playerPosition > 740)
     playerPosition = 740;
   if (playerMotion > 0)
     playerMotion--;
   if (playerMotion < 0)
     playerMotion++;
  
  playerPosition += playerMotion;
} //end playerMovement() : void

public void shoot() {
  if (cooldown != 0) {
    cooldown--;
  }
  else if (cooldown == 0)
    if (keyPressed)
      if (key == ' ') {
        playerFireX = playerPosition + 35;
        playerFireY = 550;
        cooldown = 30;
      }
  playerFireY -= 18;
  
  int hitNum = -1;
  for (int i = 0; i < sanic.length; i++)
    if ((playerFireX > sanicX[i] && playerFireX < sanicX[i] + 150) && (playerFireY > sanicY[i] && playerFireY < sanicY[i] + 136) && sanicSpawned[i])
      hitNum = i;
  if (hitNum != -1) { 
    score += multi;
    sanicSpawned[hitNum] = false;
    sanic[hitNum] = loadImage("pixel.png");
    playerFireX = -1000;
  }
  
  if (playerFireY < 0) 
    playerFireX = -1000; //nullify bullet once off screen
  
}

public void sanic() { //will create a new SANIC, or move a sanic down. 
  
  if (!sanicSpawned[sanicI]) {
    sanicTurn += 0.05f;
    if (sanicTurn > 10 - multi) {
      sanicTurn = 0;
      sanicSpawned[sanicI] = true;
      sanic[sanicI] = loadImage("sanic0.png");
      sanicX[sanicI] = rn.nextInt(665);
      sanicY[sanicI] = 0;
    }
  }
  else 
    sanicY[sanicI] += multi/2+1;
  
}


public String begingame() {
  
  String ans = "Choose a character...";
  while (ans.equals("Choose a character...")) {
    String[] players = {
        "Choose a character...",
        "troll",
        "rare_pepe",
        "potato",
        "poro",
        "obama",
        "forever_alone"
    };
    ans = (String) JOptionPane.showInputDialog(null, 
        "SanicFall by Jennings Zhang" 
        + "\nInstructions: "
        + "\nPress 'a' or 'd' to move. Press the SPACEBAR to shoot."
        + "\nPrevent the SANIC from touching the bottom of the screen!"
        + "\nGain points by shooting SANICs. Gain bonus points by shooting DOGE.",
        "SanicFall",
        JOptionPane.QUESTION_MESSAGE, null,
        players,
        players[0]);
        
    if (ans == null) {
      System.out.print("Game has ended.");
      System.exit(0);
    }
  }
  return ans;  
}

public void endgame() throws IOException {
  HighScores s = new HighScores();
  JOptionPane.showMessageDialog(null,
       "You got #REKT!!!!!!"
       + "\nYour score: " + score
       + "\nHigh score: " + s.score + "\t " + s.name
       + "\nThank you for playing.",
       "Game over!",
       JOptionPane.WARNING_MESSAGE);
       
    s.evaluate(score);
    System.out.println("Game over! Score: " + score);
    System.exit(0);
}




 
/*
 * If doge is hurt, he gets brief immunity and turns red. 
 */

public class Doge {
  
  public int health;
  public PImage leDOG;
  
  
  
  public int xpos;
  public int lvl = 1;
  private int move = 8,
      timer = 0;
  private boolean hurt = false,
      dead = false;
  private PImage[] doges;
  private String disp;
  
  public Doge() {
    health = 3;
    
    doges = new PImage[4];
    doges[0] = loadImage("doge0.png"); //regular doge
    doges[1] = loadImage("dogehurt.png"); //recently hit
    doges[2] = loadImage("dogewhite.png"); //dead
    doges[3] = loadImage("pixel.png"); //no doge
    leDOG = doges[0];
    //size of doge0.png: 125x125
    xpos = 350;
    dead = false;
    
    disp = "Doge has spawned! Level: " + lvl + ". Health: " + health;
    
  } //end constructor
  
  public int nextDoge(int playerFireX, int playerFireY) {
    int s = 0;
    
    if (xpos > 675)
      move = -8;
    if (xpos < 0)
      move = 8;
    xpos += move;
    
   
    if (hurt) {
      if (health == 0) {
        leDOG = doges[2];
        timer = 90;
        hurt = false;
      }
      else if (timer == 0)
        hurt = false;
      else
          timer--;
    }
    
    else if (health == 0) { //if recently dead, whitedoge.png will display
      timer--;
      if (timer == 0) {
        if (!dead) {
          leDOG = doges[3]; //despawns doge
          dead = true; 
          timer = 180; //death timer
        }
        else
          respawn();
      }
      
    }
    
    
    else {
      leDOG = doges[0];
      if (playerFireY < 150)
        if (playerFireX > xpos && playerFireX < xpos + 125) { //if hit
          hurt = true;
          leDOG = doges[1];
          health--;
          timer = 30;
          disp = "You hit doge! Remaining health: " + health;
          
          if (health == 0) { //doge dies
           disp = "You have quickscoped doge. \nIt's super effective! Doge dieded. ";
            s = (int) Math.pow(5, lvl);
          } //end if doge dies
        
        } //end if hit
    }
    return s;
    
  } //end nextDoge(int, int) : int
  
  
  
  private void respawn() {
    dead = false;
    lvl++;
    leDOG = doges[0];
    health = 2 * lvl + 2;
    if (health > 6)
      health = 6;
    disp = "Doge has respawned! Level: " + lvl + ". Health: " + health;
  }
  
  public void Disp() {
    fill(163, 73, 164);
    text(disp, 500, 30);
  } 
  
  
  
  
} //end class



public class HighScores {
  
  public int score;
  public String name;
  public final String fname = "Score0.txt";

  public HighScores() throws IOException {
    BufferedReader readTxt = createReader(fname);
    score = Integer.parseInt(readTxt.readLine().trim());
    name = readTxt.readLine();
  } 
  
  public void evaluate(int score) throws IOException {
    if (score < this.score)
      return; //not a high score
    String player = JOptionPane.showInputDialog("Congratulations! You made beat the high score!"
        + "\nPlease type your name: ");
    if (player == null)
      System.exit(0);
   change(score, player); 
  } 
  
  
  private void change(int newscore, String player) throws IOException {
    PrintWriter writeTxt = createWriter("data\\" + fname);
    score = newscore;
    name = player;
    writeTxt.println(score);
    writeTxt.println(name);
    System.out.println(score + " " + name);
    writeTxt.flush();
    writeTxt.close();
  } 
  
} 
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SanicFall_S" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
