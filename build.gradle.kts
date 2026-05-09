import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.JavadocJar
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform") version "2.0.21"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
    id("org.jetbrains.dokka") version "2.0.0"
    id("com.vanniktech.maven.publish") version "0.34.0"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.16.3"
}

apiValidation {
    @OptIn(kotlinx.validation.ExperimentalBCVApi::class)
    klib {
        // Enable .klib API dumps for non-JVM targets (JS / Native).
        // Disable on machines that cannot cross-compile every Native target
        // by passing -PdisableKlibApi=true.
        enabled = !project.hasProperty("disableKlibApi")
    }
}

group = "io.github.ysknsid25.iolite"
version = "v1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    js(IR) {
        browser()
        nodejs()
    }
    linuxX64()
    macosX64()
    macosArm64()
    iosArm64()
    iosSimulatorArm64()
    iosX64()

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("com.lemonappdev:konsist:0.17.3")
                implementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
            }
        }
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")
}

detekt {
    source.setFrom(
        "src/commonMain/kotlin",
        "src/commonTest/kotlin",
        "src/jvmTest/kotlin",
    )
    config.setFrom("${rootProject.projectDir}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
    autoCorrect = true
}

mavenPublishing {
    configure(KotlinMultiplatform(javadocJar = JavadocJar.Dokka("dokkaHtml")))
    publishToMavenCentral()
    if (project.hasProperty("signingInMemoryKey")) {
        signAllPublications()
    }
    coordinates(group.toString(), project.name, version.toString())

    pom {
        name.set(project.name)
        description.set("A generic Value Object library inspired by Zod for Kotlin")
        url.set("https://github.com/ysknsid25/iolite")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/ysknsid25/iolite/blob/master/LISENCE")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("ysknsid25")
                name.set("Kanon")
                email.set("inorinrinrin1202@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:github.com/ysknsid25/iolite.git")
            developerConnection.set("scm:git:ssh://github.com/ysknsid25/iolite.git")
            url.set("https://github.com/ysknsid25/iolite")
        }
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(file("${rootProject.projectDir}/docs"))
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}

tasks {
    withType<Detekt> {
        jvmTarget = "1.8"
        reports {
            html.required.set(true)
            html.outputLocation.set(file("${rootProject.projectDir}/reports/detekt.html"))
            xml.required.set(false)
            txt.required.set(false)
            sarif.required.set(false)
        }
    }
}
