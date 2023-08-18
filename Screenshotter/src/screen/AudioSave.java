package screen;

import javax.sound.sampled.*;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import userInterface.ScreenRecorderUI;
import java.util.concurrent.locks.ReentrantLock;

public class AudioSave extends Thread {
	static String pathAudioTxtFile;
	static String outputFilePath;
	private volatile static boolean execute;
	private volatile boolean stopTryCatch;
	private volatile boolean stopWhile;
	private volatile static boolean checkIfThreadAlreadyStarted;
	private static TargetDataLine line;
	private int threadint;
	private ByteArrayOutputStream audioBuffer; // To store the audio data
	private final ReentrantLock fileWriteLock = new ReentrantLock(); // Lock for file writing
	public static String outputFileMergedPath;
	public static String outputFileMergedPath1;
	private static AudioFormat audioFormat;

    public AudioSave() {
    }
    public AudioSave(int param) {
    	this.threadint = param;
    }
    public static AudioFormat getAudioFormatSettings() {
    	return audioFormat;
    	
    }
    
 public static String getAudioWav() {
    	
    	return outputFilePath;
    }
    public static TargetDataLine getLine() {
    	return line;
    }
    public boolean getExceute() {
    	
    	return execute;
    }
   public static boolean  getcheckIfThreadAlreadyStarted() {
    	
    	return checkIfThreadAlreadyStarted;
    }
   
   public static void  getcheckIfThreadAlreadyStarted(boolean param) {
   	
   	checkIfThreadAlreadyStarted = param;
   	
   }
 
  
   private void captureAudio() {
	    // Specify the audio format for capturing
	    audioFormat = new AudioFormat(
	            AudioFormat.Encoding.PCM_SIGNED,
	            44100,       // Sample Rate
	            16,          // Sample Size in bits
	            1,           // Channels (Mono)
	            2,           // Frame Size
	            44100,       // Frame Rate
	            false        // Big Endian
	    );

	    DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

	    if (!AudioSystem.isLineSupported(info)) {
	        System.err.println("Line not supported");
	        return;
	    }

	    try {
	        // Get the default audio capture device (microphone)
	        line = AudioSystem.getTargetDataLine(audioFormat);

	        // Open the line and start capturing audio
	        line.open(audioFormat);
	        line.start();

	        // Create a byte array to hold the captured audio data
	        int bufferSize = 1024; // You can adjust the buffer size as needed
	        byte[] audioData = new byte[bufferSize];

	        // Start a separate thread for writing audio data to the file asynchronously
	        Thread audioWriteThread = new Thread(() -> {
	            while (execute) {
	                // Synchronize to avoid conflicts with the audio capture thread
	                synchronized (audioBuffer) {
	                	
	                    if (audioBuffer.size() > 0) {
	                        // Write the audio data from the buffer to the file asynchronously
	                        bufferAssyncAudio(audioBuffer.toByteArray(), pathAudioTxtFile);
	                        audioBuffer.reset(); // Clear the buffer after writing
	                    }
	                    if(stopWhile == true) {
	                    	execute = false;
	                    }
	                }
	                try {
	                    Thread.sleep(50); // Adjust sleep time as needed
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        });

	        audioWriteThread.start();

	        // Continue capturing audio while the thread is running
	        while (execute) {
	            int numBytesRead = line.read(audioData, 0, bufferSize);
	            if (numBytesRead > 0) {
	                // Append the audio data to the buffer
	                synchronized (audioBuffer) {
	                    audioBuffer.write(audioData, 0, numBytesRead);
	                }
	            }
	        }

	        // Stop capturing audio and close the line when done
	        line.stop();
	        line.close();
	        System.out.println(line.isActive());
	       
	        // Wait for the audio write thread to finish before ending the method
	        audioWriteThread.join();
	        

	    } catch (LineUnavailableException | InterruptedException e) {
	        e.printStackTrace();
	    }
	}

   public void bufferAssyncAudio(byte[] audioData, String outputPath) {
       Path path = Paths.get(outputPath);

       // Use the lock for synchronization to avoid conflicts with other threads writing to the file
       fileWriteLock.lock();
       try (AsynchronousFileChannel asyncChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)) {
           // Write the audio data to the file asynchronously
           Future<Integer> future = asyncChannel.write(ByteBuffer.wrap(audioData), asyncChannel.size());

           // Wait until the write operation is done
           while (!future.isDone()) {
               // You can do other processing while waiting for the write operation to complete
               if (ScreenRecorderUI.getStatus().getText().equals("Recording Stopped")) {
                   System.out.println("Stop button has been pressed");
                   asyncChannel.close();
                   line.stop();
                   line.close();

                   break;
               }
           }

          future.get();

           // Close the AsyncFileChannel after audio data is fully written
           asyncChannel.close();
       } catch (IOException | InterruptedException | ExecutionException e) {
           e.printStackTrace();
       } finally {
           // Release the lock
           fileWriteLock.unlock();
       }
       
   }

	private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	
	public static String getpathAudioTxtFile() {
		return pathAudioTxtFile;
	}
	
	public static String getoutputFileMergedPath1() {
		return outputFileMergedPath1;
	}
	
    @Override
	public void run(){  
    	this.execute = true;
    	this.stopTryCatch = false;
    	this.stopWhile = false;
        audioBuffer = new ByteArrayOutputStream();

    	if(ScreenRecorderUI.driveSelectiInputTextField.getText().isEmpty() == false) {
    		pathAudioTxtFile = ScreenRecorderUI.driveSelectiInputTextField.getText() + ":" + File.separator + "audio.txt";
		    }else {
		    pathAudioTxtFile = "D:" + File.separator + "audio.txt";
		    	
		    }
    	// Use relative path for Unix systems
    	File g = new File(pathAudioTxtFile);

    	g.getParentFile().mkdirs(); 
    	try {
			g.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	
    	
    	if(ScreenRecorderUI.driveSelectiInputTextField.getText().isEmpty() == false) {
    		outputFileMergedPath1 = ScreenRecorderUI.driveSelectiInputTextField.getText() + ":" + File.separator + "output.avi";
    	    }else {
    	    	outputFileMergedPath1 = "D:" + File.separator + "output.avi";
    	    	
    	    }
    
    	while (this.execute) {
    		
    		if(stopWhile == true) {
    			break;
    		}
		System.out.println("thread Audio " + threadint + " is running..."); 
			
		   

		
		
    	  try {
    		  if(stopTryCatch == true) {
    			  break;
    			  
    		  }
    
             
              
            	  
            	 
              Thread.sleep(100);
              captureAudio();
          	 



	   
             
              
          
              
          } catch (InterruptedException e) {
        	  System.out.println("Exception" + e);
              this.execute = false;
          }
    	  
    	  } 
        try {
			audioBuffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(audioBuffer.size());
        audioBuffer.reset();
		System.out.println("thread audio" + this.threadint + " is stopped..."); 
		
		System.out.println(BackEndSaveImage.getVideoAvi());
		System.out.println(AudioSave.getAudioWav());
		System.out.println(outputFileMergedPath1);


		
		}
    
   	

    public static void stopExecuting() {
        execute = false;
    }
    
  
}

