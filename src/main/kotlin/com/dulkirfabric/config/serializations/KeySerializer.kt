package com.dulkirfabric.config.serializations

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object KeySerializer: KSerializer<InputUtil.Key> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("InputUtil.Key", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): InputUtil.Key {
        return InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, decoder.decodeInt())
    }

    override fun serialize(encoder: Encoder, value: InputUtil.Key) {
        encoder.encodeInt(value.code)
    }
}