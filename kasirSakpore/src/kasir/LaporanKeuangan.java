/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;

public class LaporanKeuangan extends javax.swing.JPanel {

   
    public LaporanKeuangan() {
        initComponents();
        loadDataKeuangan();
        loadDataSemua();
        hitungTotalBulanan();
        loadDataBulanTahun();
        setFilterDefault();
        setListenerTable();
    }
    
    private void setListenerTable(){
        tblSemua.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting()) {
        int row = tblSemua.getSelectedRow();
        if (row != -1) {
            Object tglObj = tblSemua.getValueAt(row, 1); // kolom tanggal, sesuaikan indeks
            if (tglObj != null) {
                java.sql.Date tgl = java.sql.Date.valueOf(tglObj.toString());
        loadDetailKeuanganPerTanggal(tgl);
            }
        }
    }
});
    }
    private void setFilterDefault() {
    Calendar cal = Calendar.getInstance();

    cal.set(Calendar.DAY_OF_MONTH, 1);
    jdcStart.setDate(cal.getTime());

    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    jdcEnd.setDate(cal.getTime());
}
    private void hitungTotalBulanan() {
    try (Connection conn = koneksi.dbKonek()) {
        LocalDate sekarang = LocalDate.now();
        int bulan = sekarang.getMonthValue();
        int tahun = sekarang.getYear();

        String sql = """
            SELECT 
                COALESCE(SUM(masuk), 0) AS total_masuk,
                COALESCE(SUM(keluar), 0) AS total_keluar
            FROM keuangan
            WHERE EXTRACT(MONTH FROM tanggal) = ? 
              AND EXTRACT(YEAR FROM tanggal) = ?
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, bulan);
        ps.setInt(2, tahun);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) { 
            double totalMasuk = rs.getDouble("total_masuk");
            double totalKeluar = rs.getDouble("total_keluar");
            double totalAkhir = totalMasuk - totalKeluar;

            // format angka jadi rupiah
            lblTotalBulan.setText(String.format("Rp %, .0f", totalAkhir));
        } else {
            lblTotalBulan.setText("Rp 0");
        }

        rs.close();
        ps.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal menghitung total bulanan: " + e.getMessage());
    }
}



    private void loadDataBulanTahun() {
    int bulan = jmcBulan.getMonth() + 1; 
    int tahun = jycTahun.getYear();

    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };

    model.addColumn("No");
    model.addColumn("Tanggal");
    model.addColumn("Masuk");
    model.addColumn("Keluar");
    model.addColumn("Total Akhir");

    double totalMasukBulan = 0;
    double totalKeluarBulan = 0;

    try (Connection conn = koneksi.dbKonek()) {
        String sql = """
            SELECT 
                tanggal::date AS tgl,
                SUM(masuk) AS total_masuk,
                SUM(keluar) AS total_keluar
            FROM keuangan
            WHERE EXTRACT(MONTH FROM tanggal) = ?
              AND EXTRACT(YEAR FROM tanggal) = ?
            GROUP BY tanggal::date
            ORDER BY tanggal::date ASC
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, bulan);
        ps.setInt(2, tahun);
        ResultSet rs = ps.executeQuery();

        int no = 1;
        while (rs.next()) {
            double masuk = rs.getDouble("total_masuk");
            double keluar = rs.getDouble("total_keluar");
            double total = masuk - keluar;

            totalMasukBulan += masuk;
            totalKeluarBulan += keluar;

            model.addRow(new Object[]{
                no++,
                rs.getDate("tgl"),
                masuk,
                keluar,
                total
            });
        }

        tblSemua.setModel(model);

        // set total bulan ke label
          double totalAkhir = totalMasukBulan - totalKeluarBulan;

        // Format bulan dan tahun biar enak dibaca
        String namaBulan = java.time.Month.of(bulan).name().substring(0, 1)
                + java.time.Month.of(bulan).name().substring(1).toLowerCase();

        // Set label total dan keuntungan
        lblTotalBulan.setText(String.format("Total Bulan %s %d: Rp %, .0f", namaBulan, tahun, totalAkhir));


        rs.close();
        ps.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
    }
}

