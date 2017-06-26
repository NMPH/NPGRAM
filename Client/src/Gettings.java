import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by noyz on 6/21/17.
 */
public class Gettings {
    public static final String ip = "127.0.0.1";
    public static byte[] getHomeIcon(){
        byte[] image=null;
        try {
            Socket server = new Socket(ip, 1234);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());
            objectOutputStream.writeObject("get_home_icon");
            objectOutputStream.flush();
            ArrayList<Byte> byteArrayList = ((ArrayList<Byte>) objectInputStream.readObject());
            image = new byte[byteArrayList.size()];
            for (int i = 0; i <image.length ; i++) {
                image[i]=byteArrayList.get(i);
            }
            return image;
        } catch (IOException e) {
            System.out.println("problem in userExists func");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("wtf class not found??!!! userExists func");
        }
        return image;
    }
    public static byte[] getRefreshIcon(){
        byte[] image=null;
        try {
            Socket server = new Socket(ip, 1234);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());
            objectOutputStream.writeObject("get_refresh_icon");
            objectOutputStream.flush();
            ArrayList<Byte> byteArrayList = ((ArrayList<Byte>) objectInputStream.readObject());
             image = new byte[byteArrayList.size()];
            for (int i = 0; i <image.length ; i++) {
                image[i]=byteArrayList.get(i);
            }
            return image;
        } catch (IOException e) {
            System.out.println("problem in userExists func");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("wtf class not found??!!! userExists func");
        }
        return image;
    }
    public static byte[] getInitImage(){
        byte[] image=null;
        try {
            Socket server = new Socket(ip, 1234);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());
            objectOutputStream.writeObject("get_init_image");
            objectOutputStream.flush();
            ArrayList<Byte> byteArrayList = ((ArrayList<Byte>) objectInputStream.readObject());
            image = new byte[byteArrayList.size()];
            for (int i = 0; i <image.length ; i++) {
                image[i]=byteArrayList.get(i);
            }
            return image;
        } catch (IOException e) {
            System.out.println("problem in userExists func");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("wtf class not found??!!! userExists func");
        }
        return image;
    }
    public static File getFileChooserImage(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select A Photo : ");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"),new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        return fileChooser.showOpenDialog(stage);
    }
    public static boolean userExists(String username) {
        try {
            Socket server = new Socket(ip, 1234);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());
            objectOutputStream.writeObject("get users");
            objectOutputStream.flush();
            HashSet<String> usernames = ((HashSet<String>) objectInputStream.readObject());
            Iterator<String> usernameIterator = usernames.iterator();
            while (usernameIterator.hasNext()) {
                if (usernameIterator.next().equals(username))
                    return true;
            }
            return false;
        } catch (IOException e) {
            System.out.println("problem in userExists func");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("wtf class not found??!!! userExists func");
        }
        return false;
    }
    public static HashSet<String> getUsers(){
        try {
            Socket server = new Socket(ip, 1234);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());
            objectOutputStream.writeObject("get users");
            objectOutputStream.flush();
            HashSet<String > usernames = ((HashSet<String>) objectInputStream.readObject());
            return usernames;
        }catch (IOException e){
            System.out.println("Problem in Gettings.getUsers");
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            System.out.println("WTF CLASS NOT FOUND??!!! in Getting.getUsers");
            e.printStackTrace();
        }
        return null;
    }
    public static User getUser(String username) {
        try {
            Socket server = new Socket(ip, 1234);
            ObjectOutputStream outputToServer = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());
            outputToServer.writeObject("get user");
            outputToServer.flush();
            outputToServer.writeObject(username);
            outputToServer.flush();
            return ((User) inputFromServer.readObject());
        } catch (IOException e) {
            System.out.println("Error while loading initialize method in Profile");
        } catch (ClassNotFoundException e) {
            System.out.println("WTF CLASS NOT FOUND ????! IN INITIALIZE OF PROFILE");
        }
        return null;
    }

    public static void writeUser(String username, User user) {
        try {
            Socket server = new Socket(ip, 1234);
            ObjectOutputStream outputToServer = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());
            outputToServer.writeObject("write user");
            outputToServer.flush();
            outputToServer.writeObject(username);
            outputToServer.writeObject(user);
        } catch (IOException e) {
            System.out.println("problem is writeUser()");
        }
    }
    public static boolean isSignUp(String fullname,String username,String password){
        try {
            Socket server = new Socket(ip, 1234);
            ObjectOutputStream outputToServer = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());
            outputToServer.writeObject("sign_up");
            outputToServer.writeObject(fullname);
            outputToServer.writeObject(username);
            outputToServer.writeObject(password);
            outputToServer.flush();
            return  inputFromServer.readBoolean();
        }catch (IOException e){
            System.out.println("problem in isSignUp");
            e.printStackTrace();
        }
        return false;
    }
    public static boolean isLogin(String username, String password) {
        try {
            Socket server = new Socket(ip, 1234);
            ObjectOutputStream outputToServer = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());
            outputToServer.writeObject("login");
            outputToServer.writeObject(username);
            outputToServer.writeObject(password);
            outputToServer.flush();
            return inputFromServer.readBoolean();
        } catch (IOException e) {
            System.out.println("problem is isLogin() on GETTINGS");
            e.printStackTrace();
        }
        return false;
    }
}
