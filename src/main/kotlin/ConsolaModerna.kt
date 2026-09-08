class ConsolaModerna(
    codigo: String,
    marca: String,
    modelo: String,
    usuario: TipoUsuario
) : Consola(codigo, marca, modelo, usuario) {

    override val tipoConsola = "Moderna"

    override fun calcularCosto(minutos: Int): Double {
        if (minutos < 20) {
            return 0.0
        }

        val horas = minutos / 60.0
        return horas * 1500
    }
}
