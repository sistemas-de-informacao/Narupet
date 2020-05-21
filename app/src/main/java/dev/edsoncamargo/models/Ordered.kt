package dev.edsoncamargo.models

import java.time.LocalDateTime

data class Ordered(
    var id: String? = null,
    var date: String,
    var total : Double,
    var products: List<ProductCart>
)