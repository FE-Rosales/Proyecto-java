package modelo;

import java.time.LocalDate;

public class Reserva {

    private static final int MAX_CLIENTES = 10;
    private static final int MAX_PAGOS = 20;
    private static final double DESCUENTO_MENOR = 0.5;   // 50% de descuento por pasajero menor
    private static final double PORCENTAJE_MINIMO = 0.3; // 30% minimo del total para confirmar

    private PaqueteTuristico paquete;
    private AsesorDeViaje asesor;
    private LocalDate fechaReserva;

    private Cliente[] clientes;
    private int contadorClientes;

    private int pasajerosAdultos;
    private int pasajerosMenores;
    private double precioTotal;

    private Pago[] pagos;
    private int contadorPagos;

    private EstadoReserva estado;

    private Resena resena; // Resena dejada por el cliente sobre esta reserva (puede ser null)

    public Reserva(PaqueteTuristico paquete, AsesorDeViaje asesor,
                    int pasajerosAdultos, int pasajerosMenores, LocalDate fechaReserva) {
        this.paquete = paquete;
        this.asesor = asesor;
        this.pasajerosAdultos = pasajerosAdultos;
        this.pasajerosMenores = pasajerosMenores;
        this.fechaReserva = fechaReserva;
        this.estado = EstadoReserva.PENDIENTE;

        this.clientes = new Cliente[MAX_CLIENTES];
        this.pagos = new Pago[MAX_PAGOS];

        this.precioTotal = calcularPrecioTotal();
    }

    private double calcularPrecioTotal() {
        double precioBaseConDescuento = paquete.getPrecioFinal(); 
        double totalAdultos = pasajerosAdultos * precioBaseConDescuento;
        double totalMenores = pasajerosMenores * precioBaseConDescuento * (1 - DESCUENTO_MENOR);
        return totalAdultos + totalMenores;
    }

    public boolean agregarCliente(Cliente c) {
        if (contadorClientes >= MAX_CLIENTES) return false;
        clientes[contadorClientes] = c;
        contadorClientes++;
        return true;
    }

    public Cliente[] getClientes() {
        Cliente[] copia = new Cliente[contadorClientes];
        for (int i = 0; i < contadorClientes; i++) {
            copia[i] = clientes[i];
        }
        return copia;
    }

    public boolean registrarPago(Pago pago) {
        if (contadorPagos >= MAX_PAGOS) return false;
        pagos[contadorPagos] = pago;
        contadorPagos++;
        actualizarEstadoSegunPago();
        return true;
    }

    public Pago[] getPagos() {
        Pago[] copia = new Pago[contadorPagos];
        for (int i = 0; i < contadorPagos; i++) {
            copia[i] = pagos[i];
        }
        return copia;
    }

    public double getMontoAbonado() {
        double total = 0;
        for (int i = 0; i < contadorPagos; i++) {
            total += pagos[i].getMonto();
        }
        return total;
    }

    public double getSaldoPendiente() {
        return precioTotal - getMontoAbonado();
    }

    private void actualizarEstadoSegunPago() {
        if (estado == EstadoReserva.CANCELADA) return;
        double minimoRequerido = precioTotal * PORCENTAJE_MINIMO;
        if (getMontoAbonado() >= minimoRequerido) {
            estado = EstadoReserva.CONFIRMADA;
        }
    }

    public void cancelar() {
        estado = EstadoReserva.CANCELADA;
    }

    /** Asocia una resena a esta reserva, una vez que el cliente la completa. */
    public void registrarResena(Resena r) {
        this.resena = r;
    }

    public Resena getResena() {
        return resena;
    }

    public PaqueteTuristico getPaquete() { return paquete; }
    public AsesorDeViaje getAsesor() { return asesor; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public int getPasajerosAdultos() { return pasajerosAdultos; }
    public int getPasajerosMenores() { return pasajerosMenores; }
    public double getPrecioTotal() { return precioTotal; }
    public EstadoReserva getEstado() { return estado; }

    @Override
    public String toString() {
        return "Reserva [" + paquete.getCodigo() + "] - " + contadorClientes + " pasajero(s) - "
                + "Total: S/ " + precioTotal + " - Saldo: S/ " + getSaldoPendiente()
                + " [" + estado + "]";
    }
}