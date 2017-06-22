
import javafx.scene.image.Image;
import sun.misc.JavaObjectInputStreamAccess;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by noyz on 6/18/17.
 */

/**
 * User class has all information about user's data
 * this class will get completed during the developement
 * every time the User class changes the serialized file should be deleted and made again because the class has changed
 */
class User implements Serializable {
    HashSet<String> followingsUsernames;
    HashSet<String> followersUsernames;
    HashSet<String> followRequestsSent;
    HashSet<String> followRequestsRecieved;
    boolean isPrivate;
    String bio;
    File profilePicture;
    ArrayList<Post> posts;
    UserFirstInfo userFirstInfo;

    User(UserFirstInfo userFirstInfo) {
        this.userFirstInfo = userFirstInfo;
        isPrivate = false;
        posts = new ArrayList<Post>();
        followersUsernames = new HashSet<String>();
        followingsUsernames = new HashSet<String>();
        followRequestsSent = new HashSet<String>();
        followRequestsRecieved = new HashSet<String>();

    }

    public boolean isFollowedBy(String username) {
        if (followersUsernames.contains(username))
            return true;
        return false;
    }
    public boolean isFollowRequestSent(String username){
        if(this.followRequestsSent.contains(username))
            return true;
        return false;
    }
    public boolean isFollowRequestRecieved(String username){
        if(this.followRequestsRecieved.contains(username))
            return true;
        return false;
    }
    public boolean isFollowing(String username) {
        if (followingsUsernames.contains(username))
            return true;
        return false;
    }

    public void setProfilePicture(File profilePicture) {
        this.profilePicture = profilePicture;
    }
}

class Post implements Serializable {
    ArrayList<String> likes;
    ArrayList<NPComment> comments;
    String caption;
    File image;

    Post(File image) {
        this.image = image;
    }
}

class NPComment {
    String username;
    String text;

