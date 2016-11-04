/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverprogram;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import packets.Packet;
import packets.Packet1Connect;
import packets.Packet1_5ConnectConfirm;
import packets.Packet2ClientConnected;
import packets.Packet3ClientDisconnect;
import packets.Packet4Chat;
import packets.Packet5ListUsers;
import packets.Packet6Error;
import packets.Packet7ServerMessage;

/**
 *
 * @author jonathan.guerne
 */
public class Main_console extends JFrame {

    private static ModelDynamique modele = new ModelDynamique();
    private JTable tableau;
    private static Server server;
    //http://baptiste-wicht.developpez.com/tutoriels/java/swing/jtable/

    static int tcp = 23900, udp = 23901;

    public static HashMap<Integer, Connection> clients = new HashMap<Integer, Connection>();
    public static HashMap<Integer, String> clients_name = new HashMap<Integer, String>();

    private static ArrayList<Integer> banIDS = new ArrayList<>();
    private static ArrayList<String> banIPS = new ArrayList<>();

    private static JTextField msgServerField = new JTextField("",20);
    public Main_console() {
        super();

        setTitle("JTable basique dans un JPanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableau = new JTable(modele);

        getContentPane().add(new JScrollPane(tableau), BorderLayout.CENTER);

        JPanel boutons = new JPanel();
        boutons.add(new JButton(new BanidAction()));
        boutons.add(new JButton(new BanipAction()));
        boutons.add(new JButton(new SendButton()));
        boutons.add(msgServerField);
        
        getContentPane().add(boutons, BorderLayout.SOUTH);
        
        try {
            getContentPane().add(new JLabel(InetAddress.getLocalHost().getHostAddress()),BorderLayout.NORTH);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Main_console.class.getName()).log(Level.SEVERE, null, ex);
        }

        pack();
    }

    public static void main(String[] args) throws IOException {
        
        
        if(!(args.length>=1 && ( "0".equals(args[0])||"false".equals(args[0]))))
            new Main_console().setVisible(true);

        server = new Server();
        server.start();
        server.bind(tcp, udp);

        server.addListener(new ServerListener());

        server.getKryo().register(Packet.class);
        server.getKryo().register(Packet1Connect.class);
        server.getKryo().register(Packet1_5ConnectConfirm.class);
        server.getKryo().register(Packet2ClientConnected.class);
        server.getKryo().register(Packet3ClientDisconnect.class);
        server.getKryo().register(Packet4Chat.class);
        server.getKryo().register(Packet5ListUsers.class);
        server.getKryo().register(Packet6Error.class);
        server.getKryo().register(Packet7ServerMessage.class);
        server.getKryo().register(java.util.ArrayList.class);
    }

    private class SendButton extends AbstractAction {

        public SendButton() {
            super("Send");
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            Packet7ServerMessage p7 = new Packet7ServerMessage();
            p7.msg = msgServerField.getText();
            msgServerField.setText("");
            System.out.println(p7.msg);
            server.sendToAllTCP(p7);
        }

    }

    private class BanidAction extends AbstractAction {

        public BanidAction() {
            super("Ban");
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            int[] selection = tableau.getSelectedRows();
            System.out.println(selection.toString());
            for (int i = selection.length - 1; i >= 0; i--) {
                int tempBan = -1;
                for (int j = 0; j < banIDS.size(); j++) {
                    if (banIDS.get(j) == (int) modele.getValueAt(selection[i], 0)) {
                        modele.setValueAt(false, selection[i], 3);
                        tempBan = j;
                    }
                }
                if (tempBan != -1) {
                    banIDS.remove(tempBan);
                } else {
                    banIDS.add((int) modele.getValueAt(selection[i], 0));
                    modele.setValueAt(true, selection[i], 3);
                }
            }
        }
    }

    private class BanipAction extends AbstractAction {

