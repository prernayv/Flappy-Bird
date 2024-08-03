package flappy.bird;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //to store all the pipe in the game
import java.util.Random; //to placing all pipe ramdomly
import javax.swing.*;

public class FlappyBird extends JPanel{
    FlappyBird(){
        setSize(360,640);
        setLocation(500,50);
        setBackground(Color.BLUE);
        
        setVisible(true);
    }
    
    public static void main(String [] args){
        new FlappyBird();
    }
}
