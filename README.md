# FieldFlow

## 📱 Sobre o Projeto

**FieldFlow** é um aplicativo Android nativo desenvolvido em Kotlin para controle de materiais em campo. O app permite o registro e rastreamento de consumo de materiais em ambientes com conectividade limitada, oferecendo sincronização automática com um servidor backend quando a conexão está disponível.

O aplicativo foi projetado com arquitetura limpa (Clean Architecture), seguindo os princípios SOLID e utilizando as melhores práticas do desenvolvimento Android moderno.

### Principais Funcionalidades

- 📦 **Listagem de Materiais**: Visualização de materiais disponíveis com quantidade em estoque
- ➖ **Registro de Consumo**: Registro offline de consumo de materiais em campo
- 🔄 **Sincronização Automática**: Sincronização bidirecional com o servidor backend
- 🌐 **Modo Dual**: Funcionamento com API fake (desenvolvimento/testes) ou servidor remoto (produção)
- 💾 **Armazenamento Local**: Persistência de dados usando Room Database
- 🔌 **Offline-First**: Funcionamento completo sem conexão com internet

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

```
app/
├── data/                    # Camada de Dados
│   ├── local/              # Banco de dados local (Room)
│   │   ├── dao/           # Data Access Objects
│   │   ├── database/      # Configuração do Room
│   │   └── entity/        # Entidades do banco
│   ├── remote/            # API remota
│   │   ├── api/          # Serviços Retrofit
│   │   └── model/        # DTOs
│   ├── repository/        # Implementação dos repositórios
│   └── worker/           # Background tasks (WorkManager)
├── domain/                 # Camada de Domínio
│   ├── enums/            # Enumerações
│   ├── model/            # Modelos de domínio
│   └── repository/       # Interfaces dos repositórios
├── presentation/          # Camada de Apresentação
│   ├── ui/              # Activities e Composables
│   └── viewmodel/       # ViewModels
└── di/                   # Injeção de Dependências (Koin)
```

### Stack Tecnológica

- **Linguagem**: Kotlin
- **UI**: Jetpack Compose
- **Arquitetura**: MVVM + Clean Architecture
- **Injeção de Dependências**: Koin
- **Banco de Dados**: Room
- **Requisições HTTP**: Retrofit + OkHttp
- **Serialização**: Gson
- **Tarefas em Background**: WorkManager
- **Coroutines**: Kotlin Coroutines + Flow

---

## ⚙️ Setup do Projeto

### Pré-requisitos

- **Android Studio**: Hedgehog (2023.1.1) ou superior
- **JDK**: 17 ou superior
- **SDK Android**: API 24 (Android 7.0) ou superior
- **Gradle**: 8.2 ou superior (gerenciado pelo Gradle Wrapper)

### Instalação

1. **Clone o repositório**:
   ```bash
   git clone <url-do-repositorio>
   cd FieldFlow
   ```

2. **Abra o projeto no Android Studio**:
   - File → Open → Selecione a pasta `FieldFlow`

3. **Sincronize as dependências**:
   - O Android Studio fará isso automaticamente, ou clique em "Sync Now" no banner que aparecer

4. **Execute o aplicativo**:
   - Conecte um dispositivo Android via USB (com depuração USB ativada) ou inicie um emulador
   - Clique no botão "Run" (▶️) ou pressione `Shift + F10`

---

## 🔌 Configuração de Conectividade

O aplicativo oferece dois modos de operação que podem ser alternados diretamente na interface:

### Modo API Local (Fake API)

Simula um servidor com dados mockados em memória. Ideal para desenvolvimento e testes sem necessidade de backend rodando.

### Modo Servidor Remoto

Integração com o backend real. Requer o projeto [**FieldFlowWeb**](https://github.com/ViniciusQuatrin/FieldFlowWeb) rodando.

#### Setup do Backend (FieldFlowWeb)

1. Clone o repositório:
   ```bash
   git clone https://github.com/ViniciusQuatrin/FieldFlowWeb
   cd FieldFlowWeb
   ```

2. Execute com Docker:
   ```bash
   docker-compose up -d
   ```
   
   Ou diretamente com Gradle:
   ```bash
   ./gradlew bootRun
   ```

3. O servidor estará disponível em: `http://localhost:8081`

#### Configuração no App

1. Execute o aplicativo no emulador ou dispositivo
2. Na tela inicial, clique no **dropdown de seleção de API** (canto superior direito)
3. Selecione **"Servidor Web"**
4. Informe a URL do servidor:
   - **Emulador**: `http://10.0.2.2:8081`
   - **Dispositivo Físico**: `http://[IP-DA-SUA-MAQUINA]:8081`
     - Descubra o IP: `ipconfig` (Windows) ou `ifconfig` (Linux/Mac)
     - Exemplo: `http://192.168.1.100:8081`

**Importante**: Certifique-se de que o backend está rodando, o firewall permite conexões na porta 8081 e o dispositivo está na mesma rede.

---

## 🔗 Projeto Relacionado

- **Backend (FieldFlowWeb)**: https://github.com/ViniciusQuatrin/FieldFlowWeb


