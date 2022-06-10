package com.mobilejazz.colloc.domain.interactor

import com.mobilejazz.colloc.domain.error.PlatformNotSupported
import com.mobilejazz.colloc.domain.model.Platform
import java.io.File
import java.net.URL

class CollocClassicInteractor {
    /** Executes the PHP version of Colloc */
    operator fun invoke(id: String, outputFolder: File, platform: Platform) {
        val url = URL("https://docs.google.com/spreadsheets/d/${id}/export?format=tsv")
        // map platform to parameter
        val p = when (platform) {
            Platform.IOS -> "100"
            Platform.ANDROID -> "010"
            Platform.JSON -> "001"
            Platform.ANGULAR -> throw PlatformNotSupported("Angular platform is not supported")
        }
        val proc = Runtime.getRuntime().exec(arrayOf("php", "colloc.php", url.toString(), outputFolder.toString(), p))
        val status = proc.waitFor()
        if (status != 0)
            throw Exception("Failed to run Colloc: $status")
    }
}
