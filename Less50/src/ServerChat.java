import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerChat {
    ServerSocket listener = null;
    Socket client = null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    int port = 8888;
    String msg = "";


    public static void main(String[] args) {
        ServerChat server = new ServerChat();
        while(true)
        {
            server.listen();
        }
    }
    void listen() {
        try {
            listener = new ServerSocket(port);
            System.out.println("Waiting for connection");
            client = listener.accept();

            System.out.println("Client connected " +
                    client.getInetAddress().getHostName());
            out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            in = new ObjectInputStream(client.getInputStream());
            do{
                try {
                    msg = (String)in.readObject();
                    System.out.println("client> " + msg);
                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date d = new Date();
                    sendMessage("Message received;" + df.format(d));
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }while(!msg.equals("exit"));
        } catch (IOException ex) {
            Logger.getLogger(ServerChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                if(in != null)
                    in.close();
                if(out != null)
                    out.close();
                if(listener != null)
                    listener.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerChat.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
}