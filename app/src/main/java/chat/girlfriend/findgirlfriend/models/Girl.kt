package chat.girlfriend.findgirlfriend.models

//data class Girl(val name: String, val thumb: String, val gallery: Array<String>, val fb_address: String, val video_url: String)

class Girl(
        var id: String = "",
        var name: String = "",
        var thumb: String = "",
        var gallery: ArrayList<String> = ArrayList<String>(),
        var fb_url: String = "",
        var hot: Boolean = false,
        var video_url: String = "",
        var time_stamp: Long = 0
)