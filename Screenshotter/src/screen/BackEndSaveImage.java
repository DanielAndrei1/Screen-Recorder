package screen;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoWriter;

import audio.FFmpegAudioVideoMerger;
import userInterface.ScreenRecorderUI;

public class BackEndSaveImage extends Thread {

	private volatile boolean execute;
	private volatile boolean stopTryCatch;
	private volatile boolean stopWhile;
	private volatile static boolean checkIfThreadAlreadyStarted;
	private static String path;
	private byte[] bytes;
	static String pathImage;

	private int threadint;
    captureImage rxs = new captureImage();
    
    public BackEndSaveImage(int param) {
    	this.threadint = param;
    }
    
    public boolean getExceute() {
    	
    	return execute;
    }
  
   public static String getVideoAvi() {
   	
   	return path;
   }
   
   public static String getImageFiles() {
	   return pathImage;
   }
   public static boolean  getcheckIfThreadAlreadyStarted() {
    	
    	return checkIfThreadAlreadyStarted;
    }
   
   public static void  getcheckIfThreadAlreadyStarted(boolean param) {
   	
   	checkIfThreadAlreadyStarted= param;
   	
   }
   
    private static int threadNumber = 0;
    //Thread Executor
    private static ExecutorService executorAudio;
	private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    @Override
	public void run(){  
    	this.execute = true;
    	this.stopTryCatch = false;
    	this.stopWhile = false;
    	if(ScreenRecorderUI.driveSelectiInputTextField.getText().isEmpty() == false) {
    		pathImage = ScreenRecorderUI.driveSelectiInputTextField.getText() + ":" + File.separator + "image.txt";
		    }else {
		    pathImage = "D:" + File.separator + "image.txt";
		    	
		    }
    	// Use relative path for Unix systems
    	File g = new File(pathImage);

    	g.getParentFile().mkdirs(); 
    	try {
			g.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//Start thread Audio 
        threadNumber += 1;
        
        // Create the thread pools outside the loop
        executorAudio = Executors.newFixedThreadPool(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
       
        	
        	System.out.println(AudioSave.getcheckIfThreadAlreadyStarted());
    	if(!AudioSave.getcheckIfThreadAlreadyStarted()) {
    		executorAudio.execute(new AudioSave(i));
    	}
    	AudioSave.getcheckIfThreadAlreadyStarted(false);
		threadNumber = 0;

        
        }
   
    	
    	
    	
    	while (this.execute) {
    		
    		if(stopWhile == true) {
    			break;
    		}
		System.out.println("thread  " + threadint + " is running..."); 
		 System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			
		   

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			
			Size size = new Size();
			 
			size.width = Toolkit.getDefaultToolkit().getScreenSize().width;	
			 
			size.height = Toolkit.getDefaultToolkit().getScreenSize().height;	

			int codec = VideoWriter.fourcc('M', 'J', 'P', 'G');  // select desired codec (must be available at runtime)
			
						
			
			
		    int fps = 5;		
		    

		    VideoWriter writer = new VideoWriter();
		     
		    //String filename = ScreenRecorderUI.file + sdf1.format(timestamp) + ".avi";
		    if(ScreenRecorderUI.driveSelectiInputTextField.getText().isEmpty() == false) {
		    path = ScreenRecorderUI.driveSelectiInputTextField.getText() + ":" + File.separator + sdf1.format(timestamp) + ".avi";
		    }else {
		    path = "D:" + File.separator +  sdf1.format(timestamp) + ".avi";
		    	
		    }
		
		    
		 File f = new File(path);

		 f.getParentFile().mkdirs(); 
		 try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    writer.open(f.toString(), codec, fps, size);
		    System.out.println(f.toString());
    	  try {
    		  if(stopTryCatch == true) {
    			  break;
    			  
    		  }
    
             
              for(; ;) {
            	  
            	 
              Thread.sleep(100);
              rxs.saveImages();
              
              try {
      			bytes = captureImage.toByteArray(rxs.getBufferedImage(), "png");
      			//System.out.println(bytes);
      			rxs.bufferAssync(bytes, pathImage);
      		} catch (Throwable e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}

      	  
 
	  		  // Capture the whole screen
			
				//System.out.println(pathImage);
				
			

				Mat image = Imgcodecs.imread(pathImage);
				writer.write(image);
			
				Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() { 
					writer.release();
					executorAudio.shutdown();
					
            	  	
					}});  
              if(ScreenRecorderUI.getStatus().getText().equals("Recording Stopped")) {
            	  	writer.release();
            	  	executorAudio.shutdown();
            	  	AudioSave.stopExecuting();
            	  	System.out.println(executorAudio.isShutdown());
  			    	stopTryCatch = true;
  			    	stopWhile = true;
  			    	Thread.sleep(500);
	                new FFmpegAudioVideoMerger(BackEndSaveImage.getVideoAvi(), AudioSave.getpathAudioTxtFile(), AudioSave.getoutputFileMergedPath1());

	 				break;
	 			} 
              
             
              while(ScreenRecorderUI.getStatus().getText() == "Recording Paused") {
  	 			//System.out.println(ScreenRecorderUI.getStatus().getText() == "Recording Stopped");
          	  int elapsedTime  = ScreenRecorderUI.getTimeInMilliseconds();
          	  System.out.println(elapsedTime);
          	  	checkIfThreadAlreadyStarted = true;
          	  	AudioSave.getLine().stop();
          	  	AudioSave.getLine().close();
          	  	Thread.sleep(elapsedTime);
  		        if(ScreenRecorderUI.getstartButton().getModel().isPressed()) {
  		 			System.out.println("Start button has been pressed");
  		 			try {
						AudioSave.getLine().open(AudioSave.getAudioFormatSettings());
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
  	   				AudioSave.getLine().start();

    			    	
  	 				break;
  	 			} 
   			} 
          
              }
          } catch (InterruptedException e) {
        	  System.out.println("Exception" + e);
              this.execute = false;
              
          }
    	  
    	  } 
    	
		System.out.println("thread" + this.threadint + " is stopped..."); 
		}
    
   	

    public void stopExecuting() {
        this.execute = false;
    }
    
  
}

