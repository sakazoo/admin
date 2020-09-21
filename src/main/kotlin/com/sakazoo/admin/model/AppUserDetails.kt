package com.sakazoo.admin.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

data class AppUserDetails(
    // needed properties for spring
    val userId: String,
    val pass: String, // avoid duplicated getter(getPassword)
    val passUpdateDate: Date,
    val loginMissTimes: Int,
    val unlock: Boolean,
    val enabled: Boolean,
    val userDueDate: Date,
    val authority: Collection<GrantedAuthority>,

    // custom properties
    val tenantId: String,
    val appUserName: String,
    val mailAddress: String
) : UserDetails {
    // kotlin's collection type is Collection<out E>, not Collection<E>
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authority
    }

    override fun getPassword(): String {
        return this.pass
    }

    override fun getUsername(): String {
        return this.userId
    }

    override fun isAccountNonExpired(): Boolean {
        return this.userDueDate.after(Date())
    }

    override fun isAccountNonLocked(): Boolean {
        return this.unlock
    }

    override fun isCredentialsNonExpired(): Boolean {
//        if check before
//        return this.passUpdateDate.after(Date())
        return true
    }

    override fun isEnabled(): Boolean {
        return this.enabled
    }
}