package services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DrOrdRestService implements Remote {

    public static List<String> ordService() throws JSONException {
        Client klient = ClientBuilder.newClient();

        Response svar = klient.target("https://www.dr.dk/mu-online/api/1.4/list/view/mostviewed?limit=40").request(MediaType.APPLICATION_JSON).get();
        String responseStr = svar.readEntity(String.class);

        JSONObject jo = new JSONObject(responseStr);
        JSONArray array = jo.getJSONArray("Items");

        List<String> ordarray = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            String beskrivelse;
            try {

                beskrivelse = new JSONObject(klient.target("https://www.dr.dk/mu-online/api/1.4/programcard/" + array.getJSONObject(i).getString("Urn"))
                        .request(MediaType.APPLICATION_JSON).get().readEntity(String.class)).getString("Description");
            } catch (Exception e) {
                continue;
            }

            beskrivelse = beskrivelse.replaceAll("<.+?#>,:/", " ").toLowerCase().replaceAll("[^a-zæøå]", " ");

            beskrivelse = beskrivelse.replaceAll("(^.$|\\s.\\s|^.\\s|\\s.$)", "").replaceAll("\\s+", " ").trim();
            ordarray.addAll(Arrays.asList(beskrivelse.split("\\s+")));
        }

        return ordarray;
    }

}
