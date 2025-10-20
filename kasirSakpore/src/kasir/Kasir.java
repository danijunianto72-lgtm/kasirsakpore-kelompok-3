/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;
import java.sql.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.Timer;
import javax.swing.table.JTableHeader;
/**
 *
 * @author yaniyan
 */
public class Kasir extends javax.swing.JPanel {

    /**
     * Creates new form Kasir
     */
        private javax.swing.Timer timer;

    public Kasir() {
        initComponents();
         header();
         
         
       
        loadMenu(null);
        setupSkuScanner(); 
String nama = Session.getNama();
if (nama == null || nama.isEmpty()) {
    nama = Session.getUsername();
}
 loadRiwayatHariIni();
 
txtPengguna.setText(nama);
        jdcTanggal.setDate(new Date());

        try (Connection conn = koneksi.dbKonek()) {
            txtNo.setText(generateNoTransaksi(conn));
        } catch (Exception e) {
            e.printStackTrace();
            txtNo.setText("TERROR");
        }

          SwingUtilities.invokeLater(() -> {
        txtSku.requestFocusInWindow();
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
btnPembayaran.getActionMap().put("ctrlS", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnPembayaran.doClick(); // Menjalankan aksi tombol
    }
});
btnPembayaran.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
    "ctrlS"
);
setKeyBindings();


    }
  
 private void header(){
 JTableHeader header = tblKasir.getTableHeader();
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

 JTableHeader Header = tblRiwayat.getTableHeader();
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
                tblKasir.requestFocus();
                if (tblKasir.getRowCount() > 0) {
                    tblKasir.setRowSelectionInterval(0, 0);
                    tblKasir.setColumnSelectionInterval(0, 0);
                    tblKasir.editCellAt(0, 0);
                }
            }
        });
    });
}
private void loadRiwayatHariIni() {
    DefaultTableModel model = new DefaultTableModel();
model.addColumn("No");
model.addColumn("Tanggal & Jam");
model.addColumn("Total Transaksi");
model.addColumn("ID Transaksi"); // kolom tambahan untuk disembunyikan

    String namaKasir = Session.getNama();
    if (namaKasir == null || namaKasir.isEmpty()) {
        namaKasir = Session.getUsername();
    }

    double totalHariIni = 0.0;

    try (Connection conn = koneksi.dbKonek()) {
        // Query transaksi hari ini untuk kasir login
        String sql = """
            SELECT idtransaksi, tgl_transaksi, grand_total
            FROM transaksi
            WHERE namapengguna = ?
              AND DATE(tgl_transaksi) = CURRENT_DATE
            ORDER BY tgl_transaksi DESC
        """;

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, namaKasir);
        ResultSet rs = pst.executeQuery();

    int no = 1;
while (rs.next()) {
    int idTransaksi = rs.getInt("idtransaksi");
    Timestamp ts = rs.getTimestamp("tgl_transaksi");
    double total = rs.getDouble("grand_total");
    totalHariIni += total;

    String tglFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts);

    model.addRow(new Object[]{no, tglFormat, String.format("Rp %,.2f", total), idTransaksi});
    no++;
}


        tblRiwayat.setModel(model);
