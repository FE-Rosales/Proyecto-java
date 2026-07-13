/*
 * Pantalla de Gestion de Empleados.
 * Creada a mano (sin editor visual .form) para cubrir el requerimiento del
 * enunciado: "El sistema debe permitir al Administrador crear, modificar y
 * eliminar Empleados (Asesores de Viaje, Operadores, Administradores)".
 */
package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import modelo.Empleado;
import modelo.Administrador;
import modelo.AsesorDeViaje;
import modelo.Operador;

public class frm_GestionEmpleados extends javax.swing.JInternalFrame {

    private final gestores.GestorEmpleados gestorEmpleados;

    private JTextField txtDni;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JComboBox<String> cmbRol;
    private JTable tblEmpleados;
    private DefaultTableModel modeloTabla;

    public frm_GestionEmpleados(gestores.GestorEmpleados gestorEmpleados) {
        super("Gestion de Empleados", true, true, true, true);
        this.gestorEmpleados = gestorEmpleados;
        construirInterfaz();
        actualizarTabla();
        setSize(560, 430);
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));

        JPanel panelForm = new JPanel(new GridLayout(6, 2, 6, 6));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtDni = new JTextField();
        txtNombres = new JTextField();
        txtApellidos = new JTextField();
        txtUsuario = new JTextField();
        txtContrasena = new JPasswordField();
        cmbRol = new JComboBox<>(new String[]{"Administrador", "Asesor de Viaje", "Operador"});

        panelForm.add(new JLabel("DNI:"));
        panelForm.add(txtDni);
        panelForm.add(new JLabel("Nombres:"));
        panelForm.add(txtNombres);
        panelForm.add(new JLabel("Apellidos:"));
        panelForm.add(txtApellidos);
        panelForm.add(new JLabel("Usuario:"));
        panelForm.add(txtUsuario);
        panelForm.add(new JLabel("Contrasena:"));
        panelForm.add(txtContrasena);
        panelForm.add(new JLabel("Rol:"));
        panelForm.add(cmbRol);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnEliminar = new JButton("Eliminar por DNI");
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEliminar);

        modeloTabla = new DefaultTableModel(new String[]{"DNI", "Nombres", "Apellidos", "Usuario", "Rol"}, 0);
        tblEmpleados = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tblEmpleados);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado());
    }

    private void registrarEmpleado() {
        String dni = txtDni.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (dni.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        Empleado nuevo;
        String rol = (String) cmbRol.getSelectedItem();
        switch (rol) {
            case "Administrador":
                nuevo = new Administrador(dni, nombres, apellidos, usuario, contrasena);
                break;
            case "Asesor de Viaje":
                nuevo = new AsesorDeViaje(dni, nombres, apellidos, usuario, contrasena);
                break;
            default:
                nuevo = new Operador(dni, nombres, apellidos, usuario, contrasena);
                break;
        }

        if (gestorEmpleados.registrar(nuevo)) {
            JOptionPane.showMessageDialog(this, "Empleado registrado correctamente.");
            limpiarCampos();
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar (limite alcanzado).");
        }
    }

    private void eliminarEmpleado() {
        String dni = JOptionPane.showInputDialog(this, "DNI del empleado a eliminar:");
        if (dni == null || dni.trim().isEmpty()) return;

        if (gestorEmpleados.eliminar(dni.trim())) {
            JOptionPane.showMessageDialog(this, "Empleado eliminado.");
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro un empleado con ese DNI.");
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Empleado e : gestorEmpleados.listarTodos()) {
            modeloTabla.addRow(new Object[]{e.getDni(), e.getNombres(), e.getApellidos(), e.getUsuario(), e.getRol()});
        }
    }

    private void limpiarCampos() {
        txtDni.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtUsuario.setText("");
        txtContrasena.setText("");
    }
}
