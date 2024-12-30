package com.hgm.googlesignin

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * @author：HGM
 * @created：2024/12/30 星期一
 * @description：
 **/
class GoogleSignInManager(
      private val activity: Activity
) {
      companion object {
            const val TAG = "GoogleSignInManager"
      }

      private val credentialManager = CredentialManager.create(activity)


      suspend fun signIn(): SignInResult {
            return try {
                  val response = buildCredentialRequest()
                  val credential = response.credential
                  if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        // Make login API call here with google account
                        // ....
                        SignInResult.Success(
                              id = tokenCredential.id,
                              username = tokenCredential.displayName.orEmpty(),
                              avatarUrl = tokenCredential.profilePictureUri.toString()
                        )
                  } else SignInResult.ErrorTypeCredentials
            } catch (e: GetCredentialCancellationException) {
                  e.printStackTrace()
                  SignInResult.Cancelled
            } catch (e: NoCredentialException) {
                  e.printStackTrace()
                  SignInResult.NoCredentials
            } catch (e: GetCredentialException) {
                  e.printStackTrace()
                  SignInResult.Failure
            }
      }

      fun signOut() {

      }

      /**
       * 构建凭证请求
       **/
      private suspend fun buildCredentialRequest(): GetCredentialResponse {
            val request = GetCredentialRequest.Builder()
                  .addCredentialOption(
                        GetGoogleIdOption.Builder()
                              .setFilterByAuthorizedAccounts(false)
                              .setServerClientId(activity.getString(R.string.google_client_id))
                              .setAutoSelectEnabled(false)
                              .build()
                  )
                  .build()

            return credentialManager.getCredential(
                  context = activity,
                  request = request
            )
      }
}