/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package kasir;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


/**
 *
 * @author yaniyan
 */
public class FormBarang extends javax.swing.JDialog {

    /**
     * Creates new form FormBarang
     */
    public FormBarang(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        JTableHeader header = tblBarang.getTableHeader();
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
        
           // panggil method untuk load data
    loadBarang("", "Semua");   // kosong artinya load semua
    loadKategori();    
    tblBarang.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Ambil nilai stok dari kolom yang sesuai
        int stokCol = 5; // kolom ke-6 (indeks mulai dari 0)
        int stok = 0;
        try {
            stok = Integer.parseInt(table.getValueAt(row, stokCol).toString());
        } catch (Exception ex) {
            stok = 0;
        }

        // Atur warna latar berdasarkan stok
        if (!isSelected) { 
            if (stok < 5) {
                c.setBackground(new java.awt.Color(255, 102, 102)); // merah muda
            } else {
                c.setBackground(java.awt.Color.WHITE); // normal
            }
        } else {
            c.setBackground(table.getSelectionBackground()); // tetap warna seleksi
        }

        return c;
    }
});
    } 
public FormBarang(java.awt.Frame parent, boolean modal, Pembelian form) {
    super(parent, modal);
    initComponents();
    this.formUtama = form; 

    // panggil method untuk load data
    loadBarang("", "Semua");   // kosong artinya load semua
    loadKategori();    
    tblBarang.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Ambil nilai stok dari kolom yang sesuai
        int stokCol = 5; // kolom ke-6 (indeks mulai dari 0)
        int stok = 0;
        try {
            stok = Integer.parseInt(table.getValueAt(row, stokCol).toString());
        } catch (Exception ex) {
            stok = 0;
        }

        // Atur warna latar berdasarkan stok
        if (!isSelected) { 
            if (stok < 5) {
                c.setBackground(new java.awt.Color(255, 102, 102)); // merah muda
            } else {
                c.setBackground(java.awt.Color.WHITE); // normal
            }
        } else {
            c.setBackground(table.getSelectionBackground()); // tetap warna seleksi
        }

        return c;
    }
});
// isi combo box filter kategori
}

Pembelian formUtama; // simpan referensi ke panel utama

private void loadKategori() {
    cmbFilter.removeAllItems();
    cmbFilter.addItem("Semua"); // default pilihan

    try (Connection conn = koneksi.dbKonek()) {
        String sql = "SELECT namakategori FROM kategori ORDER BY namakategori ASC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            cmbFilter.addItem(rs.getString("namakategori"));
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load kategori: " + e.getMessage());
    }
}
    
private void loadBarang(String keyword, String filter) {
    DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
    model.setRowCount(0); // reset tabel

    try (Connection conn = koneksi.dbKonek()) {
        String sql = "SELECT kodebarang,skubarang, nama, satuan, hargapokok, stok, kategori " +
                     "FROM barang WHERE 1=1 ";

        // tambahkan pencarian
        if (keyword != null && !keyword.isEmpty()) {
            sql += " AND LOWER(nama) LIKE ? ";
        }

        // tambahkan filter kategori
        if (filter != null && !"Semua".equalsIgnoreCase(filter)) {
            sql += " AND kategori = ? ";
        }

        sql += " ORDER BY stok ASC"; // urut stok terendah

        PreparedStatement ps = conn.prepareStatement(sql);

        int paramIndex = 1;
        if (keyword != null && !keyword.isEmpty()) {
            ps.setString(paramIndex++, "%" + keyword.toLowerCase() + "%");
        }
        if (filter != null && !"Semua".equalsIgnoreCase(filter)) {
            ps.setString(paramIndex++, filter);
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] row = {
                rs.getInt("kodebarang"),
                rs.getString("nama"),
                rs.getString("satuan"),
                rs.getBigDecimal("hargapokok"),
                 rs.getString("skubarang"),
                rs.getInt("stok"),
                rs.getString("kategori")
            };
            model.addRow(row);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error load barang: " + e.getMessage());
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBarang = new javax.swing.JTable();
        txtCari = new javax.swing.JTextField();
        cmbFilter = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tblBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode", "Nama", "Satuan", "Harga", "sku", "Stok"
            }
        ));
        tblBarang.setRowHeight(35);
        tblBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBarangMouseClicked(evt);
            }
        });
        tblBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblBarangKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblBarang);
        if (tblBarang.getColumnModel().getColumnCount() > 0) {
            tblBarang.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });

        cmbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(350, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 27, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
 String keyword = txtCari.getText().trim();
        String filter = (String) cmbFilter.getSelectedItem();
        loadBarang(keyword, filter);        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariKeyReleased

    private void cmbFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFilterActionPerformed
       String keyword = txtCari.getText().trim();
        String filter = (String) cmbFilter.getSelectedItem();
        loadBarang(keyword, filter);
          // TODO add your handling code here:
    }//GEN-LAST:event_cmbFilterActionPerformed

    private void tblBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBarangMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBarangMouseClicked

    private void tblBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblBarangKeyPressed
 if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
    int row = tblBarang.getSelectedRow();
    if (row != -1) {
        String kode   = tblBarang.getValueAt(row, 0).toString();
        String nama   = tblBarang.getValueAt(row, 1).toString();
        String satuan = tblBarang.getValueAt(row, 2).toString();
        String harga  = tblBarang.getValueAt(row, 3).toString();
        String sku = tblBarang.getValueAt(row, 4).toString();

        // kirim ke JPanel Pembelian
        formUtama.setBarangTerpilih(kode, nama, satuan, harga,sku);

        dispose(); // tutup dialog
    }
}

    }//GEN-LAST:event_tblBarangKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FormBarang dialog = new FormBarang(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbFilter;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblBarang;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