tblRiwayat.removeColumn(tblRiwayat.getColumnModel().getColumn(3)); // sembunyikan kolom ID
tblRiwayat.getColumnModel().getColumn(0).setPreferredWidth(40);
tblRiwayat.getColumnModel().getColumn(0).setMaxWidth(50);
tblRiwayat.getColumnModel().getColumn(0).setMinWidth(30);
        // tampilkan total harian
        lblTotal.setText(String.format("Total Hari Ini: Rp %, .2f", totalHariIni));

        if (model.getRowCount() == 0) {
JOptionPane.showMessageDialog(this,"Belum ada transaksi");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
    }
}
private void loadMenu(String keyword) {
    pnlMenu.removeAll();
    pnlMenu.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

    String sql = "SELECT kodeBarang, SKUBarang, nama, hargaBarang, stok, gambar FROM Barang";
    if (keyword != null && !keyword.isEmpty()) {
        sql += " WHERE LOWER(nama) LIKE ?";
    }

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        conn = koneksi.dbKonek();
        if (conn == null || conn.isClosed()) {
            conn = koneksi.dbKonek(); // buka ulang jika tertutup
        }

        ps = conn.prepareStatement(sql);
        if (keyword != null && !keyword.isEmpty()) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
        }

        rs = ps.executeQuery();

        int count = 0;
        while (rs.next()) {
            int kodeBarang = rs.getInt("kodeBarang");
            String sku = rs.getString("SKUBarang");
            String nama = rs.getString("nama");
            double harga = rs.getDouble("hargaBarang");
            int stok = rs.getInt("stok");
            String gambarPath = rs.getString("gambar");
            count++;

            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setPreferredSize(new Dimension(200, 200));

            JLabel lblGambar = new JLabel();
            lblGambar.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblGambar.setPreferredSize(new Dimension(120, 120));
            lblGambar.setHorizontalAlignment(JLabel.CENTER);
            lblGambar.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            if (gambarPath != null && !gambarPath.isEmpty()) {
                ImageIcon icon = new ImageIcon(gambarPath);
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblGambar.setIcon(new ImageIcon(img));
            } else {
                lblGambar.setText("No Image");
            }

            JLabel lblNama = new JLabel(nama);
            lblNama.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblSKU = new JLabel("SKU: " + sku);
            JLabel lblHarga = new JLabel("Rp " + harga);
            JLabel lblStok = new JLabel("Stok: " + stok);
            lblSKU.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblHarga.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblStok.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton btnBeli = new JButton("Beli");
            btnBeli.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnBeli.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

            btnBeli.addActionListener(ev -> {
                String jumlahStr = JOptionPane.showInputDialog(
                        this,
                        "Masukkan jumlah beli untuk " + nama,
                        "Input Jumlah",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (jumlahStr != null && !jumlahStr.isEmpty()) {
                    try {
                        int jumlah = Integer.parseInt(jumlahStr);
                        if (jumlah > 0 && jumlah <= stok) {
                            double total = jumlah * harga;

                            DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();
                            model.addRow(new Object[]{sku, nama, harga, jumlah, total});

                            // Hitung ulang total keseluruhan
                            double grandTotal = 0;
                            for (int i = 0; i < model.getRowCount(); i++) {
                                grandTotal += (double) model.getValueAt(i, 4);
                            }
                            txtTotal.setText(String.valueOf((int) grandTotal));

                            kurangiStok(kodeBarang, jumlah);
                            loadMenu(keyword); // refresh stok di tampilan
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Jumlah tidak valid atau stok tidak cukup!");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Input harus angka!");
                    }
                }
            });

            itemPanel.add(Box.createVerticalStrut(5));
            itemPanel.add(lblNama);
            itemPanel.add(lblSKU);
            itemPanel.add(lblHarga);
            itemPanel.add(lblStok);
            itemPanel.add(Box.createVerticalStrut(5));
            itemPanel.add(btnBeli);

            pnlMenu.add(itemPanel);
        }

        // Hitung tinggi panel berdasarkan jumlah item
        int rows = (int) Math.ceil(count / 3.0); // 3 kolom per baris
        int height = rows * 270; // tinggi per item (250 + margin)
        pnlMenu.setPreferredSize(new Dimension(662, height));

        pnlMenu.revalidate();
        pnlMenu.repaint();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load menu: " + e.getMessage());
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception ex) {}
        try { if (ps != null) ps.close(); } catch (Exception ex) {}
        // ⚠️ Jangan tutup conn, biarkan tetap terbuka (global)
    }
}


private void kurangiStok(int kodeBarang, int jumlah) {
    String sql = "UPDATE Barang SET stok = stok - ? WHERE kodeBarang = ?";

    try (Connection conn = koneksi.dbKonek();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, jumlah);
        ps.setInt(2, kodeBarang);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal update stok: " + e.getMessage());
    }
}
private int getStokBySKU(String sku) {
    String sql = "SELECT stok FROM Barang WHERE SKUBarang = ?";
    try (Connection conn = koneksi.dbKonek();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, sku);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("stok");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}

