/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;
 
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.table.JTableHeader;
public class LaporanPenjualan extends javax.swing.JPanel {

    /**
     * Creates new form LaporanPenjualan
     */
        Connection conn; // <<==== Tambahkan ini

    public LaporanPenjualan() {
        initComponents();
        header();
        loadDataTransaksi();
        setFilterDefault();
    try {
            conn = koneksi.dbKonek(); // ✅ koneksi ke PostgreSQL
            loadDataBulan();          // langsung tampilkan data awal
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal koneksi ke database: " + e.getMessage());
        }
        element();
    
        tblBulan.getSelectionModel().addListSelectionListener(event -> {
    if (!event.getValueIsAdjusting()) {
        int row = tblBulan.getSelectedRow();
        if (row >= 0) {
            String tanggal = tblBulan.getValueAt(row, 1).toString();
            loadDetailHarian(tanggal);
        }
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

           btnBulan.getActionMap().put("ctrlV", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnBulan.doClick(); // Menjalankan aksi tombol
    }
});

        btnBulan.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
    "ctrlV"
); 
        
        
            btnDetail1.getActionMap().put("ctrlX", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnDetail1.doClick(); // Menjalankan aksi tombol
    }
});

        btnDetail1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK),
    "ctrlX"
);      
    
    

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
    
    private void header(){
    JTableHeader header = tblTransaksi.getTableHeader();
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

JTableHeader Header = tblDetail.getTableHeader();
Header.setOpaque(false); // Matikan transparansi bawaan
Header.setPreferredSize(new Dimension(Header.getWidth(), 40)); // 30 = tinggi header (px)

Header.setBackground(new java.awt.Color(5,69,162)); // Warna #2c3e50
Header.setForeground(Color.WHITE); // Warna font putih
Header.setFont(new Font("Segoe UI",Font.BOLD, 14)); // Font tebal

// Nonaktifkan UI bawaan Nimbus supaya warna tidak di-override
Header.setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
    JLabel label = new JLabel(value.toString());
    label.setOpaque(true);
    label.setBackground(new java.awt.Color(5,69,162));
    label.setForeground(Color.WHITE);
    label.setFont(new Font("Segoe UI", Font.BOLD, 15));
    label.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    label.setHorizontalAlignment(SwingConstants.LEFT);
    return label;
});

JTableHeader setHeader = tblBulan.getTableHeader();
setHeader.setOpaque(false); // Matikan transparansi bawaan
setHeader.setPreferredSize(new Dimension(setHeader.getWidth(), 40)); // 30 = tinggi header (px)

setHeader.setBackground(new java.awt.Color(5,69,162)); // Warna #2c3e50
setHeader.setForeground(Color.WHITE); // Warna font putih
setHeader.setFont(new Font("Segoe UI",Font.BOLD, 14)); // Font tebal

