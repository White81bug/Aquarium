//base for animals
package Model;

import java.awt.*;
import java.util.concurrent.CyclicBarrier;

public abstract class Swimmable extends Thread {

    protected int horSpeed;
    protected int verSpeed;


    public Swimmable()               { this(0,0); }
    public Swimmable(int h,int v)    { horSpeed=h; verSpeed=v; }

    public int  getHorSpeed()        { return horSpeed; }
    public int  getVerSpeed()        { return verSpeed; }
    public void setHorSpeed(int s)   { horSpeed=s; }
    public void setVerSpeed(int s)   { verSpeed=s; }

    public abstract void setSuspend();
    public abstract void setResume();
    public abstract void setBarrier(CyclicBarrier b);

    protected abstract void move();

    public abstract String  getAnimalName();
    public abstract void    drawAnimal(Graphics g);
    public abstract int     getSize();
    public abstract void    eatInc();
    public abstract int     getEatCount();
    public abstract String  getColor();
}
