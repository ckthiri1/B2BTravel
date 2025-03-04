import librosa
import numpy as np
import json
import sys
import traceback
from fastdtw import fastdtw
from scipy.spatial.distance import euclidean

def extract_features(wav_path):
    try:
        # Load audio with strict parameters
        y, sr = librosa.load(wav_path, sr=16000, mono=True, duration=7)
        if len(y) < 16000 * 1.2:
            raise ValueError("Audio too short (minimum 1.2 seconds)")

        # Advanced voice activity detection
        y_trimmed = librosa.effects.split(y, top_db=25, frame_length=2048, hop_length=512)
        if len(y_trimmed) == 0:
            raise ValueError("No speech detected")

        # Combine non-silent intervals
        y_processed = np.concatenate([y[start:end] for (start, end) in y_trimmed])
        if len(y_processed) < 16000 * 0.8:
            raise ValueError("Insufficient speech content")

        # Extract MFCC with delta features
        mfcc = librosa.feature.mfcc(
            y=y_processed,
            sr=sr,
            n_mfcc=13,
            n_fft=2048,
            hop_length=512,
            lifter=26
        )
        delta = librosa.feature.delta(mfcc)
        delta2 = librosa.feature.delta(mfcc, order=2)

        # Combine features and normalize
        features = np.vstack([mfcc, delta, delta2])
        features = (features - np.mean(features)) / np.std(features)

        return features.T.tolist()

    except Exception as e:
        print(f"ERROR: {str(e)}", file=sys.stderr)
        traceback.print_exc(file=sys.stderr)
        sys.exit(1)

def verify_user(audio_path, stored_samples):
    try:
        # Extract features from new audio
        new_features = extract_features(audio_path)
        if not new_features or len(new_features) < 10:
            return False

        # Dynamic Time Warping comparison
        similarities = []
        for stored in stored_samples:
            try:
                distance, _ = fastdtw(new_features, stored, dist=euclidean)
                # Normalize by sequence length
                norm_distance = distance / min(len(new_features), len(stored))
                similarity = 1 / (1 + norm_distance)
                similarities.append(similarity)
            except Exception as e:
                print(f"DTW error: {str(e)}")
                continue

        if not similarities:
            return False

        # Adaptive thresholding
        avg_similarity = np.mean(similarities)
        threshold = 0.62 - (0.02 * len(stored_samples))
        print(f"Similarity: {avg_similarity:.3f} | Threshold: {threshold:.3f}")
        return avg_similarity > threshold

    except Exception as e:
        print(f"Verification error: {str(e)}", file=sys.stderr)
        return False

if __name__ == "__main__":
    try:
        if len(sys.argv) < 3:
            print("Usage:")
            print("  Enrollment: python voice_auth.py enroll <audio_path>")
            print("  Verification: python voice_auth.py verify <audio_path> <json_path>")
            sys.exit(1)

        operation = sys.argv[1]
        audio_path = sys.argv[2]

        if operation == "enroll":
            features = extract_features(audio_path)
            print(json.dumps(features))

        elif operation == "verify":
            with open(sys.argv[3], 'r') as f:
                stored_data = json.load(f)
            result = verify_user(audio_path, stored_data)
            print("true" if result else "false")

        else:
            print(f"Invalid operation: {operation}", file=sys.stderr)
            sys.exit(1)

    except Exception as e:
        print(f"FATAL ERROR: {str(e)}", file=sys.stderr)
        sys.exit(1)