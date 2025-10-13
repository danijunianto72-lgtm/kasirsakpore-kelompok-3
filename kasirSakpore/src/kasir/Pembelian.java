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
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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
        header();
        loadSupplier();
        loadTransaksi();
        loadSupplier1();
        setupSkuScanner();
        element();
        
     btnRefresh.getActionMap().put("ctrlR", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnRefresh.doClick(); // Menjalankan aksi tombol
    }
});

        btnRefresh.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK),
    "ctrlR"
);   
        
     btnPilih.getActionMap().put("ctrlP", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnPilih.doClick(); // Menjalankan aksi tombol
    }
});

        btnPilih.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK),
    "ctrlP"
);   
        
        
     btnBeli1.getActionMap().put("ctrlB", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnBeli1.doClick(); // Menjalankan aksi tombol
    }
});

        btnBeli1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK),
    "ctrlB"
); 
        
        btnBeli1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK),
    "submitAction"
);

btnBeli1.getActionMap().put("submitAction", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnBeli1.doClick();
    }
});
   
                
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
    cmbSupplier.addActionListener(evt -> {
        filterData();
    });

    // tombol refresh
    btnRefresh.addActionListener(evt -> {
        cmbSupplier.setSelectedIndex(0);
        loadTransaksi();
        setFilterDefault();
    });
        
       
jdcTanggal.setDate(new Date());
    }
    
    private void header(){
     JTableHeader header = tblPembelian.getTableHeader();
header.setOpaque(false); // Matikan transparansi bawaan
header.setPreferredSize(new Dimension(header.getWidth(), 40)); // 30 = tinggi header (px)

header.setBackground(new java.awt.Color(5,69,162)); // Warna #2c3e50
header.setForeground(Color.WHITE); // Warna font putih
header.setFont(new Font("Segoe UI",Font.BOLD, 14)); // Font tebal

// Nonaktifkan UI bawaan Nimbus supaya warna tidak di-override
header.setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
    JLabel label = new JLabel(value.toString());
    label.setOpaque(true);
    label.setBackground(new java.awt.Color(5,69,162));
    label.setForeground(Color.WHITE);
    label.setFont(new Font("Segoe UI", Font.BOLD, 15));
    label.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    label.setHorizontalAlignment(SwingConstants.LEFT);
    return label;
});
    }
    
    
        private void setFilterDefault() {
    Calendar cal = Calendar.getInstance();

    cal.set(Calendar.DAY_OF_MONTH, 1);
    jdcStart.setDate(cal.getTime());

    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    jdcEnd.setDate(cal.getTime());
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
        String sql = "SELECT kodebarang, skubarang, nama, hargapokok, satuan " +
                     "FROM barang WHERE skubarang = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, sku);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            txtKodeBarang.setText(String.valueOf(rs.getInt("kodebarang")));
            txtNamaBarang.setText(rs.getString("nama"));
            txtHarga.setText(rs.getString("hargapokok"));
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
  
  private void filterData() {
    java.util.Date tglMulai = jdcStart.getDate();
    java.util.Date tglSelesai = jdcEnd.getDate();
    String supplierDipilih = (String) cmbSupplier.getSelectedItem();

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
    private void loadTransaksi() {
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
   
    private void loadSupplier1() {
    cmbSupplier.removeAllItems();
    cmbSupplier.addItem("Semua Supplier"); // default untuk tampil semua data

    try (Connection conn = kasir.koneksi.dbKonek()) {
        String sql = "SELECT namasupplier FROM supplier ORDER BY namasupplier ASC";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            cmbSupplier.addItem(rs.getString("namasupplier"));
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error load supplier: " + ex.getMessage());
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
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPembelian = new javax.swing.JTable();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        btnRefresh = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cmbSupplier = new javax.swing.JComboBox<>();

        panelUtama.setBackground(new java.awt.Color(255, 255, 255));
        panelUtama.setPreferredSize(new java.awt.Dimension(1740, 960));
        panelUtama.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(txtKodeBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 80, 50));

        txtSku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSkuKeyReleased(evt);
            }
        });
        jPanel1.add(txtSku, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 130, 50));
        jPanel1.add(txtHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 220, 260, 50));
        jPanel1.add(txtSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 120, 260, 50));
        jPanel1.add(txtNamaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 120, 260, 50));

        cmbSuplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Suplier Pilih", " " }));
        jPanel1.add(cmbSuplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 220, 260, 50));

        btnPilih.setBackground(new java.awt.Color(102, 102, 255));
        btnPilih.setText("Pilih");
        btnPilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihActionPerformed(evt);
            }
        });
        jPanel1.add(btnPilih, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 220, 80, 50));
        jPanel1.add(jdcTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 290, 50));

        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJumlahKeyReleased(evt);
            }
        });
        jPanel1.add(txtJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 120, 250, 50));
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 220, 250, 50));

        btnBeli1.setBackground(new java.awt.Color(51, 255, 0));
        btnBeli1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnBeli1.setText("Beli");
        btnBeli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBeli1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnBeli1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 330, 320, 40));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Jumlah");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 90, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Nama Barang");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 90, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Suplier");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 190, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Total Harga");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 190, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("SkuBarang");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Satuan");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 90, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Harga");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 190, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Tanggal");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Kode Barang");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, -1, -1));

        jPanel3.setBackground(new java.awt.Color(5, 69, 162));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Pembelian Barang");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1590, 60));

        panelUtama.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 1590, 400));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblPembelian.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPembelian.setRowHeight(30);
        jScrollPane1.setViewportView(tblPembelian);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, 1540, 260));
        jPanel2.add(jdcStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 151, 40));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Sampai");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, -1, -1));
        jPanel2.add(jdcEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 143, 40));

        btnRefresh.setText("Refresh");
        jPanel2.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, 130, 40));

        jPanel4.setBackground(new java.awt.Color(5, 69, 162));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("DAFTAR PEMBELIAN");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1590, 60));

        cmbSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(cmbSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 80, -1, -1));

        panelUtama.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 1580, 410));

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
    private javax.swing.JComboBox<String> cmbSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private com.toedter.calendar.JDateChooser jdcTanggal;
    private javax.swing.JPanel panelUtama;
    private javax.swing.JTable tblPembelian;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKodeBarang;
    private javax.swing.JTextField txtNamaBarang;
    private javax.swing.JTextField txtSatuan;
    private javax.swing.JTextField txtSku;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
