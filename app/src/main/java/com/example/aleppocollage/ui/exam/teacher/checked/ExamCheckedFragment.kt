package com.example.aleppocollage.ui.exam.teacher.checkedimport android.os.Bundleimport android.util.Logimport androidx.fragment.app.Fragmentimport android.view.Viewimport androidx.core.view.isVisibleimport androidx.fragment.app.activityViewModelsimport androidx.fragment.app.viewModelsimport androidx.navigation.fragment.findNavControllerimport androidx.navigation.fragment.navArgsimport androidx.recyclerview.widget.LinearLayoutManagerimport androidx.recyclerview.widget.RecyclerViewimport com.example.aleppocollage.Rimport com.example.aleppocollage.databinding.FragmentExamCheckedBindingimport com.example.aleppocollage.model.groupstudent.domain.GroupStudentimport com.example.aleppocollage.ui.exam.teacher.checked.adapter.StudentMarkRecyclerAdapterimport com.example.aleppocollage.ui.main.model.ProfileInfoimport com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModelimport com.example.aleppocollage.util.Commonimport com.example.aleppocollage.util.DataStateimport dagger.hilt.android.AndroidEntryPoint@AndroidEntryPointclass ExamCheckedFragment : Fragment(R.layout.fragment_exam_checked) {    ///region Variables    private lateinit var studentAdapter: StudentMarkRecyclerAdapter    private var groupStudents: ArrayList<GroupStudent> = ArrayList()    private val args: ExamCheckedFragmentArgs by navArgs()    //endregion    ///region ViewModel & Binding    private val viewModel: CheckedExamViewModel by viewModels()    private val sharedViewModel: SharedViewModel by activityViewModels()    private var _binding: FragmentExamCheckedBinding? = null    private val binding get() = _binding!!    ///endregion    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        _binding = FragmentExamCheckedBinding.bind(view)        init()        binding.apply {            cardCheckedExamBack.setOnClickListener {                findNavController().navigateUp()            }            cardCheckedExamProfile.setOnClickListener {                val state = !(sharedViewModel.showProfileInfo.value!!.state)                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = state)            }        }    }    private fun init() {        studentAdapter = StudentMarkRecyclerAdapter(groupStudents, args.exam.maxMark)        initRecyclerAndAdapter()        observeExamDataState()        viewModel.setStateEvent(            CheckExamStateEvent.GroupStudent,            groupId = args.groupId,            examTableId = args.exam.id        )    }    private fun initRecyclerAndAdapter() {        binding.apply {            recyclerCheckExam.setHasFixedSize(true)            recyclerCheckExam.adapter = studentAdapter            recyclerCheckExam.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)        }    }    private fun observeExamDataState() {        viewModel.groupStudentsDataState.observe(viewLifecycleOwner) {            when (it) {                is DataState.Loading -> {                    binding.apply {                        swipeRefreshCheckedExam.isRefreshing = true                        shimmerAnimationCheckExam.isVisible = true                        recyclerCheckExam.isVisible = false                        imgExamChooseGroup.isVisible = true                    }                }                is DataState.Success -> {                    groupStudents = it.data as ArrayList<GroupStudent>                    binding.apply {                        swipeRefreshCheckedExam.isRefreshing = false                        shimmerAnimationCheckExam.isVisible = false                        recyclerCheckExam.isVisible = true                        imgExamChooseGroup.isVisible = false                    }                    studentAdapter.setData(groupStudents)                    viewModel.setStateEvent(CheckExamStateEvent.None)                }                is DataState.Failure -> {                    binding.apply {                        swipeRefreshCheckedExam.isRefreshing = false                        shimmerAnimationCheckExam.isVisible = false                        recyclerCheckExam.isVisible = false                        imgExamChooseGroup.isVisible = true                    }                    viewModel.setStateEvent(CheckExamStateEvent.None)                }                is DataState.ExceptionState -> {                    binding.apply {                        swipeRefreshCheckedExam.isRefreshing = false                        shimmerAnimationCheckExam.isVisible = false                        recyclerCheckExam.isVisible = false                        imgExamChooseGroup.isVisible = true                    }                    viewModel.setStateEvent(CheckExamStateEvent.None)                }            }        }    }}