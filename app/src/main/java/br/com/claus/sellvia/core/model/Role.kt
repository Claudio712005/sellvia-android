package br.com.claus.sellvia.core.model

enum class UserRole(
    val role: String,
    val nome: String,
    val descricaoo: String,
) {
    SYSTEM_ADMIN("ROLE_SYSTEM_ADMIN", "Administrador do Sistema", "Responsável por gerenciar o sistema como um todo, incluindo usuários, empresas, configurações e permissões."),
    COMPANY_ADMIN("ROLE_COMPANY_ADMIN", "Administrador da Empresa", "Responsável por gerenciar a empresa, incluindo usuários, produtos, vendas e relatórios."),
    COMPANY_USER("ROLE_COMPANY_USER", "Usuário da Empresa", "Responsável por realizar vendas, gerenciar produtos e acessar relatórios relacionados à empresa.");
}