int selectedId = -1; // untuk simpan idkeuangan yang dipilih

private java.util.List<Integer> idList = new ArrayList<>(); // buat simpan id

private void loadDataKeuangan() {
    java.util.Date start = java.sql.Date.valueOf(
        java.time.LocalDate.now().withDayOfMonth(1) // awal bulan
    );
    java.util.Date end = java.sql.Date.valueOf(
        java.time.LocalDate.now().withDayOfMonth(
            java.time.LocalDate.now().lengthOfMonth() // akhir bulan
        )
    );
    loadDataKeuangan(start, end);
}
private void loadDataKeuangan(java.util.Date startDate, java.util.Date endDate) {
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    model.addColumn("No");
    model.addColumn("Jenis Keuangan");
    model.addColumn("Masuk");
    model.addColumn("Keluar");
    model.addColumn("Tanggal");

    try (Connection conn = koneksi.dbKonek()) {
        String sql = "SELECT idkeuangan, jeniskeuangan, masuk, keluar, tanggal "
                   + "FROM keuangan "
                   + "WHERE tanggal::date BETWEEN ? AND ? "
                   + "ORDER BY tanggal DESC";
        PreparedStatement pst = conn.prepareStatement(sql);

        // konversi java.util.Date -> java.sql.Date
        pst.setDate(1, new java.sql.Date(startDate.getTime()));
        pst.setDate(2, new java.sql.Date(endDate.getTime()));

        ResultSet rs = pst.executeQuery();

        idList.clear();
        int no = 1;

        while (rs.next()) {
            idList.add(rs.getInt("idkeuangan"));
            model.addRow(new Object[]{
                no++,
                rs.getString("jeniskeuangan"),
                rs.getDouble("masuk"),
                rs.getDouble("keluar"),
                rs.getDate("tanggal")
            });
        }

        tblKeuangan.setModel(model);

        tblKeuangan.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblKeuangan.getColumnModel().getColumn(0).setMaxWidth(40);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
    }
}
private void tampilDetailPenjualan(int idKeuangan) {
    try (Connection conn = koneksi.dbKonek()) {
        String sql = """
            SELECT k.idasal, k.jeniskeuangan, t.notransaksi, t.namapengguna, t.tgl_transaksi,
                   t.subtotal, t.diskon, t.grand_total, t.metodepembayaran
            FROM keuangan k
            JOIN transaksi t ON k.idasal = t.idtransaksi
            WHERE k.idkeuangan = ? AND k.jeniskeuangan = 'Penjualan Barang'
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idKeuangan);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int idTransaksi = rs.getInt("idasal");
            String info = """
                No Transaksi : %s
                Kasir        : %s
                Tanggal      : %s
                Subtotal     : %.2f
                Diskon       : %.2f
                Grand Total  : %.2f
                Metode Bayar : %s
            """.formatted(
                rs.getString("notransaksi"),
                rs.getString("namapengguna"),
                rs.getTimestamp("tgl_transaksi"),
                rs.getDouble("subtotal"),
                rs.getDouble("diskon"),
                rs.getDouble("grand_total"),
                rs.getString("metodepembayaran")
            );

            String sqlDetail = """
                SELECT namabarang, jumlah, harga, subtotal, keterangan
                FROM detailtransaksi
                WHERE idtransaksi = ?
            """;
            PreparedStatement ps2 = conn.prepareStatement(sqlDetail);
            ps2.setInt(1, idTransaksi);
            ResultSet rs2 = ps2.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append("\n\n=== Detail Barang ===\n");
            while (rs2.next()) {
               sb.append(String.format("%s (%d x %.0f) = %.0f %s\n",
                rs2.getString("namabarang"),
                rs2.getInt("jumlah"),
                rs2.getDouble("harga"),
                rs2.getDouble("subtotal"),
                rs2.getString("keterangan") == null ? "" : "(" + rs2.getString("keterangan") + ")"
            ));

            }

            JOptionPane.showMessageDialog(this, info + sb.toString(), 
                "Detail Penjualan", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Data bukan dari transaksi penjualan atau tidak ditemukan.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menampilkan detail: " + e.getMessage());
    }
}
private java.util.List<java.sql.Date> tanggalList = new ArrayList<>();

private void loadDataSemua() {
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    model.addColumn("No");
    model.addColumn("Tanggal");
    model.addColumn("Masuk");
    model.addColumn("Keluar");
    model.addColumn("Total Akhir");

    try (Connection conn = koneksi.dbKonek()) {
        String sql = """
            SELECT 
                tanggal::date AS tgl,
                SUM(masuk) AS total_masuk,
                SUM(keluar) AS total_keluar,
                SUM(masuk - keluar) AS total_akhir
            FROM keuangan
            WHERE EXTRACT(MONTH FROM tanggal) = EXTRACT(MONTH FROM CURRENT_DATE)
              AND EXTRACT(YEAR FROM tanggal) = EXTRACT(YEAR FROM CURRENT_DATE)
            GROUP BY tanggal::date
            ORDER BY tanggal::date;
        """;

        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        tanggalList.clear();
        int no = 1;

        while (rs.next()) {
            java.sql.Date tgl = rs.getDate("tgl");
            tanggalList.add(tgl);

            model.addRow(new Object[]{
                no++,
                tgl,
                rs.getDouble("total_masuk"),
                rs.getDouble("total_keluar"),
                rs.getDouble("total_akhir")
            });
        }

        tblSemua.setModel(model);
        tblSemua.getColumnModel().getColumn(0).setMaxWidth(40);
        tblSemua.getColumnModel().getColumn(0).setPreferredWidth(40);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load data rekap: " + e.getMessage());
    }
}

// Tambahkan listener setelah tabel selesai diinisialisasi

private void loadDetailKeuanganPerTanggal(java.sql.Date tanggal) {
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    model.addColumn("No");
    model.addColumn("Tanggal");
    model.addColumn("Jenis Keuangan");
    model.addColumn("Masuk");
    model.addColumn("Keluar");

    try (Connection conn = koneksi.dbKonek()) {
        String sql = """
            SELECT tanggal, jeniskeuangan, masuk, keluar
            FROM keuangan
            WHERE tanggal::date = ?
            ORDER BY idkeuangan;
        """;
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setDate(1, tanggal);

        ResultSet rs = pst.executeQuery();

        int no = 1;
        while (rs.next()) {
            model.addRow(new Object[]{
                no++,
                rs.getDate("tanggal"),
                rs.getString("jeniskeuangan"),
                rs.getDouble("masuk"),
                rs.getDouble("keluar")
            });
        }

        tblDetails.setModel(model);
        tblDetails.getColumnModel().getColumn(0).setMaxWidth(40);
        tblDetails.getColumnModel().getColumn(0).setPreferredWidth(40);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load detail: " + e.getMessage());
    }
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelUtama = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        btnFIlter = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKeuangan = new javax.swing.JTable();
        btnCetak1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDetails = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSemua = new javax.swing.JTable();
        jmcBulan = new com.toedter.calendar.JMonthChooser();
        jycTahun = new com.toedter.calendar.JYearChooser();
        jLabel3 = new javax.swing.JLabel();
        lblSetKeuntungan = new javax.swing.JLabel();
        lblTotalBulan = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        panelUtama.setBackground(new java.awt.Color(255, 255, 255));
        panelUtama.setPreferredSize(new java.awt.Dimension(1740, 960));
        panelUtama.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Laporan Harian");
        panelUtama.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, -1, -1));

        jdcEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcEndPropertyChange(evt);
            }
        });
        panelUtama.add(jdcEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 140, 40));
        panelUtama.add(jdcStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 150, 40));

        jLabel2.setText("Sampai");
        panelUtama.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, -1, -1));

        btnFIlter.setBackground(new java.awt.Color(102, 255, 255));
        btnFIlter.setText("Filter");
        btnFIlter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFIlterActionPerformed(evt);
            }
        });
        panelUtama.add(btnFIlter, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, 130, 40));

        tblKeuangan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal", "Jenis Keuangan", "Masuk", "Keluar", "Total"
            }
        ));
        tblKeuangan.setRowHeight(30);
        tblKeuangan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblKeuanganKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblKeuangan);
        if (tblKeuangan.getColumnModel().getColumnCount() > 0) {
            tblKeuangan.getColumnModel().getColumn(2).setHeaderValue("Jenis Keuangan");
        }

        panelUtama.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 1630, 370));

        btnCetak1.setBackground(new java.awt.Color(0, 102, 102));
        btnCetak1.setText("Cetak");
        btnCetak1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetak1ActionPerformed(evt);
            }
        });
        panelUtama.add(btnCetak1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, 130, 40));

        tblDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Tanggal", "Jenis Keuangan", "Masuk", "Keluar"
            }
        ));
        tblDetails.setRowHeight(30);
        jScrollPane2.setViewportView(tblDetails);

        panelUtama.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 570, 620, 280));

        tblSemua.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal", "Masuk", "Keluar", "Total Akhir"
            }
        ));
        tblSemua.setRowHeight(30);
        tblSemua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblSemuaKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblSemua);

        panelUtama.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 570, 980, 280));

        jmcBulan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jmcBulanPropertyChange(evt);
            }
        });
        panelUtama.add(jmcBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 530, 130, 30));

        jycTahun.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jycTahunPropertyChange(evt);
            }
        });
        panelUtama.add(jycTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 530, 130, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel3.setText("Laporan Keuangan");
        panelUtama.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        lblSetKeuntungan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSetKeuntungan.setText("Total Keuntungan:");
        panelUtama.add(lblSetKeuntungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 530, 360, -1));

        lblTotalBulan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalBulan.setText("lblTotalBulan");
        panelUtama.add(lblTotalBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 540, 360, -1));

        add(panelUtama, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jdcEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcEndPropertyChange
 if ("date".equals(evt.getPropertyName())) {
        java.util.Date start = jdcStart.getDate();
        java.util.Date end = jdcEnd.getDate();

        if (start != null && end != null) {
            loadDataKeuangan(start, end);
        }
    }        // TODO add your handling code here:
    }//GEN-LAST:event_jdcEndPropertyChange

    private void btnFIlterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFIlterActionPerformed
loadDataKeuangan();  
setFilterDefault();// TODO add your handling code here:
    }//GEN-LAST:event_btnFIlterActionPerformed

    private void btnCetak1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetak1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCetak1ActionPerformed

    private void tblKeuanganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblKeuanganKeyPressed
  if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        int row = tblKeuangan.getSelectedRow();
        if (row == -1) return;

        int idKeuangan = idList.get(row);
        tampilDetailPenjualan(idKeuangan);

        // biar Enter gak bikin "bunyi ding" atau pindah sel
        evt.consume();
    }        // TODO add your handling code here:
    }//GEN-LAST:event_tblKeuanganKeyPressed

    private void tblSemuaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSemuaKeyPressed
 if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        int row = tblSemua.getSelectedRow();
        if (row == -1) return;
        java.sql.Date tgl = tanggalList.get(row);
        loadDetailKeuanganPerTanggal(tgl);
        evt.consume();
    }        // TODO add your handling code here:
    }//GEN-LAST:event_tblSemuaKeyPressed

    private void jmcBulanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jmcBulanPropertyChange
loadDataBulanTahun();// TODO add your handling code here:
    }//GEN-LAST:event_jmcBulanPropertyChange

    private void jycTahunPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jycTahunPropertyChange
loadDataBulanTahun();// TODO add your handling code here:
    }//GEN-LAST:event_jycTahunPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak1;
    private javax.swing.JButton btnFIlter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private com.toedter.calendar.JMonthChooser jmcBulan;
    private com.toedter.calendar.JYearChooser jycTahun;
    private javax.swing.JLabel lblSetKeuntungan;
    private javax.swing.JLabel lblTotalBulan;
    private javax.swing.JPanel panelUtama;
    private javax.swing.JTable tblDetails;
    private javax.swing.JTable tblKeuangan;
    private javax.swing.JTable tblSemua;
    // End of variables declaration//GEN-END:variables
}
