package br.com.claus.sellvia.features.login.data.model

data class LoginResponse(
    val token: String,
    val refreshToken: String,
    val user: UserLoginResponse,
    val company: CompanyLoginResponse
)

data class UserLoginResponse(
    val id: Long,
    val username: String,
    val name: String,
    val role: String
)

data class CompanyLoginResponse(
    val id: Long,
    val name: String = "",
    val websiteUrl: String? = "",
    val companyUrlLogo: String? = ""
)