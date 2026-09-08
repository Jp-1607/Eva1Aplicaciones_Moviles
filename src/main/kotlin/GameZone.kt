import kotlinx.coroutines.delay

class GameZone {
    private val puestos = mutableListOf<Puesto>()
    private val historial = mutableListOf<RegistroTurno>()

    private var numeroTicket = 1

    var totalRecaudado = 0.0
        private set

    var recaudacionClasica = 0.0
        private set
    var recaudacionModerna = 0.0
        private set
    var recaudacionVR = 0.0
        private set

    init {
        for (i in 1..10) {
            puestos.add(Puesto(i))
        }
    }

    suspend fun registrarEntrada(consola: Consola) {
        if (!Consola.codigoValido(consola.codigo)) {
            println("Error: codigo de consola invalido")
            return
        }

        var puestoLibre: Puesto? = null

        for (puesto in puestos) {
            if (puesto.estado == EstadoPuesto.LIBRE) {
                puestoLibre = puesto
                break
            }
        }

        if (puestoLibre == null) {
            println("Error: no quedan puestos disponibles")
            return
        }

        puestoLibre.estado = EstadoPuesto.EN_PROCESO
        puestoLibre.motivo = "Registrando entrada"

        println("Registrando ${consola.codigo} en el puesto ${puestoLibre.numero}...")
        delay(3000)

        puestoLibre.consola = consola
        puestoLibre.estado = EstadoPuesto.EN_JUEGO
        puestoLibre.motivo = ""

        println("Entrada lista. Puesto ${puestoLibre.numero} esta en juego")
    }

    suspend fun registrarSalida(codigo: String, minutos: Int) {
        var puestoEncontrado: Puesto? = null

        for (puesto in puestos) {
            if (puesto.estado == EstadoPuesto.EN_JUEGO) {
                if (puesto.consola?.codigo == codigo) {
                    puestoEncontrado = puesto
                    break
                }
            }
        }

        if (puestoEncontrado == null) {
            println("Error: consola $codigo no encontrada")
            return
        }

        val consola = puestoEncontrado.consola

        if (consola == null) {
            println("Error: no hay puesto en la consola")
            return
        }

        puestoEncontrado.estado = EstadoPuesto.EN_PROCESO
        puestoEncontrado.motivo = "procesando tarifa"

        println("Procesando salida de $codigo...")
        delay(6500)

        try {
            val monto = consola.calcularTotal(minutos)

            val ticket = Ticket(
                numeroTicket,
                consola.tipoConsola,
                consola.codigo,
                minutos,
                monto
            )

            historial.add(RegistroTurno(consola, ticket))
            totalRecaudado = totalRecaudado + monto

            if (consola is ConsolaClasica) {
                recaudacionClasica = recaudacionClasica + monto
            } else if (consola is ConsolaModerna) {
                recaudacionModerna = recaudacionModerna + monto
            } else if (consola is ConsolaVR) {
                recaudacionVR = recaudacionVR + monto
            }

            numeroTicket++

            println("Ticket generado")
            println("Nro: ${ticket.numero}")
            println("Codigo: ${ticket.codigo}")
            println("Tiempo: ${ticket.minutos} minutos")
            println("Monto: $${"%.0f".format(ticket.monto)}")

            puestoEncontrado.consola = null
            puestoEncontrado.estado = EstadoPuesto.LIBRE
            puestoEncontrado.motivo = ""
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")


            puestoEncontrado.estado = EstadoPuesto.EN_JUEGO
            puestoEncontrado.motivo = ""
        }
    }

    fun puestosDisponibles(): Int {
        var cantidad = 0

        for (puesto in puestos) {
            if (puesto.estado == EstadoPuesto.LIBRE) {
                cantidad++
            }
        }

        return cantidad
    }

    fun mostrarConsolasSocios() {
        println("\nConsolas de clientes socio:")

        val socios = historial.filter {
            it.consola.usuario == TipoUsuario.SOCIO
        }

        if (socios.isEmpty()) {
            println("No hay")
        } else {
            for (registro in socios) {
                println("${registro.consola.codigo} - ${registro.consola.modelo}")
            }
        }
    }

