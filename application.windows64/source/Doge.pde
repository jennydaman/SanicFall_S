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