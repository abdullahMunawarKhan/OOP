import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

class SimpleImageEditor {
    
    // Simple brightness adjustment
    public BufferedImage makeBrighter(BufferedImage image, int amount) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                
                int red = Math.min(255, color.getRed() + amount);
                int green = Math.min(255, color.getGreen() + amount);
                int blue = Math.min(255, color.getBlue() + amount);
                
                Color newColor = new Color(red, green, blue);
                newImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return newImage;
    }
    
    // Simple horizontal flip
    public BufferedImage flipImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                flipped.setRGB(width - 1 - x, y, image.getRGB(x, y));
            }
        }
        return flipped;
    }
    
    // Convert to grayscale
    public BufferedImage makeGrayscale(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage gray = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                
                int grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                Color grayColor = new Color(grayValue, grayValue, grayValue);
                
                gray.setRGB(x, y, grayColor.getRGB());
            }
        }
        return gray;
    }
}

public class ImageAugmentationPanel extends JPanel implements ActionListener {
    private BufferedImage originalImage;
    private BufferedImage editedImage;
    private SimpleImageEditor editor;
    
    private JComboBox<String> effectBox;
    private JSlider brightnessSlider;
    private JButton applyButton, resetButton;
    
    public ImageAugmentationPanel() {
        setPreferredSize(new Dimension(600, 500));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        editor = new SimpleImageEditor();
        createSampleImage();
        setupControls();
    }
    
    private void createSampleImage() {
        // Create a simple colorful image for testing
        originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = originalImage.createGraphics();
        
        // Draw colorful squares
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 100, 100);
        
        g2d.setColor(Color.BLUE);
        g2d.fillRect(100, 0, 100, 100);
        
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 100, 100, 100);
        
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(100, 100, 100, 100);
        
        g2d.dispose();
        editedImage = originalImage;
    }
    
    private void setupControls() {
        JPanel controlPanel = new JPanel();
        
        controlPanel.add(new JLabel("Effect:"));
        effectBox = new JComboBox<>(new String[]{"Brightness", "Flip", "Grayscale"});
        controlPanel.add(effectBox);
        
        controlPanel.add(new JLabel("Brightness:"));
        brightnessSlider = new JSlider(-50, 50, 0);
        brightnessSlider.setPreferredSize(new Dimension(150, 30));
        controlPanel.add(brightnessSlider);
        
        applyButton = new JButton("Apply Effect");
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
        
        // Draw original image
        if (originalImage != null) {
            g.drawImage(originalImage, 50, 80, null);
            g.setColor(Color.BLACK);
            g.drawString("Original", 50, 70);
        }
        
        // Draw edited image
        if (editedImage != null) {
            g.drawImage(editedImage, 300, 80, null);
            g.setColor(Color.BLACK);
            g.drawString("Edited", 300, 70);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            editedImage = originalImage;
            repaint();
            return;
        }
        
        if (e.getSource() == applyButton) {
            String effect = (String) effectBox.getSelectedItem();
            
            switch (effect) {
                case "Brightness":
                    int brightness = brightnessSlider.getValue();
                    editedImage = editor.makeBrighter(originalImage, brightness);
                    break;
                case "Flip":
                    editedImage = editor.flipImage(originalImage);
                    break;
                case "Grayscale":
                    editedImage = editor.makeGrayscale(originalImage);
                    break;
            }
            repaint();
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Image Editor");
        ImageAugmentationPanel panel = new ImageAugmentationPanel();
        
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