private void updateStokBySKU(String sku, int jumlah) {
    String sql = "UPDATE Barang SET stok = stok + ? WHERE SKUBarang = ?";
    try (Connection conn = koneksi.dbKonek();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, jumlah);
        ps.setString(2, sku);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal update stok: " + e.getMessage());
    }
}
private void showPopupPembayaran() {
    // Ambil parent dari JPanel
    Window parentWindow = SwingUtilities.getWindowAncestor(this);
    
    // === POPUP DIALOG ===
    JDialog dialog = new JDialog(parentWindow, "Pembayaran", Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setSize(700, 700);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout(10, 10));

   // === Panel Atas: Ringkasan Barang ===
DefaultTableModel modelKasir = (DefaultTableModel) tblKasir.getModel();
JTable tblRingkasan = new JTable(modelKasir);

// Batasi tinggi tabel (misal 150px)
JScrollPane scrollRingkasan = new JScrollPane(tblRingkasan);
scrollRingkasan.setPreferredSize(new Dimension(580, 150));
scrollRingkasan.setBorder(BorderFactory.createTitledBorder("Barang yang dibeli"));

// === Panel Tengah: Form Input ===
JPanel formPanel = new JPanel(new GridBagLayout());
formPanel.setBackground(Color.WHITE); // background putih
formPanel.setBorder(BorderFactory.createTitledBorder("Detail Pembayaran"));

GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(3, 10, 3, 10); // jarak antar komponen lebih rapat dan seimbang
gbc.fill = GridBagConstraints.HORIZONTAL;
gbc.anchor = GridBagConstraints.WEST;

// Styling font
Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

// Buat textfield
JTextField txtSubtotal   = new JTextField(txtTotal.getText(), 20);
JTextField txtDiskon     = new JTextField("0", 20);
JTextField txtGrandTotal = new JTextField(txtTotal.getText(), 20);
JTextField txtTunai      = new JTextField(txtTotal.getText(), 20);
JTextField txtKredit     = new JTextField("0", 20);
JTextField txtKembalian  = new JTextField("0", 20);
JTextArea  txtKeterangan = new JTextArea(3, 20);

// Background dan font
JTextField[] fields = {txtSubtotal, txtDiskon, txtGrandTotal, txtTunai, txtKredit, txtKembalian};
for (JTextField f : fields) {
    f.setBackground(Color.WHITE);
    f.setFont(fieldFont);
    f.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(180, 180, 180)),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    ));
}

txtKeterangan.setFont(fieldFont);
txtKeterangan.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
txtKeterangan.setBackground(Color.WHITE);

txtSubtotal.setEditable(false);
txtGrandTotal.setEditable(false);
txtKembalian.setEditable(false);

// Tambahkan ke GridBag
int row = 0;
String[] labels = {"Subtotal:", "Diskon:", "Grand Total:", "Tunai:", "Kredit:", "Kembalian:"};
JTextField[] txts = {txtSubtotal, txtDiskon, txtGrandTotal, txtTunai, txtKredit, txtKembalian};

for (int i = 0; i < labels.length; i++) {
    gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
    JLabel lbl = new JLabel(labels[i]);
    lbl.setFont(labelFont);
    formPanel.add(lbl, gbc);

    gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1;
    formPanel.add(txts[i], gbc);
    row++;
}

// Baris terakhir: Keterangan
gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
JLabel lblKet = new JLabel("Keterangan:");
lblKet.setFont(labelFont);
formPanel.add(lblKet, gbc);

gbc.gridx = 1; gbc.gridy = row; gbc.fill = GridBagConstraints.BOTH;
formPanel.add(new JScrollPane(txtKeterangan), gbc);

    // === Panel Bawah: Tombol ===
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnSelesai = new JButton("[ENTER] Selesai");
    JButton btnKembali = new JButton("[ctrl + K] Kembali");
    buttonPanel.add(btnKembali);
    buttonPanel.add(btnSelesai);

    // === Event Listener Perhitungan ===
    txtDiskon.getDocument().addDocumentListener(new SimpleDocumentListener(() ->
        updateGrandTotal(txtSubtotal, txtDiskon, txtGrandTotal)
    ));

    txtTunai.getDocument().addDocumentListener(new SimpleDocumentListener(() ->
        updateKembalian(txtGrandTotal, txtTunai, txtKredit, txtKembalian)
    ));

    txtKredit.getDocument().addDocumentListener(new SimpleDocumentListener(() ->
        updateKembalian(txtGrandTotal, txtTunai, txtKredit, txtKembalian)
    ));
  

    // === Event Tombol ===
    btnKembali.addActionListener(ev -> dialog.dispose());
