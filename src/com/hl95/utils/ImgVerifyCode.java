package com.hl95.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;

import org.pj.criterion.core.util.DateFormatUtil;

public class ImgVerifyCode extends HttpServlet {
	 public static Random random = new Random();
	    public static int r(int min,int max){
	        int num=0;
	        num=random.nextInt(max-min)+min;
	        return num;
	    }
	    
	   // public static void main(String[] args) throws IOException {
	    public static Map<String, Object> getImgVerify() throws IOException{   
    	Map<String, Object> map1 = new HashMap<String, Object>();
	        //在内存中创建一副图片
	        int w=120;
	        int h=50;
	        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	        //在图片上画一个矩形当背景
	        Graphics g = img.getGraphics();
	        g.setColor(new Color(r(50,250),r(50,250),r(50,250)));
	        //g.setColor(new Color(250,250,250));
	        g.fillRect(0, 0, w, h);
	         
	        String num = RandomID.createImgVerify();
	       // System.out.println(num);
	        String sdf = num.substring(0, 1);
	       for(int i=0;i<num.length();i++){
	        	sdf = num.substring(i, i+1);
	        	 g.setColor(new Color(r(50,180),r(50,180),r(50,180)));
	        	//g.setColor(new Color(0,0,0));
		         g.setFont(new Font("黑体",Font.PLAIN,30));
		         g.drawString(String.valueOf(sdf), 10+i*30, r(h-30,h));
	        }
	        /*for(int i=0;i<4;i++){
	            g.setColor(new Color(r(50,180),r(50,180),r(50,180)));
	            g.setFont(new Font("黑体",Font.PLAIN,30));
	            char c = str.charAt(r(0,str.length()));
	            g.drawString(String.valueOf(c), 10+i*30, r(h-30,h));
	        }*/
	         
	        //画随机线
	       for(int i=0;i<25;i++){
	            g.setColor(new Color(r(50,180),r(50,180),r(50,180)));
	            g.drawLine(r(0,w), r(0,h),r(0,w), r(0,h));
	        }
	        //把内存中创建的图像输出到文件中
	        String seqNo =MD5Util.MD5(DateFormatUtil.format(new Date(), DateFormatUtil.YYMMDDHHMMSS)+"_"+num);
	        File file =new File(seqNo+".png");
	        ImageIO.write(img, "png", file);
	        String path = file.getCanonicalPath();
	        //System.out.println("图片输出完成");
	        map1.put("num", num);
	        map1.put("path", path);
			return map1;
	        
	    }
}
