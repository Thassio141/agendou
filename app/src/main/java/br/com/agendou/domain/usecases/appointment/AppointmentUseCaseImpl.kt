import br.com.agendou.data.repository.AppointmentRepository
import br.com.agendou.domain.models.Appointment
import br.com.agendou.domain.usecases.appointment.AppointmentUseCase
import br.com.agendou.domain.usecases.auth.AuthUseCase
import br.com.agendou.domain.usecases.user.UserUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppointmentUseCaseImpl @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val authUseCase: AuthUseCase,
    private val userUC: UserUseCase,
    private val firestore: FirebaseFirestore
) : AppointmentUseCase {

    private fun ref(uid: String) =
        firestore.collection("users").document(uid)

    private fun currentUserOrThrow() =
        authUseCase.currentUser ?: throw IllegalStateException("Usuário não autenticado")

    private fun userRef(uid: String) =
        firestore.collection("users").document(uid)

    override fun getAllAppointments(): Flow<List<Appointment>> =
        appointmentRepository.getAllAppointments()

    override suspend fun createAppointment(appt: Appointment): Result<Appointment> =
        runCatching {
            currentUserOrThrow()
            val profile = userUC.getCurrentUser().getOrElse {
                throw IllegalStateException("Perfil de usuário não encontrado")
            }

            val toSave = appt.copy(
                clientRef = userRef(profile.id)
            )
            appointmentRepository.createAppointment(toSave).getOrThrow()
        }

    override suspend fun updateAppointment(appt: Appointment): Result<Appointment> =
        runCatching {
            val me = currentUserOrThrow()
            val existing = appointmentRepository.getAppointmentById(appt.id).getOrThrow()

            if (existing.clientRef.id != me.uid && existing.professionalRef.id != me.uid)
                throw SecurityException("Não autorizado")

            appointmentRepository.updateAppointment(appt).getOrThrow()
        }

    override suspend fun deleteAppointment(id: String): Result<Unit> =
        runCatching {
            val me = currentUserOrThrow()
            val existing = appointmentRepository.getAppointmentById(id).getOrThrow()

            if (existing.clientRef.id != me.uid && existing.professionalRef.id != me.uid)
                throw SecurityException("Não autorizado")

            appointmentRepository.deleteAppointment(id).getOrThrow()
        }

    override fun getAppointmentsByClient(clientId: String): Flow<List<Appointment>> =
        appointmentRepository.getAppointmentsByClient(ref(clientId))

    override fun getAppointmentsByProfessional(proId: String): Flow<List<Appointment>> =
        appointmentRepository.getAppointmentsByProfessional(ref(proId))

    override fun getAppointmentsByService(serviceId: String): Flow<List<Appointment>> =
        appointmentRepository.getAppointmentsByService(
            firestore.collection("services").document(serviceId)
        )

    override suspend fun getAppointmentById(id: String): Result<Appointment> {
        TODO("Not yet implemented")
    }

    override suspend fun getClientAppointments(): Result<List<Appointment>> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfessionalAppointments(): Result<List<Appointment>> {
        TODO("Not yet implemented")
    }
}
