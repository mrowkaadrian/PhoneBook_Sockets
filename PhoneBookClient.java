/*
 *  Książka telefoniczna realizowana przy pomocy komunikacji sieciowej
 *   - klient
 *
 *  Autor: Adrian Mrówka
 *  Nr indeksu: 234939
 *  Data: 25 stycznia 2019 r.
 */

package phoneBook;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PhoneBookClient extends JFrame implements ActionListener, Runnable {

    static final int SERVER_PORT = 25000;

    private String name;
    private String hostname;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    JPanel panel = new JPanel();
    JLabel messageLabel = new JLabel("Zapytanie:");
    JLabel consoleLabel = new JLabel("Konsola:");
    JTextField messageField = new JTextField(20);
    JTextArea console = new JTextArea(15,20);
    JButton clear = new JButton("Wyczysc konsole");
    JScrollPane scroll = new JScrollPane(console, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    PhoneBookClient(String name, String hostname) {
        super(name);
        this.name = name;
        this.hostname = hostname;
        setSize(300, 380);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        console.setEditable(false);

        panel.add(messageLabel);
        panel.add(messageField);
        panel.add(consoleLabel);
        panel.add(scroll);
        panel.add(clear);

        messageField.addActionListener(this);
        clear.addActionListener(this);

        setContentPane(panel);
        setVisible(true);

        new Thread(this).start();
    }

    synchronized public void printSent(String message) {
        String textBefore = console.getText();
        console.setText("[" + this.name + " -> Server]: " + message + '\n' + textBefore);
    }

    synchronized public void printReceived(String message) {
        String textBefore = console.getText();
        console.setText("[Server -> " + this.name + "]: " + message + '\n' + textBefore);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String message;
        Object source = actionEvent.getSource();
        if (source == messageField) {
            try {
                message = messageField.getText();
                oos.writeObject(message);
                printSent(message);
                messageField.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (source == clear) {
            console.setText("");
        }
    }

    @Override
    public void run() {
        if (hostname.equals("")) {
            hostname = "localhost";
        }
        try {
            this.socket = new Socket(hostname, SERVER_PORT);
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(name);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Polaczenie przerwane.");
            setVisible(false);
            e.printStackTrace();
            dispose();
            return;
        }
        try {
            while(true) {
                String message = (String)ois.readObject();
                printReceived(message);
                if (message.equals("CLOSE")) {
                    ois.close();
                    oos.close();
                    socket.close();
                    setVisible(false);
                    dispose();
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Polaczenie przerwane.");
            e.printStackTrace();
            dispose();
        }
    }

    public static void main(String [] args) {
        String name, hostname;

        hostname = JOptionPane.showInputDialog("Podaj adres serwera: ");
        name = JOptionPane.showInputDialog("Podaj swoje imie: ");

        if (name != null && !name.equals("")) {
            new PhoneBookClient(name, hostname);
        }
    }
}
