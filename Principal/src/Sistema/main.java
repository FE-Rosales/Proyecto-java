/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;

import gestores.GestorClientes;
import gestores.GestorDestinos;
import gestores.GestorEmpleados;
import gestores.GestorPaquetes;
import gestores.GestorPromociones;
import gestores.GestorReservas;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import modelo.Administrador;
import modelo.AsesorDeViaje;
import modelo.Cliente;
import modelo.DescuentoMontoFijo;
import modelo.DescuentoPorcentaje;
import modelo.DescuentoTemporada;
import modelo.DestinoTuristico;
import modelo.DiaItinerario;
import modelo.Empleado;
import modelo.MetodoPago;
import modelo.Operador;
import modelo.Pago;
import modelo.PaqueteTuristico;
import modelo.Promocion;
import modelo.Resena;
import modelo.Reserva;
import modelo.ServicioIncluido;
import modelo.TipoPago;
import modelo.TipoPaquete;
import modelo.Voucher;
import reportes.GeneradorReportes;

/**
 *
 * @author L33424
 */
public class main {
 
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
 
    private static GestorEmpleados gestorEmpleados = new GestorEmpleados();
    private static GestorDestinos gestorDestinos = new GestorDestinos();
    private static GestorPaquetes gestorPaquetes = new GestorPaquetes();
    private static GestorClientes gestorClientes = new GestorClientes();
    private static GestorPromociones gestorPromociones = new GestorPromociones();
    private static GestorReservas gestorReservas = new GestorReservas();
    private static GeneradorReportes reportes =
            new GeneradorReportes(gestorReservas, gestorPaquetes, gestorClientes);
 
    public static void main(String[] args) {
        cargarDatosIniciales();
 
        System.out.println("=========================================");
        System.out.println(" SISTEMA DE GESTION - AGENCIA DE VIAJES");
        System.out.println("=========================================");
 
        boolean salirSistema = false;
        while (!salirSistema) {
            Empleado sesion = login();
            if (sesion == null) {
                System.out.println("\n¿Desea intentar de nuevo? (S/N)");
                if (!sc.nextLine().trim().equalsIgnoreCase("S")) {
                    salirSistema = true;
                }
                continue;
            }
 
            System.out.println("\nBienvenido(a), " + sesion.getNombres() + " [" + sesion.getRol() + "]");
            System.out.println("Permisos: " + sesion.mostrarPermisos());
 
            if (sesion instanceof Administrador) {
                menuAdministrador((Administrador) sesion);
            } else if (sesion instanceof AsesorDeViaje) {
                menuAsesor((AsesorDeViaje) sesion);
            } else if (sesion instanceof Operador) {
                menuOperador((Operador) sesion);
            }
 
            System.out.println("\nSesion cerrada. ¿Desea salir del sistema? (S/N)");
            if (sc.nextLine().trim().equalsIgnoreCase("S")) {
                salirSistema = true;
            }
        }
 
        System.out.println("Gracias por usar el sistema.");
    }
 
    // ==================== DATOS INICIALES ====================
 
    /** Precarga un usuario por cada rol para poder ingresar la primera vez. */
    private static void cargarDatosIniciales() {
        gestorEmpleados.registrar(new Administrador("10000001", "Ana", "Torres", "admin", "admin123")); //(dni, nombres, apellidos, usuario, contrasena, "Administrador")
        gestorEmpleados.registrar(new AsesorDeViaje("10000002", "Luis", "Ramos", "asesor", "asesor123"));
        gestorEmpleados.registrar(new Operador("10000003", "Mia", "Diaz", "operador", "operador123"));
    }
 
    // ==================== LOGIN ====================
 
    private static Empleado login() {
        System.out.println("\n--- INICIO DE SESION ---");
        System.out.print("Usuario: ");
        String usuario = sc.nextLine().trim();
        System.out.print("Contrasena: ");
        String contrasena = sc.nextLine().trim();
 
        Empleado empleado = gestorEmpleados.validarLogin(usuario, contrasena);
        if (empleado == null) {
            System.out.println("Usuario o contrasena incorrectos.");
        }
        return empleado;
    }
 
