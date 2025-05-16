package br.com.agendou.data.repository

import br.com.agendou.domain.models.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override val currentUserFlow: Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Falha ao autenticar"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, user: User): Result<FirebaseUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.let { firebaseUser ->
                if (user.name.isNotEmpty()) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(user.name)
                        .build()
                    firebaseUser.updateProfile(profileUpdates).await()
                }
                
                val userMap = hashMapOf(
                    "id" to firebaseUser.uid,
                    "name" to user.name,
                    "email" to email,
                    "phone" to user.phone,
                    "type" to user.type,
                    "imageUrl" to user.imageUrl,
                    "createdAt" to user.createdAt
                )
                
                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(userMap)
                    .await()
                
                Result.success(firebaseUser)
            } ?: Result.failure(Exception("Falha ao criar usuário"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(credential: AuthCredential): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithCredential(credential).await()
            authResult.user?.let { firebaseUser ->
                val userDocument = firestore.collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .await()
                
                if (!userDocument.exists()) {
                    val userMap = hashMapOf(
                        "id" to firebaseUser.uid,
                        "name" to (firebaseUser.displayName ?: ""),
                        "email" to (firebaseUser.email ?: ""),
                        "phoneNumber" to (firebaseUser.phoneNumber ?: ""),
                        "role" to "cliente",
                        "imageUrl" to (firebaseUser.photoUrl?.toString() ?: ""),
                        "createdAt" to System.currentTimeMillis()
                    )
                    
                    firestore.collection("users")
                        .document(firebaseUser.uid)
                        .set(userMap)
                        .await()
                }
                
                Result.success(firebaseUser)
            } ?: Result.failure(Exception("Falha ao autenticar com Google"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginAnonymously(): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInAnonymously().await()
            authResult.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Falha ao autenticar anonimamente"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(name: String, photoUri: String?): Result<Unit> {
        return try {
            val profileUpdatesBuilder = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
            
            photoUri?.let {
                profileUpdatesBuilder.setPhotoUri(it.toUri())
            }
            
            val profileUpdates = profileUpdatesBuilder.build()
            
            auth.currentUser?.updateProfile(profileUpdates)?.await()
            
            auth.currentUser?.let { user ->
                val updates = mutableMapOf<String, Any>()
                updates["name"] = name
                if (photoUri != null) {
                    updates["imageUrl"] = photoUri
                }
                
                firestore.collection("users")
                    .document(user.uid)
                    .update(updates)
                    .await()
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }
} 