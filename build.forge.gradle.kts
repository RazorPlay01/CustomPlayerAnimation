plugins {
	id("mod-platform")
	id("net.neoforged.moddev.legacyforge")
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)

	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
}

platform {
	loader = "forge"
	dependencies {
		required("minecraft") {
			forgeLikeVersionRange = prop("deps.minecraft")
		}
		required("forge") {
			forgeLikeVersionRange.set("[1,)")
		}
		required("cloth-config") {
			modrinth = "9s6osm5g"
			curseforge = "348521"
			slug("cloth-config")
			forgeLikeVersionRange.set("[1,)")
		}
		required("player-animator") {
			modrinth = "gedNE4y2"
			curseforge = "658587"
			slug("playeranimator")
			forgeLikeVersionRange.set("[1,)")
		}
	}
}

legacyForge {
	version = "${prop("deps.minecraft")}-${prop("deps.forge")}"

	validateAccessTransformers = true

	accessTransformers.from(
		rootProject.file("src/main/resources/aw/${sc.current.version}.cfg")
	)

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "Forge Client (${sc.current.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "Forge Server (${sc.current.version})"
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

	// implementation(libs.moulberry.mixinconstraints)
	// jarJar(libs.moulberry.mixinconstraints)

	annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.4")
	compileOnly("io.github.llamalad7:mixinextras-common:0.5.4")
	implementation("io.github.llamalad7:mixinextras-forge:0.5.4")
	jarJar("io.github.llamalad7:mixinextras-forge:0.5.4")

	compileOnly("org.projectlombok:lombok:1.18.46")
	annotationProcessor("org.projectlombok:lombok:1.18.46")

	modApi("me.shedaniel.cloth:cloth-config-forge:${prop("deps.cloth-config")}")
	modCompileOnly("maven.modrinth:carry-on:${prop("deps.carryon_version")}")
	findProperty("deps.player_animator")?.let { version ->
		modImplementation("dev.kosmx.player-anim:player-animation-lib-forge:$version")
	}
}

sourceSets {
	main {
		resources.srcDir(
			"${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated"
		)
	}
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}
