package Application;

import java.awt.AlphaComposite;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class WaterMark {
    
    //String folder = "C:\\Users\\Bassey Oddy\\Desktop\\WaterMark Project\\image";
    
    public void showFile(File file, String waterMark, String outputImage){
        File aFile[] = file.listFiles();
        
        int i = 0;
        for(int j = aFile.length; i<j; i++){
            File files = aFile[i];
            File originalFile = new File(file +"\\"+ files.getName());
            File mark = new File(waterMark);
            
            //Outout file
            int min = 1000, max = 99999;
            int randomNumber = ThreadLocalRandom.current().nextInt(min, max+1);
            
            String filename = "IMG-"+randomNumber;
            File outputFile = new File(outputImage+"\\"+filename+".jpg");
            
            addMark(mark, "png", originalFile, outputFile);
        }
    }
    
    public void addMark(File mark, String type, File file, File dest){
        try{
            BufferedImage image = ImageIO.read(file);
            BufferedImage overLay = resize(ImageIO.read(mark), 150, 150);
            
            int imageType = "png".equalsIgnoreCase(type)? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
            BufferedImage waterMarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);
            
            Graphics2D g = (Graphics2D) waterMarked.getGraphics();
            g.drawImage(image, 0, 0, null);
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
            g.setComposite(alphaChannel);
            
            //Position WaterMark
            int centerX = image.getWidth()/2;
            int centerY = image.getHeight()/2;
            
            g.drawImage(overLay, centerX, centerY, null);
            ImageIO.write(waterMarked, type, dest);
            
            g.dispose();
            
        } catch (IOException ex) {
            Logger.getLogger(WaterMark.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private BufferedImage resize(BufferedImage image, int height, int width) {
        Image temp = image.getScaledInstance(width, height, image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();
        
        return resizedImage;
    }
}

