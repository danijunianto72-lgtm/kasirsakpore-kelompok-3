/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author yaniyan
 */
public class KelolaKeuangan extends javax.swing.JPanel {

    /**
     * Creates new form LaporanTransaksi
     */
   public KelolaKeuangan() {
        initComponents();
        header();
        loadDataKeuangan();
                    jdcTanggal.setDate(new java.util.Date());
                    setFilterDefault();
                    element();
                   

        txtMasuk.setText("0");
        txtKeluar.setText("0");
this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "submitAction");

    this.getActionMap().put("submitAction", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            btnSubmit.doClick(); // klik tombol submit
        }
    });

btnSubmit.getActionMap().put("submitAction", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnSubmit.doClick();
    }
});

btnReset.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK),
    "ctrlR"
);

btnReset.getActionMap().put("ctrlR", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnReset.doClick(); // Menjalankan aksi tombol
    }
});


btnEdit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK),
    "ctrlE"
);

btnEdit.getActionMap().put("ctrlE", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnEdit.doClick(); // Menjalankan aksi tombol
    }
});
btnDelete.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK),
    "ctrlD"
);

btnDelete.getActionMap().put("ctrlD", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnDelete.doClick(); // Menjalankan aksi tombol
    }
});
setKeyBindings();
// Misalnya tabel kamu bernama table1
JTableHeader header = tblKeuangan.getTableHeader();
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

    
   
private void header(){
 JTableHeader header = tblKeuangan.getTableHeader();
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
   private void setKeyBindings() {
    SwingUtilities.invokeLater(() -> {
        JRootPane root = this.getRootPane();
        if (root == null) return; // jaga-jaga null
        KeyStroke ctrlT = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK);
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlT, "focusTable");
        root.getActionMap().put("focusTable", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tblKeuangan.requestFocus();
                if (tblKeuangan.getRowCount() > 0) {
                    tblKeuangan.setRowSelectionInterval(0, 0);
                    tblKeuangan.setColumnSelectionInterval(0, 0);
                    tblKeuangan.editCellAt(0, 0);
                }
            }
        });
    });
}


boolean isEditMode = false; 
int selectedId = -1; // untuk simpan idkeuangan yang dipilih
// taruh di atas class


private java.util.List<Integer> idList = new ArrayList<>(); // buat simpan id

