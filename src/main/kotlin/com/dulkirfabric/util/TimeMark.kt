package com.dulkirfabric.util

import java.time.Duration

data class TimeMark(val timestamp: Long) {
    companion object {
        fun now(): TimeMark {
            return TimeMark(System.currentTimeMillis())
        }

        fun ago(duration: Duration): TimeMark {
            return TimeMark(System.currentTimeMillis() -  duration.toMillis())
        }

        fun farPast(): TimeMark {
            return TimeMark(0L)
        }
    }

    fun timePassed(): Duration {
        return Duration.ofMillis(System.currentTimeMillis() - timestamp)
    }
}