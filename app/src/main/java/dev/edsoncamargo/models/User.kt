package dev.edsoncamargo.models

data class User(
    var uid: String?,
    var name: String?,
    var displayName: String?,
    var email: String?,
    var cpf: String?,
    var phoneNumber: String?
)