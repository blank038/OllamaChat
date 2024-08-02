package com.blank038.ollamachat.network

import com.blank038.ollamachat.OllamaChat
import com.blank038.ollamachat.factory.MessageFactory
import com.blank038.ollamachat.message.IMessage
import com.blank038.ollamachat.provider.GsonProvider
import com.google.gson.JsonObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.logging.Level
import kotlin.reflect.KClass

class OllamaConnector<T : IMessage>(
    private val url: String,
    private val prompt: JsonObject,
    private val consumer: Consumer<T>,
    private val error: Runnable = Runnable { }
) {

    fun open(clazz: KClass<T>) {
        CompletableFuture.supplyAsync {
            val result = mutableListOf<JsonObject>()
            try {
                val client = URL(url)
                with(client.openConnection() as HttpURLConnection) {
                    this.setRequestProperty("Content-Type", "application/json; utf-8")
                    this.setRequestProperty("Accept", "application/json")
                    this.setRequestProperty("Connection", "keep-alive")
                    this.requestMethod = "POST"
                    this.doOutput = true
                    this.outputStream.use { os ->
                        val input: ByteArray = prompt.toString().toByteArray(StandardCharsets.UTF_8)
                        os.write(input, 0, input.size)
                    }
                    this.inputStream.use {
                        val isr = InputStreamReader(it, StandardCharsets.UTF_8)
                        val bufferedReader = BufferedReader(isr)
                        bufferedReader.lines().forEach { line -> result.add(GsonProvider.fromString(line)) }
                    }
                }
            } catch (e: Exception) {
                OllamaChat.instance.logger.log(Level.WARNING, e) { "执行请求过程出现错误." }
            }
            result
        }.thenAccept {
            if (it.size == 1 && it[0].has("error")) {
                this.error.run()
            } else try {
                val message: T = MessageFactory.create(clazz, it)
                this.consumer.accept(message)
            } catch (e: Exception) {
                OllamaChat.instance.logger.log(Level.WARNING, e) { "消息转化过程异常." }
            }
        }
    }
}