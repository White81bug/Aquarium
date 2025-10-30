//window for adding fish
package View;

import javax.swing.*;
import java.awt.*;

public class AddAnimalDialog extends JDialog {
    private boolean confirmed = false;

    private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"Fish","Jellyfish"});
    private final JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(20,20,320,1));
    private final JSpinner hSpeedSpinner = new JSpinner(new SpinnerNumberModel(5,1,10,1));
    private final JSpinner vSpeedSpinner = new JSpinner(new SpinnerNumberModel(5,1,10,1));
    private final JComboBox<String> colorBox = new JComboBox<>(new String[]{"Black","Red","Blue","Green","Cyan","Orange","Yellow","Magenta","Pink"});

    //adding animal pop-up
    public AddAnimalDialog(JFrame parent){
        super(parent, "Add Animal", true);
        setLayout(new GridLayout(6,2));
        add(new JLabel("Type:")); add(typeBox);
        add(new JLabel("Size:")); add(sizeSpinner);
        add(new JLabel("Hor speed:")); add(hSpeedSpinner);
        add(new JLabel("Ver speed:")); add(vSpeedSpinner);
        add(new JLabel("Color:")); add(colorBox);
        JButton ok = new JButton("OK"); JButton cancel = new JButton("Cancel");
        add(ok); add(cancel);
        ok.addActionListener(e -> { confirmed = true; dispose(); });
        cancel.addActionListener(e -> dispose());
        pack(); setLocationRelativeTo(parent);
    }

    public boolean isConfirmed(){ return confirmed; }
    public boolean isFish(){ return typeBox.getSelectedIndex() == 0; }
    public int getSizeVal(){ return (int) sizeSpinner.getValue(); }
    public int getHS(){ return (int) hSpeedSpinner.getValue(); }
    public int getVS(){ return (int) vSpeedSpinner.getValue(); }
    public int getColorIdx(){ return colorBox.getSelectedIndex()+1; }
}
