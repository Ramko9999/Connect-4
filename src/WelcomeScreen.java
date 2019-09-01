import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JFrame {
    private JPanel Main;
    private JLabel header;
    private JButton Login;
    private JButton Register;

    public WelcomeScreen(){

        super("WelcomeScreen");
        this.setVisible(true);
        this.add(Main);
        this.setSize(600,600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Login.setForeground(Color.YELLOW);Login.setBackground(Color.BLACK);Login.setBorderPainted(false);Login.setOpaque(true);
        Register.setForeground(Color.YELLOW); Register.setBackground(Color.BLACK);Register.setBorderPainted(false);Register.setOpaque(true);
        listen_for_action();

    }

    public void listen_for_action(){
        Login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    super.finalize();
                    new LoginScreen();
                }
                catch(Throwable t){
                    System.out.println(t.getCause());
                }
            }
        });
        Register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    super.finalize();
                    new RegisterScreen();
                }
                catch(Throwable t){
                    System.out.println(t.getCause());
                }
            }
        });
    }

    public static void main(String[] args) {
        new WelcomeScreen();
    }

}
