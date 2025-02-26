package Projet.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleAuthService {

    // Replace with the relative or absolute path to your client_secret.json file
    private static final String CLIENT_SECRET_FILE = "src/main/resources/client_secret.json";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // For desktop apps, we use a special redirect URI
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    // Scope to get the user's email and basic profile info
    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/userinfo.email");

    private GoogleAuthorizationCodeFlow flow;

    public GoogleAuthService() throws GeneralSecurityException, IOException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new FileReader(CLIENT_SECRET_FILE));
        flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                JSON_FACTORY,
                clientSecrets,
                SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
                .setAccessType("offline")
                .build();
    }

    // Returns the URL where the user must authorize your app
    public String getAuthorizationUrl() {
        return flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    }

    // Exchanges the authorization code for credentials
    public Credential getCredentials(String code) throws IOException {
        TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
        return flow.createAndStoreCredential(tokenResponse, "user");
    }
}
