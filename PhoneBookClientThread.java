/*
 *  Książka telefoniczna realizowana przy pomocy komunikacji sieciowej
 *   - Wątek klienta
 *
 *  Autor: Adrian Mrówka
 *  Nr indeksu: 234939
 *  Data: 25 stycznia 2019 r.
 */

package phoneBook;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PhoneBookClientThread implements Runnable {
    private Socket socket;
    private String name;
    private PhoneBookServer server;
    private ObjectOutputStream oos;
    private PhoneBook database;

    PhoneBookClientThread(String name) {
        this.name = name;
    }

    PhoneBookClientThread(PhoneBookServer server, Socket socket, PhoneBook database) {
        this.server = server;
        this.socket = socket;
        this.database = database;
        new Thread(this).start();
    }

    String getName() {
        return name;
    }

    public void sendMessage(String message) {
        try {
            oos.writeObject(message);
            if (message.equals("CLOSE")) {
                socket.close();
                socket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void run() {
        String message;

        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());) {

            this.oos = out;
            this.name = (String)in.readObject();
            while(true) {
                message = (String)in.readObject();
                server.printReceived(this, message);
                if (message.startsWith("LOAD ")) {
                    String [] parts = message.split(" ");
                    String newMessage = database.load(parts[1]);
                    server.printSent(this, newMessage);
                    sendMessage(newMessage);
                }
                else if (message.startsWith("SAVE ")) {
                    String [] parts = message.split(" ");
                    String newMessage = database.save(parts[1]);
                    server.printSent(this, newMessage);
                    sendMessage(newMessage);
                }
                else if (message.startsWith("GET ")) {
                    String[] parts = message.split(" ");
                    String newMessage = database.get(parts[1]);
                    server.printSent(this, newMessage);
                    sendMessage(newMessage);
                }
                else if (message.startsWith("PUT ")) {
                    String [] parts = message.split(" ");
                    String newMessage = database.put(parts[1], parts[2]);
                    server.printSent(this, newMessage);
                    sendMessage(newMessage);
                }
                else if (message.startsWith("REPLACE ")) {
                    String [] parts = message.split(" ");
                    String newMessage = database.replace(parts[1], parts[2]);
                    server.printSent(this, newMessage);
                    sendMessage(newMessage);
                }
                else if (message.startsWith("DELETE ")) {
                    String [] parts = message.split(" ");
                    String newMessage = database.delete(parts[1]);
                    server.printSent(this, newMessage);
                    sendMessage(newMessage);
                }
                else if (message.equals("LIST")) {
                    String newMessage = database.list();
                    server.printSent(this, newMessage);
                    sendMessage(newMessage);
                }
                else if (message.equals("CLOSE")) {
                    sendMessage("Zamykanie gniazda...");
                    server.close();
                    //TODO
                }
                else if (message.equals("BYE")) {
                    server.printSent(this, "Zamkniecie okna za 3s...");
                    sendMessage("Zamkniecie okna za 3s...");
                    Thread.sleep(3000);
                    throw new Exception("Zamkniecie programu na zyczenie uzytkownika");
                }
                else if (message.startsWith("HELP")) {
                    server.printSent(this, MyStrings.HELP_INFO);
                    sendMessage(MyStrings.HELP_INFO);
                }
                else {
                    server.printSent(this, MyStrings.UNKNOWN_COMMAND_INFO);
                    sendMessage(MyStrings.UNKNOWN_COMMAND_INFO);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
