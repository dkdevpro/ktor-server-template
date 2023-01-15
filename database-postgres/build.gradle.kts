val exposed_version: String by project
val dagger_version: String by project
val postgres_version: String by project

repositories {
    jcenter()
}

dependencies {
    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jodatime:$exposed_version")

    // PostgreSQL
    implementation("org.postgresql:postgresql:$postgres_version")
}