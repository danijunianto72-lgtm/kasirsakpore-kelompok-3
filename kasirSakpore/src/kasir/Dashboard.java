/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasir;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import java.sql.*;
import javax.swing.*;
import static javax.swing.GroupLayout.Alignment.CENTER;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author user
 */
public class Dashboard extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
            txtPengguna.setText(Session.getUsername());

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    String tgl = sdf.format(new Date());
    lblTanggal.setText(tgl);
    Login();
    fungsibutton () ;
    }
private void Login() {
    String role = Session.getRole();

    // Set visibilitas tombol
    if ("kasir".equalsIgnoreCase(role)) {
        btnKasir.setVisible(true);
        btnAdmin.setVisible(false);
        btnBarang.setVisible(false);
        btnUser.setVisible(false);
        btnKeuangan.setVisible(false);
        btnSupplier.setVisible(false);
        btnPembelian.setVisible(false);
        btnLaporanPenjualan.setVisible(false);
        btnLaporanPembelian.setVisible(false);
        btnLaporanKeuangan.setVisible(false);

        // === GANTI PANEL ===
        setPanelUtama(new Kasir());

    } else if ("manager".equalsIgnoreCase(role)) {
        btnAdmin.setVisible(false);
        btnBarang.setVisible(false);
        btnUser.setVisible(false);
        btnKeuangan.setVisible(false);
        btnSupplier.setVisible(false);
        btnLaporanPenjualan.setVisible(true);
        btnLaporanPembelian.setVisible(true);
        btnLaporanKeuangan.setVisible(true);
        btnPembelian.setVisible(false);

        // === GANTI PANEL ===
        setPanelUtama(new LaporanKeuangan());

    } else if ("admin".equalsIgnoreCase(role)) {
        btnKasir.setVisible(true);

        // === GANTI PANEL ===
        setPanelUtama(new Admin());
    }
}

// Fungsi bantu untuk mengganti isi panel utama
private void setPanelUtama(JPanel panelBaru) {
    panelUtama.removeAll();
    panelUtama.setLayout(new BorderLayout());
    panelUtama.add(panelBaru, BorderLayout.CENTER);
    panelUtama.revalidate();
    panelUtama.repaint();
}

    
    
    private void fungsibutton (){
btnAdmin.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke("F1"), "f1Action");

btnAdmin.getActionMap().put("f1Action", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Jalankan aksi button
        btnAdmin.doClick();
    }
});

btnBarang.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke("F2"), "f2Action");

btnBarang.getActionMap().put("f2Action", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Jalankan aksi button
        btnBarang.doClick();
    }
});



btnUser.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke("F3"), "f3Action");

btnUser.getActionMap().put("f3Action", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Jalankan aksi button
        btnUser.doClick();
    }
});

btnKeuangan.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke("F4"), "f4Action");

btnKeuangan.getActionMap().put("f4Action", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Jalankan aksi button
        btnKeuangan.doClick();
    }
});

btnSupplier.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke("F5"), "f5Action");

btnSupplier.getActionMap().put("f5Action", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Jalankan aksi button
        btnSupplier.doClick();
    }
});

btnPembelian.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke("F6"), "f6Action");

btnPembelian.getActionMap().put("f6Action", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Jalankan aksi button
        btnPembelian.doClick();
    }
});

btnKasir.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke("F7"), "f7Action");

btnKasir.getActionMap().put("f7Action", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Jalankan aksi button
        btnKasir.doClick();
    }
});


btnLaporanPenjualan.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke("F8"), "f8Action");

btnLaporanPenjualan.getActionMap().put("f8Action", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Jalankan aksi button
        btnLaporanPenjualan.doClick();
    }
});


btnLaporanKeuangan.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke("F9"), "f9Action");

btnLaporanKeuangan.getActionMap().put("f9Action", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Jalankan aksi button
        btnLaporanKeuangan.doClick();
    }
});

btnLaporanPembelian.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke("F10"), "f10Action");

btnLaporanPembelian.getActionMap().put("f10Action", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Jalankan aksi button
        btnLaporanPembelian.doClick();
    }
});


    }

