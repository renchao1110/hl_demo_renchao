package com.hl95.utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import sun.awt.SunHints;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class TestPhoto {
	
	public static void main(String[] args) {
		exportImg2("王世民", "d:/setImage.jpg");
		//exportImg1();
		//pressText("d:/image.jpg", "测试", "华文细黑", Font.PLAIN, 12, Color.RED);
	}
	
	
	public static void exportImg2(String username,String headImg){  
        try {  
            //1.jpg是你的 主图片的路径  
            InputStream is = new FileInputStream("d:/image.jpg");  
            //通过JPEG图象流创建JPEG数据流解码器  
            JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);  
            //解码当前JPEG数据流，返回BufferedImage对象  
            BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();  
            //得到画笔对象  
            Graphics g = buffImg.getGraphics();
            
            //创建你要附加的图象。  
            //小图片的路径  
            ImageIcon imgIcon = new ImageIcon(headImg);   
            
            //得到Image对象。  
            Image img = imgIcon.getImage();  
            //将小图片绘到大图片上。  
            //5,300 .表示你的小图片在大图片上的位置。  
            //g.drawImage(img,10,40,null);  
            g.drawImage(img, 80, 110, 12, 12, null);
            g.drawImage(img, 80, 125, 12, 12, null);
            //设置颜色。  
            //g.setColor(Color.BLACK);  
            //最后一个参数用来设置字体的大小  
            Font f = new Font("楷书",Font.PLAIN,12);  
            Color mycolor = new Color(0, 0, 255);  
            g.setColor(mycolor);  
            g.setFont(f);  
            //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。  
            
//            g2d.setRenderingHint(SunHints.KEY_ANTIALIASING, SunHints.VALUE_ANTIALIAS_OFF);
//            g2d.setRenderingHint(SunHints.KEY_TEXT_ANTIALIASING, SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
//            g2d.setRenderingHint(SunHints.KEY_STROKE_CONTROL, SunHints.VALUE_STROKE_DEFAULT);
//            g2d.setRenderingHint(SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST, 140);
//            g2d.setRenderingHint(SunHints.KEY_FRACTIONALMETRICS, SunHints.VALUE_FRACTIONALMETRICS_OFF);
//            g2d.setRenderingHint(SunHints.KEY_RENDERING, SunHints.VALUE_RENDER_DEFAULT);
            
            
            g.drawString(username,100,120);  
            g.drawString(username,100,135);  
            g.dispose();  
            OutputStream os;  
            //os = new FileOutputStream("d:/union.jpg");  
            String shareFileName = "d:/" + System.currentTimeMillis() + ".jpg";  
            os = new FileOutputStream(shareFileName);  
             //创键编码器，用于编码内存中的图象数据。            
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);  
            en.encode(buffImg);           
            is.close();  
            os.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (ImageFormatException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
    } 
	 public static void exportImg1(){  
	        int width = 100;     
	        int height = 100;     
	        String s = "你好";     
	             
	        File file = new File("d:/66.jpg");     
	             
	        Font font = new Font("Serif", Font.BOLD, 10);     
	        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);     
	        Graphics2D g2 = (Graphics2D)bi.getGraphics();     
	        g2.setBackground(Color.WHITE);     
	        g2.clearRect(0, 0, width, height);     
	        g2.setPaint(Color.RED);     
	             
	        FontRenderContext context = g2.getFontRenderContext();     
	        Rectangle2D bounds = font.getStringBounds(s, context);     
	        double x = (width - bounds.getWidth()) / 2;     
	        double y = (height - bounds.getHeight()) / 2;     
	        double ascent = -bounds.getY();     
	        double baseY = y + ascent;     
	             
	        g2.drawString(s, (int)x, (int)baseY);     
	             
	        try {  
	            ImageIO.write(bi, "jpg", file);  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }   
	    }  
	public static void pressText(String targetImg, String pressText, String fontName, int fontStyle, int fontSize, Color color) {
	        try {
	            File file = new File(targetImg);
	            Image image = ImageIO.read(file);
	            int width = image.getWidth(null);
	            int height = image.getHeight(null);
	            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	            Graphics2D g = bufferedImage.createGraphics();
	            
	            g.setRenderingHint(SunHints.KEY_ANTIALIASING, SunHints.VALUE_ANTIALIAS_OFF);
	            g.setRenderingHint(SunHints.KEY_TEXT_ANTIALIASING, SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
	            g.setRenderingHint(SunHints.KEY_STROKE_CONTROL, SunHints.VALUE_STROKE_DEFAULT);
	            g.setRenderingHint(SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST, 140);
	            g.setRenderingHint(SunHints.KEY_FRACTIONALMETRICS, SunHints.VALUE_FRACTIONALMETRICS_OFF);
	            g.setRenderingHint(SunHints.KEY_RENDERING, SunHints.VALUE_RENDER_DEFAULT);
	            
	            g.setFont(new Font(fontName, fontStyle, fontSize));
	            g.drawImage(image, 0, 0, width, height, null);
	            g.setColor(color);
	            g.drawString(pressText, 150, 200);
	            g.dispose();
	            ImageIO.write(bufferedImage, "jpg", file);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
}
