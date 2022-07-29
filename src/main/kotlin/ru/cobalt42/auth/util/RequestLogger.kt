package ru.cobalt42.auth.util

import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

fun writeLog(
    method: String,
    status: String,
    uri: String,
    token: String?,
    body: String,
    exceptionMessage: String = "",
) {
    try {

        val dir = File(
            System.getProperty("user.dir"),
            "Logs"
        )
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss"
        )

        val dateFormatFileName = SimpleDateFormat(
            "yyyy-MM"
        )
        val date = Date()

        val filename: String = dateFormatFileName.format(date)

        var numberOfFile = 1
        // Write the file into the folder
        var reportFile = File(dir, filename + "_" + numberOfFile + ".log")

        while (reportFile.length() > 10485760) {
            numberOfFile++
            reportFile = File(dir, filename + "_" + numberOfFile + ".log")
        }
        val fileWriter = FileWriter(reportFile, true)

        fileWriter.append("\n")
        fileWriter.append("[INFO] ").append(dateFormat.format(date)).append(" ")
        fileWriter.append("\n")
            .append(method).append(" ")
            .append(status).append(" ")
            .append(exceptionMessage).append("")
            .append("\n")
            .append(uri).append(" ")
            .append("\n")
            .append(token).append(" ")
            .append("\n")
            .append(extractPostRequestBody(body, method))
            .append("\n")
        fileWriter.flush()
        fileWriter.close()
    } catch (e: Exception) {
    }
}

fun extractPostRequestBody(body: String, method: String): String? {
    if (method == "POST") {
        val s = Scanner(body.byteInputStream(), "UTF-8").useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }
    return ""
}