# Screen-Recorder
Screen Recorder Created in Java using CV Open Library

# Runnable jar only for Windows
To run the program you need to download cv open library and JDK Java and set the system variables paths to the bin as shown below.
For cv open to point to:
C:\Program Files\CVOpen\opencv\build\java\x64
For Java:
C:\Program Files\Java\jdk-20\bin

![image](https://user-images.githubusercontent.com/44091613/235814230-a99fdc59-c85a-4157-8327-5c2764785c98.png)


# Debugging the runnable jar
If it does not work just run a command prompt from the folder where you downloaded it and use the following command:
java -agentlib:jdwp=transport=dt_socket,address=8080,server=y,suspend=n -jar recorder.jar
This command will open the jar and show any errors plus prints in the terminal.

![image](https://user-images.githubusercontent.com/44091613/235814451-def1aa1c-8561-4c1a-83e8-89ad7ba3194f.png)

