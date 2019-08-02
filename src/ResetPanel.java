import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResetPanel {
    public JPanel Main;
    private JButton reset;
    private JButton resize;

    public ResetPanel(){
        reset.setBackground(Color.BLACK);reset.setOpaque(true);reset.setBorderPainted(false);
        resize.setBackground(Color.BLACK);resize.setOpaque(true);resize.setBorderPainted(false);
        Main.setBackground(Color.BLACK);
    }
    public void listen_for_action(Game g){
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.reset();
            }
        });
        resize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            g.setSize(612, 618);
            }
        });

    }


}
