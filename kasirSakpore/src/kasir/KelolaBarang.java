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
        tampilDataJenis();
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
        
  btnSubmit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK),
    "submitAction"
);

btnSubmit.getActionMap().put("submitAction", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnSubmit.doClick();
    }
});

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

        btnJEdit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK),
    "ctrlT"
);
        

btnJEdit.getActionMap().put("ctrlT", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnJEdit.doClick(); // Menjalankan aksi tombol
    }
});

        btnJDelete.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK),
    "ctrlY"
);
        

btnJDelete.getActionMap().put("ctrlY", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnJDelete.doClick(); // Menjalankan aksi tombol
    }
});

       btnJBatal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK),
    "ctrlR"
);
        

btnJBatal.getActionMap().put("ctrlR", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnJBatal.doClick(); // Menjalankan aksi tombol
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


    private boolean modeEdit = false; // penanda apakah sedang edit
    private int idEdit = -1; // menyimpan id kategori yang sedang diedit
 // === FUNGSI MENAMPILKAN DATA ===
    private void tampilDataJenis() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama Kategori");
        tblKategori.setModel(model);

        String cari = txtJCari.getText();
        String sql = "SELECT * FROM kategori WHERE namakategori ILIKE ? ORDER BY idkategori ASC";

        try (Connection conn = koneksi.dbKonek();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, "%" + cari + "%");
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("idkategori"),
                    rs.getString("namakategori")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan data: " + e.getMessage());
        }
    }

    // === FUNGSI SIMPAN / UPDATE ===
    private void simpanAtauEdit() {
        String nama = txtJenis.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kategori harus diisi!");
            return;
        }

        if (modeEdit) {
            // MODE EDIT
     String sqlUpdateKategori = "UPDATE kategori SET namakategori=? WHERE idkategori=?";
String sqlUpdateBarang   = "UPDATE barang SET kategori=? WHERE kategori=?";

try (Connection conn = koneksi.dbKonek()) {
    conn.setAutoCommit(false); // supaya bisa rollback kalau ada error

    // 1️⃣ Ambil nama lama dulu sebelum update
    String namaLama = null;
    try (PreparedStatement pstOld = conn.prepareStatement("SELECT namakategori FROM kategori WHERE idkategori=?")) {
        pstOld.setInt(1, idEdit);
        ResultSet rsOld = pstOld.executeQuery();
        if (rsOld.next()) {
            namaLama = rsOld.getString("namakategori");
        }
    }

    // 2️⃣ Update kategori
    try (PreparedStatement pst = conn.prepareStatement(sqlUpdateKategori)) {
        pst.setString(1, nama);
        pst.setInt(2, idEdit);
        pst.executeUpdate();
    }

    // 3️⃣ Update semua barang yang punya kategori lama
    if (namaLama != null) {
        try (PreparedStatement pst2 = conn.prepareStatement(sqlUpdateBarang)) {
            pst2.setString(1, nama);
            pst2.setString(2, namaLama);
            pst2.executeUpdate();
        }
    }

    conn.commit();
    JOptionPane.showMessageDialog(this, "Kategori dan data barang terkait berhasil diperbarui!");
    batal();
    tampilData();
    tampilDataJenis();
        loadKategori(); // ⬅️ Tambahkan ini

} catch (SQLException e) {
    JOptionPane.showMessageDialog(this, "Gagal mengedit: " + e.getMessage());
}

        } else {
            // MODE SIMPAN BARU
            String sql = "INSERT INTO kategori (namakategori) VALUES (?)";
            try (Connection conn = koneksi.dbKonek();
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, nama);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data kategori berhasil disimpan!");
                batal();
    tampilData();
    tampilDataJenis();
        loadKategori(); // ⬅️ Tambahkan ini

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + e.getMessage());
            }
        }
    }

    // === FUNGSI HAPUS ===
    private void hapusData() {
        int row = tblKategori.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
            return;
        }

int id = Integer.parseInt(tblKategori.getValueAt(row, 0).toString().trim());
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM kategori WHERE idkategori=?";
            try (Connection conn = koneksi.dbKonek();
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                 batal();
    tampilData();
    tampilDataJenis();
        loadKategori(); // ⬅️ Tambahkan ini

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus: " + e.getMessage());
            }
        }
    }

    // === FUNGSI BATAL / RESET ===
    private void batal() {
        txtJenis.setText("");
        txtJCari.setText("");
        tblKategori.clearSelection();
        modeEdit = false;
        idEdit = -1;
        btnJenis.setText("Simpan");
        tampilDataJenis();
    }

    // === FUNGSI EDIT (MENGISI FORM SAJA) ===
    private void isiUntukEdit() {
        int row = tblKategori.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit di tabel!");
            return;
        }

