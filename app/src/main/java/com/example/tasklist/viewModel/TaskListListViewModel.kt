package com.example.tasklist.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tasklist.model.RetrofitF
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.tasklist.model.TaskList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

@HiltViewModel
class TaskListListViewModel @Inject constructor(private val retrofit: RetrofitF) : ViewModel() {

	fun getTaskListList() {
		var observableRetrofit: Single<List<TaskList>> = retrofit.retrofitService.getALLTaskLists()
		observableRetrofit.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe( {
				v -> Log.d("TAG", "d: $v")
			}, {
				err -> Log.d("TAG", "d: $err")
			})
	}

		/*.enqueue(object : Callback<List<TaskList>> {
			override fun onResponse(
				call: Call<List<TaskList>>,
				response: Response<List<TaskList>>
			) {
			}

			override fun onFailure(call: Call<List<TaskList>>, t: Throwable) {
			}

		})*/
	}
