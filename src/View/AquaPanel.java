// main window
package View;

import Model.Swimmable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;


public class AquaPanel extends JPanel {


    private static AquaPanel INSTANCE;

    private final Set<Swimmable> animals = new HashSet<>();

    private Image backgroundImage = null;
    private Color backgroundColor = Color.WHITE;

    private static final int WORM_DIAM = 16;
    private volatile boolean wormPresent = false;
    private int wormX, wormY;

    private static final String[] INFO_COLS = {
            "Animal", "Color", "Size",
            "Hor. speed", "Ver. speed", "Eat counter"
    };
    private final DefaultTableModel tableModel = new DefaultTableModel(INFO_COLS, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable infoTable = new JTable(tableModel);
    private boolean showInfo = false;

    private CyclicBarrier barrier;

    //main window
    public AquaPanel() {
        INSTANCE = this;
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        infoTable.setVisible(false);
        add(infoTable, BorderLayout.SOUTH);

        new Timer(40, e -> repaint()).start();
    }
    //creating instance of a fish
    public void addAnimal(Swimmable s) {
        if (animals.size() >= 5) return;
        animals.add(s);
        s.start();
    }

    // thread status controller
    public void sleepAll() { animals.forEach(Swimmable::setSuspend); }
    public void wakeAll()  { animals.forEach(Swimmable::setResume ); }

    //resetting all
    public void reset() {
        animals.forEach(Thread::interrupt);
        animals.clear();
        wormPresent = false;
        repaint();
    }

    //adding food
    public void dropFood() {
        if (animals.isEmpty() || wormPresent) return;
        wormX = getWidth() / 2;
        wormY = getHeight() / 2;
        wormPresent = true;

        barrier = new CyclicBarrier(animals.size());
        animals.forEach(a -> a.setBarrier(barrier));
    }

    //info panel
    public void toggleInfo() {
        showInfo = !showInfo;
        infoTable.setVisible(showInfo);
        if (showInfo) refreshTable();
    }
    private void refreshTable() {
        tableModel.setRowCount(0);
        animals.forEach(a -> tableModel.addRow(new Object[]{
                a.getAnimalName(),
                a.getColor(),
                a.getSize(),
                a.getHorSpeed(),
                a.getVerSpeed(),
                a.getEatCount()
        }));
    }

    //drawing
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        else {
            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }


        if (wormPresent) {
            g.setColor(new Color(139, 69, 19));
            g.fillOval(wormX - WORM_DIAM / 2,
                    wormY - WORM_DIAM / 2,
                    WORM_DIAM, WORM_DIAM);
        }

        animals.forEach(a -> a.drawAnimal(g));
    }

    //background
    public void setBackgroundImage(Image img) { backgroundImage = img; repaint(); }
    public void setBackgroundBlue()           { backgroundImage = null; backgroundColor = Color.BLUE;  repaint(); }
    public void clearBackground()             { backgroundImage = null; backgroundColor = Color.WHITE; repaint(); }

    public static boolean isWormPresent() { return INSTANCE != null && INSTANCE.wormPresent; }
    public static int     getWormX()      { return INSTANCE != null ? INSTANCE.wormX : 0;    }
    public static int     getWormY()      { return INSTANCE != null ? INSTANCE.wormY : 0;    }

    //plug-in for food interaction processing
    public static void wormEaten(Swimmable s) {
        if (INSTANCE != null && INSTANCE.wormPresent) {
            INSTANCE.wormPresent = false;
            s.eatInc();
            INSTANCE.refreshTable();
        }
    }
}