// Nonaktifkan UI bawaan Nimbus supaya warna tidak di-override
setHeader.setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
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

    // awal bulan
    cal.set(Calendar.DAY_OF_MONTH, 1);
    jdcStart.setDate(cal.getTime());

    // akhir bulan
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    jdcEnd.setDate(cal.getTime());
}


    private void loadDataBulan() {
    int bulan = jmcBulan.getMonth() + 1; // getMonth() biasanya mulai dari 0
    int tahun = jycTahun.getYear();
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No");
    model.addColumn("Tanggal");
    model.addColumn("Total Harian");
    tblBulan.setModel(model);

    String sql = "SELECT DATE(tgl_transaksi) AS tanggal, SUM(grand_total) AS total_harian "
               + "FROM transaksi WHERE EXTRACT(MONTH FROM tgl_transaksi)=? AND EXTRACT(YEAR FROM tgl_transaksi)=? "
               + "GROUP BY DATE(tgl_transaksi) ORDER BY DATE(tgl_transaksi)";

    try (Connection conn =koneksi.dbKonek();
            PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, bulan);
        pst.setInt(2, tahun);
        ResultSet rs = pst.executeQuery();

        int no = 1;
        double totalBulan = 0;
        while (rs.next()) {
            String tanggal = rs.getString("tanggal");
            double total = rs.getDouble("total_harian");
            totalBulan += total;

            model.addRow(new Object[]{no++, tanggal, total});
        }
        lblTotalBulan.setText("Total Pemasukkan bulan ini Rp " + String.format("%,.0f", totalBulan));
    } catch (Exception e) {
        e.printStackTrace();
    }
}

   private void loadDataTransaksi() {
    LocalDate startLocal = LocalDate.now().withDayOfMonth(1); // awal bulan
    LocalDate endLocal   = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()); // akhir bulan

    // Konversi ke java.util.Date
    java.util.Date start = java.util.Date.from(startLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
    java.util.Date end   = java.util.Date.from(endLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

    LoadDataTransaksi(start, end);
}
   private void loadDetailHarian(String tanggal) {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No");
    model.addColumn("ID Transaksi"); // tambahkan ini
    model.addColumn("No Transaksi");
    model.addColumn("Nama Pengguna");
    model.addColumn("Tanggal Transaksi");
    model.addColumn("Subtotal");
    model.addColumn("Diskon");
    model.addColumn("Grand Total");
    tblDetail.setModel(model);

    String sql = """
        SELECT idtransaksi, notransaksi, namapengguna, tgl_transaksi, subtotal, diskon, grand_total
        FROM transaksi
        WHERE DATE(tgl_transaksi)=?
        ORDER BY tgl_transaksi
    """;

    try (Connection conn = koneksi.dbKonek();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setDate(1, java.sql.Date.valueOf(tanggal));
        ResultSet rs = pst.executeQuery();

        int no = 1;
        while (rs.next()) {
            model.addRow(new Object[]{
                no++,
                rs.getInt("idtransaksi"),
                rs.getString("notransaksi"),
                rs.getString("namapengguna"),
                rs.getTimestamp("tgl_transaksi"),
                rs.getDouble("subtotal"),
                rs.getDouble("diskon"),
                rs.getDouble("grand_total")
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
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
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        lblKeterangan = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();
        btnDetail = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTransaksi = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetail = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBulan = new javax.swing.JTable();
        jmcBulan = new com.toedter.calendar.JMonthChooser();
        jycTahun = new com.toedter.calendar.JYearChooser();
        lblTotalBulan = new javax.swing.JLabel();
        btnDetail1 = new javax.swing.JButton();
        btnBulan = new javax.swing.JButton();
        btnTahun = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1740, 960));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(5, 69, 162));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Laporan Penjualan");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel9)
                .addContainerGap(1411, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 1638, -1));
        jPanel2.add(jdcStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 90, 150, 40));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Selesai");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 90, -1, 40));

        jdcEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcEndPropertyChange(evt);
            }
        });
        jPanel2.add(jdcEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 150, 40));

        lblKeterangan.setText("jLabel1");
        jPanel2.add(lblKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 100, 450, -1));

        btnRefresh.setBackground(new java.awt.Color(0, 153, 153));
        btnRefresh.setText("[CTRL+R] REFRESH");
        jPanel2.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 80, 150, 50));

        btnCetak.setBackground(new java.awt.Color(0, 102, 102));
        btnCetak.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak.setText("[CTRL+C] CETAK");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });
        jPanel2.add(btnCetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 80, 160, 50));

        btnDetail.setBackground(new java.awt.Color(255, 255, 0));
        btnDetail.setText("[CTR;+D] DETAIL");
        btnDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetailActionPerformed(evt);
            }
        });
        jPanel2.add(btnDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 80, 160, 50));

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
        tblTransaksi.setRowHeight(35);
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

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 1620, 250));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setText("Daftar Transaksi");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1640, 420));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(5, 69, 162));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Laporan Bulanan dan harian");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel11)
                .addContainerGap(1302, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 1638, -1));

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
        jScrollPane1.setViewportView(tblDetail);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 130, 610, 250));

        tblBulan.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBulan.setRowHeight(35);
        tblBulan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBulanMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblBulan);

        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 990, 250));

        jmcBulan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jmcBulanPropertyChange(evt);
            }
        });
        jPanel4.add(jmcBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, -1, 40));

        jycTahun.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jycTahunPropertyChange(evt);
            }
        });
        jPanel4.add(jycTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 120, 40));

        lblTotalBulan.setText("jLabel1");
        jPanel4.add(lblTotalBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 90, -1, -1));

        btnDetail1.setBackground(new java.awt.Color(255, 255, 0));
        btnDetail1.setText("[CTRL+X] DETAIL");
        btnDetail1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetail1ActionPerformed(evt);
            }
        });
        jPanel4.add(btnDetail1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 70, 160, 50));

        btnBulan.setBackground(new java.awt.Color(255, 0, 0));
        btnBulan.setForeground(new java.awt.Color(255, 255, 255));
        btnBulan.setText("[CTRL+B] BULANAN");
        btnBulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBulanActionPerformed(evt);
            }
        });
        jPanel4.add(btnBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, 160, 50));

        btnTahun.setBackground(new java.awt.Color(255, 153, 0));
        btnTahun.setForeground(new java.awt.Color(0, 0, 51));
        btnTahun.setText("[CTRL+T] TAHUNAN");
        btnTahun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTahunActionPerformed(evt);
            }
        });
        jPanel4.add(btnTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 70, 160, 50));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 1640, 480));

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan Penjualan");

        // Default nama file
        fileChooser.setSelectedFile(new java.io.File("LaporanTransaksi.pdf"));

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
                com.itextpdf.text.Paragraph judul = new com.itextpdf.text.Paragraph("Laporan Transaksi", fontJudul);
                judul.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(judul);

                document.add(new com.itextpdf.text.Paragraph(
                    "Tanggal Cetak: " + new java.util.Date().toString()));
            document.add(new com.itextpdf.text.Paragraph(" ")); // spasi kosong

            // Buat tabel PDF sesuai JTable
            int colCount = tblTransaksi.getColumnCount();
            com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(colCount);
            pdfTable.setWidthPercentage(100); // tabel full lebar halaman

            // Header kolom
            for (int i = 0; i < colCount; i++) {
                com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(
                    new com.itextpdf.text.Phrase(tblTransaksi.getColumnName(i)));
                headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                pdfTable.addCell(headerCell);
            }

            // Isi data baris
            int rowCount = tblTransaksi.getRowCount();
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < colCount; col++) {
                    Object value = tblTransaksi.getValueAt(row, col);
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
            new com.itextpdf.text.Paragraph(lblKeterangan.getText(), fontKeterangan);

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

    private void jdcEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcEndPropertyChange
        if ("date".equals(evt.getPropertyName())) {
            java.util.Date start = jdcStart.getDate();
            java.util.Date end = jdcEnd.getDate();

            if (start != null && end != null) {
                LoadDataTransaksi(start, end);
            }
        }         // TODO add your handling code here:
    }//GEN-LAST:event_jdcEndPropertyChange

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

    private void jmcBulanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jmcBulanPropertyChange
        loadDataBulan();
        // TODO add your handling code here:
    }//GEN-LAST:event_jmcBulanPropertyChange

    private void jycTahunPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jycTahunPropertyChange
        loadDataBulan();
        // TODO add your handling code here:
    }//GEN-LAST:event_jycTahunPropertyChange

    private void btnDetail1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetail1ActionPerformed
         int selectedRow = tblDetail.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih transaksi terlebih dahulu!");
        return;
    }

    // Ambil idTransaksi dari tabel
    int idTransaksi = (int) tblDetail.getValueAt(selectedRow, 1); // kolom ke-1 adalah ID Transaksi

    // Buka popup detail transaksi
    PopupDetailTransaksi popup = new PopupDetailTransaksi((java.awt.Frame) SwingUtilities.getWindowAncestor(this), true, idTransaksi);
    popup.setLocationRelativeTo(this);
    popup.setVisible(true);
    }//GEN-LAST:event_btnDetail1ActionPerformed

    private void tblBulanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBulanMouseClicked
