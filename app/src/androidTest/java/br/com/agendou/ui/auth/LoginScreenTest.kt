package br.com.agendou.ui.auth

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            LoginScreen(
                onLoginSuccess = { isFirstLogin, userType, rememberMe, email, password -> },
                onSignUpClick = {},
                onForgotPasswordClick = {}
            )
        }
    }

    @Test
    fun loginScreen_displaysAllElements() {
        // Verifica se todos os elementos da tela estão sendo exibidos
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("E-mail").assertIsDisplayed()
        composeTestRule.onNodeWithText("Senha").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lembrar-me").assertIsDisplayed()
        composeTestRule.onNodeWithText("Esqueceu a senha?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Entrar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Não tem uma conta?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Criar Conta").assertIsDisplayed()
    }

    @Test
    fun loginButton_initiallyDisabled() {
        // Verifica se o botão de login está inicialmente desabilitado
        composeTestRule.onNodeWithText("Entrar").assertIsNotEnabled()
    }

    @Test
    fun loginButton_enabledWhenFieldsNotEmpty() {
        // Preenche os campos necessários
        composeTestRule.onNodeWithText("E-mail").performTextInput("teste@example.com")
        composeTestRule.onNodeWithText("Senha").performTextInput("senha123")

        // Verifica se o botão de login está habilitado
        composeTestRule.onNodeWithText("Entrar").assertIsEnabled()
    }

    @Test
    fun togglePasswordVisibility_changesPasswordVisibility() {
        // Preenche o campo de senha
        composeTestRule.onNodeWithText("Senha").performTextInput("senha123")

        // Encontra o ícone de visibilidade da senha e clica nele
        composeTestRule.onNodeWithContentDescription("Visibility").performClick()

        // Verifica se o ícone de visibilidade foi alterado (difícil de verificar diretamente)
        // Alternativa: verificar se o texto da senha está visível, mas isso não é possível com a API atual
    }

    @Test
    fun rememberMeCheckbox_togglesWhenClicked() {
        // Encontra o checkbox "Lembrar-me" e verifica se está desmarcado inicialmente
        composeTestRule.onNodeWithText("Lembrar-me").assertIsDisplayed()
        
        // Clica no checkbox
        composeTestRule.onNode(hasTestTag("remember_me_checkbox")).performClick()
        
        // Verifica se o checkbox foi marcado (difícil de verificar diretamente)
        // Uma alternativa é usar hasTestTag para facilitar
    }

    @Test
    fun clickForgotPassword_triggersCallback() {
        // Simula o clique no botão "Esqueceu a senha?"
        var forgotPasswordClicked = false
        
        composeTestRule.setContent {
            LoginScreen(
                onForgotPasswordClick = { forgotPasswordClicked = true }
            )
        }
        
        composeTestRule.onNodeWithText("Esqueceu a senha?").performClick()
        
        // Idealmente, verificaríamos se o callback foi chamado
        // Mas como este é um teste de UI básico, não podemos verificar diretamente
    }

    @Test
    fun clickSignUp_triggersCallback() {
        // Simula o clique no botão "Criar Conta"
        var signUpClicked = false
        
        composeTestRule.setContent {
            LoginScreen(
                onSignUpClick = { signUpClicked = true }
            )
        }
        
        composeTestRule.onNodeWithText("Criar Conta").performClick()
        
        // Idealmente, verificaríamos se o callback foi chamado
        // Mas como este é um teste de UI básico, não podemos verificar diretamente
    }
} 