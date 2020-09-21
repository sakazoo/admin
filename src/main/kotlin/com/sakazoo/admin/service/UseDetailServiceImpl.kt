package com.sakazoo.admin.service

import com.sakazoo.admin.exception.RecordUpdateException
import com.sakazoo.admin.repository.LoginUserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.text.ParseException
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


@Service("UseDetailServiceImpl")
class UseDetailServiceImpl(
    private val userRepository: LoginUserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
    companion object {
        private val log = LoggerFactory.getLogger(UseDetailServiceImpl::class.java)

        private const val PASSWORD_UPDATE_SPAN_MONTH = 2L
        private const val LOGIN_MISS_LIMIT = 3
    }

    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.selectUser(username)

    @Throws(ParseException::class)
    fun updatePasswordDate(userId: String, password: String) {
        val encryptPass: String = passwordEncoder.encode(password)
        val date: Date = Date.from(
            LocalDate.now()
                .plusMonths(PASSWORD_UPDATE_SPAN_MONTH)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )
        val updatedCount = userRepository.updatePassword(userId, encryptPass, date)
        if (updatedCount != 1) {
            throw RecordUpdateException("failed to update user password. userId = $userId")
        }
    }

    fun updateUnlock(userId: String, loginMissTime: Int) {
        val unlock = if (LOGIN_MISS_LIMIT <= loginMissTime) {
            log.info("exceed login miss limit. user is locked. userId = $userId")
            false
        } else {
            true
        }
        userRepository.updateUnlock(userId, loginMissTime, unlock)
    }
}