private boolean cekDanBatalkanKasirJikaAda() {
    if (panelUtama.getComponentCount() > 0 && panelUtama.getComponent(0) instanceof Kasir) {
        Kasir panelKasir = (Kasir) panelUtama.getComponent(0);
DefaultTableModel model = (DefaultTableModel) panelKasir.getTblKasir().getModel();

        if (model.getRowCount() > 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Masih ada transaksi di kasir.\nApakah ingin membatalkan dan lanjut berpindah halaman?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                panelKasir.batalkanTransaksi();
                return true; // boleh lanjut
            } else {
                return false; // batal pindah halaman
            }
        }
    }
    return true; // tidak ada kasir aktif / tabel kosong
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
        navbar = new javax.swing.JPanel();
        lblTanggal = new javax.swing.JLabel();
        btnLaporanPembelian = new javax.swing.JButton();
        btnLaporanPenjualan = new javax.swing.JButton();
        btnLaporanKeuangan = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnLogout = new javax.swing.JButton();
        txtPengguna = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        sidebar = new javax.swing.JPanel();
        btnAdmin = new javax.swing.JButton();
        btnBarang = new javax.swing.JButton();
        btnUser = new javax.swing.JButton();
        btnKeuangan = new javax.swing.JButton();
        btnKasir = new javax.swing.JButton();
        btnPembelian = new javax.swing.JButton();
        btnSupplier = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(47, 47, 47));

        panelUtama.setBackground(new java.awt.Color(255, 255, 255));
        panelUtama.setPreferredSize(new java.awt.Dimension(1740, 960));
        panelUtama.setLayout(new java.awt.BorderLayout());

        navbar.setBackground(new java.awt.Color(30, 58, 158));
        navbar.setMinimumSize(new java.awt.Dimension(1980, 140));
        navbar.setPreferredSize(new java.awt.Dimension(1920, 150));
        navbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTanggal.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblTanggal.setForeground(new java.awt.Color(255, 255, 255));
        lblTanggal.setText("WEDNESDAY 99, OKTOVER 2025");
        navbar.add(lblTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 30, 760, 90));

        btnLaporanPembelian.setText("[F10] LAPORAN PEMBELIAN");
        btnLaporanPembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporanPembelianActionPerformed(evt);
            }
        });
        navbar.add(btnLaporanPembelian, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 100, 200, 40));

        btnLaporanPenjualan.setText("[F8] LAPORAN PENJUALAN");
        btnLaporanPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporanPenjualanActionPerformed(evt);
            }
        });
        navbar.add(btnLaporanPenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, 200, 40));

        btnLaporanKeuangan.setText("[F9] LAPORAN KEUANGAN");
        btnLaporanKeuangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporanKeuanganActionPerformed(evt);
            }
        });
        navbar.add(btnLaporanKeuangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 55, 200, 40));

        jPanel1.setBackground(new java.awt.Color(30, 58, 158));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnLogout.setBackground(new java.awt.Color(255, 0, 0));
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        txtPengguna.setBackground(new java.awt.Color(0, 0, 0));
        txtPengguna.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        txtPengguna.setForeground(new java.awt.Color(255, 255, 255));
        txtPengguna.setText("manager");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtPengguna))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtPengguna)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        navbar.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1690, 20, 160, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/image-removebg-preview (2).png"))); // NOI18N
        navbar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 360, 130));

        sidebar.setBackground(new java.awt.Color(59, 130, 246));
        sidebar.setPreferredSize(new java.awt.Dimension(168, 960));

        btnAdmin.setText("[F1] ADMIN");
        btnAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminActionPerformed(evt);
            }
        });

        btnBarang.setText("[F2] BARANG");
        btnBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBarangActionPerformed(evt);
            }
        });

        btnUser.setText("[F3] USER");
        btnUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserActionPerformed(evt);
            }
        });

        btnKeuangan.setText("[F4] KEUANGAN");
        btnKeuangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeuanganActionPerformed(evt);
            }
        });

        btnKasir.setText("[F7] KASIR");
        btnKasir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKasirActionPerformed(evt);
            }
        });

        btnPembelian.setText("[F6] PEMBELIAN");
        btnPembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPembelianActionPerformed(evt);
            }
        });

        btnSupplier.setText("[F5] SUPPLIER");
        btnSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sidebarLayout = new javax.swing.GroupLayout(sidebar);
        sidebar.setLayout(sidebarLayout);
        sidebarLayout.setHorizontalGroup(
            sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKeuangan, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPembelian, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        sidebarLayout.setVerticalGroup(
            sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(btnKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPembelian, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(btnSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnKeuangan, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(354, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(navbar, javax.swing.GroupLayout.DEFAULT_SIZE, 1982, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelUtama, javax.swing.GroupLayout.PREFERRED_SIZE, 1805, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1810, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(navbar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelUtama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 162, Short.MAX_VALUE)
                    .addComponent(sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminActionPerformed
    if (!cekDanBatalkanKasirJikaAda()) {
        return; // batal pindah halaman kalau user pilih "No"
    }
        panelUtama.removeAll();                
    Admin adminPanel = new Admin();        

        panelUtama.add(adminPanel, BorderLayout.CENTER); 
    panelUtama.revalidate();               
    panelUtama.repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_btnAdminActionPerformed

    private void btnUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserActionPerformed
    if (!cekDanBatalkanKasirJikaAda()) {
        return; // batal pindah halaman kalau user pilih "No"
    }
        panelUtama.removeAll();                
    KelolaUser kuser = new KelolaUser();        
    panelUtama.add(kuser, BorderLayout.CENTER); 
    panelUtama.revalidate();               
    panelUtama.repaint();           
             // TODO add your handling code here:
    }//GEN-LAST:event_btnUserActionPerformed

    private void btnKasirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKasirActionPerformed
   if (!cekDanBatalkanKasirJikaAda()) {
        return; // batal pindah halaman kalau user pilih "No"
    }
        panelUtama.removeAll();                
    Kasir kasirP = new Kasir(); 
    panelUtama.add(kasirP,BorderLayout.CENTER); 
    panelUtama.revalidate();               
    panelUtama.repaint();   
    
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKasirActionPerformed

    private void btnBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBarangActionPerformed
    if (!cekDanBatalkanKasirJikaAda()) {
        return; // batal pindah halaman kalau user pilih "No"
    }
        panelUtama.removeAll();                
    KelolaBarang kbarang = new KelolaBarang();        
    panelUtama.add(kbarang,BorderLayout.CENTER); 
    panelUtama.revalidate();               
    panelUtama.repaint();             // TODO add your handling code here:
    }//GEN-LAST:event_btnBarangActionPerformed

    private void btnKeuanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeuanganActionPerformed
   if (!cekDanBatalkanKasirJikaAda()) {
        return; // batal pindah halaman kalau user pilih "No"
    }
        panelUtama.removeAll();                
    KelolaKeuangan kuang = new KelolaKeuangan();        
    panelUtama.add(kuang,BorderLayout.CENTER); 
    panelUtama.revalidate(); 
    
    panelUtama.repaint();          // TODO add your handling code here:
    }//GEN-LAST:event_btnKeuanganActionPerformed

    private void btnPembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPembelianActionPerformed
   if (!cekDanBatalkanKasirJikaAda()) {
        return; // batal pindah halaman kalau user pilih "No"
    }
        panelUtama.removeAll();                
    Pembelian kbeli = new Pembelian();        
    panelUtama.add(kbeli,BorderLayout.CENTER); 
    panelUtama.revalidate();               
    panelUtama.repaint();                                                 
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPembelianActionPerformed

    private void btnSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierActionPerformed
   if (!cekDanBatalkanKasirJikaAda()) {
        return; // batal pindah halaman kalau user pilih "No"
    }
        panelUtama.removeAll();                
    Suplier ksup = new Suplier();        
    panelUtama.add(ksup,BorderLayout.CENTER); 
    panelUtama.revalidate();               
    panelUtama.repaint();                                                 
               // TODO add your handling code here:
    }//GEN-LAST:event_btnSupplierActionPerformed

    private void btnLaporanPembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanPembelianActionPerformed
   if (!cekDanBatalkanKasirJikaAda()) {
        return; // batal pindah halaman kalau user pilih "No"
    }
        panelUtama.removeAll();                
    LaporanPembelian klapem = new LaporanPembelian();        
    panelUtama.add(klapem,BorderLayout.CENTER); 
    panelUtama.revalidate();               
    panelUtama.repaint();          // TODO add your handling code here:
    }//GEN-LAST:event_btnLaporanPembelianActionPerformed

    private void btnLaporanPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanPenjualanActionPerformed
   if (!cekDanBatalkanKasirJikaAda()) {
        return; // batal pindah halaman kalau user pilih "No"
    }
        panelUtama.removeAll();                
    LaporanPenjualan klapkeu = new LaporanPenjualan();        
    panelUtama.add(klapkeu,BorderLayout.CENTER); 
    panelUtama.revalidate();               
    panelUtama.repaint();           // TODO add your handling code here:
    }//GEN-LAST:event_btnLaporanPenjualanActionPerformed

    private void btnLaporanKeuanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanKeuanganActionPerformed
   if (!cekDanBatalkanKasirJikaAda()) {
        return; // batal pindah halaman kalau user pilih "No"
    }
        panelUtama.removeAll();                
    LaporanKeuangan klapkeu = new LaporanKeuangan();        
    panelUtama.add(klapkeu,BorderLayout.CENTER); 
    panelUtama.revalidate();               
    panelUtama.repaint();          // TODO add your handling code here:
    }//GEN-LAST:event_btnLaporanKeuanganActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
 Session.clear();
    new loginForm().setVisible(true);
    this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoutActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdmin;
    private javax.swing.JButton btnBarang;
    private javax.swing.JButton btnKasir;
    private javax.swing.JButton btnKeuangan;
    private javax.swing.JButton btnLaporanKeuangan;
    private javax.swing.JButton btnLaporanPembelian;
    private javax.swing.JButton btnLaporanPenjualan;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPembelian;
    private javax.swing.JButton btnSupplier;
    private javax.swing.JButton btnUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JPanel navbar;
    private javax.swing.JPanel panelUtama;
    private javax.swing.JPanel sidebar;
    private javax.swing.JLabel txtPengguna;
    // End of variables declaration//GEN-END:variables
}
