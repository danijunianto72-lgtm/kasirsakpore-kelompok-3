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

    // event filter supplier
    cmbSupplier.addActionListener(evt -> {
        filterData();
    });

    // tombol refresh
    btnRefresh.addActionListener(evt -> {
        cmbSupplier.setSelectedIndex(0);
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





    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1740, 960));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 1620, 250));

        btnRefresh.setBackground(new java.awt.Color(0, 153, 153));
        btnRefresh.setText("Refresh");
        jPanel2.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 100, 130, 40));

        btnCetak.setBackground(new java.awt.Color(0, 102, 102));
        btnCetak.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak.setText("Cetak");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });
        jPanel2.add(btnCetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(1490, 100, 140, 40));

        cmbSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(cmbSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, 140, 40));

        jdcEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcEndPropertyChange(evt);
            }
        });
        jPanel2.add(jdcEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, 160, 40));
        jPanel2.add(jdcStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 160, 40));

        lblKeluar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKeluar.setText("Selesai");
        jPanel2.add(lblKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Sampai");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, -1, 40));

        jPanel3.setBackground(new java.awt.Color(5, 69, 162));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Laporan Pembelian");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 340, 30));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1680, 60));

        jLabel2.setText("[ Ctrl+C ]");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1490, 80, -1, -1));

        jLabel4.setText("[ Ctrl+R ]");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 80, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 1660, 450));

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

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 510, 1660, 400));

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cmbSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private javax.swing.JLabel lblKeluar;
    private javax.swing.JTable tblPembelian;
    // End of variables declaration//GEN-END:variables
}
