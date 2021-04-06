package com.example.tasklist

data class Task (
    val kind : String,
    val id : String,
    val etag : String,
    val title : String,
    val updated : String,
    val selfLink : String,
    val parent : String,
    val position : String,
    val notes : String,
    val status : String,
    val due : String,
    val completed : String,
    val deleted : Boolean,
    val hidden : Boolean,
    val links : List<Links>
)
data class Links (
    val type : String,
    val description : String,
    val link : String
)