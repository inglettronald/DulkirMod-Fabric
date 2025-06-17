package com.dulkirfabric.commands

import com.dulkirfabric.util.TextUtils
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object JoinDungeonCommands {

    val objects: List<Registerable> = listOf(
        F1Command, F2Command, F3Command, F4Command, F5Command, F6Command, F7Command,
        M1Command, M2Command, M3Command, M4Command, M5Command, M6Command, M7Command,
    )

    object F1Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("f1").executes {
                TextUtils.info("§6Attempting to join F1...")
                TextUtils.sendCommand("joindungeon catacombs 1")
                return@executes 0
            })
        }
    }

    object F2Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("f2").executes {
                TextUtils.info("§6Attempting to join F2...")
                TextUtils.sendCommand("joindungeon catacombs 2")
                return@executes 0
            })
        }
    }

    object F3Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("f3").executes {
                TextUtils.info("§6Attempting to join F3...")
                TextUtils.sendCommand("joindungeon catacombs 3")
                return@executes 0
            })
        }
    }

    object F4Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("f4").executes {
                TextUtils.info("§6Attempting to join F4...")
                TextUtils.sendCommand("joindungeon catacombs 4")
                return@executes 0
            })
        }
    }

    object F5Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("f5").executes {
                TextUtils.info("§6Attempting to join F5...")
                TextUtils.sendCommand("joindungeon catacombs 5")
                return@executes 0
            })
        }
    }

    object F6Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("f6").executes {
                TextUtils.info("§6Attempting to join F6...")
                TextUtils.sendCommand("joindungeon catacombs 6")
                return@executes 0
            })
        }
    }

    object F7Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("f7").executes {
                TextUtils.info("§6Attempting to join F7...")
                TextUtils.sendCommand("joindungeon catacombs 7")
                return@executes 0
            })
        }
    }

    object M1Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("m1").executes {
                TextUtils.info("§6Attempting to join M1...")
                TextUtils.sendCommand("joindungeon master_catacombs 1")
                return@executes 0
            })
        }
    }

    object M2Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("m2").executes {
                TextUtils.info("§6Attempting to join M2...")
                TextUtils.sendCommand("joindungeon master_catacombs 2")
                return@executes 0
            })
        }
    }

    object M3Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("m3").executes {
                TextUtils.info("§6Attempting to join M3...")
                TextUtils.sendCommand("joindungeon master_catacombs 3")
                return@executes 0
            })
        }
    }

    object M4Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("m4").executes {
                TextUtils.info("§6Attempting to join M4...")
                TextUtils.sendCommand("joindungeon master_catacombs 4")
                return@executes 0
            })
        }
    }

    object M5Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("m5").executes {
                TextUtils.info("§6Attempting to join M5...")
                TextUtils.sendCommand("joindungeon master_catacombs 5")
                return@executes 0
            })
        }
    }

    object M6Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("m6").executes {
                TextUtils.info("§6Attempting to join M6...")
                TextUtils.sendCommand("joindungeon master_catacombs 6")
                return@executes 0
            })
        }
    }

    object M7Command : Registerable {
        override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
            dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("m7").executes {
                TextUtils.info("§6Attempting to join M7...")
                TextUtils.sendCommand("joindungeon master_catacombs 7")
                return@executes 0
            })
        }
    }
}

interface Registerable {
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess)
}
