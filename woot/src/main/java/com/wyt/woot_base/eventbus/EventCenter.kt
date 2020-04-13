package com.wyt.woot_base.eventbus

/**
 * EventBus使用
 * 2019.11.28 -> wyt
 */
class EventCenter<T> {
    var eventCode = -1
        private set

    var data: T? = null

    constructor(eventCode: Int) {
        this.eventCode = eventCode
    }
    constructor(eventCode: Int, data: T) {
        this.eventCode = eventCode
        this.data = data
    }
}