btnSelesai.addActionListener(ev -> {
      // === VALIDASI INPUT ===
    if (txtSubtotal.getText().trim().isEmpty() || 
        txtGrandTotal.getText().trim().isEmpty() || 
        txtTunai.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(
            dialog,
            "Pastikan Subtotal, Grand Total, dan Tunai sudah diisi!",
            "Input Tidak Lengkap",
            JOptionPane.WARNING_MESSAGE
        );
        return; // hentikan proses simpan
    }

    try (Connection conn = koneksi.dbKonek()) {
        conn.setAutoCommit(false); // biar atomic semua insert

        // === 1. Insert Transaksi ===
        String sqlTransaksi = "INSERT INTO Transaksi (noTransaksi, namaPengguna, tgl_transaksi, subtotal, diskon, grand_total, metodePembayaran) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING idTransaksi";
        PreparedStatement pstTrans = conn.prepareStatement(sqlTransaksi);
        pstTrans.setString(1, txtNo.getText());
        pstTrans.setString(2, txtPengguna.getText());
        pstTrans.setTimestamp(3, new java.sql.Timestamp(jdcTanggal.getDate().getTime()));
        pstTrans.setBigDecimal(4, new java.math.BigDecimal(txtSubtotal.getText()));
        pstTrans.setBigDecimal(5, new java.math.BigDecimal(txtDiskon.getText()));
        pstTrans.setBigDecimal(6, new java.math.BigDecimal(txtGrandTotal.getText()));

        String metodePembayaran = Double.parseDouble(txtKredit.getText()) > 0 ? "Kredit" : "Tunai";
        pstTrans.setString(7, metodePembayaran);

        ResultSet rs = pstTrans.executeQuery();
        int idTransaksi = 0;
        if (rs.next()) {
            idTransaksi = rs.getInt("idTransaksi");
        }
        rs.close();
        pstTrans.close();

        DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();
        String sqlDetail = "INSERT INTO DetailTransaksi (kodeBarang, idTransaksi, namaBarang, jumlah, harga, keterangan, subtotal) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);

        for (int i = 0; i < model.getRowCount(); i++) {
            String sku = model.getValueAt(i, 0).toString();
            String namaBarang = model.getValueAt(i, 1).toString();
            double harga = Double.parseDouble(model.getValueAt(i, 2).toString());
            int jumlah = Integer.parseInt(model.getValueAt(i, 3).toString());
            double total = Double.parseDouble(model.getValueAt(i, 4).toString());

            int kodeBarang = 0;
            PreparedStatement pstCek = conn.prepareStatement("SELECT kodeBarang FROM Barang WHERE SKUBarang = ?");
            pstCek.setString(1, sku);
            ResultSet rsCek = pstCek.executeQuery();
            if (rsCek.next()) {
                kodeBarang = rsCek.getInt("kodeBarang");
            }
            rsCek.close();
            pstCek.close();

            pstDetail.setInt(1, kodeBarang);
            pstDetail.setInt(2, idTransaksi);
            pstDetail.setString(3, namaBarang);
            pstDetail.setInt(4, jumlah);
            pstDetail.setBigDecimal(5, new java.math.BigDecimal(harga));
            pstDetail.setString(6, txtKeterangan.getText());
            pstDetail.setBigDecimal(7, new java.math.BigDecimal(total));
            pstDetail.addBatch();
        }
        pstDetail.executeBatch();
        pstDetail.close();

        String sqlKeu = "INSERT INTO Keuangan (idAsal, jenisKeuangan, masuk, keluar, tanggal) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstKeu = conn.prepareStatement(sqlKeu);
        pstKeu.setInt(1, idTransaksi);
        pstKeu.setString(2, "Penjualan Barang");
        pstKeu.setBigDecimal(3, new java.math.BigDecimal(txtGrandTotal.getText()));
        pstKeu.setBigDecimal(4, new java.math.BigDecimal("0"));
        pstKeu.setDate(5, new java.sql.Date(jdcTanggal.getDate().getTime()));
        pstKeu.executeUpdate();
        pstKeu.close();

               conn.commit(); 
        JOptionPane.showMessageDialog(dialog, "Transaksi berhasil disimpan!");
        dialog.dispose();

        // Kosongkan keranjang
        ((DefaultTableModel) tblKasir.getModel()).setRowCount(0);
        txtTotal.setText("0");
        txtNo.setText(generateNoTransaksi(conn));
         loadRiwayatHariIni();

  SwingUtilities.invokeLater(() -> {
        txtSku.requestFocusInWindow();
    });
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(dialog, "Gagal simpan transaksi: " + ex.getMessage());
    }
});

