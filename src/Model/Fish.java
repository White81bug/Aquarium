//fish class
package Model;

import java.awt.*;
import java.util.concurrent.CyclicBarrier;
import View.AquaPanel;

public class Fish extends Swimmable implements Comparable<Fish> {
    private static final int EAT_DISTANCE = 10;

    private int   size;
    private Color col;
    private int   eatCount;
    private int   x_front, y_front;
    private int   x_dir,   y_dir;

    private volatile boolean suspended = false;
    private final    Object  suspendLock = new Object();
    private          CyclicBarrier barrier;

//constructor
    public Fish(int size,int x,int y,int xd,int yd,int colorIdx){
        super(0, 0);
        this.size=size; x_front=x; y_front=y; x_dir=xd==0?1:xd; y_dir=yd==0?1:yd;
        this.col = setColor(colorIdx);
    }

//therad methods
    @Override public void setSuspend(){ suspended=true; }
    @Override public void setResume(){ suspended=false; synchronized (suspendLock){ suspendLock.notifyAll(); } }
    @Override public void setBarrier(CyclicBarrier b){ barrier=b; }

    @Override public void run(){
        try {
            if(barrier!=null) barrier.await();
            while (!isInterrupted()){
                synchronized (suspendLock){ while (suspended) suspendLock.wait(); }
                move();
                Thread.sleep(40);
            }
        } catch (Exception e){ interrupt(); }
    }

    //moving fish
    @Override
    protected void move() {


        if (AquaPanel.isWormPresent()) {
            x_dir = Integer.compare(AquaPanel.getWormX() - x_front, 0);
            y_dir = Integer.compare(AquaPanel.getWormY() - y_front, 0);
            if (x_dir == 0) x_dir = 1;
            if (y_dir == 0) y_dir = 1;
        }

        x_front += horSpeed * x_dir;
        y_front += verSpeed * y_dir;

        if (x_front >= 780 || x_front <= 20) x_dir *= -1;
        if (y_front >= 580 || y_front <= 20) y_dir *= -1;

        if (AquaPanel.isWormPresent() &&
                Math.abs(x_front - AquaPanel.getWormX()) < getSize() &&
                Math.abs(y_front - AquaPanel.getWormY()) < getSize()) {
            AquaPanel.wormEaten(this);
        }
    }

//setting color
    public static Color setColor(int col){
        return switch (col){
            case 1->Color.BLACK; case 2->Color.RED; case 3->Color.BLUE;
            case 4->Color.GREEN; case 5->Color.CYAN; case 6->Color.ORANGE;
            case 7->Color.YELLOW; case 8->Color.MAGENTA; case 9->Color.PINK;
            default->Color.GRAY; };
    }

    @Override public String getAnimalName(){ return "Fish"; }
    @Override public int    getSize(){ return size; }
    @Override public int    getEatCount(){ return eatCount; }

    @Override public void   eatInc(){ eatCount++; if(eatCount>=EAT_DISTANCE){ size++; eatCount=0; }}

    @Override public String getColor(){
        return switch (col.getRGB()){
            case 0xFF000000 -> "Black";
            case 0xFFFF0000 -> "Red";
            case 0xFF0000FF -> "Blue";
            case 0xFF00FF00 -> "Green";
            case 0xFF00FFFF -> "Cyan";
            case 0xFFFFA500 -> "Orange";
            case 0xFFFFFF00 -> "Yellow";
            case 0xFFFF00FF -> "Magenta";
            case 0xFFFFC0CB -> "Pink";
            default          -> "Gray"; };
    }
//drawing fish
    @Override public void drawAnimal(Graphics g){
        g.setColor(col);
        if(x_dir==1){
            g.fillOval(x_front-size,y_front-size/4,size,size/2);
            int[] xt={x_front-size-size/4,x_front-size-size/4,x_front-size};
            int[] yt={y_front-size/4,y_front+size/4,y_front};
            g.fillPolygon(new Polygon(xt,yt,3));
            Graphics2D g2=(Graphics2D)g;
            g2.setColor(new Color(255-col.getRed(),255-col.getGreen(),255-col.getBlue()));
            g2.fillOval(x_front-size/5,y_front-size/10,size/10,size/10);
            setStroke(g2); g2.drawLine(x_front,y_front,x_front-size/10,y_front+size/10); g2.setStroke(new BasicStroke(1));
        }else{
            g.fillOval(x_front,y_front-size/4,size,size/2);
            int[] xt={x_front+size+size/4,x_front+size+size/4,x_front+size};
            int[] yt={y_front-size/4,y_front+size/4,y_front};
            g.fillPolygon(new Polygon(xt,yt,3));
            Graphics2D g2=(Graphics2D)g;
            g2.setColor(new Color(255-col.getRed(),255-col.getGreen(),255-col.getBlue()));
            g2.fillOval(x_front+size/10,y_front-size/10,size/10,size/10);
            setStroke(g2); g2.drawLine(x_front,y_front,x_front+size/10,y_front+size/10); g2.setStroke(new BasicStroke(1));
        }
    }

    private void setStroke(Graphics2D g){ g.setStroke(new BasicStroke(size>70?3:size>30?2:1)); }

    private void changeColor(){
        int idx=(col.equals(Color.BLACK)?1: col.equals(Color.RED)?2: col.equals(Color.BLUE)?3:
                col.equals(Color.GREEN)?4: col.equals(Color.CYAN)?5: col.equals(Color.ORANGE)?6:
                        col.equals(Color.YELLOW)?7: col.equals(Color.MAGENTA)?8: col.equals(Color.PINK)?9:0);
        col = setColor(idx%9+1);
    }

    @Override public int compareTo(Fish o){ return Integer.compare(size,o.size);} }
