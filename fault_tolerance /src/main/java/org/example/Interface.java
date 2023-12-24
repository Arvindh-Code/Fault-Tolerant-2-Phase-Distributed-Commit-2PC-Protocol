package org.example;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;

public interface Interface extends Remote {
    String get(UUID id) throws RemoteException;
    boolean commit(UUID id) throws IOException;
    UUID put(UUID id,String message) throws RemoteException;
    void removeTemp() throws IOException;
    boolean abort(UUID id) throws IOException;
    boolean prepare(UUID id) throws IOException;






}
