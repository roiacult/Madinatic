package com.roacult.data.remote.entities


import com.google.gson.annotations.SerializedName
import com.roacult.domain.entities.Announce

data class RemoteAnnouncePage(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<RemoteAnnounce>
)


data class RemoteAnnounce(
    @SerializedName("aid") val aid: String,
    @SerializedName("title") val title: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("service") val remoteService: RemoteService,
    @SerializedName("status") val status: String,
    @SerializedName("created_on") val createdOn: String,
    @SerializedName("start_at") val startAt: String,
    @SerializedName("end_at") val endAt: String
) {
    fun toAnnounce() : Announce {
        return Announce(
            this.aid,
            this.title,
            this.desc,
            this.remoteService.toService(),
            this.status,
            this.createdOn,
            this.startAt,
            this.endAt
        )
    }
}

fun Announce.toRemote() : RemoteAnnounce {
    return RemoteAnnounce(
        this.aid,
        this.title,
        this.desc,
        this.service.toRemote(),
        this.status,
        this.createdOn,
        this.startAt,
        this.endAt
    )
}