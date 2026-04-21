plugins {
	id("mod-platform")
	id("net.fabricmc.fabric-loom")
}

platform {
	loader = "fabric"
	dependencies {
		required("minecraft") {
			versionRange = ">=${prop("deps.minecraft")}"
		}
		required("fabric-api") {
			slug("fabric-api")
			versionRange = ">=${prop("deps.fabric-api")}"
		}
		required("fabricloader") {
			versionRange = ">=${libs.fabric.loader.get().version}"
		}
		optional("modmenu") {}
		required("cloth-config") {
			modrinth = "9s6osm5g"
			curseforge = "348521"
			slug("cloth-config")
			versionRange = ">=${prop("deps.cloth-config")}"
		}
		required("player_animation_library") {
			modrinth = "ha1mEyJS"
			curseforge = "1283899"
			slug("player-animation-library")
			versionRange = ">=${prop("deps.player_animation_library")}"
		}
	}
}

loom {
	accessWidenerPath = rootProject.file("src/main/resources/aw/${stonecutter.current.version}.accesswidener")
	runs.named("client") {
		client()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "client"
		programArgs("--username=Dev")
		configName = "Fabric Client"
	}
	runs.named("server") {
		server()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "server"
		configName = "Fabric Server"
	}
}

fabricApi {
	configureDataGeneration {
		outputDirectory = file("${rootDir}/versions/datagen/${stonecutter.current.version.split("-")[0]}/src/main/generated")
		client = true
	}
}

repositories {
	mavenCentral()
	strictMaven("https://maven.terraformersmc.com/", "com.terraformersmc") { name = "TerraformersMC" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
	strictMaven("https://repo.redlance.org/public")
	strictMaven("https://maven.shedaniel.me/")
}

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")

	implementation(libs.fabric.loader)
	implementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	implementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}")

	implementation(libs.moulberry.mixinconstraints)
	include(libs.moulberry.mixinconstraints)

	compileOnly("org.projectlombok:lombok:1.18.44")
	annotationProcessor("org.projectlombok:lombok:1.18.44")

	testCompileOnly("org.projectlombok:lombok:1.18.44")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.44")

	api("me.shedaniel.cloth:cloth-config-fabric:${prop("deps.cloth-config")}")
	compileOnly("maven.modrinth:carry-on:${prop("deps.carryon_version")}")
	implementation("com.zigythebird.playeranim:PlayerAnimationLibFabric:${prop("deps.player_animation_library")}")
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
