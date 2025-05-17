package br.com.agendou

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Um JUnit Runner personalizado para configurar a classe de aplicação Hilt para testes de instrumentação.
 */
class HiltTestRunner : AndroidJUnitRunner() {
    
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
} 