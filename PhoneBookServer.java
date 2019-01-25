/*
 *  Książka telefoniczna realizowana przy pomocy komunikacji sieciowej
 *   - Serwer
 *
 *  Autor: Adrian Mrówka
 *  Nr indeksu: 234939
 *  Data: 25 stycznia 2019 r.
 */

package phoneBook;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class PhoneBookServer extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;
    static final int SERVER_PORT = 25000;

    JLabel console_label = new JLabel("Log: ");
    JTextArea console = new JTextArea(15,20);
    JScrollPane console_scroll = new JScrollPane(console, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private PhoneBook database = new PhoneBook();

    PhoneBookServer() {
        super("Serwer ksiazki telefonicznej");
        setSize(300,350);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        panel.add(console_label);

        panel.add(console_scroll);
        console.setEditable(false);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);

        setContentPane(panel);
        setVisible(true);

        new Thread(this).start();
    }

    synchronized public void printReceived(PhoneBookClientThread client, String message) {
        String textBefore = console.getText();
        console.setText("[" + client.getName() + " -> Server]: " + message + '\n' + textBefore);
    }

    synchronized public void printSent(PhoneBookClientThread client, String message) {
        String textBefore = console.getText();
        console.setText("[Server -> " + client.getName() + "]: " + message + '\n' + textBefore);
    }

    void close() {

    }

    @Override
    public void run() {

        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            String host = InetAddress.getLocalHost().getHostName();
            System.out.println("Serwer uruchomiony: " + host);

            while (true) {
                Socket socket = server.accept();
                if (socket != null) {
                    new PhoneBookClientThread(this, socket, database);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "BLAD (IOException)");
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        new PhoneBookServer();
    }
}