    fun ingresoPromedio(): Double {
        if (historial.isEmpty()) {
            return 0.0
        }

        return totalRecaudado / historial.size
    }

    fun mostrarCodigosFinalizados() {
        val codigos = historial.map {
            it.consola.codigo
        }

        println("Codigos de consolas finalizadas: $codigos")
    }

    fun mostrarConsolaMayorTiempo() {
        val mayor = historial.maxByOrNull {
            it.ticket.minutos
        }

        if (mayor == null) {
            println("No hay datos de tiempo")
        } else {
            println("Consola con mas tiempo: ${mayor.consola.codigo} con ${mayor.ticket.minutos} minutos")
        }
    }

    fun ponerEnReparacion(numero: Int, motivo: String) {
        val puesto = puestos.find { it.numero == numero }

        if (puesto == null) {
            println("Ese puesto no existe")
            return
        }

        if (puesto.estado != EstadoPuesto.LIBRE) {
            println("No se puede mandar a reparacion porque no esta libre")
            return
        }

        puesto.estado = EstadoPuesto.EN_REPARACION
        puesto.motivo = motivo
        println("Puesto $numero enviado a reparacion")
    }

    fun sacarDeReparacion(numero: Int) {
        val puesto = puestos.find { it.numero == numero }

        if (puesto == null) {
            println("Ese puesto no existe")
            return
        }

        if (puesto.estado == EstadoPuesto.EN_REPARACION) {
            puesto.estado = EstadoPuesto.LIBRE
            puesto.motivo = ""
            println("Puesto $numero nuevamente libre")
        }
    }

    fun mostrarPuestos() {
        println("\nESTADO DE PUESTOS")

        for (puesto in puestos) {
            if (puesto.estado == EstadoPuesto.EN_JUEGO) {
                println("Puesto ${puesto.numero}: EN JUEGO - ${puesto.consola?.codigo}")
            } else if (puesto.estado == EstadoPuesto.EN_PROCESO) {
                println("Puesto ${puesto.numero}: EN PROCESO - ${puesto.motivo}")
            } else if (puesto.estado == EstadoPuesto.EN_REPARACION) {
                println("Puesto ${puesto.numero}: EN REPARACION - ${puesto.motivo}")
            } else {
                println("Puesto ${puesto.numero}: LIBRE")
            }
        }
    }

    private fun tipoQueMasGenero(): String {
        if (historial.isEmpty()) {
            return "Sin datos"
        }

        var tipo = "Clasica"
        var mayor = recaudacionClasica

        if (recaudacionModerna > mayor) {
            mayor = recaudacionModerna
            tipo = "Moderna"
        }

        if (recaudacionVR > mayor) {
            tipo = "VR"
        }

        return tipo
    }

    fun reporteCierre() {
        println("\n============================")
        println("REPORTE DE CIERRE GAMEZONE")
        println("============================")

        if (historial.isEmpty()) {
            println("No se atendieron consolas")
        }

        for (registro in historial) {
            val t = registro.ticket

            println("\nTicket: ${t.numero}")
            println("Tipo: ${t.tipoConsola}")
            println("Codigo: ${t.codigo}")
            println("Tiempo: ${t.minutos} minutos")
            println("Pagado: $${"%.0f".format(t.monto)}")
        }

        println("\n----------------------------")
        println("Total recaudado: $${"%.0f".format(totalRecaudado)}")
        println("Consolas atendidas: ${historial.size}")
        println("Ingreso promedio: $${"%.0f".format(ingresoPromedio())}")
        println("Tipo que mas genero: ${tipoQueMasGenero()}")
        println("Puestos disponibles: ${puestosDisponibles()}")

        println("\nRecaudacion por tipo")
        println("Clasica: $${"%.0f".format(recaudacionClasica)}")
        println("Moderna: $${"%.0f".format(recaudacionModerna)}")
        println("VR: $${"%.0f".format(recaudacionVR)}")
    }
}
