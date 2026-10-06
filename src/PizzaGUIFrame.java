import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class PizzaGUIFrame extends JFrame {
    private static final String[] crusts = {"Thin","Regular","Deep-Dish"};
    private static final String[] sizes = {"Small","Medium","Large"};
    private static  final double[] Base_prices = {8.00,12.00,16.00,20.00};
    private static final String[]toppings = {"Cheese","Tomatoes", "mushrooms","peppers","pepperoni"};

    private static final double topping_price = 1.00;
    private static final double tax_rate = 0.078;

    private final JRadioButton[] crustButtons = new JRadioButton[crusts.length];
    private final ButtonGroup crustGroup = new ButtonGroup();
    private final JComboBox<String> sizeCombo = new JComboBox<>(sizes);
    private final JCheckBox[] toppingBoxes = new JCheckBox[toppings.length];
    private final JTextArea receiptArea = new JTextArea(14,40);

    public PizzaGUIFrame(){
        super("Trey Sims's Resturant Pizza Order");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e){
                confirmQuit();
            }
        });
        JPanel main = new JPanel(new BorderLayout(10,10));
        main.setBorder(new EmptyBorder(10,10,10,10));

        JPanel optionsRow = new JPanel(new GridLayout(1,3,10,10));
        optionsRow.add(createCrustPanel());
        optionsRow.add(createSizePanel());
        optionsRow.add(createToppingsPanel());
        main.add(optionsRow, BorderLayout.NORTH);
        main.add(createRecieptPanel(),BorderLayout.CENTER);
        main.add(createButtonPanel(),BorderLayout.SOUTH);
        add(main);
    }

    public JPanel createCrustPanel(){
        JPanel panel = new JPanel(new GridLayout(crusts.length,1));
        panel.setBorder(new TitledBorder("Crust"));
        for(int i =0; i< crusts.length; i++){
            crustButtons[i] = new JRadioButton(crusts[i]);
            crustGroup.add(crustButtons[i]);
            panel.add(crustButtons[i]);
        }
        return panel;
    }
    public JPanel createSizePanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new TitledBorder("Size"));
        panel.add(sizeCombo);
        return panel;
    }
    public JPanel createToppingsPanel(){
        JPanel panel = new JPanel(new GridLayout(0,2));
        panel.setBorder(new TitledBorder("Toppings ($1.00 each Here)"));
        for(int i=0; i<toppings.length; i++){
            toppingBoxes[i]=new JCheckBox(toppings[i]);
            panel.add(toppingBoxes[i]);
        }
        return  panel;
    }
    public JPanel createRecieptPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Your Order: "));
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Times New Roman",Font.BOLD,15));
        panel.add(new JScrollPane(receiptArea),BorderLayout.CENTER);
        return panel;
    }
    public JPanel createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15,6));

        JButton orderButton = new JButton("Order");
        JButton clearButton = new JButton("Clear");
        JButton quitButton = new JButton("Quit");
        
        orderButton.addActionListener(e -> placeOrder());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> confirmQuit());

        panel.add(orderButton);
        panel.add(clearButton);
        panel.add(quitButton);
        return panel;
    }

    private void confirmQuit(){
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?","Quit",JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }
    private void clearForm(){
        crustGroup.clearSelection();
        sizeCombo.setSelectedIndex(0);
        for(JCheckBox checkBox : toppingBoxes){
            checkBox.setSelected(false);
        }
        receiptArea.setText("");
    }
    private void placeOrder(){
        String crust = null;
        for(JRadioButton radioButton : crustButtons){
            if (radioButton.isSelected()){
                crust = radioButton.getText();
            }
        }
        if (crust == null){
            JOptionPane.showMessageDialog(this, "Please Select Crust.");
            return;
        }
        ArrayList<String> chosen = new ArrayList<>();
        for(JCheckBox checkBox : toppingBoxes){
            if (checkBox.isSelected()){
                chosen.add(checkBox.getText());
            }
        }
        if (chosen.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please choose at least one topping.", "Imcomplete Order",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int sizeIndex = sizeCombo.getSelectedIndex();
        receiptArea.setText(buildReceipt(crust, sizeIndex, chosen));
        receiptArea.setCaretPosition(0);
    }
    static String buildReceipt(String crust, int sizeIndex, List<String> toppings) {
        double base = Base_prices[sizeIndex];
        double subTotal = base + toppings.size() * topping_price;
        double tax = subTotal * tax_rate;
        double total = subTotal + tax;

        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(40)).append("\n");
        sb.append(line(crust + " Crust, " + sizes[sizeIndex], base));
        for (String t : toppings) {
            sb.append(line(t, topping_price));
        }
        sb.append("\n");
        sb.append(line("Sub-total:", subTotal));
        sb.append(line("Tax:", tax));
        sb.append("-".repeat(40)).append("\n");
        sb.append(line("Total:", total));
        sb.append("=".repeat(40)).append("\n");
        return sb.toString();
    }

    private static String line(String label, double amount) {
        return String.format("%-28s%12s%n", label, String.format("$%.2f", amount));
    }
}

