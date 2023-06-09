package com.dulkirfabric.config

class Pair<T, R>(val t: T, val r: R) {

    fun getLeft(): T {
        return t
    }

    fun getRight(): R {
        return r
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        } else if (other != null && this::class == other::class) {
            other as Pair<*, *>
            return t == other.t && r == other.r
        }
        return false
    }

    override fun hashCode(): Int {
        var result = t?.hashCode() ?: 0
        result = 31 * result + (r?.hashCode() ?: 0)
        return result
    }
}
