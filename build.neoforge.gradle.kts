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
		required("cloth-config") {
			modrinth = "9s6osm5g"
			curseforge = "348521"
			slug("cloth-config")
			versionRange = ">=${prop("deps.cloth-config")}"
		}
		required("player-animation-library") {
			modrinth = "ha1mEyJS"
			curseforge = "1283899"
			slug("player-animation-library")
			versionRange = ">=${prop("deps.player-animation-library")}"
		}
	}
}

neoForge {
	version = property("deps.neoforge") as String
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
	maven("https://maven.terraformersmc.com/") { name = "Terraformers" }
	maven("https://maven.shedaniel.me/")
	maven("https://maven.terraformersmc.com/releases/")
	maven("https://repo.redlance.org/public")
}

dependencies {
	api("me.shedaniel.cloth:cloth-config-neoforge:${prop("deps.cloth-config")}")
	implementation ("com.zigythebird.playeranim:PlayerAnimationLibNeo:${prop("deps.player-animation-library")}")
	runtimeOnly("org.javassist:javassist:3.30.2-GA")

	compileOnly("org.projectlombok:lombok:1.18.36")
	annotationProcessor("org.projectlombok:lombok:1.18.36")

	testCompileOnly("org.projectlombok:lombok:1.18.36")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

tasks.named<ProcessResources>("processResources") {
	val mcVersion = stonecutter.current.version.split("-")[0]  // e.g., "1.21.9"

	// Lista base de mixins client
	val clientMixins = mutableListOf(
		"AbstractClientPlayerEntityMixin",
		"HumanoidRenderStateMixin",
		"ItemInHandLayerMixin",
		"ItemStackRenderStateMixin",
		"LivingEntityRendererMixin",
		"PlayerEntityRendererMixin",
		"PlayerRendererAccesor"
	)

	// Añadimos el mixin solo en 1.21.9+
	if (stonecutter.compare(mcVersion, "1.21.9") >= 0) {
		clientMixins.add("ClientMannequinMixin")
	}

	// ← ESTO ES LO NUEVO: Generamos el array COMPLETO como string (con corchetes y comas perfectas)
	val clientArrayString = clientMixins.joinToString(", ") { "\"$it\"" }  // ["mixin1", "mixin2", ...]
	val clientFull = "[\n    $clientArrayString\n  ]"

	// Mapa con el placeholder
	val placeholders = mapOf(
		"client_array" to clientFull
	)

	// Expandimos el JSON con esto
	filesMatching("cpa.mixins.json") {
		expand(placeholders)
	}

	// Para cache de Gradle
	inputs.property("mcVersion", mcVersion)
	inputs.property("clientArray", clientFull)
}