// === Tambahkan Panel ke Dialog ===
dialog.add(scrollRingkasan, BorderLayout.NORTH);
dialog.add(formPanel, BorderLayout.CENTER);
dialog.add(buttonPanel, BorderLayout.SOUTH);

// === Fokus otomatis ke txtTunai saat popup muncul ===
dialog.addWindowListener(new WindowAdapter() {
    @Override
    public void windowOpened(WindowEvent e) {
        txtTunai.requestFocusInWindow();
    }
});
// === Key Bindings untuk Enter dan Ctrl+K di dalam Dialog ===
InputMap im = dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
ActionMap am = dialog.getRootPane().getActionMap();

// Tekan ENTER → klik tombol Selesai
im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "submitDialog");
am.put("submitDialog", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnSelesai.doClick();
    }
});

// Tekan CTRL + K → klik tombol Kembali
im.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK), "cancelDialog");
am.put("cancelDialog", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnKembali.doClick();
    }
});
// === Warna dasar putih untuk seluruh popup ===
Color putih = Color.WHITE;
dialog.getContentPane().setBackground(putih);

// scrollRingkasan (tabel barang)
scrollRingkasan.setBackground(putih);
scrollRingkasan.getViewport().setBackground(putih);
tblRingkasan.setBackground(putih);
tblRingkasan.setGridColor(new Color(220, 220, 220)); // garis tabel lembut
tblRingkasan.setSelectionBackground(new Color(230, 240, 255));
tblRingkasan.setSelectionForeground(Color.BLACK);

// formPanel (sudah putih di versi sebelumnya)
formPanel.setBackground(putih);

// button panel
buttonPanel.setBackground(putih);

// seluruh tombol juga bisa diberi warna lebih kontras
btnSelesai.setBackground(new Color(0, 123, 255));
btnSelesai.setForeground(Color.WHITE);
btnSelesai.setFocusPainted(false);
btnSelesai.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

btnKembali.setBackground(new Color(0, 123, 255));
btnKembali.setForeground(Color.WHITE);
btnKembali.setFocusPainted(false);
btnKembali.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

dialog.setVisible(true);

}
private void hitungTotal() {
    DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();
    double total = 0;

    for (int i = 0; i < model.getRowCount(); i++) {
        double subTotal = Double.parseDouble(model.getValueAt(i, 4).toString()); // kolom 4 = Total
        total += subTotal;
    }

    txtTotal.setText(String.valueOf(total));
}

// Helper untuk tambah field
private void formPanelAdd(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
    gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
    panel.add(new JLabel(label), gbc);

    gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.7;
    panel.add(field, gbc);
}

// Listener sederhana
class SimpleDocumentListener implements javax.swing.event.DocumentListener {
    private Runnable callback;
    public SimpleDocumentListener(Runnable callback) {
        this.callback = callback;
    }
    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { callback.run(); }
    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { callback.run(); }
    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { callback.run(); }
}

// Helper parsing aman
private double parseDoubleSafe(String text) {
    try {
        return Double.parseDouble(text);
    } catch (Exception e) {
        return 0;
    }
}

// Perhitungan
private void updateGrandTotal(JTextField txtSubtotal, JTextField txtDiskon, JTextField txtGrandTotal) {
    double subtotal = parseDoubleSafe(txtSubtotal.getText());
    double diskonPersen = parseDoubleSafe(txtDiskon.getText());

    // Validasi batas diskon biar gak aneh
    if (diskonPersen < 0) diskonPersen = 0;
    if (diskonPersen > 100) diskonPersen = 100;

    // Hitung potongan dan grand total
    double potongan = subtotal * (diskonPersen / 100.0);
    double grandTotal = subtotal - potongan;

    txtGrandTotal.setText(String.format("%.2f", grandTotal));
}


