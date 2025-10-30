//main controller

package Controller;

import Model.Fish;
import Model.Jellyfish;
import Model.Swimmable;
import View.AddAnimalDialog;
import View.AquaPanel;

import javax.swing.*;
import java.awt.*;


public class AquaFrame extends JFrame {
    private final AquaPanel panel = new AquaPanel();

    //window
    public AquaFrame(){
        super("Aquarium – HW4");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(panel, BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
        setJMenuBar(buildMenu());
        pack(); setLocationRelativeTo(null);
    }


    //buttons
    private JPanel buildButtons(){
        JPanel p = new JPanel(new FlowLayout());
        JButton add=new JButton("Add Animal"), sleep=new JButton("Sleep"), wake=new JButton("Wake up"),
                reset=new JButton("Reset"), food=new JButton("Food"), info=new JButton("Info");
        p.add(add); p.add(sleep); p.add(wake); p.add(reset); p.add(food); p.add(info);
        add.addActionListener(e -> showAddDialog());
        sleep.addActionListener(e -> panel.sleepAll());
        wake.addActionListener(e -> panel.wakeAll());
        reset.addActionListener(e -> panel.reset());
        food.addActionListener(e -> panel.dropFood());
        info.addActionListener(e -> panel.toggleInfo());
        return p;
    }

    // menu bar
    private JMenuBar buildMenu(){
        JMenuBar bar = new JMenuBar();
        JMenu file=new JMenu("File"), bg=new JMenu("Background"), help=new JMenu("Help");
        JMenuItem exit=new JMenuItem("Exit"), img=new JMenuItem("Image"), blue=new JMenuItem("Blue"), none=new JMenuItem("None"), about=new JMenuItem("About");
        exit.addActionListener(e -> System.exit(0));
        file.add(exit);
        img.addActionListener(e -> chooseImage());
        blue.addActionListener(e -> panel.setBackgroundBlue());
        none.addActionListener(e -> panel.clearBackground());
        bg.add(img); bg.add(blue); bg.add(none);
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,"Aquarium HW4 – Gui, Threads, Fish","Help",JOptionPane.INFORMATION_MESSAGE));
        help.add(about);
        bar.add(file); bar.add(bg); bar.add(help);
        return bar;
    }

    //method for choosing custom background
    private void chooseImage(){
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            Image img = Toolkit.getDefaultToolkit().getImage(fc.getSelectedFile().getAbsolutePath());
            panel.setBackgroundImage(img); }
    }

    //adding new fish
    private void showAddDialog() {
        AddAnimalDialog dlg = new AddAnimalDialog(this);
        dlg.setVisible(true);

        if (dlg.isConfirmed()) {


            Swimmable s = dlg.isFish()
                    ? new Fish(dlg.getSizeVal(), 400, 300, 1, 1, dlg.getColorIdx())
                    : new Jellyfish(dlg.getSizeVal(), 400, 300, 1, 1, dlg.getColorIdx());

            s.setHorSpeed(dlg.getHS());
            s.setVerSpeed(dlg.getVS());

            panel.addAnimal(s);
        }
    }

    //main
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new AquaFrame().setVisible(true));
    }
}