private void loadDataKeuangan() {
    java.util.Date start = java.sql.Date.valueOf(
        java.time.LocalDate.now().withDayOfMonth(1) // awal bulan
    );
    java.util.Date end = java.sql.Date.valueOf(
        java.time.LocalDate.now().withDayOfMonth(
            java.time.LocalDate.now().lengthOfMonth() // akhir bulan
        )
    );
    loadDataKeuangan(start, end);
}
private void loadDataKeuangan(java.util.Date startDate, java.util.Date endDate) {
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    model.addColumn("No");
    model.addColumn("Jenis Keuangan");
    model.addColumn("Masuk");
    model.addColumn("Keluar");
    model.addColumn("Tanggal");

    try (Connection conn = koneksi.dbKonek()) {
        String sql = "SELECT idkeuangan, jeniskeuangan, masuk, keluar, tanggal "
                   + "FROM keuangan "
                   + "WHERE tanggal::date BETWEEN ? AND ? "
                   + "ORDER BY tanggal DESC";
        PreparedStatement pst = conn.prepareStatement(sql);

        // konversi java.util.Date -> java.sql.Date
        pst.setDate(1, new java.sql.Date(startDate.getTime()));
        pst.setDate(2, new java.sql.Date(endDate.getTime()));

        ResultSet rs = pst.executeQuery();

        idList.clear();
        int no = 1;

        while (rs.next()) {
            idList.add(rs.getInt("idkeuangan"));
            model.addRow(new Object[]{
                no++,
                rs.getString("jeniskeuangan"),
                rs.getDouble("masuk"),
                rs.getDouble("keluar"),
                rs.getDate("tanggal")
            });
        }

        tblKeuangan.setModel(model);

        tblKeuangan.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblKeuangan.getColumnModel().getColumn(0).setMaxWidth(40);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
    }
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


private void clearForm() {
    txtJenis.setText("");
    txtMasuk.setText("0");
    txtKeluar.setText("0");
    Calendar cal = Calendar.getInstance();

    // awal bulan
    cal.set(Calendar.DAY_OF_MONTH, 1);
    jdcStart.setDate(cal.getTime());

    isEditMode = false;
    selectedId = -1;
    tblKeuangan.clearSelection();
}
private void element(){
    List<Component> tabOrder = Arrays.asList(
    jdcTanggal,
    txtJenis,
    txtMasuk,        
    txtKeluar,
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
        pnFormUser = new javax.swing.JPanel();
        jdcTanggal = new com.toedter.calendar.JDateChooser();
        lUsername = new javax.swing.JLabel();
        lPass = new javax.swing.JLabel();
        txtJenis = new javax.swing.JTextField();
        txtMasuk = new javax.swing.JTextField();
        lPass1 = new javax.swing.JLabel();
        txtKeluar = new javax.swing.JTextField();
        lPass2 = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        pnDaftarUser = new javax.swing.JPanel();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKeuangan = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1740, 960));

        pnFormUser.setBackground(new java.awt.Color(255, 255, 255));
        pnFormUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnFormUser.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnFormUser.add(jdcTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 340, 60));

        lUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lUsername.setText("Tanggal:");
        pnFormUser.add(lUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, -1));

        lPass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lPass.setText("Jenis Keuangan:");
        pnFormUser.add(lPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, -1, -1));

        txtJenis.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pnFormUser.add(txtJenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 340, 60));

        txtMasuk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pnFormUser.add(txtMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 360, 340, 60));

        lPass1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lPass1.setText("Masuk:");
        pnFormUser.add(lPass1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, -1, -1));

        txtKeluar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKeluarActionPerformed(evt);
            }
        });
        pnFormUser.add(txtKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 470, 340, 60));

        lPass2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lPass2.setText("Keluar:");
        pnFormUser.add(lPass2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 440, -1, -1));

        btnSubmit.setBackground(new java.awt.Color(0, 255, 51));
        btnSubmit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSubmit.setText("[ENTER] Sumbit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });
        pnFormUser.add(btnSubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 640, 290, 60));

        jPanel2.setBackground(new java.awt.Color(5, 69, 162));

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Kelola Keuangan");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(270, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel10)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pnFormUser.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 60));

        pnDaftarUser.setBackground(new java.awt.Color(255, 255, 255));
        pnDaftarUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnDaftarUser.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEdit.setBackground(new java.awt.Color(255, 153, 51));
        btnEdit.setText("EDIT");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        pnDaftarUser.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 110, 120, 40));

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        pnDaftarUser.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 110, 120, 40));

        jdcStart.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcStartPropertyChange(evt);
            }
        });
        pnDaftarUser.add(jdcStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 150, 40));

        jdcEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcEndPropertyChange(evt);
            }
        });
        pnDaftarUser.add(jdcEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, 150, 40));

        jLabel2.setText("SAMPAI");
        pnDaftarUser.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, -1, -1));

        btnReset.setBackground(new java.awt.Color(0, 153, 153));
        btnReset.setText("REFRESH");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        pnDaftarUser.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 110, 120, 40));

        tblKeuangan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal", "JenisKeuangan", "Masuk", "Keluar", "Total"
            }
        ));
        tblKeuangan.setRowHeight(35);
        tblKeuangan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKeuanganMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblKeuangan);
        if (tblKeuangan.getColumnModel().getColumnCount() > 0) {
            tblKeuangan.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        pnDaftarUser.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 1040, 520));

        jPanel3.setBackground(new java.awt.Color(5, 69, 162));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Daftar Keuangan");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addContainerGap(970, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pnDaftarUser.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1170, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
        jLabel3.setText("[ Ctrl=D ]");
        pnDaftarUser.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 90, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
        jLabel4.setText("[ Ctrl+R ]");
        pnDaftarUser.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 90, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
        jLabel5.setText("[ Ctrl+E ]");
        pnDaftarUser.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 90, -1, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(pnFormUser, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnDaftarUser, javax.swing.GroupLayout.PREFERRED_SIZE, 1142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(96, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnDaftarUser, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnFormUser, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(199, Short.MAX_VALUE))
        );

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

    private void txtKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKeluarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKeluarActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
