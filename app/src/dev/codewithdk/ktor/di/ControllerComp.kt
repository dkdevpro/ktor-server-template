package dev.codewithdk.ktor.di

import dev.codewithdk.ktor.controller.*
import dagger.Component
import io.ktor.util.*
import javax.inject.Singleton

@Singleton
@Component
interface ControllerComp {
    fun userOnboardController(): UserOnboardController
}