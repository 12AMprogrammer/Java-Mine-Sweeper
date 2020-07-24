/*
Coding by Matthew Brooks
MineField2.java
5/4/2020
This program starts an interactive game similar to mine sweeper called mine field.
**Note that Mr. Loring said in his video we will be skipping menus since we did not cover them.***
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random.*;
//Needed for the JFrame icon image.
import java.awt.image.BufferedImage;
//Get the image from a file.
import javax.imageio.ImageIO;
//Hnadles exception if the image can't be loaded.
import java.io.IOException;

public class MineField2 extends JFrame implements MouseListener
{
  //Constansts for the amount of columns, rows, the number of sqaures, and the gap needed for GridLayout().
  final int ROWS = 4;
  final int COLS = 5;
  final int GAP = 8;
  final int NUM = (ROWS * COLS);
  //Will be used to track the mine location among the squares.
   int mine;
  //Tracks how many times we have clicked a square.
  int clickCounter = 0;
  //How many square clicks needed to win. Will be set to 10 initially.
  int winClicks = 10;
  //Variable to keep track of total wins. Set to 0 initially.
  int winCount = 0;
  //int total games played. Set to one initially.
  int gameCount = 0;
  //Upper and lower limits of random numbers for mine generation.
  int MAX = 20;
  int MIN = 0;
  //The biggest panel.
  JPanel gameBoard = new JPanel(new BorderLayout());
  //Creates a game that contains 20 JPanels.
  JPanel gamePieces = new JPanel(new GridLayout(ROWS, COLS, GAP, GAP));
  //Creates 20 JPanel objects.
  JPanel[] panel = new JPanel[NUM];
  //Creates a statusBar to display statistics at the bottom of the
  //gameBoard pane.
  JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
  //Status bar label showing how many games user has won and played.
  JLabel statusLabel = new JLabel("Wins: 0 out of 0");
  JLabel percentLabel = new JLabel("   0%");
  //Creates a label that lets the user know the square "exploded".
  JLabel mineLabel = new JLabel("***MINE***");
  
  
  public MineField2()
  {
    //Sets the title.
    super("Mine Field");
    //Allows x button to close the application.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //Adds gamePieces panels to the gameBoard panel.
    gameBoard.add(gamePieces, BorderLayout.CENTER);
    //Adds the statuseBar panel to the gameBoard panel.
    gameBoard.add(statusBar, BorderLayout.SOUTH);
    //Adds the stats labels to the statusBar panel.
    statusBar.add(statusLabel);
    statusBar.add(percentLabel);
    //For loop to add our array of panel objects.
    for(int i = 0; i < panel.length; i++)
    {
      //Creates our color object
      Color cougarBlue = new Color(0,58,112);
      //Needed to set each panel array element as a new panel object.
      panel[i] = new JPanel();
      //Sets it to a flow layout, so our gamePieces panel will 
      //fill from left to right.
      panel[i].setLayout(new FlowLayout());
      //Adds a mouse listener to each panel.
      panel[i].addMouseListener(this);
      //Adds each panel object to the gamePieces.
      gamePieces.add(panel[i]);
      //Sets the panel object blue.
      panel[i].setBackground(cougarBlue);
    } 
    //Sets our giant panel to hold the gameBoard.
    setContentPane(gameBoard);
    //Generates the mine location and assigns it to int using our random object.
    mine = ((int)(Math.random() * 100) % MAX + MIN);
    //Will call NewGame() on the first run in order to set the game difficulty since we're not using a menu
    //for this project.
    NewGame();
  }
  public static void main(String[] args)
  {
    //Creates the frame to display our panels and components.
    MineField2 aFrame = new MineField2();
    //Sets the frame size.
    aFrame.setSize(800, 700);
    //Makes the frame visible.
    aFrame.setVisible(true);
    //Reads the image that will be used as the application.
    BufferedImage image = null;
    try
    {
      //Reads the image and locates image location.
      image = ImageIO.read(aFrame.getClass().getResource("/minefield.png"));
    }
    catch(IOException e)
    {
      //Will generate an error message from the compiler.
      e.printStackTrace();
    }
    //Adds the image to the frame.
    aFrame.setIconImage(image);
    //Makes it visible.
    aFrame.setVisible(true); 
  }
  //New method to reset everything in the game.
  private void NewGame()
  {
   //Resets all counters and labels.
    clickCounter = 0;
    winCount = 0;
    gameCount = 0;
    statusLabel.setText("Wins: 0 out of 0");
    percentLabel.setText("   0");
    //We need to ask what difficulty the player would like.
    //Creates a string of options.
    String[] level = new String[] {"Easy = 5 clicks", "Normal = 10 clicks", "Hard = 15 clicks", "Exit Game"};
    //Presents the user with a dialog box and assigns the int response with an index number from the string depending on the option clicked.
    int response = JOptionPane.showOptionDialog(null, "Choose a Difficulty", "Mine Field", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, level, level[1]);
    if(response == 0)
    {
      //Sets the number of clicks required to win to 5.
      winClicks = 5;
    }
    if(response == 1)
    {
      //Sets the number of clicks required to win to 10.
      winClicks = 10;
    }
    if(response == 2)
    {
      //Sets the number of clicks required to win to 15.
      winClicks = 15;
    }
    if(response == 3)
    {
      //Exits the program.
      System.exit(0);
    }
  }
  
  //New method to reset a game but keep a series going.
  private void PlayAgain()
  {
     //Sets the click count back to zero.
     clickCounter = 0;
     statusLabel.setText("Wins: " + winCount + " out of  " + gameCount + ".");
     //Calls the HitRate class for the number of 
     percentLabel.setText("   " + HitRate.Calculate(winCount, gameCount));
     //Sets percentLabel to hold no string until gameCount is higher than zero.
     if(gameCount <= 0)
     {
       percentLabel.setText("");
     }
     //Generates the mine location and assigns it to int using our random object.
     mine = ((int)(Math.random() * 100) % MAX + MIN);
     //Will go through all panels[] and re-add them after a delayed blink to show the player things are being reset.
     for(int i = 0; i < panel.length; i++)
     {
        panel[i].removeMouseListener(this);
        gamePieces.remove(panel[i]);
        //Creates our color object
        Color cougarBlue = new Color(0,58,112);
        //Needed to set each panel array element as a new panel object.
        panel[i] = new JPanel();
        panel[i].setLayout(new FlowLayout());
        //Adds a mouse listener to each panel.
        panel[i].addMouseListener(this);
        //Sets the panel object yellow.
        panel[i].setBackground(Color.YELLOW);
        //So we can see the change in color
        repaint();
        panel[i].setBackground(cougarBlue);
        //Validates the panel.
        panel[i].validate();
        //Updates the color back to blue.
        repaint();
        //Adds each panel object back to the gamePieces board.
        gamePieces.add(panel[i]);
    } 
  }
  //*********************************************************************************************************************************
  //Here the logic for the main game!
  //Which panel was clicked?
  public void mouseClicked(MouseEvent e) 
  {
    //Tracks how many times we've clicked.
    clickCounter++;
    //Gets the panel object we've clicked.
    Object source = e.getSource();
    
    //Will go through each panel to find the one we clicked.
    for(int x = 0; x < panel.length; x++)
    {
      //Means we clicked panel[x].
      if(source == panel[x])
      {
        //If we clicked the winning amount of times.
        if(clickCounter == winClicks)
        {
          //We hit the mine and lost.
          if(mine == x)
          {
            //Sets the mine panel to red.
            panel[x].setBackground(Color.RED);
            //Adds the label that says ***MINE*** to the panel.
            panel[x].add(mineLabel);
            //Will relayout the component.
            panel[x].validate();
            //Redraws the program to make sure we see everything.
            repaint();
            
            //Allows the user to choose to play again or leave the game.
            int answer = JOptionPane.showConfirmDialog(null, "You hit the mine. You lost. Play again?", "Play Again?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            //If the user clicks yes.
            if(answer == JOptionPane.YES_OPTION)
            {
              //Tracks the number of games played.
              gameCount++;
              //Removes the mine label.
              panel[x].remove(mineLabel);
              //Lays out the component again.
              panel[x].validate();
              //Resets the gameboard.
            }
            else
            {
              //Exits the program.
              System.exit(0);
            }
          }
          //We missed the mine and won.
          else
          {
           winCount++;
           //Sets the panel color to white.
            panel[x].setBackground(Color.WHITE); 
            //So we can see the color change.
            repaint();
             //Allows the user to choose to play again or leave the game.
            int answer = JOptionPane.showConfirmDialog(null, "Awesome job, you won! Play again?", "Play Again?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            //If the user clicks yes.
            if(answer == JOptionPane.YES_OPTION)
            {
              //Tracks the number of games played.
              gameCount++;
              //Removes the mine label.
              panel[x].remove(mineLabel);
              //Lays out the component again.
              panel[x].validate();
              //Resets the gameboard.
              PlayAgain();
            }
            else
            {
              //Exits the program.
              System.exit(0);
            }
          }
        }
        //We hit the mine and lost.
        else if(mine == x)
        {
           //Sets the mine panel to red.
            panel[x].setBackground(Color.RED);
            //Adds the label that says ***MINE*** to the panel.
            panel[x].add(mineLabel);
            //Will relayout the component.
            panel[x].validate();
            //Redraws the program to make sure we see everything.
            repaint();
            
            //Allows the user to choose to play again or leave the game.
            int answer = JOptionPane.showConfirmDialog(null, "You hit the mine. You lost. Play again?", "Play Again?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            //If the user clicks yes.
            if(answer == JOptionPane.YES_OPTION)
            {
              //Tracks the number of games played.
              gameCount++;
              //Removes the mine label.
              panel[x].remove(mineLabel);
              //Lays out the component again.
              panel[x].validate();
              //Resets the gameboard.
              PlayAgain();
            }
            else
            {
              //Exits the program.
              System.exit(0);
            }
        }
        //We didn't win or hit the mine, which will turn the panel white.
        else
        {
          //Sets the selected panel as white.
          panel[x].setBackground(Color.WHITE);
          //Redraws the screen to make sure we see everything.
          repaint();
          //Removes the ability to click the panel.
          panel[x].removeMouseListener(this);
        }
      }
    }
   }
   //Decided to add some code to highlight the square in yellows for each mouseEntered event for the panel.
    @Override
   public void mouseEntered(MouseEvent e)
   {
     //Creates a source object.
     Object source = e.getSource();
     //For loop to search all the panels.
     for(int y = 0; y < panel.length; y++)
     //In the event that any of our panels is "moused over".
     if(source == panel[y])
     {
       //Gives the panel a yellow border.
       panel[y].setBorder(BorderFactory.createLineBorder(Color.YELLOW));
       //Validates the panel and sets it.
       panel[y].validate();
     }
   }
   //Method to remove the border when we "mouse off".
   @Override
   public void mouseExited(MouseEvent e)
   {
     Object source = e.getSource();
     for(int y = 0; y < panel.length; y++)
     if(source == panel[y])
     {
       //Removes the border from the panel.
       panel[y].setBorder(BorderFactory.createEmptyBorder());
       //Validates it.
       panel[y].validate();
     }

   }

   @Override
  public void mousePressed(MouseEvent e)
  {
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
  }
}