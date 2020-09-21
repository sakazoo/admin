package com.sakazoo.admin.controller.handler

import com.sakazoo.admin.model.AppUserDetails
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component("SuccessHandler")
class SuccessHandler : AuthenticationSuccessHandler {
    companion object {
        private val log = LoggerFactory.getLogger(SuccessHandler::class.java)
    }

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val user = SecurityContextHolder.getContext().authentication.principal as AppUserDetails
        log.info("get user from SecurityContext. user = $user")

        val redirectPath = request.contextPath + if (user.passUpdateDate.after(Date())) {
            "/home"
        } else {
            "/password/change"
        }

        log.info("redirect to $redirectPath")
        response.sendRedirect(redirectPath)
    }
}