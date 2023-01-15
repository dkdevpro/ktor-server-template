val logback_version: String by project
val ktor_version: String by project
val coroutines_version: String by project
val kotlin_version: String by project
val kotlin_serialization: String by project
val swagger_codegen_version: String by project

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization").version("1.8.0")
}

group = "dev.codewithdk.ktor"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("dev.codewithdk.ktor.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}



repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {


    // Data module
    implementation(project(":database-postgres"))

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    //Ktor

    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")

    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-locations-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-apache-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-compression-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-id-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-metrics-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")


    //Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //Paytm
    //implementation("com.paytm:paytm-checksum:1.2.0")
    //implementation("com.paytm.pg:paytm-pg:0.0.6")
    //implementation("com.paytm:pgplussdk:1.4.4")

    //Paypal
    //implementation("com.paypal.sdk:rest-api-sdk:1.14.0")

    //Stripe
    //implementation("com.stripe:stripe-java:10.12.1")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // Swagger documentation
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
    implementation("io.swagger.codegen.v3:swagger-codegen-generators:$swagger_codegen_version")
    implementation("io.ktor:ktor-server-openapi:$ktor_version")
}
