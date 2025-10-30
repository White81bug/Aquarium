//jellyfish
package Model;

import java.awt.*;
import java.util.concurrent.CyclicBarrier;
import View.AquaPanel;

public class Jellyfish extends Swimmable implements Comparable<Jellyfish> {
    private static final int EAT_DISTANCE = 10;

    private int   size,eatCount;
    private Color col;
    private int   x_front,y_front,x_dir,y_dir;

    private volatile boolean suspended=false;
    private final Object suspendLock=new Object();
    private CyclicBarrier barrier;

    //constructor
    public Jellyfish(int size,int x,int y,int xd,int yd,int colorIdx){
        super(1+(int)(Math.random()*10),1+(int)(Math.random()*10));
        this.size=size; x_front=x; y_front=y; x_dir=xd==0?1:xd; y_dir=yd==0?1:yd;
        col = Fish.setColor(colorIdx);
    }

    //thread methods
    @Override public void setSuspend(){ suspended=true; }
    @Override public void setResume(){ suspended=false; synchronized (suspendLock){ suspendLock.notifyAll(); } }
    @Override public void setBarrier(CyclicBarrier b){ barrier=b; }

    @Override public void run(){
        try {
            if(barrier!=null) barrier.await();
            while(!isInterrupted()){
                synchronized (suspendLock){ while(suspended) suspendLock.wait(); }
                move();
                Thread.sleep(40);
            }
        }catch(Exception e){ interrupt(); }
    }

    //movement
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

    @Override public String getAnimalName(){ return "Jellyfish"; }
    @Override public int    getSize(){ return size; }
    @Override public int    getEatCount(){ return eatCount; }
    @Override public void   eatInc(){ eatCount++; if(eatCount>=EAT_DISTANCE){ size++; eatCount=0; }}

    //drawing
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
    @Override public void drawAnimal(Graphics g) {
        int legs = (size < 40) ? 5 : (size < 80 ? 9 : 12);
        g.setColor(col);
        g.fillArc(x_front - size / 2, y_front - size / 4, size, size / 2, 0, 180);
        for (int i = 0; i < legs; i++)
            g.drawLine(x_front - size / 2 + size / legs + size * i / (legs + 1), y_front,
                    x_front - size / 2 + size / legs + size * i / (legs + 1), y_front + size / 3);
    }

    @Override public int compareTo(Jellyfish o) { return Integer.compare(this.size, o.size); }
}