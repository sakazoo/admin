package com.sakazoo.admin.config

import com.sakazoo.admin.model.AppUserDetails
import com.sakazoo.admin.service.UseDetailServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component


@Component
class BadCredentialsEventListener(private val userDetailservice: UseDetailServiceImpl) {
    companion object {
        private val log = LoggerFactory.getLogger(BadCredentialsEventListener::class.java)
    }

    @EventListener
    fun onBadCredentialsEvent(event: AuthenticationFailureBadCredentialsEvent) {
        if (event.exception is UsernameNotFoundException) {
            log.info("user is not exist")
            return
        }

        val userId = event.authentication.name
        val user = userDetailservice.loadUserByUsername(userId) as AppUserDetails
        val count = user.loginMissTimes + 1
        userDetailservice.updateUnlock(userId, count)
        log.info("user loginMissTimes is counted. userId = $userId, count = $count")
    }
}