import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by noyz on 6/18/17.
 */

/**
 * User class has all information about user's data
 * this class will get completed during the developement
 * every time the User class changes the serialized file should be deleted and made again because the class has changed
 */
class User{
    UserFirstInfo userFirstInfo;
    User(UserFirstInfo userFirstInfo){
        this.userFirstInfo=userFirstInfo;
    }
}
/*
the information which is entered on sign up
 */
class UserFirstInfo implements Serializable {
    String fullName;
    String username;
    String password;

    UserFirstInfo(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }
}

class SocketAndStreams {
    Socket socket;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    SocketAndStreams(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
        this.socket = socket;
        this.objectInputStream = objectInputStream;
    }
}

class SocketHandler implements Runnable {
    Socket socket;
    ObjectInputStream inputFromSocket;
    ObjectOutputStream outputToSocket;

    public SocketHandler(Socket socket, ObjectInputStream inputFromSocket, ObjectOutputStream outputToSocket) {
        this.socket = socket;
        this.inputFromSocket = inputFromSocket;
        this.outputToSocket = outputToSocket;
    }

    @Override
    public void run() {
        try {
            String command = (String) inputFromSocket.readObject();
            switch (command) {
                case "get users": {
                    File usersFile = new File("data/users.ser");
                    if (!usersFile.exists()) {
                        usersFile.createNewFile();
                    }
                    ObjectInputStream userInput = new ObjectInputStream(new FileInputStream(usersFile));
                    ArrayList<UserFirstInfo> users = (ArrayList<UserFirstInfo>) (userInput.readObject());
                    HashSet<String> usernames = new HashSet<>();
                    Iterator<UserFirstInfo> userIterator = users.iterator();
                    while (userIterator.hasNext()) {
                        usernames.add(userIterator.next().username);
                    }
                    outputToSocket.writeObject(usernames);
                    outputToSocket.close();
                    inputFromSocket.close();
                    break;
                }
                case "sign_up": {
                    String fullName = ((String) inputFromSocket.readObject());
                    String username = ((String) inputFromSocket.readObject());
                    String password = ((String) inputFromSocket.readObject());
                    UserFirstInfo newUserFirstInfo = new UserFirstInfo(fullName, username, password);
                    User newUser = new User(newUserFirstInfo);
                    File newUserFolder = new File("data/Users/"+ newUser.userFirstInfo.username);
                    newUserFolder.mkdir();
                    File newUserFile = new File("data/Users/"+ newUser.userFirstInfo.username +"/" + "users.ser");
                    newUserFile.createNewFile();
                    try {
                        ObjectOutputStream usersOutputStream = new ObjectOutputStream(new FileOutputStream(newUserFile));
                        usersOutputStream.writeObject(newUser);
                        usersOutputStream.close();
                    }catch (IOException e){
                        System.out.println("Problem while serializing the user data");
                        e.printStackTrace();
                    }
                    File usersFile = new File("data/users.ser");
                    if (!usersFile.exists()) {
                        usersFile.createNewFile();
                    }
                    ObjectInputStream userFileInput = new ObjectInputStream(new FileInputStream(usersFile));
                    ArrayList<UserFirstInfo> users = (ArrayList<UserFirstInfo>) (userFileInput.readObject());
                    users.add(newUserFirstInfo);
                    ObjectOutputStream userFileOutput = new ObjectOutputStream(new FileOutputStream(usersFile));
                    userFileOutput.writeObject(users);
                    userFileOutput.close();
                    userFileInput.close();
                    outputToSocket.writeBoolean(true);
                    outputToSocket.flush();
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("IN RUN PROBLEM CLASS NOT FOUND");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IN RUN IO");
            e.printStackTrace();
        }
    }
}

class Waiter implements Runnable {
    static int count;
    ServerSocket server;
    ArrayList<SocketAndStreams> sockets;
    Waiter(ServerSocket server) {
        sockets = new ArrayList<SocketAndStreams>();
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = server.accept();
                ObjectInputStream inputFromSocket = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputToSocket = new ObjectOutputStream(socket.getOutputStream());
                if (socket != null)
                    sockets.add(new SocketAndStreams(socket, inputFromSocket, outputToSocket));
                count++;
                SocketHandler socketHandler = new SocketHandler(socket, inputFromSocket, outputToSocket);
                Thread t = new Thread(socketHandler);
                t.start();
            } catch (Exception e) {
                System.out.println("WAITER ACCEPT PROBLEM");
                System.out.println(count);
                e.printStackTrace();
                return;
            }

        }
    }
}

public class Server {
    public static void inits() {
        File usersDir = new File("data/Users");
        if(!usersDir.exists()){
            usersDir.mkdirs();
        }
        File users_file = new File("data/users.ser");
        try {
            if (!users_file.exists()) {
                users_file.createNewFile();
                ArrayList<User> users = new ArrayList<User>();
                ObjectOutputStream userOutput = new ObjectOutputStream(new FileOutputStream(users_file));
                userOutput.writeObject(users);
                userOutput.close();
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Problem in initing data users.ser");
        }
    }

    public static void main(String[] args) {
        inits();
        Scanner commandGetter = new Scanner(System.in);
        try {
            ServerSocket server = new ServerSocket(1234);
            Waiter waiter = new Waiter(server);
            Thread t = new Thread(waiter);
            t.start();

            while (true) {
                String command = commandGetter.next();
                if ((command.compareTo("exit")) == 0) {
                    for (SocketAndStreams i : waiter.sockets) {
                        if ((!i.socket.isClosed()) && (i.socket.isConnected())) {
                            i.objectOutputStream.writeFloat(2);
                            i.objectOutputStream.flush();
                            i.objectOutputStream.close();
                        }
                    }
                    server.close();
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Problem While making server");
            e.printStackTrace();
        }
    }
}
