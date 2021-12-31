package com.example.aleppocollage.ui.mark.teacherimport android.os.Bundleimport androidx.fragment.app.Fragmentimport android.view.Viewimport androidx.activity.OnBackPressedCallbackimport androidx.core.view.isVisibleimport androidx.fragment.app.activityViewModelsimport androidx.fragment.app.viewModelsimport androidx.navigation.fragment.findNavControllerimport androidx.navigation.fragment.navArgsimport androidx.recyclerview.widget.LinearLayoutManagerimport androidx.recyclerview.widget.RecyclerViewimport com.example.aleppocollage.Rimport com.example.aleppocollage.databinding.FragmentMarkTeacherBindingimport com.example.aleppocollage.model.groupstudent.domain.GroupStudentimport com.example.aleppocollage.ui.attendance.teacher.AttendanceTeacherStateEventimport com.example.aleppocollage.ui.mark.teacher.adapter.MarkTeacherRecyclerAdapterimport com.example.aleppocollage.ui.main.model.ProfileInfoimport com.example.aleppocollage.ui.mark.student.MarkStateEventimport com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModelimport com.example.aleppocollage.util.Commonimport com.example.aleppocollage.util.DataStateimport dagger.hilt.android.AndroidEntryPoint@AndroidEntryPointclass MarkTeacherFragment : Fragment(R.layout.fragment_mark_teacher) {    ///region Variables    private lateinit var groupStudent: GroupStudent    private lateinit var studentAdapter: MarkTeacherRecyclerAdapter    private var groupStudents: ArrayList<GroupStudent> = ArrayList()    private val args: MarkTeacherFragmentArgs by navArgs()    //endregion    ///region ViewModel & Binding    private val viewModel: CheckedExamViewModel by viewModels()    private val sharedViewModel: SharedViewModel by activityViewModels()    private var _binding: FragmentMarkTeacherBinding? = null    private val binding get() = _binding!!    ///endregion    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        _binding = FragmentMarkTeacherBinding.bind(view)        init()        binding.apply {            cardMarkTeacherBack.setOnClickListener {                findNavController().navigateUp()            }            cardMarkTeacherProfile.setOnClickListener {                val state = !(sharedViewModel.showProfileInfo.value!!.state)                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = state)            }            btnMarkTeacherSave.setOnClickListener {                observeDataState()                viewModel.setStateEvent(                    MarkTeacherStateEvent.Marks,                    groupStudents                )            }        }        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {            override fun handleOnBackPressed() {                if (sharedViewModel.showProfileInfo.value!!.state) {                    sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = false)                } else{                    findNavController().navigateUp()                }            }        })    }    private fun init() {        studentAdapter = MarkTeacherRecyclerAdapter(            { groupStudent -> clickListener(groupStudent) },             groupStudents,             args.exam.maxMark        )        initRecyclerAndAdapter()        observeGroupStudentDataState()        viewModel.setStateEvent(            MarkTeacherStateEvent.GroupStudent,            groupId = args.groupId,            examTableId = args.exam.id        )        observeVariables()    }    private fun clickListener(groupStudnet: GroupStudent) {        groupStudent = groupStudnet        val action = MarkTeacherFragmentDirections.actionGlobalEnterMark(groupStudnet.studentName, args.exam.maxMark)        findNavController().navigate(action)    }    private fun initRecyclerAndAdapter() {        binding.apply {            recyclerMarkTeacher.setHasFixedSize(true)            recyclerMarkTeacher.adapter = studentAdapter            recyclerMarkTeacher.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)        }    }    private fun observeGroupStudentDataState() {        viewModel.groupStudentsDataState.observe(viewLifecycleOwner) {            binding.apply {                when (it) {                    is DataState.Loading -> {                        shimmerAnimationMarkTeacher.isVisible = true                        recyclerMarkTeacher.isVisible = false                        imgMarkTeacherChooseGroup.isVisible = true                    }                    is DataState.Success -> {                        groupStudents = it.data as ArrayList<GroupStudent>                        shimmerAnimationMarkTeacher.isVisible = false                        recyclerMarkTeacher.isVisible = true                        imgMarkTeacherChooseGroup.isVisible = false                        studentAdapter.setData(groupStudents)                        viewModel.setStateEvent(MarkTeacherStateEvent.NoneGroup)                    }                    is DataState.Failure -> {                        shimmerAnimationMarkTeacher.isVisible = false                        recyclerMarkTeacher.isVisible = false                        imgMarkTeacherChooseGroup.isVisible = true                        Common.showToast(requireActivity(), getString(R.string.strNotFoundData),"error")                        viewModel.setStateEvent(MarkTeacherStateEvent.NoneGroup)                    }                    is DataState.ExceptionState -> {                        shimmerAnimationMarkTeacher.isVisible = false                        recyclerMarkTeacher.isVisible = false                        imgMarkTeacherChooseGroup.isVisible = true                        Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))                        viewModel.setStateEvent(MarkTeacherStateEvent.NoneGroup)                    }                    is DataState.Connection -> {                        shimmerAnimationMarkTeacher.isVisible = false                        recyclerMarkTeacher.isVisible = false                        imgMarkTeacherChooseGroup.isVisible = true                        Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))                        viewModel.setStateEvent(MarkTeacherStateEvent.NoneGroup)                    }                }            }        }    }    private fun observeDataState() {        viewModel.setMarkDataState.observe(viewLifecycleOwner) {            when (it) {                is DataState.Loading -> {                    sharedViewModel.stateLoading.value = true                    sharedViewModel.textLoading.value = getString(R.string.strSaveMarks)                    findNavController().navigate(R.id.action_global_loadingDialog)                }                is DataState.Success -> {                    sharedViewModel.stateLoading.value = false                    findNavController().navigateUp()                    Common.showToast(requireActivity(), it.data, "success" )                    viewModel.setStateEvent(MarkTeacherStateEvent.NoneMarks)                }                is DataState.Failure -> {                    sharedViewModel.stateLoading.value = false                    Common.showToast(requireActivity(), it.message ,"error")                    viewModel.setStateEvent(MarkTeacherStateEvent.NoneMarks)                }                is DataState.ExceptionState -> {                    sharedViewModel.stateLoading.value = false                    Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))                    viewModel.setStateEvent(MarkTeacherStateEvent.NoneMarks)                }                is DataState.Connection -> {                    sharedViewModel.stateLoading.value = false                    Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))                    viewModel.setStateEvent(MarkTeacherStateEvent.NoneGroup)                }            }        }    }    private fun observeVariables() {        sharedViewModel.markValue.observe(viewLifecycleOwner) {            if (!it.equals(null) && it != 0) {                groupStudent.grade = it                studentAdapter.setData(groupStudents)            }        }    }    override fun onDestroy() {        super.onDestroy()        sharedViewModel.markValue.value = 0    }}