        public BanipAction() {
            super("BanIP");
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            int[] selection = tableau.getSelectedRows();
            for (int i = selection.length - 1; i >= 0; i--) {
                int tempBan = -1;
                for (int j = 0; j < banIPS.size(); j++) {
                    if (banIPS.get(j).equals((String) modele.getValueAt(selection[i], 2))) {
                        modele.setValueAt(false, selection[i], 4);
                        tempBan = j;
                    }
                }
                if (tempBan != -1) {
                    banIPS.remove(tempBan);
                } else {
                    banIPS.add((String) modele.getValueAt(selection[i], 2));
                    modele.setValueAt(true, selection[i], 4);
                }
            }
        }
    }

    private static class ServerListener extends Listener {

        @Override
        public void received(Connection connection, Object object) {
            if (object instanceof Packet) {
                Packet p = (Packet) object;
                if (p.maj >= ConstNET.majMinClient) {
                    if (object instanceof Packet1Connect) {
                        Packet1Connect p1 = (Packet1Connect) object;
                        clients.put(connection.getID(), connection);
                        clients_name.put(connection.getID(), p1.username);
                        modele.addUser(new Users(p1.username,
                                connection.getID(),
                                connection.getRemoteAddressTCP().getAddress().getHostAddress(),
                                false,
                                false));

                        Packet1_5ConnectConfirm p1_5 = new Packet1_5ConnectConfirm();
                        p1_5.ID = connection.getID();

                        server.sendToTCP(connection.getID(), p1_5);

                        Packet2ClientConnected p2 = new Packet2ClientConnected();
                        p2.clientName = p1.username;
                        server.sendToAllExceptTCP(connection.getID(), p2);
                        Packet5ListUsers p5 = new Packet5ListUsers();
                        for (Map.Entry<Integer, Connection> entry : clients.entrySet()) {
                            int key = entry.getKey();
                            p5.users.add(clients_name.get(key));
                        }

                        server.sendToAllTCP(p5);
                    } else if (object instanceof Packet3ClientDisconnect) {
                        Packet3ClientDisconnect p3 = (Packet3ClientDisconnect) object;
                        clients.remove(p3.ClientName);

                        server.sendToAllExceptTCP(clients.get(p3.ClientName).getID(), p3);
                    } else if (object instanceof Packet4Chat) {
                        Packet4Chat p4 = (Packet4Chat) object;
                        if (!banIDS.contains(p4.ID) && !banIPS.contains(connection.getRemoteAddressTCP().getAddress().getHostAddress())) {
                            server.sendToAllTCP(p4);
                            if(p4.message.contains("bug")){
                                Packet7ServerMessage p7 = new Packet7ServerMessage();
                                p7.msg=("https://github.com/JonathanGuerne/Chat_GIT");
                                server.sendToAllTCP(p7);
                            }
                        }
                    }
                } else {
                    Packet6Error p6 = new Packet6Error();
                    p6.erreorMessage = "Mauvaise mise à jour";
                    server.sendToTCP(connection.getID(), p6);
                }
            }
        }

        @Override
        public void disconnected(Connection connection) {
            Packet3ClientDisconnect p3 = new Packet3ClientDisconnect();
            Iterator it = clients.entrySet().iterator();
            int user_id = -1;
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                if (pairs.getValue().equals(connection)) {
                    user_id = (int) pairs.getKey();
                    break;
                }
            }
            if (user_id != -1) {

                System.out.println("ID : " + connection.getID());

                p3.ClientName = clients_name.get(user_id);
                clients.remove(user_id);
                clients_name.remove(user_id);
                for (int i = 0; i < modele.getRowCount(); i++) {
                    if (user_id == (int) modele.getValueAt(i, 0)) {
                        modele.removeUser(i);
                    }
                }

                server.sendToAllExceptTCP(connection.getID(), p3);
                Packet5ListUsers p5 = new Packet5ListUsers();
                for (Map.Entry<Integer, Connection> entry : clients.entrySet()) {
                    int key = entry.getKey();
                    p5.users.add(clients_name.get(key));
                }
                server.sendToAllTCP(p5);
            }
        }
    }
}
