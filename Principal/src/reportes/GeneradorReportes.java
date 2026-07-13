package reportes;

import gestores.GestorReservas;
import gestores.GestorPaquetes;
import gestores.GestorClientes;
import modelo.Reserva;
import modelo.Cliente;
import modelo.DestinoTuristico;
import modelo.Resena;
import modelo.EstadoReserva;
import java.time.LocalDate;

/**
 * Genera los reportes del sistema, consultando las Gestoras correspondientes.
 * No almacena datos propios: solo lee y procesa la informacion que ya
 * administran GestorReservas, GestorPaquetes y GestorClientes.
 *
 * Cada reporte tiene dos versiones:
 *  - un metodo que devuelve un String formateado (para mostrarlo como texto)
 *  - un metodo "...Tabla()" que devuelve un Object[][] con filas y columnas
 *    (para mostrarlo en un JTable en la GUI)
 * Ambas versiones comparten la misma logica de calculo, para no duplicarla.
 */
public class GeneradorReportes {

    private GestorReservas gestorReservas;
    private GestorPaquetes gestorPaquetes;
    private GestorClientes gestorClientes;

    public GeneradorReportes(GestorReservas gestorReservas, GestorPaquetes gestorPaquetes,
                              GestorClientes gestorClientes) {
        this.gestorReservas = gestorReservas;
        this.gestorPaquetes = gestorPaquetes;
        this.gestorClientes = gestorClientes;
    }

    // ==================== REPORTE 1: Reservas por paquete ====================

    /** Columnas: Codigo Paquete | Fecha Reserva | Pasajeros | Precio Total | Estado */
    public Object[][] reservasPorPaqueteTabla(String codigoPaquete) {
        Reserva[] reservas = gestorReservas.listarPorPaquete(codigoPaquete);
        Object[][] filas = new Object[reservas.length][5];
        for (int i = 0; i < reservas.length; i++) {
            Reserva r = reservas[i];
            filas[i][0] = r.getPaquete().getCodigo();
            filas[i][1] = r.getFechaReserva();
            filas[i][2] = (r.getPasajerosAdultos() + r.getPasajerosMenores());
            filas[i][3] = r.getPrecioTotal();
            filas[i][4] = r.getEstado();
        }
        return filas;
    }

    public String reservasPorPaquete(String codigoPaquete) {
        Reserva[] reservas = gestorReservas.listarPorPaquete(codigoPaquete);
        StringBuilder sb = new StringBuilder();
        sb.append("Reservas del paquete ").append(codigoPaquete).append(":\n");
        for (int i = 0; i < reservas.length; i++) {
            sb.append(" - ").append(reservas[i]).append("\n");
        }
        sb.append("Total: ").append(reservas.length).append(" reserva(s)\n");
        return sb.toString();
    }

    // ==================== REPORTE 2: Reservas por rango de fechas ====================

    /** Columnas: Codigo Paquete | Fecha Reserva | Pasajeros | Precio Total | Estado */
    public Object[][] reservasPorRangoFechasTabla(LocalDate desde, LocalDate hasta) {
        Reserva[] reservas = gestorReservas.listarPorRangoFechas(desde, hasta);
        Object[][] filas = new Object[reservas.length][5];
        for (int i = 0; i < reservas.length; i++) {
            Reserva r = reservas[i];
            filas[i][0] = r.getPaquete().getCodigo();
            filas[i][1] = r.getFechaReserva();
            filas[i][2] = (r.getPasajerosAdultos() + r.getPasajerosMenores());
            filas[i][3] = r.getPrecioTotal();
            filas[i][4] = r.getEstado();
        }
        return filas;
    }

    public String reservasPorRangoFechas(LocalDate desde, LocalDate hasta) {
        Reserva[] reservas = gestorReservas.listarPorRangoFechas(desde, hasta);
        StringBuilder sb = new StringBuilder();
        sb.append("Reservas entre ").append(desde).append(" y ").append(hasta).append(":\n");
        for (int i = 0; i < reservas.length; i++) {
            sb.append(" - ").append(reservas[i]).append("\n");
        }
        sb.append("Total: ").append(reservas.length).append(" reserva(s)\n");
        return sb.toString();
    }

