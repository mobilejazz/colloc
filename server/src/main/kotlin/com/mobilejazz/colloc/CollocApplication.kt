package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.FormViewInteractor
import com.mobilejazz.colloc.domain.interactor.GoogleTsvEndPointInteractor
import com.mobilejazz.colloc.domain.model.Platform
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class CollocApplication(
    private val collocInteractor: GoogleTsvEndPointInteractor,
    private val homeViewInteractor: FormViewInteractor,
) {
    @GetMapping("/")
    suspend fun home(): String {
        val html = homeViewInteractor()
        return html
    }

    @GetMapping(
        value = ["/colloc"],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    suspend fun colloc(
        @RequestParam(value = "id") id: String,
        @RequestParam(value = "platform") platform: Platform,
    ): ResponseEntity<ByteArray> {
        val result = collocInteractor(id, listOf(platform))
        val bytes = result.readBytes()

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=colloc.zip")
            .contentType(MediaType("application","zip"))
            .contentLength(bytes.size.toLong())
            .body(bytes);
    }
}

fun main(args: Array<String>) {
    runApplication<CollocApplication>(*args)
}
