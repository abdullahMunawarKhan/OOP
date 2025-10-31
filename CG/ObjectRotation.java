import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ObjectRotation extends JPanel implements ActionListener {
    private int[] shapeX = {150, 200, 200, 150};
    private int[] shapeY = {100, 100, 150, 150};
    private int[] rotatedX = {150, 200, 200, 150};
    private int[] rotatedY = {100, 100, 150, 150};
    
    private JTextField angleField;
    private JButton rotateButton, resetButton;
    
    public ObjectRotation() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        setupControls();
    }
    
    private void setupControls() {
        JPanel controlPanel = new JPanel();
        
        controlPanel.add(new JLabel("Rotation Angle:"));
        angleField = new JTextField("45", 5);
        controlPanel.add(angleField);
        
        rotateButton = new JButton("Rotate");
        rotateButton.addActionListener(this);
        controlPanel.add(rotateButton);
        
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        controlPanel.add(resetButton);
        
        add(controlPanel, BorderLayout.NORTH);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw original shape in blue
        g.setColor(Color.BLUE);
        g.drawPolygon(shapeX, shapeY, 4);
        g.setColor(new Color(0, 0, 255, 100));
        g.fillPolygon(shapeX, shapeY, 4);
        
        // Draw rotated shape in red
        g.setColor(Color.RED);
        g.drawPolygon(rotatedX, rotatedY, 4);
        g.setColor(new Color(255, 0, 0, 100));
        g.fillPolygon(rotatedX, rotatedY, 4);
        
        // Draw center point (rotation pivot)
        int centerX = (shapeX[0] + shapeX[1] + shapeX[2] + shapeX[3]) / 4;
        int centerY = (shapeY[0] + shapeY[1] + shapeY[2] + shapeY[3]) / 4;
        g.setColor(Color.GREEN);
        g.fillOval(centerX - 3, centerY - 3, 6, 6);
        
        // Show legend
        g.setColor(Color.BLACK);
        g.drawString("Blue: Original", 10, 350);
        g.drawString("Red: Rotated", 10, 365);
        g.drawString("Green: Center", 10, 380);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            resetShape();
            return;
        }
        
        try {
            double angle = Double.parseDouble(angleField.getText());
            rotateShape(angle);
            repaint();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid angle!");
        }
    }
    
    private void rotateShape(double degrees) {
        // Convert degrees to radians
        double angle = Math.toRadians(degrees);
        
        // Find center of the shape
        int centerX = (shapeX[0] + shapeX[1] + shapeX[2] + shapeX[3]) / 4;
        int centerY = (shapeY[0] + shapeY[1] + shapeY[2] + shapeY[3]) / 4;
        
        // Rotate each point around the center
        for (int i = 0; i < shapeX.length; i++) {
            // Get position relative to center
            int x = shapeX[i] - centerX;
            int y = shapeY[i] - centerY;
            
            // Apply rotation formula
            int newX = (int) (x * Math.cos(angle) - y * Math.sin(angle));
            int newY = (int) (x * Math.sin(angle) + y * Math.cos(angle));
            
            // Move back from center
            rotatedX[i] = newX + centerX;
            rotatedY[i] = newY + centerY;
        }
    }
    
    private void resetShape() {
        // Reset to original position
        rotatedX[0] = 150; rotatedY[0] = 100;
        rotatedX[1] = 200; rotatedY[1] = 100;
        rotatedX[2] = 200; rotatedY[2] = 150;
        rotatedX[3] = 150; rotatedY[3] = 150;
        repaint();
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Object Rotation");
        ObjectRotation panel = new ObjectRotation();
        
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
