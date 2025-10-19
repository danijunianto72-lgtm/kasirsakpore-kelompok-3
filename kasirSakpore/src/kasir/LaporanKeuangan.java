/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.Font;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Font;

public class LaporanKeuangan extends javax.swing.JPanel {

   
    public LaporanKeuangan() {
        initComponents();
        loadDataKeuangan();
        loadDataSemua();
//        hitungTotalBulanan();
        loadDataBulanTahun();
        setFilterDefault();
        setListenerTable();
        element();
            setupDateChooserBehavior(); // ⬅️ tambahkan ini

          btnFilter.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK),
    "ctrlF"
);

btnFilter.getActionMap().put("ctrlF", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnFilter.doClick(); // Menjalankan aksi tombol
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

         btnCetak1.getActionMap().put("ctrlV", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnCetak1.doClick(); // Menjalankan aksi tombol
    }
});

        btnCetak1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
    "ctrlV"
); 
        
              btnFilter.getActionMap().put("ctrlR", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnFilter.doClick(); // Menjalankan aksi tombol
    }
});

        btnFilter.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK),
    "ctrlR"
);
    SwingUtilities.invokeLater(() -> {
        jdcStart.getDateEditor().getUiComponent().requestFocusInWindow();
        setupGlobalShortcuts(); // ⬅️ tambahkan di sini
    });  
    
    }
private void setupGlobalShortcuts() {
    JRootPane root = SwingUtilities.getRootPane(this);
    if (root == null) return;

    InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = root.getActionMap();

    // --- CTRL + 1 → Fokus ke jdcStart ---
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK), "focusStart");
    am.put("focusStart", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField tf = (JTextField) jdcStart.getDateEditor().getUiComponent();
            tf.requestFocusInWindow();
        }
    });

    // --- CTRL + 2 → Fokus ke jdcEnd ---
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK), "focusEnd");
    am.put("focusEnd", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField tf = (JTextField) jdcEnd.getDateEditor().getUiComponent();
            tf.requestFocusInWindow();
        }        
    });

    // --- CTRL + 3 → Fokus ke JMonthChooser ---
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_DOWN_MASK), "focusMonth");
    am.put("focusMonth", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jmcBulan.getComboBox().requestFocusInWindow(); // jmcBulan = JMonthChooser
        }
    });



    // --- CTRL + J → buka popup kalender dari JDateChooser yang aktif ---
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_DOWN_MASK), "openCalendar");
    am.put("openCalendar", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isFocused(jdcStart)) {
                clickDateButton(jdcStart);
            } else if (isFocused(jdcEnd)) {
                clickDateButton(jdcEnd);
            }
        }
    });
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

JTableHeader Header = tblDetails.getTableHeader();
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

JTableHeader setHeader = tblSemua.getTableHeader();
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

// 🔍 Helper untuk cek apakah date chooser atau textfield-nya sedang fokus
private boolean isFocused(JDateChooser chooser) {
    return chooser.isFocusOwner() ||
           ((JTextField) chooser.getDateEditor().getUiComponent()).isFocusOwner();
}

// 🔘 Helper untuk klik tombol kalender JDateChooser
private void clickDateButton(JDateChooser chooser) {
    for (Component comp : chooser.getComponents()) {
        if (comp instanceof JButton) {
            ((JButton) comp).doClick();
            break;
        }
    }
}

    private void setupDateChooserBehavior() {
    JTextField tfStart = (JTextField) jdcStart.getDateEditor().getUiComponent();
    JButton btnStart = findDateChooserButton(jdcStart);

    JTextField tfEnd = (JTextField) jdcEnd.getDateEditor().getUiComponent();
    JButton btnEnd = findDateChooserButton(jdcEnd);

    tfStart.setFocusTraversalKeysEnabled(false);
    tfEnd.setFocusTraversalKeysEnabled(false);

    InputMap imStart = tfStart.getInputMap(JComponent.WHEN_FOCUSED);
    ActionMap amStart = tfStart.getActionMap();
    imStart.put(KeyStroke.getKeyStroke("control J"), "openCalendar");
    amStart.put("openCalendar", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (btnStart != null) btnStart.doClick();
        }
    });

    InputMap imEnd = tfEnd.getInputMap(JComponent.WHEN_FOCUSED);
    ActionMap amEnd = tfEnd.getActionMap();
    imEnd.put(KeyStroke.getKeyStroke("control J"), "openCalendar");
    amEnd.put("openCalendar", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (btnEnd != null) btnEnd.doClick();
        }
    });
}