private void updateKembalian(JTextField txtGrandTotal, JTextField txtTunai, JTextField txtKredit, JTextField txtKembalian) {
    double grand   = parseDoubleSafe(txtGrandTotal.getText());
    double tunai   = parseDoubleSafe(txtTunai.getText());
    double kredit  = parseDoubleSafe(txtKredit.getText());
    txtKembalian.setText(String.valueOf((tunai + kredit) - grand));
}
private String generateNoTransaksi(Connection conn) throws SQLException {
    java.util.Date now = new java.util.Date();
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMdd"); 
    String hariBulan = sdf.format(now); // contoh: 0921

    // Ambil transaksi terakhir hari ini
    String sql = "SELECT noTransaksi FROM Transaksi " +
                 "WHERE to_char(tgl_transaksi, 'MMdd') = ? " +
                 "ORDER BY idTransaksi DESC LIMIT 1";
    PreparedStatement pst = conn.prepareStatement(sql);
    pst.setString(1, hariBulan);

    ResultSet rs = pst.executeQuery();
    int urutan = 1;
    if (rs.next()) {
        String lastNo = rs.getString("noTransaksi"); 
        System.out.println("Last NoTransaksi = " + lastNo);

        // Ambil angka di belakang kode tanggal
        String angka = lastNo.replaceAll("\\D+", ""); // hanya ambil angka
        if (angka.length() > 4) { // setelah "0921"
            urutan = Integer.parseInt(angka.substring(4)) + 1;
        }
    }
    rs.close();
    pst.close();

    return "T" + hariBulan + urutan; 
}
private void setupSkuScanner() {
    // bikin timer dengan delay 500ms
    timer = new javax.swing.Timer(500, e -> {
        String sku = txtSku.getText().trim();
        if (!sku.isEmpty()) {
            jalankanCariBarang(sku);  // fungsi untuk ambil data barang
            loadMenu(null); // refresh stok di panel menu

            txtSku.setText("");       // kosongkan setelah scan selesai
        }
    });
    timer.setRepeats(false);

    // pasang listener ke textfield
    txtSku.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            restartTimer();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            restartTimer();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            restartTimer();
        }

        private void restartTimer() {
            if (timer.isRunning()) {
                timer.restart(); // kalau ada input baru, reset hitungan
            } else {
                timer.start();   // mulai hitungan 500ms
            }
        }
    });
}


   private void jalankanCariBarang(String sku) {
    try {
        Connection conn = koneksi.dbKonek();
        String sql = "SELECT nama, hargabarang, stok FROM barang WHERE skubarang=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, sku);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String nama = rs.getString("nama");
            double harga = rs.getDouble("hargabarang");
            int stok = rs.getInt("stok");

            if (stok > 0) {
                DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();
                boolean barangSudahAda = false;

                // cek apakah barang sudah ada di tabel
                for (int i = 0; i < model.getRowCount(); i++) {
                    String skuTabel = model.getValueAt(i, 0).toString();
                    if (skuTabel.equals(sku)) {
                        // barang sudah ada → update jumlah & subtotal
                        int jumlah = (int) model.getValueAt(i, 3);
                        jumlah++;
                        model.setValueAt(jumlah, i, 3);
                        model.setValueAt(harga * jumlah, i, 4);
                        barangSudahAda = true;
                        break;
                    }
                }

                if (!barangSudahAda) {
                    // barang belum ada → tambahkan baris baru
                    model.addRow(new Object[]{ sku, nama, harga, 1, harga });
                    loadMenu(null); // refresh stok di panel menu

                }

                // update stok di database (kurangi 1)
                String update = "UPDATE barang SET stok = stok - 1 WHERE skubarang=?";
                PreparedStatement psUpdate = conn.prepareStatement(update);
                psUpdate.setString(1, sku);
                psUpdate.executeUpdate();

                hitungTotal();
            } else {
                JOptionPane.showMessageDialog(this, "Stok habis!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Barang tidak ditemukan!");
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}public void batalkanTransaksi() {
    DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();

    for (int i = 0; i < model.getRowCount(); i++) {
        String sku = model.getValueAt(i, 0).toString();
        int jumlah = Integer.parseInt(model.getValueAt(i, 3).toString());
        updateStokBySKU(sku, jumlah); // fungsi kamu yang sudah ada
    }

    model.setRowCount(0); // kosongkan tabel
    txtTotal.setText("Rp 0");
 
}
public javax.swing.JTable getTblKasir() {
    return tblKasir;
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
        txtSku = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnPembayaran = new javax.swing.JButton();
        txtTotal = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKasir = new javax.swing.JTable();
        txtPengguna = new javax.swing.JTextField();
        btnDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jdcTanggal = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        btnBatal = new javax.swing.JButton();
        txtNo = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnlMenu = new javax.swing.JPanel();
        txtSku1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnDetail = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblRiwayat = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1740, 960));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtSku.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSkuActionPerformed(evt);
            }
        });
        txtSku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSkuKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSkuKeyReleased(evt);
            }
        });
        jPanel2.add(txtSku, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 120, 301, 51));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("SKU barang");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, -1, -1));

        btnPembayaran.setBackground(new java.awt.Color(51, 255, 0));
        btnPembayaran.setText("[CTRL + S] PEMBAYARAN");
        btnPembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPembayaranActionPerformed(evt);
            }
        });
        jPanel2.add(btnPembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 770, 210, 50));

        txtTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel2.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 230, 301, 51));

        tblKasir.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "KodeSKU", "Nama", "Harga", "Jumlah", "Total"
            }
        ));
        tblKasir.setRowHeight(35);
        jScrollPane1.setViewportView(tblKasir);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, 630, 350));
        jPanel2.add(txtPengguna, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 281, 51));

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 350, 120, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("No transaksi");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Nama Pengguna");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, -1, -1));

        btnEdit.setBackground(new java.awt.Color(255, 153, 51));
        btnEdit.setText("EDIT");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jPanel2.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 350, 140, 50));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("TOTAL");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 190, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Tanggal");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, -1, -1));
        jPanel2.add(jdcTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 290, 60));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("CARI");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 80, -1, -1));

        btnBatal.setBackground(new java.awt.Color(204, 204, 204));
        btnBatal.setText("[CTRL+B] BATAL");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        jPanel2.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 770, 130, 50));

        txtNo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel2.add(txtNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 281, 51));

        pnlMenu.setBackground(new java.awt.Color(255, 255, 255));
        pnlMenu.setPreferredSize(new java.awt.Dimension(662, 858));
        jScrollPane2.setViewportView(pnlMenu);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 140, -1, 670));

        txtSku1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSku1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSku1KeyReleased(evt);
            }
        });
        jPanel2.add(txtSku1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 70, 610, 51));

        jPanel3.setBackground(new java.awt.Color(5, 69, 162));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("KASIR SAKPORE");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel7)
                .addContainerGap(1175, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel7)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, 1390, 60));

        jLabel12.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jLabel12.setText("[ Ctrl+D ]");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 330, -1, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jLabel13.setText("[ Ctrl+E ]");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 330, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 1380, 840));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(5, 69, 162));

        btnDetail.setBackground(new java.awt.Color(255, 255, 0));
        btnDetail.setText("[CTRL+D] Detail");
        btnDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(btnDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, 60));

        tblRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "No", "Tanggal", "Total"
            }
        ));
        tblRiwayat.setRowHeight(35);
        jScrollPane3.setViewportView(tblRiwayat);
        if (tblRiwayat.getColumnModel().getColumnCount() > 0) {
            tblRiwayat.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 270, 720));

        lblTotal.setText("jLabel8");
        jPanel4.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 796, 270, 30));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1410, 10, 290, 840));

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

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
    int row = tblKasir.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih dulu item yang mau dihapus!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();

        String sku = model.getValueAt(row, 0).toString();
        int jumlah = (int) model.getValueAt(row, 3);

        // Kembalikan stok ke DB
        updateStokBySKU(sku, jumlah);

        // Hapus baris dari JTable
        model.removeRow(row);
