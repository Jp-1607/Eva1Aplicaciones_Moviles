class ConsolaClasica(
    codigo: String,
    marca: String,
    modelo: String,
    usuario: TipoUsuario
) : Consola(codigo, marca, modelo, usuario) {

    override val tipoConsola = "Clasica"

    override fun calcularCosto(minutos: Int): Double {
        var horas = minutos / 60.0
        var valor = horas * 800

        if (usuario == TipoUsuario.SOCIO) {
            valor = valor * 0.80
        }

        return valor
    }
}