int row = tblBulan.getSelectedRow();
    if (row < 0) return;
    String tanggal = tblBulan.getValueAt(row, 1).toString();
    loadDetailHarian(tanggal);        // TODO add your handling code here:
    }//GEN-LAST:event_tblBulanMouseClicked

    private void btnBulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBulanActionPerformed
      int bulan = jmcBulan.getMonth() + 1;
    int tahun = jycTahun.getYear();

    try {
        conn = koneksi.dbKonek();

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Laporan PDF");
        chooser.setSelectedFile(new java.io.File("Laporan_Penjualan" + bulan + "_" + tahun + ".pdf"));

        int userSelection = chooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return; // Batal simpan
        }

        java.io.File fileToSave = chooser.getSelectedFile();
        if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
            fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".pdf");
        }

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(fileToSave));
        doc.open();

        // ==== HEADER ====
        Paragraph title = new Paragraph("LAPORAN PENJUALAN BULANAN",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK));
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(new Paragraph("Bulan: " + bulan + "  Tahun: " + tahun));
        doc.add(new Paragraph("Total Keuntungan: " + lblTotalBulan.getText()));
        doc.add(new Paragraph(" "));

        // ==== AMBIL DATA ====
        String sqlTanggal = """
            SELECT DISTINCT DATE(tgl_transaksi) AS tanggal
            FROM transaksi
            WHERE EXTRACT(MONTH FROM tgl_transaksi)=? 
              AND EXTRACT(YEAR FROM tgl_transaksi)=?
            ORDER BY DATE(tgl_transaksi)
        """;

        PreparedStatement pstTanggal = conn.prepareStatement(sqlTanggal);
        pstTanggal.setInt(1, bulan);
        pstTanggal.setInt(2, tahun);
        ResultSet rsTanggal = pstTanggal.executeQuery();

        double totalBulan = 0;

        while (rsTanggal.next()) {
            String tanggal = rsTanggal.getString("tanggal");
            doc.add(new Paragraph("Tanggal: " + tanggal,
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK)));
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(6);
            table.setWidths(new float[]{1.5f, 3f, 3f, 3f, 3f, 3f});
            table.setWidthPercentage(100);
            table.addCell("No");
            table.addCell("No Transaksi");
            table.addCell("Nama Pengguna");
            table.addCell("Subtotal");
            table.addCell("Diskon");
            table.addCell("Total");

            String sqlDetail = """
                SELECT notransaksi, namapengguna, subtotal, diskon, grand_total
                FROM transaksi
                WHERE DATE(tgl_transaksi)=?
                ORDER BY tgl_transaksi
            """;

            PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);
            pstDetail.setDate(1, java.sql.Date.valueOf(tanggal));
            ResultSet rsDetail = pstDetail.executeQuery();

            int no = 1;
            double subtotalTanggal = 0;
            while (rsDetail.next()) {
                table.addCell(String.valueOf(no++));
                table.addCell(rsDetail.getString("notransaksi"));
                table.addCell(rsDetail.getString("namapengguna"));
                table.addCell(String.format("%,.0f", rsDetail.getDouble("subtotal")));
                table.addCell(String.format("%,.0f", rsDetail.getDouble("diskon")));
                table.addCell(String.format("%,.0f", rsDetail.getDouble("grand_total")));
                subtotalTanggal += rsDetail.getDouble("grand_total");
            }

            doc.add(table);
            doc.add(new Paragraph("Subtotal tanggal " + tanggal + ": Rp " + String.format("%,.0f", subtotalTanggal)));
            doc.add(new Paragraph(" "));

            totalBulan += subtotalTanggal;
            rsDetail.close();
            pstDetail.close();
        }

        doc.add(new Paragraph("====================================================="));
        doc.add(new Paragraph("TOTAL KEUNTUNGAN BULAN INI: Rp " + String.format("%,.0f", totalBulan),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK)));
        doc.add(new Paragraph("====================================================="));

        doc.close();
        JOptionPane.showMessageDialog(this, "Laporan berhasil disimpan di:\n" + fileToSave.getAbsolutePath());

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal mencetak laporan: " + e.getMessage());
    }      // TODO add your handling code here:
    }//GEN-LAST:event_btnBulanActionPerformed

    private void btnTahunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTahunActionPerformed
