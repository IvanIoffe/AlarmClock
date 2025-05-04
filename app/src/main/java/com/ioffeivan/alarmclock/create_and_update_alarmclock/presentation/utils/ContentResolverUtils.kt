package com.ioffeivan.alarmclock.create_and_update_alarmclock.presentation.utils

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns

fun ContentResolver.queryFileName(uri: Uri): String {
    query(
        uri, null, null, null, null
    )?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        return cursor.getString(nameIndex)
    }

    return "Без названия"
}

fun takePersistableUriPermission(contentResolver: ContentResolver, uri: Uri) {
    contentResolver.takePersistableUriPermission(
        uri,
        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    )
}