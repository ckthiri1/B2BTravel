package Projet.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.List;

public class VoiceAuthService {
    private static final Gson gson = new Gson();
    private static final int REQUIRED_FEATURES = 13;

    public static List<Double> enrollUser(String pythonPath, File audioFile)
            throws IOException, InterruptedException, VoiceEnrollmentException {

        // Validate audio file first
        if (!audioFile.exists() || audioFile.length() == 0) {
            throw new VoiceEnrollmentException("Audio file is empty or doesn't exist");
        }

        try {
            Process process = new ProcessBuilder(
                    pythonPath,
                    "voice_auth.py",
                    "enroll",
                    audioFile.getAbsolutePath()  // Only 2 arguments for enrollment
            ).redirectErrorStream(true).start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new VoiceEnrollmentException(
                        "Python enrollment failed. Output: " + output.toString()
                );
            }

            List<Double> features = parseFeatures(output.toString());
            validateFeatures(features);

            return features;

        } catch (IOException | InterruptedException e) {
            throw new VoiceEnrollmentException("Enrollment process failed: " + e.getMessage());
        }
    }

    public static boolean verifyUser(String pythonPath, File audioFile, List<List<Double>> storedFeatures)
            throws IOException, InterruptedException {

        File jsonTempFile = File.createTempFile("voice_features", ".json");
        try (FileWriter writer = new FileWriter(jsonTempFile)) {
            JsonObject jsonData = new JsonObject();
            JsonArray featuresArray = gson.toJsonTree(storedFeatures).getAsJsonArray();
            JsonArray labelsArray = new JsonArray();
            for(int i = 0; i < storedFeatures.size(); i++) {
                labelsArray.add(0);
            }
            jsonData.add("features", featuresArray);
            jsonData.add("labels", labelsArray);
            writer.write(gson.toJson(jsonData));
        }

        try {
            Process process = new ProcessBuilder(
                    pythonPath,
                    "voice_auth.py",
                    "verify",
                    audioFile.getAbsolutePath(),
                    jsonTempFile.getAbsolutePath()  // 3 arguments for verification
            ).redirectErrorStream(true).start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Python verification failed. Output: " + output.toString());
            }

            return Boolean.parseBoolean(output.toString().trim());
        } finally {
            jsonTempFile.delete();
        }
    }

    private static List<Double> parseFeatures(String json) throws VoiceEnrollmentException {
        try {
            return gson.fromJson(json, new TypeToken<List<Double>>(){}.getType());
        } catch (JsonSyntaxException e) {
            throw new VoiceEnrollmentException("Invalid feature format: " + e.getMessage());
        }
    }

    private static void validateFeatures(List<Double> features) throws VoiceEnrollmentException {
        if (features == null || features.size() != REQUIRED_FEATURES) {
            throw new VoiceEnrollmentException(
                    "Invalid MFCC features received from Python script. " +
                            "Expected 13 coefficients, got " + (features == null ? "null" : features.size())
            );
        }

        for (Double value : features) {
            if (value == null || value.isNaN() || value.isInfinite()) {
                throw new VoiceEnrollmentException("Invalid numerical value in MFCC features");
            }
        }
    }
}