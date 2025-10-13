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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
/**
 *
 * @author user
 */
public class KelolaUser extends javax.swing.JPanel {

 
    
    private boolean editMode = false; 
    private int editId = -1; 
    public KelolaUser() {
        initComponents();
        isiComboBox();
        tampilData();
        loadRiwayatData();
        loadUsernameCombo();
        element();
        setKeyBindings();
        setHeader();
        
        
    // Tambahkan shortcut CTRL + ENTER untuk tombol Submit
  btSubmit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK),
    "submitAction"
);

btSubmit.getActionMap().put("submitAction", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btSubmit.doClick();
    }
});


//  btSubmit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
//    KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
//    "ctrlS"
//);
//
//btSubmit.getActionMap().put("ctrlS", new AbstractAction() {
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        btSubmit.doClick();
//    }
//});

  this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "submitAction");

    this.getActionMap().put("submitAction", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            btSubmit.doClick(); // klik tombol submit
        }
    });
     Batal.getActionMap().put("ctrlB", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        Batal.doClick(); // Menjalankan aksi tombol
    }
});

        Batal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK),
    "ctrlB"
); 
        
             btEdit.getActionMap().put("ctrlE", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btEdit.doClick(); // Menjalankan aksi tombol
    }
});

        btEdit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK),
    "ctrlE"
); 
        
           btDelete.getActionMap().put("ctrlD", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btDelete.doClick(); // Menjalankan aksi tombol
    }
});

        btDelete.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK),
    "ctrlD"
); 

    }
       private void loadUsernameCombo() {
        try (Connection conn = koneksi.dbKonek()) {
            String sql = "SELECT username FROM pengguna ORDER BY username";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            cmbUsername.removeAllItems();
            cmbUsername.addItem("Semua");
            while (rs.next()) {
                cmbUsername.addItem(rs.getString("username"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load username: " + e.getMessage());
        }
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
                tblUser.requestFocus();
                if (tblUser.getRowCount() > 0) {
                    tblUser.setRowSelectionInterval(0, 0);
                    tblUser.setColumnSelectionInterval(0, 0);
                    tblUser.editCellAt(0, 0);
                }
            }
        });
    });
}

private void setHeader(){
JTableHeader header = tblUser.getTableHeader();
header.setOpaque(false); 
header.setPreferredSize(new Dimension(header.getWidth(), 40)); 

header.setBackground(new java.awt.Color(5,69,162)); 
header.setForeground(Color.WHITE);
header.setFont(new Font("Segoe UI",Font.BOLD, 14)); 

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
JTableHeader garis = tblRiwayat.getTableHeader();
garis.setOpaque(false); 
garis.setPreferredSize(new Dimension(header.getWidth(), 40)); 

garis.setBackground(new java.awt.Color(5,69,162)); 
garis.setForeground(Color.WHITE);
garis.setFont(new Font("Segoe UI",Font.BOLD, 14)); 

garis.setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
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
    
    // tampilkan data user di tabel
    private void tampilData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID");
        model.addColumn("Username");
        model.addColumn("Role");
        model.addColumn("Status");
        
        try (Connection conn = kasir.koneksi.dbKonek()) {
            String sql = "SELECT *FROM pengguna ORDER BY idpengguna ASC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getInt("idpengguna"),
                    rs.getString("username"),
                    rs.getString("role"),
                    rs.getString("status")
                });
            }
            tblUser.setModel(model);
            
            // sembunyikan kolom ID (jangan ditampilkan ke user)
            tblUser.getColumnModel().getColumn(1).setMinWidth(0);
            tblUser.getColumnModel().getColumn(1).setMaxWidth(0);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error tampil data: " + ex.getMessage());
        }
    }
    // isi combobox role dan status
    private void isiComboBox() {
        cbRole.removeAllItems();
        cbRole.addItem("admin");
        cbRole.addItem("kasir");
        cbRole.addItem("manager");
        
        cbStatus.removeAllItems();
        cbStatus.addItem("aktif");
        cbStatus.addItem("tidak aktif");
    }
    

