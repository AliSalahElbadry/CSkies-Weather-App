package com.app.our.cskies.utils

object Setting{
    var location: Location?=null
    var temperature: Temperature?=null
    var lang: Lang?=null
    var wSpeed: WSpeed?=null
    var notificationState: NotificationState?=null
    enum class Location{MAP,GPS}
    enum class Temperature{C,K,F}
    enum class Lang{AR,EN}
    enum class WSpeed{M_S,MILE_HOUR}
    enum class NotificationState{ON,OFF}
}
