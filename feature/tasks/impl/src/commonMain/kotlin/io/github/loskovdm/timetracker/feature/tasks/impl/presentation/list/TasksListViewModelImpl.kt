//package io.github.loskovdm.timetracker.feature.tasks.impl.presentation.list
//
//import androidx.compose.runtime.State
//import androidx.lifecycle.viewModelScope
//import io.github.loskovdm.domain.usecase.project.GetProjectByIdUseCase
//import io.github.loskovdm.domain.usecase.task.GetActiveTasksUseCase
//import io.github.loskovdm.domain.usecase.task.GetCompletedTasksUseCase
//import io.github.loskovdm.domain.usecase.task.GetTasksUseCase
//import io.github.loskovdm.timetracker.feature.tasks.api.model.Task
//import io.github.loskovdm.timetracker.feature.tasks.api.presentation.TasksListState
//import io.github.loskovdm.timetracker.feature.tasks.api.presentation.TasksListViewModel
//import io.github.loskovdm.timetracker.feature.tasks.impl.mapper.ProjectMapper
//import io.github.loskovdm.timetracker.feature.tasks.impl.mapper.TaskMapper
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import kotlin.uuid.ExperimentalUuidApi
//import kotlin.uuid.Uuid
//
//@OptIn(ExperimentalUuidApi::class)
//internal class TasksListViewModelImpl(
//    private val projectId: Uuid,
//    private val taskMapper: TaskMapper,
//    private val projectMapper: ProjectMapper,
//    private val getActiveTasksUseCase: GetActiveTasksUseCase,
//    private val getCompletedTasksUseCase: GetCompletedTasksUseCase,
//    private val getProjectByIdUseCase: GetProjectByIdUseCase,
//) : TasksListViewModel() {
////    private val _state = MutableStateFlow<TasksListState>(TasksListState.Loading)
////    override val state: StateFlow<TasksListState> = _state.asStateFlow()
//
//    val state: StateFlow<TasksListState> = combine(
//        getActiveTasksUseCase(projectId),
//        getCompletedTasksUseCase(projectId)
//    ) { activeTasks, completedTasks ->
//
//    }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = TasksListState.Loading,
//        )
//
//    fun loadTasks(projectId: Uuid) {
//
//        viewModelScope.launch {
//            _state.value = TasksListState.Loading
//
//            getProjectByIdUseCase(projectId)?.let {
//                projectMapper.toView(it)
//            }?.let { project ->
//                getTasksUseCase(projectId).collect { tasksList ->
//                    val mappedTasks = tasksList.map { task -> taskMapper.toView(task) }
//
//                    _state.value = if (mappedTasks.isEmpty()) {
//                        TasksListState.Empty
//                    } else {
//                        TasksListState.Loaded(
//                            tasks = mappedTasks
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    fun completeTask(task: Task) {
//
//    }
//
//    fun deleteTask(task: Task) {
//
//    }
//}