package com.example.tasklist.view.itemModel

abstract class BaseItemModel {
	abstract val id: String
	abstract val title: String
	var clickable: Boolean = true
}