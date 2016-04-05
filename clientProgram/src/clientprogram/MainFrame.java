package clientprogram;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import packets.Packet;
import packets.Packet1Connect;
import packets.Packet1_5ConnectConfirm;
import packets.Packet2ClientConnected;
import packets.Packet3ClientDisconnect;
import packets.Packet4Chat;
import packets.Packet5ListUsers;
import packets.Packet6Error;

/**
 * @author Jonathan
 */
public class MainFrame extends javax.swing.JFrame {

    int tcp = 23900, udp = 23901;
    final Client client;
    final private int majClient = 2;
    int ID;
    String username;
    DefaultStyledDocument doc;
    StyleContext sc;
    constantes cons;

    public MainFrame() {

        client = new Client();
        client.start();

        LoginDialog dial;
        do {
            dial = new LoginDialog(null, true);
            dial.setVisible(true);
        } while (dial.userName == null);
        
        username = dial.userName;

        try {
            client.connect(1000,dial.adressIP, tcp, udp);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de se connecter au serveur.");
            System.exit(0);
        }

        initComponents();

        sc = new StyleContext();
        doc = new DefaultStyledDocument(sc);
        content.setDocument(doc);

        // Create and add the main document style
        cons = new constantes(sc);

        doc.setLogicalStyle(0, cons.getMainText());

        client.getKryo().register(Packet.class);
        client.getKryo().register(Packet1Connect.class);
        client.getKryo().register(Packet1_5ConnectConfirm.class);
        client.getKryo().register(Packet2ClientConnected.class);
        client.getKryo().register(Packet3ClientDisconnect.class);
        client.getKryo().register(Packet4Chat.class);
        client.getKryo().register(Packet5ListUsers.class);
        client.getKryo().register(java.util.ArrayList.class);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Packet) {
                    if (object instanceof Packet1_5ConnectConfirm) {
                        Packet1_5ConnectConfirm p5 = (Packet1_5ConnectConfirm) object;
                        ID = p5.ID;
                        System.err.println(ID + "\n");
                    }
                    if (object instanceof Packet2ClientConnected) {
                        Packet2ClientConnected p2 = (Packet2ClientConnected) object;

                        try {
                            doc.setLogicalStyle(doc.getLength(), cons.getCo());
                            doc.insertString(doc.getLength(), p2.clientName + " est maintenant connecté(e)\n", null);
                            doc.setLogicalStyle(doc.getLength(), cons.getMainText());
                            content.select(doc.getLength(), doc.getLength());

                        } catch (BadLocationException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else if (object instanceof Packet3ClientDisconnect) {
                        Packet3ClientDisconnect p3 = (Packet3ClientDisconnect) object;

                        try {
                            doc.setLogicalStyle(doc.getLength(), cons.getDeco());
                            doc.insertString(doc.getLength(), p3.ClientName + " est maintenant hors ligne\n", null);
                            doc.setLogicalStyle(doc.getLength(), cons.getMainText());
                            content.select(doc.getLength(), doc.getLength());
                        } catch (BadLocationException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else if (object instanceof Packet4Chat) {
                        Packet4Chat p4 = (Packet4Chat) object;
                        try {
                            String value;
                            if (ID == p4.ID) {
                                doc.setLogicalStyle(doc.getLength(), cons.getPerso());
                                value = "";
                            } else {
                                value = p4.username + " : ";
                            }
                            doc.insertString(doc.getLength(), value + "" + p4.message + "\n", null);
                            doc.setLogicalStyle(doc.getLength(), cons.getMainText());
                            content.select(doc.getLength(), doc.getLength());

                        } catch (BadLocationException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else if (object instanceof Packet5ListUsers) {
                        Packet5ListUsers p5 = (Packet5ListUsers) object;
                        if (jTextArea2 != null) {
                            jTextArea2.setText("");
                        }
                        for (String usr : p5.users) {
                            if (jTextArea2 != null) {
                                jTextArea2.append(usr + "\n");
                            }
                        }
                    } else if (object instanceof Packet6Error) {
                        Packet6Error p6 = (Packet6Error) object;
                        JOptionPane.showMessageDialog(null, p6.erreorMessage, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        nameLabel.setText(username);

        Packet1Connect p1 = new Packet1Connect();
        p1.username = username;
        p1.maj = this.majClient;
        client.sendTCP(p1);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textField = new javax.swing.JTextField();
        submitButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        content = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        textField.setColumns(5);
        textField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldKeyReleased(evt);
            }
        });

        submitButton.setText("Envoyer");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("Created by Jonathan Guerne ");

        jTextArea2.setEditable(false);
        jTextArea2.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jLabel2.setText("Connecté(e)s :");

        nameLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        nameLabel.setText("Marcel");

        jScrollPane1.setAutoscrolls(true);

        content.setEditable(false);
        jScrollPane1.setViewportView(content);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(398, 398, 398))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textField)
                                .addGap(18, 18, 18)
                                .addComponent(submitButton))
                            .addComponent(jScrollPane1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(submitButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        if (!"".equals(textField.getText())) {
            Packet4Chat p4 = new Packet4Chat();
            p4.username = username;
            p4.message = textField.getText();
            p4.ID = this.ID;
            p4.maj = this.majClient;
            client.sendTCP(p4);
            textField.setText("");
        }
    }//GEN-LAST:event_submitButtonActionPerformed

    private void textFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldKeyReleased
        if (evt.getKeyCode() == 10) {
            submitButtonActionPerformed(null);
        }
    }//GEN-LAST:event_textFieldKeyReleased

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane content;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton submitButton;
    private javax.swing.JTextField textField;
    // End of variables declaration//GEN-END:variables

}
