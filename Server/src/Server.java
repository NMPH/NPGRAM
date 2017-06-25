
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sun.misc.JavaObjectInputStreamAccess;
import sun.util.resources.cldr.ebu.CalendarData_ebu_KE;

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
    HashSet<String> blockedUsernames;
    HashSet<String> blockedByUsernames;
    boolean Private;
    String bio;
    byte[] profilePicture;
    TreeSet<Post> posts;
    UserFirstInfo userFirstInfo;
    HashSet<Chat> chats;
    boolean online;
    @Override
    public int hashCode(){
         return userFirstInfo.username.hashCode() + userFirstInfo.password.hashCode()+userFirstInfo.fullName.hashCode();
    }
    @Override
    public boolean equals(Object o){
        return hashCode()==o.hashCode() ? true : false;
    }
    User(UserFirstInfo userFirstInfo) {
        this.userFirstInfo = userFirstInfo;
        Private = false;
        posts = new TreeSet<Post>();
        followersUsernames = new HashSet<String>();
        followingsUsernames = new HashSet<String>();
        followRequestsSent = new HashSet<String>();
        followRequestsRecieved = new HashSet<String>();
        blockedUsernames = new HashSet<String>();
        blockedByUsernames = new HashSet<String>();
        chats = new HashSet<Chat>();

    }
    public static User getUserFromHashSet(HashSet<User> userFile,String username){
        Iterator<User> userIterator = userFile.iterator();
        while(userIterator.hasNext()){
            User thisUser = userIterator.next();
            if(thisUser.userFirstInfo.username.equals(username)){
                return thisUser;
            }
        }
        return null;

    }
    public HashSet<String> getChattersSet() {
        HashSet<String> usernames = new HashSet<String>();
        Iterator<Chat> chatIterator = chats.iterator();
        while (chatIterator.hasNext()) {
            Chat chat = chatIterator.next();
            if (chat.chatter1.equals(userFirstInfo.username)) {
                usernames.add(chat.chatter2);
            } else {
                usernames.add(chat.chatter1);
            }
        }
        return usernames;
    }

    public boolean removePostByHashCode(int hashcode) {
        Iterator<Post> postIterator = posts.iterator();
        while (postIterator.hasNext()) {
            Post post = postIterator.next();
            if (post.hashCode() == hashcode) {
                posts.remove(post);
                return true;
            }
        }
        return false;
    }

    public boolean isBlockedBy(String username) {
        if (blockedByUsernames.contains(username))
            return true;
        return false;
    }

    public boolean isFollowedBy(String username) {
        if (followersUsernames.contains(username))
            return true;
        return false;
    }

    public boolean isFollowRequestSent(String username) {
        if (this.followRequestsSent.contains(username))
            return true;
        return false;
    }

    public boolean isFollowRequestRecieved(String username) {
        if (this.followRequestsRecieved.contains(username))
            return true;
        return false;
    }

    public boolean isFollowing(String username) {
        if (followingsUsernames.contains(username))
            return true;
        return false;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
}

class ChatMessage implements Serializable {
    String owner;
    String text;
    byte[] image;

    ChatMessage(String owner, String text, byte[] image) {
        this.text = text;
        this.image = image;
        this.owner = owner;
    }
}

class Chat implements Serializable {
    @Override
    public int hashCode() {
        return chatter1.hashCode() + chatter2.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if(object==null)
            return false;
        return hashCode() == object.hashCode() ? true : false;
    }

    public boolean containsChatter(String chatter) {
        if (chatter1.equals(chatter) || chatter2.equals(chatter))
            return true;
        return false;
    }

    String chatter1;
    String chatter2;
    ArrayList<ChatMessage> chatMessages;

    Chat(String chatter1, String chatter2) {
        this.chatter1 = chatter1;
        this.chatter2 = chatter2;
        chatMessages = new ArrayList<ChatMessage>();
    }

}

class Post implements Serializable, Comparable<Post> {
    @Override
    public int compareTo(Post post) {
        int ret = -date.compareTo(post.date);
        return ret;
    }

    class PostDate implements Serializable, Comparable<PostDate> {
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int seconds;

        PostDate() {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);
            seconds = cal.get(Calendar.SECOND);
        }

        @Override
        public int hashCode() {
            return year * month * day;
        }

        @Override
        public String toString() {
            return year + "/" + month + "/" + day + " on:" + hour + ": " + minute;
        }

