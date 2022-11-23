package com.namseox.mymusicproject.remote.response

open class BaseResponse<Data> {
    var err: Int = 0
    var msg: String = ""
    var data: Data? = null
}