hitungTotal();
        // Refresh menu
        loadMenu(null);        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    int row = tblKasir.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Pilih dulu item yang mau diedit!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();

    String sku = model.getValueAt(row, 0).toString();
    String nama = model.getValueAt(row, 1).toString();
    double harga = Double.parseDouble(model.getValueAt(row, 2).toString());
    int jumlahLama = Integer.parseInt(model.getValueAt(row, 3).toString());

    // Pop-up input jumlah baru
    String jumlahStr = JOptionPane.showInputDialog(
        this, 
        "Edit jumlah untuk " + nama, 
        jumlahLama
    );

    if (jumlahStr != null && !jumlahStr.isEmpty()) {
        try {
            int jumlahBaru = Integer.parseInt(jumlahStr);

            if (jumlahBaru <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah tidak boleh 0 atau negatif!");
                return;
            }

            // Ambil stok dari database
            int stokDb = getStokBySKU(sku);

            // Hitung selisih antara jumlah baru dan lama
            int selisih = jumlahBaru - jumlahLama;

            // Kalau nambah barang → pastikan stok di DB cukup
            if (selisih > 0 && selisih > stokDb) {
                JOptionPane.showMessageDialog(this, "Stok tidak cukup! Sisa stok: " + stokDb);
                return;
            }

            // Update stok di DB
            updateStokBySKU(sku, -selisih); // negatif kalau nambah, positif kalau ngurang

            // Update JTable
            double totalBaru = jumlahBaru * harga;
            model.setValueAt(jumlahBaru, row, 3);
            model.setValueAt(totalBaru, row, 4);

            // Refresh total kasir
            hitungTotal();
loadMenu(null); // refresh stok di panel menu

            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input harus berupa angka!");
        }
    }
    // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed
