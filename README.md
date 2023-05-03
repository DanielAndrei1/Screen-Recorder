# Screen-Recorder
Screen Recorder Created in Java using CV Open Library

# Runnable jar (recorder) only tested on Windows
To run the program you need to download cv open library and JDK or  JRE Java:

cv open: https://opencv.org/releases/

JDK Java: https://www.oracle.com/java/technologies/downloads/

You will also need to set the system variable paths to direct to the build\bin folders as shown below.

For CV Open:
C:\Program Files\CVOpen\opencv\build\java\x64

For Java:
C:\Program Files\Java\jdk-20\bin

For cv open to point to:
![image](https://user-images.githubusercontent.com/44091613/235814230-a99fdc59-c85a-4157-8327-5c2764785c98.png)


# Debugging the runnable jar
If it does not work just run a command prompt from the folder where you downloaded it and use the following command:
java -agentlib:jdwp=transport=dt_socket,address=8080,server=y,suspend=n -jar recorder.jar
This command will open the jar and show any errors plus prints in the terminal.
![image](https://user-images.githubusercontent.com/44091613/235815004-75ee95c1-d64d-4f21-9bfb-3fd93279807b.png)

![image](https://user-images.githubusercontent.com/44091613/235814451-def1aa1c-8561-4c1a-83e8-89ad7ba3194f.png)

Created by Daniel Andrei

