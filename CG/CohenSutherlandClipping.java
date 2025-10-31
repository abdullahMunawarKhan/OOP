import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CohenSutherlandClipping extends JPanel implements ActionListener {
    private int windowX = 100, windowY = 100, windowWidth = 200, windowHeight = 150;
    private int lineX1 = 50, lineY1 = 50, lineX2 = 350, lineY2 = 300;
    
    private JTextField x1Field, y1Field, x2Field, y2Field;
    private JButton clipButton, resetButton;
    private boolean showClipped = false;
    
    public CohenSutherlandClipping() {
        setPreferredSize(new Dimension(500, 400));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        setupControls();
    }
    
    private void setupControls() {
        JPanel controlPanel = new JPanel();
        
        controlPanel.add(new JLabel("Line from ("));
        x1Field = new JTextField(String.valueOf(lineX1), 3);
        controlPanel.add(x1Field);
        controlPanel.add(new JLabel(","));
        y1Field = new JTextField(String.valueOf(lineY1), 3);
        controlPanel.add(y1Field);
        controlPanel.add(new JLabel(") to ("));
        x2Field = new JTextField(String.valueOf(lineX2), 3);
        controlPanel.add(x2Field);
        controlPanel.add(new JLabel(","));
        y2Field = new JTextField(String.valueOf(lineY2), 3);
        controlPanel.add(y2Field);
        controlPanel.add(new JLabel(")"));
        
        clipButton = new JButton("Clip Line");
        clipButton.addActionListener(this);
        controlPanel.add(clipButton);
        
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        controlPanel.add(resetButton);
        
        add(controlPanel, BorderLayout.NORTH);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw the clipping window (rectangle)
        g.setColor(Color.BLUE);
        g.drawRect(windowX, windowY, windowWidth, windowHeight);
        g.drawString("Clipping Window", windowX + 10, windowY - 5);
        
        // Draw the original line
        g.setColor(Color.RED);
        g.drawLine(lineX1, lineY1, lineX2, lineY2);
        g.drawString("Original Line", lineX1 + 10, lineY1 - 10);
        
        // Draw clipped line if button was clicked
        if (showClipped) {
            drawClippedLine(g);
        }
    }
    
    private void drawClippedLine(Graphics g) {
        // Simple clipping - check if line is inside window
        int clippedX1 = Math.max(lineX1, windowX);
        int clippedY1 = Math.max(lineY1, windowY);
        int clippedX2 = Math.min(lineX2, windowX + windowWidth);
        int clippedY2 = Math.min(lineY2, windowY + windowHeight);
        
        // Check if there's something to draw
        if (clippedX1 <= clippedX2 && clippedY1 <= clippedY2) {
            // Check if line actually passes through the window
            if (isLineInWindow()) {
                g.setColor(Color.GREEN);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(3));
                
                // Simple clipping by restricting drawing to window area
                Shape oldClip = g.getClip();
                g.setClip(windowX, windowY, windowWidth, windowHeight);
                g.drawLine(lineX1, lineY1, lineX2, lineY2);
                g.setClip(oldClip);
                
                g.setColor(Color.BLACK);
                g.drawString("Clipped Line (Green)", windowX + 10, windowY + windowHeight + 20);
            } else {
                g.setColor(Color.BLACK);
                g.drawString("Line is outside window - Rejected", windowX, windowY + windowHeight + 20);
            }
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Line is outside window - Rejected", windowX, windowY + windowHeight + 20);
        }
    }
    
    private boolean isLineInWindow() {
        // Simple check: if any part of line is in window
        return !(lineX2 < windowX || lineX1 > windowX + windowWidth ||
                 lineY2 < windowY || lineY1 > windowY + windowHeight);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            lineX1 = 50; lineY1 = 50;
            lineX2 = 350; lineY2 = 300;
            showClipped = false;
            
            x1Field.setText(String.valueOf(lineX1));
            y1Field.setText(String.valueOf(lineY1));
            x2Field.setText(String.valueOf(lineX2));
            y2Field.setText(String.valueOf(lineY2));
            
            repaint();
            return;
        }
        
        if (e.getSource() == clipButton) {
            try {
                lineX1 = Integer.parseInt(x1Field.getText());
                lineY1 = Integer.parseInt(y1Field.getText());
                lineX2 = Integer.parseInt(x2Field.getText());
                lineY2 = Integer.parseInt(y2Field.getText());
                
                showClipped = true;
                repaint();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
            }
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Line Clipping");
        CohenSutherlandClipping panel = new CohenSutherlandClipping();
        
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
