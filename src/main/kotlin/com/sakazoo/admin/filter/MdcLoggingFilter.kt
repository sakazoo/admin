package com.sakazoo.admin.filter

import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest


@Order(99)
@Component
class MdcLoggingFilter : Filter {
    companion object {
        private const val USER_ID = "USER_ID"
        private const val SESSION_ID = "SESSION_ID"
    }

    override fun init(filterConfig: FilterConfig?) {
        // do nothing
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest

        val user = req.userPrincipal
        user?.name?.let {
            MDC.put(USER_ID, it)
        }
        val session = request.getSession(false)
        session?.let {
            MDC.put(SESSION_ID, it.id)
        }

        try {
            chain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }

    override fun destroy() {
        // do nothing
    }
}