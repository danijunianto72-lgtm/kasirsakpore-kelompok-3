/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Calendar;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
/**
 *
 * @author yaniyan
 */
public class Admin extends javax.swing.JPanel {

    /**
     * Creates new form Admin
     */

 public Admin() {
    initComponents();
    setFilterDefault();
getTotalStokKurang();

btnReset.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl R"), "reset");
        btnReset.getActionMap().put("reset", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
        btnReset.doClick();
        }
        });

    // atur layout panelChart
    panelChart.setLayout(new BorderLayout());

    // ambil tanggal hari ini
    java.time.LocalDate today = java.time.LocalDate.now();
    // ambil tanggal awal bulan ini
    String startDate = today.withDayOfMonth(1).toString();
    // ambil tanggal hari ini (akhir rentang default)
    String endDate = today.toString(); 

    loadChart(startDate, endDate);
    
    warna();

    try {
        // koneksi database
        Connection conn = koneksi.dbKonek();
        // update label dari database
        updateLabels(conn);
    } catch (SQLException e) {
        e.printStackTrace();
    }   

    // setelah semua selesai, set focus ke komponen date chooser
    SwingUtilities.invokeLater(() -> {
        jdcStart.getDateEditor().getUiComponent().requestFocusInWindow();
    });
}
     private void setFilterDefault() {
    Calendar cal = Calendar.getInstance();

    cal.set(Calendar.DAY_OF_MONTH, 1);
    jdcStart.setDate(cal.getTime());

    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    jdcEnd.setDate(cal.getTime());
}
public void getTotalStokKurang() {
    try {
    // koneksi ke PostgreSQL
    Connection conn = koneksi.dbKonek();
    

    // === Hitung barang dengan stok < 5 ===
    String sqlKurang = "SELECT COUNT(*) AS total FROM barang WHERE stok < 5";
    PreparedStatement psKurang = conn.prepareStatement(sqlKurang);
    ResultSet rsKurang = psKurang.executeQuery();
    if (rsKurang.next()) {
        int totalKurang = rsKurang.getInt("total");
        lblStok.setText(""+totalKurang);
    }

    // === Hitung semua barang ===
    String sqlTotal = "SELECT COUNT(*) AS total FROM barang";
    PreparedStatement psTotal = conn.prepareStatement(sqlTotal);
    ResultSet rsTotal = psTotal.executeQuery();
    if (rsTotal.next()) {
        int totalBarang = rsTotal.getInt("total");
        lblTotal.setText("" + totalBarang);
    }

        String sql = "SELECT COUNT(*) AS total FROM transaksi WHERE tgl_transaksi::date = CURRENT_DATE";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int total = rs.getInt("total");
            lblTransaksi.setText("" + total);
        }

    conn.close();

} catch (Exception e) {
    e.printStackTrace();
}
    

}

   
   private void warna(){
   applyGradient(panelPemasukkan, new Color(204, 255, 204), new Color(102, 204, 102), false);

// warna merah
applyGradient(panelPengeluaran, new Color(255, 204, 204), new Color(255, 102, 102), false);
applyGradient(pnlStok, new Color(255, 204, 204), new Color(255, 102, 102), false);

//warna orange
applyGradient(panelKeuntungan, new Color(255, 229, 204), new Color(255, 178, 102), false);
applyGradient(pnlBarang, new Color(255, 229, 204), new Color(255, 178, 102), false);

// warna ungu
applyGradient(pnlTransaksi, new Color(229, 204, 255), new Color(153, 102, 255), false);

// atas: biru muda, bawah: biru segar
applyGradient(panelKeuntunganTotal, new Color(204, 229, 255), new Color(102, 153, 255), false);
applyGradient(pnlMen, new Color(204, 229, 255), new Color(102, 153, 255), false);
   }
    private void updateLabels(Connection conn) {
        try {
            NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            // Total pemasukan hari ini
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT SUM(masuk) FROM Keuangan WHERE tanggal = CURRENT_DATE"
            );
            double totalMasuk = rs.next() ? rs.getDouble(1) : 0;
            lblMasuk.setText(rupiah.format(totalMasuk));
            rs.close();

            // Total pengeluaran hari ini
            rs = conn.createStatement().executeQuery(
                "SELECT SUM(keluar) FROM Keuangan WHERE tanggal = CURRENT_DATE"
            );
            double totalKeluar = rs.next() ? rs.getDouble(1) : 0;
            lblKeluar.setText(rupiah.format(totalKeluar));
            rs.close();

            // Keuntungan hari ini
            lblKeuntungan.setText(rupiah.format(totalMasuk - totalKeluar));

            // Keuntungan total (semua data)
            rs = conn.createStatement().executeQuery(
                "SELECT SUM(masuk) - SUM(keluar) FROM Keuangan"
            );
            double totalKeuntungan = rs.next() ? rs.getDouble(1) : 0;
            lblKeuntunganTotal.setText(rupiah.format(totalKeuntungan));
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

private void loadChart(String startDate, String endDate) {
    LineChart chart = new LineChart(startDate, endDate);

    JScrollPane scrollPane = new JScrollPane(chart,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    panelChart.removeAll();
    panelChart.add(scrollPane, BorderLayout.CENTER);
    panelChart.revalidate();
    panelChart.repaint();
}

/**
     * @param panel panel yang mau diwarnai
     * @param c1 warna awal
     * @param c2 warna akhir
     * @param horizontal true = kiri→kanan, false = atas→bawah
     */
    private void applyGradient(JPanel panel, Color c1, Color c2, boolean horizontal) {
        panel.setOpaque(false);
        panel.setUI(new javax.swing.plaf.PanelUI() {
            @Override
            public void update(Graphics g, javax.swing.JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                int w = c.getWidth();
                int h = c.getHeight();

                GradientPaint gp;
                if (horizontal) {
                    gp = new GradientPaint(0, 0, c1, w, 0, c2); // kiri → kanan
                } else {
                    gp = new GradientPaint(0, 0, c1, 0, h, c2); // atas → bawah
                }

                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);

                super.update(g, c);
            }
        });
    }

 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        panelChart = new javax.swing.JPanel();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        btnReset = new javax.swing.JButton();
        merah = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        merah1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        panelKeuntunganTotal = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblKeuntunganTotal = new javax.swing.JLabel();
        panelPemasukkan = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblMasuk = new javax.swing.JLabel();
        panelPengeluaran = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblKeluar = new javax.swing.JLabel();
        panelKeuntungan = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblKeuntungan = new javax.swing.JLabel();
        pnlMen = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        pnlTransaksi = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblTransaksi = new javax.swing.JLabel();
        pnlStok = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblStok = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        pnlBarang = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lblPemasukan7 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setFocusable(false);
        setMinimumSize(new java.awt.Dimension(20000, 20000));
        setPreferredSize(new java.awt.Dimension(1740, 960));
        setRequestFocusEnabled(false);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setToolTipText("focus");
        jPanel2.setFocusable(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(1740, 960));
        jPanel2.setRequestFocusEnabled(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Data pemasukkan dan pengeluran"));
        jPanel1.setToolTipText("focus");
        jPanel1.setFocusable(false);
        jPanel1.setRequestFocusEnabled(false);

        panelChart.setBackground(new java.awt.Color(255, 255, 255));
        panelChart.setFocusable(false);
        panelChart.setRequestFocusEnabled(false);
        panelChart.setLayout(new java.awt.BorderLayout());

        jdcStart.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcStartPropertyChange(evt);
            }
        });
        jdcStart.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jdcStartKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jdcStartKeyTyped(evt);
            }
        });

        jdcEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcEndPropertyChange(evt);
            }
        });
        jdcEnd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jdcEndKeyPressed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(51, 102, 255));
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        merah.setBackground(new java.awt.Color(255, 51, 51));
        merah.setToolTipText("focus");
        merah.setFocusable(false);
        merah.setRequestFocusEnabled(false);

        jLabel1.setText("Pengeluararn");
        jLabel1.setToolTipText("focus");
        jLabel1.setFocusable(false);
        jLabel1.setRequestFocusEnabled(false);

        jLabel2.setText("Pemasukkan");
        jLabel2.setToolTipText("focus");
        jLabel2.setFocusable(false);
        jLabel2.setRequestFocusEnabled(false);

        merah1.setBackground(new java.awt.Color(0, 153, 51));
        merah1.setToolTipText("focus");
        merah1.setFocusable(false);
        merah1.setRequestFocusEnabled(false);

        jLabel3.setText("Sampai");
        jLabel3.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(merah, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(merah1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jdcStart, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdcEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(925, Short.MAX_VALUE))
            .addComponent(panelChart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(merah, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)
                                .addComponent(merah1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jdcStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jdcEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnReset, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)))
                .addGap(18, 18, 18)
                .addComponent(panelChart, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 1690, 460));

        panelKeuntunganTotal.setToolTipText("focus");
        panelKeuntunganTotal.setFocusable(false);
        panelKeuntunganTotal.setRequestFocusEnabled(false);
        panelKeuntunganTotal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setText("Total Keuntungan");
        jLabel6.setFocusable(false);
        jLabel6.setRequestFocusEnabled(false);
        panelKeuntunganTotal.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblKeuntunganTotal.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblKeuntunganTotal.setText("Rp 400.000,00");
        lblKeuntunganTotal.setFocusable(false);
        lblKeuntunganTotal.setRequestFocusEnabled(false);
        panelKeuntunganTotal.add(lblKeuntunganTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 540, 90));

        jPanel2.add(panelKeuntunganTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 190, 610, 160));

        panelPemasukkan.setToolTipText("focus");
        panelPemasukkan.setFocusable(false);
        panelPemasukkan.setRequestFocusEnabled(false);
        panelPemasukkan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setText("Pemasukkan Hari Ini");
        jLabel4.setFocusable(false);
        jLabel4.setRequestFocusEnabled(false);
        panelPemasukkan.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblMasuk.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblMasuk.setText("Rp 200.000,00");
        lblMasuk.setFocusable(false);
        lblMasuk.setRequestFocusEnabled(false);
        panelPemasukkan.add(lblMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 540, 90));

        jPanel2.add(panelPemasukkan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 610, 160));

        panelPengeluaran.setToolTipText("focus");
        panelPengeluaran.setFocusable(false);
        panelPengeluaran.setRequestFocusEnabled(false);
        panelPengeluaran.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText("Pengeluaran Hari ini");
        jLabel5.setFocusable(false);
        jLabel5.setRequestFocusEnabled(false);
        panelPengeluaran.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblKeluar.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblKeluar.setText("Rp 160.000,00");
        lblKeluar.setFocusable(false);
        lblKeluar.setRequestFocusEnabled(false);
        panelPengeluaran.add(lblKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 540, 90));

        jPanel2.add(panelPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, 610, 160));

        panelKeuntungan.setToolTipText("focus");
        panelKeuntungan.setFocusable(false);
        panelKeuntungan.setRequestFocusEnabled(false);
        panelKeuntungan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setText("Keuntungan Hari ini");
        jLabel7.setFocusable(false);
        jLabel7.setRequestFocusEnabled(false);
        panelKeuntungan.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblKeuntungan.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblKeuntungan.setText("Rp 80.000,00");
        lblKeuntungan.setFocusable(false);
        lblKeuntungan.setRequestFocusEnabled(false);
        panelKeuntungan.add(lblKeuntungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 540, 90));

        jPanel2.add(panelKeuntungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 610, 160));

        pnlMen.setToolTipText("focus");
        pnlMen.setFocusable(false);
        pnlMen.setRequestFocusEnabled(false);
        pnlMen.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setText("Total Menu tersedia");
        jLabel10.setToolTipText("focus");
        jLabel10.setFocusable(false);
        jLabel10.setRequestFocusEnabled(false);
        pnlMen.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblTotal.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblTotal.setText("100");
        lblTotal.setToolTipText("focus");
        lblTotal.setFocusable(false);
        lblTotal.setRequestFocusEnabled(false);
        pnlMen.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 100, 90));

        jPanel2.add(pnlMen, new org.netbeans.lib.awtextra.AbsoluteConstraints(1290, 190, 180, 160));

        pnlTransaksi.setToolTipText("focus");
        pnlTransaksi.setFocusable(false);
        pnlTransaksi.setRequestFocusEnabled(false);
        pnlTransaksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setText("Total Transaksi Hari ini");
        jLabel8.setToolTipText("focus");
        jLabel8.setFocusable(false);
        jLabel8.setRequestFocusEnabled(false);
        pnlTransaksi.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblTransaksi.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblTransaksi.setText("99");
        lblTransaksi.setToolTipText("focus");
        lblTransaksi.setFocusable(false);
        lblTransaksi.setRequestFocusEnabled(false);
        pnlTransaksi.add(lblTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 100, 90));

        jPanel2.add(pnlTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(1290, 10, 180, 160));

        pnlStok.setToolTipText("focus");
        pnlStok.setFocusable(false);
        pnlStok.setRequestFocusEnabled(false);
        pnlStok.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setText("Jumlah Stok < 5");
        jLabel9.setToolTipText("focus");
        jLabel9.setFocusable(false);
        jLabel9.setRequestFocusEnabled(false);
        pnlStok.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblStok.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblStok.setText("10");
        lblStok.setToolTipText("focus");
        lblStok.setFocusable(false);
        lblStok.setRequestFocusEnabled(false);
        pnlStok.add(lblStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 100, 90));

        jButton1.setText("LIHAT DETAIL");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        pnlStok.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, -1));

        jPanel2.add(pnlStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(1510, 10, 180, 160));

        pnlBarang.setToolTipText("focus");
        pnlBarang.setFocusable(false);
        pnlBarang.setRequestFocusEnabled(false);
        pnlBarang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setText("Barang Terjual hari ini");
        jLabel11.setToolTipText("focus");
        jLabel11.setFocusable(false);
        jLabel11.setRequestFocusEnabled(false);
        pnlBarang.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblPemasukan7.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblPemasukan7.setText("40");
        lblPemasukan7.setToolTipText("focus");
        lblPemasukan7.setFocusable(false);
        lblPemasukan7.setRequestFocusEnabled(false);
        pnlBarang.add(lblPemasukan7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 100, 90));

        jButton2.setText("LIHAT DETAIL");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        pnlBarang.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, -1));

        jPanel2.add(pnlBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(1510, 190, 180, 160));

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1740, 960));
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
    // ambil tanggal awal bulan
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
    String startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

    // ambil tanggal hari ini
    String endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

    // load ulang chart
    loadChart(startDate, endDate);        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetActionPerformed

    private void jdcStartKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jdcStartKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jdcStartKeyTyped

    private void jdcStartKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jdcStartKeyPressed

    }//GEN-LAST:event_jdcStartKeyPressed

    private void jdcEndKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jdcEndKeyPressed
 
    }//GEN-LAST:event_jdcEndKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
   java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getWindowAncestor(this);

    // buat dan tampilkan JDialog FormBarang
    FormBarang dialog = new FormBarang(parentFrame, true, null); 
    dialog.setLocationRelativeTo(parentFrame); // tampil di tengah
    dialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jdcStartPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcStartPropertyChange
   if (jdcStart.getDate() != null && jdcEnd.getDate() != null) {
        String start = new java.sql.Date(jdcStart.getDate().getTime()).toString();
        String end = new java.sql.Date(jdcEnd.getDate().getTime()).toString();
        loadChart(start, end);
    }
    }//GEN-LAST:event_jdcStartPropertyChange

    private void jdcEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcEndPropertyChange
  if (jdcStart.getDate() != null && jdcEnd.getDate() != null) {
        String start = new java.sql.Date(jdcStart.getDate().getTime()).toString();
        String end = new java.sql.Date(jdcEnd.getDate().getTime()).toString();
        loadChart(start, end);
    }        // TODO add your handling code here:
    }//GEN-LAST:event_jdcEndPropertyChange

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReset;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private javax.swing.JLabel lblKeluar;
    private javax.swing.JLabel lblKeuntungan;
    private javax.swing.JLabel lblKeuntunganTotal;
    private javax.swing.JLabel lblMasuk;
    private javax.swing.JLabel lblPemasukan7;
    private javax.swing.JLabel lblStok;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTransaksi;
    private javax.swing.JPanel merah;
    private javax.swing.JPanel merah1;
    private javax.swing.JPanel panelChart;
    private javax.swing.JPanel panelKeuntungan;
    private javax.swing.JPanel panelKeuntunganTotal;
    private javax.swing.JPanel panelPemasukkan;
    private javax.swing.JPanel panelPengeluaran;
    private javax.swing.JPanel pnlBarang;
    private javax.swing.JPanel pnlMen;
    private javax.swing.JPanel pnlStok;
    private javax.swing.JPanel pnlTransaksi;
    // End of variables declaration//GEN-END:variables
}