    // ==================== MENU ADMINISTRADOR ====================
 
    private static void menuAdministrador(Administrador admin) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== MENU ADMINISTRADOR =====");
            System.out.println("1. Gestionar empleados");
            System.out.println("2. Gestionar destinos turisticos");
            System.out.println("3. Gestionar paquetes turisticos");
            System.out.println("4. Gestionar promociones");
            System.out.println("5. Ver reportes");
            System.out.println("0. Cerrar sesion");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> menuEmpleados();
                case 2 -> menuDestinos();
                case 3 -> menuPaquetes();
                case 4 -> menuPromociones();
                case 5 -> menuReportes();
                case 0 -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }
 
    private static void menuEmpleados() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n-- Empleados (" + gestorEmpleados.getCantidadEmpleados() + ") --");
            System.out.println("1. Crear empleado");
            System.out.println("2. Listar empleados");
            System.out.println("3. Eliminar empleado por DNI");
            System.out.println("0. Volver");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> crearEmpleado();
                case 2 -> {
                    for (Empleado e : gestorEmpleados.listarTodos()) {
                        System.out.println(" - " + e + " | DNI: " + e.getDni() + " | usuario: " + e.getUsuario());
                    }
                }
                case 3 -> {
                    String dni = leerLinea("DNI a eliminar: ");
                    System.out.println(gestorEmpleados.eliminar(dni) ? "Eliminado." : "No encontrado.");
                }
                case 0 -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }
 
    private static void crearEmpleado() {
        String dni = leerLinea("DNI: ");
        String nombres = leerLinea("Nombres: ");
        String apellidos = leerLinea("Apellidos: ");
        String usuario = leerLinea("Usuario: ");
        String contrasena = leerLinea("Contrasena: ");
 
        System.out.println("Rol: 1) Administrador  2) Asesor de Viaje  3) Operador");
        int rol = leerEntero("Opcion: ");
        Empleado nuevo = switch (rol) {
            case 1 -> new Administrador(dni, nombres, apellidos, usuario, contrasena);
            case 2 -> new AsesorDeViaje(dni, nombres, apellidos, usuario, contrasena);
            case 3 -> new Operador(dni, nombres, apellidos, usuario, contrasena);
            default -> null;
        };
        if (nuevo == null) {
            System.out.println("Rol invalido.");
            return;
        }
        System.out.println(gestorEmpleados.registrar(nuevo) ? "Empleado registrado." : "No se pudo registrar (limite alcanzado).");
    }
 
    private static void menuDestinos() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n-- Destinos turisticos (" + gestorDestinos.getCantidadDestinos() + ") --");
            System.out.println("1. Registrar destino");
            System.out.println("2. Listar destinos");
            System.out.println("3. Eliminar destino (nombre + ciudad)");
            System.out.println("0. Volver");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> {
                    String nombre = leerLinea("Nombre: ");
                    String pais = leerLinea("Pais: ");
                    String ciudad = leerLinea("Ciudad: ");
                    String descripcion = leerLinea("Descripcion: ");
                    String clima = leerLinea("Clima: ");
                    String idioma = leerLinea("Idioma principal: ");
                    String ruta = leerLinea("Ruta de imagen referencial: ");
                    DestinoTuristico d = new DestinoTuristico(nombre, pais, ciudad, descripcion, clima, idioma, ruta);
                    System.out.println(gestorDestinos.registrar(d) ? "Destino registrado." : "No se pudo registrar.");
                }
                case 2 -> {
                    for (DestinoTuristico d : gestorDestinos.listarTodos()) {
                        System.out.println(" - " + d);
                    }
                }
                case 3 -> {
                    String nombre = leerLinea("Nombre: ");
                    String ciudad = leerLinea("Ciudad: ");
                    System.out.println(gestorDestinos.eliminar(nombre, ciudad) ? "Eliminado." : "No encontrado.");
                }
                case 0 -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }
 
    private static void menuPaquetes() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n-- Paquetes turisticos (" + gestorPaquetes.getCantidadPaquetes() + ") --");
            System.out.println("1. Crear paquete");
            System.out.println("2. Agregar destino a un paquete");
            System.out.println("3. Agregar servicio incluido a un paquete");
            System.out.println("4. Agregar dia de itinerario a un paquete");
            System.out.println("5. Listar paquetes");
            System.out.println("6. Cancelar paquete");
            System.out.println("0. Volver");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> crearPaquete();
                case 2 -> {
                    PaqueteTuristico p = seleccionarPaquete();
                    if (p == null) break;
                    DestinoTuristico d = seleccionarDestino();
                    if (d == null) break;
                    System.out.println(p.agregarDestino(d) ? "Destino agregado." : "Limite de destinos alcanzado.");
                }
                case 3 -> {
                    PaqueteTuristico p = seleccionarPaquete();
                    if (p == null) break;
                    String desc = leerLinea("Descripcion del servicio: ");
                    String tipo = leerLinea("Tipo de servicio (Vuelo/Hotel/Traslado/Visita guiada/Seguro): ");
                    System.out.println(p.agregarServicio(new ServicioIncluido(desc, tipo)) ? "Servicio agregado." : "Limite alcanzado.");
                }
                case 4 -> {
                    PaqueteTuristico p = seleccionarPaquete();
                    if (p == null) break;
                    int dia = leerEntero("Numero de dia: ");
                    String actividades = leerLinea("Actividades del dia: ");
                    System.out.println(p.agregarDiaItinerario(new DiaItinerario(dia, actividades)) ? "Dia agregado." : "Limite alcanzado.");
                }
                case 5 -> {
                    for (PaqueteTuristico p : gestorPaquetes.listarTodos()) {
                        System.out.println(" - " + p);
                    }
                }
                case 6 -> {
                    PaqueteTuristico p = seleccionarPaquete();
                    if (p == null) break;
                    p.cancelar();
                    System.out.println("Paquete cancelado.");
                }
                case 0 -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }
 
    private static void crearPaquete() {
        String codigo = leerLinea("Codigo: ");
        String nombre = leerLinea("Nombre: ");
 
        System.out.println("Tipo: 1) Nacional 2) Internacional 3) Aventura 4) Relax 5) Cultural");
        int tipoOp = leerEntero("Opcion: ");
        TipoPaquete tipo = switch (tipoOp) {
            case 1 -> TipoPaquete.NACIONAL;
            case 2 -> TipoPaquete.INTERNACIONAL;
            case 3 -> TipoPaquete.AVENTURA;
            case 4 -> TipoPaquete.RELAX;
            case 5 -> TipoPaquete.CULTURAL;
            default -> TipoPaquete.NACIONAL;
        };
 
        int dias = leerEntero("Duracion en dias: ");
        int noches = leerEntero("Duracion en noches: ");
        double precio = leerDouble("Precio por persona (S/): ");
        LocalDate salida = leerFecha("Fecha de salida (dd/MM/yyyy): ");
        LocalDate retorno = leerFecha("Fecha de retorno (dd/MM/yyyy): ");
        int cupo = leerEntero("Cupo maximo: ");
 
        PaqueteTuristico p = new PaqueteTuristico(codigo, nombre, tipo, dias, noches, precio, salida, retorno, cupo);
        System.out.println(gestorPaquetes.registrar(p) ? "Paquete registrado." : "No se pudo registrar (limite alcanzado).");
    }
 
    private static void menuPromociones() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n-- Promociones (" + gestorPromociones.getCantidadPromociones() + ") --");
            System.out.println("1. Crear descuento por porcentaje");
            System.out.println("2. Crear descuento por monto fijo");
            System.out.println("3. Crear descuento por temporada");
            System.out.println("4. Listar promociones");
            System.out.println("5. Eliminar promocion por codigo");
            System.out.println("0. Volver");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> {
                    String codigo = leerLinea("Codigo: ");
                    LocalDate ini = leerFecha("Fecha inicio (dd/MM/yyyy): ");
                    LocalDate fin = leerFecha("Fecha fin (dd/MM/yyyy): ");
                    double pct = leerDouble("Porcentaje (ej. 0.15 = 15%): ");
                    Promocion promo = new DescuentoPorcentaje(codigo, ini, fin, pct);
                    System.out.println(gestorPromociones.registrar(promo) ? "Promocion registrada." : "No se pudo registrar.");
                }
                case 2 -> {
                    String codigo = leerLinea("Codigo: ");
                    LocalDate ini = leerFecha("Fecha inicio (dd/MM/yyyy): ");
                    LocalDate fin = leerFecha("Fecha fin (dd/MM/yyyy): ");
                    double monto = leerDouble("Monto fijo (S/): ");
                    Promocion promo = new DescuentoMontoFijo(codigo, ini, fin, monto);
                    System.out.println(gestorPromociones.registrar(promo) ? "Promocion registrada." : "No se pudo registrar.");
                }
                case 3 -> {
                    String codigo = leerLinea("Codigo: ");
                    LocalDate ini = leerFecha("Fecha inicio (dd/MM/yyyy): ");
                    LocalDate fin = leerFecha("Fecha fin (dd/MM/yyyy): ");
                    String temporada = leerLinea("Nombre de temporada (ej. Verano): ");
                    double pct = leerDouble("Porcentaje de temporada (ej. 0.20 = 20%): ");
                    Promocion promo = new DescuentoTemporada(codigo, ini, fin, temporada, pct);
                    System.out.println(gestorPromociones.registrar(promo) ? "Promocion registrada." : "No se pudo registrar.");
                }
                case 4 -> {
                    for (Promocion p : gestorPromociones.listarTodas()) {
                        System.out.println(" - " + p.getCodigo() + " | vigente hasta " + p.getFechaFin());
                    }
                }
                case 5 -> {
                    String codigo = leerLinea("Codigo a eliminar: ");
                    System.out.println(gestorPromociones.eliminar(codigo) ? "Eliminada." : "No encontrada.");
                }
                case 0 -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }
 
    // ==================== MENU ASESOR DE VIAJE ====================
 
    private static void menuAsesor(AsesorDeViaje asesor) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== MENU ASESOR DE VIAJE =====");
            System.out.println("1. Gestionar clientes");
            System.out.println("2. Crear reserva");
            System.out.println("3. Registrar pago de una reserva");
            System.out.println("4. Generar voucher de una reserva");
            System.out.println("5. Registrar resena de una reserva");
            System.out.println("6. Ver reportes");
            System.out.println("0. Cerrar sesion");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> menuClientes();
                case 2 -> crearReserva(asesor);
                case 3 -> registrarPago();
                case 4 -> generarVoucher();
                case 5 -> registrarResena();
                case 6 -> menuReportes();
                case 0 -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }
 
    private static void menuClientes() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n-- Clientes (" + gestorClientes.getCantidadClientes() + ") --");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Eliminar cliente por documento");
            System.out.println("0. Volver");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> {
                    String doc = leerLinea("DNI o pasaporte: ");
                    String nombres = leerLinea("Nombres: ");
                    String apellidos = leerLinea("Apellidos: ");
                    LocalDate nacimiento = leerFecha("Fecha de nacimiento (dd/MM/yyyy): ");
                    String nacionalidad = leerLinea("Nacionalidad: ");
                    String telefono = leerLinea("Telefono: ");
                    String correo = leerLinea("Correo electronico: ");
                    Cliente c = new Cliente(doc, nombres, apellidos, nacimiento, nacionalidad, telefono, correo);
                    System.out.println(gestorClientes.registrar(c) ? "Cliente registrado." : "No se pudo registrar.");
                }
                case 2 -> {
                    for (Cliente c : gestorClientes.listarTodos()) {
                        System.out.println(" - " + c + " | edad: " + c.calcularEdad());
                    }
                }
                case 3 -> {
                    String doc = leerLinea("Documento a eliminar: ");
                    System.out.println(gestorClientes.eliminar(doc) ? "Eliminado." : "No encontrado.");
                }
                case 0 -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }
 
    private static void crearReserva(AsesorDeViaje asesor) {
        PaqueteTuristico paquete = seleccionarPaquete();
        if (paquete == null) return;
 
        int adultos = leerEntero("Pasajeros adultos: ");
        int menores = leerEntero("Pasajeros menores: ");
        int totalPasajeros = adultos + menores;
 
        if (!paquete.hayCupoDisponible(totalPasajeros)) {
            System.out.println("No hay cupo disponible para " + totalPasajeros + " pasajero(s). Cupo actual: " + paquete.getCupoDisponible());
            return;
        }
 
        Cliente[] grupo = new Cliente[totalPasajeros];
        for (int i = 0; i < totalPasajeros; i++) {
            String doc = leerLinea("Documento del pasajero " + (i + 1) + " de " + totalPasajeros + ": ");
            Cliente cliente = gestorClientes.buscarPorDocumento(doc);
            if (cliente == null) {
                System.out.println("Cliente no encontrado. Registrelo primero en 'Gestionar clientes'. Reserva cancelada.");
                return;
            }
            grupo[i] = cliente;
        }
 
        Reserva reserva = gestorReservas.crearReserva(paquete, asesor, grupo, adultos, menores, LocalDate.now());
        if (reserva == null) {
            System.out.println("No se pudo crear la reserva (sin cupo o limite de reservas alcanzado).");
            return;
        }
 
        // Aplica automaticamente la mejor promocion vigente sobre el precio total, si existe.
        double descuento = gestorPromociones.obtenerMejorDescuento(reserva.getPrecioTotal(), LocalDate.now());
        System.out.println("Reserva creada: " + reserva);
        if (descuento > 0) {
            System.out.println("Se encontro una promocion vigente. Descuento aplicable: S/ " + descuento);
        }
        System.out.println("Cupo restante del paquete: " + paquete.getCupoDisponible() + "/" + paquete.getCupoMaximo());
    }
 
    private static void registrarPago() {
        Reserva reserva = seleccionarReserva();
        if (reserva == null) return;
 
        System.out.println("Saldo pendiente actual: S/ " + reserva.getSaldoPendiente());
        double monto = leerDouble("Monto a pagar (S/): ");
 
        System.out.println("Tipo de pago: 1) Adelanto  2) Saldo");
        int tipoOp = leerEntero("Opcion: ");
        TipoPago tipoPago = (tipoOp == 2) ? TipoPago.SALDO : TipoPago.ADELANTO;
 
        System.out.println("Metodo de pago: 1) Efectivo  2) Tarjeta  3) Transferencia");
        int metodoOp = leerEntero("Opcion: ");
        MetodoPago metodo = switch (metodoOp) {
            case 2 -> MetodoPago.TARJETA;
            case 3 -> MetodoPago.TRANSFERENCIA;
            default -> MetodoPago.EFECTIVO;
        };
 
        Pago pago = new Pago(monto, tipoPago, metodo, LocalDate.now());
        boolean ok = reserva.registrarPago(pago);
        System.out.println(ok ? "Pago registrado. Estado de la reserva: " + reserva.getEstado()
                              : "No se pudo registrar el pago (limite alcanzado).");
        System.out.println("Nuevo saldo pendiente: S/ " + reserva.getSaldoPendiente());
    }
 
    private static void generarVoucher() {
        Reserva reserva = seleccionarReserva();
        if (reserva == null) return;
        Voucher voucher = gestorReservas.generarVoucher(reserva);
        System.out.println("\n" + voucher.generarContenido());
    }
 
    private static void registrarResena() {
        Reserva reserva = seleccionarReserva();
        if (reserva == null) return;
        int calificacion = leerEntero("Calificacion (1 a 5): ");
        String comentario = leerLinea("Comentario: ");
        try {
            Resena r = new Resena(reserva, calificacion, comentario);
            reserva.registrarResena(r);
            System.out.println("Resena registrada: " + r);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    // ==================== MENU OPERADOR ====================
 
    private static void menuOperador(Operador operador) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== MENU OPERADOR (solo lectura) =====");
            System.out.println("1. Ver itinerario de un paquete");
            System.out.println("2. Ver cupos de todos los paquetes");
            System.out.println("0. Cerrar sesion");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> {
                    PaqueteTuristico p = seleccionarPaquete();
                    if (p == null) break;
                    DiaItinerario[] itinerario = p.getItinerario();
                    if (itinerario.length == 0) {
                        System.out.println("Este paquete aun no tiene itinerario registrado.");
                    }
                    for (DiaItinerario d : itinerario) {
                        System.out.println(" - " + d);
                    }
                }
                case 2 -> {
                    for (PaqueteTuristico p : gestorPaquetes.listarTodos()) {
                        System.out.println(" - " + p.getCodigo() + ": " + p.getCupoDisponible() + "/" + p.getCupoMaximo()
                                + " [" + p.getEstado() + "]");
                    }
                }
                case 0 -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }
 
    // ==================== REPORTES (requerimiento 11) ====================
 
    private static void menuReportes() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n-- Reportes --");
            System.out.println("1. Reservas por paquete");
            System.out.println("2. Reservas por rango de fechas");
            System.out.println("3. Destinos mas solicitados");
            System.out.println("4. Ingresos por asesor");
            System.out.println("5. Clientes con pagos pendientes");
            System.out.println("6. Paquetes mejor calificados");
            System.out.println("0. Volver");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> {
                    String codigo = leerLinea("Codigo de paquete: ");
                    System.out.println(reportes.reservasPorPaquete(codigo));
                }
                case 2 -> {
                    LocalDate desde = leerFecha("Desde (dd/MM/yyyy): ");
                    LocalDate hasta = leerFecha("Hasta (dd/MM/yyyy): ");
                    System.out.println(reportes.reservasPorRangoFechas(desde, hasta));
                }
                case 3 -> System.out.println(reportes.destinosMasSolicitados());
                case 4 -> System.out.println(reportes.ingresosPorAsesor());
                case 5 -> System.out.println(reportes.clientesPendientesPago());
                case 6 -> System.out.println(reportes.paquetesMejorCalificados());
                case 0 -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }
 
    // ==================== SELECTORES AUXILIARES ====================
 
    private static PaqueteTuristico seleccionarPaquete() {
        PaqueteTuristico[] paquetes = gestorPaquetes.listarTodos();
        if (paquetes.length == 0) {
            System.out.println("No hay paquetes registrados.");
            return null;
        }
        for (PaqueteTuristico p : paquetes) {
            System.out.println(" - " + p);
        }
        String codigo = leerLinea("Ingrese el codigo del paquete: ");
        PaqueteTuristico p = gestorPaquetes.buscarPorCodigo(codigo);
        if (p == null) System.out.println("Paquete no encontrado.");
        return p;
    }
 
    private static DestinoTuristico seleccionarDestino() {
        DestinoTuristico[] destinos = gestorDestinos.listarTodos();
        if (destinos.length == 0) {
            System.out.println("No hay destinos registrados.");
            return null;
        }
        for (DestinoTuristico d : destinos) {
            System.out.println(" - " + d);
        }
        String nombre = leerLinea("Nombre del destino: ");
        String ciudad = leerLinea("Ciudad del destino: ");
        DestinoTuristico d = gestorDestinos.buscar(nombre, ciudad);
        if (d == null) System.out.println("Destino no encontrado.");
        return d;
    }
 
    private static Reserva seleccionarReserva() {
        Reserva[] reservas = gestorReservas.listarTodas();
        if (reservas.length == 0) {
            System.out.println("No hay reservas registradas.");
            return null;
        }
        for (int i = 0; i < reservas.length; i++) {
            System.out.println(" [" + i + "] " + reservas[i]);
        }
        int indice = leerEntero("Seleccione el numero de reserva: ");
        if (indice < 0 || indice >= reservas.length) {
            System.out.println("Indice invalido.");
            return null;
        }
        return reservas[indice];
    }
 
    // ==================== LECTURA DE DATOS (validada) ====================
 
    private static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }
 
    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }
 
    private static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim().replace(",", ".");
            try {
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un monto valido (ej. 250.50).");
            }
        }
    }
 
    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            try {
                return LocalDate.parse(texto, FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Use dd/MM/yyyy (ej. 25/12/2026).");
            }
        }
    }
}