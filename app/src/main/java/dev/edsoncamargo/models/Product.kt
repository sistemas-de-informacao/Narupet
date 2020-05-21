package dev.edsoncamargo.models

data class Product(
    val nomeProduto: String,
    val descProduto: String,
    val precProduto: Double,
    val idCategoria: Int,
    val qtdMinEstoque: Int,
    val ativoProduto: Boolean,
    val idProduto: Int,
    val descontoPromocao: Double
)