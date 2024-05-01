import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class SnakeGame extends JPanel implements ActionListener,KeyListener{
    private class Tile{
        int x;
        int y;
        Tile(int x , int y)
        {
            this.x=x;
            this.y = y;
        }
    }
    int boardheight;
    int boardwidth;
    int tilesize = 25;
    //snake
    Tile snakeHead ;
    ArrayList<Tile> snakeBody;
    //food
    Tile food;
    Random random;

    // game logic - to move snake -- changes the position of the panel
    Timer gameLoop;
    int velocity_x;
    int velocity_y;
    boolean gameOver = false;
    SnakeGame(int boardheight , int boardwidth)
    {
        this.boardheight = boardheight;
        this.boardwidth = boardheight;
        setPreferredSize(new Dimension(this.boardheight,this.boardwidth));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        //snake 
        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        //food
        food = new Tile(10,10);
        random = new Random();
        placeFood();

        velocity_x =0;
        velocity_y = 1;

        // Timer
        gameLoop = new Timer(100,this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        //food
        g.setColor(Color.red);
        g.fill3DRect(food.x*tilesize,food.y*tilesize,tilesize,tilesize,true);

        // snake color
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tilesize, snakeHead.y*tilesize,tilesize,tilesize,true);

        //snake body
        for(int i =0;i<snakeBody.size();i++)
        {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x*tilesize, snakePart.y*tilesize,tilesize,tilesize,true);
        }
        //score
        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameOver)
        {
            g.setColor(Color.red);
            g.drawString("Game Over :"+String.valueOf(snakeBody.size()) , tilesize-16,tilesize);
        }
        else{
            g.drawString("Score :"+String.valueOf(snakeBody.size()), tilesize-16,tilesize);
        }
    }

    public void placeFood()
    {
        food.x = random.nextInt(boardwidth/tilesize);
        food.y = random.nextInt(boardheight/tilesize);//0 - 24
    }
    public boolean collision(Tile tile1 , Tile tile2)
    {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void move()
    {
        //eat food
        if(collision(snakeHead, food))
        {
            snakeBody.add(new Tile(food.x,food.y));
            placeFood();
        }


        //snake body
         for(int i = snakeBody.size()-1;i>=0;i--)
         {
            Tile snakePart = snakeBody.get(i);
            if(i==0)
            {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
         }


        //snake Head 
        snakeHead.x+= velocity_x;
        snakeHead.y+=velocity_y;


        //game over condition
        for(int i =0;i<snakeBody.size();i++)
        {
            Tile snakePart = snakeBody.get(i);
            // collide with snake head
            if(collision(snakeHead, snakePart))
            {
                gameOver = true;
            }
        }
        if(snakeHead.x*tilesize<0 || snakeHead.x*tilesize>boardwidth || snakeHead.y*tilesize<0 ||snakeHead.y> boardheight )
        {
            gameOver = true;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       move();
        repaint();
        if(gameOver)
        {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocity_y!=1)
        {
            velocity_x = 0;
            velocity_y = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocity_y!=-1)
        {
            velocity_x = 0;
            velocity_y = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocity_x!=1)
        {
            velocity_x = -1;
            velocity_y = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocity_x!=-1)
        {
            velocity_x = 1;
            velocity_y = 0;
        }
    }
    // donot need
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
