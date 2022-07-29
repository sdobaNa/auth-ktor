val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val kmongo_version: String by project
val json_version: String by project
val jsonwebtoken_version: String by project
val jaxb_version: String by project
val jbcrypt: String by project
val mongodb: String by project
val gson: String by project

plugins {
    application
    java
    kotlin("jvm") version "1.6.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0"
}

group = "ru.cobalt42"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")
    implementation("org.json:json:$json_version")
    implementation("io.jsonwebtoken:jjwt:$jsonwebtoken_version")
    implementation("javax.xml.bind:jaxb-api:$jaxb_version")
    implementation ("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation ("io.ktor:ktor-client-gson:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("org.mindrot:jbcrypt:$jbcrypt")
    implementation("org.litote.kmongo:kmongo-id-serialization:$kmongo_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    implementation("net.sf.flexjson:flexjson:3.3")
}