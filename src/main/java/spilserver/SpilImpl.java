package spilserver;

import model.Galgelogik;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SpilImpl extends UnicastRemoteObject implements SpilInterface {


    private Galgelogik logik = new Galgelogik();


    public SpilImpl() throws java.rmi.RemoteException {
        logik.hentOrdFraDr();

    }


    public void gætBogstav(String bogstav, String name, String id) {
        if (SpilServerImpl.sessionmap.get(name).equals(id)) {
            logik.gætBogstav(bogstav);
            return;
        }
        SpilServerImpl.sessionmap.remove(name);





    }


    @Override
    public int getAntalForkerteBogstaver() {

        return logik.getAntalForkerteBogstaver();


    }

    @Override
    public boolean erSpilletSlut() {

        return logik.erSpilletSlut();


    }

    @Override
    public boolean erSpilletVundet() {

        return logik.erSpilletVundet();


    }

    @Override
    public boolean erSpilletTabt() {

        return logik.erSpilletTabt();


    }


    @Override
    public String getSynligtOrd() {

        return logik.getSynligtOrd();


    }

    @Override
    public String getordet() {
        return logik.getOrdet();
    }

    @Override
    public ArrayList<String> getBrugteBogstaver() {
        return logik.getBrugteBogstaver();
    }

    @Override
    public void nulstil(String name, String id) {
        if (SpilServerImpl.sessionmap.get(name).equals(id)) {
            logik.nulstil();
            return;
        }
        SpilServerImpl.sessionmap.remove(name);
    }


}


