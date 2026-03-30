package br.com.claus.sellvia.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CategoryRequest(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val companyId: Long? = null,

){
    init {
        require(!name.isNullOrBlank()) { "O campo 'Nome' é obrigatório" }
        require(!description.isNullOrBlank()) { "O campo 'Descrição' é obrigatório" }
        require(companyId != null) { "O campo 'companyId' é obrigatório" }
    }
}