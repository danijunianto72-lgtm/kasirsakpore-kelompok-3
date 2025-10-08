/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// setelah loadSupplier dipanggil

/**
 *
 * @author LABFO-14
 */
public class Pembelian extends javax.swing.JPanel {

    /**
     * Creates new form Pembelian
     */
    public Pembelian() {
        initComponents();
        loadSupplier();
        loadTransaksi();
        setupSkuScanner();
        element();
                

jdcTanggal.setDate(new Date());
    }
private void loadSupplier() {
    try (Connection conn = koneksi.dbKonek()) {
        String sql = "SELECT namasupplier FROM supplier";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        cmbSuplier.removeAllItems();
        while (rs.next()) {
            cmbSuplier.addItem(rs.getString("namasupplier"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void setBarangTerpilih(String kode, String nama, String satuan, String harga, String sku) {
    txtKodeBarang.setText(kode);
        txtSku.setText(sku);

    txtNamaBarang.setText(nama);
    txtSatuan.setText(satuan);
    txtHarga.setText(harga);
}
// deklarasi timer di class
private javax.swing.Timer timer;

// di constructor atau init form, buat timer
private void setupSkuScanner() {
    timer = new javax.swing.Timer(500, e -> {
        String sku = txtSku.getText().trim();
        if (!sku.isEmpty()) {
            cariBarangBySku(sku);
        } else {
            clearFields();
        }
    });
    timer.setRepeats(false); // hanya jalan sekali tiap ketik
}

                                

// fungsi query ke database
private void cariBarangBySku(String sku) {
    try (Connection conn = koneksi.dbKonek()) {
        String sql = "SELECT kodebarang, skubarang, nama, hargabarang, satuan " +
                     "FROM barang WHERE skubarang = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, sku);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            txtKodeBarang.setText(String.valueOf(rs.getInt("kodebarang")));
            txtNamaBarang.setText(rs.getString("nama"));
            txtHarga.setText(rs.getString("hargabarang"));
            txtSatuan.setText(rs.getString("satuan"));
        } else {
            clearFields();
            JOptionPane.showMessageDialog(this, "Barang tidak ditemukan!");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

// fungsi clear kalau tidak ketemu
private void clearFields() {
    txtKodeBarang.setText("");
    txtNamaBarang.setText("");
    txtHarga.setText("");
    txtSatuan.setText("");
    txtSku.setText("");
}

private void loadTransaksi() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID");
    model.addColumn("Kode Barang");
    model.addColumn("Nama");
    model.addColumn("Satuan");
    model.addColumn("Jumlah");
    model.addColumn("Harga");
    model.addColumn("Total");
    model.addColumn("Tanggal");
    model.addColumn("Supplier");
    model.addColumn("Sku Barang");

    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

    try {
        conn = koneksi.dbKonek(); // koneksi ke PostgreSQL
        String sql = "SELECT idbarangmasuk, kodebarang, nama, satuan, jumlahmasuk, hargabarang, totalharga, tanggal, supplier,skubarang FROM barangmasuk ORDER BY idbarangmasuk DESC";
        st = conn.createStatement();
        rs = st.executeQuery(sql);

        while (rs.next()) {
            Object[] row = {
                rs.getInt("idbarangmasuk"),
                rs.getInt("kodebarang"),
                rs.getString("nama"),
                rs.getString("satuan"),
                rs.getInt("jumlahmasuk"),
                rs.getBigDecimal("hargabarang"),
                rs.getBigDecimal("totalharga"),
                rs.getDate("tanggal"),
                rs.getString("supplier"),
                rs.getString("skubarang")
            };
            model.addRow(row);
        }

        tblTransaksi.setModel(model);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal load transaksi: " + e.getMessage());
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (st != null) st.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}
private void simpanTransaksi() {
    Connection conn = null;
    PreparedStatement pst1 = null;
    PreparedStatement pst2 = null;
    PreparedStatement pst3 = null; // tambahan untuk update stok
    ResultSet rs = null;

    try {
        conn = koneksi.dbKonek(); // koneksi ke PostgreSQL

        // 1. Insert ke tabel barangmasuk + RETURNING id
        String sql1 = "INSERT INTO barangmasuk (kodebarang, nama, satuan, jumlahmasuk, hargabarang, totalharga, tanggal, supplier, skubarang) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?) RETURNING idbarangmasuk";
        pst1 = conn.prepareStatement(sql1);

        pst1.setInt(1, Integer.parseInt(txtKodeBarang.getText()));
        pst1.setString(2, txtNamaBarang.getText());
        pst1.setString(3, txtSatuan.getText());
        int jumlahMasuk = Integer.parseInt(txtJumlah.getText());
        pst1.setInt(4, jumlahMasuk);
        pst1.setBigDecimal(5, new java.math.BigDecimal(txtHarga.getText()));
        pst1.setBigDecimal(6, new java.math.BigDecimal(txtTotal.getText()));
        pst1.setDate(7, new java.sql.Date(jdcTanggal.getDate().getTime()));
        pst1.setString(8, cmbSuplier.getSelectedItem().toString());
        pst1.setString(9, txtSku.getText());

        rs = pst1.executeQuery();
        int idBarangMasuk = 0;
        if (rs.next()) {
            idBarangMasuk = rs.getInt(1);
        }

        // 2. Update stok di tabel barang
        String sql3 = "UPDATE barang SET stok = stok + ? WHERE kodebarang = ?";
        pst3 = conn.prepareStatement(sql3);
        pst3.setInt(1, jumlahMasuk);
        pst3.setInt(2, Integer.parseInt(txtKodeBarang.getText()));
        pst3.executeUpdate();

        // 3. Insert ke tabel keuangan
        String sql2 = "INSERT INTO keuangan (idasal, jeniskeuangan, masuk, keluar, tanggal) "
                + "VALUES (?, ?, ?, ?, ?)";
        pst2 = conn.prepareStatement(sql2);

        pst2.setInt(1, idBarangMasuk);
        pst2.setString(2, "Pembelian - " + txtNamaBarang.getText());
        pst2.setBigDecimal(3, new java.math.BigDecimal("0")); // masuk = 0
        pst2.setBigDecimal(4, new java.math.BigDecimal(txtTotal.getText())); // keluar = total
        pst2.setDate(5, new java.sql.Date(jdcTanggal.getDate().getTime()));

        pst2.executeUpdate();

        JOptionPane.showMessageDialog(this, "Pembelian sukses, stok barang sudah diperbarui!");
        loadTransaksi();
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (pst1 != null) pst1.close(); } catch (Exception e) {}
        try { if (pst2 != null) pst2.close(); } catch (Exception e) {}
        try { if (pst3 != null) pst3.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
} 

private void element(){
    List<Component> tabOrder = Arrays.asList(
    txtSku,
    cmbSuplier,
    txtJumlah,
    jdcStart,
    jdcEnd
);

setFocusTraversalPolicy(new CustomFocusTraversalPolicy(tabOrder));
setFocusCycleRoot(true);

}
public class CustomFocusTraversalPolicy extends FocusTraversalPolicy {
    private final List<Component> order;

    public CustomFocusTraversalPolicy(List<Component> order) {
        this.order = new ArrayList<>(order);
    }

    @Override
    public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
        int idx = (order.indexOf(aComponent) + 1) % order.size();
        return order.get(idx);
    }

    @Override
    public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
        int idx = order.indexOf(aComponent) - 1;
        if (idx < 0) idx = order.size() - 1;
        return order.get(idx);
    }

    @Override
    public Component getFirstComponent(Container focusCycleRoot) {
        return order.get(0);
    }

    @Override
    public Component getLastComponent(Container focusCycleRoot) {
        return order.get(order.size() - 1);
    }

    @Override
    public Component getDefaultComponent(Container focusCycleRoot) {
        return order.get(0);
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
        jPanel1 = new javax.swing.JPanel();
        txtKodeBarang = new javax.swing.JTextField();
        txtSku = new javax.swing.JTextField();
        txtHarga = new javax.swing.JTextField();
        txtSatuan = new javax.swing.JTextField();
        txtNamaBarang = new javax.swing.JTextField();
        cmbSuplier = new javax.swing.JComboBox<>();
        btnPilih = new javax.swing.JButton();
        jdcTanggal = new com.toedter.calendar.JDateChooser();
        txtJumlah = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        btnBeli1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTransaksi = new javax.swing.JTable();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        btnRefresh = new javax.swing.JButton();

        panelUtama.setBackground(new java.awt.Color(255, 255, 255));
        panelUtama.setPreferredSize(new java.awt.Dimension(1740, 960));
        panelUtama.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(txtKodeBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, 80, 50));

        txtSku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSkuKeyReleased(evt);
            }
        });
        jPanel1.add(txtSku, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 130, 50));
        jPanel1.add(txtHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 210, 260, 50));
        jPanel1.add(txtSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 90, 260, 50));
        jPanel1.add(txtNamaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 90, 260, 50));

        cmbSuplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Suplier Pilih", " " }));
        jPanel1.add(cmbSuplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 210, 260, 50));

        btnPilih.setBackground(new java.awt.Color(102, 102, 255));
        btnPilih.setText("Pilih");
        btnPilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihActionPerformed(evt);
            }
        });
        jPanel1.add(btnPilih, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, 80, 50));
        jPanel1.add(jdcTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 290, 50));

        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJumlahKeyReleased(evt);
            }
        });
        jPanel1.add(txtJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 90, 250, 50));
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 210, 250, 50));

        btnBeli1.setBackground(new java.awt.Color(51, 255, 51));
        btnBeli1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnBeli1.setText("Beli");
        btnBeli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBeli1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnBeli1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 300, 370, 70));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Jumlah");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 60, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Nama Barang");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 60, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Suplier");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 180, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Total Harga");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 180, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("SkuBarang");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Satuan");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 60, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Harga");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 180, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Tanggal");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Kode Barang");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, -1, -1));

        panelUtama.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 1590, 400));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal", "Kode Barang", "Nama Barang", "Suplier", "Satuan", "Harga", "Jumlah", "Total Harga", "Stok"
            }
        ));
        jScrollPane1.setViewportView(tblTransaksi);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Sampai");

        btnRefresh.setBackground(new java.awt.Color(204, 204, 204));
        btnRefresh.setText("Refresh");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jdcStart, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addComponent(jdcEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(924, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1540, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdcStart, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdcEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        panelUtama.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 1580, 390));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelUtama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelUtama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnPilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihActionPerformed
 FormBarang dialog = new FormBarang(null, true, this); 
    dialog.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_btnPilihActionPerformed

    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
 try {
            int jumlah = Integer.parseInt(txtJumlah.getText().trim());
            double harga = Double.parseDouble(txtHarga.getText().trim());
            txtTotal.setText(String.valueOf(jumlah * harga));
        } catch (Exception ex) {
            txtTotal.setText("");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtJumlahKeyReleased

    private void btnBeli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBeli1ActionPerformed
 String kode = txtKodeBarang.getText();
    String nama = txtNamaBarang.getText();
    String satuan = txtSatuan.getText();
    String jumlah = txtJumlah.getText();
    String harga = txtHarga.getText();
    String total = txtTotal.getText();
    String supplier = cmbSuplier.getSelectedItem().toString();
    java.util.Date tgl = jdcTanggal.getDate();
    String sku = txtSku.getText();
    // bikin string konfirmasi
    String pesan = "Yakin ingin membeli barang ini?\n\n"
            + "Kode: " + kode + "\n"
            + "Kode Sku: " + sku + "\n"
            + "Nama: " + nama + "\n"
            + "Jumlah: " + jumlah + " " + satuan + "\n"
            + "Harga: " + harga + "\n"
            + "Total: " + total + "\n"
            + "Supplier: " + supplier + "\n"
            + "Tanggal: " + tgl;

    int konfirmasi = JOptionPane.showConfirmDialog(this, pesan, 
            "Konfirmasi Pembelian", JOptionPane.OK_CANCEL_OPTION);

    if (konfirmasi == JOptionPane.OK_OPTION) {
        simpanTransaksi(); // panggil fungsi insert DB
    }        // TODO add your handling code here:
    }//GEN-LAST:event_btnBeli1ActionPerformed

    private void txtSkuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSkuKeyReleased
 if (timer.isRunning()) {
        timer.restart();
    } else {
        timer.start();
    }        // TODO add your handling code here:
    }//GEN-LAST:event_txtSkuKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBeli1;
    private javax.swing.JButton btnPilih;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cmbSuplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private com.toedter.calendar.JDateChooser jdcTanggal;
    private javax.swing.JPanel panelUtama;
    private javax.swing.JTable tblTransaksi;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKodeBarang;
    private javax.swing.JTextField txtNamaBarang;
    private javax.swing.JTextField txtSatuan;
    private javax.swing.JTextField txtSku;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
