import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Main {

    public static void main(String[] args) {
        // Membuat instance LoginPage
        LoginPage loginPage = new LoginPage();

        // Menambahkan listener untuk menangani login sukses
        loginPage.setListener(new LoginPage.ListenerSuksesLogin() {
            @Override
            public void onLoginSuccess() {
                // Ketika login berhasil, tampilkan Tampilan
                Tampilan tampilan = new Tampilan();
                tampilan.createUIComponents();
                JFrame frame = new JFrame("Program Catatan Keuangan Sederhana");
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(900, 450);
                frame.getAccessibleContext();
                frame.setContentPane(new Tampilan().Panel);
                frame.setVisible(true);
                frame.setResizable(true);
            }
        });
    }
}
