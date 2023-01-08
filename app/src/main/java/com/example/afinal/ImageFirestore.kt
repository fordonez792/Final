package com.example.afinal

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class ImageFirestore(
    var name: String? = null,
    var description: String? = null,
    var imageUrl: String? = null,
    var uploader: String? = null,
    @ServerTimestamp
    var timestamp: Timestamp? = null
)
