import java.awt.*;
import javax.swing.*;

public class LineDrawingPattern extends JPanel {
    
    public LineDrawingPattern() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw a simple pattern with rectangles and lines
        drawSimplePattern(g);
    }
    
    private void drawSimplePattern(Graphics g) {
        // Set line color
        g.setColor(Color.BLUE);
        
        // Draw outer rectangle
        g.drawRect(50, 50, 200, 150);
        
        // Draw inner rectangle
        g.setColor(Color.RED);
        g.drawRect(100, 75, 100, 100);
        
        // Draw diagonal lines
        g.setColor(Color.GREEN);
        g.drawLine(50, 50, 250, 200);    // Top-left to bottom-right
        g.drawLine(250, 50, 50, 200);    // Top-right to bottom-left
        
        // Draw cross lines
        g.setColor(Color.BLACK);
        g.drawLine(150, 50, 150, 200);   // Vertical line
        g.drawLine(50, 125, 250, 125);   // Horizontal line
        
        // Draw some circles for decoration
        g.setColor(Color.ORANGE);
        g.drawOval(140, 115, 20, 20);    // Center circle
        
        // Draw corner circles
        g.setColor(Color.MAGENTA);
        g.drawOval(45, 45, 10, 10);      // Top-left
        g.drawOval(245, 45, 10, 10);     // Top-right
        g.drawOval(45, 195, 10, 10);     // Bottom-left
        g.drawOval(245, 195, 10, 10);    // Bottom-right
        
        // Add some text
        g.setColor(Color.BLACK);
        g.drawString("Simple Pattern", 130, 30);
        g.drawString("Made with Java!", 125, 230);
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Line Drawing Pattern");
        LineDrawingPattern panel = new LineDrawingPattern();
        
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