    // ==================== REPORTE 3: Destinos mas solicitados ====================

    /** Columnas: Destino | Cantidad de Reservas. Ya viene ordenado de mayor a menor. */
    public Object[][] destinosMasSolicitadosTabla() {
        String[] nombres = new String[100];
        int[] conteos = new int[100];
        int cantidad = 0;

        Reserva[] todas = gestorReservas.listarTodas();
        for (int i = 0; i < todas.length; i++) {
            DestinoTuristico[] destinos = todas[i].getPaquete().getDestinos();
            for (int j = 0; j < destinos.length; j++) {
                String nombre = destinos[j].getNombre();
                int indice = buscarIndice(nombres, cantidad, nombre);
                if (indice == -1) {
                    nombres[cantidad] = nombre;
                    conteos[cantidad] = 1;
                    cantidad++;
                } else {
                    conteos[indice]++;
                }
            }
        }

        // Ordenar de mayor a menor con burbuja (mismo algoritmo visto en clase)
        for (int i = 0; i < cantidad - 1; i++) {
            for (int j = 0; j < cantidad - 1 - i; j++) {
                if (conteos[j] < conteos[j + 1]) {
                    int tempConteo = conteos[j];
                    conteos[j] = conteos[j + 1];
                    conteos[j + 1] = tempConteo;

                    String tempNombre = nombres[j];
                    nombres[j] = nombres[j + 1];
                    nombres[j + 1] = tempNombre;
                }
            }
        }

        Object[][] filas = new Object[cantidad][2];
        for (int i = 0; i < cantidad; i++) {
            filas[i][0] = nombres[i];
            filas[i][1] = conteos[i];
        }
        return filas;
    }

    public String destinosMasSolicitados() {
        Object[][] filas = destinosMasSolicitadosTabla();
        StringBuilder sb = new StringBuilder();
        sb.append("Destinos mas solicitados:\n");
        for (int i = 0; i < filas.length; i++) {
            sb.append(" - ").append(filas[i][0]).append(": ").append(filas[i][1]).append(" reserva(s)\n");
        }
        return sb.toString();
    }

    // ==================== REPORTE 4: Ingresos por asesor ====================

    /** Columnas: Asesor | Ingresos (S/) */
    public Object[][] ingresosPorAsesorTabla() {
        String[] asesores = new String[50];
        double[] totales = new double[50];
        int cantidad = 0;

        Reserva[] todas = gestorReservas.listarTodas();
        for (int i = 0; i < todas.length; i++) {
            String usuario = todas[i].getAsesor().getUsuario();
            double monto = todas[i].getMontoAbonado();

            int indice = buscarIndice(asesores, cantidad, usuario);
            if (indice == -1) {
                asesores[cantidad] = usuario;
                totales[cantidad] = monto;
                cantidad++;
            } else {
                totales[indice] += monto;
            }
        }

        Object[][] filas = new Object[cantidad][2];
        for (int i = 0; i < cantidad; i++) {
            filas[i][0] = asesores[i];
            filas[i][1] = totales[i];
        }
        return filas;
    }

    public String ingresosPorAsesor() {
        Object[][] filas = ingresosPorAsesorTabla();
        StringBuilder sb = new StringBuilder();
        sb.append("Ingresos por asesor:\n");
        for (int i = 0; i < filas.length; i++) {
            sb.append(" - ").append(filas[i][0]).append(": S/ ").append(filas[i][1]).append("\n");
        }
        return sb.toString();
    }

    // ==================== REPORTE 5: Clientes con pagos pendientes ====================

