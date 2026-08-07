plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
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
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeLikeVersionRange = prop("deps.minecraft")
		}
		required("neoforge") {
			forgeLikeVersionRange.set("[1,)")
		}
		required("cloth_config") {
			modrinth = "9s6osm5g"
			curseforge = "348521"
			slug("cloth-config")
			forgeLikeVersionRange.set("[1,)")
		}
		required("player_animation_library") {
			modrinth = "ha1mEyJS"
			curseforge = "1283899"
			slug("player-animation-library")
			forgeLikeVersionRange.set("[1,)")
		}
	}
}

mixins {
	client {
		always(
			"AbstractClientPlayerEntityMixin",
			"InventoryAccessor",
			"ItemInHandLayerMixin",
			"LivingEntityRendererMixin",
			"PlayerEntityRendererMixin",
		)
		minVersion("1.21.9","ClientMannequinMixin")
		minVersion("1.21.2","HumanoidRenderStateMixin")
		minVersion("1.21.2","ItemStackRenderStateMixin")
		minVersion("1.21.2","PlayerRendererAccesor")
		maxVersion("1.20.1","KeyframeAnimationPlayerMixin")
	}
}

neoForge {
	version = prop("deps.neoforge")
	accessTransformers.from(rootProject.file("src/main/resources/aw/${stonecutter.current.version}.cfg"))
	validateAccessTransformers = true

	if (hasProperty("deps.parchment")) parchment {
		val (mc, ver) = prop("deps.parchment").split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${stonecutter.current.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${stonecutter.current.version})"
		}
	}

	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
	sourceSets["main"].resources.srcDir("${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated")
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
	// implementation(libs.moulberry.mixinconstraints)
	// jarJar(libs.moulberry.mixinconstraints)

	compileOnly("org.projectlombok:lombok:1.18.46")
	annotationProcessor("org.projectlombok:lombok:1.18.46")

	api("me.shedaniel.cloth:cloth-config-neoforge:${prop("deps.cloth-config")}")
	compileOnly("maven.modrinth:carry-on:${prop("deps.carryon_version")}")
	implementation ("com.zigythebird.playeranim:PlayerAnimationLibNeo:${prop("deps.player_animation_library")}")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}
