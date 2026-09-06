package com.example.basketmesaapp.model

enum class TipoCalculoTarifa {
    FIJO,
    SEGUN_NUMERO_OFICIALES,
    SOLITARIO_CON_AUTORIZACION;

    companion object {
        fun fromString(valor: String): TipoCalculoTarifa =
            entries.find { it.name == valor } ?: FIJO
    }
}

data class TarifaReglaRemota(
    var id: String = "",
    val rol: String = "",
    val orden: Int = 0,
    val nombre: String = "",
    val condiciones: List<String> = emptyList(),
    val tipoCalculo: String = TipoCalculoTarifa.FIJO.name,
    val valorPorDefecto: Double = 0.0,
    val numeroOficialesReferencia: Int = 1,
    val valorConReferencia: Double = 0.0,
    val valorSolitarioSinAutorizacion: Double = 0.0
)