    /** Columnas: Cliente | Documento | Paquete | Saldo Pendiente (S/) */
    public Object[][] clientesPendientesPagoTabla() {
        String[] nombres = new String[500];
        String[] documentos = new String[500];
        String[] paquetes = new String[500];
        double[] saldos = new double[500];
        int cantidad = 0;

        Reserva[] todas = gestorReservas.listarTodas();
        for (int i = 0; i < todas.length; i++) {
            boolean tieneSaldo = todas[i].getSaldoPendiente() > 0;
            boolean noCancelada = todas[i].getEstado() != EstadoReserva.CANCELADA;

            if (tieneSaldo && noCancelada) {
                Cliente[] clientes = todas[i].getClientes();
                for (int j = 0; j < clientes.length; j++) {
                    nombres[cantidad] = clientes[j].getNombres() + " " + clientes[j].getApellidos();
                    documentos[cantidad] = clientes[j].getDocumento();
                    paquetes[cantidad] = todas[i].getPaquete().getCodigo();
                    saldos[cantidad] = todas[i].getSaldoPendiente();
                    cantidad++;
                }
            }
        }

        Object[][] filas = new Object[cantidad][4];
        for (int i = 0; i < cantidad; i++) {
            filas[i][0] = nombres[i];
            filas[i][1] = documentos[i];
            filas[i][2] = paquetes[i];
            filas[i][3] = saldos[i];
        }
        return filas;
    }

    public String clientesPendientesPago() {
        Object[][] filas = clientesPendientesPagoTabla();
        StringBuilder sb = new StringBuilder();
        sb.append("Clientes con pagos pendientes:\n");
        for (int i = 0; i < filas.length; i++) {
            sb.append(" - ").append(filas[i][0]).append(" (").append(filas[i][1]).append(") - Paquete ")
              .append(filas[i][2]).append(" - Saldo: S/ ").append(filas[i][3]).append("\n");
        }
        return sb.toString();
    }

    // ==================== REPORTE ADICIONAL: Paquetes mejor calificados ====================

    /** Columnas: Codigo Paquete | Calificacion Promedio | Cantidad de Resenas */
    public Object[][] paquetesMejorCalificadosTabla() {
        String[] codigos = new String[200];
        double[] sumaCalificaciones = new double[200];
        int[] cantidadResenas = new int[200];
        int cantidad = 0;

        Reserva[] todas = gestorReservas.listarTodas();
        for (int i = 0; i < todas.length; i++) {
            Resena r = todas[i].getResena();
            if (r != null) {
                String codigo = r.getPaquete().getCodigo();
                int indice = buscarIndice(codigos, cantidad, codigo);
                if (indice == -1) {
                    codigos[cantidad] = codigo;
                    sumaCalificaciones[cantidad] = r.getCalificacion();
                    cantidadResenas[cantidad] = 1;
                    cantidad++;
                } else {
                    sumaCalificaciones[indice] += r.getCalificacion();
                    cantidadResenas[indice]++;
                }
            }
        }

        Object[][] filas = new Object[cantidad][3];
        for (int i = 0; i < cantidad; i++) {
            double promedio = sumaCalificaciones[i] / cantidadResenas[i];
            filas[i][0] = codigos[i];
            filas[i][1] = promedio;
            filas[i][2] = cantidadResenas[i];
        }
        return filas;
    }

    public String paquetesMejorCalificados() {
        Object[][] filas = paquetesMejorCalificadosTabla();
        StringBuilder sb = new StringBuilder();
        sb.append("Paquetes mejor calificados:\n");
        for (int i = 0; i < filas.length; i++) {
            sb.append(" - ").append(filas[i][0]).append(": ").append(filas[i][1])
              .append("/5 (").append(filas[i][2]).append(" resena(s))\n");
        }
        return sb.toString();
    }

    /** Busca un texto dentro de un arreglo, hasta la posicion "cantidad". Devuelve -1 si no esta. */
    private int buscarIndice(String[] arreglo, int cantidad, String valor) {
        for (int i = 0; i < cantidad; i++) {
            if (arreglo[i].equals(valor)) {
                return i;
            }
        }
        return -1;
    }
}
