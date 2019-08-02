import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuScreen extends JFrame{
    private JPanel Main;
    private JButton local;
    private JButton global;
    private JButton file_storage;
    private JLabel Header;
    private Document userFile;

    public MenuScreen(Document user){
        super("MenuScreen");
        this.setVisible(true);
        this.add(Main);
        this.setSize(600,600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        userFile = user;
        local.setForeground(Color.YELLOW);local.setBackground(Color.BLACK);local.setBorderPainted(false);local.setOpaque(true);
        global.setForeground(Color.YELLOW);global.setBackground(Color.BLACK);global.setBorderPainted(false);global.setOpaque(true);
        if(userFile != null){
            file_storage.setForeground(Color.YELLOW);file_storage.setBackground(Color.BLACK);file_storage.setBorderPainted(false);file_storage.setOpaque(true);

        }
        else{
            file_storage.setBackground(Color.BLACK); file_storage.setEnabled(false); file_storage.setBorderPainted(false);
            file_storage.setOpaque(true);
        }
        listen_for_action();
    }

    public void listen_for_action(){

        local.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Game(userFile, true).listenForDrops();
            }
        });
        file_storage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OldGame(userFile).listenForDrops();
            }
        });
        global.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GlobalScreen();
            }
        });
    }
    public static void main(String[] args) {
        new MenuScreen(null);
    }

}
