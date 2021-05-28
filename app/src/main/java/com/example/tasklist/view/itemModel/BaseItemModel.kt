package com.example.tasklist.view.itemModel

import androidx.lifecycle.MutableLiveData

abstract class BaseItemModel {
	abstract val id: String
	abstract val title: String
	var clickable = MutableLiveData(true)
}