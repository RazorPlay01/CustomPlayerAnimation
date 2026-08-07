plugins {
	id("mod-platform")
	id("dev.kikugie.loom-back-compat")
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)

	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
	replacements.string(current.parsed >= "26.1.2") {
		replace("FabricDataOutput", "FabricPackOutput")
	}
}

platform {
	loader = "fabric"
	dependencies {
		required("minecraft") {
			fabricLikeVersionRange = prop("deps.minecraft")
		}
		required("fabric-api") {
			slug("fabric-api")
			fabricLikeVersionRange = ">=${prop("deps.fabric-api")}"
		}
		required("fabricloader") {
			fabricLikeVersionRange = ">=${prop("deps.fabric-loader")}"
		}
		optional("modmenu") {}
		required("cloth-config") {
			modrinth = "9s6osm5g"
			curseforge = "348521"
			slug("cloth-config")
			fabricLikeVersionRange = ">=${prop("deps.cloth-config")}"
		}
		if (hasProperty("deps.player_animation_library")) {
			required("player_animation_library") {
				modrinth = "ha1mEyJS"
				curseforge = "1283899"
				slug("player-animation-library")
				fabricLikeVersionRange = ">=${prop("deps.player_animation_library")}"
			}
		}
		if (hasProperty("deps.player_animator")) {
			required("player-animator") {
				modrinth = "gedNE4y2"
				curseforge = "658587"
				slug("playeranimator")
				fabricLikeVersionRange = ">=${prop("deps.player_animator")}"
			}
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

loom {
	accessWidenerPath = rootProject.file("src/main/resources/aw/${sc.current.version}.accesswidener")
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
		outputDirectory = file("${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated")
		client = true
	}
}

repositories {
	mavenCentral()
	strictMaven("https://maven.terraformersmc.com/", "com.terraformersmc") { name = "TerraformersMC" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
	strictMaven("https://repo.redlance.org/public")
	strictMaven("https://maven.shedaniel.me/")
	strictMaven("https://maven.kosmx.dev/")
}

configurations.all {
	resolutionStrategy {
		force("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	if (sc.current.parsed < "26") {
		mappings(loom.layered {
			officialMojangMappings()
			if (hasProperty("deps.parchment"))
				parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
		})
	}
	modImplementation("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	// implementation(libs.moulberry.mixinconstraints)
	// include(libs.moulberry.mixinconstraints)
	modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	modImplementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}")

	compileOnly("org.projectlombok:lombok:1.18.46")
	annotationProcessor("org.projectlombok:lombok:1.18.46")

	modApi("me.shedaniel.cloth:cloth-config-fabric:${prop("deps.cloth-config")}")
	modCompileOnly("maven.modrinth:carry-on:${prop("deps.carryon_version")}")
	findProperty("deps.player_animation_library")?.let { version ->
		modImplementation("com.zigythebird.playeranim:PlayerAnimationLibFabric:$version")
	}
	findProperty("deps.player_animator")?.let { version ->
		modImplementation("dev.kosmx.player-anim:player-animation-lib-fabric:$version")
	}
}
