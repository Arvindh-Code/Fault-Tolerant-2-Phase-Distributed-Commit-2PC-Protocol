package org.example;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Coordinator {

    private static void resumeCommit(Interface INode1, Interface INode2) {
        List<String> idList = new ArrayList<>();
        File finalFile = new File("tc/temp.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(finalFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                idList.add(line);
            }
            for (String element : idList) {
                boolean final1 = handleCommit(UUID.fromString(element), INode1);
                boolean final2 = handleCommit(UUID.fromString(element), INode2);
                if (!final1) {
                    System.out.println("Node 1 did not commit properly for: " + element);
                }
                if (!final2) {
                    System.out.println("Node 2 did not commit properly for: " + element);
                }
                if (final1 && final2) {
                    System.out.println("Committed complete for: " + element);
                }
                try (BufferedWriter bf = new BufferedWriter(new FileWriter("tc/temp.txt"))) {
                    bf.write("");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private static void ackException(UUID id, Interface node1, Interface node2) {
        boolean n1 = false;
        boolean n2 = false;
        int count = 0;
        while (!n1 || !n2) {
            try {
                if (!n1) {
                    node1 = connectNode("node1", "127.0.0.1", 1888);
                    if (node1 != null) {
                        System.out.println("Sending abort to node 1 for id: " + id);
                        n1 = node1.abort(id);
                    } else {
                        System.out.println("Sending abort to node 1 failed...");
                    }
                }
            } catch (IOException ignored) {
            }
            try {
                if (!n2) {
                    node2 = connectNode("node2", "127.0.0.1", 1889);
                    if (node2 != null) {
                        System.out.println("Sending abort to node 2 for id: " + id);
                        n2 = node2.abort(id);
                    } else {
                        System.out.println("Sending abort to node 2 failed...");
                    }
                }
            } catch (IOException ignored) {
            }
            count++;
            if (count == 10) {
                break;
            }
        }
    }

    private static void handlePrepareException(UUID id, Interface node1, Interface node2) {
        boolean n1 = false;
        boolean n2 = false;
        int count = 0;
        while (!n1 || !n2) {
            try {
                if (!n1) {
                    node1 = connectNode("node1", "127.0.0.1", 1888);
                    if (node1 != null) {
                        System.out.println("Sending abort to node 1 for id: " + id);
                        n1 = node1.abort(id);
                    } else {
                        System.out.println("Sending abort to node 1 failed...");
                    }
                }
            } catch (IOException ignored) {
            }
            try {
                if (!n2) {
                    node2 = connectNode("node2", "127.0.0.1", 1889);
                    if (node2 != null) {
                        System.out.println("Sending abort to node 2 for id: " + id);
                        n2 = node2.abort(id);
                    } else {
                        System.out.println("Sending abort to node 2 failed...");
                    }
                }
            } catch (IOException ignored) {
            }
            count++;
            if (count == 10) {
                break;
            }
        }
    }
    private static Interface connectNode(String url, String urlhost, int number) {
        Interface NodeI = null;
        while (NodeI == null) {
            try {
                Registry registry = LocateRegistry.getRegistry(urlhost, number);
                NodeI = (Interface) registry.lookup(url);
            } catch (RemoteException | NotBoundException ignored) {
            }
        }
        return NodeI;
    }
    private static void commitException(UUID id, Interface Inode1, Interface Inode2) throws IOException, InterruptedException {
        int count = 0;
        while (count < 10) {
            Inode1 = connectNode("node1", "127.0.0.1", 1888);
            if (Inode1 != null) {
                if (!Inode1.commit(id)) {
                    System.out.println("Node 1 did not commit properly");
                } else {
                    //System.out.println("node 1 committed");
                    break;
                }
            }
            count++;
        }
        if (count == 10) {
            System.out.println("Node 1 did not commit properly");
        }

        count = 0;
        while (count < 10) {
            Inode2 = connectNode("node2", "127.0.0.1", 1889);
            if (Inode2 != null) {
                if (!Inode2.commit(id)) {
                    System.out.println("Node 2 did not commit properly");
                } else {
                    System.out.println("Node 2 committed");
                    break;
                }
            }
            count++;
        }
        if (count == 10) {
            System.out.println("Node 2 did not commit properly");
        }
    }



    private static boolean handleCommit(UUID id, Interface node) {
        try {
            return node.commit(id);
        } catch (IOException e) {
            return false;
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Initializing Coordinator");
        File st = new File("tc");
        st.mkdirs();
        File tempFile = new File("tc/temp.txt");
        tempFile.createNewFile();

        Interface INode1 = connectNode("node1", "127.0.0.1", 1888);
        Interface INode2 = connectNode("node2", "127.0.0.1", 1889);
        resumeCommit(INode1, INode2);

        while (true) {
            System.out.println("Choose Operation [Acknowledge - give / To see the Value of Node1/Node2 - see / To Exit - exit]");
            Scanner sc = new Scanner(System.in);
            String operation = sc.nextLine();
                if(operation.equals("see")){
                    System.out.println("Enter ID");
                    UUID id = UUID.fromString(sc.nextLine());
                    System.out.println("value from Node 1: " + INode1.get(id));
                    System.out.println("value from Node 2: " + INode2.get(id));
                }
                else if(operation.equals("give")){
                    try {
                        System.out.println("Enter the option : ");
                        String message = sc.nextLine();
                        UUID id = UUID.randomUUID();
                        System.out.println("ID: " + id);

                        try {
                            INode1.put(id, message);
                            INode2.put(id, message);
                        } catch (IOException e) {
                            ackException(id, INode1, INode2);
                            break;
                        }
                        System.out.println("Preparing for commit");
                        //case 1:  40000
                        Thread.sleep(40000);

                        try {
                            boolean nodee1 = INode1.prepare(id);
                            boolean nodee2 = INode2.prepare(id);
                            if (!nodee1 || !nodee2) {
                                handlePrepareException(id, INode1, INode2);
                                break;
                            }
                        } catch (IOException e) {
                            handlePrepareException(id, INode1, INode2);
                            break;
                        }

                        System.out.println("Prepare Complete");
                        saveTemp(id);
                        Thread.sleep(10000);
                        if (!handleCommit(id, INode1) || !handleCommit(id, INode2))
                            commitException(id, INode1, INode2);
                        else
                            System.out.println("Committed complete");

                    } catch (IOException ignored) {
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(operation.equals("exit")){
                    System.exit(0);
                }
                else{
                    System.out.println("Invalid operation");
                }
        }
    }
    private static void saveTemp(UUID id) throws IOException {
        File temp = new File("tc/temp.txt");
        temp.delete();
        temp.createNewFile();
        try (BufferedWriter b = new BufferedWriter(new FileWriter(temp))) {
            b.write(id.toString());
            b.newLine();
            b.close();
        }
    }
}