    NPComment(String text, String username) {
        this.text = text;
        this.username = username;
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
            if (command.equals("get user")) {
                command = command;
            }
            switch (command) {
                case "login": {
                    String usernameEntered = ((String) inputFromSocket.readObject());
                    String passwordEntered = ((String) inputFromSocket.readObject());
                    File usersFile = new File("data/users.ser");
                    System.out.println(usersFile.exists());
                    FileInputStream fileInputStream = new FileInputStream(usersFile);
                    ObjectInputStream userFileInput = new ObjectInputStream(fileInputStream);
                    ArrayList<UserFirstInfo> users = (ArrayList<UserFirstInfo>) (userFileInput.readObject());
                    Iterator<UserFirstInfo> userFirstInfoIterator = users.iterator();
                    boolean isLoginCorrect = false;
                    while (userFirstInfoIterator.hasNext()) {
                        UserFirstInfo userFirstInfo = userFirstInfoIterator.next();
                        if (userFirstInfo.username.equals(usernameEntered)) {
                            if (userFirstInfo.password.equals(passwordEntered)) {
                                outputToSocket.writeBoolean(true);
                                isLoginCorrect = true;
                            }
                        }
                    }
                    if (!isLoginCorrect)
                        outputToSocket.writeBoolean(false);
                    outputToSocket.flush();
                    userFileInput.close();
                    fileInputStream.close();
                    break;
                }
                case "write user": {
                    String oldUsername = ((String) inputFromSocket.readObject());
                    User newUserFile = ((User) inputFromSocket.readObject());
                    File userFile = new File("data/Users/" + oldUsername + "/" + "users.ser");
                    File userFolder = new File("data/Users/" + oldUsername);
                    File allUserFile = new File("data/users.ser");
                    try {
                        //changing user file to the new one
                        FileOutputStream usersFileOutputStream = new FileOutputStream(userFile);
                        ObjectOutputStream usersOutputStream = new ObjectOutputStream(usersFileOutputStream);
                        usersOutputStream.writeObject(newUserFile);
                        usersOutputStream.close();
                        usersFileOutputStream.close();
                        //changing the allUserFile
                        FileInputStream allUserFileInputStream = new FileInputStream(allUserFile);
                        ObjectInputStream userInput = new ObjectInputStream(allUserFileInputStream);
                        ArrayList<UserFirstInfo> usersArrayList = (ArrayList<UserFirstInfo>) (userInput.readObject());
                        Iterator<UserFirstInfo> userFirstInfoIterator = usersArrayList.iterator();
                        while (userFirstInfoIterator.hasNext()) {
                            UserFirstInfo thisUserFirstInfo = userFirstInfoIterator.next();
                            //not changing it!!!
                            if (thisUserFirstInfo.username.equals(oldUsername)) {
                                //thisUserFirstInfo=newUserFile.userFirstInfo;
                                userFirstInfoIterator.remove();
                                usersArrayList.add(newUserFile.userFirstInfo);
                                break;
                            }
                        }
                        allUserFileInputStream.close();
                        userInput.close();
                        FileOutputStream fileOutputStream =new FileOutputStream(allUserFile);
                        ObjectOutputStream allUserFileOutputStream = new ObjectOutputStream(fileOutputStream);
                        allUserFileOutputStream.writeObject(usersArrayList);
                        allUserFileOutputStream.close();
                        fileOutputStream.close();

                    } catch (IOException e) {
                        System.out.println("Problem while writing user");
                        e.printStackTrace();
                    }
                    if (!newUserFile.userFirstInfo.username.equals(oldUsername)) {
                        /*
                        this is not just writing!
                        we should also change the folder name because the username has changed!!!
                         */
                        File newName = new File("data/Users/" + newUserFile.userFirstInfo.username);
                        userFolder.renameTo(newName);

                    } else {
                        //just write!

                    }

                    break;
                }
                case "get user": {
                    String username = ((String) inputFromSocket.readObject());
                    File userFile = new File("data/Users/" + username + "/" + "users.ser");
                    FileInputStream userFileInputStream = new FileInputStream(userFile);
                    ObjectInputStream userObjectInputStream = new ObjectInputStream(userFileInputStream);
                    outputToSocket.writeObject(userObjectInputStream.readObject());
                    outputToSocket.flush();
                    System.out.println("FUCK");
                    //just wrote user file!
                    userFileInputStream.close();
                    userObjectInputStream.close();
                    break;
                }
                case "get users": {
                    File usersFile = new File("data/users.ser");
/*                    if (!usersFile.exists()) {
                        usersFile.createNewFile();
                    }*/
                    FileInputStream userFileInputStream = new FileInputStream(usersFile);
                    ObjectInputStream userInput = new ObjectInputStream(userFileInputStream);
                    ArrayList<UserFirstInfo> users = (ArrayList<UserFirstInfo>) (userInput.readObject());
                    HashSet<String> usernames = new HashSet<>();
                    Iterator<UserFirstInfo> userIterator = users.iterator();
                    while (userIterator.hasNext()) {
                        usernames.add(userIterator.next().username);
                    }
                    outputToSocket.writeObject(usernames);
                    outputToSocket.close();
                    inputFromSocket.close();
                    userFileInputStream.close();
                    userInput.close();
                    break;
                }
                case "sign_up": {
                    String fullName = ((String) inputFromSocket.readObject());
                    String username = ((String) inputFromSocket.readObject());
                    String password = ((String) inputFromSocket.readObject());
                    UserFirstInfo newUserFirstInfo = new UserFirstInfo(fullName, username, password);
                    User newUser = new User(newUserFirstInfo);
                    //setting initImage
                   /* File userInitImage = new File("data/InitData/initImage.jpg");
                    System.out.println(userInitImage.exists());
                    FileInputStream userFileInputStream = new FileInputStream(userInitImage);
                    newUser.setProfilePicture(userInitImage);*/
                    File newUserFolder = new File("data/Users/" + newUser.userFirstInfo.username);
                    newUserFolder.mkdir();
                    File newUserFile = new File("data/Users/" + newUser.userFirstInfo.username + "/" + "users.ser");
                    newUserFile.createNewFile();
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(newUserFile);
                        ObjectOutputStream usersOutputStream = new ObjectOutputStream(fileOutputStream);
                        usersOutputStream.writeObject(newUser);
                        usersOutputStream.close();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        System.out.println("Problem while serializing the user data");
                        e.printStackTrace();
                    }
                    File usersFile = new File("data/users.ser");
                    if (!usersFile.exists()) {
                        usersFile.createNewFile();
                    }
                    FileInputStream userFileInputStream = new FileInputStream(usersFile);
                    ObjectInputStream userFileInput = new ObjectInputStream(userFileInputStream);
                    ArrayList<UserFirstInfo> users = (ArrayList<UserFirstInfo>) (userFileInput.readObject());
                    users.add(newUserFirstInfo);
                    userFileInput.close();
                    userFileInputStream.close();
                    FileOutputStream fileOutputStream = new FileOutputStream(usersFile);
                    ObjectOutputStream userFileOutput = new ObjectOutputStream(fileOutputStream);
                    userFileOutput.writeObject(users);
                    userFileOutput.close();
                    fileOutputStream.close();
                    outputToSocket.writeBoolean(true);
                    outputToSocket.flush();
                    //userFileInputStream.close();
                    //userFileObjectInputStream.close();
                    break;
                }
         /*       case "get_init":{
                    File initFolder=new File("data/InitData");

                    break;
                }*/
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
        if (!usersDir.exists()) {
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
        } catch (IOException e) {
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
