import javax.swing.*;
import java.awt.*;

public class PizzaGUIRunner {
    public static void main(String[] args){
        PizzaGUIFrame frame = new PizzaGUIFrame();
        frame.setTitle("Trey Sime Pizza Order");
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(650, 500);
        frame.getContentPane().setBackground(Color.cyan);
    }
}
