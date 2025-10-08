/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kasir;
import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class DiagramBatang extends JPanel {
    private final java.util.List<String> namaBarang = new ArrayList<>();
    private final java.util.List<Integer> totalTerjual = new ArrayList<>();

    private final java.sql.Date startDate;
    private final java.sql.Date endDate;

    public DiagramBatang(java.sql.Date start, java.sql.Date end) {
        this.startDate = start;
        this.endDate = end;
        loadData();
        setPreferredSize(new Dimension(900, 400));
        setBackground(new Color(245, 247, 250)); // warna latar lembut
    }

    private void loadData() {
        namaBarang.clear();
        totalTerjual.clear();
        try (Connection conn = koneksi.dbKonek()) {
            String sql = """
                SELECT 
                    b.nama AS namabarang,
                    SUM(d.jumlah) AS total_terjual
                FROM 
                    detailtransaksi d
                JOIN 
                    barang b ON d.kodebarang = b.kodebarang
                JOIN 
                    transaksi t ON d.idtransaksi = t.idtransaksi
                WHERE 
                    t.tgl_transaksi BETWEEN ? AND ?
                GROUP BY 
                    b.nama
                ORDER BY 
                    total_terjual DESC
                LIMIT 3
            """;
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDate(1, startDate);
            pst.setDate(2, endDate);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                namaBarang.add(rs.getString("namabarang"));
                totalTerjual.add(rs.getInt("total_terjual"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (namaBarang.isEmpty()) {
            drawEmptyMessage(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 80;
        int barSpacing = 40;
        int barWidth = (width - (2 * padding) - (barSpacing * (namaBarang.size() - 1))) / namaBarang.size();

        int maxValue = Collections.max(totalTerjual);

        // Judul dengan periode
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.setColor(new Color(40, 40, 40));
        String title = String.format("3 Produk Terlaris (%s s.d %s)",
                startDate.toString(), endDate.toString());
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (width - titleWidth) / 2, 40);

        // Sumbu dasar (garis horizontal)
        int axisY = height - padding;
        g2.setColor(new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(padding - 20, axisY, width - padding + 20, axisY);

        // Gambar batang
        for (int i = 0; i < namaBarang.size(); i++) {
            int value = totalTerjual.get(i);
            double scale = (double) value / maxValue;
            int barHeight = (int) (scale * (height - 2 * padding));

            int x = padding + i * (barWidth + barSpacing);
            int y = axisY - barHeight;

            // Gradasi batang
            GradientPaint gradient = new GradientPaint(
                    x, y, new Color(99, 150, 255),
                    x, y + barHeight, new Color(140, 190, 255)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(x, y, barWidth, barHeight);

            // Nilai di atas batang
            g2.setColor(new Color(50, 50, 50));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            String valText = String.valueOf(value);
            int valWidth = g2.getFontMetrics().stringWidth(valText);
            g2.drawString(valText, x + (barWidth - valWidth) / 2, y - 8);

            // Label nama barang di bawah batang
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            String label = namaBarang.get(i);
            if (label.length() > 12) label = label.substring(0, 12) + "...";
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.setColor(new Color(70, 70, 70));
            g2.drawString(label, x + (barWidth - labelWidth) / 2, axisY + 20);
        }

        g2.dispose();
    }

    private void drawEmptyMessage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        g2.setColor(new Color(120, 120, 120));
        String msg = "Tidak ada data penjualan untuk periode ini.";
        int textWidth = g2.getFontMetrics().stringWidth(msg);
        g2.drawString(msg, (getWidth() - textWidth) / 2, getHeight() / 2);
        g2.dispose();
    }
}
