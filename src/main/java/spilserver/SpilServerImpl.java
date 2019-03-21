package spilserver;

import brugerautorisation.UniqueKegGen;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

public final class SpilServerImpl extends UnicastRemoteObject implements SpilserverInterface, Serializable {
    public static HashMap<String, String> sessionmap = new HashMap<>();
    public static HashMap<String,Integer> scoremap= new HashMap<>();
    public static HashMap<String, SpilImpl> spilmap = new HashMap<>();


    public SpilServerImpl() throws RemoteException {
        System.out.println(Collections.singletonList(sessionmap));
        scoremap.put("s175100",201);
        scoremap.put("s175312",90);
        scoremap.put("s175132",200);
        scoremap.put("s175101",220);

    }


    public String login(String navn, String password) throws RemoteException {

        try {
            URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
            QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
            Service service = Service.create(url, qname);
            brugerautorisation.transport.soap.Brugeradmin ba = service.getPort(brugerautorisation.transport.soap.Brugeradmin.class);
            if (ba.hentBruger(navn, password) != null) {
                String sessionkey = UniqueKegGen.getKey();
                sessionmap.put(navn, sessionkey);
                if (spilmap.get(navn) == null) {
                    spilmap.put(navn, new SpilImpl());
                }


                return sessionkey;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Failed";

        }
        return "Failed";
    }

    @Override
    public void logud(String navn) {
        sessionmap.remove(navn);

    }

    @Override
    public SpilInterface startSpil(String brugernavn, String id) {
        if (!spilmap.containsKey(brugernavn)) {
            throw new IllegalArgumentException(brugernavn + " har ingen registrerede spil");
        }
        if (!sessionmap.get(brugernavn).equals(id)) {
            logud(brugernavn);
            throw new IllegalArgumentException(brugernavn + " unikt id er forkert! logger ud af sessionen");
        }

        return spilmap.get(brugernavn);

    }
    @Override
    public Map<String,Integer> gethighscore(){
        Map<String, Integer> result = scoremap.entrySet()
                .stream()
                .limit(10)
                .sorted(reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return result;
    }

    @Override
    public void addnewScore(String navn, int score) throws RemoteException {
        int temp=0;
        if(scoremap.get(navn)!=null){
            temp = scoremap.get(navn);
        }

        if(score>temp){
            scoremap.put(navn,score);
        }

    }


}




