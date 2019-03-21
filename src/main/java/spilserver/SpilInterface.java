package spilserver;

import java.util.ArrayList;


public interface SpilInterface extends java.rmi.Remote {


    void gætBogstav(String bogstav, String name, String id) throws java.rmi.RemoteException;

    int getAntalForkerteBogstaver() throws java.rmi.RemoteException;

    boolean erSpilletSlut() throws java.rmi.RemoteException;

    boolean erSpilletVundet() throws java.rmi.RemoteException;

    boolean erSpilletTabt() throws java.rmi.RemoteException;

    String getSynligtOrd() throws java.rmi.RemoteException;

    ArrayList<String> getBrugteBogstaver() throws java.rmi.RemoteException;

    String getordet() throws java.rmi.RemoteException;

    void nulstil(String name, String id) throws java.rmi.RemoteException;

}
