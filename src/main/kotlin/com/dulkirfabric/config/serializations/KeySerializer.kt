package com.dulkirfabric.config.serializations

import com.mojang.blaze3d.platform.InputConstants
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object KeySerializer: KSerializer<InputConstants.Key> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("InputUtil.Key", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): InputConstants.Key {
        return InputConstants.Type.KEYSYM.getOrCreate(decoder.decodeInt())
    }

    override fun serialize(encoder: Encoder, value: InputConstants.Key) {
        encoder.encodeInt(value.value)
    }

}