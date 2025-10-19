/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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
        header();
        tampilData();
        loadSupplier();
        loadDataBulan();
        setFilterDefault();
        java.util.Date tglMulai = jdcStart.getDate();
    java.util.Date tglSelesai = jdcEnd.getDate();
       double totalKeluar = Session.hitungPengeluaran(tglMulai, tglSelesai);
NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
lblKeluar.setText(nf.format(totalKeluar));
SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
String dari = sdf.format(tglMulai);
String sampai = sdf.format(tglSelesai);
lblKeluar.setText("Pengeluaran dari " + dari + " sampai " + sampai + " adalah: " + nf.format(totalKeluar));

        
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
    btnRefresh.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK),
    "ctrlR"
);
    
btnCetak.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK),
    "ctrlC"
);

btnCetak.getActionMap().put("ctrlC", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnCetak.doClick(); // Menjalankan aksi tombol
    }
});

    
btnRefresh.getActionMap().put("ctrlR", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnRefresh.doClick(); // Menjalankan aksi tombol
    }
});
btnBulan.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK),
    "ctrlB"
);

btnBulan.getActionMap().put("ctrlB", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnBulan.doClick(); // Menjalankan aksi tombol
    }
});
btnTahun.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK),
    "ctrlT"
);

btnTahun.getActionMap().put("ctrlT", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnTahun.doClick(); // Menjalankan aksi tombol
    }
});
    // event filter supplier
    cmbSupplier.addActionListener(evt -> {
        filterData();
    });

tblBulanan.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
    @Override
    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        // Hindari eksekusi ganda saat seleksi berubah dua kali
        if (!e.getValueIsAdjusting()) {
            int row = tblBulanan.getSelectedRow();
            if (row >= 0) {
                String tanggal = tblBulanan.getValueAt(row, 1).toString(); // kolom ke-1 = tanggal
                tampilkanDetail(tanggal);
            }
        }
    }
});

    btnRefresh.addActionListener(evt -> {
        cmbSupplier.setSelectedIndex(0);
        tampilData();
        setFilterDefault();
    });
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
    label.setBackground(new java.awt.Color (5,69,162));
    label.setForeground(Color.WHITE);
    label.setFont(new Font("Segoe UI", Font.BOLD, 15));
    label.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    label.setHorizontalAlignment(SwingConstants.LEFT);
    return label;
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
double totalKeluar = Session.hitungPengeluaran(tglMulai, tglSelesai);
NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
lblKeluar.setText(nf.format(totalKeluar));
SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
String dari = sdf.format(tglMulai);
String sampai = sdf.format(tglSelesai);
lblKeluar.setText("Pengeluaran dari " + dari + " sampai " + sampai + " adalah: " + nf.format(totalKeluar));

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error filter data: " + ex.getMessage());
    }
}

   
    private void loadSupplier() {
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
    
     private void loadDataBulan() {
    int bulan = jmcBulan.getMonth() + 1; // getMonth() biasanya mulai dari 0, jadi tambahkan 1
    int tahun = jycTahun.getYear(); // Mengambil tahun dari jycTahun
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No");
    model.addColumn("Tanggal");
    model.addColumn("Total Harian");
    tblBulanan.setModel(model);

    String sql = "SELECT EXTRACT(MONTH FROM tanggal) AS bulan, EXTRACT(YEAR FROM tanggal) AS tahun, "
                 + "TO_CHAR(tanggal, 'YYYY-MM-DD') AS tanggal, SUM(totalharga) AS total_harga "
                 + "FROM barangmasuk "
                 + "WHERE EXTRACT(MONTH FROM tanggal) = ? AND EXTRACT(YEAR FROM tanggal) = ? "
                 + "GROUP BY bulan, tahun, tanggal "
                 + "ORDER BY tanggal ASC";

    try (Connection conn = koneksi.dbKonek();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, bulan);
        pst.setInt(2, tahun);
        ResultSet rs = pst.executeQuery();

        int no = 1;
        double totalBulan = 0;
        while (rs.next()) {
            String tanggal = rs.getString("tanggal");
            double total = rs.getDouble("total_harga"); // Pastikan ini sesuai dengan nama kolom di query
            totalBulan += total;

            // Menambahkan baris ke tabel
            model.addRow(new Object[]{no++, tanggal, total});
        }

        // Menampilkan total bulanan
        lblTotalBulanan.setText("Rp " + String.format("%,.0f", totalBulan));

    } catch (SQLException e) {
        e.printStackTrace(); // Menangani kesalahan SQL
    } catch (Exception e) {
        e.printStackTrace(); // Menangani kesalahan lainnya
    }
}


