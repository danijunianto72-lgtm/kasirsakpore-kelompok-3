/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import kasir.koneksi;

/**
 *
 * @author LABFO-14
 */
public class Suplier extends javax.swing.JPanel {

    /**
     * Creates new form Suplier
     */
       private boolean editMode = false; 
    private int editId = -1; // simpan id user yg diedit
    
    public Suplier() {
        initComponents();
        isiComboBox();
        tampilData();
        element();
        btnSimpan.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
    "ctrlS"
);

btnSimpan.getActionMap().put("ctrlS", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnSimpan.doClick(); // Menjalankan aksi tombol
    }
});


       btnSimpan.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK),
    "SimpanAction"
);

btnSimpan.getActionMap().put("SimpanAction", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnSimpan.doClick();
    }
});
    
btnBatal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK),
    "ctrlB"
);

btnBatal.getActionMap().put("ctrlB", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnBatal.doClick(); // Menjalankan aksi tombol
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


    }

      // tampilkan data user di tabel
    private void tampilData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("No Telepon");
        model.addColumn("Status");
        model.addColumn("Alamat");
        
        try (Connection conn = kasir.koneksi.dbKonek()) {
            String sql = "SELECT *FROM supplier ORDER BY idsupplier ASC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getInt("idsupplier"),
                    rs.getString("namasupplier"),
                    rs.getString("notelp"),
                    rs.getString("status"),
                    rs.getString("alamat")
                });
            }
            tblSuplier.setModel(model);
            
            // sembunyikan kolom ID (jangan ditampilkan ke user)
            tblSuplier.getColumnModel().getColumn(1).setMinWidth(0);
            tblSuplier.getColumnModel().getColumn(1).setMaxWidth(0);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error tampil data: " + ex.getMessage());
        }
    }
    
       // isi combobox role dan status
    private void isiComboBox() {
  
        cmbStatus.removeAllItems();
        cmbStatus.addItem("aktif");
        cmbStatus.addItem("tidak aktif");
    }
    private void element(){
    List<Component> tabOrder = Arrays.asList(
    txtUsername,
    txtTlp,
    cmbStatus,
    txtAlamat        
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSuplier = new javax.swing.JTable();
        btnDelete = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pnFormUser1 = new javax.swing.JPanel();
        lStatus2 = new javax.swing.JLabel();
        lUsername1 = new javax.swing.JLabel();
        lPass1 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        txtAlamat = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        txtTlp = new javax.swing.JTextField();
        lStatus3 = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1740, 960));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnFormUser.setBackground(new java.awt.Color(255, 255, 255));
        pnFormUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnFormUser.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblSuplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "Nama", "No Telepon", "Status ", "Alamat"
            }
        ));
        jScrollPane1.setViewportView(tblSuplier);

        pnFormUser.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 1030, 510));

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        pnFormUser.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 130, 170, 40));

        btnBatal.setBackground(new java.awt.Color(204, 204, 204));
        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        pnFormUser.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 130, 170, 40));

        btnEdit.setBackground(new java.awt.Color(255, 153, 51));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        pnFormUser.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 130, 170, 40));

        jPanel2.setBackground(new java.awt.Color(5, 69, 162));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 30)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Daftar Supplier");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        pnFormUser.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 60));

        jPanel1.add(pnFormUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, 1090, 740));

        pnFormUser1.setBackground(new java.awt.Color(255, 255, 255));
        pnFormUser1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnFormUser1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lStatus2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lStatus2.setText("Alamat :");
        pnFormUser1.add(lStatus2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 70, 30));

        lUsername1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lUsername1.setText("Username :");
        pnFormUser1.add(lUsername1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        lPass1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lPass1.setText("No. Telp");
        pnFormUser1.add(lPass1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, -1, -1));

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aktif", "Tidak aktif" }));
        pnFormUser1.add(cmbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, 260, 40));

        txtAlamat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAlamatActionPerformed(evt);
            }
        });
        pnFormUser1.add(txtAlamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 390, 260, 140));

        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });
        pnFormUser1.add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 260, 60));

        txtTlp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTlpActionPerformed(evt);
            }
        });
        pnFormUser1.add(txtTlp, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 260, 60));

        lStatus3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lStatus3.setText("Status :");
        pnFormUser1.add(lStatus3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 60, 30));

        btnSimpan.setBackground(new java.awt.Color(51, 255, 0));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        pnFormUser1.add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 570, 360, 60));

        jPanel3.setBackground(new java.awt.Color(5, 69, 162));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 30)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Kelola Supplier");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        pnFormUser1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 60));

        jPanel1.add(pnFormUser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 460, 740));

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtAlamatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAlamatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAlamatActionPerformed

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void txtTlpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTlpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTlpActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
         int row = tblSuplier.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
                return;
            }
            
            int id = Integer.parseInt(tblSuplier.getValueAt(row, 1).toString());
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Yakin ingin menghapus user ini?", 
                    "Konfirmasi Hapus", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = koneksi.dbKonek()) {
                    String sql = "DELETE FROM supplier WHERE idsupplier=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User berhasil dihapus!");
                    tampilData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error hapus: " + ex.getMessage());
                }
            }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        String username = txtUsername.getText().trim();
            String telpon = txtTlp.getText().trim();
            String alamat = txtAlamat.getText().trim();
            String status   = cmbStatus.getSelectedItem().toString();
            
           
            
            try (Connection conn = koneksi.dbKonek()) {
                if (!editMode) { 
                    // mode tambah user baru
                    String sql = "INSERT INTO supplier (namasupplier, notelp, status, alamat) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, username);
                    ps.setString(2, telpon);
                    ps.setString(3, status);
                    ps.setString(4, alamat);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User berhasil ditambahkan!");
                    
                } else {
                    // mode edit user
                    String sql = "UPDATE supplier SET namasupplier=?, notelp=?, status=?, alamat=? WHERE idsupplier=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, username);
                    ps.setString(2, telpon);
                    ps.setString(3, status);
                    ps.setString(4, alamat);
                    ps.setInt(5, editId);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User berhasil diupdate!");
                    
                    editMode = false; 
                    editId = -1;
                }
                
                // reset form
                txtUsername.setText("");
                txtTlp.setText("");
                txtAlamat.setText("");
                cmbStatus.setSelectedIndex(0);
                
                tampilData(); // refresh tabel
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
         int row = tblSuplier.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!");
                return;
            }
            
            // ambil data dari tabel
            int id = Integer.parseInt(tblSuplier.getValueAt(row, 1).toString());
            String username = tblSuplier.getValueAt(row, 2).toString();
            String telpon     = tblSuplier.getValueAt(row, 3).toString();
            String status   = tblSuplier.getValueAt(row, 4).toString();
            String alamat = tblSuplier.getValueAt(row, 5).toString();
            
            // isi form
            txtUsername.setText(username);
            txtTlp.setText(telpon);
            txtAlamat.setText(alamat);
            cmbStatus.setSelectedItem(status);
            
            editMode = true;
            editId = id;
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        txtUsername.setText("");
    txtTlp.setText("");
    txtAlamat.setText("");
    cmbStatus.setSelectedIndex(0); 
    tblSuplier.clearSelection();

    }//GEN-LAST:event_btnBatalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lPass1;
    private javax.swing.JLabel lStatus2;
    private javax.swing.JLabel lStatus3;
    private javax.swing.JLabel lUsername1;
    private javax.swing.JPanel pnFormUser;
    private javax.swing.JPanel pnFormUser1;
    private javax.swing.JTable tblSuplier;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtTlp;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
