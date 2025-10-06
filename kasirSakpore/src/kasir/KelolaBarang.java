/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author yaniyan
 */
public class KelolaBarang extends javax.swing.JPanel {

    private boolean editMode = false; 
    private int editId = -1;
    /**
     * Creates new form KelolaBarang
     */
    public KelolaBarang() {
        initComponents();
        tampilData();
        element();
        loadKategori();
        filterKategori();
        generateKodeBarang();
        
        tPajak.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                hitungHargaJual();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                hitungHargaJual();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                hitungHargaJual();
            }
        });

        tHargaPokok.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { hitungHargaJual(); }
            @Override
            public void removeUpdate(DocumentEvent e) { hitungHargaJual(); }
            @Override
            public void changedUpdate(DocumentEvent e) { hitungHargaJual(); }
        });

        tCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                cariBarang();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                cariBarang();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                cariBarang();
            }
        });
        
        
        btTambah.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "tambah");
        btTambah.getActionMap().put("tambah", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            btTambah.doClick(); // seakan tombol diklik
        }
        });
        
        btEdit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl E"), "edit");
        btEdit.getActionMap().put("edit", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
        btEdit.doClick();
        }
        });

        btDelete.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl D"), "hapus");
        btDelete.getActionMap().put("hapus", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
        btDelete.doClick();
        }
        });
        
        DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tblBarang.setRowSorter(sorter);
        cbfJenis.addActionListener(e -> {
        String kategori = cbfJenis.getSelectedItem().toString();
        if (kategori.equals("Semua")) {
        sorter.setRowFilter(null); // tampilkan semua
        } else {
        sorter.setRowFilter(RowFilter.regexFilter(kategori, 4)); 
        // angka 2 = index kolom kategori di tabel
        }
        });
    }
    
    private void hitungHargaJual() {
    try {
        int hargaPokok = Integer.parseInt(tHargaPokok.getText().trim());
        int pajak = Integer.parseInt(tPajak.getText().trim());

        int hargaJual = hargaPokok + (hargaPokok * pajak / 100);
        tHargaJual.setText(String.valueOf(hargaJual));

    } catch (NumberFormatException ex) {
        // kalau textfield kosong atau bukan angka
        tHargaJual.setText("");
    }
}
    
    private void resetForm() {
        tKdBarang.setText("");
        tSKU.setText("");
        tNamaBarang.setText("");
        cbJenis.setSelectedIndex(0);
        tSatuan.setText("");
        tHargaPokok.setText("");
        tPajak.setText("");
        tHargaJual.setText("");
    }
    
    private void cariBarang() {
    String key = tCari.getText().trim();
    
    // Kosongkan tabel sebelum isi ulang
    DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
    model.setRowCount(0);

    // Query dengan multi kolom + CAST untuk integer
    String sql = "SELECT kodebarang, skubarang, nama, kategori, satuan, " +
                 "hargapokok, ppn, hargabarang, stok " +
                 "FROM barang " +
                 "WHERE CAST(kodebarang AS TEXT) ILIKE ? " +
                 "OR skubarang ILIKE ? " +
                 "OR nama ILIKE ? " +
                 "OR kategori ILIKE ? " +
                 "OR satuan ILIKE ? " +
                 "OR CAST(hargapokok AS TEXT) ILIKE ? " +
                 "OR CAST(ppn AS TEXT) ILIKE ? " +
                 "OR CAST(hargabarang AS TEXT) ILIKE ? " +
                 "OR CAST(stok AS TEXT) ILIKE ? ";
                 

    try (Connection conn = koneksi.dbKonek();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        // Set semua parameter dengan key pencarian
        for (int i = 1; i <= 9; i++) {
            pst.setString(i, "%" + key + "%");
        }

        ResultSet rs = pst.executeQuery();
        int no = 1;
        while (rs.next()) {
            Object[] row = {
                no++,
                rs.getInt("kodebarang"),
                rs.getString("skubarang"),
                rs.getString("nama"),
                rs.getString("kategori"),
                rs.getString("satuan"),
                rs.getInt("hargapokok"),
                rs.getInt("ppn"),
                rs.getInt("hargabarang"),
                rs.getInt("stok")
            };
            model.addRow(row);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error pencarian: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    private void loadKategori() {
    cbJenis.removeAllItems(); // kosongkan dulu
    try (Connection conn = koneksi.dbKonek()) {
        String sql = "SELECT namakategori FROM kategori ORDER BY namakategori ASC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            cbJenis.addItem(rs.getString("namakategori"));
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gagal load kategori: " + ex.getMessage());
        ex.printStackTrace();
    }
    }
    
    private void filterKategori() {    
    try (Connection conn = koneksi.dbKonek()) {
        String sql = "SELECT namakategori FROM kategori ORDER BY namakategori ASC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            cbfJenis.addItem(rs.getString("namaKategori"));
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gagal load kategori: " + ex.getMessage());
        ex.printStackTrace();
    }
    }
    
    private void generateKodeBarang() {
    Connection conn = null;
    PreparedStatement pst = null;    
    ResultSet rs = null;

    try {
        conn = koneksi.dbKonek(); // ganti sesuai class koneksi Anda

        String sql = "SELECT MAX(kodebarang) AS kodeTerbesar FROM barang";
        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        if (rs.next()) {
            int kodeTerbesar = rs.getInt("kodeTerbesar");
            int kodeBaru = kodeTerbesar + 1; 

            tKdBarang.setText(String.valueOf(kodeBaru));
        } else {
            // Jika tabel masih kosong → mulai dari 1
            tKdBarang.setText("1");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error generate kode barang: " + e.getMessage());
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (pst != null) pst.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}

    // tampilkan data user di tabel
    private void tampilData() {
        DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
        model.setRowCount(0); // hapus semua baris lama

        try (Connection conn = kasir.koneksi.dbKonek()) {
            String sql = "SELECT * FROM barang ORDER BY kodebarang ASC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    
                    rs.getInt("kodebarang"),
                    rs.getString("skubarang"),
                    rs.getString("nama"),
                    rs.getString("kategori"),
                    rs.getString("satuan"),
                    rs.getInt("hargapokok"),
                    rs.getInt("ppn"),
                    rs.getInt("hargabarang"),
                    rs.getInt("stok"),
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error tampil data: " + ex.getMessage());
        }
    }
private void element(){
    List<Component> tabOrder = Arrays.asList(
    tKdBarang,
    cbJenis,
    tHargaPokok,
    tPajak,
    tSKU,
    tSatuan,
    tNamaBarang,
    tHargaJual,
    tCari,
    cbfJenis
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

        jLabel1 = new javax.swing.JLabel();
        pnForm = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tKdBarang = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbJenis = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        tHargaPokok = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tPajak = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tSKU = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tSatuan = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tNamaBarang = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tHargaJual = new javax.swing.JTextField();
        btTambah = new javax.swing.JButton();
        pnDaftarBarang = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBarang = new javax.swing.JTable();
        tCari = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cbfJenis = new javax.swing.JComboBox<>();
        btEdit = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel1.setText("Kelola Barang");

        pnForm.setBackground(new java.awt.Color(255, 255, 255));
        pnForm.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("Input Barang");

        jLabel3.setText("Kode Barang");

        tKdBarang.setEditable(false);

        jLabel4.setText("Kategori");

        cbJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Harga Pokok");

        jLabel6.setText("PPN");

        jLabel7.setText("SKU Barang");

        jLabel8.setText("Satuan");

        jLabel9.setText("Nama Barang");

        jLabel10.setText("Harga Jual");

        btTambah.setBackground(new java.awt.Color(255, 255, 51));
        btTambah.setText("SUBMIT");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnFormLayout = new javax.swing.GroupLayout(pnForm);
        pnForm.setLayout(pnFormLayout);
        pnFormLayout.setHorizontalGroup(
            pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFormLayout.createSequentialGroup()
                .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnFormLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnFormLayout.createSequentialGroup()
                                .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(tKdBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)
                                    .addComponent(tSKU, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnFormLayout.createSequentialGroup()
                                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cbJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4))
                                        .addGap(18, 18, 18)
                                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(tHargaPokok, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(tPajak, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(pnFormLayout.createSequentialGroup()
                                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(tSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9)
                                            .addComponent(tNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addComponent(tHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnFormLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnFormLayout.setVerticalGroup(
            pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFormLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFormLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tKdBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(117, 117, 117))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFormLayout.createSequentialGroup()
                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnFormLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tHargaPokok, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnFormLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tPajak, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnFormLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tSKU, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnFormLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnFormLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnFormLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(33, 33, 33)))
                .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );

        pnDaftarBarang.setBackground(new java.awt.Color(255, 255, 255));
        pnDaftarBarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setText("Daftar Barang");

        tblBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No. ", "Kode Barang", "SKU Barang", "Nama Barang", "Kategori", "Satuan", "Harga Pokok", "PPN", "Harga Jual", "Stok"
            }
        ));
        jScrollPane1.setViewportView(tblBarang);

        jLabel12.setText("Cari");

        cbfJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));

        btEdit.setBackground(new java.awt.Color(255, 153, 51));
        btEdit.setText("Edit");
        btEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditActionPerformed(evt);
            }
        });

        btDelete.setBackground(new java.awt.Color(255, 51, 51));
        btDelete.setText("Delete");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        btCancel.setBackground(new java.awt.Color(204, 204, 204));
        btCancel.setText("Batal");
        btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnDaftarBarangLayout = new javax.swing.GroupLayout(pnDaftarBarang);
        pnDaftarBarang.setLayout(pnDaftarBarangLayout);
        pnDaftarBarangLayout.setHorizontalGroup(
            pnDaftarBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDaftarBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnDaftarBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnDaftarBarangLayout.createSequentialGroup()
                        .addGroup(pnDaftarBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(pnDaftarBarangLayout.createSequentialGroup()
                                .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbfJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(891, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        pnDaftarBarangLayout.setVerticalGroup(
            pnDaftarBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDaftarBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addGroup(pnDaftarBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tCari, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbfJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(pnForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnDaftarBarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(pnForm, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnDaftarBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditActionPerformed
        // TODO add your handling code here:
        int row = tblBarang.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!");
                return;
            }
            
            // ambil data dari tabel
            int kode = Integer.parseInt(tblBarang.getValueAt(row, 1).toString());
            String sku = tblBarang.getValueAt(row, 2).toString();
            String nama     = tblBarang.getValueAt(row, 3).toString();
            String kategori   = tblBarang.getValueAt(row, 4).toString();
            String satuan   = tblBarang.getValueAt(row, 5).toString();
            int hargapokok = Integer.parseInt(tblBarang.getValueAt(row, 6).toString());
            int ppn = Integer.parseInt(tblBarang.getValueAt(row, 7).toString());
            int hargajual = Integer.parseInt(tblBarang.getValueAt(row, 8).toString());
            
            
            // isi form
            tKdBarang.setText(String.valueOf(kode));
            tSKU.setText(sku);
            tNamaBarang.setText(nama);
            cbJenis.setSelectedItem(kategori);
            tSatuan.setText(satuan);
            tHargaPokok.setText(String.valueOf(hargapokok));
            tPajak.setText(String.valueOf(ppn));
            tHargaJual.setText(String.valueOf(hargajual));
            
            editMode = true;
            editId = kode;
    }//GEN-LAST:event_btEditActionPerformed

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
        // TODO add your handling code here:
            int kode = Integer.parseInt(tKdBarang.getText().trim());
            String skubarang = tSKU.getText().trim();
            String nama     = tNamaBarang.getText().trim();
            String kategori   = cbJenis.getSelectedItem().toString();
            String satuan = tSatuan.getText().trim();
            int hargapokok = Integer.parseInt(tHargaPokok.getText().trim());
            int ppn        = Integer.parseInt(tPajak.getText().trim());
            int hargajual  = Integer.parseInt(tHargaJual.getText().trim());


            // ambil path lengkap dari textfield
          
            try (Connection conn = koneksi.dbKonek()) {
               
                if (!editMode) { 
                    // mode tambah user baru
                    String sql = "INSERT INTO barang (kodebarang, skubarang, nama, hargabarang, kategori, hargapokok, ppn, satuan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, kode);
                    ps.setString(2, skubarang);
                    ps.setString(3, nama);
                    ps.setInt(4, hargajual);
                    ps.setString(5, kategori);
                    ps.setInt(6, hargapokok);
                    ps.setInt(7, ppn);
                    ps.setString(8, satuan);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan!");
                    
                    
                    
                    
                } else {
                    // mode edit user
                    String sql = "UPDATE barang SET nama=?, kategori=?, satuan=?, hargapokok=?, hargabarang=?, skubarang=? WHERE kodebarang=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, nama);
                    ps.setString(2, kategori);
                    ps.setString(3, satuan);
                    ps.setInt(4, hargapokok);
                    ps.setInt(5, hargajual);
                    ps.setString(6, skubarang);
                    ps.setInt(7, kode);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Barang berhasil diupdate!");
                    
                    editMode = false; 
                    editId = -1;
                }
                
                // reset form
                resetForm();
                tampilData(); // refresh tabel
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
    }//GEN-LAST:event_btTambahActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        // TODO add your handling code here:
        int row = tblBarang.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
                return;
            }
            
            int id = Integer.parseInt(tblBarang.getValueAt(row, 1).toString());
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Yakin ingin menghapus barang ini?", 
                    "Konfirmasi Hapus", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = koneksi.dbKonek()) {
                    String sql = "DELETE FROM barang WHERE kodebarang=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Barang berhasil dihapus!");
                    tampilData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error hapus: " + ex.getMessage());
                }
            }
            
            tampilData();
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        // TODO add your handling code here:
        resetForm();
        tblBarang.clearSelection();
    }//GEN-LAST:event_btCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btEdit;
    private javax.swing.JButton btTambah;
    private javax.swing.JComboBox<String> cbJenis;
    private javax.swing.JComboBox<String> cbfJenis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnDaftarBarang;
    private javax.swing.JPanel pnForm;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tHargaJual;
    private javax.swing.JTextField tHargaPokok;
    private javax.swing.JTextField tKdBarang;
    private javax.swing.JTextField tNamaBarang;
    private javax.swing.JTextField tPajak;
    private javax.swing.JTextField tSKU;
    private javax.swing.JTextField tSatuan;
    private javax.swing.JTable tblBarang;
    // End of variables declaration//GEN-END:variables
}