        @Override
        public int compareTo(PostDate postDate) {
            if (year != postDate.year)
                return year - postDate.year;
            if (month != postDate.month)
                return month - postDate.month;
            if (day != postDate.day)
                return day - postDate.day;
            if (hour != postDate.hour)
                return hour - postDate.hour;
            if (day != postDate.day)
                return day - postDate.day;
            return seconds - postDate.seconds;
        }
    }

    ArrayList<String> likes;
    ArrayList<NPComment> comments;
    String caption;
    byte[] image;
    String ownerUsername;
    PostDate date;

    Post(byte[] image, String caption, String ownerUsername) {
        this.ownerUsername = ownerUsername;
        this.image = image;
        this.caption = caption;
        comments = new ArrayList<NPComment>();
        likes = new ArrayList<String>();
        date = new PostDate();
    }

    Post(Post post) {
        this.ownerUsername = post.ownerUsername;
        this.image = post.image;
        this.caption = post.caption;
        this.comments = post.comments;
        this.likes = post.likes;
        this.date = post.date;
    }

    @Override
    public int hashCode() {
        return caption.length() * 3 + (int) image.length + ownerUsername.length() + date.hashCode();
    }
}

class NPComment implements Serializable {
    String username;
    String text;
    HashSet<String> likes;

    NPComment(String text, String username) {
        this.text = text;
        this.username = username;
        likes = new HashSet<String>();
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

class FileHandler {
    synchronized public static void writeUserFilesAndUsers(HashSet<User> userFile, HashSet<UserFirstInfo> users){
        File usersFirstInfoFile = new File("data/users.ser");
        usersFirstInfoFile.delete();
        try {
            usersFirstInfoFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(usersFirstInfoFile);
            ObjectOutputStream fileObjectOutputStream = new ObjectOutputStream(fileOutputStream);
            fileObjectOutputStream.writeObject(users);
            fileObjectOutputStream.close();
            fileOutputStream.close();
        }catch (IOException e){
            System.out.println("Exception while writing files :(");
            e.printStackTrace();
        }
        Iterator<UserFirstInfo> userFirstInfoIterator = users.iterator();
        while(userFirstInfoIterator.hasNext()){
            UserFirstInfo thisUserFirstInfo = userFirstInfoIterator.next();
            File thisUserFile = new File("data/Users/" + thisUserFirstInfo.username + "/" + "users.ser");
            File userFolder = new File("data/Users/"+ thisUserFirstInfo.username);
            thisUserFile.delete();
            try{
                userFolder.mkdirs();
                thisUserFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(thisUserFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(User.getUserFromHashSet(userFile,thisUserFirstInfo.username));
                fileOutputStream.close();
                objectOutputStream.close();
            }catch (IOException e){
                System.out.println("Problem while writing into each user file");
                e.printStackTrace();
            }
        }

    }
    synchronized public static User getUser(String username) {
        User retUser = null;
        try {
            File userFile = new File("data/Users/" + username + "/" + "users.ser");
            FileInputStream userFileInputStream = new FileInputStream(userFile);
            ObjectInputStream userObjectInputStream = new ObjectInputStream(userFileInputStream);
            retUser = ((User) userObjectInputStream.readObject());
            userObjectInputStream.close();
            userFileInputStream.close();
        } catch (IOException e) {
            System.out.println("Problem while reading user file");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("class not found?!!!!!! wtf!!! in getUser");
            e.printStackTrace();
        }
        return retUser;

    }

    synchronized public static ArrayList<Byte> getIcon(String path) {
        ArrayList<Byte> byteArrayList = new ArrayList<Byte>();
        File iconFile = new File(path);
        try {
            FileInputStream fileInputStream = new FileInputStream(iconFile);
            int i;
            while ((i = fileInputStream.read()) != -1) {
                byteArrayList.add((byte) i);
            }
            fileInputStream.close();
            return byteArrayList;
        } catch (IOException e) {
            System.out.println("icon profileImage Sending PROBLEM");
            e.printStackTrace();
        }
        return byteArrayList;

    }

    synchronized public static HashSet<UserFirstInfo> getUsersFile() {
        HashSet<UserFirstInfo> users = null;
        try {
            File usersFile = new File("data/users.ser");
            FileInputStream fileInputStream = new FileInputStream(usersFile);
            ObjectInputStream userFileInput = new ObjectInputStream(fileInputStream);
            users = (HashSet<UserFirstInfo>) (userFileInput.readObject());
            fileInputStream.close();
            userFileInput.close();
        } catch (IOException e) {
            System.out.println("problem getUsersFIle");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("CLASS NOT FOUND getUsersFile");
            e.printStackTrace();
        }
        return users;
    }

}

class SocketHandler implements Runnable {
    Socket socket;
    ObjectInputStream inputFromSocket;
    ObjectOutputStream outputToSocket;
    HashSet<User> userFile;
    HashSet<UserFirstInfo> usersFirstInfos;

    public SocketHandler
            (Socket socket, ObjectInputStream inputFromSocket,
             ObjectOutputStream outputToSocket, HashSet<User> userFile, HashSet<UserFirstInfo> usersFirstInfos) {
        this.socket = socket;
        this.inputFromSocket = inputFromSocket;
        this.outputToSocket = outputToSocket;
        this.userFile = userFile;
        this.usersFirstInfos = usersFirstInfos;
    }

    @Override
    public void run() {
        try {
            String command = (String) inputFromSocket.readObject();
            if (command.equals("get user")) {
                command = command;
            }
            switch (command) {
                case "get_home_icon": {
                    outputToSocket.writeObject(FileHandler.getIcon("data/InitData/home-button.png"));
                    break;
                }
                case "get_init_image": {
                    outputToSocket.writeObject(FileHandler.getIcon("data/InitData/initImage.jpg"));
                    break;
                }
                case "get_refresh_icon": {
                    outputToSocket.writeObject(FileHandler.getIcon("data/InitData/Refresh_icon.png"));
                    break;
                }
                case "login": {
                    String usernameEntered = ((String) inputFromSocket.readObject());
                    String passwordEntered = ((String) inputFromSocket.readObject());
                    Iterator<UserFirstInfo> userFirstInfoIterator = usersFirstInfos.iterator();
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
                    break;
                }
                case "write user": {
                    String oldUsername = ((String) inputFromSocket.readObject());
                    User newUserFile = ((User) inputFromSocket.readObject());
                        //changing user file to the new one
                        //changing the allUserFile
                    Iterator<UserFirstInfo> userFirstInfoIterator = usersFirstInfos.iterator();
                    User oldUser = User.getUserFromHashSet(userFile,oldUsername);
                    userFile.remove(oldUser);
                    userFile.add(newUserFile);
                    while (userFirstInfoIterator.hasNext()) {
                            UserFirstInfo thisUserFirstInfo = userFirstInfoIterator.next();
                            if(thisUserFirstInfo.username.equals(oldUsername)){
                                userFirstInfoIterator.remove();
                                usersFirstInfos.add(newUserFile.userFirstInfo);
                                break;
                            }
                            //not changing it!!!
                            /*userFile.remove(User.getUserFromHashSet(userFile,oldUsername));
                                userFile.add(newUserFile);
                                userFirstInfoIterator.remove();
                                usersFirstInfos.add(newUserFile.userFirstInfo);
                                break;*/
                        }

                    break;
                }
                case "get user": {
                    String username = ((String) inputFromSocket.readObject());
                    outputToSocket.writeObject(User.getUserFromHashSet(userFile,username));
                    outputToSocket.flush();
                    System.out.println("FUCK");
                    //just wrote user file!
                    break;
                }
                case "get users": {
                    HashSet<String> usernames = new HashSet<String>();
                    Iterator<UserFirstInfo> userFirstInfoIterator = usersFirstInfos.iterator();
                    while(userFirstInfoIterator.hasNext()){
                        usernames.add(userFirstInfoIterator.next().username);
                    }
                    outputToSocket.writeObject(usernames);
                    break;
                }
                case "sign_up": {
                    String fullName = ((String) inputFromSocket.readObject());
                    String username = ((String) inputFromSocket.readObject());
                    String password = ((String) inputFromSocket.readObject());
                    UserFirstInfo newUserFirstInfo = new UserFirstInfo(fullName, username, password);
                    User newUser = new User(newUserFirstInfo);
                    userFile.add(newUser);
/*                    if (!usersFile.exists()) {
                        usersFile.createNewFile();
                    }*/
                    usersFirstInfos.add(newUser.userFirstInfo);
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
    HashSet<User> userFiles;
    HashSet<UserFirstInfo> usersFirstInfo;

    Waiter(ServerSocket server, HashSet<User> userFiles, HashSet<UserFirstInfo> usersFirstInfo) {
        sockets = new ArrayList<SocketAndStreams>();
        this.server = server;
        this.userFiles = userFiles;
        this.usersFirstInfo = usersFirstInfo;
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
                SocketHandler socketHandler = new SocketHandler(socket, inputFromSocket, outputToSocket, userFiles, usersFirstInfo);
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

public class Server extends Application{
    public static void inits() {
        File usersDir = new File("data/Users");
        if (!usersDir.exists()) {
            usersDir.mkdirs();
        }
        File users_file = new File("data/users.ser");
        try {
            if (!users_file.exists()) {
                users_file.createNewFile();
                HashSet<UserFirstInfo> users = new HashSet<UserFirstInfo>();
                ObjectOutputStream userOutput = new ObjectOutputStream(new FileOutputStream(users_file));
                userOutput.writeObject(users);
                userOutput.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem in initing data users.ser");
        }
    }

    public static void setHashSetsFromFiles(HashSet<User> userFiles, HashSet<UserFirstInfo> usersFirstInfo) {
        Iterator<UserFirstInfo> userFirstInfoIterator = FileHandler.getUsersFile().iterator();
        while (userFirstInfoIterator.hasNext()) {
            UserFirstInfo userFirstInfo = userFirstInfoIterator.next();
            userFiles.add(FileHandler.getUser(userFirstInfo.username));
            usersFirstInfo.add(userFirstInfo);
        }
    }
/*    public static void openGUI(){
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root,600,400);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/
    public static void main(String[] args) {
        launch(args);


/*        Scanner commandGetter = new Scanner(System.in);
        try {
            ServerSocket server = new ServerSocket(1234);
            Waiter waiter = new Waiter(server, userFiles, usersFirstInfo);
            Thread t = new Thread(waiter);
            t.start();

            while (true) {
                String command = commandGetter.next();
                if ((command.equals("exit"))) {
                    FileHandler.writeUserFilesAndUsers(userFiles,usersFirstInfo);
                 *//*   for (SocketAndStreams i : waiter.sockets) {
                        if ((!i.socket.isClosed()) && (i.socket.isConnected())) {
                           // i.objectOutputStream.writeFloat(2);
                            i.objectOutputStream.flush();
                            i.objectOutputStream.close();
                        }
                    }*//*
                    server.close();
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Problem While making server");
            e.printStackTrace();
        }*/
    }

    @Override
    public void start(Stage stage) throws Exception {
        inits();
        HashSet<User> userFiles = new HashSet<User>();
        HashSet<UserFirstInfo> usersFirstInfo = new HashSet<UserFirstInfo>();
        setHashSetsFromFiles(userFiles, usersFirstInfo);
        serverThread serverThread = new serverThread(userFiles,usersFirstInfo);
        Thread t = new Thread(serverThread);
        t.start();
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setControllerFactory(c -> {
                return new ServerGUIPageController(userFiles,usersFirstInfo,stage);
            });
            Parent root=loader.load(getClass().getResource("ServerGUIPage.fxml").openStream());
            ServerGUIPageController serverGUIPageController=(ServerGUIPageController) loader.getController();
            Scene scene=new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        }catch (IOException e){
            System.out.println("problem in showSearch function in Showings");
        }
    }
}
class serverThread implements Runnable {
    HashSet<User> userFiles;
    HashSet<UserFirstInfo> usersFirstInfo;
    public serverThread(HashSet<User> userFiles,HashSet<UserFirstInfo> usersFirstInfo){
        this.userFiles=userFiles;
        this.usersFirstInfo=usersFirstInfo;
    }

    @Override
    public void run() {
        Server.inits();
        Server.setHashSetsFromFiles(userFiles, usersFirstInfo);
        Scanner commandGetter = new Scanner(System.in);
        try {
            ServerSocket server = new ServerSocket(1234);
            //ServerSocket server = new ServerSocket(1234);
            Waiter waiter = new Waiter(server, userFiles, usersFirstInfo);
            Thread t = new Thread(waiter);
            t.start();

            while (true) {
                String command = commandGetter.next();
                if ((command.equals("exit"))) {
                    FileHandler.writeUserFilesAndUsers(userFiles,usersFirstInfo);
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