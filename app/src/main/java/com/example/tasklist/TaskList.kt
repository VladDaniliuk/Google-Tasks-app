package com.example.tasklist

data class TaskList (
    val kind : String,
    val id : String,
    val etag : String,
    val title : String,
    val updated : String,
    val selfLink : String
)