import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientChat {
    Socket client = null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    String msg = "";
    int port = 8888;

    public static void main(String[] args) {
        ClientChat chat = new ClientChat();
        chat.setConnection();
    }

    void setConnection() {
        try {
            client = new Socket("127.0.0.1", port);
            System.out.println("Connected to server");
            out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            in = new ObjectInputStream(client.getInputStream());
            do {
                msg = JOptionPane.showInputDialog(this, "Enter your message:");
                if (msg == null)
                    msg = "";
                sendMessage(msg);
                if (!msg.equals("exit")) {
                    try {
                        msg = (String) in.readObject();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("server> " + msg);
                }
            } while (!msg.equals("exit"));
        } catch (IOException ex) {
            Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
                if (client != null)
                    client.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}