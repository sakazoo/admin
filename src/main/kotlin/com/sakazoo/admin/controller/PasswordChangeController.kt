package com.sakazoo.admin.controller

import com.sakazoo.admin.exception.RecordUpdateException
import com.sakazoo.admin.model.AppUserDetails
import com.sakazoo.admin.model.PasswordForm
import com.sakazoo.admin.service.UseDetailServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RequestMapping("/password/change")
@Controller
class PasswordChangeController(private val useDetailService: UseDetailServiceImpl) {
    companion object {
        private val log = LoggerFactory.getLogger(PasswordChangeController::class.java)
    }

    @GetMapping
    fun getPasswordChange(@ModelAttribute form: PasswordForm) = "password_change"

    @PostMapping
    fun postPasswordChange(
        @ModelAttribute form: PasswordForm,
        @AuthenticationPrincipal user: AppUserDetails
    ): String {
        useDetailService.updatePasswordDate(
            userId = user.userId,
            password = form.password ?: ""
        )
        log.info("password has updated.")
        return "home"
    }

    @ExceptionHandler(RecordUpdateException::class)
    fun handleRecordUpdateException(e: RecordUpdateException): ResponseEntity<String> {
        log.error("${e.message}")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("${e.message}")
    }
}