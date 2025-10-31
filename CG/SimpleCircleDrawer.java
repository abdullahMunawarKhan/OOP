import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SimpleCircleDrawer extends JPanel implements ActionListener {
    private int radius = 50;
    private JTextField radiusField;
    private JButton drawButton;
    
    public SimpleCircleDrawer() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        setupControls();
    }
    
    private void setupControls() {
        JPanel controlPanel = new JPanel();
        
        controlPanel.add(new JLabel("Radius:"));
        radiusField = new JTextField("50", 5);
        controlPanel.add(radiusField);
        
        drawButton = new JButton("Draw Circle");
        drawButton.addActionListener(this);
        controlPanel.add(drawButton);
        
        add(controlPanel, BorderLayout.NORTH);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw a simple circle in the center
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        
        g.setColor(Color.BLACK);
        g.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        
        // Show radius info
        g.drawString("Radius: " + radius, 10, 30);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            radius = Integer.parseInt(radiusField.getText());
            if (radius <= 0 || radius > 150) {
                radius = 50;
                radiusField.setText("50");
                JOptionPane.showMessageDialog(this, "Please enter radius between 1 and 150");
            }
        } catch (NumberFormatException ex) {
            radius = 50;
            radiusField.setText("50");
            JOptionPane.showMessageDialog(this, "Please enter a valid number");
        }
        
        repaint(); // Redraw the circle
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Circle Drawer");
        SimpleCircleDrawer panel = new SimpleCircleDrawer();
        
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
