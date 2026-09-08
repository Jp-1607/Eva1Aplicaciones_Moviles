import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val gameZone = GameZone()

    val play = ConsolaClasica(
        "CC12CD",
        "Sony",
        "PlayStation 5",
        TipoUsuario.SOCIO
    )

    val xbox = ConsolaClasica(
        "CC99ZA",
        "Microsoft",
        "Xbox Series X",
        TipoUsuario.INFANTIL
    )

    val nintendo = ConsolaModerna(
        "CM22TO",
        "Nintendo",
        "Nintendo Switch",
        TipoUsuario.INFANTIL
    )

    val meta = ConsolaVR(
        "VR44RG",
        "Meta",
        "Meta Quest 3",
        TipoUsuario.EDUCACIONAL,
        true
    )

    val vive = ConsolaVR(
        "VR77RG",
        "HTC",
        "Vive Pro",
        TipoUsuario.INFANTIL,
        false
    )

    val codigoMalo = ConsolaClasica(
        "123ABC",
        "Sony",
        "PlayStation 4",
        TipoUsuario.INFANTIL
    )

    gameZone.registrarEntrada(codigoMalo)

    gameZone.registrarEntrada(play)
    gameZone.registrarEntrada(xbox)
    gameZone.registrarEntrada(nintendo)
    gameZone.registrarEntrada(meta)
    gameZone.registrarEntrada(vive)

    gameZone.mostrarPuestos()


    gameZone.registrarSalida("AA00AA", 30)

    gameZone.registrarSalida("CC12CD", 75)
    gameZone.registrarSalida("CC99ZA", 180)
    gameZone.registrarSalida("CM22TO", 18)
    gameZone.registrarSalida("VR44RG", 120)
    gameZone.registrarSalida("VR77RG", 45)

    gameZone.mostrarConsolasSocios()
    gameZone.mostrarCodigosFinalizados()
    gameZone.mostrarConsolaMayorTiempo()

    gameZone.reporteCierre()
}
