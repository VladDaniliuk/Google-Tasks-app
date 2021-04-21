package com.example.tasklist.api.model.response

data class BaseListResponse<T>(
	val kind: String,
	val etag: String,
	val nextToken: String?,
	val items: List<T>
)