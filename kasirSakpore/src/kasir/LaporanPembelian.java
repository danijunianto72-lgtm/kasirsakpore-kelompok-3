/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LAB FO-05
 */
public class LaporanPembelian extends javax.swing.JPanel {

    /**
     * Creates new form LaporanPembelian
     */
    public LaporanPembelian() {
        initComponents();
        tampilData();
        loadSupplier();
        setFilterDefault();
       
        
         // filter otomatis saat tanggal berubah
    jdcStart.addPropertyChangeListener(evt -> {
        if ("date".equals(evt.getPropertyName())) {
            filterData();
        }
    });
    jdcEnd.addPropertyChangeListener(evt -> {
        if ("date".equals(evt.getPropertyName())) {
            filterData();
        }
    });

    // event filter supplier
    cbfSupplier.addActionListener(evt -> {
        filterData();
    });

    // tombol refresh
    btnRefresh.addActionListener(evt -> {
        cbfSupplier.setSelectedIndex(0);
        tampilData();
        setFilterDefault();
    });
    }      
       

    private void tampilData() {
        DefaultTableModel model = (DefaultTableModel) tblPembelian.getModel();
        model.setRowCount(0); // hapus semua baris lama

        try (Connection conn = kasir.koneksi.dbKonek()) {
            String sql = "SELECT * FROM barangmasuk ORDER BY idbarangmasuk ASC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getDate("tanggal"),
                    rs.getInt("kodebarang"),
                    rs.getString("nama"),
                    rs.getString("supplier"),
                    rs.getString("satuan"),
                    rs.getInt("hargabarang"),
                    rs.getInt("jumlahmasuk"),
                    rs.getInt("totalharga")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error tampil data: " + ex.getMessage());
        }
    }
    
    private void setFilterDefault() {
    Calendar cal = Calendar.getInstance();

    cal.set(Calendar.DAY_OF_MONTH, 1);
    jdcStart.setDate(cal.getTime());

    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    jdcEnd.setDate(cal.getTime());
}
    
  private void filterData() {
    java.util.Date tglMulai = jdcStart.getDate();
    java.util.Date tglSelesai = jdcEnd.getDate();
    String supplierDipilih = (String) cbfSupplier.getSelectedItem();

    DefaultTableModel model = (DefaultTableModel) tblPembelian.getModel();
    model.setRowCount(0);

    StringBuilder sql = new StringBuilder("SELECT * FROM barangmasuk WHERE 1=1");

    // filter tanggal
    if (tglMulai != null && tglSelesai != null) {
        sql.append(" AND tanggal BETWEEN ? AND ?");
    }

    // filter supplier (kecuali “Semua Supplier”)
    if (supplierDipilih != null && !"Semua Supplier".equals(supplierDipilih)) {
        sql.append(" AND supplier = ?");
    }

    sql.append(" ORDER BY idbarangmasuk ASC");

    try (Connection conn = kasir.koneksi.dbKonek()) {
        PreparedStatement pst = conn.prepareStatement(sql.toString());

        int index = 1;

        // isi parameter tanggal
        if (tglMulai != null && tglSelesai != null) {
            pst.setDate(index++, new java.sql.Date(tglMulai.getTime()));
            pst.setDate(index++, new java.sql.Date(tglSelesai.getTime()));
        }

        // isi parameter supplier
        if (supplierDipilih != null && !"Semua Supplier".equals(supplierDipilih)) {
            pst.setString(index++, supplierDipilih);
        }

        ResultSet rs = pst.executeQuery();

        int no = 1;
        while (rs.next()) {
            model.addRow(new Object[]{
                no++,
                rs.getDate("tanggal"),
                rs.getInt("kodebarang"),
                rs.getString("nama"),
                rs.getString("supplier"),
                rs.getString("satuan"),
                rs.getInt("hargabarang"),
                rs.getInt("jumlahmasuk"),
                rs.getInt("totalharga")
            });
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error filter data: " + ex.getMessage());
    }
}

   
    private void loadSupplier() {
    cbfSupplier.removeAllItems();
    cbfSupplier.addItem("Semua Supplier"); // default untuk tampil semua data

    try (Connection conn = kasir.koneksi.dbKonek()) {
        String sql = "SELECT namasupplier FROM supplier ORDER BY namasupplier ASC";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            cbfSupplier.addItem(rs.getString("namasupplier"));
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error load supplier: " + ex.getMessage());
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPembelian = new javax.swing.JTable();
        btnCetak = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cbfSupplier = new javax.swing.JComboBox<>();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1740, 960));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Laporan Pembelian");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Mulai");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 140, -1, 40));
        jPanel1.add(jdcStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 140, 160, 40));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 2, 24)); // NOI18N
        jLabel3.setText(" /");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 20, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Selesai");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 140, -1, 40));

        btnRefresh.setBackground(new java.awt.Color(0, 153, 153));
        btnRefresh.setText("Refresh");
        jPanel1.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(1350, 140, 130, 40));

        jdcEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcEndPropertyChange(evt);
            }
        });
        jPanel1.add(jdcEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 140, 160, 40));

        tblPembelian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal", "Kode Barang", "Nama Barang", "Supplier", "Satuan", "Harga", "Jumlah", "Total Harga"
            }
        ));
        jScrollPane1.setViewportView(tblPembelian);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 1630, 570));

        btnCetak.setBackground(new java.awt.Color(0, 102, 102));
        btnCetak.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak.setText("Cetak");
        jPanel1.add(btnCetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(1530, 140, 140, 40));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Daftar Pembelian");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, -1, -1));

        cbfSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cbfSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 140, 140, 40));

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jdcEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcEndPropertyChange

    }//GEN-LAST:event_jdcEndPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cbfSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private javax.swing.JTable tblPembelian;
    // End of variables declaration//GEN-END:variables
}
