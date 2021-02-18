package com.hatfat.swccg.data.format

data class SWCCGFormat(
    val name: String,
    val code: String,
    val set: Array<String>, /* array of gemp set codes */
    val bannedIcons: Array<String>, /* array of gemp icon strings */
    val banned: Array<String> /* array of banned gemp card ids */
)