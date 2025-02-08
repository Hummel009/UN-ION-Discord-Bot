package com.github.hummel.union.integration

import com.github.hummel.union.utils.gson
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import java.net.URI

var xvqd4: String? = null

fun getDuckAnswer(request: DuckRequest): String? {
	val payload = gson.toJson(request)

	refreshToken()

	xvqd4 ?: return null

	return getDuckResponse(payload)
}

private fun getDuckResponse(payload: String): String? = HttpClients.createDefault().use { client ->
	try {
		val url = URI("https://duckduckgo.com/duckchat/v1/chat")

		val request = HttpPost(url)
		request.addHeader("x-vqd-4", xvqd4)

		request.entity = StringEntity(payload, ContentType.APPLICATION_JSON)

		client.execute(request) { response ->
			if (response.code in 200..299) {
				val entity = response.entity
				val jsonResponse = EntityUtils.toString(entity)

				val apiResponse = jsonResponse?.split("data: ")?.filter {
					it.isNotEmpty()
				}?.takeWhile {
					!it.contains("[DONE]")
				}?.mapNotNull {
					gson.fromJson(it, DuckResponse::class.java)
				}

				apiResponse?.mapNotNull {
					it.message
				}?.joinToString("")
			} else {
				xvqd4 = null
				null
			}
		}
	} catch (e: Exception) {
		e.printStackTrace()

		xvqd4 = null
		null
	}
}

private fun refreshToken() {
	if (xvqd4 == null) {
		xvqd4 = HttpClients.createDefault().use { client ->
			try {
				val url = URI("https://duckduckgo.com/duckchat/v1/status")

				val request = HttpGet(url)
				request.addHeader("x-vqd-accept", "1")

				client.execute(request) { response ->
					if (response.code in 200..299) {
						response.headers.find { it.name == "x-vqd-4" }?.value
					} else {
						null
					}
				}
			} catch (e: Exception) {
				e.printStackTrace()
				null
			}
		}
	}
}

data class DuckRequest(
	val model: String, val messages: List<DuckMessage>
) {
	data class DuckMessage(
		val role: String, val content: String
	)
}

data class DuckResponse(
	val role: String,
	val message: String,
	val created: Long,
	val id: String,
	val action: String,
	val model: String,
)