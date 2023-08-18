package userInterface;
import screen.captureImage;
import screen.BackEndSaveImage;
import screen.AudioSave;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;
import javax.swing.border.Border;


import java.awt.*;


public class ScreenRecorderUI{
//Frame
private	static JFrame frame;
// JTextField
public static JTextField driveSelectiInputTextField = new JTextField(16);


static JFileChooser chooser;
String choosertitle;
	
//booleans
private static boolean pauseBool = false;
private static boolean threadSleepNr = false;

//numbers
private static int timeInSec;
private static int threadNumber = 0;
	
public static captureImage scRec;
ScreenRecorderUI scrUI;

//strings
public static String file;
//Buttons
static JButton startRecording = new JButton("Start Recording");//create button  ;
static JButton stopRecording = new JButton("Stop Recording");
static JButton pauseRecording = new JButton("Pause Recording");


//LABELS
private JLabel directoryLabel = new JLabel("Choose non-system Drive Directory letter(ex: D - just the letter)", JLabel.CENTER);
private static JLabel statusLabel = new JLabel("Not Started", JLabel.CENTER);;


//Thread Executor
private static ExecutorService executor;

// Colors Buttons
//Color btnColor = new Color(152, 152, 152);
Color btnColor = new Color(39, 190, 146);
Color btnColorOnHover = new Color(157, 62, 29);


//Border
Border emptyBorder = BorderFactory.createEmptyBorder();

public static String getFile() throws URISyntaxException{
	return new File(ScreenRecorderUI.class.getProtectionDomain().getCodeSource().getLocation()
		    .toURI()).getPath();
}

public static JFrame getFrame() {
	return frame;
}

//Executor get
public static ExecutorService getExecutor() {
	return executor;
}

public static boolean getpauseBool() {
	return pauseBool;
}

public static void setpauseBool(boolean pauseBool) {
		ScreenRecorderUI.pauseBool = pauseBool;
}
public static boolean getthreadSleepNr() {
	return threadSleepNr;
}

public static void setthreadSleepNr(boolean threadSleepNr) {
		ScreenRecorderUI.threadSleepNr = threadSleepNr;
}

public static JButton getstartButton() {
	return startRecording;
}

public static JButton getpauseButton() {
	return pauseRecording;
}

public static JButton getstopButton() {
	return stopRecording;
}

public static JLabel getStatus() {
	return statusLabel;
}
public static void setStatus(String setStatus) {
	 statusLabel.setText(setStatus);
}

public static int getTimeInMilliseconds() {
	return timeInSec;
}

public ScreenRecorderUI(){ 
		

/*BufferedImage img = null;
try {
    img = ImageIO.read(new File("image.jpg"));
} catch (IOException e) {
	e.printStackTrace();
}
Image dimg = img.getScaledInstance(800, 508, Image.SCALE_SMOOTH);
ImageIcon imageIcon = new ImageIcon(dimg); */


frame = new JFrame("Screen Recorder"); // Creation of a new JFrame with the title 
frame.setLayout(null); // To position our components
frame.setVisible(true); // To display the frame
frame.setBounds(200, 200, 1050, 300); // To set the locations of the frame.
    

	   
    
//Stops the program on exit
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	


JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
JPanel pnlMiddle = new JPanel(new FlowLayout(FlowLayout.LEFT));



pnlTop.add(startRecording);
pnlTop.add(stopRecording);
pnlTop.add(pauseRecording);
pnlTop.add(statusLabel);


pnlMiddle.add(directoryLabel);
pnlMiddle.add(driveSelectiInputTextField);

frame.add(pnlTop);
frame.add(pnlMiddle);

//setContentPane(new JLabel(imageIcon));
JLabel panelLabel = new JLabel();
panelLabel.setBounds(400, 350, 50, 50);

panelLabel.setBackground(Color.black);
panelLabel.setOpaque(true);
frame.setContentPane(panelLabel);


Clicklistener click= new Clicklistener();

driveSelectiInputTextField.setBounds(600,75,150, 40);
driveSelectiInputTextField.setSize(150,40);
driveSelectiInputTextField.setOpaque(true);
driveSelectiInputTextField.setBorder(emptyBorder);
driveSelectiInputTextField.setForeground(Color.BLACK);
driveSelectiInputTextField.setLayout(null);  
driveSelectiInputTextField.setVisible(true); 
frame.add(driveSelectiInputTextField);
//Buttons
//............
//................


//Start button
startRecording.setBounds(15,15,150, 40);  
frame.add(startRecording);//adding button on frame  
startRecording.setSize(150,50);
startRecording.setBackground(btnColor);
startRecording.setOpaque(true);
startRecording.setBorder(emptyBorder);
startRecording.setFocusPainted(false);
startRecording.setForeground(Color.white);
startRecording.setLayout(null);  
startRecording.setVisible(true); 



// Stop Recording button
stopRecording.addActionListener(click);
stopRecording.setBounds(15,15,150, 40);


frame.add(stopRecording);
stopRecording.setBackground(btnColor);
stopRecording.setOpaque(true);
stopRecording.setSize(150,50);
stopRecording.setLayout(null);
stopRecording.setBorder(emptyBorder);
stopRecording.setFocusPainted(false);
stopRecording.setForeground(Color.white);
stopRecording.setEnabled(false);
stopRecording.setLayout(null);  
stopRecording.setVisible(false); 



//Pause button
pauseRecording.addActionListener(click);
pauseRecording.setBounds(180, 15, 150, 40);  
frame.add(pauseRecording);//adding button on frame  
pauseRecording.setSize(150,50);
pauseRecording.setBackground(btnColor);
pauseRecording.setOpaque(true);
pauseRecording.setBorder(emptyBorder);
pauseRecording.setFocusPainted(false);
pauseRecording.setForeground(Color.white);
pauseRecording.setLayout(null);
pauseRecording.setEnabled(false);
pauseRecording.setVisible(false); 



//Labels
//............
//................
statusLabel.setBounds(300, 15, 200, 50);
statusLabel.setForeground(Color.white);
frame.add(statusLabel);

directoryLabel.setBounds(1, 80, 500, 50);
directoryLabel.setForeground(Color.white);


frame.add(directoryLabel);

//MouseListeners
//..............
//..............
//OnHover Start Recording Enter and exit change color

startRecording.addMouseListener(new java.awt.event.MouseAdapter() {
  public void mouseEntered(java.awt.event.MouseEvent evt) {
  	startRecording.setBackground(btnColorOnHover);
  }

  public void mouseExited(java.awt.event.MouseEvent evt) {
  	startRecording.setBackground(btnColor);
  }
});

//OnHover Pause Recording Enter and exit change color
pauseRecording.addMouseListener(new java.awt.event.MouseAdapter() {
  public void mouseEntered(java.awt.event.MouseEvent evt) {
  	pauseRecording.setBackground(btnColorOnHover);
  }

  public void mouseExited(java.awt.event.MouseEvent evt) {
  	pauseRecording.setBackground(btnColor);
  }
});

//OnHover Stop Recording Enter and exit change color
stopRecording.addMouseListener(new java.awt.event.MouseAdapter() {
  public void mouseEntered(java.awt.event.MouseEvent evt) {
	  stopRecording.setBackground(btnColorOnHover);
  }

  public void mouseExited(java.awt.event.MouseEvent evt) {
	  stopRecording.setBackground(btnColor);
	  stopRecording.setForeground(Color.white);

  }
});

}  



private class Clicklistener implements ActionListener
{
public void actionPerformed(ActionEvent e)
{
if (e.getSource() == stopRecording)
{
//stopRecording.setText("The button has been clicked");

}


}
}
private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

public static void main(String[] args) {  
	SwingUtilities.invokeLater(() -> {
	new ScreenRecorderUI(); 
	
	
	
getstartButton().addActionListener(new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent e) {
        //your actions
    	if (e.getSource() == startRecording)
    	{
    		if(getStatus().getText() == "Recording Stopped") {
            	
    			getstartButton().setText("Start Recording");
            }
    	
            timeInSec =  0;
            threadNumber += 1;
            
            // Create the thread pools outside the loop
            executor = Executors.newFixedThreadPool(threadNumber);
            
   		for (int i = 0; i < threadNumber; i++) {
            try{
           
            if(pauseBool == false) {
            	if(!BackEndSaveImage.getcheckIfThreadAlreadyStarted()) {
    			executor.execute(new BackEndSaveImage(i));

            }
           
        	BackEndSaveImage.getcheckIfThreadAlreadyStarted(false);
			threadNumber = 0;

    	}
            	
            } catch(Exception err){
                err.printStackTrace();
                return;
            }
            
   		
         }
    
        getStatus().setText("Recording...");
        if(ScreenRecorderUI.getStatus().getText() == "Recording...") {
			 ScreenRecorderUI.getpauseButton().setVisible(true);
			 ScreenRecorderUI.getpauseButton().setEnabled(true);
	   	  
	     }

        	/*try {
        		if(BackEndSaveImage.getImageFiles() != null && AudioSave.getpathAudioTxtFile() != null){
			FileChannel.open(Paths.get(BackEndSaveImage.getImageFiles()), StandardOpenOption.WRITE).truncate(1).close();
			FileChannel.open(Paths.get(AudioSave.getpathAudioTxtFile()), StandardOpenOption.WRITE).truncate(1).close();
        		}
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/

        getstartButton().setEnabled(false);
        getstartButton().setVisible(false);
        getstopButton().setVisible(true);
		getstopButton().setEnabled(true);
		getstopButton().setText("Stop Recording");
    	}
    }
});



getpauseButton().addActionListener(new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent f) {

    	timeInSec = 1;
    		
        getStatus().setText("Recording Paused");
       
        
  	  ScreenRecorderUI.getstartButton().setText("Resume Recording");

       setthreadSleepNr(true);
       getstartButton().setEnabled(true);
     getstartButton().setVisible(true);
     getstopButton().setVisible(false);
	 getstopButton().setEnabled(false);
	

    }
});



getstopButton().addActionListener(new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent g) {
        //your actions
    	
    	if (g.getSource() == stopRecording)
    	{
    		 getStatus().setText("Recording Stopped");
    		 
    		 
    		 executor.shutdown();
    		 if(executor.isTerminated()) {
        		 pauseBool = false;

    		 }
    		 
        getstopButton().setEnabled(false);
        getstopButton().setVisible(false);
        getstartButton().setVisible(true);
        getstartButton().setEnabled(true);
        
    	if(getStatus().getText() == "Recording Stopped") {
        	
			getstartButton().setText("Start Recording");
        }
    	
        if(getStatus().getText() == "Recording started")
         	 getstartButton().setText("Start Recording");

       }
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
});
	});


}
}

 