private void element(){
    List<Component> tabOrder = Arrays.asList(
    tfUsername,
    tfPassword,
    cbRole,
    cbStatus            
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
   private void loadRiwayatData() {
        DefaultTableModel model = (DefaultTableModel) tblRiwayat.getModel();
        model.setRowCount(0);

        int bulan = jmcBulan.getMonth() + 1; // karena index dimulai dari 0
        int tahun = jycTahun.getYear();
        String selectedUser = (String) cmbUsername.getSelectedItem();
        String keyword = txtCari.getText().trim();

        StringBuilder sql = new StringBuilder(
            "SELECT waktu_login, nama_pemakai, username FROM riwayat_login WHERE EXTRACT(MONTH FROM waktu_login)=? AND EXTRACT(YEAR FROM waktu_login)=? "
        );

        if (selectedUser != null && !"Semua".equals(selectedUser)) {
            sql.append(" AND username = ? ");
        }

        if (!keyword.isEmpty()) {
            sql.append(" AND (LOWER(nama_pemakai) LIKE ? OR LOWER(username) LIKE ?) ");
        }

        sql.append(" ORDER BY waktu_login DESC");

        try (Connection conn = koneksi.dbKonek()) {
            PreparedStatement pst = conn.prepareStatement(sql.toString());
            int paramIndex = 1;

            pst.setInt(paramIndex++, bulan);
            pst.setInt(paramIndex++, tahun);

            if (selectedUser != null && !"Semua".equals(selectedUser)) {
                pst.setString(paramIndex++, selectedUser);
            }

            if (!keyword.isEmpty()) {
                String like = "%" + keyword.toLowerCase() + "%";
                pst.setString(paramIndex++, like);
                pst.setString(paramIndex++, like);
            }

            ResultSet rs = pst.executeQuery();
            int no = 1;
            while (rs.next()) {
                Object[] row = {
                    no++,
                    rs.getTimestamp("waktu_login"),
                    rs.getString("nama_pemakai"),
                    rs.getString("username")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load riwayat: " + e.getMessage());
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnback = new javax.swing.JPanel();
        pnFormUser = new javax.swing.JPanel();
        lStatus = new javax.swing.JLabel();
        lUsername = new javax.swing.JLabel();
        lPass = new javax.swing.JLabel();
        tfUsername = new javax.swing.JTextField();
        tfPassword = new javax.swing.JTextField();
        cbStatus = new javax.swing.JComboBox<>();
        lRole = new javax.swing.JLabel();
        cbRole = new javax.swing.JComboBox<>();
        btSubmit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pnDaftarUser = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblUser = new javax.swing.JTable();
        btDelete = new javax.swing.JButton();
        btEdit = new javax.swing.JButton();
        Batal = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbUsername = new javax.swing.JComboBox<>();
        jmcBulan = new com.toedter.calendar.JMonthChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRiwayat = new javax.swing.JTable();
        jycTahun = new com.toedter.calendar.JYearChooser();
        txtCari = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1720, 960));
        setPreferredSize(new java.awt.Dimension(1720, 960));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnback.setBackground(new java.awt.Color(255, 255, 255));
        pnback.setPreferredSize(new java.awt.Dimension(1740, 960));
        pnback.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnFormUser.setBackground(new java.awt.Color(255, 255, 255));
        pnFormUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnFormUser.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lStatus.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lStatus.setText("Status :");
        pnFormUser.add(lStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 60, 30));

        lUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lUsername.setText("Username :");
        pnFormUser.add(lUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        lPass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lPass.setText("Password :");
        pnFormUser.add(lPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        tfUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pnFormUser.add(tfUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 260, 60));

        tfPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pnFormUser.add(tfPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 260, 60));

        cbStatus.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnFormUser.add(cbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 350, 260, 50));

        lRole.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lRole.setText("Role : ");
        pnFormUser.add(lRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 50, -1));

        cbRole.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRoleActionPerformed(evt);
            }
        });
        pnFormUser.add(cbRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 260, 50));

        btSubmit.setBackground(new java.awt.Color(102, 255, 102));
        btSubmit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btSubmit.setText("SUBMIT");
        btSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSubmitActionPerformed(evt);
            }
        });
        btSubmit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btSubmitKeyReleased(evt);
            }
        });
        pnFormUser.add(btSubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 640, 310, 60));

        jPanel1.setBackground(new java.awt.Color(5, 69, 162));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 27)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Kelola Pengguna");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        pnFormUser.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 70));

        pnback.add(pnFormUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 460, 820));

        pnDaftarUser.setBackground(new java.awt.Color(255, 255, 255));
        pnDaftarUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnDaftarUser.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "ID User", "Username", "Password", "Status"
            }
        ));
        tblUser.setRowHeight(30);
        jScrollPane2.setViewportView(tblUser);
        if (tblUser.getColumnModel().getColumnCount() > 0) {
            tblUser.getColumnModel().getColumn(0).setMaxWidth(45);
            tblUser.getColumnModel().getColumn(1).setMaxWidth(45);
        }

        pnDaftarUser.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 1080, 210));

        btDelete.setBackground(new java.awt.Color(255, 51, 51));
        btDelete.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btDelete.setText("Delete");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });
        pnDaftarUser.add(btDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 80, 90, 40));

        btEdit.setBackground(new java.awt.Color(255, 153, 51));
        btEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btEdit.setText("Edit");
        btEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditActionPerformed(evt);
            }
        });
        pnDaftarUser.add(btEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 80, 90, 40));

        Batal.setBackground(new java.awt.Color(204, 204, 204));
        Batal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Batal.setText("Batal");
        Batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BatalActionPerformed(evt);
            }
        });
        pnDaftarUser.add(Batal, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 80, 90, 40));

        jPanel2.setBackground(new java.awt.Color(5, 69, 162));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 27)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Daftar Pengguna");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        pnDaftarUser.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1110, 70));

        pnback.add(pnDaftarUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 1110, 360));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(5, 69, 162));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 27)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Riwayat Pengguna");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 70));

        cmbUsername.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbUsernameActionPerformed(evt);
            }
        });
        jPanel3.add(cmbUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 120, 40));

        jmcBulan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jmcBulanPropertyChange(evt);
            }
        });
        jPanel3.add(jmcBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 140, 40));

        tblRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "tanggal", "Nama Pengguna", "username"
            }
        ));
        tblRiwayat.setRowHeight(30);
        jScrollPane1.setViewportView(tblRiwayat);
        if (tblRiwayat.getColumnModel().getColumnCount() > 0) {
            tblRiwayat.getColumnModel().getColumn(0).setMaxWidth(45);
        }

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 1090, 280));
        jPanel3.add(jycTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, 40));

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });
        jPanel3.add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 90, 410, 50));

        pnback.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 390, 1110, 450));

        add(pnback, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1740, 920));

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel3.setText("Kelola User");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditActionPerformed
        // TODO add your handling code here:
         int row = tblUser.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!");
                return;
            }
            
            // ambil data dari tabel
            int id = Integer.parseInt(tblUser.getValueAt(row, 1).toString());
            String username = tblUser.getValueAt(row, 2).toString();
            String role     = tblUser.getValueAt(row, 3).toString();
            String status   = tblUser.getValueAt(row, 4).toString();
            
            // isi form
            tfUsername.setText(username);
            tfPassword.setText(""); // kosongkan (password tidak ditampilkan)
            cbRole.setSelectedItem(role);
            cbStatus.setSelectedItem(status);
            
            editMode = true;
            editId = id;
    }//GEN-LAST:event_btEditActionPerformed

    private void cbRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbRoleActionPerformed

    private void btSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSubmitActionPerformed
        // TODO add your handling code here:
            String username = tfUsername.getText().trim();
            String password = tfPassword.getText().trim();
            String role     = cbRole.getSelectedItem().toString();
            String status   = cbStatus.getSelectedItem().toString();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "Username dan Password tidak boleh kosong!");
                return;
            }
            
            try (Connection conn = koneksi.dbKonek()) {
                if (!editMode) { 
                    // mode tambah user baru
                    String sql = "INSERT INTO pengguna (username, password, role, status) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ps.setString(3, role);
                    ps.setString(4, status);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User berhasil ditambahkan!");
                    
                } else {
                    // mode edit user
                    String sql = "UPDATE pengguna SET username=?, password=?, role=?, status=? WHERE idpengguna=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ps.setString(3, role);
                    ps.setString(4, status);
                    ps.setInt(5, editId);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User berhasil diupdate!");
                    
                    editMode = false; 
                    editId = -1;
                }
                
                // reset form
                tfUsername.setText("");
                tfPassword.setText("");
                cbRole.setSelectedIndex(0);
                cbStatus.setSelectedIndex(0);
                
                tampilData(); // refresh tabel
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        
    }//GEN-LAST:event_btSubmitActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        // TODO add your handling code here:
         int row = tblUser.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
                return;
            }
            
            int id = Integer.parseInt(tblUser.getValueAt(row, 1).toString());
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Yakin ingin menghapus user ini?", 
                    "Konfirmasi Hapus", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = koneksi.dbKonek()) {
                    String sql = "DELETE FROM pengguna WHERE idpengguna=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User berhasil dihapus!");
                    tampilData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error hapus: " + ex.getMessage());
                }
            }
    }//GEN-LAST:event_btDeleteActionPerformed

    private void BatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BatalActionPerformed
    tfUsername.setText("");
    tfPassword.setText("");
    cbRole.setSelectedIndex(0);  
    cbStatus.setSelectedIndex(0); 
    tblUser.clearSelection();

    }//GEN-LAST:event_BatalActionPerformed

    private void cmbUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUsernameActionPerformed
loadRiwayatData();        // TODO add your handling code here:
    }//GEN-LAST:event_cmbUsernameActionPerformed

    private void jmcBulanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jmcBulanPropertyChange
loadRiwayatData();        // TODO add your handling code here:
    }//GEN-LAST:event_jmcBulanPropertyChange

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
                loadRiwayatData();
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariKeyReleased

    private void btSubmitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btSubmitKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btSubmitKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Batal;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btEdit;
    private javax.swing.JButton btSubmit;
    private javax.swing.JComboBox<String> cbRole;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JComboBox<String> cmbUsername;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JMonthChooser jmcBulan;
    private com.toedter.calendar.JYearChooser jycTahun;
    private javax.swing.JLabel lPass;
    private javax.swing.JLabel lRole;
    private javax.swing.JLabel lStatus;
    private javax.swing.JLabel lUsername;
    private javax.swing.JPanel pnDaftarUser;
    private javax.swing.JPanel pnFormUser;
    private javax.swing.JPanel pnback;
    private javax.swing.JTable tblRiwayat;
    private javax.swing.JTable tblUser;
    private javax.swing.JTextField tfPassword;
    private javax.swing.JTextField tfUsername;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