int row = tblKeuangan.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data dulu di tabel!");
        return;
    }

    // set mode edit
    isEditMode = true;

    // isi field dari tabel
    String jenis = tblKeuangan.getValueAt(row, 1).toString();
    String masuk = tblKeuangan.getValueAt(row, 2).toString();
    String keluar = tblKeuangan.getValueAt(row, 3).toString();
    Object tgl = tblKeuangan.getValueAt(row, 4);

    txtJenis.setText(jenis);
    txtMasuk.setText(masuk);
    txtKeluar.setText(keluar);

    if (tgl != null) {
        if (tgl instanceof java.sql.Date || tgl instanceof java.util.Date) {
            jdcTanggal.setDate((java.util.Date) tgl);
        }
    }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
   String jenis = txtJenis.getText();
        double masuk = Double.parseDouble(txtMasuk.getText());
        double keluar = Double.parseDouble(txtKeluar.getText());
        java.util.Date utilDate = jdcTanggal.getDate();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        try (Connection conn = koneksi.dbKonek()) {
            if (isEditMode) {
                // UPDATE
                String sql = "UPDATE keuangan SET jeniskeuangan=?, masuk=?, keluar=?, tanggal=? WHERE idkeuangan=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, jenis);
                pst.setDouble(2, masuk);
                pst.setDouble(3, keluar);
                pst.setDate(4, sqlDate);
                pst.setInt(5, selectedId);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
                isEditMode = false; // reset mode
                selectedId = -1;
            } else {
                // INSERT
                String sql = "INSERT INTO keuangan (jeniskeuangan, masuk, keluar, tanggal) VALUES (?,?,?,?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, jenis);
                pst.setDouble(2, masuk);
                pst.setDouble(3, keluar);
                pst.setDate(4, sqlDate);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
            }
            // refresh table setelah insert / update
            loadDataKeuangan();
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void tblKeuanganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKeuanganMouseClicked
         int row = tblKeuangan.getSelectedRow();
    if (row != -1) {
        // simpan id keuangan yang dipilih
        selectedId = idList.get(row);
    }
    }//GEN-LAST:event_tblKeuanganMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
 int row = tblKeuangan.getSelectedRow();
    if (row == -1 || selectedId == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data yang mau dihapus!");
        return;
    }

    int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
    if (konfirmasi == JOptionPane.YES_OPTION) {
        try (Connection conn = koneksi.dbKonek()) {
            String sql = "DELETE FROM keuangan WHERE idkeuangan=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, selectedId);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            loadDataKeuangan();
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error hapus: " + e.getMessage());
        }
    }        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
clearForm();        // TODO add your handling code here:
 loadDataKeuangan();  // panggil ulang fungsi load semua data
setFilterDefault();
    }//GEN-LAST:event_btnResetActionPerformed

    private void jdcStartPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcStartPropertyChange
 if ("date".equals(evt.getPropertyName())) {
        java.util.Date start = jdcStart.getDate();
        java.util.Date end = jdcEnd.getDate();

        if (start != null && end != null) {
            loadDataKeuangan(start, end);
        }
    }         // TODO add your handling code here:
    }//GEN-LAST:event_jdcStartPropertyChange

    private void jdcEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcEndPropertyChange
 if ("date".equals(evt.getPropertyName())) {
        java.util.Date start = jdcStart.getDate();
        java.util.Date end = jdcEnd.getDate();

        if (start != null && end != null) {
            loadDataKeuangan(start, end);
        }
    }         // TODO add your handling code here:
    }//GEN-LAST:event_jdcEndPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private com.toedter.calendar.JDateChooser jdcTanggal;
    private javax.swing.JLabel lPass;
    private javax.swing.JLabel lPass1;
    private javax.swing.JLabel lPass2;
    private javax.swing.JLabel lUsername;
    private javax.swing.JPanel pnDaftarUser;
    private javax.swing.JPanel pnFormUser;
    private javax.swing.JTable tblKeuangan;
    private javax.swing.JTextField txtJenis;
    private javax.swing.JTextField txtKeluar;
    private javax.swing.JTextField txtMasuk;
    // End of variables declaration//GEN-END:variables
}
