plugins {
	id("mod-platform")
	id("fabric-loom")
}

platform {
	loader = "fabric"
	dependencies {
		required("minecraft") {
			versionRange = prop("deps.minecraft")
		}
		required("fabric-api") {
			slug("fabric-api")
			versionRange = ">=${prop("deps.fabric-api")}"
		}
		required("cloth-config") {
			modrinth = "9s6osm5g"
			curseforge = "348521"
			slug("cloth-config")
			versionRange = ">=${prop("deps.cloth-config")}"
		}
		required("player_animation_library") {
			modrinth = "ha1mEyJS"
			curseforge = "1283899"
			slug("player_animation_library")
			versionRange = ">=${prop("deps.player_animation_library")}"
		}
		required("fabricloader") {
			versionRange = ">=${libs.fabric.loader.get().version}"
		}
		optional("modmenu") {}
	}
}

loom {
	accessWidenerPath = rootProject.file("src/main/resources/${prop("mod.id")}.accesswidener")
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
	configureDataGeneration() {
		outputDirectory =
			file("${rootDir}/versions/datagen/${stonecutter.current.version.split("-")[0]}/src/main/generated")
		client = true
	}
}

repositories {
	maven("https://maven.terraformersmc.com/") { name = "Terraformers" }
	maven("https://maven.shedaniel.me/")
	maven("https://maven.terraformersmc.com/releases/")
	maven("https://repo.redlance.org/public")
}

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	mappings(
		loom.layered {
			officialMojangMappings()
			if (hasProperty("deps.parchment")) parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
		})
	modImplementation(libs.fabric.loader)
	modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	modImplementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}")
	modApi("me.shedaniel.cloth:cloth-config-fabric:${prop("deps.cloth-config")}")
	modImplementation("com.zigythebird.playeranim:PlayerAnimationLibFabric:${prop("deps.player_animation_library")}")

	compileOnly("org.projectlombok:lombok:1.18.36")
	annotationProcessor("org.projectlombok:lombok:1.18.36")

	testCompileOnly("org.projectlombok:lombok:1.18.36")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
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
