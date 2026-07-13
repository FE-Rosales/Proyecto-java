package modelo;
/**
 * Representa un paquete turistico ofrecido por la agencia. Contiene,
 * mediante arreglos internos de tamano fijo (patron arreglo + contador),
 * los destinos incluidos, los servicios ofrecidos y el itinerario dia a dia.
 */
import java.time.LocalDate;

public class PaqueteTuristico {

    private static final int MAX_DESTINOS = 5;
    private static final int MAX_SERVICIOS = 10;
    private static final int MAX_DIAS_ITINERARIO = 30;

    private String codigo;
    private String nombre;
    private TipoPaquete tipo;
    private int duracionDias;
    private int duracionNoches;
    private double precioPorPersona;
    private Promocion promocion; 
    private LocalDate fechaSalida;
    private LocalDate fechaRetorno;
    private int cupoMaximo;
    private int cupoDisponible;
    private EstadoPaquete estado;

    private DestinoTuristico[] destinos;
    private int contadorDestinos;

    private ServicioIncluido[] servicios;
    private int contadorServicios;

    private DiaItinerario[] itinerario;
    private int contadorItinerario;

    public PaqueteTuristico(String codigo, String nombre, TipoPaquete tipo,
                             int duracionDias, int duracionNoches, double precioPorPersona,
                             LocalDate fechaSalida, LocalDate fechaRetorno, int cupoMaximo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.duracionDias = duracionDias;
        this.duracionNoches = duracionNoches;
        this.precioPorPersona = precioPorPersona;
        
        this.fechaSalida = fechaSalida;
        this.fechaRetorno = fechaRetorno;
        this.cupoMaximo = cupoMaximo;
        this.cupoDisponible = cupoMaximo;
        this.estado = EstadoPaquete.ACTIVO;

        this.destinos = new DestinoTuristico[MAX_DESTINOS];
        this.servicios = new ServicioIncluido[MAX_SERVICIOS];
        this.itinerario = new DiaItinerario[MAX_DIAS_ITINERARIO];
    }
/** Agrega un destino al paquete si aun hay espacio en el arreglo. */
    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }
    
    public double getPrecioFinal() {
        if (promocion != null && promocion.estaVigente(LocalDate.now())) {
            // Restamos el descuento al precio original
            return precioPorPersona - promocion.calcularDescuento(precioPorPersona);
        }
        return precioPorPersona; // Si no hay promo, devuelve el precio normal
    }
    
    public boolean agregarDestino(DestinoTuristico d) {
        if (contadorDestinos >= MAX_DESTINOS) return false;
        destinos[contadorDestinos] = d;
        contadorDestinos++;
        return true;
    }

    public boolean agregarServicio(ServicioIncluido s) {
        if (contadorServicios >= MAX_SERVICIOS) return false;
        servicios[contadorServicios] = s;
        contadorServicios++;
        return true;
    }

    public boolean agregarDiaItinerario(DiaItinerario d) {
        if (contadorItinerario >= MAX_DIAS_ITINERARIO) return false;
        itinerario[contadorItinerario] = d;
        contadorItinerario++;
        return true;
    }

    public DestinoTuristico[] getDestinos() {
        DestinoTuristico[] copia = new DestinoTuristico[contadorDestinos];
        for (int i = 0; i < contadorDestinos; i++) {
            copia[i] = destinos[i];
        }
        return copia;
    }

    public ServicioIncluido[] getServicios() {
        ServicioIncluido[] copia = new ServicioIncluido[contadorServicios];
        for (int i = 0; i < contadorServicios; i++) {
            copia[i] = servicios[i];
        }
        return copia;
    }

    public DiaItinerario[] getItinerario() {
        DiaItinerario[] copia = new DiaItinerario[contadorItinerario];
        for (int i = 0; i < contadorItinerario; i++) {
            copia[i] = itinerario[i];
        }
        return copia;
    }

    public boolean hayCupoDisponible(int cantidad) {
        return cupoDisponible >= cantidad && estado == EstadoPaquete.ACTIVO;
    }
 /**
     * Descuenta cupos del paquete (llamado al confirmar una reserva).
     * Si el cupo llega a cero, el estado cambia automaticamente a AGOTADO,
     * para que nunca queden desincronizados cupo y estado.
     */
    public void reducirCupo(int cantidad) {
        cupoDisponible -= cantidad;
        if (cupoDisponible <= 0) {
            cupoDisponible = 0;
            estado = EstadoPaquete.AGOTADO;
        }
    }
  /** Cancela el paquete manualmente (accion del Administrador). */
    public void cancelar() {
        estado = EstadoPaquete.CANCELADO;
    }

    public String getCodigo() {
        return codigo; 
    }
    public String getNombre() {
        return nombre; 
    }
    public TipoPaquete getTipo() {
        return tipo; 
    }
    public int getDuracionDias() {
        return duracionDias;
    }
    public int getDuracionNoches() {
        return duracionNoches; 
    }
    public double getPrecioPorPersona() {
        return precioPorPersona; 
    }
    public LocalDate getFechaSalida() {
        return fechaSalida; 
    }
    public LocalDate getFechaRetorno() {
        return fechaRetorno; 
    }
    public int getCupoMaximo() { 
        return cupoMaximo; 
    }
    public int getCupoDisponible() {
        return cupoDisponible; 
    }
    public EstadoPaquete getEstado() {
        return estado; 
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + tipo + ") - S/ " + precioPorPersona
                + " - Cupos: " + cupoDisponible + "/" + cupoMaximo + " [" + estado + "]";
    }
}