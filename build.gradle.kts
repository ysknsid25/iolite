import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm") version "2.0.21"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
}

group = "inorin"
version = "beta"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("com.lemonappdev:konsist:0.17.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")
}

detekt{
    config.setFrom("${rootProject.projectDir}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
    autoCorrect = true
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    withType<Detekt> {
        reports {
            html.required.set(true)
            html.outputLocation.set(file("${rootProject.projectDir}/reports/detekt.html"))
            xml.required.set(false)
            txt.required.set(false)
            sarif.required.set(false)
        }
    }
}
