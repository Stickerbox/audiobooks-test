package com.stickebox.audiobooks_test.models

import androidx.compose.ui.text.AnnotatedString

/**
 * Domain model for a podcast
 *
 * @param id The ID of the podcast
 * @param name The title of the podcast
 * @param description The subtitle/description
 * @param isFavourite Whether the user has favourited this podcast
 * @param imageUrl The URL of the podcast's image to be remotely loaded
 * @param thumbnailImageUrl The thumbnail URL of the podcast's image to be remotely loaded
 * @param publisher The name of the podcast's publisher
 */
data class Podcast(
    val id: String,
    val name: String,
    val description: AnnotatedString,
    val isFavourite: Boolean,
    val imageUrl: String,
    val thumbnailImageUrl: String,
    val publisher: String,
)