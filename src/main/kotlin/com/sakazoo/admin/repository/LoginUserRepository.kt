package com.sakazoo.admin.repository

import com.sakazoo.admin.model.AppUserDetails
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import java.util.*


@Repository
class LoginUserRepository(private val jdbcTemplate: JdbcTemplate) {
    companion object {
        const val SELECT_USER_SQL = """
            SELECT
              *
            FROM
              m_user
            WHERE
              user_id = ?
        """

        const val SELECT_USER_ROLE_SQL = """
            SELECT
              role.role_name
            FROM
              m_user
            INNER JOIN
              t_user_role user_role
            ON
              m_user.user_id = user_role.user_id
            INNER JOIN
              m_role role
            ON
              user_role.role_id = role.role_id
            WHERE
              m_user.user_id = ?  
        """

        const val UPDATE_PASSWORD_SQL = """
            UPDATE
              m_user
            SET
              password = ?,
              pass_update_date = ?
            WHERE
              user_id = ?
        """

        const val UPDATE_LOCK_SQL = """
            UPDATE
              m_user
            SET
              login_miss_times = ?,
              `unlock` = ?
            WHERE
              user_id = ?
        """
    }

    fun selectUser(userId: String): UserDetails {
        val userMap: Map<String, Any> = jdbcTemplate.queryForMap(SELECT_USER_SQL, userId)

        val grantedAuthorityList: List<GrantedAuthority> = getRoleList(userId)

        return buildUserDetails(userMap, grantedAuthorityList)
    }

    private fun buildUserDetails(
        userMap: Map<String, Any>,
        grantedAuthorityList: List<GrantedAuthority>
    ) = AppUserDetails(
        userId = userMap["user_id"] as String,
        pass = userMap["password"] as String,
        passUpdateDate = userMap["pass_update_date"] as Date,
        loginMissTimes = userMap["login_miss_times"] as Int,
        unlock = userMap["unlock"] as Boolean,
        enabled = userMap["enabled"] as Boolean,
        userDueDate = userMap["user_due_date"] as Date,
        tenantId = userMap["tenant_id"] as String,
        appUserName = userMap["user_name"] as String,
        mailAddress = userMap["mail_address"] as String,
        authority = grantedAuthorityList
    )


    private fun getRoleList(userId: String): List<GrantedAuthority> {
        val authorityList: List<Map<String, Any>> = jdbcTemplate.queryForList(SELECT_USER_ROLE_SQL, userId)
        return authorityList.map { SimpleGrantedAuthority(it["role_name"] as? String) }.toList()
    }

    fun updatePassword(userId: String, password: String, passwordUpdateDate: Date): Int {
        return jdbcTemplate.update(
            UPDATE_PASSWORD_SQL,
            password,
            passwordUpdateDate,
            userId
        )
    }

    fun updateUnlock(userId: String, loginMissTime: Int, unlock: Boolean): Int =
        jdbcTemplate.update(UPDATE_LOCK_SQL, loginMissTime, unlock, userId)

}