plugins {
	id("mod-platform")
	id("net.neoforged.moddev.legacyforge")
}

platform {
	loader = "forge"
	dependencies {
		required("minecraft") {
			forgeVersionRange = "[${prop("deps.minecraft")},)"
		}
		required("forge") {
			forgeVersionRange = "[1,)"
		}
		required("cloth-config") {
			modrinth = "9s6osm5g"
			curseforge = "348521"
			slug("cloth-config")
			versionRange = ">=${prop("deps.cloth-config")}"
		}
		required("playeranimator") {
			modrinth = "gedNE4y2"
			curseforge = "658587"
			slug("playeranimator")
			versionRange = ">=${prop("deps.cloth-config")}"
		}
	}
}

legacyForge {
	version = "${property("deps.minecraft")}-${property("deps.forge")}"

	validateAccessTransformers = true

	accessTransformers.from(
		rootProject.file("src/main/resources/aw/${stonecutter.current.version}.cfg")
	)

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "Forge Client (${stonecutter.active?.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "Forge Server (${stonecutter.active?.version})"
		}
	}


	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
}

mixin {
	add(sourceSets.main.get(), "${prop("mod.id")}.mixins.refmap.json")
	config("${prop("mod.id")}.mixins.json")
}

repositories {
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
	strictMaven("https://maven.kosmx.dev/")
	strictMaven("https://maven.shedaniel.me/")
	strictMaven("https://repo.redlance.org/public")
}

dependencies {
	annotationProcessor("org.spongepowered:mixin:${libs.versions.mixin.get()}:processor")

	implementation(libs.moulberry.mixinconstraints)
	jarJar(libs.moulberry.mixinconstraints)

	annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.4")
	compileOnly("io.github.llamalad7:mixinextras-common:0.5.4")
	implementation("io.github.llamalad7:mixinextras-forge:0.5.4")
	jarJar("io.github.llamalad7:mixinextras-forge:0.5.4")

	compileOnly("org.projectlombok:lombok:1.18.44")
	annotationProcessor("org.projectlombok:lombok:1.18.44")

	testCompileOnly("org.projectlombok:lombok:1.18.44")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.44")
	modApi("me.shedaniel.cloth:cloth-config-forge:${prop("deps.cloth-config")}")
	modImplementation("maven.modrinth:carry-on:${prop("deps.carryon_version")}")
	findProperty("deps.player_animator")?.let { version ->
		modImplementation("dev.kosmx.player-anim:player-animation-lib-forge:$version")
	}
}

sourceSets {
	main {
		resources.srcDir(
			"${rootDir}/versions/datagen/${stonecutter.current.version.split("-")[0]}/src/main/generated"
		)
	}
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
