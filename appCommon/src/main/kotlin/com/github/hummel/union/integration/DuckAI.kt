package com.github.hummel.union.integration

import com.github.hummel.union.utils.getRandomUserAgent
import com.github.hummel.union.utils.gson
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import java.net.URI

private val headers = mutableMapOf(
	"User-Agent" to getRandomUserAgent(),
	"Accept" to "*/*",
	"Accept-Language" to "en-US,en;q=0.5",
	"Accept-Encoding" to "gzip, deflate, br, zstd",
	"Referer" to "https://duckduckgo.com/",
	"Cache-Control" to "no-store",
	"Connection" to "keep-alive",
	"Cookie" to "dcm=3",
	"Sec-Fetch-Dest" to "empty",
	"Sec-Fetch-Mode" to "cors",
	"Sec-Fetch-Site" to "same-origin",
	"Priority" to "u=4",
	"Pragma" to "no-cache",
	"TE" to "trailers"
)

fun getDuckAnswer(request: DuckRequest): String? {
	val payload = gson.toJson(request)

	val xvqdAndHash = getXvqdAndHash()

	if (xvqdAndHash.first == null || xvqdAndHash.second == null) {
		return null
	}

	return getResponse(xvqdAndHash, payload)
}

private fun getResponse(xvqdAndHash: Pair<String?, String?>, payload: String): String? =
	HttpClients.createDefault().use { client ->
		try {
			val url = URI("https://duckduckgo.com/duckchat/v1/chat")

			val request = HttpPost(url)
			request.addHeader("x-vqd-4", xvqdAndHash.first)
			request.addHeader("x-vqd-hash-1", "")

			headers.forEach { (key, value) -> request.addHeader(key, value) }

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
					null
				}
			}
		} catch (e: Exception) {
			e.printStackTrace()
			null
		}
	}

private fun getXvqdAndHash(): Pair<String?, String?> {
	return HttpClients.createDefault().use { client ->
		try {
			val url = URI("https://duckduckgo.com/duckchat/v1/status")

			val request = HttpGet(url)
			request.addHeader("x-vqd-accept", "1")

			headers.forEach { (key, value) -> request.addHeader(key, value) }

			client.execute(request) { response ->
				if (response.code in 200..299) {
					response.headers.find { it.name == "x-vqd-4" }?.value to response.headers.find { it.name == "x-vqd-hash-1" }?.value
				} else {
					null to null
				}
			}
		} catch (e: Exception) {
			e.printStackTrace()
			null to null
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