/*
 * Pantalla de solo lectura para el Operador.
 * Creada a mano (sin editor visual .form) para cubrir el requerimiento del
 * enunciado: "el Operador accede solo a los itinerarios y cupos".
 */
package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import modelo.PaqueteTuristico;
import modelo.DiaItinerario;

public class frm_VerCupos extends javax.swing.JInternalFrame {

    private final gestores.GestorPaquetes gestorPaquetes;
    private JTable tblPaquetes;
    private DefaultTableModel modeloTabla;
    private JTextArea txtItinerario;

    public frm_VerCupos(gestores.GestorPaquetes gestorPaquetes) {
        super("Cupos e Itinerarios (solo lectura)", true, true, true, true);
        this.gestorPaquetes = gestorPaquetes;
        construirInterfaz();
        actualizarTabla();
        setSize(650, 450);
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));

        modeloTabla = new DefaultTableModel(new String[]{"Codigo", "Nombre", "Cupo disponible", "Cupo maximo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // solo lectura
            }
        };
        tblPaquetes = new JTable(modeloTabla);
        tblPaquetes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tblPaquetes);
        scrollTabla.setPreferredSize(new Dimension(600, 220));

        txtItinerario = new JTextArea(8, 40);
        txtItinerario.setEditable(false);
        txtItinerario.setLineWrap(true);
        JScrollPane scrollItinerario = new JScrollPane(txtItinerario);

        tblPaquetes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarItinerario();
        });

        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.add(new JLabel("Itinerario del paquete seleccionado:"), BorderLayout.NORTH);
        panelCentro.add(scrollItinerario, BorderLayout.CENTER);

        add(scrollTabla, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (PaqueteTuristico p : gestorPaquetes.listarTodos()) {
            modeloTabla.addRow(new Object[]{
                p.getCodigo(), p.getNombre(), p.getCupoDisponible(), p.getCupoMaximo(), p.getEstado()
            });
        }
    }

    private void mostrarItinerario() {
        int fila = tblPaquetes.getSelectedRow();
        if (fila == -1) return;

        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        PaqueteTuristico paquete = gestorPaquetes.buscarPorCodigo(codigo);
        if (paquete == null) return;

        DiaItinerario[] dias = paquete.getItinerario();
        if (dias.length == 0) {
            txtItinerario.setText("Este paquete aun no tiene itinerario registrado.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (DiaItinerario d : dias) {
            sb.append(d).append("\n");
        }
        txtItinerario.setText(sb.toString());
    }
}
