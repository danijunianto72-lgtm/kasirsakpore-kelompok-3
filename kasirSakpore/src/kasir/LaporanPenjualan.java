/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;
 
import javax.swing.JDialog;
import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;
public class LaporanPenjualan extends javax.swing.JPanel {

    /**
     * Creates new form LaporanPenjualan
     */
    public LaporanPenjualan() {
        initComponents();
        loadDataTransaksi();
    }
    private void loadDataTransaksi() {
    DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID", "No Transaksi", "Kasir", "Tanggal", "Grand Total", "Metode"}, 0
    );
    
    try (Connection conn = koneksi.dbKonek()) {
        String sql = "SELECT idtransaksi, notransaksi, namapengguna, tgl_transaksi, grand_total, metodepembayaran " +
                     "FROM transaksi ORDER BY tgl_transaksi DESC";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("idtransaksi"),
                rs.getString("notransaksi"),
                rs.getString("namapengguna"),
                rs.getTimestamp("tgl_transaksi"),
                rs.getBigDecimal("grand_total"),
                rs.getString("metodepembayaran")
            });
        }
        
        tblTransaksi.setModel(model);
        
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load transaksi: " + e.getMessage());
    }
}


    // ==== INNER CLASS: Popup Detail ====
    private class PopupDetailTransaksi extends JDialog {
        private int idTransaksi;

        // komponen UI
        private JLabel lblKode, lblKasir, lblTanggal, lblSubtotal, lblDiskon, lblGrandTotal, lblMetode;
        private JTable tblDetail;

        public PopupDetailTransaksi(Frame parent, boolean modal, int idTransaksi) {
            super(parent, modal);
            this.idTransaksi = idTransaksi;
            initUI();
            loadDetailTransaksi();
        }

        private void initUI() {
            setTitle("Detail Transaksi");
            setSize(500, 400);
            setLayout(new BorderLayout());

            JPanel header = new JPanel(new GridLayout(0, 2));
            lblKode = new JLabel();
            lblKasir = new JLabel();
            lblTanggal = new JLabel();
            lblSubtotal = new JLabel();
            lblDiskon = new JLabel();
            lblGrandTotal = new JLabel();
            lblMetode = new JLabel();

            header.add(new JLabel("Kode:")); header.add(lblKode);
            header.add(new JLabel("Kasir:")); header.add(lblKasir);
            header.add(new JLabel("Tanggal:")); header.add(lblTanggal);
            header.add(new JLabel("Subtotal:")); header.add(lblSubtotal);
            header.add(new JLabel("Diskon:")); header.add(lblDiskon);
            header.add(new JLabel("Grand Total:")); header.add(lblGrandTotal);
            header.add(new JLabel("Metode:")); header.add(lblMetode);

            add(header, BorderLayout.NORTH);

            tblDetail = new JTable();
            add(new JScrollPane(tblDetail), BorderLayout.CENTER);
        }

        private void loadDetailTransaksi() {
            try (Connection conn = koneksi.dbKonek()) {
                // header
                String sqlTrans = "SELECT * FROM transaksi WHERE idtransaksi = ?";
                PreparedStatement pst = conn.prepareStatement(sqlTrans);
                pst.setInt(1, idTransaksi);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    lblKode.setText(rs.getString("notransaksi"));
                    lblKasir.setText(rs.getString("namapengguna"));
                    lblTanggal.setText(rs.getString("tgl_transaksi"));
                    lblSubtotal.setText(rs.getBigDecimal("subtotal").toString());
                    lblDiskon.setText(rs.getBigDecimal("diskon").toString());
                    lblGrandTotal.setText(rs.getBigDecimal("grand_total").toString());
                    lblMetode.setText(rs.getString("metodepembayaran"));
                }

                // detail barang
                DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Nama Barang", "Jumlah", "Harga", "Subtotal"}, 0
                );
                String sqlDetail = "SELECT namabarang, jumlah, harga, subtotal FROM detailtransaksi WHERE idtransaksi = ?";
                pst = conn.prepareStatement(sqlDetail);
                pst.setInt(1, idTransaksi);
                rs = pst.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("namabarang"),
                        rs.getInt("jumlah"),
                        rs.getBigDecimal("harga"),
                        rs.getBigDecimal("subtotal")
                    });
                }
                tblDetail.setModel(model);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal load detail: " + e.getMessage());
            }
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

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnCetak = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTransaksi = new javax.swing.JTable();
        jdcMulai = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jdcSelesai = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        btnCetak1 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1740, 960));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("/");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, 10, 40));

        btnCetak.setBackground(new java.awt.Color(0, 102, 102));
        btnCetak.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak.setText("Cetak");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });
        jPanel1.add(btnCetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 140, 160, 50));

        tblTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "id", "nomer", "Kode Transaksi", "Tanggal", "Nama Kasir", "Total Akhir"
            }
        ));
        tblTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransaksiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblTransaksi);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 1620, 600));
        jPanel1.add(jdcMulai, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 150, 50));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Mulai");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, -1, 40));

        btnRefresh.setBackground(new java.awt.Color(0, 153, 153));
        btnRefresh.setText("Refresh");
        jPanel1.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 140, 140, 50));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("Daftar Transaksi");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setText("Laporan Transaksi");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));
        jPanel1.add(jdcSelesai, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 140, 150, 50));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Selesai");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, -1, 40));

        btnCetak1.setBackground(new java.awt.Color(0, 102, 102));
        btnCetak1.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak1.setText("Cetak");
        jPanel1.add(btnCetak1, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 140, 160, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransaksiMouseClicked
            // TODO add your handling code here:
    }//GEN-LAST:event_tblTransaksiMouseClicked

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
   int selectedRow = tblTransaksi.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi dulu!");
            return;
        }

        int idTransaksi = (int) tblTransaksi.getValueAt(selectedRow, 0);

        // panggil popup inner class
        PopupDetailTransaksi popup = new PopupDetailTransaksi(
            (java.awt.Frame) SwingUtilities.getWindowAncestor(this),
            true,
            idTransaksi
        );
        popup.setLocationRelativeTo(this);
        popup.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_btnCetakActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnCetak1;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser jdcMulai;
    private com.toedter.calendar.JDateChooser jdcSelesai;
    private javax.swing.JTable tblTransaksi;
    // End of variables declaration//GEN-END:variables
}