// Helper untuk cari tombol kalender di JDateChooser
private JButton findDateChooserButton(JDateChooser chooser) {
    for (Component comp : chooser.getComponents()) {
        if (comp instanceof JButton) {
            return (JButton) comp;
        }
    }
    return null;
}

    private void setListenerTable(){
        tblSemua.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting()) {
        int row = tblSemua.getSelectedRow();
        if (row != -1) {
            Object tglObj = tblSemua.getValueAt(row, 1); // kolom tanggal, sesuaikan indeks
            if (tglObj != null) {
                java.sql.Date tgl = java.sql.Date.valueOf(tglObj.toString());
        loadDetailKeuanganPerTanggal(tgl);
            }
        }
    }
});
    }
    private void setFilterDefault() {
    Calendar cal = Calendar.getInstance();

    cal.set(Calendar.DAY_OF_MONTH, 1);
    jdcStart.setDate(cal.getTime());

    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    jdcEnd.setDate(cal.getTime());
}
//    private void hitungTotalBulanan() {
//    try (Connection conn = koneksi.dbKonek()) {
//        LocalDate sekarang = LocalDate.now();
//        int bulan = sekarang.getMonthValue();
//        int tahun = sekarang.getYear();
//
//        String sql = """
//            SELECT 
//                COALESCE(SUM(masuk), 0) AS total_masuk,
//                COALESCE(SUM(keluar), 0) AS total_keluar
//            FROM keuangan
//            WHERE EXTRACT(MONTH FROM tanggal) = ? 
//              AND EXTRACT(YEAR FROM tanggal) = ?
//        """;
//
//        PreparedStatement ps = conn.prepareStatement(sql);
//        ps.setInt(1, bulan);
//        ps.setInt(2, tahun);
//        ResultSet rs = ps.executeQuery();
//
//        if (rs.next()) { 
//            double totalMasuk = rs.getDouble("total_masuk");
//            double totalKeluar = rs.getDouble("total_keluar");
//            double totalAkhir = totalMasuk - totalKeluar;
//
//            // format angka jadi rupiah
//            lblMasukBulan.setText(String.format("Rp %, .0f", totalAkhir));
//        } else {
//            lblMasukBulan.setText("Rp 0");
//        }
//
//        rs.close();
//        ps.close();
//    } catch (SQLException e) {
//        JOptionPane.showMessageDialog(this, "Gagal menghitung total bulanan: " + e.getMessage());
//    }
//}



 private void loadDataBulanTahun() {
    int bulan = jmcBulan.getMonth() + 1; 
    int tahun = jycTahun.getYear();

    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };

    model.addColumn("No");
    model.addColumn("Tanggal");
    model.addColumn("Total Masuk");
    model.addColumn("Total Keluar");
    model.addColumn("Saldo Harian");

    double totalMasukBulan = 0;
    double totalKeluarBulan = 0;

    try (Connection conn = koneksi.dbKonek()) {
        String sql = """
            SELECT 
                tanggal::date AS tgl,
                COALESCE(SUM(masuk), 0) AS total_masuk,
                COALESCE(SUM(keluar), 0) AS total_keluar
            FROM keuangan
            WHERE EXTRACT(MONTH FROM tanggal) = ?
              AND EXTRACT(YEAR FROM tanggal) = ?
            GROUP BY tanggal::date
            ORDER BY tanggal::date ASC
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, bulan);
        ps.setInt(2, tahun);
        ResultSet rs = ps.executeQuery();

        int no = 1;
        while (rs.next()) {
            double masuk = rs.getDouble("total_masuk");
            double keluar = rs.getDouble("total_keluar");
            double saldo = masuk - keluar;

            totalMasukBulan += masuk;
            totalKeluarBulan += keluar;

            model.addRow(new Object[]{
                no++,
                rs.getDate("tgl"),
                String.format("Rp %, .0f", masuk),
                String.format("Rp %, .0f", keluar),
                String.format("Rp %, .0f", saldo)
            });
        }

        tblSemua.setModel(model);

        // Hitung total bulan
        double totalKeuntungan = totalMasukBulan - totalKeluarBulan;

        // Format nama bulan biar enak dibaca
        java.time.Month namaBulan = java.time.Month.of(bulan);
        String namaBulanProper = namaBulan.name().substring(0, 1) + namaBulan.name().substring(1).toLowerCase();

        // Set semua label
        lblMasukBulan.setText(String.format("Total Pemasukkan: Rp %, .0f", totalMasukBulan));
        lblKeluarBulan.setText(String.format("Total Pengeluaran: Rp %, .0f", totalKeluarBulan));
        lblKeuntunganBulan.setText(String.format("Total Keuntungan: Rp %, .0f", totalKeuntungan));

        lblTotalBulan.setText("Laporan Keuangan Bulan " + namaBulanProper + " " + tahun);

        rs.close();
        ps.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
    }
}


int selectedId = -1; // untuk simpan idkeuangan yang dipilih

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
Session.updateSaldo(tblKeuangan, lblMasuk, lblKeluar, lblSaldo);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
    }
}
private void tampilDetailPenjualan(int idKeuangan) {
    try (Connection conn = koneksi.dbKonek()) {
        String sql = """
            SELECT k.idasal, k.jeniskeuangan, t.notransaksi, t.namapengguna, t.tgl_transaksi,
                   t.subtotal, t.diskon, t.grand_total, t.metodepembayaran
            FROM keuangan k
            JOIN transaksi t ON k.idasal = t.idtransaksi
            WHERE k.idkeuangan = ? AND k.jeniskeuangan = 'Penjualan Barang'
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idKeuangan);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int idTransaksi = rs.getInt("idasal");
            String info = """
                No Transaksi : %s
                Kasir        : %s
                Tanggal      : %s
                Subtotal     : %.2f
                Diskon       : %.2f
                Grand Total  : %.2f
                Metode Bayar : %s
            """.formatted(
                rs.getString("notransaksi"),
                rs.getString("namapengguna"),
                rs.getTimestamp("tgl_transaksi"),
                rs.getDouble("subtotal"),
                rs.getDouble("diskon"),
                rs.getDouble("grand_total"),
                rs.getString("metodepembayaran")
            );

            String sqlDetail = """
                SELECT namabarang, jumlah, harga, subtotal, keterangan
                FROM detailtransaksi
                WHERE idtransaksi = ?
            """;
            PreparedStatement ps2 = conn.prepareStatement(sqlDetail);
            ps2.setInt(1, idTransaksi);
            ResultSet rs2 = ps2.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append("\n\n=== Detail Barang ===\n");
            while (rs2.next()) {
               sb.append(String.format("%s (%d x %.0f) = %.0f %s\n",
                rs2.getString("namabarang"),
                rs2.getInt("jumlah"),
                rs2.getDouble("harga"),
                rs2.getDouble("subtotal"),
                rs2.getString("keterangan") == null ? "" : "(" + rs2.getString("keterangan") + ")"
            ));

            }

            JOptionPane.showMessageDialog(this, info + sb.toString(), 
                "Detail Penjualan", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Data bukan dari transaksi penjualan atau tidak ditemukan.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menampilkan detail: " + e.getMessage());
    }
}
private java.util.List<java.sql.Date> tanggalList = new ArrayList<>();

