# Sellvia Android

Aplicativo Android nativo da plataforma Sellvia, desenvolvido em Kotlin com Jetpack Compose.

---

## Visao Geral

O Sellvia Android e o cliente móvel da plataforma Sellvia, voltado para gestão de produtos, categorias, catálogo e informações de empresa. O aplicativo consome uma API REST e segue os princípios de Clean Architecture com o padrão MVVM.

---

## Funcionalidades

- Autenticação de usuários com persistência de sessão via DataStore
- Listagem, cadastro e edição de produtos com suporte a upload de imagem
- Atualização independente da imagem do produto
- Gestão de categorias de produtos
- Visualização e edição de informações da empresa
- Catálogo de produtos com download de arquivos
- Paginação e ordenação em listagens
- Navegação por bottom bar com múltiplas telas

---

## Stack Tecnologica

| Camada             | Tecnologia                              |
|--------------------|-----------------------------------------|
| Linguagem          | Kotlin                                  |
| UI                 | Jetpack Compose + Material Design 3     |
| Arquitetura        | MVVM + Clean Architecture               |
| Injeção de Dep.    | Koin 3.5                                |
| Networking         | Retrofit 2.9 + OkHttp 4.12 + Gson      |
| Imagens            | Coil 2.6                                |
| Navegação          | Navigation Compose 2.9                  |
| Async              | Kotlin Coroutines + Flow                |
| Armazenamento      | DataStore Preferences 1.1              |
| Build System       | Gradle (Kotlin DSL) + Version Catalog   |

---

## Arquitetura

O projeto adota **Clean Architecture** com separação em três camadas principais dentro de cada feature:

```
feature/
  data/
    model/          # DTOs de requisição e resposta
    mapper/         # Mapeamento entre camadas
    ProductService  # Interface Retrofit
    *RepositoryImpl # Implementação do repositório
  domain/
    model/          # Entidades de domínio
    repository/     # Contratos de repositório (interfaces)
    usecase/        # Casos de uso com lógica de negócio
  presentation/
    *ViewModel      # Estado e eventos via StateFlow
    *Screen         # Composables de tela
    component/      # Composables reutilizáveis da feature
```

O estado da UI é exposto exclusivamente via `StateFlow` no ViewModel. Lógica de negócio reside nos use cases. Composables são stateless sempre que possível, recebendo estado e callbacks por parâmetro (State Hoisting).

---

## Estrutura do Projeto

```
app/src/main/java/br/com/claus/sellvia/
  core/
    data/storage/       # TokenManager (DataStore)
    di/module/          # Módulos Koin por feature
    model/              # Modelos compartilhados (Direction, etc.)
    network/            # Configuração Retrofit, interceptors, ResultWrapper
    navigation/         # Grafo de navegação
    presentation/       # ViewModels base (LoadingViewModel)
    utils/              # Funções utilitárias
  features/
    catalog/            # Catálogo de produtos
    category/           # Gestão de categorias
    company/            # Informações da empresa
    home/               # Tela inicial
    login/              # Autenticação
    product/            # Gestão de produtos
    splash/             # Tela de splash
    user/               # Perfil e gestão de usuários
  ui/
    components/         # Componentes Compose reutilizáveis
    theme/              # Configuração de tema e cores
```

---

## Requisitos

- **Android Studio** Hedgehog (2023.1.1) ou superior
- **JDK** 17
- **Android SDK** com API 26+ instalada
- **Gradle** 8.x (gerenciado pelo wrapper)

---

## Configuração e Execução

### 1. Clone o repositório

```bash
git clone https://github.com/SEU_USUARIO/sellvia-android.git
cd sellvia-android
```

### 2. Configure a URL base da API

No arquivo `app/src/main/java/br/com/claus/sellvia/core/network/`, localize a constante de configuração da URL base e defina o endereço do servidor backend:

```kotlin
const val BASE_URL = "https://sua-api.dominio.com/api/"
```

### 3. Compile e execute

Abra o projeto no Android Studio e execute em um dispositivo ou emulador com API 26 ou superior, ou utilize o Gradle:

```bash
./gradlew assembleDebug
```

Para instalar diretamente em um dispositivo conectado:

```bash
./gradlew installDebug
```

---

## Permissões Requeridas

| Permissao                            | Uso                                         |
|--------------------------------------|---------------------------------------------|
| `android.permission.INTERNET`        | Comunicação com a API REST                  |
| `android.permission.WRITE_EXTERNAL_STORAGE` | Download de arquivos de catálogo (API <= 28) |

---

## Versões do Aplicativo

| Campo         | Valor                  |
|---------------|------------------------|
| versionName   | 1.0                    |
| versionCode   | 1                      |
| minSdk        | 26 (Android 8.0)       |
| targetSdk     | 36                     |
| compileSdk    | 36                     |
| applicationId | br.com.claus.sellvia   |

---

## Convenções de Código

- Telas Compose seguem o padrão `NomeDaFeatureScreen`
- ViewModels seguem o padrão `NomeDaFeatureViewModel`
- Estados de UI seguem o padrão `NomeDaFeatureUiState`
- Toda lógica de negócio e validação reside nos use cases, nunca em composables
- Chamadas de API são feitas exclusivamente na camada `data`, via repositório
- Estado mutável e encapsulado no ViewModel; a UI recebe apenas `StateFlow` imutável

---

## Dependências Principais

```toml
# Compose
androidx-compose-bom = "2024.09.00"
androidx-navigation-compose = "2.9.6"
androidx-material3 = "1.4.0"

# Lifecycle / ViewModel
androidx-lifecycle-viewmodel-compose = "2.8.4"
androidx-lifecycle-runtime-compose = "2.8.4"

# Networking
retrofit2 = "2.9.0"
okhttp3-logging-interceptor = "4.12.0"

# DI
koin-android = "3.5.3"
koin-androidx-compose = "3.5.3"

# Coroutines
kotlinx-coroutines-core = "1.7.3"

# Imagens
coil-compose = "2.6.0"

# Storage
androidx-datastore-preferences = "1.1.1"
```

---

## Aviso de Portfólio

Este repositório é disponibilizado publicamente para fins exclusivos de divulgação profissional e demonstração de competências técnicas. O código pode ser lido e referenciado livremente, porém não é permitido o uso comercial, redistribuição como produto próprio ou qualquer forma de aproveitamento sem autorização expressa do autor.

Consulte o arquivo [LICENSE](LICENSE) para os termos completos.

---

## Autor

Desenvolvido por **Cláudio da Silva Araújo Filho Araujo** como parte do portfólio profissional de desenvolvimento Android.

Contato e perfil profissional disponíveis via GitHub: [@Claudio712005](https://github.com/Claudio712005)
Contato e perfil profissional disponíveis via linkedin: [@Cláudio da Silva Araújo Filho](https://www.linkedin.com/in/claudiodesenvolvedorjava/)