package modelo;
/**
 * Genera el comprobante de una Reserva confirmada, con todos los datos
 * relevantes (cliente, paquete, fechas, servicios, precio y saldo)
 * listos para mostrarse en pantalla desde la GUI.
 */         
import java.time.LocalDate;

public class Voucher {

    private String codigoReserva;
    private LocalDate fechaEmision;
    private Reserva reserva;

    public Voucher(Reserva reserva) {
        this.reserva = reserva;
        this.fechaEmision = LocalDate.now();
        this.codigoReserva = generarCodigo();
    }

    private String generarCodigo() {
        return "VCH-" + reserva.getPaquete().getCodigo() + "-"
                + fechaEmision.getYear() + fechaEmision.getMonthValue() + fechaEmision.getDayOfMonth();
    }
/**
     * Arma el texto completo del voucher, listo para mostrarse en pantalla.
     * Devuelve un String (no imprime directamente) para que la GUI decida
     * donde y como mostrarlo.
     */
    public String generarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== VOUCHER DE RESERVA =====\n");
        sb.append("Codigo: ").append(codigoReserva).append("\n");
        sb.append("Fecha de emision: ").append(fechaEmision).append("\n\n");

        sb.append("Paquete: ").append(reserva.getPaquete().getNombre()).append("\n");
        sb.append("Fecha de salida: ").append(reserva.getPaquete().getFechaSalida()).append("\n");
        sb.append("Fecha de retorno: ").append(reserva.getPaquete().getFechaRetorno()).append("\n\n");

        sb.append("Pasajeros adultos: ").append(reserva.getPasajerosAdultos()).append("\n");
        sb.append("Pasajeros menores: ").append(reserva.getPasajerosMenores()).append("\n\n");

        sb.append("Clientes:\n");
        for (Cliente c : reserva.getClientes()) {
            sb.append(" - ").append(c).append("\n");
        }

        sb.append("\nServicios incluidos:\n");
        for (ServicioIncluido s : reserva.getPaquete().getServicios()) {
            sb.append(" - ").append(s).append("\n");
        }

        sb.append("\nPrecio total: S/ ").append(reserva.getPrecioTotal()).append("\n");
        sb.append("Monto abonado: S/ ").append(reserva.getMontoAbonado()).append("\n");
        sb.append("Saldo pendiente: S/ ").append(reserva.getSaldoPendiente()).append("\n");
        sb.append("Estado: ").append(reserva.getEstado()).append("\n");

        return sb.toString();
    }

    public String getCodigoReserva() { return codigoReserva; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public Reserva getReserva() { return reserva; }
}