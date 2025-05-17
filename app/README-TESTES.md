# Testes no Agendou App

Este documento descreve a estratégia de testes implementada no Agendou App, abrangendo testes unitários, de integração e de UI.

## Estrutura de testes

A implementação de testes segue a divisão padrão do Android:

- `src/test/java/`: Testes unitários que rodam na JVM sem dependências do Android
- `src/androidTest/java/`: Testes de instrumentação (integração e UI) que exigem emulador ou dispositivo real

## Tipos de testes implementados

### 1. Testes Unitários

Os testes unitários validam componentes individuais de forma isolada, como modelos de domínio, casos de uso e viewmodels.

Exemplos:
- `UserTest`: Testa a classe de modelo User
- `AuthRepositoryImplTest`: Testa o repositório de autenticação usando mocks
- `LoginUseCaseTest`: Testa o caso de uso de login com dependências simuladas

**Ferramentas utilizadas:**
- JUnit: Framework base para testes
- Mockito/MockK: Para criação de mocks
- Kotlinx-coroutines-test: Para testar código assíncrono

### 2. Testes de Integração

Os testes de integração validam a interação entre componentes reais do sistema.

Exemplos:
- `AuthRepositoryIntegrationTest`: Testa o repositório de autenticação com Firebase real
- `AuthViewModelIntegrationTest`: Testa o ViewModel de autenticação com seus casos de uso reais

**Ferramentas utilizadas:**
- Hilt Testing: Para injeção de dependências em testes
- HiltTestRunner: Runner personalizado para configurar o ambiente de teste

### 3. Testes de UI

Os testes de UI validam a interface do usuário e a interação com ela.

Exemplos:
- `LoginScreenTest`: Testa a tela de login verificando elementos visuais e interações

**Ferramentas utilizadas:**
- Compose Testing: Para testar interfaces construídas com Jetpack Compose
- Espresso: Para testes de UI tradicionais (quando necessário)

## Configuração do ambiente de testes

Para executar os testes, as seguintes configurações foram implementadas:

1. **HiltTestRunner**: Um runner personalizado que configura o ambiente Hilt para testes.
2. **TestAppModule**: Um módulo Hilt que fornece implementações de teste para componentes do aplicativo.
3. **Dependências**: Adicionadas no `build.gradle.kts` para suportar os diferentes tipos de testes.

## Executando os testes

### Testes Unitários:
```bash
./gradlew test
```

### Testes de Instrumentação (Integração e UI):
```bash
./gradlew connectedAndroidTest
```

## Boas práticas adotadas

1. **Nomenclatura clara**: Padrão de nome `[Componente]Test` para testes unitários e `[Componente]IntegrationTest` para testes de integração.
2. **Organização em pacotes**: Estrutura de pacotes espelha a estrutura do código principal.
3. **Teste de um único conceito**: Cada método de teste valida um único comportamento ou funcionalidade.
4. **Preparação, execução e verificação**: Cada teste segue o padrão AAA (Arrange, Act, Assert). 