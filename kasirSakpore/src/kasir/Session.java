/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kasir;

/**
 *
 * @author yaniyan
 */    
import java.util.Date;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class Session {
    private static String username;
    private static String role;

    public static void setUsername(String user) {
        username = user;
    }

    public static String getUsername() {
        return username;
    }

    public static void setRole(String r) {
        role = r;
    }

    public static String getRole() {
        return role;
    }

    public static void clear() {
        username = null;
        role = null;
    }


    public static void updateSaldo(JTable table, JLabel lblMasuk, JLabel lblKeluar, JLabel lblSaldo) {
        double totalMasuk = 0.0;
        double totalKeluar = 0.0;

        try {
            TableModel model = table.getModel();

            // Cek posisi kolom (pastikan sesuai urutan di tabel kamu)
            int colMasuk = 2;   // kolom ke-3 = "Masuk"
            int colKeluar = 3;  // kolom ke-4 = "Keluar"

            for (int i = 0; i < model.getRowCount(); i++) {
                Object valMasuk = model.getValueAt(i, colMasuk);
                Object valKeluar = model.getValueAt(i, colKeluar);

                if (valMasuk != null) totalMasuk += Double.parseDouble(valMasuk.toString());
                if (valKeluar != null) totalKeluar += Double.parseDouble(valKeluar.toString());
            }

            double saldo = totalMasuk - totalKeluar;

            // Format jadi string angka (bisa juga rupiah)
            lblMasuk.setText(String.format("Total pemasukan "+"%.2f", totalMasuk));
            lblKeluar.setText(String.format("Total pengeluaran "+"%.2f", totalKeluar));
            lblSaldo.setText(String.format("Keuntungan "+"%.2f", saldo));

        } catch (Exception e) {
            lblMasuk.setText("0.00");
            lblKeluar.setText("0.00");
            lblSaldo.setText("0.00");
            System.err.println("Gagal menghitung saldo: " + e.getMessage());
        }
    }
    public static double hitungTotalTransaksi(Date startDate, Date endDate) {
        double total = 0.0;

        try (Connection conn = koneksi.dbKonek()) {

            String sql = "SELECT SUM(grand_total) AS total_pemasukan "
                       + "FROM transaksi "
                       + "WHERE tgl_transaksi >= ? AND tgl_transaksi < ?";

            PreparedStatement pst = conn.prepareStatement(sql);

            // konversi java.util.Date ke LocalDate
            LocalDate startLocal = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocal = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            pst.setTimestamp(1, Timestamp.valueOf(startLocal.atStartOfDay()));
            pst.setTimestamp(2, Timestamp.valueOf(endLocal.plusDays(1).atStartOfDay()));

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total_pemasukan");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
    
    public static double hitungPengeluaran(Date tglMulai, Date tglSelesai) {
        double total = 0.0;

        try (Connection conn = koneksi.dbKonek()) {

            String sql = "SELECT SUM(totalharga) AS totalharga "
                       + "FROM barangmasuk "
                       + "WHERE tanggal >= ? AND tanggal < ?";

            PreparedStatement pst = conn.prepareStatement(sql);

            // konversi java.util.Date ke LocalDate
            LocalDate startLocal = tglMulai.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocal = tglSelesai.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            pst.setTimestamp(1, Timestamp.valueOf(startLocal.atStartOfDay()));
            pst.setTimestamp(2, Timestamp.valueOf(endLocal.plusDays(1).atStartOfDay()));

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("totalharga");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }



}
