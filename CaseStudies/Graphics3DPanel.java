import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Graphics3DPanel extends JPanel implements ActionListener {
    private int rotationX = 0;
    private int rotationY = 0;
    private int shapeSize = 100;
    private String currentShape = "Cube";
    
    private JComboBox<String> shapeBox;
    private JSlider rotXSlider, rotYSlider, sizeSlider;
    private JCheckBox animateBox;
    private Timer animationTimer;
    
    public Graphics3DPanel() {
        setPreferredSize(new Dimension(600, 500));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        
        setupControls();
        setupAnimation();
    }
    
    private void setupControls() {
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.GRAY);
        
        controlPanel.add(new JLabel("Shape:"));
        shapeBox = new JComboBox<>(new String[]{"Cube", "Pyramid"});
        shapeBox.addActionListener(this);
        controlPanel.add(shapeBox);
        
        controlPanel.add(new JLabel("Rotate X:"));
        rotXSlider = new JSlider(0, 360, 0);
        rotXSlider.addChangeListener(e -> {
            rotationX = rotXSlider.getValue();
            repaint();
        });
        controlPanel.add(rotXSlider);
        
        controlPanel.add(new JLabel("Rotate Y:"));
        rotYSlider = new JSlider(0, 360, 0);
        rotYSlider.addChangeListener(e -> {
            rotationY = rotYSlider.getValue();
            repaint();
        });
        controlPanel.add(rotYSlider);
        
        controlPanel.add(new JLabel("Size:"));
        sizeSlider = new JSlider(50, 200, 100);
        sizeSlider.addChangeListener(e -> {
            shapeSize = sizeSlider.getValue();
            repaint();
        });
        controlPanel.add(sizeSlider);
        
        animateBox = new JCheckBox("Animate");
        animateBox.addActionListener(this);
        controlPanel.add(animateBox);
        
        add(controlPanel, BorderLayout.NORTH);
    }
    
    private void setupAnimation() {
        animationTimer = new Timer(50, e -> {
            rotationY = (rotationY + 2) % 360;
            rotYSlider.setValue(rotationY);
            repaint();
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        
        if (currentShape.equals("Cube")) {
            drawSimpleCube(g2d, centerX, centerY);
        } else {
            drawSimplePyramid(g2d, centerX, centerY);
        }
        
        // Show info
        g2d.setColor(Color.WHITE);
        g2d.drawString("3D Shape: " + currentShape, 10, getHeight() - 20);
    }
    
    private void drawSimpleCube(Graphics2D g2d, int centerX, int centerY) {
        // Calculate simple 3D effect with basic math
        double angleX = Math.toRadians(rotationX);
        double angleY = Math.toRadians(rotationY);
        
        int size = shapeSize;
        
        // Define 8 cube vertices (simplified 3D coordinates)
        int[][] vertices = {
            {-size/2, -size/2, -size/2}, // 0: back-bottom-left
            {size/2, -size/2, -size/2},  // 1: back-bottom-right
            {size/2, size/2, -size/2},   // 2: back-top-right
            {-size/2, size/2, -size/2},  // 3: back-top-left
            {-size/2, -size/2, size/2},  // 4: front-bottom-left
            {size/2, -size/2, size/2},   // 5: front-bottom-right
            {size/2, size/2, size/2},    // 6: front-top-right
            {-size/2, size/2, size/2}    // 7: front-top-left
        };
        
        // Project 3D points to 2D (simple perspective)
        Point[] points = new Point[8];
        for (int i = 0; i < 8; i++) {
            points[i] = project3DTo2D(vertices[i], angleX, angleY, centerX, centerY);
        }
        
        // Draw cube faces
        g2d.setStroke(new BasicStroke(2));
        
        // Back face (darker)
        g2d.setColor(Color.BLUE);
        drawQuad(g2d, points[0], points[1], points[2], points[3]);
        
        // Front face (brighter)
        g2d.setColor(Color.CYAN);
        drawQuad(g2d, points[4], points[5], points[6], points[7]);
        
        // Connect edges
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            g2d.drawLine(points[i].x, points[i].y, points[i+4].x, points[i+4].y);
        }
    }
    
    private void drawSimplePyramid(Graphics2D g2d, int centerX, int centerY) {
        double angleX = Math.toRadians(rotationX);
        double angleY = Math.toRadians(rotationY);
        
        int size = shapeSize;
        
        // Define 5 pyramid vertices
        int[][] vertices = {
            {0, -size, 0},        // 0: apex
            {-size/2, size/2, -size/2}, // 1: base corner 1
            {size/2, size/2, -size/2},  // 2: base corner 2
            {size/2, size/2, size/2},   // 3: base corner 3
            {-size/2, size/2, size/2}   // 4: base corner 4
        };
        
        // Project to 2D
        Point[] points = new Point[5];
        for (int i = 0; i < 5; i++) {
            points[i] = project3DTo2D(vertices[i], angleX, angleY, centerX, centerY);
        }
        
        // Draw pyramid faces
        g2d.setStroke(new BasicStroke(2));
        
        // Base
        g2d.setColor(Color.RED);
        drawQuad(g2d, points[1], points[2], points[3], points[4]);
        
        // Triangular faces
        g2d.setColor(Color.ORANGE);
        drawTriangle(g2d, points[0], points[1], points[2]);
        drawTriangle(g2d, points[0], points[2], points[3]);
        
        g2d.setColor(Color.YELLOW);
        drawTriangle(g2d, points[0], points[3], points[4]);
        drawTriangle(g2d, points[0], points[4], points[1]);
    }
    
    private Point project3DTo2D(int[] vertex3D, double angleX, double angleY, int centerX, int centerY) {
        // Simple 3D to 2D projection with rotation
        double x = vertex3D[0];
        double y = vertex3D[1];
        double z = vertex3D[2];
        
        // Rotate around Y axis
        double newX = x * Math.cos(angleY) - z * Math.sin(angleY);
        double newZ = x * Math.sin(angleY) + z * Math.cos(angleY);
        
        // Rotate around X axis
        double newY = y * Math.cos(angleX) - newZ * Math.sin(angleX);
        newZ = y * Math.sin(angleX) + newZ * Math.cos(angleX);
        
        // Simple perspective (divide by distance)
        double perspective = 1.0 / (1.0 + newZ / 500.0);
        
        int projX = centerX + (int) (newX * perspective);
        int projY = centerY + (int) (newY * perspective);
        
        return new Point(projX, projY);
    }
    
    private void drawQuad(Graphics2D g2d, Point p1, Point p2, Point p3, Point p4) {
        int[] xPoints = {p1.x, p2.x, p3.x, p4.x};
        int[] yPoints = {p1.y, p2.y, p3.y, p4.y};
        g2d.fillPolygon(xPoints, yPoints, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, 4);
    }
    
    private void drawTriangle(Graphics2D g2d, Point p1, Point p2, Point p3) {
        int[] xPoints = {p1.x, p2.x, p3.x};
        int[] yPoints = {p1.y, p2.y, p3.y};
        g2d.fillPolygon(xPoints, yPoints, 3);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, 3);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == shapeBox) {
            currentShape = (String) shapeBox.getSelectedItem();
            repaint();
        } else if (e.getSource() == animateBox) {
            if (animateBox.isSelected()) {
                animationTimer.start();
            } else {
                animationTimer.stop();
            }
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple 3D Graphics");
        Graphics3DPanel panel = new Graphics3DPanel();
        
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
