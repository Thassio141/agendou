package br.com.agendou.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.agendou.ui.appointments.MyAppointmentsScreen
import br.com.agendou.ui.auth.ForgotPasswordScreen
import br.com.agendou.ui.auth.LoginScreen
import br.com.agendou.ui.auth.SignUpScreen
import br.com.agendou.ui.booking.BookingScreen
import br.com.agendou.ui.category.CategoryListScreen
import br.com.agendou.ui.client.HomeClientScreen
import br.com.agendou.ui.evaluation.EvaluationScreen
import br.com.agendou.ui.onboarding.ProfileOnboardingScreen
import br.com.agendou.ui.onboarding.UserType
import br.com.agendou.ui.professional.DashboardProScreen
import br.com.agendou.ui.professional.ProfessionalDetailScreen
import br.com.agendou.ui.professional.ScheduleManagementScreen
import br.com.agendou.ui.professional.ServiceManagementScreen
import br.com.agendou.ui.profile.ProfileScreen
import br.com.agendou.ui.splash.SplashScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ForgotPassword : Screen("forgotPassword")
    object Onboarding : Screen("onboarding")
    object CategoryList : Screen("categoryList")
    
    object HomeClient : Screen("homeClient?categoryId={categoryId}") {
        fun createRoute(categoryId: String? = null): String {
            return categoryId?.let { "homeClient?categoryId=$it" } ?: "homeClient"
        }
    }
    
    object ProfessionalDetail : Screen("professionalDetail/{professionalId}") {
        fun createRoute(professionalId: String): String {
            return "professionalDetail/$professionalId"
        }
    }
    
    object Booking : Screen("booking/{serviceId}") {
        fun createRoute(serviceId: String): String {
            return "booking/$serviceId"
        }
    }
    
    object MyAppointments : Screen("myAppointments")
    
    object Evaluation : Screen("evaluation/{appointmentId}") {
        fun createRoute(appointmentId: String): String {
            return "evaluation/$appointmentId"
        }
    }
    
    object DashboardPro : Screen("dashboardPro")
    object ServiceManagement : Screen("serviceManagement")
    object ScheduleManagement : Screen("scheduleManagement")
    object Profile : Screen("profile")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val coroutineScope = rememberCoroutineScope()
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
            
            // Simula a navegação automática após 2 segundos
            LaunchedEffect(key1 = true) {
                delay(2000)
                navController.navigate(Screen.Login.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { isFirstLogin, userType ->
                    when {
                        isFirstLogin -> {
                            navController.navigate(Screen.Onboarding.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        userType == "CLIENT" -> {
                            navController.navigate(Screen.HomeClient.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        userType == "PROFESSIONAL" -> {
                            navController.navigate(Screen.DashboardPro.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }
        
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpClick = { email, password, confirmPassword ->
                    // Normalmente aqui faria a criação da conta
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onSendLinkClick = { email ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Link enviado para $email")
                    }
                }
            )
        }
        
        composable(Screen.Onboarding.route) {
            ProfileOnboardingScreen(
                onContinueClick = { userType, category ->
                    if (userType == UserType.CLIENT) {
                        navController.navigate(Screen.CategoryList.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.DashboardPro.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.CategoryList.route) {
            CategoryListScreen(
                onCategorySelected = { category ->
                    navController.navigate(Screen.HomeClient.createRoute(category.id)) {
                        popUpTo(Screen.CategoryList.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.HomeClient.route,
            arguments = listOf(
                navArgument("categoryId") {
                    nullable = true
                    type = NavType.StringType
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            HomeClientScreen(
                onProfessionalClick = { professionalId ->
                    navController.navigate(Screen.ProfessionalDetail.createRoute(professionalId))
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onAppointmentsClick = {
                    navController.navigate(Screen.MyAppointments.route)
                },
                onHistoryClick = {
                    // Aqui poderia navegar para uma tela de histórico específica
                    // ou talvez filtrar a tela de agendamentos
                    navController.navigate(Screen.MyAppointments.route)
                },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        
        composable(
            route = Screen.ProfessionalDetail.route,
            arguments = listOf(
                navArgument("professionalId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val professionalId = backStackEntry.arguments?.getString("professionalId") ?: "1"
            ProfessionalDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onBookClick = { serviceId ->
                    navController.navigate(Screen.Booking.createRoute(serviceId))
                }
            )
        }
        
        composable(
            route = Screen.Booking.route,
            arguments = listOf(
                navArgument("serviceId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: "1"
            BookingScreen(
                serviceId = serviceId,
                onBackClick = {
                    navController.popBackStack()
                },
                onBookingConfirmed = {
                    navController.navigate(Screen.MyAppointments.route) {
                        popUpTo(Screen.HomeClient.route)
                    }
                }
            )
        }
        
        composable(Screen.MyAppointments.route) {
            MyAppointmentsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCancelAppointment = { appointmentId ->
                    // Aqui haveria lógica para cancelar o agendamento
                    // Simula refresh dos dados
                    // Na implementação real, você recarregaria os dados
                },
                onEvaluateAppointment = { appointmentId ->
                    navController.navigate(Screen.Evaluation.createRoute(appointmentId))
                }
            )
        }
        
        composable(
            route = Screen.Evaluation.route,
            arguments = listOf(
                navArgument("appointmentId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: "1"
            EvaluationScreen(
                appointmentId = appointmentId,
                onBackClick = {
                    navController.popBackStack()
                },
                onSubmitEvaluation = { rating, comment ->
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.DashboardPro.route) {
            DashboardProScreen(
                onServicesClick = {
                    navController.navigate(Screen.ServiceManagement.route)
                },
                onScheduleClick = {
                    navController.navigate(Screen.ScheduleManagement.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        
        composable(Screen.ServiceManagement.route) {
            ServiceManagementScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.ScheduleManagement.route) {
            ScheduleManagementScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onUpdateProfile = { updatedProfile ->
                    navController.popBackStack()
                }
            )
        }
    }
} 