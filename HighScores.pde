import java.io.IOException;
import javax.swing.JOptionPane;

public class HighScores {
  
  public int score;
  public String name;
  public final String fname = "Score0.txt";

  public HighScores() throws IOException {
    BufferedReader readTxt = createReader(fname); //<>//
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