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

	return getDuckResponse(payload)
}

private fun getDuckResponse(payload: String): String? = HttpClients.createDefault().use { client ->
	try {
		val url = URI("https://duckduckgo.com/duckchat/v1/chat")

		val request = HttpPost(url)
		request.setHeader("Accept", "text/event-stream")
		request.setHeader("Accept-Encoding", "gzip, deflate, br, zstd")
		request.setHeader("Accept-Language", "ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7,uk;q=0.6,be;q=0.5")
		request.setHeader("Content-Type", "application/json")
		request.setHeader("Cookie", "dcs=1; dcm=3")
		request.setHeader("DNT", "1")
		request.setHeader("Origin", "https://duckduckgo.com")
		request.setHeader("Priority", "u=1, i")
		request.setHeader("Referer", "https://duckduckgo.com/")
		request.setHeader("Sec-Ch-Ua", "\"Not A(Brand\";v=\"8\", \"Chromium\";v=\"132\", \"Microsoft Edge\";v=\"132\"")
		request.setHeader("Sec-Ch-Ua-Mobile", "?0")
		request.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"")
		request.setHeader("Sec-Fetch-Dest", "empty")
		request.setHeader("Sec-Fetch-Mode", "cors")
		request.setHeader("Sec-Fetch-Site", "same-origin")
		request.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv,127.0) Gecko/20100101 Firefox/127.0")
		request.addHeader("X-Vqd-4", xvqd4)

		request.entity = StringEntity(payload, ContentType.APPLICATION_JSON)

		client.execute(request) { response ->
			if (response.code in 200..299) {
				val entity = response.entity
				val jsonResponse = EntityUtils.toString(entity)

				val newXvqd4 = response.headers.find { it.name == "x-vqd-4" }?.value

				if (newXvqd4 != null) {
					xvqd4 = newXvqd4
				}

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

private fun refreshToken() {
	if (xvqd4 == null) {
		xvqd4 = HttpClients.createDefault().use { client ->
			try {
				val url = URI("https://duckduckgo.com/duckchat/v1/status")

				val request = HttpGet(url)
				request.addHeader("x-vqd-accept", "1")
				request.setHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
				)
				request.addHeader("x-vqd-4", xvqd4)
				request.setHeader("Host", "duckduckgo.com")
				request.setHeader("Accept", "text/event-stream")
				request.setHeader("Accept-Language", "en-US,en;q=0.5")
				request.setHeader("Accept-Encoding", "gzip, deflate, br")
				request.setHeader("Referer", "https://duckduckgo.com/")
				request.setHeader("DNT", "1")
				request.setHeader("Sec-GPC", "1")
				request.setHeader("Connection", "keep-alive")
				request.setHeader("Sec-Fetch-Dest", "empty")
				request.setHeader("Sec-Fetch-Mode", "cors")
				request.setHeader("Sec-Fetch-Site", "same-origin")
				request.setHeader("TE", "trailers")

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