import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage {
    private JTextField TXUSER;
    private JPanel panel1;
    private JPasswordField PSPASS;
    private JButton LOGINButton;
    private JFrame frame;
    private ListenerSuksesLogin listener;

    public LoginPage (){
        this.listener = listener;
        frame = new JFrame ("PROGRAM CATATAN KEUANGAN SEDERHANA");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(430,250));
        frame.setResizable(false);

        frame.add(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = TXUSER.getText();
                String password = new String(PSPASS.getPassword());

                String validUser = "admin";
                String validPassword = "12345";

                if (user.equals(validUser) && password.equals(validPassword)) {
                    JOptionPane.showMessageDialog(null, "Login Berhasil!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose(); // Tutup frame login
                    listener.onLoginSuccess(); // Panggil listener untuk membuka Tampilan
                } else if (user.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Isi semua data terlebih dahulu", "PERINGATAN!!!", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Username atau Password salah!", "PERINGATAN!!!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // Setter untuk listener
    public void setListener(ListenerSuksesLogin listener) {
        this.listener = listener;
    }

    //Interface untuk callback
    public interface ListenerSuksesLogin {
        void onLoginSuccess();
    }
}
