import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoginScreen extends JFrame{
    private JTextField username;
    private JPasswordField password;
    private JButton login;
    private JButton register;
    private JPanel Auth;
    private JPanel Main;
    private JLabel Header;
    private Registrar r = new Registrar();
    private Document userFile;

    public LoginScreen(){
        super("LoginScreen");
        this.setVisible(true);
        this.add(Main);
        this.setSize(600,600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        login.setForeground(Color.YELLOW);login.setOpaque(true);login.setBorderPainted(false); login.setBackground(Color.BLACK);
        register.setForeground(Color.YELLOW);register.setOpaque(true);register.setBorderPainted(false);register.setBackground(Color.BLACK);
        listen_for_login();
        username.setBackground(Color.BLACK);password.setBackground(Color.BLACK);
    }

    public static void main(String[] args) {
        new LoginScreen();
    }

    public void listen_for_login() {
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //checks whether the document for the credentials is there or not
                String uName = username.getText();
                char[] pWord = password.getPassword();
                Document checker = new Document("USERNAME", uName);
                checker.append("PASSWORD", unWrap(pWord));
                Document d = (Document) r.collection.find(checker).first();
                if (d == null) { //conditions checks whether the document is there or not
                    Header.setText("TRY AGAIN");
                } else {
                    username.setForeground(Color.GREEN); password.setForeground(Color.GREEN);
                    d.replace("LATEST LOGIN", new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
                    r.collection.replaceOne(checker, d);
                    userFile = d;
                    System.out.println((int) userFile.get("Recent Color"));
                    new MenuScreen(userFile);
                }
            }
        });
        register.addActionListener(new ActionListener() {
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
    public String unWrap(char[] c) { //used to unWrap the char [] array password
        String s = "";
        for (char character : c) {
            s += character;
        }
        return s;
    }

}
