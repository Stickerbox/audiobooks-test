package com.stickebox.audiobooks_test.models

/**
 * Domain model for a podcast
 *
 * @param id The ID of the podcast
 * @param name The title of the podcast
 * @param description The subtitle/description
 * @param isFavourite Whether the user has favourited this podcast
 * @param imageUrl The URL of the podcast's image to be remotely loaded
 * @param thumbnailImageUrl The thumbnail URL of the podcast's image to be remotely loaded
 */
data class Podcast(
    val id: String,
    val name: String,
    val description: String,
    val isFavourite: Boolean,
    val imageUrl: String,
    val thumbnailImageUrl: String,
)