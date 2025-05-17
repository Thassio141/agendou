package br.com.agendou.di

import br.com.agendou.data.repository.AuthRepository
import br.com.agendou.domain.usecases.auth.*
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * Módulo Hilt para fornecer implementações de teste para os casos de uso
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = []
)
object TestAppModule {

    /**
     * Fornece uma implementação real dos casos de uso de autenticação para teste de integração
     */
    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideLoginWithGoogleUseCase(authRepository: AuthRepository): LoginWithGoogleUseCase {
        return LoginWithGoogleUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideLoginAnonymouslyUseCase(authRepository: AuthRepository): LoginAnonymouslyUseCase {
        return LoginAnonymouslyUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideResetPasswordUseCase(authRepository: AuthRepository): ResetPasswordUseCase {
        return ResetPasswordUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(authRepository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserProfileUseCase(authRepository: AuthRepository): UpdateUserProfileUseCase {
        return UpdateUserProfileUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideIsUserAuthenticatedUseCase(authRepository: AuthRepository): IsUserAuthenticatedUseCase {
        return IsUserAuthenticatedUseCase(authRepository)
    }
} 