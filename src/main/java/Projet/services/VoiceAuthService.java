package Projet.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VoiceAuthService {
    private static final Gson gson = new Gson();
    private static final int REQUIRED_FEATURES = 39; // 13 MFCC + deltas

    public static List<List<Double>> enrollUser(String pythonPath, List<File> audioFiles)
            throws VoiceException {
        List<List<Double>> allFeatures = new ArrayList<>();

        for (File audioFile : audioFiles) {
            try {
                Process process = new ProcessBuilder(
                        pythonPath, "voice_auth.py", "enroll", audioFile.getAbsolutePath()
                ).redirectErrorStream(true).start();

                StringBuilder output = readOutput(process);
                int exitCode = process.waitFor(10, TimeUnit.SECONDS) ? process.exitValue() : -1;

                if (exitCode != 0) {
                    throw new VoiceException("Enrollment failed: " + output);
                }

                List<Double> features = parseFeatures(output.toString());
                validateFeatures(features);
                allFeatures.add(features);
            } catch (Exception e) {
                throw new VoiceException("Failed to process " + audioFile.getName() + ": " + e.getMessage());
            }
        }

        if (allFeatures.size() < 3) {
            throw new VoiceException("Minimum 3 good recordings required");
        }

        return allFeatures;
    }

    public static boolean verifyUser(String pythonPath, File audioFile, List<List<Double>> storedFeatures)
            throws VoiceException {
        try {
            File jsonFile = createTempFeaturesFile(storedFeatures);

            Process process = new ProcessBuilder(
                    pythonPath, "voice_auth.py", "verify",
                    audioFile.getAbsolutePath(), jsonFile.getAbsolutePath()
            ).redirectErrorStream(true).start();

            StringBuilder output = readOutput(process);
            int exitCode = process.waitFor(15, TimeUnit.SECONDS) ? process.exitValue() : -1;

            Files.deleteIfExists(jsonFile.toPath());

            if (exitCode != 0) {
                throw new VoiceException("Verification failed: " + output);
            }

            return "true".equalsIgnoreCase(output.toString().trim());
        } catch (Exception e) {
            throw new VoiceException("Verification error: " + e.getMessage());
        }
    }

    private static File createTempFeaturesFile(List<List<Double>> features) throws IOException {
        File tempFile = File.createTempFile("voice_", ".json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            gson.toJson(features, writer);
        }
        return tempFile;
    }

    private static StringBuilder readOutput(Process process) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        }
        return output;
    }

    private static List<Double> parseFeatures(String json) throws VoiceException {
        try {
            return gson.fromJson(json, new TypeToken<List<Double>>(){}.getType());
        } catch (JsonSyntaxException e) {
            throw new VoiceException("Invalid feature format: " + e.getMessage());
        }
    }

    private static void validateFeatures(List<Double> features) throws VoiceException {
        if (features == null || features.size() != REQUIRED_FEATURES) {
            throw new VoiceException("Invalid features count: " +
                    (features != null ? features.size() : "null"));
        }

        for (Double val : features) {
            if (val == null || val.isNaN() || val.isInfinite()) {
                throw new VoiceException("Invalid feature value detected");
            }
        }
    }

    public static class VoiceException extends Exception {
        public VoiceException(String message) { super(message); }
    }
}