package Projet.tools;
// AudioRecorder.java
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class AudioRecorder {
    private static final float SAMPLE_RATE = 16000;
    private static final int SAMPLE_SIZE_BITS = 16;
    private static final int CHANNELS = 1;
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = false;


    public static File recordVoice(String outputPath, int durationSeconds)
            throws LineUnavailableException, IOException {

        AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_BITS,
                CHANNELS, SIGNED, BIG_ENDIAN);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Audio format not supported");
        }

        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[format.getFrameSize() * (int)(SAMPLE_RATE / 10)]; // 100ms buffer

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (durationSeconds * 1000);

        while (System.currentTimeMillis() < endTime) {
            int bytesRead = line.read(buffer, 0, buffer.length);
            if (bytesRead > 0) {
                out.write(buffer, 0, bytesRead);
            }
        }

        line.stop();
        line.close();

        byte[] audioData = out.toByteArray();
        if (audioData.length == 0) {
            throw new IOException("No audio data recorded");
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream ais = new AudioInputStream(bais, format,
                audioData.length / format.getFrameSize());

        File outputFile = new File(outputPath);
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outputFile);
        return outputFile;
    }
}
