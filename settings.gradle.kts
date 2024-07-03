pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
//        maven {
//            url = uri("https://maven.emad.dev/repository/maven-public/")
//        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
//        maven {
//              url = uri("https://maven.emad.dev/repository/maven-public/")
//        }
    }
}

rootProject.name = "Samarium"
include(":app")
 