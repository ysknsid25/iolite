import io.gitlab.arturbosch.detekt.Detekt
import cl.franciscosolis.sonatypecentralupload.SonatypeCentralUploadTask


plugins {
    kotlin("multiplatform") version "2.0.21"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
    id("org.jetbrains.dokka") version "2.0.0"
    `maven-publish`
    id("cl.franciscosolis.sonatype-central-upload") version "1.0.3"
}

group = "io.github.ysknsid25.iolite"
version = "beta-v3"

repositories {
    mavenCentral()
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        jvmMain.dependencies {
        }
        jvmTest.dependencies {
            implementation("com.lemonappdev:konsist:0.17.3")
            implementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
            implementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
            runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
        }
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")
}

detekt{
    config.setFrom("${rootProject.projectDir}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
    autoCorrect = true
}

publishing {
    publications {
        register<MavenPublication>("maven") {
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
                    url.set("https://github.com/ysknsid25/iolite")
                }
            }
            artifact("${rootProject.projectDir}/build/kotlinToolingMetadata/kotlin-tooling-metadata.json") {
                extension = "json"
                builtBy(tasks.named("buildKotlinToolingMetadata"))
            }
        }
    }
}

tasks.named<SonatypeCentralUploadTask>("sonatypeCentralUpload") {
    dependsOn("jar", "sourcesJar", "javadocJar", "buildKotlinToolingMetadata", "generatePomFileForMavenPublication")

    username = System.getenv("SONATYPE_CENTRAL_USERNAME")
    password = System.getenv("SONATYPE_CENTRAL_PASSWORD")

    archives = files(
        tasks.named("jar"),
        tasks.named("sourcesJar"),
        tasks.named("javadocJar"),
    )
    pom = file(
        tasks.named("generatePomFileForMavenPublication").get().outputs.files.single()
    )

    signingKey = System.getenv("PGP_SIGNING_KEY")
    signingKeyPassphrase = System.getenv("PGP_SIGNING_KEY_PASSPHRASE")
}

tasks.dokkaHtml.configure {
    outputDirectory.set(file("${rootProject.projectDir}/docs"))
}

tasks.withType<Test> {
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