int tahun = jycTahun.getYear();

try {
    Connection conn = koneksi.dbKonek();

    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Simpan Laporan Penjualan Tahunan");
    chooser.setSelectedFile(new java.io.File("Laporan_Penjualan" + tahun + ".pdf"));

    int userSelection = chooser.showSaveDialog(this);
    if (userSelection != JFileChooser.APPROVE_OPTION) return;

    java.io.File fileToSave = chooser.getSelectedFile();
    if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
        fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".pdf");
    }

    Document doc = new Document(PageSize.A4);
    PdfWriter.getInstance(doc, new FileOutputStream(fileToSave));
    doc.open();

    // ==== HEADER UTAMA ====
    Paragraph title = new Paragraph("LAPORAN PENJUALAN TAHUN " + tahun,
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK));
    title.setAlignment(Element.ALIGN_CENTER);
    doc.add(title);
    doc.add(new Paragraph(" "));
    doc.add(new Paragraph(" "));

    // ==== AMBIL DATA BULAN ====
    String sqlBulan = """
        SELECT DISTINCT EXTRACT(MONTH FROM tgl_transaksi) AS bulan
        FROM transaksi
        WHERE EXTRACT(YEAR FROM tgl_transaksi) = ?
        ORDER BY bulan;
    """;
    PreparedStatement pstBulan = conn.prepareStatement(sqlBulan);
    pstBulan.setInt(1, tahun);
    ResultSet rsBulan = pstBulan.executeQuery();

    double totalTahun = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0");

    while (rsBulan.next()) {
        int bulan = rsBulan.getInt("bulan");
        String namaBulan = java.time.Month.of(bulan)
                .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.forLanguageTag("id-ID"));

        // === HEADER BULAN ===
        doc.add(new Paragraph("Bulan " + namaBulan,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK)));
        doc.add(new Paragraph(" "));

        // ==== AMBIL DATA TANGGAL ====
        String sqlTanggal = """
            SELECT DISTINCT DATE(tgl_transaksi) AS tanggal
            FROM transaksi
            WHERE EXTRACT(MONTH FROM tgl_transaksi)=? 
              AND EXTRACT(YEAR FROM tgl_transaksi)=?
            ORDER BY DATE(tgl_transaksi)
        """;
        PreparedStatement pstTanggal = conn.prepareStatement(sqlTanggal);
        pstTanggal.setInt(1, bulan);
        pstTanggal.setInt(2, tahun);
        ResultSet rsTanggal = pstTanggal.executeQuery();

        double totalBulan = 0;

        while (rsTanggal.next()) {
            String tanggal = rsTanggal.getString("tanggal");

            // === TAMPILKAN HEADER TANGGAL ===
            doc.add(new Paragraph("Tanggal: " + sdf.format(java.sql.Date.valueOf(tanggal)),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK)));

            // === AMBIL DATA TRANSAKSI HARIAN ===
            String sqlDetail = """
                SELECT notransaksi, namapengguna, subtotal, diskon, grand_total
                FROM transaksi
                WHERE DATE(tgl_transaksi)=?
                ORDER BY notransaksi;
            """;
            PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);
            pstDetail.setDate(1, java.sql.Date.valueOf(tanggal));
            ResultSet rsDetail = pstDetail.executeQuery();

            PdfPTable table = new PdfPTable(6);
            table.setWidths(new float[]{1.5f, 3f, 3f, 3f, 3f, 3f});
            table.setWidthPercentage(100);
            table.addCell("No");
            table.addCell("No Transaksi");
            table.addCell("Nama Pengguna");
            table.addCell("Subtotal");
            table.addCell("Diskon");
            table.addCell("Total");

            int no = 1;
            double subtotalTanggal = 0;
            while (rsDetail.next()) {
                table.addCell(String.valueOf(no++));
                table.addCell(rsDetail.getString("notransaksi"));
                table.addCell(rsDetail.getString("namapengguna"));
                table.addCell(String.format("%,.0f", rsDetail.getDouble("subtotal")));
                table.addCell(String.format("%,.0f", rsDetail.getDouble("diskon")));
                table.addCell(String.format("%,.0f", rsDetail.getDouble("grand_total")));
                subtotalTanggal += rsDetail.getDouble("grand_total");
            }

            doc.add(table);
            doc.add(new Paragraph("Total Pemasukan Tanggal " + sdf.format(java.sql.Date.valueOf(tanggal))
                    + ": Rp " + df.format(subtotalTanggal)));
            doc.add(new Paragraph(" "));

            totalBulan += subtotalTanggal;

            rsDetail.close();
            pstDetail.close();
        }

        // === TOTAL BULANAN ===
        doc.add(new Paragraph("------------------------------------------------------------"));
        doc.add(new Paragraph("Total Pemasukan Bulan " + namaBulan + ": Rp " + df.format(totalBulan),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK)));
        doc.add(new Paragraph("------------------------------------------------------------"));
        doc.add(new Paragraph(" "));

        totalTahun += totalBulan;
        rsTanggal.close();
        pstTanggal.close();
    }

    // === TOTAL AKHIR TAHUN ===
    doc.add(new Paragraph("============================================================"));
    doc.add(new Paragraph("TOTAL PEMASUKAN TAHUN " + tahun + ": Rp " + df.format(totalTahun),
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK)));
    doc.add(new Paragraph("============================================================"));

    doc.close();
    JOptionPane.showMessageDialog(this, "Laporan tahunan berhasil disimpan di:\n" + fileToSave.getAbsolutePath());

} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Gagal mencetak laporan tahunan: " + e.getMessage());
}
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTahunActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBulan;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnDetail;
    private javax.swing.JButton btnDetail1;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnTahun;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
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
    private javax.swing.JLabel lblKeterangan;
    private javax.swing.JLabel lblTotalBulan;
    private javax.swing.JTable tblBulan;
    private javax.swing.JTable tblDetail;
    private javax.swing.JTable tblTransaksi;
    // End of variables declaration//GEN-END:variables
}
