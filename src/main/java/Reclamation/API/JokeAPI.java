package Reclamation.API;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class JokeAPI {

    /**
     * Récupère une blague aléatoire depuis l'API JokeAPI.
     *
     * @return Une blague aléatoire sous forme de chaîne de caractères.
     */
    public static String getRandomJoke() {
        String url = "https://v2.jokeapi.dev/joke/Any"; // URL de l'API JokeAPI

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);

            // Lire la réponse
            String jsonResponse = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Extraire la blague
            if (jsonObject.has("joke")) {
                return jsonObject.getString("joke");
            } else if (jsonObject.has("setup") && jsonObject.has("delivery")) {
                return jsonObject.getString("setup") + " " + jsonObject.getString("delivery");
            } else {
                return "Aucune blague trouvée.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération de la blague.";
        }
    }

    /**
     * Méthode principale pour tester l'API.
     */
    public static void main(String[] args) {
        String blague = getRandomJoke();
        System.out.println(blague);
    }
}