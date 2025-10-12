/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        tampilkanDiagram();
panelGrafik.setLayout(new BorderLayout());
setPeriodeHariIni();
// ambil tanggal hari ini
LocalDate today = LocalDate.now();
// ambil tanggal awal bulan ini
String startDate = today.withDayOfMonth(1).toString();
// ambil tanggal hari ini (akhir rentang default)
String endDate = today.toString();

loadChart(startDate, endDate);

        
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
        
        
        btnSubmit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "tambah");
        btnSubmit.getActionMap().put("tambah", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            btnSubmit.doClick(); // seakan tombol diklik
        }
        });
        
        btnEdit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl E"), "edit");
        btnEdit.getActionMap().put("edit", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
        btnEdit.doClick();
        }
        });

        btnDelete.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl D"), "hapus");
        btnDelete.getActionMap().put("hapus", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
        btnDelete.doClick();
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
        
        btnSubmit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
    "ctrlS"
);
        

btnSubmit.getActionMap().put("ctrlS", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnSubmit.doClick(); // Menjalankan aksi tombol
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
setKeyBindings();
// Misalnya tabel kamu bernama table1
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
                tblBarang.requestFocus();
                if (tblBarang.getRowCount() > 0) {
                    tblBarang.setRowSelectionInterval(0, 0);
                    tblBarang.setColumnSelectionInterval(0, 0);
                    tblBarang.editCellAt(0, 0);
                }
            }
        });
    });
}

