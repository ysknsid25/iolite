import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.JavadocJar
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("multiplatform") version "2.0.21"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
    id("org.jetbrains.dokka") version "2.0.0"
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "io.github.ysknsid25.iolite"
version = "beta-v3"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

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

listOf(
    "SONATYPE_CENTRAL_USERNAME" to "mavenCentralUsername",
    "SONATYPE_CENTRAL_PASSWORD" to "mavenCentralPassword",
    "PGP_SIGNING_KEY" to "signingInMemoryKey",
    "PGP_SIGNING_KEY_PASSPHRASE" to "signingInMemoryKeyPassword",
).forEach { (envName, propName) ->
    System.getenv(envName)?.let { extra.set(propName, it) }
}

mavenPublishing {
    configure(KotlinMultiplatform(javadocJar = JavadocJar.Dokka("dokkaHtml")))
    publishToMavenCentral()
    signAllPublications()
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

tasks.named<Jar>("allMetadataJar") {
    dependsOn(tasks.named("buildKotlinToolingMetadata"))
    from(tasks.named("buildKotlinToolingMetadata").map { it.outputs.files })
}

tasks.dokkaHtml.configure {
    outputDirectory.set(file("${rootProject.projectDir}/docs"))
}

tasks.withType<Test>().configureEach {
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
