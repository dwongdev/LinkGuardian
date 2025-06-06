package dev.elbullazul.linkguardian.data.linkwarden

import dev.elbullazul.linkguardian.data.extensions.Creatable
import dev.elbullazul.linkguardian.data.generic.User
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenUser(
    val id: Int,
    override val username: String = "",
    val email: String? = "",
    val name: String? = "",
    val image: String? = "",
    val archiveAsScreenshot: Boolean? = false,
    val archiveAsMonolith: Boolean? = false,
    val archiveAsPDF: Boolean? = false,
    val emailVerified: String? = "",
    val subscriptions: String? = "",
    override val createdAt: String = ""
) : User, Creatable {
    override fun getId(): String {
        return id.toString()
    }
}