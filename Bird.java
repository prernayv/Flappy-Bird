package flappy.bird;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //to store all the pipe in the game
import java.util.Random; //to placing all pipe ramdomly
import javax.swing.*;


public class Bird extends JFrame implements ActionListener, KeyListener{
    
         private Image backgroundImg;
         private Image birdImg;
         private Image topPipeImg;
         private Image bottomPipeImg;
         
         int birdX=360/8;
         int birdY=640/2;
         int birdWidth=34;
         int birdHeight=24;

 
         class BirdImg{
             int x=birdX;
             int y=birdY;
             int width= birdWidth;
             int height= birdHeight;
             Image img;
             
             BirdImg(Image img){
                 this.img=img;
             }
         }
         
         //Pipes
         int pipeX= 360;
         int pipeY=0;
         int pipeWidth= 64;
         int pipeHeight = 512;
         
         class Pipe{
             int x=pipeX;
             int y=pipeY;
             int width=pipeWidth;
             int height=pipeHeight;
             Image img;
             boolean passed =false;
             
             Pipe(Image img){
                 this.img=img;
             }
         }
         
         //gamelogic
         BirdImg bird;
         int velocityX=-4; //pipe move to the left speed(simulate bird moving right)
         int velocityY= 0; //bird move up/down speed
         int gravity =1;
         
         ArrayList<Pipe>pipes;
         Random random = new Random();
         
         Timer gameLoop;
         Timer placePipesTimer;
         double score =0;
         boolean gameOver = false;
         
    Bird(){
        setFocusable(true);
        addKeyListener(this);
        
        
         setSize(360,640);
         setLocation(500,50);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setResizable(false);
         
          MyPanel panel = new MyPanel();
         panel.setBounds(500, 50, 360, 640);
         //panel.setBackground(Color.BLUE);
         add(panel);
         
         loadImage();
         bird=new BirdImg(birdImg);
         pipes = new ArrayList<Pipe>();
         
         
         
         
         //place pipes timer
         placePipesTimer = new Timer(1500,new ActionListener(){
             @Override
             public void actionPerformed(ActionEvent e){
                 placePipes();
             }
         });
         
         placePipesTimer.start();   
         gameLoop=new Timer(1000/60,this);
         gameLoop.start();
         setVisible(true);
         
        
        
   
    }
    
    public void loadImage(){
        //background
        ImageIcon i1=new ImageIcon(ClassLoader.getSystemResource("flappy img/flappybirdbg.png"));
        backgroundImg=i1.getImage();
        ImageIcon i2=new ImageIcon(ClassLoader.getSystemResource("flappy img/flappybird.png"));
        birdImg=i2.getImage();
        ImageIcon i3=new ImageIcon(ClassLoader.getSystemResource("flappy img/toppipe.png"));
        topPipeImg=i3.getImage();
        ImageIcon i4=new ImageIcon(ClassLoader.getSystemResource("flappy img/bottompipe.png"));
        bottomPipeImg=i4.getImage();
         
    }
    class MyPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }

        public void draw(Graphics g) {
            //System.out.println("draw");
            g.drawImage(backgroundImg, 0, 0, 360, 640, null);
            g.drawImage(bird.img, bird.x, bird.y,bird.width,bird.height, null);
            // Add more drawing code here if needed
            
        for(int i = 0; i<pipes.size();i++){
            Pipe pipe=pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y,pipe.width,pipe.height,null);
            
        }  
        
        //score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf((int) score),10,35);
        }else{
            g.drawString(String.valueOf((int)score), 10, 35);
        }
        }
        
        
    }
    public void placePipes(){
         //(0-1) * pipeHeight/2.
        // 0 -> -128 (pipeHeight/4)
        // 1 -> -128 - 256 (pipeHeight/4 - pipeHeight/2) = -3/4 pipeHeight
        int randomPipeY = (int)(pipeY - pipeHeight/4- Math.random()*(pipeHeight/2));
        int openingSpace = 640/4; //the space b/w two pipes
        
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y=randomPipeY;
        pipes.add(topPipe);
        
        Pipe bottomPipe = new Pipe(bottomPipeImg); //settle the bottom pipe accordingly
        bottomPipe.y= topPipe.y + pipeHeight +  openingSpace;
        pipes.add(bottomPipe);
    }
    public void move(){
        //bird
        velocityY += gravity;
        bird.y +=velocityY;
        bird.y= Math.max(bird.y, 0);
        
        //pipe
        for(int i = 0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;
            
        if(!pipe.passed && birdX > pipe.x +pipe.width){
            pipe.passed = true;
            score += 0.5; //0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
        }
         if (collision(bird,pipe)){
            gameOver= true;
        }  
        
        }    
        if(bird.y > 640){
            gameOver=true;
        }
    }
    
     boolean collision(BirdImg a,Pipe b){
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }
    
     
  

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY= -9;
        if(gameOver){
            //restart the game
            bird.y = birdY;
            velocityY=0;
            pipes.clear();
            gameOver = false;
            score=0;
            gameLoop.start();
            placePipesTimer.start();
        }    
        }
    }
    
     @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }

    public static void main(String []args){
        new Bird();
    }
}
