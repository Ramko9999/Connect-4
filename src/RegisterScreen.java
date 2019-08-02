import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterScreen extends JFrame{
    private JLabel Header;
    private JTextField first_name;
    private JTextField last_name;
    private JTextField username;
    private JPasswordField password;
    private JPanel Main;
    private JButton register;
    private JButton login;
    private Registrar r = new Registrar();
    private Document userFile;

    public RegisterScreen(){
        super("RegisterScreen");
        this.setVisible(true);
        this.add(Main);
        this.setSize(600,600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        first_name.setBackground(Color.BLACK); last_name.setBackground(Color.BLACK); username.setBackground(Color.BLACK); password.setBackground(Color.BLACK);
        login.setForeground(Color.YELLOW);login.setOpaque(true);login.setBorderPainted(false); login.setBackground(Color.BLACK);
        register.setForeground(Color.YELLOW);register.setOpaque(true);register.setBorderPainted(false);register.setBackground(Color.BLACK);
        super.add(Main);
        listen_for_registration();

    }

    public String unWrap(char[] c) { //used to unWrap the char [] array password
        String s = "";
        for (char character : c) {
            s += character;
        }
        return s;
    }

    public void listen_for_registration(){
        register.addActionListener(new ActionListener() { //checks whether the credentials that are entered are valid for a new account opening
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = first_name.getText(); //registration information that will be passed to make a new document
                String lastName = last_name.getText();
                String uName = username.getText();
                char[] pWord = password.getPassword();

                Document doc = new Document("First Name", firstName); //creating a document and checking whether that document even exists
                doc.append("Last Name", lastName);doc.append("USERNAME", uName);doc.append("PASSWORD", unWrap(pWord));
                Document check = (Document) r.collection.find(doc).first();
                if (check == null) {
                    doc.append("LATEST LOGIN", new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
                    doc.append("Recent Color", -1);
                    doc.append("Recent Game", "0000000-0000000-0000000-0000000-0000000-0000000-"); //this is basically the most recent game the person has played
                    r.updateRegistrar(doc);
                    first_name.setForeground(Color.GREEN); last_name.setForeground(Color.GREEN); username.setForeground(Color.GREEN); password.setForeground(Color.GREEN);
                    userFile = doc;
                    new MenuScreen(userFile);
                } else {
                    Header.setText("TRY AGAIN");
                    first_name.setForeground(Color.RED); last_name.setForeground(Color.RED); username.setForeground(Color.RED); password.setForeground(Color.RED);
                }


            }
        });
        login.addActionListener(new ActionListener() {
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
    }

    public static void main(String[] args) {
        new RegisterScreen();
    }


}
