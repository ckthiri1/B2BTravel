import librosa
import numpy as np
import json
import sys
import traceback
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.metrics.pairwise import cosine_similarity

def extract_mfcc(wav_path):
    try:
        y, sr = librosa.load(wav_path, sr=16000, mono=True)
        if len(y) < 16000 * 1:  # Minimum 1 second
            raise ValueError("Audio too short (minimum 1 second required)")

        mfcc = librosa.feature.mfcc(
            y=y,
            sr=sr,
            n_mfcc=13,
            n_fft=2048,
            hop_length=512
        )
        return np.mean(mfcc.T, axis=0).tolist()

    except Exception as e:
        print(f"ERROR: {str(e)}", file=sys.stderr)
        traceback.print_exc(file=sys.stderr)
        sys.exit(1)

def verify_user(audio_path, stored_samples):
    try:
        new_sample = extract_mfcc(audio_path)
        if not stored_samples:
            return False

        similarities = []
        for stored_sample in stored_samples:
            stored_sample = np.array(stored_sample).flatten()[:13]
            new_sample_flat = np.array(new_sample).flatten()[:13]

            similarity = cosine_similarity([new_sample_flat], [stored_sample])[0][0]
            similarities.append(similarity)

        avg_similarity = np.mean(similarities)
        return avg_similarity > 0.65
    except Exception as e:
        print(f"Verification error: {str(e)}", file=sys.stderr)
        return False

def train_model(features, labels):
    try:
        scaler = StandardScaler()
        features_scaled = scaler.fit_transform(features)
        model = KNeighborsClassifier(n_neighbors=3, metric='cosine')
        model.fit(features_scaled, labels)
        return model, scaler
    except Exception as e:
        print(f"Training error: {str(e)}", file=sys.stderr)
        sys.exit(1)

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
            if len(sys.argv) != 3:
                print("Enrollment requires 2 arguments: enroll <audio_path>")
                sys.exit(1)

            features = extract_mfcc(audio_path)
            print(json.dumps(features))

        elif operation == "verify":
            if len(sys.argv) != 4:
                print("Verification requires 3 arguments: verify <audio_path> <json_path>")
                sys.exit(1)

            with open(sys.argv[3], 'r') as f:
                stored_data = json.load(f)

            sample = extract_mfcc(audio_path)
            stored_features = stored_data['features']
            labels = stored_data['labels']

            model, scaler = train_model(stored_features, labels)
            sample_scaled = scaler.transform([sample])
            confidence = model.predict_proba(sample_scaled)[0][0]
            print("true" if confidence > 0.5 else "false")

        else:
            print(f"Invalid operation: {operation}", file=sys.stderr)
            sys.exit(1)

    except Exception as e:
        print(f"FATAL ERROR: {str(e)}", file=sys.stderr)
        sys.exit(1)