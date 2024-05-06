package org.example;

import java.awt.*;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

public class SquareEnemy extends Rectangle {
    GameFrame gameFrame;
    public int HP = 10;
    int speed = 2;
    int xPos;
    int yPos;
    int width = 15;
    int height = 15;

    int ID;
    static int nextID = 1;
    boolean impactedWithEpsilon = false;
    int xDeath;
    int yDeath;
    int inside = 0;

    public SquareEnemy(GameFrame gameFrame){
        this.gameFrame = gameFrame;
        GamePanel.squares.add(this);
        GamePanel.allEnemies.add(this);
        generateRandomPointOutsideFrame(this.gameFrame);
        GamePanel.creation++;
        ID = nextID++;
    }

    public void generateRandomPointOutsideFrame(GameFrame frame) {

        Random random = new Random();
        int x, y;

        do {
            x = random.nextInt(2000 - 0) + 0; // Generate a random x coordinate within the range
            y = random.nextInt(1000 - 0) + 0;
        } while (x >= frame.getX() && x <= frame.getX() + frame.getWidth() &&
                y >= frame.getY() && y <= frame.getY() + frame.getHeight()); // Repeat until the point is outside the frame
        xPos = x;
        yPos = y;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(xPos,yPos,width,height);
    }


    public void moveTowardsEpsilon(Epsilon epsilon) {
        if (gameFrame.contains(this.xPos,this.yPos)){
            inside++;
            if (inside == 1){
                gameFrame.gamePanel.playSE(gameFrame.gamePanel.sound.enemyInsideSE);
            }
        }
        int epsilonX = epsilon.xPos;
        int epsilonY = epsilon.yPos;

        // Calculate direction towards epsilon
        int dx = epsilonX - xPos;
        int dy = epsilonY - yPos;

        // Normalize the direction vector
        double magnitude = Math.sqrt(dx * dx + dy * dy);
        if (magnitude < 2) { // Adjust the threshold as needed

            return;
        }
        double normalizedDX = dx / magnitude;
        double normalizedDY = dy / magnitude;

        if (!impactedWithEpsilon) {
            speed = 2;
        }
        xPos += normalizedDX * speed;
        yPos += normalizedDY * speed;
    }


    public boolean checkVerticesHitEpsilon(Epsilon epsilon){
            Rectangle Square = new Rectangle(xPos,yPos,width,height);
            Rectangle epsilonRect = new Rectangle(epsilon.xPos,epsilon.yPos,epsilon.width,epsilon.height);
            if (Square.intersects(epsilonRect)){

                return true;
            }
        return false;
    }




        //impact:Shot _ Square
    public void moveBack(Epsilon epsilon) {
        int moveDistance = 10; // Adjust this value as needed

        // Calculate the direction opposite to the epsilon's direction
        int oppositeX = xPos + (xPos - epsilon.xPos);
        int oppositeY = yPos + (yPos - epsilon.yPos);

        // Move the square enemy back for a brief period
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int count = 0;
            @Override
            public void run() {
                count++;
                if (count <= 1) {
                    xPos += (oppositeX - xPos) / 4; // Move 1/10th of the distance each iteration
                    yPos += (oppositeY - yPos) / 4;
                } else {
                    timer.cancel();
                }
            }
        }, 0, 100); // Adjust the delay and period as needed (0 ms delay, 100 ms period)
    }



    //when a shot hits a square the impact affects other squares'movement:
    public void moveOtherSquaresBack(Epsilon epsilon) {


        // Calculate the direction opposite to the epsilon's direction
        int oppositeX = xPos + (xPos - epsilon.xPos);
        int oppositeY = yPos + (yPos - epsilon.yPos);

        // Move the square enemy back for a brief period
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int count = 0;
            @Override
            public void run() {
                count++;
                if (count <= 1) {
                    xPos += (oppositeX - xPos) / 12; // Move 1/10th of the distance each iteration
                    yPos += (oppositeY - yPos) / 12;
                } else {
                    timer.cancel();
                }
            }
        }, 0, 100); // Adjust the delay and period as needed (0 ms delay, 100 ms period)
    }

}
