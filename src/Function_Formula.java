import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Function_Formula {
    public static boolean TahunKabisat(int tahun) {
        return (tahun % 4 == 0 && tahun % 100 != 0) || (tahun % 400 == 0);
    }

    public static boolean validasiTanggal(int tanggal, String bulan, int tahun) {
        if (tanggal <= 0) {
            return false;
        }

        switch (bulan) {
            case "Februari":
                if (TahunKabisat(tahun)) {
                    return tanggal <= 29; // Tahun kabisat, Februari sampai 29 hari
                } else {
                    return tanggal <= 28; // Bukan tahun kabisat, Februari sampai 28 hari
                }
            case "April":
            case "Juni":
            case "September":
            case "November":
                return tanggal <= 30; // Bulan dengan maksimal 30 hari
            default:
                return tanggal <= 31; // Bulan lainnya dengan maksimal 31 hari
        }
    }

    // Menghitung saldo berdasarkan data di table
    public static int hitungSaldo(DefaultTableModel tableModel) {
        int saldo = 0;

        // Loop untuk menghitung saldo berdasarkan jenis dan jumlah
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String jenis = (String) tableModel.getValueAt(i, 0);
            int jumlah = Integer.parseInt((String) tableModel.getValueAt(i, 5));

            if ("Pemasukan".equals(jenis)) {
                saldo += jumlah; // Tambahkan jumlah jika jenisnya Pemasukan
            } else if ("Pengeluaran".equals(jenis)) {
                saldo -= jumlah; // Kurangi jumlah jika jenisnya Pengeluaran
            }
        }

        return saldo; // Kembalikan saldo akhir
    }

    // Menampilkan pesan saldo berdasarkan kondisi saldo
    public static void tampilkanSaldo(int saldo) {
        if (saldo < 0) {
            JOptionPane.showMessageDialog(null, "Kamu ngutang: Rp " + saldo + "\n" +
                    "SEGERA LUNASI UTANGMU BAGAIMANAPUN CARANYA!", "RUGI!!!", JOptionPane.WARNING_MESSAGE);
        } else if (saldo > 0 && saldo < 200000) {
            JOptionPane.showMessageDialog(null, "Saldo kamu tinggal: Rp " + saldo + "\n" +
                    "JANGAN BOROS-BOROS, BELI YANG MURAH-MURAH AJA!", "Saldo", JOptionPane.WARNING_MESSAGE);
        } else if (saldo >= 200000) {
            JOptionPane.showMessageDialog(null, "Saldo kamu sekarang: Rp " + saldo, "Saldo", JOptionPane.INFORMATION_MESSAGE);
        } else if (saldo == 0) {
            JOptionPane.showMessageDialog(null, "Kok BOKEK?", "Kenapa?", JOptionPane.QUESTION_MESSAGE);
        }
    }
}