private void tampilkanDetail(String tanggal) {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No");
    model.addColumn("Nama Barang");
    model.addColumn("Satuan");
    model.addColumn("Jumlah Masuk");
    model.addColumn("Harga Barang");
    model.addColumn("Total Harga");
    model.addColumn("Supplier");

    String sql = "SELECT nama, satuan, jumlahmasuk, hargabarang, totalharga, supplier "
               + "FROM barangmasuk WHERE tanggal = ? ORDER BY idbarangmasuk ASC";

    try (Connection conn = koneksi.dbKonek();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setDate(1, java.sql.Date.valueOf(tanggal));
        ResultSet rs = pst.executeQuery();

        int no = 1;
        while (rs.next()) {
            model.addRow(new Object[]{
                no++,
                rs.getString("nama"),
                rs.getString("satuan"),
                rs.getInt("jumlahmasuk"),
                rs.getDouble("hargabarang"),
                rs.getDouble("totalharga"),
                rs.getString("supplier")
            });
        }

        tblDetail.setModel(model);

    } catch (SQLException e) {
        e.printStackTrace();
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
        pp = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPembelian = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();
        cmbSupplier = new javax.swing.JComboBox<>();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        jdcStart = new com.toedter.calendar.JDateChooser();
        lblKeluar = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jmcBulan = new com.toedter.calendar.JMonthChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDetail = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBulanan = new javax.swing.JTable();
        btnBulan = new javax.swing.JButton();
        lblTotalBulanan = new javax.swing.JLabel();
        jycTahun = new com.toedter.calendar.JYearChooser();
        btnTahun = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1740, 960));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pp.setBackground(new java.awt.Color(255, 255, 255));
        pp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pp.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        tblPembelian.setRowHeight(35);
        jScrollPane1.setViewportView(tblPembelian);

        pp.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 1620, 290));

        btnRefresh.setBackground(new java.awt.Color(0, 153, 153));
        btnRefresh.setText("[CTRL+R] Refresh");
        pp.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 70, 130, 40));

        btnCetak.setBackground(new java.awt.Color(0, 102, 102));
        btnCetak.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak.setText("[CTRL+C] Cetak");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });
        pp.add(btnCetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 70, 140, 40));

        cmbSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pp.add(cmbSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, 140, 40));

        jdcEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcEndPropertyChange(evt);
            }
        });
        pp.add(jdcEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, 160, 40));
        pp.add(jdcStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 160, 40));

        lblKeluar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKeluar.setText("Selesai");
        pp.add(lblKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Sampai");
        pp.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, -1, 40));

        jPanel3.setBackground(new java.awt.Color(5, 69, 162));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Laporan Pembelian");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 340, 30));

        pp.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1680, 60));

        jPanel1.add(pp, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 1660, 450));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(5, 69, 162));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Laporan Pembelian Bulanan dan harian");
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 500, 30));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1680, 60));

        jmcBulan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jmcBulanPropertyChange(evt);
            }
        });
        jPanel4.add(jmcBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 140, 40));

        tblDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDetail.setRowHeight(35);
        jScrollPane2.setViewportView(tblDetail);

        jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 130, 620, 230));

        tblBulanan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblBulanan.setRowHeight(35);
        tblBulanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBulananMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblBulanan);

        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 980, 230));

        btnBulan.setBackground(new java.awt.Color(255, 0, 0));
        btnBulan.setForeground(new java.awt.Color(255, 255, 255));
        btnBulan.setText("[CTRL+B] Bulanan");
        btnBulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBulanActionPerformed(evt);
            }
        });
        jPanel4.add(btnBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, 150, 50));

        lblTotalBulanan.setText("lblTotalBulanan");
        jPanel4.add(lblTotalBulanan, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, -1, -1));

        jycTahun.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jycTahunPropertyChange(evt);
            }
        });
        jPanel4.add(jycTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 120, 40));

        btnTahun.setBackground(new java.awt.Color(255, 204, 0));
        btnTahun.setText("[CTRL+T]  Tahunan");
        btnTahun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTahunActionPerformed(evt);
            }
        });
        jPanel4.add(btnTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 70, 150, 50));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, 1660, 400));

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jdcEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcEndPropertyChange

    }//GEN-LAST:event_jdcEndPropertyChange

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
    // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Simpan Laporan Pembelian");

    // Default nama file
    fileChooser.setSelectedFile(new java.io.File("LaporanPembelian.pdf"));

    int userSelection = fileChooser.showSaveDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        java.io.File fileToSave = fileChooser.getSelectedFile();

        // Pastikan file berekstensi .pdf
        if (!fileToSave.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
            fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".pdf");
        }

        try {
            // === Buat PDF pakai iText ===
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(fileToSave));
            document.open();

            // Judul laporan
            com.itextpdf.text.Font fontJudul = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Paragraph judul = new com.itextpdf.text.Paragraph("Laporan Pembelian", fontJudul);
            judul.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(judul);

            document.add(new com.itextpdf.text.Paragraph(
                    "Tanggal Cetak: " + new java.util.Date().toString()));
            document.add(new com.itextpdf.text.Paragraph(" ")); // spasi kosong

            // Buat tabel PDF sesuai JTable
            int colCount = tblPembelian.getColumnCount();
            com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(colCount);
            pdfTable.setWidthPercentage(100); // tabel full lebar halaman

            // Header kolom
            for (int i = 0; i < colCount; i++) {
                com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(
                        new com.itextpdf.text.Phrase(tblPembelian.getColumnName(i)));
                headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                pdfTable.addCell(headerCell);
            }

            // Isi data baris
            int rowCount = tblPembelian.getRowCount();
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < colCount; col++) {
                    Object value = tblPembelian.getValueAt(row, col);
                    com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(
                            new com.itextpdf.text.Phrase(value != null ? value.toString() : ""));
                    cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    pdfTable.addCell(cell);
                }
            }

            document.add(pdfTable);

            // === Tambahkan jarak antar tabel dan keterangan total ===
            document.add(new com.itextpdf.text.Paragraph("\n"));

            com.itextpdf.text.Font fontKeterangan = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);

            com.itextpdf.text.Paragraph paragrafKeterangan =
                    new com.itextpdf.text.Paragraph(lblKeluar.getText(), fontKeterangan);

            paragrafKeterangan.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(paragrafKeterangan);

            // Tutup dokumen
            document.close();

            JOptionPane.showMessageDialog(this, "Laporan berhasil disimpan di:\n" + fileToSave.getAbsolutePath());

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mencetak PDF: " + ex.getMessage());
        }
    }
    }//GEN-LAST:event_btnCetakActionPerformed

    private void btnBulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBulanActionPerformed
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Pilih lokasi penyimpanan laporan bulanan");
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setSelectedFile(new File("Laporan_Pembelian_Bulanan.pdf"));

    int result = chooser.showSaveDialog(this);
    if (result != JFileChooser.APPROVE_OPTION) return;

    File file = chooser.getSelectedFile();
    if (!file.getName().toLowerCase().endsWith(".pdf")) {
        file = new File(file.getAbsolutePath() + ".pdf");
    }

    int bulan = jmcBulan.getMonth() + 1;
    int tahun = jycTahun.getYear();

    try (Connection conn = koneksi.dbKonek()) {
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();

        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        com.itextpdf.text.Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        Paragraph title = new Paragraph("LAPORAN PEMBELIAN BARANG BULAN " +
                new DateFormatSymbols().getMonths()[bulan - 1] + " TAHUN " + tahun, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(new Paragraph("\n"));

        String sqlBulan = """
            SELECT tanggal, SUM(totalharga) AS total_harian
            FROM barangmasuk
            WHERE EXTRACT(MONTH FROM tanggal)=? AND EXTRACT(YEAR FROM tanggal)=?
            GROUP BY tanggal ORDER BY tanggal
        """;

        double totalBulan = 0;
        try (PreparedStatement pst = conn.prepareStatement(sqlBulan)) {
            pst.setInt(1, bulan);
            pst.setInt(2, tahun);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                java.sql.Date tanggalSql = rs.getDate("tanggal");
                String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(tanggalSql);
                double totalHarian = rs.getDouble("total_harian");
                totalBulan += totalHarian;

                doc.add(new Paragraph("Tanggal " + tanggal + " - Pengeluaran: Rp" + String.format("%,.0f", totalHarian), normalFont));
                doc.add(new Paragraph(" ")); 

                String sqlDetail = """
                    SELECT nama, satuan, jumlahmasuk, hargabarang, totalharga, supplier
                    FROM barangmasuk WHERE tanggal=?
                """;

                try (PreparedStatement pstDetail = conn.prepareStatement(sqlDetail)) {
                    pstDetail.setDate(1, tanggalSql);
                    ResultSet rsDetail = pstDetail.executeQuery();

                  PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            String[] headers = {"No", "Barang", "Satuan", "Jumlah", "Harga", "Total", "Supplier"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            int no = 1;
            while (rsDetail.next()) {
                        table.addCell(new Phrase(String.valueOf(no++), normalFont));
                        table.addCell(new Phrase(rsDetail.getString("nama"), normalFont));
                        table.addCell(new Phrase(rsDetail.getString("satuan"), normalFont));
                        table.addCell(new Phrase(String.valueOf(rsDetail.getInt("jumlahmasuk")), normalFont));
                        table.addCell(new Phrase("Rp" + String.format("%,.0f", rsDetail.getDouble("hargabarang")), normalFont));
                        table.addCell(new Phrase("Rp" + String.format("%,.0f", rsDetail.getDouble("totalharga")), normalFont));
                        table.addCell(new Phrase(rsDetail.getString("supplier"), normalFont));
                    }
                    doc.add(table);
                    doc.add(new Paragraph("\n"));
                }
            }

            doc.add(new Paragraph("Total Pengeluaran Bulan Ini: Rp" + String.format("%,.0f", totalBulan), titleFont));
        }

        doc.close();
        JOptionPane.showMessageDialog(this, "Laporan bulanan berhasil disimpan di: " + file.getAbsolutePath());
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal mencetak laporan bulanan: " + e.getMessage());
    }

    }//GEN-LAST:event_btnBulanActionPerformed

    private void tblBulananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBulananMouseClicked

    }//GEN-LAST:event_tblBulananMouseClicked

    private void btnTahunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTahunActionPerformed
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Pilih lokasi penyimpanan laporan tahunan");
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setSelectedFile(new File("Laporan_Pembelian_Tahunan.pdf"));

    int result = chooser.showSaveDialog(this);
    if (result != JFileChooser.APPROVE_OPTION) return;

    File file = chooser.getSelectedFile();
    if (!file.getName().toLowerCase().endsWith(".pdf")) {
        file = new File(file.getAbsolutePath() + ".pdf");
    }

    int tahun = jycTahun.getYear();

    try (Connection conn = koneksi.dbKonek()) {
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();

        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        com.itextpdf.text.Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        Paragraph title = new Paragraph("LAPORAN PEMBELIAN BARANG TAHUN " + tahun, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(new Paragraph("\n"));

        double totalTahun = 0;

        for (int b = 1; b <= 12; b++) {
            String namaBulan = new DateFormatSymbols().getMonths()[b - 1];
            Paragraph pBulan = new Paragraph("Bulan " + namaBulan, headerFont);
            pBulan.setSpacingBefore(10);
            doc.add(pBulan);

            String sqlBulan = """
                SELECT tanggal, SUM(totalharga) AS total_harian
                FROM barangmasuk
                WHERE EXTRACT(MONTH FROM tanggal)=? AND EXTRACT(YEAR FROM tanggal)=?
                GROUP BY tanggal ORDER BY tanggal
            """;

            try (PreparedStatement pstBulan = conn.prepareStatement(sqlBulan)) {
                pstBulan.setInt(1, b);
                pstBulan.setInt(2, tahun);
                ResultSet rsBulan = pstBulan.executeQuery();

                double totalBulan = 0;
                while (rsBulan.next()) {
                    java.sql.Date tanggalSql = rsBulan.getDate("tanggal");
                    String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(tanggalSql);
                    double totalHarian = rsBulan.getDouble("total_harian");
                    totalBulan += totalHarian;

                    doc.add(new Paragraph("Tanggal " + tanggal + " - Pengeluaran: Rp" + String.format("%,.0f", totalHarian), normalFont));
                    doc.add(new Paragraph(" ")); 

                    String sqlDetail = """
                        SELECT nama, satuan, jumlahmasuk, hargabarang, totalharga, supplier
                        FROM barangmasuk WHERE tanggal=?
                    """;

                    try (PreparedStatement pstDetail = conn.prepareStatement(sqlDetail)) {
                        pstDetail.setDate(1, tanggalSql);
                        ResultSet rsDetail = pstDetail.executeQuery();

                        PdfPTable table = new PdfPTable(7);
                        table.setWidthPercentage(100);
                        String[] headers = {"No","Barang", "Satuan", "Jumlah", "Harga", "Total", "Supplier"};
                        for (String h : headers)
                            table.addCell(new PdfPCell(new Phrase(h, headerFont)));
                            int no = 1;
                        while (rsDetail.next()) {
                            table.addCell(new Phrase(String.valueOf(no++), normalFont));
                            table.addCell(new Phrase(rsDetail.getString("nama"), normalFont));
                            table.addCell(new Phrase(rsDetail.getString("satuan"), normalFont));
                            table.addCell(new Phrase(String.valueOf(rsDetail.getInt("jumlahmasuk")), normalFont));
                            table.addCell(new Phrase("Rp" + String.format("%,.0f", rsDetail.getDouble("hargabarang")), normalFont));
                            table.addCell(new Phrase("Rp" + String.format("%,.0f", rsDetail.getDouble("totalharga")), normalFont));
                            table.addCell(new Phrase(rsDetail.getString("supplier"), normalFont));
                        }
                        doc.add(table);
                        doc.add(new Paragraph("\n"));
                    }
                }

                doc.add(new Paragraph("Total Pengeluaran Bulan " + namaBulan + ": Rp" + String.format("%,.0f", totalBulan), headerFont));
                doc.add(new Paragraph("\n"));
                totalTahun += totalBulan;
            }
        }

        doc.add(new Paragraph("Total Pengeluaran Setahun: Rp" + String.format("%,.0f", totalTahun), titleFont));
        doc.close();

        JOptionPane.showMessageDialog(this, "Laporan tahunan berhasil disimpan di: " + file.getAbsolutePath());
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal mencetak laporan tahunan: " + e.getMessage());
    }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTahunActionPerformed

    private void jycTahunPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jycTahunPropertyChange
loadDataBulan();        // TODO add your handling code here:
    }//GEN-LAST:event_jycTahunPropertyChange

    private void jmcBulanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jmcBulanPropertyChange
loadDataBulan();        // TODO add your handling code here:
    }//GEN-LAST:event_jmcBulanPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBulan;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnTahun;
    private javax.swing.JComboBox<String> cmbSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private com.toedter.calendar.JMonthChooser jmcBulan;
    private com.toedter.calendar.JYearChooser jycTahun;
    private javax.swing.JLabel lblKeluar;
    private javax.swing.JLabel lblTotalBulanan;
    private javax.swing.JPanel pp;
    private javax.swing.JTable tblBulanan;
    private javax.swing.JTable tblDetail;
    private javax.swing.JTable tblPembelian;
    // End of variables declaration//GEN-END:variables
}
