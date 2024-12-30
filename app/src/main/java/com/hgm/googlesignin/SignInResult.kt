package com.hgm.googlesignin

/**
 * @author：HGM
 * @created：2024/12/30 星期一
 * @description：
 **/
sealed interface SignInResult {
      data object Failure : SignInResult
      data object Cancelled : SignInResult
      data object NoCredentials : SignInResult
      data object ErrorTypeCredentials : SignInResult
      data class Success(val id: String, val username: String, val avatarUrl: String) : SignInResult
}