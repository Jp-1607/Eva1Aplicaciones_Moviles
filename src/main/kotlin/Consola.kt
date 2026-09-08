import java.time.LocalDateTime

abstract class Consola(
    val codigo: String,
    val marca: String,
    val modelo: String,
    val usuario: TipoUsuario,
    val fechaIngreso: LocalDateTime = LocalDateTime.now()
) {
    abstract val tipoConsola: String

    abstract fun calcularCosto(minutos: Int): Double

    fun calcularTotal(minutos: Int): Double {
        if (minutos < 0) {
            throw IllegalArgumentException("El tiempo no puede ser negativo")
        }

        var total = calcularCosto(minutos)


        if (this is ConsolaModerna && minutos < 20) {
            return 0.0
        }

        if (total <= 0) {
            throw IllegalArgumentException("La tarifa calculada no es valida")
        }

        total = total * 1.19

        if (usuario == TipoUsuario.EDUCACIONAL) {
            total = total * 0.50
        }

        return total
    }

    companion object {
        fun codigoValido(codigo: String): Boolean {
            val expresion = Regex("^[A-Za-z]{2}[0-9]{2}[A-Za-z]{2}$")
            return expresion.matches(codigo)
        }
    }
}