private void loadDataSemua() {
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    model.addColumn("No");
    model.addColumn("Tanggal");
    model.addColumn("Masuk");
    model.addColumn("Keluar");
    model.addColumn("Total Akhir");

    try (Connection conn = koneksi.dbKonek()) {
        String sql = """
            SELECT 
                tanggal::date AS tgl,
                SUM(masuk) AS total_masuk,
                SUM(keluar) AS total_keluar,
                SUM(masuk - keluar) AS total_akhir
            FROM keuangan
            WHERE EXTRACT(MONTH FROM tanggal) = EXTRACT(MONTH FROM CURRENT_DATE)
              AND EXTRACT(YEAR FROM tanggal) = EXTRACT(YEAR FROM CURRENT_DATE)
            GROUP BY tanggal::date
            ORDER BY tanggal::date;
        """;

        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        tanggalList.clear();
        int no = 1;

        while (rs.next()) {
            java.sql.Date tgl = rs.getDate("tgl");
            tanggalList.add(tgl);

            model.addRow(new Object[]{
                no++,
                tgl,
                rs.getDouble("total_masuk"),
                rs.getDouble("total_keluar"),
                rs.getDouble("total_akhir")
            });
        }

        tblSemua.setModel(model);
        tblSemua.getColumnModel().getColumn(0).setMaxWidth(40);
        tblSemua.getColumnModel().getColumn(0).setPreferredWidth(40);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load data rekap: " + e.getMessage());
    }
}

// Tambahkan listener setelah tabel selesai diinisialisasi

