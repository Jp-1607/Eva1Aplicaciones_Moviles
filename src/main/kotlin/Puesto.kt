class Puesto(val numero: Int) {
    var estado: EstadoPuesto = EstadoPuesto.LIBRE
    var consola: Consola? = null
    var motivo: String = ""
}
