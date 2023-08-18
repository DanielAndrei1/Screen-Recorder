package screen;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.opencv.core.*;
import org.opencv.core.Size;
import userInterface.ScreenRecorderUI;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import java.io.*;

public class captureImage {
		private int min;
		private int max;
	  
		private int x ;
		private int y;
		private int width;
		private int height;
		private Robot robot;
		private Rectangle area;
		private Size size;
		private BufferedImage bufferedImage;
		private String filename;
		ScreenRecorderUI scrUI;
		 public captureImage(){
				
		  }

		 public captureImage(int min, int max, int x, int y, int width, int height, String filename ) {
			  
			  this.min = min;
			  this.max = max;
			  this.x = x;
			  this.y = y;
			  this.width = width;
			  this.height = height;
			  this.filename = filename;
		  }
		  
		  public captureImage getInt() {
			  
			    this.min = 0;
			    this.max = 10000;
			    this.x = 100;
			    this.y = 100;
			    this.width = 200;
			    this.height = 200;
			    return new captureImage(min, max, x, y, width, height, filename);
			}
		 
	
		  
    	public Robot getRobot() {
    		
    		return this.robot;
    	}
    	
    	public Rectangle areaRCT() {
    		
    		return this.area;
    	}
    	
    	public Size getSize2() {
    		
    		return this.size;
    	}
    	
    	public BufferedImage getBufferedImage() {
    		
    		return this.bufferedImage;
    	}
    	
    	
	  public void saveImages() {
		  System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			
		  
		 try {
				robot = new Robot();
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		    area = new Rectangle(getInt().x, getInt().y, getInt().width, getInt().height);
			area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());		    	
		    bufferedImage = robot.createScreenCapture(area);
		    /*
		    // Create a red border on the image
	        Graphics2D g2d = bufferedImage.createGraphics();
	        g2d.setColor(Color.RED);
	        g2d.drawRect(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width - 1, Toolkit.getDefaultToolkit().getScreenSize().width - 1);
	        g2d.dispose();
			*/
				     }
	  
	
	  // convert BufferedImage to byte[]
	    public static byte[] toByteArray(BufferedImage bi, String format)
	        throws IOException {

	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(bi, format, baos);
	        byte[] bytes = baos.toByteArray();
	        return bytes;

	    }
	    

	 
	    
	    
		public void bufferAssync(byte[] bytes, String pathImage) throws Throwable {
			String file = pathImage;
		    Path path = Paths.get(file);
			 // increase the buffer size if required
		    ByteBuffer buffer = ByteBuffer.allocate(1024*100000);
		  
		    // Write Data to buffer
		   // System.out.println(buffer);

		    buffer.put(bytes, 0, bytes.length);
		    buffer.flip();
		    
		    try( AsynchronousFileChannel asyncChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)){
		      // Write to async channel from buffer
		      // starting from position 0
		      Future<Integer> future =  asyncChannel.write(buffer, 0);
		      while(!future.isDone()) {
		        //System.out.println("Waiting for async write operation... ");
		        // You can do other processing
		        if(ScreenRecorderUI.getStatus().getText() == "Recording Stopped") {
		 			System.out.println("Stop button has been pressed");
		 			asyncChannel.close();
			    	
	 				
	 			} 
		      }            
		      buffer.clear();            
		      future.get();

		    } catch (IOException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		    } catch (InterruptedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		    } catch (ExecutionException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		    } 
		    
		    

		}		  
					    	
	
	 
}
	 
	
