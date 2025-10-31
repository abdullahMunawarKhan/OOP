 import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Transformations3D extends JPanel implements ActionListener {
    private int[] shapeX = {150, 200, 175};
    private int[] shapeY = {100, 100, 150};
    private int[] transformedX = {150, 200, 175};
    private int[] transformedY = {100, 100, 150};
    
    private JComboBox<String> transformBox;
    private JTextField valueField;
    private JButton applyButton, resetButton;
    
    public Transformations3D() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        setupControls();
    }
    
    private void setupControls() {
        JPanel controlPanel = new JPanel();
        
        controlPanel.add(new JLabel("Transform:"));
        transformBox = new JComboBox<>(new String[]{"Move", "Scale", "Rotate"});
        controlPanel.add(transformBox);
        
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
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw original shape (blue triangle)
        g2d.setColor(Color.BLUE);
        g2d.drawPolygon(shapeX, shapeY, 3);
        g2d.setColor(new Color(0, 0, 255, 100));
        g2d.fillPolygon(shapeX, shapeY, 3);
        
        // Draw transformed shape (red triangle)
        g2d.setColor(Color.RED);
        g2d.drawPolygon(transformedX, transformedY, 3);
        g2d.setColor(new Color(255, 0, 0, 100));
        g2d.fillPolygon(transformedX, transformedY, 3);
        
        // Draw legend
        g2d.setColor(Color.BLACK);
        g2d.drawString("Blue: Original Shape", 10, 350);
        g2d.drawString("Red: Transformed Shape", 10, 365);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            resetShape();
            return;
        }
        
        try {
            int value = Integer.parseInt(valueField.getText());
            String transform = (String) transformBox.getSelectedItem();
            
            switch (transform) {
                case "Move":
                    moveShape(value);
                    break;
                case "Scale":
                    scaleShape(value);
                    break;
                case "Rotate":
                    rotateShape(value);
                    break;
            }
            
            repaint();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!");
        }
    }
    
    private void moveShape(int distance) {
        // Move shape to the right by distance
        for (int i = 0; i < shapeX.length; i++) {
            transformedX[i] = shapeX[i] + distance;
        }
        // Copy Y coordinates (no vertical movement)
        System.arraycopy(shapeY, 0, transformedY, 0, shapeY.length);
    }
    
    private void scaleShape(int percent) {
        // Scale shape by percentage (100 = same size, 50 = half, 200 = double)
        double factor = percent / 100.0;
        
        // Find center of shape
        int centerX = (shapeX[0] + shapeX[1] + shapeX[2]) / 3;
        int centerY = (shapeY[0] + shapeY[1] + shapeY[2]) / 3;
        
        // Scale each point around center
        for (int i = 0; i < shapeX.length; i++) {
            transformedX[i] = (int) (centerX + (shapeX[i] - centerX) * factor);
            transformedY[i] = (int) (centerY + (shapeY[i] - centerY) * factor);
        }
    }
    
    private void rotateShape(int degrees) {
        // Convert degrees to radians
        double angle = Math.toRadians(degrees);
        
        // Find center of shape
        int centerX = (shapeX[0] + shapeX[1] + shapeX[2]) / 3;
        int centerY = (shapeY[0] + shapeY[1] + shapeY[2]) / 3;
        
        // Rotate each point around center
        for (int i = 0; i < shapeX.length; i++) {
            // Get position relative to center
            int x = shapeX[i] - centerX;
            int y = shapeY[i] - centerY;
            
            // Apply rotation formulas
            int newX = (int) (x * Math.cos(angle) - y * Math.sin(angle));
            int newY = (int) (x * Math.sin(angle) + y * Math.cos(angle));
            
            // Move back from center
            transformedX[i] = newX + centerX;
            transformedY[i] = newY + centerY;
        }
    }
    
    private void resetShape() {
        // Reset to original position
        System.arraycopy(shapeX, 0, transformedX, 0, shapeX.length);
        System.arraycopy(shapeY, 0, transformedY, 0, shapeY.length);
        repaint();
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple 2D Transformations");
        Transformations3D panel = new Transformations3D();
        
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