idEdit = Integer.parseInt(tblKategori.getValueAt(row, 0).toString().trim());
        String nama = tblKategori.getValueAt(row, 1).toString();

        txtJenis.setText(nama);
        modeEdit = true;
        btnJenis.setText("Simpan Perubahan");
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        panelGrafik = new javax.swing.JPanel();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtJenis = new javax.swing.JTextField();
        btnJenis = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblKategori = new javax.swing.JTable();
        btnJEdit = new javax.swing.JButton();
        btnJDelete = new javax.swing.JButton();
        btnJBatal = new javax.swing.JButton();
        txtJCari = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        pnForm.add(btnSubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 270, 40));

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

        add(pnForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 14, -1, 318));

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

        pnDaftarBarang.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 800, 280));
        pnDaftarBarang.add(tCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 300, 40));

        jLabel12.setText("Cari");
        pnDaftarBarang.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 45, 34));

        cbfJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        pnDaftarBarang.add(cbfJenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, 90, 40));

        btnEdit.setBackground(new java.awt.Color(255, 153, 51));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        pnDaftarBarang.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 100, 83, 36));

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        pnDaftarBarang.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 100, 83, 36));

        btnBatal.setBackground(new java.awt.Color(204, 204, 204));
        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        pnDaftarBarang.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 100, 83, 36));

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

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jLabel1.setText("[ Ctrl+B ]");
        pnDaftarBarang.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 80, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jLabel2.setText("[ Ctrl+E ]");
        pnDaftarBarang.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jLabel11.setText("[ Ctrl+D ]");
        pnDaftarBarang.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 80, -1, -1));

        add(pnDaftarBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(841, 14, -1, 450));

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
            .addGap(0, 370, Short.MAX_VALUE)
        );

        jPanel1.add(panelGrafik, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 80, 780, 370));

        jdcStart.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcStartPropertyChange(evt);
            }
        });
        jPanel1.add(jdcStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 147, 31));

        jdcEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcEndPropertyChange(evt);
            }
        });
        jPanel1.add(jdcEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, 147, 31));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 346, 806, 458));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setPreferredSize(new java.awt.Dimension(408, 458));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(5, 69, 162));
        jPanel4.setPreferredSize(new java.awt.Dimension(806, 100));

        jLabel16.setFont(new java.awt.Font("Calibri", 1, 27)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Tambah Kategori Barang");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addContainerGap())
        );

        jPanel5.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 410, 50));
        jPanel5.add(txtJenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 290, 100));

        btnJenis.setBackground(new java.awt.Color(102, 255, 102));
        btnJenis.setText("[ Ctrl+S ]");
        btnJenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJenisActionPerformed(evt);
            }
        });
        jPanel5.add(btnJenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 260, 40));

        add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(841, 474, 330, 330));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setPreferredSize(new java.awt.Dimension(408, 458));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(5, 69, 162));
        jPanel7.setPreferredSize(new java.awt.Dimension(806, 100));

        jLabel17.setFont(new java.awt.Font("Calibri", 1, 27)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Daftar Kategori Barang");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addContainerGap())
        );

        jPanel6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 490, 50));

        tblKategori.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "No", "NamaKategori"
            }
        ));
        jScrollPane2.setViewportView(tblKategori);
        if (tblKategori.getColumnModel().getColumnCount() > 0) {
            tblKategori.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        jPanel6.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 460, 180));

        btnJEdit.setBackground(new java.awt.Color(255, 153, 0));
        btnJEdit.setText("Edit");
        btnJEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJEditActionPerformed(evt);
            }
        });
        jPanel6.add(btnJEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, -1, -1));

        btnJDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnJDelete.setText("Delete");
        btnJDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJDeleteActionPerformed(evt);
            }
        });
        jPanel6.add(btnJDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, -1, -1));

        btnJBatal.setBackground(new java.awt.Color(204, 204, 204));
        btnJBatal.setText("Batal");
        jPanel6.add(btnJBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 90, -1, -1));

        txtJCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJCariKeyReleased(evt);
            }
        });
        jPanel6.add(txtJCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 190, 30));

        jLabel14.setText("[ Ctrl+R ]");
        jPanel6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, -1, -1));

        jLabel18.setText("[ Ctrl+C ]");
        jPanel6.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        jLabel19.setText("[ Ctrl+Y ]");
        jPanel6.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, -1, -1));

        jLabel20.setText("[ Ctrl+T ]");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, -1, -1));

        jLabel21.setText("Cari");
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 45, 34));

        add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 474, 480, 330));
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

    private void btnJEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJEditActionPerformed
        isiUntukEdit();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnJEditActionPerformed

    private void btnJDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJDeleteActionPerformed
        hapusData();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnJDeleteActionPerformed

    private void btnJenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJenisActionPerformed
        simpanAtauEdit();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnJenisActionPerformed

    private void txtJCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJCariKeyReleased
        tampilDataJenis();
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJCariKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnJBatal;
    private javax.swing.JButton btnJDelete;
    private javax.swing.JButton btnJEdit;
    private javax.swing.JButton btnJenis;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<String> cbJenis;
    private javax.swing.JComboBox<String> cbfJenis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
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
    private javax.swing.JTable tblKategori;
    private javax.swing.JTextField txtJCari;
    private javax.swing.JTextField txtJenis;
    // End of variables declaration//GEN-END:variables
}
