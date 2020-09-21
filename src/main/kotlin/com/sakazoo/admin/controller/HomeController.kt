package com.sakazoo.admin.controller

import com.sakazoo.admin.model.AppUserDetails
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
    companion object {
        private val log = LoggerFactory.getLogger(HomeController::class.java)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_GENERAL')")
    @GetMapping("/home")
    fun home(model: Model, @AuthenticationPrincipal user: AppUserDetails): String {
        log.info("home page opened. user = $user")
        return "home"
    }
}