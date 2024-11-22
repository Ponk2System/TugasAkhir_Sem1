import javax.swing.*;
import javax.swing.table.DefaultTableModel; // pengelolaan tabel
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack; // penyimpanan data sementara untuk melakukan Undo dan Redo

public class Tampilan {
    private JTextField TXKETERANGAN;
    private JComboBox CBBULAN;
    private JTextField TXJUMLAH;
    private JTextField TXTANGGAL;
    private JButton HAPUSButton;
    private JButton SIMPANButton;
    private JButton REDOButton;
    private JButton UNDOButton;
    private JComboBox CBJENIS;
    private JButton SALDOButton;
    public JTable table1;
    private JButton REFRESHButton;
    private JScrollPane scrollPane;
    private JTextField TXTAHUN;

    public DefaultTableModel tableModel;
    public Stack<String[]> UNDOStack = new Stack<>();
    public Stack<String[]> REDOStack = new Stack<>();
    public Stack<String[]> dataStack = new Stack<>(); // setiap data yang di-input, akan masuk ke dalam 1 stack yang berupa 1 array string.

    JPanel Panel;

    public Tampilan() {
        SIMPANButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mengambil data yang di-inputkan
                String jenis = (String) CBJENIS.getSelectedItem();
                String keterangan = TXKETERANGAN.getText();
                String bulan = (String) CBBULAN.getSelectedItem();
                String tahun = TXTAHUN.getText();
                String jumlah = TXJUMLAH.getText();
                String tanggal = TXTANGGAL.getText();

                // Cek data sudah terisi
                if (keterangan.isEmpty() || jumlah.isEmpty() || tanggal.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Isi semua data terlebih dahulu", "PERINGATAN!!!", JOptionPane.WARNING_MESSAGE);
                    return;
                }


                // Validasi tanggal dan tahun
                try {
                    int tanggalan = Integer.parseInt(TXTANGGAL.getText()); // Konversi ke integer
                    int cekTahun = Integer.parseInt(tahun);

                    if (!Function_Formula.validasiTanggal(tanggalan, bulan, cekTahun)) {
                        JOptionPane.showMessageDialog(null, "Tanggal tidak valid!", "KESALAHAN TANGGAL", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "TANGGAL harus berupa ANGKA!", "KESALAHAN TANGGAL", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Validasi pada TXJUMLAH harus angka (bukan huruf) yang lebih dari 0
                try {
                    int value = Integer.parseInt(TXJUMLAH.getText()); // Konversi ke integer
                    if (value <= 0) {
                        JOptionPane.showMessageDialog(null, "Tuliskan NOMINAL yang BENAR!", "KESALAHAN JUMLAH", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                catch (NumberFormatException ex) { // Jika jumlah bukan angka
                    JOptionPane.showMessageDialog(null, "NOMINAL harus berupa ANGKA!", "KESALAHAN JUMLAH", JOptionPane.WARNING_MESSAGE);
                    return;
                }


                // Masukkan data dalam 1 baris pada tabel
                String[] row = {jenis, keterangan, tanggal, bulan, tahun, jumlah};
                tableModel.insertRow(0, row);

                // Masukkan juga data ke dalam stack agar bisa dilakukan Undo dan Redo
                String[] data = {jenis, keterangan, tanggal, bulan, tahun, jumlah};
                dataStack.push(data);

                // Me-refresh/mengosongkan TextField input setelah klik icon save
                TXKETERANGAN.setText("");
                TXJUMLAH.setText("");
                TXTANGGAL.setText("");
                TXTAHUN.setText("");

                UNDOStack.push(row); // deklarasi agar bisa menyimpan data ke dalam stack yang tersimpan di dalam 1 row/baris agar bisa dilakukan Undo
                REDOStack.clear(); // memastikan agar tidak ada lagi perubahan setelah melakukan redo yang terakhir
            }
        });
        REFRESHButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //membersihkan data yang sedang di-inputkan dalam TextField
                TXKETERANGAN.setText("");
                TXJUMLAH.setText("");
                TXTANGGAL.setText("");
                TXTAHUN.setText("");
            }
        });
        HAPUSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    // Hapus baris yang dipilih
                    String[] lastAction = UNDOStack.pop();
                    UNDOStack.push(lastAction);
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Pilih baris yang ingin dihapus", "PERINGATAN!!!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        SALDOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menghitung saldo menggunakan metode dari RumusKabisat
                int saldo = Function_Formula.hitungSaldo(tableModel);

                // Menampilkan saldo menggunakan metode dari RumusKabisat
                Function_Formula.tampilkanSaldo(saldo);
            }
        });

        UNDOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // Menghapus data yang baru saja ditambahkan
                if (!UNDOStack.isEmpty()) { // Cek apakah UNDOStack kosong
                    // Mengeluarkan data dan menyimpannya di lastAction
                    String[] lastAction = UNDOStack.pop();

                    REDOStack.push(lastAction); // Juga memasukkan data ke dalam REDOStack

                    // Menghapus baris terakhir yang ditambahkan
                    int rowCount = tableModel.getRowCount();
                    if (rowCount > 0) {
                        tableModel.removeRow(rowCount - 1);
                    }
                }
                else if (!REDOStack.isEmpty()) {
                    // Ambil data yang di-redo dan tambahkan ke tabel
                    String[] lastAction = REDOStack.pop();
                    tableModel.addRow(lastAction);  // Tambahkan baris yang di-redo

                    // Simpan untuk undo
                    UNDOStack.push(lastAction);
                }
            }
        });
        REDOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!REDOStack.isEmpty()) {
                    // Ambil data yang di-redo dan tambahkan ke tabel
                    String[] lastAction = REDOStack.pop();
                    tableModel.addRow(lastAction);  // Tambahkan baris yang di-redo

                    // Simpan untuk undo
                    UNDOStack.push(lastAction);
                }
            }
        });
    }

    void createUIComponents() {
        // TODO: place custom component creation code here
        // Membuat model tabel dengan kolom-kolom yang sesuai
        tableModel = new DefaultTableModel(new Object[]{"Jenis", "Keterangan", "Tanggal" , "Bulan", "Tahun", "Jumlah"}, 0);

        // deklarasi untuk membuat JTable yang sesuai model
        table1 = new JTable(tableModel);

        // deklarasi untuk membuat urutan pada tabel
        TableRowSorter myTableRowSorter = new TableRowSorter(tableModel);
        table1.setRowSorter(myTableRowSorter);

        // Memasukkan JTable ke dalam JScrollPane agar bisa di-scroll
        JScrollPane scrollPane = new JScrollPane(table1);
    }
}