private void loadDetailKeuanganPerTanggal(java.sql.Date tanggal) {
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    model.addColumn("No");
    model.addColumn("Tanggal");
    model.addColumn("Jenis Keuangan");
    model.addColumn("Masuk");
    model.addColumn("Keluar");

    try (Connection conn = koneksi.dbKonek()) {
        String sql = """
            SELECT tanggal, jeniskeuangan, masuk, keluar
            FROM keuangan
            WHERE tanggal::date = ?
            ORDER BY idkeuangan;
        """;
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setDate(1, tanggal);

        ResultSet rs = pst.executeQuery();

        int no = 1;
        while (rs.next()) {
            model.addRow(new Object[]{
                no++,
                rs.getDate("tanggal"),
                rs.getString("jeniskeuangan"),
                rs.getDouble("masuk"),
                rs.getDouble("keluar")
            });
        }

        tblDetails.setModel(model);
        tblDetails.getColumnModel().getColumn(0).setMaxWidth(40);
        tblDetails.getColumnModel().getColumn(0).setPreferredWidth(40);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load detail: " + e.getMessage());
    }
}
private void element() {
    List<Component> tabOrder = Arrays.asList(
        (JTextField) jdcStart.getDateEditor().getUiComponent(),
        (JTextField) jdcEnd.getDateEditor().getUiComponent(),
        jycTahun,
        jmcBulan
    );

    setFocusTraversalPolicy(new CustomFocusTraversalPolicy(tabOrder));
    setFocusCycleRoot(true);

    // pastikan TAB tidak ditangkap di dalam textfield date chooser
    ((JTextField) jdcStart.getDateEditor().getUiComponent()).setFocusTraversalKeysEnabled(false);
    ((JTextField) jdcEnd.getDateEditor().getUiComponent()).setFocusTraversalKeysEnabled(false);
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

        panelUtama = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnCetak = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnFilter = new javax.swing.JButton();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKeuangan = new javax.swing.JTable();
        lblSaldo = new javax.swing.JLabel();
        lblKeluar = new javax.swing.JLabel();
        lblMasuk = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lblTotalBulan = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDetails = new javax.swing.JTable();
        lblMasukBulan = new javax.swing.JLabel();
        jycTahun = new com.toedter.calendar.JYearChooser();
        jmcBulan = new com.toedter.calendar.JMonthChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSemua = new javax.swing.JTable();
        lblKeluarBulan = new javax.swing.JLabel();
        lblKeuntunganBulan = new javax.swing.JLabel();
        btnCetak1 = new javax.swing.JButton();
        btnCetak2 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        panelUtama.setBackground(new java.awt.Color(255, 255, 255));
        panelUtama.setPreferredSize(new java.awt.Dimension(1740, 960));
        panelUtama.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCetak.setBackground(new java.awt.Color(255, 204, 0));
        btnCetak.setText("[CTRL+C] CETAK");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });
        jPanel1.add(btnCetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 60, 160, 50));

        jLabel2.setText("Sampai");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, -1, -1));

        btnFilter.setBackground(new java.awt.Color(0, 153, 153));
        btnFilter.setText("[CTRL+R] REFRESH");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });
        jPanel1.add(btnFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 60, 140, 50));

        jdcStart.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcStartPropertyChange(evt);
            }
        });
        jPanel1.add(jdcStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 150, 50));

        jdcEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcEndPropertyChange(evt);
            }
        });
        jPanel1.add(jdcEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, 140, 50));

        tblKeuangan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal", "Jenis Keuangan", "Masuk", "Keluar", "Total"
            }
        ));
        tblKeuangan.setRowHeight(35);
        tblKeuangan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblKeuanganKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblKeuangan);
        if (tblKeuangan.getColumnModel().getColumnCount() > 0) {
            tblKeuangan.getColumnModel().getColumn(2).setHeaderValue("Jenis Keuangan");
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 1610, 240));

        lblSaldo.setText("lblSaldo");
        jPanel1.add(lblSaldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 100, 400, -1));

        lblKeluar.setText("lblKeluar");
        jPanel1.add(lblKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 80, 400, -1));

        lblMasuk.setText("lblMasuk");
        jPanel1.add(lblMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 60, 390, -1));

        jPanel2.setBackground(new java.awt.Color(5, 69, 162));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Laporan Keuangan");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1660, 50));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 60, -1, -1));

        panelUtama.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 1640, 380));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(5, 69, 162));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalBulan.setBackground(new java.awt.Color(255, 255, 255));
        lblTotalBulan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalBulan.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalBulan.setText("lblTotalBulan");
        jPanel4.add(lblTotalBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 360, -1));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1650, 40));

        tblDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Tanggal", "Jenis Keuangan", "Masuk", "Keluar"
            }
        ));
        tblDetails.setRowHeight(35);
        jScrollPane2.setViewportView(tblDetails);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 110, 620, 260));

        lblMasukBulan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMasukBulan.setText("lblTotalBulan");
        jPanel3.add(lblMasukBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 360, -1));

        jycTahun.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jycTahunPropertyChange(evt);
            }
        });
        jPanel3.add(jycTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 130, 50));

        jmcBulan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jmcBulanPropertyChange(evt);
            }
        });
        jPanel3.add(jmcBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 130, 50));

        tblSemua.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal", "Masuk", "Keluar", "Total Akhir"
            }
        ));
        tblSemua.setRowHeight(35);
        tblSemua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblSemuaKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblSemua);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 950, 260));

        lblKeluarBulan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKeluarBulan.setText("lblTotalBulan");
        jPanel3.add(lblKeluarBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 370, 360, -1));

        lblKeuntunganBulan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKeuntunganBulan.setText("lblTotalBulan");
        jPanel3.add(lblKeuntunganBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 370, 360, -1));

        btnCetak1.setBackground(new java.awt.Color(204, 0, 0));
        btnCetak1.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak1.setText("[CTRL+B] BULANAN");
        btnCetak1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetak1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnCetak1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 150, 50));

        btnCetak2.setBackground(new java.awt.Color(255, 204, 0));
        btnCetak2.setText("[CTRL+T] TAHUNAN");
        btnCetak2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetak2ActionPerformed(evt);
            }
        });
        jPanel3.add(btnCetak2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, 150, 50));

        panelUtama.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, 1640, 400));

        add(panelUtama, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jdcEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcEndPropertyChange
 if ("date".equals(evt.getPropertyName())) {
        java.util.Date start = jdcStart.getDate();
        java.util.Date end = jdcEnd.getDate();

        if (start != null && end != null) {
            loadDataKeuangan(start, end);
        }
    }        // TODO add your handling code here:
    }//GEN-LAST:event_jdcEndPropertyChange

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
loadDataKeuangan();  
setFilterDefault();// TODO add your handling code here:
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Simpan Laporan Keuangan");
    chooser.setSelectedFile(new File("LaporanKeuangan.pdf"));

    int userSelection = chooser.showSaveDialog(this);
    if (userSelection != JFileChooser.APPROVE_OPTION) {
        return;
    }

    File fileToSave = chooser.getSelectedFile();
    String filePath = fileToSave.getAbsolutePath();

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    Date startDate = jdcStart.getDate();
    Date endDate = jdcEnd.getDate();
    String startStr = (startDate != null) ? sdf.format(startDate) : "-";
    String endStr = (endDate != null) ? sdf.format(endDate) : "-";

    JTable table = tblKeuangan;
    TableModel model = table.getModel();

    Document document = new Document(PageSize.A4.rotate());
    try {
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Judul
        Paragraph title = new Paragraph("LAPORAN KEUANGAN\n\n",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Periode
        Paragraph info = new Paragraph(
                "Periode: " + startStr + " s/d " + endStr + "\n\n",
                FontFactory.getFont(FontFactory.HELVETICA, 12));
        info.setAlignment(Element.ALIGN_CENTER);
        document.add(info);

        // Tabel data
        PdfPTable pdfTable = new PdfPTable(model.getColumnCount());
        pdfTable.setWidthPercentage(100);
        pdfTable.setSpacingBefore(10f);

        // Header kolom
        for (int i = 0; i < model.getColumnCount(); i++) {
            PdfPCell headerCell = new PdfPCell(new Phrase(model.getColumnName(i)));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(headerCell);
        }

        // Isi tabel
        for (int r = 0; r < model.getRowCount(); r++) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                Object val = model.getValueAt(r, c);
                PdfPCell cell = new PdfPCell(new Phrase(val == null ? "" : val.toString()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }
        }

        document.add(pdfTable);

        // Tambahkan ringkasan total di bawah tabel
        document.add(new Paragraph("\n\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));

        Paragraph totalMasuk = new Paragraph("Total pemasukan: " + lblMasuk.getText(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        Paragraph totalKeluar = new Paragraph("Total pengeluaran: " + lblKeluar.getText(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        Paragraph totalSaldo = new Paragraph("Keuntungan: " + lblSaldo.getText(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));

        totalMasuk.setAlignment(Element.ALIGN_LEFT);
        totalKeluar.setAlignment(Element.ALIGN_LEFT);
        totalSaldo.setAlignment(Element.ALIGN_LEFT);

        document.add(totalMasuk);
        document.add(totalKeluar);
        document.add(totalSaldo);

        // Footer tanggal cetak
        Paragraph footer = new Paragraph(
                "\nDicetak pada: " + sdf.format(new java.util.Date()),
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);

        document.close();

        JOptionPane.showMessageDialog(this,
                "PDF berhasil dibuat:\n" + filePath,
                "Sukses", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Gagal membuat PDF:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
// TODO add your handling code here:
    }//GEN-LAST:event_btnCetakActionPerformed

    private void tblKeuanganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblKeuanganKeyPressed
  if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        int row = tblKeuangan.getSelectedRow();
        if (row == -1) return;

        int idKeuangan = idList.get(row);
        tampilDetailPenjualan(idKeuangan);

        // biar Enter gak bikin "bunyi ding" atau pindah sel
        evt.consume();
    }        // TODO add your handling code here:
    }//GEN-LAST:event_tblKeuanganKeyPressed

    private void tblSemuaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSemuaKeyPressed
 if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        int row = tblSemua.getSelectedRow();
        if (row == -1) return;
        java.sql.Date tgl = tanggalList.get(row);
        loadDetailKeuanganPerTanggal(tgl);
        evt.consume();
    }        // TODO add your handling code here:
    }//GEN-LAST:event_tblSemuaKeyPressed

    private void jmcBulanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jmcBulanPropertyChange
loadDataBulanTahun();// TODO add your handling code here:
    }//GEN-LAST:event_jmcBulanPropertyChange

    private void jycTahunPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jycTahunPropertyChange
loadDataBulanTahun();// TODO add your handling code here:
    }//GEN-LAST:event_jycTahunPropertyChange

    private void jdcStartPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcStartPropertyChange
 if ("date".equals(evt.getPropertyName())) {
        java.util.Date start = jdcStart.getDate();
        java.util.Date end = jdcEnd.getDate();

        if (start != null && end != null) {
            loadDataKeuangan(start, end);
        }
    }         // TODO add your handling code here:
    }//GEN-LAST:event_jdcStartPropertyChange

    private void btnCetak1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetak1ActionPerformed
     int bulan = jmcBulan.getMonth() + 1;
    int tahun = jycTahun.getYear();

    String namaBulan = java.time.Month.of(bulan).name().substring(0, 1)
            + java.time.Month.of(bulan).name().substring(1).toLowerCase();

    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Simpan Laporan Keuangan");
    chooser.setSelectedFile(new java.io.File("Laporan_Keuangan_" + namaBulan + "_" + tahun + ".pdf"));

    int userSelection = chooser.showSaveDialog(this);
    if (userSelection != JFileChooser.APPROVE_OPTION) {
        return; 
    }

    java.io.File fileToSave = chooser.getSelectedFile();
    if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
        fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".pdf");
    }

    try (Connection conn = koneksi.dbKonek()) {
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
        com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new FileOutputStream(fileToSave));
        doc.open();

        // Judul utama
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Paragraph judul = new com.itextpdf.text.Paragraph(
                "LAPORAN KEUANGAN BULAN " + namaBulan.toUpperCase() + " TAHUN " + tahun, titleFont);
        judul.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        doc.add(judul);
        doc.add(new com.itextpdf.text.Paragraph(" "));

        com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font cellFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 11);

        // Query data keuangan per hari
        String sql = """
            SELECT 
                tanggal::date AS tgl,
                COALESCE(SUM(masuk), 0) AS total_masuk,
                COALESCE(SUM(keluar), 0) AS total_keluar
            FROM keuangan
            WHERE EXTRACT(MONTH FROM tanggal) = ?
              AND EXTRACT(YEAR FROM tanggal) = ?
            GROUP BY tanggal::date
            ORDER BY tanggal::date ASC
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, bulan);
        ps.setInt(2, tahun);
        ResultSet rs = ps.executeQuery();

        double totalMasukBulan = 0;
        double totalKeluarBulan = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        while (rs.next()) {
            java.sql.Date tgl = rs.getDate("tgl");
            double totalMasuk = rs.getDouble("total_masuk");
            double totalKeluar = rs.getDouble("total_keluar");
            double saldo = totalMasuk - totalKeluar;

            totalMasukBulan += totalMasuk;
            totalKeluarBulan += totalKeluar;

            // Tambah subjudul tanggal
            com.itextpdf.text.Paragraph subjudul = new com.itextpdf.text.Paragraph(
                    "Tanggal: " + sdf.format(tgl),
                    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 13, com.itextpdf.text.Font.BOLD)
            );
            doc.add(subjudul);
            doc.add(new com.itextpdf.text.Paragraph(
                    "Total Masuk: " + String.format("Rp %, .0f", totalMasuk) + 
                    " | Total Keluar: " + String.format("Rp %, .0f", totalKeluar) + 
                    " | Saldo: " + String.format("Rp %, .0f", saldo),
                    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11)
            ));
            doc.add(new com.itextpdf.text.Paragraph(" "));

            // Buat tabel detail harian
            com.itextpdf.text.pdf.PdfPTable tableDetail = new com.itextpdf.text.pdf.PdfPTable(4);
            tableDetail.setWidthPercentage(100);
            tableDetail.setWidths(new float[]{1f, 4f, 3f, 3f});

            tableDetail.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("No", headerFont)));
            tableDetail.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Jenis Keuangan", headerFont)));
            tableDetail.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Masuk", headerFont)));
            tableDetail.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Keluar", headerFont)));

            String sqlDetail = """
                SELECT jeniskeuangan, masuk, keluar
                FROM keuangan
                WHERE tanggal::date = ?
                ORDER BY idkeuangan;
            """;
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            psDetail.setDate(1, tgl);
            ResultSet rsDetail = psDetail.executeQuery();

            int no = 1;
            while (rsDetail.next()) {
                tableDetail.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(String.valueOf(no++), cellFont)));
                tableDetail.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(rsDetail.getString("jeniskeuangan"), cellFont)));
                tableDetail.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(String.format("Rp %, .0f", rsDetail.getDouble("masuk")), cellFont)));
                tableDetail.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(String.format("Rp %, .0f", rsDetail.getDouble("keluar")), cellFont)));
            }

            doc.add(tableDetail);
            doc.add(new com.itextpdf.text.Paragraph(" ")); // spasi antar tanggal

            rsDetail.close();
            psDetail.close();
        }

        double totalKeuntunganBulan = totalMasukBulan - totalKeluarBulan;

        doc.add(new com.itextpdf.text.Paragraph(" "));
        doc.add(new com.itextpdf.text.Paragraph("Total Pemasukan Bulan Ini : " + String.format("Rp %, .0f", totalMasukBulan)));
        doc.add(new com.itextpdf.text.Paragraph("Total Pengeluaran Bulan Ini : " + String.format("Rp %, .0f", totalKeluarBulan)));
        doc.add(new com.itextpdf.text.Paragraph("Keuntungan Bulan Ini : " + String.format("Rp %, .0f", totalKeuntunganBulan)));

        doc.close();
        rs.close();
        ps.close();

        JOptionPane.showMessageDialog(this, "Laporan berhasil disimpan di:\n" + fileToSave.getAbsolutePath());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mencetak laporan: " + e.getMessage());
        e.printStackTrace();
    }

    }//GEN-LAST:event_btnCetak1ActionPerformed

    private void btnCetak2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetak2ActionPerformed
int tahun = jycTahun.getYear();

JFileChooser chooser = new JFileChooser();
chooser.setDialogTitle("Simpan Laporan Keuangan Tahunan");
chooser.setSelectedFile(new java.io.File("Laporan_Keuangan_Tahun_" + tahun + ".pdf"));

int userSelection = chooser.showSaveDialog(this);
if (userSelection != JFileChooser.APPROVE_OPTION) return;

java.io.File fileToSave = chooser.getSelectedFile();
if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
    fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".pdf");
}

try (Connection conn = koneksi.dbKonek()) {
    com.itextpdf.text.Document doc = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
    com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new FileOutputStream(fileToSave));
    doc.open();

    // FONT
    com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD);
    com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
    com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);
    com.itextpdf.text.Font cellFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);

    // === JUDUL UTAMA ===
    com.itextpdf.text.Paragraph judul = new com.itextpdf.text.Paragraph("LAPORAN KEUANGAN TAHUN " + tahun, titleFont);
    judul.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    doc.add(judul);
    doc.add(new com.itextpdf.text.Paragraph(" "));

    // Query daftar bulan aktif
    String sqlBulan = """
        SELECT DISTINCT EXTRACT(MONTH FROM tanggal) AS bulan
        FROM keuangan
        WHERE EXTRACT(YEAR FROM tanggal) = ?
        ORDER BY bulan;
    """;
    PreparedStatement psBulan = conn.prepareStatement(sqlBulan);
    psBulan.setInt(1, tahun);
    ResultSet rsBulan = psBulan.executeQuery();

    double totalMasukTahun = 0, totalKeluarTahun = 0;

    while (rsBulan.next()) {
        int bulan = rsBulan.getInt("bulan");
        String namaBulan = java.time.Month.of(bulan).getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.forLanguageTag("id-ID"));

        // === HEADER BULAN ===
        doc.add(new com.itextpdf.text.Paragraph("Bulan " + namaBulan, headerFont));
        doc.add(new com.itextpdf.text.Paragraph(" "));

        // === AMBIL DAFTAR TANGGAL DALAM BULAN ===
        String sqlTanggal = """
            SELECT DISTINCT tanggal::date AS tgl
            FROM keuangan
            WHERE EXTRACT(MONTH FROM tanggal) = ? AND EXTRACT(YEAR FROM tanggal) = ?
            ORDER BY tgl;
        """;
        PreparedStatement psTanggal = conn.prepareStatement(sqlTanggal);
        psTanggal.setInt(1, bulan);
        psTanggal.setInt(2, tahun);
        ResultSet rsTanggal = psTanggal.executeQuery();

        double totalMasukBulan = 0, totalKeluarBulan = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        while (rsTanggal.next()) {
            java.sql.Date tgl = rsTanggal.getDate("tgl");

            // Ambil data transaksi per tanggal
            String sqlDetail = """
                SELECT jeniskeuangan, masuk, keluar
                FROM keuangan
                WHERE tanggal::date = ?
                ORDER BY idkeuangan;
            """;
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            psDetail.setDate(1, tgl);
            ResultSet rsDetail = psDetail.executeQuery();

            // === Hitung total harian ===
            double totalMasukHarian = 0, totalKeluarHarian = 0;
            java.util.List<String[]> rows = new java.util.ArrayList<>();

            while (rsDetail.next()) {
                String jenis = rsDetail.getString("jeniskeuangan");
                double masuk = rsDetail.getDouble("masuk");
                double keluar = rsDetail.getDouble("keluar");
                totalMasukHarian += masuk;
                totalKeluarHarian += keluar;
                rows.add(new String[]{
                    jenis,
                    String.format("Rp %, .0f", masuk),
                    String.format("Rp %, .0f", keluar)
                });
            }

            double saldoHarian = totalMasukHarian - totalKeluarHarian;

            // === HEADER TANGGAL ===
            doc.add(new com.itextpdf.text.Paragraph("Tanggal: " + sdf.format(tgl), boldFont));
            doc.add(new com.itextpdf.text.Paragraph(
                    String.format("Total Masuk: Rp %, .0f | Total Keluar: Rp %, .0f | Saldo: Rp %, .0f",
                            totalMasukHarian, totalKeluarHarian, saldoHarian), cellFont));
            doc.add(new com.itextpdf.text.Paragraph(" "));

            // === TABEL DETAIL ===
            com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 5f, 3f, 3f});
            table.addCell(new com.itextpdf.text.Phrase("No", headerFont));
            table.addCell(new com.itextpdf.text.Phrase("Jenis Keuangan", headerFont));
            table.addCell(new com.itextpdf.text.Phrase("Masuk", headerFont));
            table.addCell(new com.itextpdf.text.Phrase("Keluar", headerFont));

            int no = 1;
            for (String[] r : rows) {
                table.addCell(new com.itextpdf.text.Phrase(String.valueOf(no++), cellFont));
                table.addCell(new com.itextpdf.text.Phrase(r[0], cellFont));
                table.addCell(new com.itextpdf.text.Phrase(r[1], cellFont));
                table.addCell(new com.itextpdf.text.Phrase(r[2], cellFont));
            }

            doc.add(table);
            doc.add(new com.itextpdf.text.Paragraph(" "));

            // Update total bulan & tahun
            totalMasukBulan += totalMasukHarian;
            totalKeluarBulan += totalKeluarHarian;
            totalMasukTahun += totalMasukHarian;
            totalKeluarTahun += totalKeluarHarian;

            rsDetail.close();
            psDetail.close();
        }

        double saldoBulan = totalMasukBulan - totalKeluarBulan;
        doc.add(new com.itextpdf.text.Paragraph(String.format(
                "Total Bulan %s → Masuk: Rp %, .0f | Keluar: Rp %, .0f | Saldo: Rp %, .0f",
                namaBulan, totalMasukBulan, totalKeluarBulan, saldoBulan), boldFont));
        doc.add(new com.itextpdf.text.Paragraph(" "));
        doc.add(new com.itextpdf.text.Paragraph(" "));

        rsTanggal.close();
        psTanggal.close();
    }

    // === TOTAL AKHIR TAHUN ===
    double saldoTahun = totalMasukTahun - totalKeluarTahun;
    com.itextpdf.text.Paragraph totalTahun = new com.itextpdf.text.Paragraph(
            String.format("TOTAL AKHIR TAHUN → Masuk: Rp %, .0f | Keluar: Rp %, .0f | Saldo: Rp %, .0f",
                    totalMasukTahun, totalKeluarTahun, saldoTahun),
            new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 13, com.itextpdf.text.Font.BOLD)
    );
    totalTahun.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    doc.add(totalTahun);

    doc.close();
    JOptionPane.showMessageDialog(this, "Laporan tahunan berhasil disimpan di:\n" + fileToSave.getAbsolutePath());

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Gagal mencetak laporan tahunan: " + e.getMessage());
    e.printStackTrace();
}
    }//GEN-LAST:event_btnCetak2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnCetak1;
    private javax.swing.JButton btnCetak2;
    private javax.swing.JButton btnFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private com.toedter.calendar.JMonthChooser jmcBulan;
    private com.toedter.calendar.JYearChooser jycTahun;
    private javax.swing.JLabel lblKeluar;
    private javax.swing.JLabel lblKeluarBulan;
    private javax.swing.JLabel lblKeuntunganBulan;
    private javax.swing.JLabel lblMasuk;
    private javax.swing.JLabel lblMasukBulan;
    private javax.swing.JLabel lblSaldo;
    private javax.swing.JLabel lblTotalBulan;
    private javax.swing.JPanel panelUtama;
    private javax.swing.JTable tblDetails;
    private javax.swing.JTable tblKeuangan;
    private javax.swing.JTable tblSemua;
    // End of variables declaration//GEN-END:variables
}
