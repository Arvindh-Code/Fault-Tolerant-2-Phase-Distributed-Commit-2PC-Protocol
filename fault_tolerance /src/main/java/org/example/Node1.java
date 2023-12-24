package org.example;
import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Node1 {
    public static void main(String[] args) throws IOException {
        System.out.println("Initializing node 1");
        try {
            File st2 = new File ("node1/final");
            st2.mkdirs();

            File st = new File ("node1/temp");
            st.mkdirs();

            File main = new File ("node1/final/final.txt");
            main.createNewFile();

            File temp = new File ("node1/temp/temp.txt");
            temp.createNewFile();

            Functionality f = new Functionality("node1");
            Interface s = (Interface) UnicastRemoteObject.exportObject(f, 0);
            Registry registry = LocateRegistry.createRegistry(1888);
            registry.bind("node1", s);


        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
