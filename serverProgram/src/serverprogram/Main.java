package serverprogram;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javafx.scene.Parent;
import packets.Packet;
import packets.Packet1Connect;
import packets.Packet1_5ConnectConfirm;
import packets.Packet2ClientConnected;
import packets.Packet3ClientDisconnect;
import packets.Packet4Chat;
import packets.Packet5ListUsers;
import packets.Packet6Error;

/**
 * @author Jonathan Guerne
 */
public class Main {

    static int tcp = 23900, udp = 23901;

    public static HashMap<Integer, Connection> clients = new HashMap<Integer, Connection>();
    public static HashMap<Integer, String> clients_name = new HashMap<Integer, String>();

    public static void main(String args[]) throws IOException {
        Server server = new Server();
        server.start();
        server.bind(tcp, udp);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Packet) {
                    Packet p = (Packet) object;
                    if (p.maj >= ConstNET.majMinClient) {
                        if (object instanceof Packet1Connect) {
                            Packet1Connect p1 = (Packet1Connect) object;
                            clients.put(connection.getID(), connection);
                            clients_name.put(connection.getID(), p1.username);

                            Packet1_5ConnectConfirm p1_5 = new Packet1_5ConnectConfirm();
                            p1_5.ID = connection.getID();

                            server.sendToTCP(connection.getID(), p1_5);
                            
                            Packet2ClientConnected p2 = new Packet2ClientConnected();
                            p2.clientName = p1.username;
                            server.sendToAllExceptTCP(connection.getID(), p2);
                            Packet5ListUsers p5 = new Packet5ListUsers();
                            for (Entry<Integer, Connection> entry : clients.entrySet()) {
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
                            server.sendToAllTCP(p4);
                        }
                    } else {
//                        Packet6Error p6 = new Packet6Error();
//                        p6.erreorMessage = "Mauvaise mise à jour";
//                        server.sendToTCP(connection.getID(), p6);
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

                    server.sendToAllExceptTCP(connection.getID(), p3);
                    Packet5ListUsers p5 = new Packet5ListUsers();
                    for (Entry<Integer, Connection> entry : clients.entrySet()) {
                        int key = entry.getKey();
                        p5.users.add(clients_name.get(key));
                    }
                    server.sendToAllTCP(p5);
                }
            }
        });

        server.getKryo().register(Packet.class);
        server.getKryo().register(Packet1Connect.class);
        server.getKryo().register(Packet1_5ConnectConfirm.class);
        server.getKryo().register(Packet2ClientConnected.class);
        server.getKryo().register(Packet3ClientDisconnect.class);
        server.getKryo().register(Packet4Chat.class);
        server.getKryo().register(Packet5ListUsers.class);
        server.getKryo().register(java.util.ArrayList.class);
    }
}
