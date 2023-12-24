package org.example;
import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Node2 {
    public static void main(String[] args) throws IOException{
        System.out.println("Initializing Node 2");
        File st2 = new File ("node2/final");
        st2.mkdirs();

        File st1 = new File ("node2/temp");
        st1.mkdirs();

        File main = new File ("node2/final/final.txt");
        main.createNewFile();

        File temp = new File ("node2/temp/temp.txt");
        temp.createNewFile();
        try {
            Functionality f = new Functionality("node2");
            Interface s = (Interface) UnicastRemoteObject.exportObject(f, 0);
            Registry registry = LocateRegistry.createRegistry(1889);
            registry.bind("node2", s);




        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