private void setPeriodeHariIni() {
    java.time.LocalDate today = java.time.LocalDate.now();
    java.util.Date tanggalHariIni = java.sql.Date.valueOf(today);

    // Set JDateChooser ke tanggal hari ini
    jdcStart.setDate(tanggalHariIni);
    jdcEnd.setDate(tanggalHariIni);

    // Tampilkan diagram langsung
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
    
private void tampilkanDiagram() {
  java.util.Date start = jdcStart.getDate();
java.util.Date end = jdcEnd.getDate();
if (start == null || end == null) return;

// tambah 1 hari ke end agar mencakup seluruh hari
Calendar cal = Calendar.getInstance();
cal.setTime(end);
cal.add(Calendar.DATE, 1);
java.util.Date endPlusOne = cal.getTime();

String startDate = new java.sql.Date(start.getTime()).toString();
String endDate = new java.sql.Date(endPlusOne.getTime()).toString();

DiagramBatang diagram = new DiagramBatang(startDate, endDate);

    JScrollPane scrollPane = new JScrollPane(diagram,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    panelGrafik.removeAll();
    panelGrafik.setLayout(new java.awt.BorderLayout());
    panelGrafik.add(scrollPane, java.awt.BorderLayout.CENTER);
    panelGrafik.revalidate();
    panelGrafik.repaint();
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
    private void loadChart(String startDate, String endDate) {
    DiagramBatang chart = new DiagramBatang(startDate, endDate);

    JScrollPane scrollPane = new JScrollPane(chart,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    panelGrafik.removeAll();
    panelGrafik.add(scrollPane, BorderLayout.CENTER);
    panelGrafik.revalidate();
    panelGrafik.repaint();
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

        pnForm = new javax.swing.JPanel();
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
        btnSubmit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        pnDaftarBarang = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBarang = new javax.swing.JTable();
        tCari = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cbfJenis = new javax.swing.JComboBox<>();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        panelGrafik = new javax.swing.JPanel();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jdcEnd = new com.toedter.calendar.JDateChooser();

        setBackground(new java.awt.Color(255, 255, 255));

        pnForm.setBackground(new java.awt.Color(255, 255, 255));
        pnForm.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnForm.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("Kode Barang");
        pnForm.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 81, -1, -1));

        tKdBarang.setEditable(false);
        pnForm.add(tKdBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 103, 180, 38));

        jLabel4.setText("Kategori");
        pnForm.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 81, -1, -1));

        cbJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnForm.add(cbJenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 103, 180, 38));

        jLabel5.setText("Harga Pokok");
        pnForm.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(421, 87, -1, -1));
        pnForm.add(tHargaPokok, new org.netbeans.lib.awtextra.AbsoluteConstraints(421, 109, 180, 38));

        jLabel6.setText("PPN");
        pnForm.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 87, -1, -1));
        pnForm.add(tPajak, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 109, 180, 38));

        jLabel7.setText("SKU Barang");
        pnForm.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 165, -1, -1));
        pnForm.add(tSKU, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 187, 180, 38));

        jLabel8.setText("Satuan");
        pnForm.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 165, -1, -1));
        pnForm.add(tSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 187, 180, 38));

        jLabel9.setText("Nama Barang");
        pnForm.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(421, 165, -1, -1));
        pnForm.add(tNamaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(421, 187, 180, 38));

        jLabel10.setText("Harga Jual");
        pnForm.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 165, -1, -1));
        pnForm.add(tHargaJual, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 187, 180, 38));

        btnSubmit.setBackground(new java.awt.Color(0, 255, 0));
        btnSubmit.setText("SUBMIT");
        btnSubmit.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });
        pnForm.add(btnSubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 297, 50));

        jPanel2.setBackground(new java.awt.Color(5, 69, 162));
        jPanel2.setPreferredSize(new java.awt.Dimension(806, 100));

        jLabel13.setFont(new java.awt.Font("Calibri", 1, 27)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Kelola Barang");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(621, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addContainerGap())
        );

        pnForm.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 60));

        pnDaftarBarang.setBackground(new java.awt.Color(255, 255, 255));
        pnDaftarBarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnDaftarBarang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No. ", "Kode ", "SKU Barang", "Nama Barang", "Kategori", "Satuan", "Harga Pokok", "PPN", "Harga Jual", "Stok"
            }
        ));
        tblBarang.setRowHeight(30);
        jScrollPane1.setViewportView(tblBarang);
        if (tblBarang.getColumnModel().getColumnCount() > 0) {
            tblBarang.getColumnModel().getColumn(0).setMaxWidth(38);
            tblBarang.getColumnModel().getColumn(1).setMaxWidth(45);
            tblBarang.getColumnModel().getColumn(2).setResizable(false);
            tblBarang.getColumnModel().getColumn(5).setMaxWidth(80);
            tblBarang.getColumnModel().getColumn(7).setMaxWidth(40);
            tblBarang.getColumnModel().getColumn(9).setMaxWidth(40);
        }

        pnDaftarBarang.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 808, 650));
        pnDaftarBarang.add(tCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 300, 40));

        jLabel12.setText("Cari");
        pnDaftarBarang.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 45, 34));

        cbfJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        pnDaftarBarang.add(cbfJenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, 90, 40));

        btnEdit.setBackground(new java.awt.Color(255, 153, 51));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        pnDaftarBarang.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, 83, 36));

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        pnDaftarBarang.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, 83, 36));

        btnBatal.setBackground(new java.awt.Color(204, 204, 204));
        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        pnDaftarBarang.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 70, 83, 36));

        jPanel3.setBackground(new java.awt.Color(5, 69, 162));
        jPanel3.setPreferredSize(new java.awt.Dimension(806, 100));

        jLabel15.setFont(new java.awt.Font("Calibri", 1, 27)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Daftar Barang");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(635, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addContainerGap())
        );

        pnDaftarBarang.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 60));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout panelGrafikLayout = new javax.swing.GroupLayout(panelGrafik);
        panelGrafik.setLayout(panelGrafikLayout);
        panelGrafikLayout.setHorizontalGroup(
            panelGrafikLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 780, Short.MAX_VALUE)
        );
        panelGrafikLayout.setVerticalGroup(
            panelGrafikLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );

        jPanel1.add(panelGrafik, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 60, 780, 390));

        jdcStart.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcStartPropertyChange(evt);
            }
        });
        jPanel1.add(jdcStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 147, 31));

        jdcEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcEndPropertyChange(evt);
            }
        });
        jPanel1.add(jdcEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 147, 31));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 806, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(pnDaftarBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnDaftarBarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnForm, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(183, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
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
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
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
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
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
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        resetForm();
        tblBarang.clearSelection();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void jdcStartPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcStartPropertyChange
    tampilkanDiagram();
        // TODO add your handling code here:
    }//GEN-LAST:event_jdcStartPropertyChange

    private void jdcEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcEndPropertyChange
    tampilkanDiagram();
        // TODO add your handling code here:
    }//GEN-LAST:event_jdcEndPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<String> cbJenis;
    private javax.swing.JComboBox<String> cbfJenis;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private javax.swing.JPanel panelGrafik;
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
