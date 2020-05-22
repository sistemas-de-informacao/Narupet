package dev.edsoncamargo.models

class OrderedEntity {
    var id: String? = null
    var date: String? = null
    var total : Double? = null
    var products: List<ProductCart>? = null
}