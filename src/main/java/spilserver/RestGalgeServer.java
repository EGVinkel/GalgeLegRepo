package spilserver;

import brugerautorisation.UniqueKegGen;
import model.Player;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.reverseOrder;

@Path("galgeserver")
public class RestGalgeServer {


    @GET
    @Path("login/{navn}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@PathParam("navn") String navn, @PathParam("password") String password) {
        String temp = decode(password);
       // String temp= password;
        try {
            URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
            QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
            Service service = Service.create(url, qname);
            brugerautorisation.transport.soap.Brugeradmin ba = service.getPort(brugerautorisation.transport.soap.Brugeradmin.class);
            if (ba.hentBruger(navn, temp) != null) {
                String sessionkey = UniqueKegGen.getKey();
                SpilServerImpl.sessionmap.put(navn, sessionkey);
                if (SpilServerImpl.spilmap.get(navn) == null) {
                    SpilServerImpl.spilmap.put(navn, new SpilImpl());
                }

                return "{\"status\": \"" + sessionkey + "\"}";
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "{\"Status\": \"Failed\"}";

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "{\"Status\": \"Failed\"}";
    }

    @POST
    @Path("/guess/{bogstav}")
    @Produces(MediaType.APPLICATION_JSON)
    public void makeguess(Player player, @PathParam("bogstav") String bogstav) {
        if (!SpilServerImpl.sessionmap.get(player.name).equals(player.key)) {
            logud(player.name);
            throw new IllegalArgumentException(player.name + " unikt id er forkert! logger ud af sessionen");
        }
        if (!SpilServerImpl.spilmap.containsKey(player.name)) {
            throw new IllegalArgumentException(player.name + " har ingen registrerede spil");


        } else
            SpilServerImpl.spilmap.get(player.name).gætBogstav(bogstav, player.name, player.key);


    }

    @GET
    @Path("result/{brugernavn}/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getresult(@PathParam("brugernavn") String brugernavn, @PathParam("key") String id) {
        HashMap<String, String> tempmap = new HashMap<>();
        if (!SpilServerImpl.spilmap.containsKey(brugernavn)) {
            throw new IllegalArgumentException(brugernavn + " har ingen registrerede spil");
        }
        if (!SpilServerImpl.sessionmap.get(brugernavn).equals(id)) {
            logud(brugernavn);
            System.out.println("Fejl pga af ID");
            throw new IllegalArgumentException(brugernavn + " unikt id er forkert! logger ud af sessionen");
        } else {
            SpilImpl temp = SpilServerImpl.spilmap.get(brugernavn);
            tempmap.put("Antal", temp.getAntalForkerteBogstaver() + "");
            tempmap.put("Tabt", temp.erSpilletTabt() + "");
            tempmap.put("Vundet", temp.erSpilletVundet() + "");
            tempmap.put("Slut", temp.erSpilletSlut() + "");
            tempmap.put("SynligtOrd", temp.getSynligtOrd());
            tempmap.put("Ordet", temp.getordet());
            tempmap.put("Brugte", temp.getBrugteBogstaver().toString());
        }


        return new JSONObject(tempmap).toString();
    }

    @POST
    @Path("/logud/{brugernavn}")
    public void logud(@PathParam("brugernavn") String navn) {
        SpilServerImpl.sessionmap.remove(navn);
        System.out.println(navn + "Er logget ud");


    }

    @POST
    @Path("nulstil")
    @Consumes(MediaType.APPLICATION_JSON)
    public void nulstil(Player player) {
        System.out.println("Hej" + player.name);
        if (SpilServerImpl.sessionmap.get(player.name).equals(player.key)) {
            SpilServerImpl.spilmap.get(player.name).nulstil(player.name, player.key);
            return;
        }
        SpilServerImpl.sessionmap.remove(player.name);
    }

    private String decode(String key) {
        char[] temp = key.toCharArray();
        String password = "";
        int counter = 0;
        for (char s : temp) {
            if (counter % 40 == 0) {
                password = password.concat(s + "");
            }
            counter++;
        }
        password = password.replaceAll(" ", "");

        return password;
    }

    @GET
    @Path("gethigh/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Player> gethigh(@PathParam("key") String id) {
        System.out.println(Collections.singletonList("gethighscore" + SpilServerImpl.scoremap));
        ArrayList<Player> plist = new ArrayList<>();
        SpilServerImpl.scoremap.entrySet()
                .stream()
                .limit(10)
                .sorted(reverseOrder(Map.Entry.comparingByValue())).forEach(e -> plist.add(new Player(e.getKey(), e.getValue())));


        return plist;
    }

    @POST
    @Path("newhighscore")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addnewScore(Player player) {
        int temp = 0;
        if (SpilServerImpl.scoremap.get(player.name) != null) {
            temp = SpilServerImpl.scoremap.get(player.name);
        }

        if (player.score > temp) {
            SpilServerImpl.scoremap.put(player.name, player.score);
        }

    }

    @DELETE
    @Path("deletenewhighscore")
    public void deletescore() {
        SpilServerImpl.scoremap.clear();

    }

    @GET
    @Path("getmap/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Player> getmap(@PathParam("key") String id) {
        System.out.println(Collections.singletonList("gethighscore" + SpilServerImpl.scoremap));
        ArrayList<Player> plist = new ArrayList<>();
        SpilServerImpl.sessionmap.forEach((key, value) -> plist.add(new Player(key, value)));
        return plist;
    }

    @POST
    @Path("removeplayer")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeplayer(Player player) {

        SpilServerImpl.sessionmap.remove(player.name);

    }


}

