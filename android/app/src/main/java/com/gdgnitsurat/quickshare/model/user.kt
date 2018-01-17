package com.gdgnitsurat.quickshare.model

/**
 * Created by yolo on 6/1/18.
 */

class user {
    var name: String? = null
    var email: String? = null

    constructor() {
    }

    constructor(name: String?, email: String?) {
        this.name = name
        this.email = email
    }
}
