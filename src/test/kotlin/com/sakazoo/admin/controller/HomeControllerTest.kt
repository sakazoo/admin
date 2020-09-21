package com.sakazoo.admin.controller

import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest(@Autowired val mockMvc: MockMvc){
    @BeforeAll
    fun setup() {
        println(">> Setup")
    }

    @AfterAll
    fun teardown() {
        println(">> Tear down")
    }

    @Test
    @WithUserDetails(value="system", userDetailsServiceBeanName="UseDetailServiceImpl")
    fun `home success`() {
        mockMvc
            .perform(get("/home"))
            .andExpect(content().string(containsString("User ID：<span>system</span>")))
    }
}