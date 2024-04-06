import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.awt.*;

public class Playground extends JPanel implements ActionListener, KeyListener{
    private int boardWidth;
    private int boardHeight;
    int snakePartSize = 25;

    private class Square{
        private int x;
        private int y;

        public Square(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    //snake
    private Square snakeHead;
    private ArrayList<Square> snakeBody = new ArrayList<Square>();

    //Apple
    private Square apple;

    //Random
    private Random random = new Random();

    //Timer
    private Timer timer;

    //Snake direction
    private int dx = 1; //1 = right, -1 = left
    private int dy = 0; //1 = down, -1 = up




    public Playground(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));

        //Snake
        snakeHead = new Square(5, 5);
        snakeBody.add(snakeHead);


        //Apple
        apple = new Square(15, 20);
        randomApple(); //Random apple position

        this.timer = new Timer(1000/10, this);//10 frames per second
        this.timer.start();//Start the timer
 

        //Add KeyListener
        addKeyListener(this);
        setFocusable(true);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
        
    }
    public void draw(Graphics g){
        //Grid  
        /*for(int i = 0; i < boardWidth/snakePartSize; i++){
            g.drawLine(i*snakePartSize, 0, i*snakePartSize, boardHeight);
            g.drawLine(0, i*snakePartSize, boardWidth, i*snakePartSize);

        }*/

        //Snake Head
        g.setColor(Color.WHITE);
        g.fillRect(snakeHead.x * snakePartSize, snakeHead.y * snakePartSize, snakePartSize, snakePartSize);
        
        //Apple
        g.setColor(Color.RED);
        g.fillRect(apple.x * snakePartSize, apple.y * snakePartSize, snakePartSize, snakePartSize);

        //Draw each part of the snake
        for(Square part : snakeBody){
            g.setColor(Color.GREEN);
            g.fillRect(part.x * snakePartSize, part.y * snakePartSize, snakePartSize, snakePartSize);
        }
    }

    public void randomApple(){
        apple.x = random.nextInt(boardWidth/snakePartSize);
        apple.y = random.nextInt(boardHeight/snakePartSize);
    }

    public void move() {
 
        // Move the snake body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Square currentPart = snakeBody.get(i);
            if(i == 0){
                currentPart.x += dx;
                currentPart.y += dy;
            }else{
                Square nextPart = snakeBody.get(i - 1);
                currentPart.x = nextPart.x;
                currentPart.y = nextPart.y;
            }
        }

        //If the snake eats itself
        for (int i = 1; i < snakeBody.size(); i++) {
            if (collision(snakeHead, snakeBody.get(i))) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over");
            }
        }
    
        
        // If the snake eats the food
        if (collision(snakeHead, apple)) {
            // Add a new part to the snake body
            snakeBody.add(new Square(apple.x, apple.y));

            //increase the speed of the snake
            if(timer.getDelay() > 22){
                timer.setDelay(timer.getDelay() - 3);
            }
            // Randomize the apple position
            randomApple();
        }
    
        // If the snake head reaches the border
        if (snakeHead.x >= boardWidth / snakePartSize || snakeHead.x < 0 || snakeHead.y >= boardHeight / snakePartSize || snakeHead.y < 0) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over");
        }
    }
    

    public Boolean collision(Square a, Square b){
        return a.x == b.x && a.y == b.y;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        //Key events
        if (key == KeyEvent.VK_LEFT && dx == 0) {
            dx = -1;
            dy = 0;
        } else if (key == KeyEvent.VK_RIGHT && dx == 0) {
            dx = 1;
            dy = 0;  
        } else if (key == KeyEvent.VK_UP && dy == 0) {
            dx = 0;
            dy = -1;
        } else if (key == KeyEvent.VK_DOWN && dy == 0) {
            dx = 0;
            dy = 1;
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        
    }


    

    
}
