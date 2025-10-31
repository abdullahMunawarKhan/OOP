import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SimpleShapeTransformer extends JPanel implements ActionListener {
    private int[] triangleX = {150, 200, 100};
    private int[] triangleY = {100, 200, 200};
    
    private JComboBox<String> transformType;
    private JTextField valueField;
    private JButton applyButton, resetButton;
    
    public SimpleShapeTransformer() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        setupControls();
    }
    
    private void setupControls() {
        JPanel controlPanel = new JPanel();
        
        controlPanel.add(new JLabel("Transform:"));
        transformType = new JComboBox<>(new String[]{"Move", "Scale", "Rotate"});
        controlPanel.add(transformType);
        
        controlPanel.add(new JLabel("Value:"));
        valueField = new JTextField("50", 5);
        controlPanel.add(valueField);
        
        applyButton = new JButton("Apply");
        applyButton.addActionListener(this);
        controlPanel.add(applyButton);
        
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        controlPanel.add(resetButton);
        
        add(controlPanel, BorderLayout.NORTH);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw the triangle
        g.setColor(Color.BLUE);
        g.drawPolygon(triangleX, triangleY, 3);
        g.setColor(new Color(0, 0, 255, 100)); // Light blue fill
        g.fillPolygon(triangleX, triangleY, 3);
        
        // Show current transformation
        g.setColor(Color.BLACK);
        g.drawString("Current Transform: " + transformType.getSelectedItem(), 10, 350);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            resetTriangle();
            return;
        }
        
        try {
            int value = Integer.parseInt(valueField.getText());
            String transform = (String) transformType.getSelectedItem();
            
            switch (transform) {
                case "Move":
                    moveTriangle(value);
                    break;
                case "Scale":
                    scaleTriangle(value);
                    break;
                case "Rotate":
                    rotateTriangle(value);
                    break;
            }
            
            repaint();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!");
        }
    }
    
    private void moveTriangle(int distance) {
        // Move triangle to the right
        for (int i = 0; i < triangleX.length; i++) {
            triangleX[i] += distance;
        }
    }
    
    private void scaleTriangle(int percent) {
        // Scale triangle by percentage (100 = same size)
        double factor = percent / 100.0;
        
        // Find center of triangle
        int centerX = (triangleX[0] + triangleX[1] + triangleX[2]) / 3;
        int centerY = (triangleY[0] + triangleY[1] + triangleY[2]) / 3;
        
        // Scale each point around center
        for (int i = 0; i < triangleX.length; i++) {
            triangleX[i] = (int) (centerX + (triangleX[i] - centerX) * factor);
            triangleY[i] = (int) (centerY + (triangleY[i] - centerY) * factor);
        }
    }
    
    private void rotateTriangle(int degrees) {
        // Convert degrees to radians
        double angle = Math.toRadians(degrees);
        
        // Find center of triangle
        int centerX = (triangleX[0] + triangleX[1] + triangleX[2]) / 3;
        int centerY = (triangleY[0] + triangleY[1] + triangleY[2]) / 3;
        
        // Rotate each point around center
        for (int i = 0; i < triangleX.length; i++) {
            int x = triangleX[i] - centerX;
            int y = triangleY[i] - centerY;
            
            triangleX[i] = (int) (centerX + x * Math.cos(angle) - y * Math.sin(angle));
            triangleY[i] = (int) (centerY + x * Math.sin(angle) + y * Math.cos(angle));
        }
    }
    
    private void resetTriangle() {
        triangleX[0] = 150; triangleY[0] = 100;
        triangleX[1] = 200; triangleY[1] = 200;
        triangleX[2] = 100; triangleY[2] = 200;
        repaint();
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Shape Transformer");
        SimpleShapeTransformer panel = new SimpleShapeTransformer();
        
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
