package dev.edsoncamargo.models

data class ProductCart (
        var name: String? = null,
        var id: Int? = null,
        var qtd: Int? = null,
        var unitPrice: Double? = null,
        var totalPrice: Double? = null,
        var specialPrice: Double? = null
)