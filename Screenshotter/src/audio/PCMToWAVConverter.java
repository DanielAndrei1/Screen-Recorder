package audio;

import java.io.*;

public class PCMToWAVConverter {
    private byte[] audioData;
    private String outputFilePath;
    // Other variables for audio format like sampleRate, numChannels, bitsPerSample

    public PCMToWAVConverter(byte[] audioData, String outputFilePath) {
        this.audioData = audioData;
        this.outputFilePath = outputFilePath;
    }

    public void convertToWav() {
        int sampleRate = 44100; // Change this to match the sample rate used for recording the audio
        int numChannels = 1; // Change this to match the number of channels used for recording the audio
        int bitsPerSample = 16; // Change this to match the number of bits per sample used for recording the audio

        try (FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
            // Calculate the size of the data chunk and the overall file size
            int dataSize = audioData.length;
            int fileSize = dataSize + 36;

            // Write the WAV file header
            writeString(outputStream, "RIFF"); // Chunk ID
            writeInt(outputStream, fileSize); // Chunk Size
            writeString(outputStream, "WAVE"); // Format

            // Write the "fmt " sub-chunk
            writeString(outputStream, "fmt "); // Sub-chunk 1 ID
            writeInt(outputStream, 16); // Sub-chunk 1 Size (16 for PCM)
            writeShort(outputStream, 1); // Audio Format (1 for PCM)
            writeShort(outputStream, numChannels); // Number of Channels
            writeInt(outputStream, sampleRate); // Sample Rate
            writeInt(outputStream, sampleRate * numChannels * bitsPerSample / 8); // Byte Rate
            writeShort(outputStream, numChannels * bitsPerSample / 8); // Block Align
            writeShort(outputStream, bitsPerSample); // Bits per Sample

            // Write the "data" sub-chunk
            writeString(outputStream, "data"); // Sub-chunk 2 ID
            writeInt(outputStream, dataSize); // Sub-chunk 2 Size

            // Write the audio data to the output stream
            outputStream.write(audioData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper methods to write various data types to the output stream
    private static void writeInt(OutputStream outputStream, int value) throws IOException {
        outputStream.write(value & 0xFF);
        outputStream.write((value >> 8) & 0xFF);
        outputStream.write((value >> 16) & 0xFF);
        outputStream.write((value >> 24) & 0xFF);
    }

    private static void writeShort(OutputStream outputStream, int value) throws IOException {
        outputStream.write(value & 0xFF);
        outputStream.write((value >> 8) & 0xFF);
    }

    private static void writeString(OutputStream outputStream, String value) throws IOException {
        outputStream.write(value.getBytes());
    }
}
