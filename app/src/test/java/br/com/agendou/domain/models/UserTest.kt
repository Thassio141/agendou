package br.com.agendou.domain.models

import br.com.agendou.domain.enums.UserType
import com.google.firebase.Timestamp
import org.junit.Assert.assertEquals
import org.junit.Test

class UserTest {

    @Test
    fun `quando criar um usuário com valores padrão, deve retornar valores esperados`() {
        val user = User()
        
        assertEquals("", user.id)
        assertEquals("", user.name)
        assertEquals("", user.email)
        assertEquals(UserType.CLIENT, user.type)
        assertEquals(null, user.phone)
        assertEquals(null, user.imageUrl)
        assertEquals(0.0, user.rating, 0.0)
        assertEquals(null, user.categoryRef)
        assertEquals(null, user.createdAt)
        assertEquals(null, user.updatedAt)
    }

    @Test
    fun `quando criar um usuário com valores específicos, deve manter esses valores`() {
        val timestamp = Timestamp.now()
        val user = User(
            id = "123",
            name = "João Silva",
            email = "joao@exemplo.com",
            type = UserType.PROVIDER,
            phone = "11987654321",
            imageUrl = "https://exemplo.com/imagem.jpg",
            rating = 4.5
        )
        
        assertEquals("123", user.id)
        assertEquals("João Silva", user.name)
        assertEquals("joao@exemplo.com", user.email)
        assertEquals(UserType.PROVIDER, user.type)
        assertEquals("11987654321", user.phone)
        assertEquals("https://exemplo.com/imagem.jpg", user.imageUrl)
        assertEquals(4.5, user.rating, 0.0)
    }

    @Test
    fun `quando criar uma cópia do usuário com valores alterados, deve refletir as alterações`() {
        val originalUser = User(id = "123", name = "João", email = "joao@exemplo.com")
        val copiedUser = originalUser.copy(name = "João Silva", email = "joao.silva@exemplo.com")
        
        assertEquals("123", copiedUser.id)
        assertEquals("João Silva", copiedUser.name)
        assertEquals("joao.silva@exemplo.com", copiedUser.email)
    }
} 