package audio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FFmpegAudioVideoMerger {

    public FFmpegAudioVideoMerger(String videoPath, String audioPath, String outputFilePath) {
        mergeAudioAndVideo(videoPath, audioPath, outputFilePath);
    }

    public static void mergeAudioAndVideo(String videoPath, String audioPath, String outputFilePath) {
        try {
            // Read the audio data from the audio.txt file
            byte[] audioData = Files.readAllBytes(Paths.get(audioPath));

            // Build the FFmpeg command
            String ffmpegPath = "D:\\ffmpeg-2023-07-19-git-efa6cec759-essentials_build\\bin\\ffmpeg.exe"; // Replace with the actual path to ffmpeg
            ProcessBuilder processBuilder = new ProcessBuilder(
                    ffmpegPath,
                    "-i", videoPath,
                    "-f", "s16le",
                    "-ar", "44100",
                    "-ac", "1",
                    "-i", "-",
                    "-c:v", "copy",
                    "-c:a", "aac",
                    "-strict", "experimental",
                    outputFilePath
            );

            // Start the process
            Process process = processBuilder.start();

            // Write the audio data to the process's input stream
            process.getOutputStream().write(audioData);
            process.getOutputStream().close();
            
            // Wait for the process to finish
            int exitCode = process.waitFor();

            // Check if FFmpeg was successful (exit code 0)
            if (exitCode == 0) {
                System.out.println("Audio and video merged successfully.");
            } else {
                System.err.println("Error merging audio and video. Exit code: " + exitCode);

                // Capture the error stream and print it
                try (java.util.Scanner scanner = new java.util.Scanner(process.getErrorStream()).useDelimiter("\\A")) {
                    if (scanner.hasNext()) {
                        System.err.println(scanner.next());
                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
