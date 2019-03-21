package spilserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface SpilserverInterface extends Remote {

    String login(String navn, String password) throws RemoteException;

    void logud(String navn) throws RemoteException;

    public SpilInterface startSpil(String bruger, String id) throws RemoteException;

    Map<String,Integer> gethighscore() throws RemoteException;

    void addnewScore(String navn, int score)throws RemoteException;
}
