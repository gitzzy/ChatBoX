
import javax.swing.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MainX {

    private static JTextField tf;
    private static JTextArea ta;
    private static BufferedWriter write1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frm = new JFrame();
        frm.setSize(500, 600);
        frm.setLayout(null);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel ChatBox = new JPanel();
        ChatBox.setBounds(0, 0, 500, 600);
        ChatBox.setLayout(null);
        frm.add(ChatBox);

        tf = new JTextField();
        tf.setBounds(0, 500, 400, 50);
        ChatBox.add(tf);

        ta = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(ta);
        scrollPane.setBounds(0, 50, 500, 450);
        ChatBox.add(scrollPane);
        ta.setEditable(false);

        JLabel lb = new JLabel("Server's Side");
        lb.setBounds(0, 0, 500, 50);
        ChatBox.add(lb);
        lb.setHorizontalAlignment(JLabel.CENTER);

        JButton btn = new JButton("Send -->");
        btn.setBounds(400, 500, 100, 50);
        ChatBox.add(btn);

        btn.addActionListener(e -> handleSendMessage());
        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleSendMessage();
                }
            }
        });

        frm.setVisible(true);

        new Thread(MainX::setupServer).start();
    }

    private static void setupServer() {
        try (ServerSocket serverSocket = new ServerSocket(1235);
             Socket socket = serverSocket.accept();
             Scanner scanner = new Scanner(System.in);
             InputStream inputStream = socket.getInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(reader);
             OutputStream outputStream = socket.getOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream))
        
        
        {
        	ta.append("Connection established with Client");
            write1 = new BufferedWriter(writer);

            while (true) {
                String clientResponse = bufferedReader.readLine();
                System.out.println("Client: " + clientResponse);
                ta.append("\nThey: " + clientResponse);

                if ("EXIT".equals(clientResponse)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleSendMessage() {
        ta.append("\nYou : " + tf.getText());
        try {
            write1.write(tf.getText() + "\n");
            write1.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        tf.setText("");
    }
}
