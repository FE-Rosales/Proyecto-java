/*
 * Pantalla de Gestion de Promociones y Descuentos.
 * Creada a mano (sin editor visual .form) para cubrir el requerimiento del
 * enunciado: "El sistema debe permitir al Administrador registrar y gestionar
 * Promociones y Descuentos asociados a paquetes".
 */
package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import modelo.Promocion;
import modelo.DescuentoPorcentaje;
import modelo.DescuentoMontoFijo;
import modelo.DescuentoTemporada;

public class frm_GestionPromociones extends javax.swing.JInternalFrame {

    private final gestores.GestorPromociones gestorPromociones;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField txtCodigo;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JComboBox<String> cmbTipo;
    private JTextField txtValor;
    private JTextField txtTemporada;
    private JLabel lblTemporada;
    private JTable tblPromociones;
    private DefaultTableModel modeloTabla;

    public frm_GestionPromociones(gestores.GestorPromociones gestorPromociones) {
        super("Gestion de Promociones", true, true, true, true);
        this.gestorPromociones = gestorPromociones;
        construirInterfaz();
        actualizarTabla();
        setSize(600, 430);
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));

        JPanel panelForm = new JPanel(new GridLayout(6, 2, 6, 6));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtCodigo = new JTextField();
        txtFechaInicio = new JTextField("dd/MM/yyyy");
        txtFechaFin = new JTextField("dd/MM/yyyy");
        cmbTipo = new JComboBox<>(new String[]{"Descuento por Porcentaje", "Descuento por Monto Fijo", "Descuento por Temporada"});
        txtValor = new JTextField();
        txtTemporada = new JTextField();
        lblTemporada = new JLabel("Nombre de temporada:");

        panelForm.add(new JLabel("Codigo:"));
        panelForm.add(txtCodigo);
        panelForm.add(new JLabel("Fecha inicio (dd/MM/yyyy):"));
        panelForm.add(txtFechaInicio);
        panelForm.add(new JLabel("Fecha fin (dd/MM/yyyy):"));
        panelForm.add(txtFechaFin);
        panelForm.add(new JLabel("Tipo de promocion:"));
        panelForm.add(cmbTipo);
        panelForm.add(new JLabel("Valor (ej. 0.15 = 15%, o monto en S/):"));
        panelForm.add(txtValor);
        panelForm.add(lblTemporada);
        panelForm.add(txtTemporada);

        cmbTipo.addActionListener(e -> {
            boolean esTemporada = cmbTipo.getSelectedIndex() == 2;
            txtTemporada.setEnabled(esTemporada);
            lblTemporada.setEnabled(esTemporada);
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnEliminar = new JButton("Eliminar por codigo");
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEliminar);

        modeloTabla = new DefaultTableModel(new String[]{"Codigo", "Tipo", "Vigente hasta"}, 0);
        tblPromociones = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tblPromociones);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrarPromocion());
        btnEliminar.addActionListener(e -> eliminarPromocion());
    }

    private void registrarPromocion() {
        try {
            String codigo = txtCodigo.getText().trim();
            LocalDate inicio = LocalDate.parse(txtFechaInicio.getText().trim(), FMT);
            LocalDate fin = LocalDate.parse(txtFechaFin.getText().trim(), FMT);
            double valor = Double.parseDouble(txtValor.getText().trim().replace(",", "."));

            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El codigo es obligatorio.");
                return;
            }

            Promocion nueva;
            switch (cmbTipo.getSelectedIndex()) {
                case 0:
                    nueva = new DescuentoPorcentaje(codigo, inicio, fin, valor);
                    break;
                case 1:
                    nueva = new DescuentoMontoFijo(codigo, inicio, fin, valor);
                    break;
                default:
                    String temporada = txtTemporada.getText().trim();
                    if (temporada.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Ingrese el nombre de la temporada.");
                        return;
                    }
                    nueva = new DescuentoTemporada(codigo, inicio, fin, temporada, valor);
                    break;
            }

            if (gestorPromociones.registrar(nueva)) {
                JOptionPane.showMessageDialog(this, "Promocion registrada correctamente.");
                limpiarCampos();
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar (limite alcanzado).");
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha invalido. Use dd/MM/yyyy.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El valor debe ser un numero (ej. 0.15).");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminarPromocion() {
        String codigo = JOptionPane.showInputDialog(this, "Codigo de la promocion a eliminar:");
        if (codigo == null || codigo.trim().isEmpty()) return;

        if (gestorPromociones.eliminar(codigo.trim())) {
            JOptionPane.showMessageDialog(this, "Promocion eliminada.");
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro una promocion con ese codigo.");
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Promocion p : gestorPromociones.listarTodas()) {
            String tipo = p.getClass().getSimpleName();
            modeloTabla.addRow(new Object[]{p.getCodigo(), tipo, p.getFechaFin()});
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        txtValor.setText("");
        txtTemporada.setText("");
    }
}
