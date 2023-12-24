package org.example;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.*;

public class Functionality implements Interface {
    Map<UUID, String> map = new HashMap<>();
    String node = "";


    Map<UUID, String> temp = new HashMap<>();
    @Override
    public boolean commit(UUID id) throws IOException {
        System.out.println("commit for id: " + id);
        if (map.containsKey(id)) {
            return true;
        }
        if (temp.containsKey(id)) {
            map.put(id, temp.get(id));
            temp.remove(id);
            File finalFile = new File(node + "/final/final.txt");
            finalFile.delete();
            finalFile.createNewFile();

            try (BufferedWriter bf = new BufferedWriter(new FileWriter(finalFile))) {
                bf.write("");
                for (Map.Entry<UUID, String> entry : map.entrySet()) {
                    bf.write(entry.getKey() + ":" + entry.getValue());
                    bf.newLine();
                }
                bf.flush();
                File tempFile = new File(node + "/temp/temp.txt");
                tempFile.delete();
                tempFile.createNewFile();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            return false;
        }
    }
    public static void print(Map<UUID, String> m) {
        m.forEach((k, v) ->
                System.out.println("Clearing :" + k + " = " + v));
    }





    @Override
    public boolean abort(UUID id) throws IOException {
        System.out.println("aborting for id: " + id);temp.remove(id);
        File tempFile = new File(node + "/temp/temp.txt");
        tempFile.delete();tempFile.createNewFile();


        try (BufferedWriter bf = new BufferedWriter(new FileWriter(tempFile))) {
            bf.write("");
            for (Map.Entry<UUID, String> entry : temp.entrySet()) {
                bf.write(entry.getKey() + ":" + entry.getValue());
                bf.newLine();
            }
            bf.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    public void load() {
        try {
            Files.lines(Paths.get(node + "/temp/temp.txt"))
                    .forEach(line -> {
                        String[] parts = line.split(":");
                        String uid = parts[0].trim();
                        String value = parts[1].trim();
                        if (!uid.equals("") && !value.equals(""))
                            temp.put(UUID.fromString(uid), value);
                    });

            Files.lines(Paths.get(node + "/final/final.txt"))
                    .forEach(line -> {
                        String[] parts = line.split(":");
                        String uid = parts[0].trim();
                        String value = parts[1].trim();
                        if (!uid.equals("") && !value.equals(""))
                            map.put(UUID.fromString(uid), value);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean prepare(UUID id) throws IOException {
        System.out.println("prepare for: " + id);
        if (temp.containsKey(id)) {
            File tempFile = new File(node + "/temp/temp.txt");
            try (BufferedWriter bf = new BufferedWriter(new FileWriter(tempFile))) {
                bf.write("");
                for (Map.Entry<UUID, String> entry : temp.entrySet()) {
                    bf.write(entry.getKey() + ":" + entry.getValue());
                    bf.newLine();
                }
                bf.flush();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Responding yes for: " + id);
            return true;
        }
        System.out.println("Responding no for: " + id);
        return false;
    }

    @Override
    public void removeTemp() throws IOException {
        print(temp);
        temp.clear();

        Files.write(Paths.get(node + "/temp/temp.txt"), new byte[0]);
    }




    @Override
    public UUID put(UUID id, String m) throws RemoteException {
        System.out.println("give");
        temp.put(id, m);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            removeTemp();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, 30000);
        return id;
    }
    public Functionality(String node) {
        this.node = node;
        load();
    }


    @Override
    public String get(UUID id) throws RemoteException {
        return map.get(id);
    }


}
