/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;
 
import javax.swing.JDialog;
import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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
public class LaporanPenjualan extends javax.swing.JPanel {

    /**
     * Creates new form LaporanPenjualan
     */
    public LaporanPenjualan() {
        initComponents();
        loadDataTransaksi();
        setFilterDefault();
        element();
        btnRefresh.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK),
    "ctrlR"
);

btnRefresh.getActionMap().put("ctrlR", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnRefresh.doClick(); // Menjalankan aksi tombol
    }
});

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

btnDetail.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK),
    "ctrlD"
);

btnDetail.getActionMap().put("ctrlD", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnDetail.doClick(); // Menjalankan aksi tombol
    }
});


    }
    private void setFilterDefault() {
    Calendar cal = Calendar.getInstance();

    // awal bulan
    cal.set(Calendar.DAY_OF_MONTH, 1);
    jdcStart.setDate(cal.getTime());

    // akhir bulan
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    jdcEnd.setDate(cal.getTime());
}


    
   private void loadDataTransaksi() {
    LocalDate startLocal = LocalDate.now().withDayOfMonth(1); // awal bulan
    LocalDate endLocal   = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()); // akhir bulan

    // Konversi ke java.util.Date
    java.util.Date start = java.util.Date.from(startLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
    java.util.Date end   = java.util.Date.from(endLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

    LoadDataTransaksi(start, end);
}
   
private void LoadDataTransaksi(java.util.Date startDate, java.util.Date endDate) {
    DefaultTableModel model = new DefaultTableModel(
        new String[]{"No", "ID", "No Transaksi", "Kasir", "Tanggal", "Grand Total", "Metode"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    try (Connection conn = koneksi.dbKonek()) {
        String sql = "SELECT idtransaksi, notransaksi, namapengguna, tgl_transaksi, grand_total, metodepembayaran " +
                     "FROM transaksi " +
                     "WHERE tgl_transaksi >= ? AND tgl_transaksi < ? " +
                     "ORDER BY tgl_transaksi DESC";

        PreparedStatement pst = conn.prepareStatement(sql);

        // === Ubah java.util.Date ke Timestamp ===
        LocalDate startLocal = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocal   = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Set parameter ke query
        pst.setTimestamp(1, Timestamp.valueOf(startLocal.atStartOfDay()));
        pst.setTimestamp(2, Timestamp.valueOf(endLocal.plusDays(1).atStartOfDay())); 

        ResultSet rs = pst.executeQuery();

        int no = 1;
        while (rs.next()) {
            model.addRow(new Object[]{
                no++,
                rs.getInt("idtransaksi"),
                rs.getString("notransaksi"),
                rs.getString("namapengguna"),
                rs.getTimestamp("tgl_transaksi"),
                rs.getBigDecimal("grand_total"),
                rs.getString("metodepembayaran")
            });
        }

        tblTransaksi.setModel(model);

        // Atur ukuran kolom
        tblTransaksi.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblTransaksi.getColumnModel().getColumn(0).setMaxWidth(50);
        tblTransaksi.getColumnModel().getColumn(0).setMinWidth(40);

        // Sembunyikan kolom ID
        tblTransaksi.getColumnModel().getColumn(1).setMinWidth(0);
        tblTransaksi.getColumnModel().getColumn(1).setMaxWidth(0);
        tblTransaksi.getColumnModel().getColumn(1).setWidth(0);
double totalMasuk = Session.hitungTotalTransaksi(startDate, endDate);
NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
lblKeterangan.setText(nf.format(totalMasuk));

// === Tambahan: tampilkan keterangan ===
SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
String dari = sdf.format(startDate);
String sampai = sdf.format(endDate);
lblKeterangan.setText("Pemasukan dari " + dari + " sampai " + sampai + " adalah: " + nf.format(totalMasuk));

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load transaksi: " + e.getMessage());
    }
}
private void element(){
    List<Component> tabOrder = Arrays.asList(
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

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnDetail = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTransaksi = new javax.swing.JTable();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        btnCetak = new javax.swing.JButton();
        lblKeterangan = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1740, 960));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("/");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, 10, 40));

        btnDetail.setBackground(new java.awt.Color(255, 255, 0));
        btnDetail.setText("Detail");
        btnDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetailActionPerformed(evt);
            }
        });
        jPanel1.add(btnDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 140, 160, 50));

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
        tblTransaksi.setRowHeight(33);
        tblTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransaksiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblTransaksi);
        if (tblTransaksi.getColumnModel().getColumnCount() > 0) {
            tblTransaksi.getColumnModel().getColumn(0).setMaxWidth(30);
            tblTransaksi.getColumnModel().getColumn(1).setMaxWidth(30);
        }

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 1620, 320));
        jPanel1.add(jdcStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 150, 50));

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

        jdcEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcEndPropertyChange(evt);
            }
        });
        jPanel1.add(jdcEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 140, 150, 50));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Selesai");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, -1, 40));

        btnCetak.setBackground(new java.awt.Color(0, 102, 102));
        btnCetak.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak.setText("Cetak");
        jPanel1.add(btnCetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 140, 160, 50));

        lblKeterangan.setText("jLabel1");
        jPanel1.add(lblKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 60, -1, -1));

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

    private void btnDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetailActionPerformed
int selectedRow = tblTransaksi.getSelectedRow();
if (selectedRow == -1) {
    JOptionPane.showMessageDialog(this, "Pilih transaksi dulu!");
    return;
}

// ID ada di kolom ke-1 (karena kolom 0 = nomor urut)
int idTransaksi = (int) tblTransaksi.getValueAt(selectedRow, 1);
PopupDetailTransaksi popup = new PopupDetailTransaksi(
    (java.awt.Frame) SwingUtilities.getWindowAncestor(this), 
    true, 
    idTransaksi
);
popup.pack(); // ukurannya menyesuaikan isi
popup.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this)); // tengah parent
popup.setVisible(true);

    }//GEN-LAST:event_btnDetailActionPerformed

    private void jdcEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcEndPropertyChange
 if ("date".equals(evt.getPropertyName())) {
        java.util.Date start = jdcStart.getDate();
        java.util.Date end = jdcEnd.getDate();

        if (start != null && end != null) {
            LoadDataTransaksi(start, end);
        }
    }         // TODO add your handling code here:
    }//GEN-LAST:event_jdcEndPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnDetail;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private javax.swing.JLabel lblKeterangan;
    private javax.swing.JTable tblTransaksi;
    // End of variables declaration//GEN-END:variables
}