private javax.swing.Timer searchTimer;

    private void txtSku1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSku1KeyReleased
    if (searchTimer != null && searchTimer.isRunning()) {
        searchTimer.stop();
    }

    searchTimer = new javax.swing.Timer(300, e -> {
        String keyword = txtSku1.getText().trim();
        loadMenu(keyword);
    });
    searchTimer.setRepeats(false); // cuma sekali jalan
    searchTimer.start();

    }//GEN-LAST:event_txtSku1KeyReleased

    private void btnPembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPembayaranActionPerformed
showPopupPembayaran();        // TODO add your handling code here:
    }//GEN-LAST:event_btnPembayaranActionPerformed

    private void txtSkuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSkuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSkuActionPerformed

    private void txtSkuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSkuKeyPressed

    }//GEN-LAST:event_txtSkuKeyPressed

    private void txtSkuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSkuKeyReleased

    }//GEN-LAST:event_txtSkuKeyReleased

    private void btnDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetailActionPerformed
        int selectedRow = tblRiwayat.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih transaksi terlebih dahulu!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblRiwayat.getModel();
    int idTransaksi = (int) model.getValueAt(selectedRow, 3);

    PopupDetailTransaksi popup = new PopupDetailTransaksi(
        (java.awt.Frame) SwingUtilities.getWindowAncestor(this),
        true,
        idTransaksi
    );
    popup.setLocationRelativeTo(null);
    popup.setVisible(true);
    }//GEN-LAST:event_btnDetailActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
    DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();

    if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Tidak ada transaksi yang dibatalkan.");
        return;
    }

    try {
        // Kembalikan stok semua barang yang ada di tabel kasir
        for (int i = 0; i < model.getRowCount(); i++) {
            String sku = model.getValueAt(i, 0).toString();       // kolom SKUBarang
            int jumlah = Integer.parseInt(model.getValueAt(i, 3).toString()); // kolom Jumlah

            // panggil fungsi update stok yang sudah kamu buat
            updateStokBySKU(sku, jumlah);
        }

        // Kosongkan tabel kasir
        model.setRowCount(0);

        // Reset label total, diskon, grand total (ubah sesuai nama label kamu)
        txtTotal.setText("0");
     
        JOptionPane.showMessageDialog(this, "Transaksi dibatalkan dan stok telah dikembalikan.");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal membatalkan transaksi: " + e.getMessage());
    }


    }//GEN-LAST:event_btnBatalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDetail;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnPembayaran;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private com.toedter.calendar.JDateChooser jdcTanggal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JTable tblKasir;
    private javax.swing.JTable tblRiwayat;
    private javax.swing.JTextField txtNo;
    private javax.swing.JTextField txtPengguna;
    private javax.swing.JTextField txtSku;
    private javax.swing.JTextField txtSku1;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
