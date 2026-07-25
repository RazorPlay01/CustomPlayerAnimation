import kotlin.toString

plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
}

platform {
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeVersionRange = "[${prop("deps.minecraft")},)"
		}
		required("neoforge") {
			forgeVersionRange = "[1,)"
		}
		required("cloth_config") {
			modrinth = "9s6osm5g"
			curseforge = "348521"
			slug("cloth-config")
			forgeVersionRange = "[1,)"
		}
		required("player_animation_library") {
			modrinth = "ha1mEyJS"
			curseforge = "1283899"
			slug("player-animation-library")
			forgeVersionRange = "[1,)"
		}
	}
}

neoForge {
	version = property("deps.neoforge") as String
	accessTransformers.from(rootProject.file("src/main/resources/aw/${stonecutter.current.version}.cfg"))
	validateAccessTransformers = true

	if (hasProperty("deps.parchment")) parchment {
		val (mc, ver) = (property("deps.parchment") as String).split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${stonecutter.active?.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${stonecutter.active?.version})"
		}
	}

	mods {
		register(property("mod.id") as String) {
			sourceSet(sourceSets["main"])
		}
	}
	sourceSets["main"].resources.srcDir("${rootDir}/versions/datagen/${stonecutter.current.version.split("-")[0]}/src/main/generated")
}

repositories {
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
	strictMaven("https://repo.redlance.org/public")
	strictMaven("https://maven.shedaniel.me/")
	strictMaven("https://maven.kosmx.dev/")
}

dependencies {
	configurations.all {
		resolutionStrategy {
			force("com.google.code.gson:gson:2.10")
			force("io.netty:netty-buffer:4.1.118.Final")
			force("io.netty:netty-common:4.1.118.Final")
			force("it.unimi.dsi:fastutil:8.5.12")
			force("org.slf4j:slf4j-api:2.0.13")
			force("org.joml:joml:1.10.8")
		}
	}

	implementation(libs.moulberry.mixinconstraints)
	jarJar(libs.moulberry.mixinconstraints)

	compileOnly("org.projectlombok:lombok:1.18.44")
	annotationProcessor("org.projectlombok:lombok:1.18.44")

	testCompileOnly("org.projectlombok:lombok:1.18.44")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.44")

	api("me.shedaniel.cloth:cloth-config-neoforge:${prop("deps.cloth-config")}")
	compileOnly("maven.modrinth:carry-on:${prop("deps.carryon_version")}")
	implementation ("com.zigythebird.playeranim:PlayerAnimationLibNeo:${prop("deps.player_animation_library")}")
	//runtimeOnly("org.javassist:javassist:3.30.2-GA")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

stonecutter {
	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
	replacements.string(current.parsed < "1.21.11") {
		replace("Identifier", "ResourceLocation")
		replace("identifier()", "location()")
	}
}
