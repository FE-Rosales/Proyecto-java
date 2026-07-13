/*
 * Pantalla de Reportes, reescrita para permitir seleccionar el tipo de
 * reporte desde un JComboBox y mostrar el resultado en una tabla (JTable),
 * usando las versiones tabulares de GeneradorReportes.
 */
package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import reportes.GeneradorReportes;

public class frm_Reportes extends javax.swing.JInternalFrame {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final GeneradorReportes reportes;

    private JComboBox<String> cmbTipoReporte;
    private JPanel panelFiltros;
    private CardLayout cardLayout;

    private JTextField txtCodigoPaquete;
    private JTextField txtFechaDesde;
    private JTextField txtFechaHasta;

    private JTable tblResultados;
    private DefaultTableModel modeloTabla;
    private JLabel lblCantidadResultados;

    private static final String[] TIPOS_REPORTE = {
        "Reservas por Paquete",
        "Reservas por Rango de Fechas",
        "Destinos mas Solicitados",
        "Ingresos por Asesor",
        "Clientes con Pagos Pendientes",
        "Paquetes Mejor Calificados"
    };

    public frm_Reportes(gestores.GestorReservas gestorReservas, gestores.GestorPaquetes gestorPaquetes,
                         gestores.GestorClientes gestorClientes) {
        super("Reportes", true, true, true, true);
        this.reportes = new GeneradorReportes(gestorReservas, gestorPaquetes, gestorClientes);
        construirInterfaz();
        setSize(720, 480);
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));

        // ---- Panel superior: combo + filtros dinamicos + boton ----
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel panelCombo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panelCombo.add(new JLabel("Seleccione un reporte:"));
        cmbTipoReporte = new JComboBox<>(TIPOS_REPORTE);
        panelCombo.add(cmbTipoReporte);
        panelSuperior.add(panelCombo);

        // Filtros que cambian segun el reporte elegido (CardLayout)
        cardLayout = new CardLayout();
        panelFiltros = new JPanel(cardLayout);

        JPanel cardPaquete = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        txtCodigoPaquete = new JTextField(12);
        cardPaquete.add(new JLabel("Codigo del paquete:"));
        cardPaquete.add(txtCodigoPaquete);

        JPanel cardFechas = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        txtFechaDesde = new JTextField("dd/MM/yyyy", 10);
        txtFechaHasta = new JTextField("dd/MM/yyyy", 10);
        cardFechas.add(new JLabel("Desde:"));
        cardFechas.add(txtFechaDesde);
        cardFechas.add(new JLabel("Hasta:"));
        cardFechas.add(txtFechaHasta);

        JPanel cardVacio = new JPanel();

        panelFiltros.add(cardPaquete, "paquete");
        panelFiltros.add(cardFechas, "fechas");
        panelFiltros.add(cardVacio, "ninguno");
        panelSuperior.add(panelFiltros);

        cmbTipoReporte.addActionListener(e -> actualizarFiltroVisible());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton btnGenerar = new JButton("Generar Reporte");
        btnGenerar.addActionListener(e -> generarReporte());
        panelBoton.add(btnGenerar);
        lblCantidadResultados = new JLabel(" ");
        panelBoton.add(lblCantidadResultados);
        panelSuperior.add(panelBoton);

        add(panelSuperior, BorderLayout.NORTH);

        // ---- Tabla de resultados ----
        modeloTabla = new DefaultTableModel();
        tblResultados = new JTable(modeloTabla);
        add(new JScrollPane(tblResultados), BorderLayout.CENTER);

        actualizarFiltroVisible();
    }

    /** Muestra el filtro correspondiente (codigo de paquete, rango de fechas, o ninguno). */
    private void actualizarFiltroVisible() {
        int indice = cmbTipoReporte.getSelectedIndex();
        if (indice == 0) {
            cardLayout.show(panelFiltros, "paquete");
        } else if (indice == 1) {
            cardLayout.show(panelFiltros, "fechas");
        } else {
            cardLayout.show(panelFiltros, "ninguno");
        }
    }

    private void generarReporte() {
        try {
            int indice = cmbTipoReporte.getSelectedIndex();
            switch (indice) {
                case 0:
                    generarReservasPorPaquete();
                    break;
                case 1:
                    generarReservasPorRangoFechas();
                    break;
                case 2:
                    mostrarTabla(new String[]{"Destino", "Cantidad de Reservas"}, reportes.destinosMasSolicitadosTabla());
                    break;
                case 3:
                    mostrarTabla(new String[]{"Asesor", "Ingresos (S/)"}, reportes.ingresosPorAsesorTabla());
                    break;
                case 4:
                    mostrarTabla(new String[]{"Cliente", "Documento", "Paquete", "Saldo Pendiente (S/)"}, reportes.clientesPendientesPagoTabla());
                    break;
                case 5:
                    mostrarTabla(new String[]{"Paquete", "Calificacion Promedio", "Cantidad de Resenas"}, reportes.paquetesMejorCalificadosTabla());
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage());
        }
    }

    private void generarReservasPorPaquete() {
        String codigo = txtCodigoPaquete.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el codigo del paquete.");
            return;
        }
        Object[][] filas = reportes.reservasPorPaqueteTabla(codigo);
        mostrarTabla(new String[]{"Paquete", "Fecha Reserva", "Pasajeros", "Precio Total (S/)", "Estado"}, filas);
    }

    private void generarReservasPorRangoFechas() {
        try {
            LocalDate desde = LocalDate.parse(txtFechaDesde.getText().trim(), FMT);
            LocalDate hasta = LocalDate.parse(txtFechaHasta.getText().trim(), FMT);
            Object[][] filas = reportes.reservasPorRangoFechasTabla(desde, hasta);
            mostrarTabla(new String[]{"Paquete", "Fecha Reserva", "Pasajeros", "Precio Total (S/)", "Estado"}, filas);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha invalido. Use dd/MM/yyyy.");
        }
    }

    /** Reemplaza las columnas y filas de la tabla con el resultado del reporte elegido. */
    private void mostrarTabla(String[] columnas, Object[][] filas) {
        modeloTabla.setDataVector(filas, columnas);
        lblCantidadResultados.setText(filas.length + " resultado(s)");
    }
}
