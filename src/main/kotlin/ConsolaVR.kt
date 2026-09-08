class ConsolaVR(
    codigo: String,
    marca: String,
    modelo: String,
    usuario: TipoUsuario,
    val accesoriosPremium: Boolean
) : Consola(codigo, marca, modelo, usuario) {

    override val tipoConsola = "VR"

    override fun calcularCosto(minutos: Int): Double {
        val horas = minutos / 60.0
        var valor = horas * 3000

        if (accesoriosPremium) {
            valor = valor + (valor * 0.30)
        }

        return valor
    }
}
