package com.example.aleppocollage.ui.auth.login.teacherimport android.os.Bundleimport android.text.method.HideReturnsTransformationMethodimport android.text.method.PasswordTransformationMethodimport android.view.Viewimport android.widget.AdapterViewimport android.widget.Toastimport androidx.core.view.isVisibleimport androidx.fragment.app.Fragmentimport androidx.fragment.app.activityViewModelsimport androidx.fragment.app.viewModelsimport androidx.navigation.fragment.findNavControllerimport com.example.aleppocollage.Rimport com.example.aleppocollage.databinding.FragmentRegisterTeacherBindingimport androidx.lifecycle.lifecycleScopeimport com.example.aleppocollage.model.user.domain.Teacherimport com.example.aleppocollage.ui.auth.login.teacher.adapter.TeacherSpinnerAdapterimport com.example.aleppocollage.ui.util.loading.LoadingViewModelimport com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModelimport com.example.aleppocollage.util.Commonimport com.example.aleppocollage.util.DataStateimport dagger.hilt.android.AndroidEntryPointimport es.dmoral.toasty.Toastyimport io.paperdb.Paperimport kotlinx.coroutines.delay@AndroidEntryPointclass RegisterTeacherFragment : Fragment(R.layout.fragment_register_teacher) {    ///region Variables    private var indexTeacher = -1    private var teachers: ArrayList<Teacher> = ArrayList()    //endregion    ///region ViewModel & Binding    private val viewModel: RegisterTeacherViewModel by viewModels()    private val sharedViewModel: SharedViewModel by activityViewModels()    private var _binding: FragmentRegisterTeacherBinding? = null    private val binding get() = _binding!!    ///endregion    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        _binding = FragmentRegisterTeacherBinding.bind(view)        init()        binding.apply {            btnRegisterTeacherBack.setOnClickListener {                findNavController().navigateUp()            }            btnRegisterTeacherLogin.setOnClickListener {                Common.hideKeyboard(requireActivity())                val password = binding.edtRegisterTeacherPassword.text.toString().trim()                if (indexTeacher == -1) {                    Toasty.error(requireContext(), getString(R.string.strPleaseChooseTeacher), Toast.LENGTH_SHORT,true).show()                    return@setOnClickListener                }                if (password.isEmpty()) {                    binding.edtRegisterTeacherPassword.error = getString(R.string.strPleaseEnterPassword)                    return@setOnClickListener                }                if (password.equals(teachers[indexTeacher].password)) {                    lifecycleScope.launchWhenCreated {                        aviRegisterTeacher.isVisible = true                        btnRegisterTeacherLogin.isVisible = false                        delay(3000)                        Common.storeCurrentUser(teacher = teachers[indexTeacher])                        val action = RegisterTeacherFragmentDirections.actionRegisterTeacherFragmentToWorkStudentOrTeacherFragment()                        findNavController().navigate(action)                        Toasty.success(requireContext(), "Success!", Toast.LENGTH_SHORT, true)                            .show()                        sharedViewModel.stateStartApp.value = R.id.HomeTeacherFragment                    }                } else {                    aviRegisterTeacher.isVisible = false                    btnRegisterTeacherLogin.isVisible = true                    Common.showSnackBar(requireContext(), binding.root, getString(R.string.strWrongPassword))                }            }            spinnerRegisterTeacherName.onItemSelectedListener =                object : AdapterView.OnItemSelectedListener {                    override fun onNothingSelected(adapterView: AdapterView<*>?) {                    }                    override fun onItemSelected(                        adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {                        indexTeacher = position                    }                }            imgRegisterTeacherShowPassword.setOnClickListener {                viewModel.showPassword.value = !(viewModel.showPassword.value)!!            }        }    }    private fun init() {        observeShowPassword()        observeTeachersDataState()        viewModel.setStateEvent(RegisterTeacherStateEvent.GetAllTeachers)    }    private fun observeTeachersDataState() {        viewModel.teachersDataState.observe(viewLifecycleOwner) {            when (it) {                is DataState.Loading -> {                    binding.apply {                        spinnerRegisterTeacherName.isEnabled = false                        aviRegisterTeacherSpinner.isVisible = true                        txtRegisterTeacherSpinner.isVisible = false                    }                }                is DataState.Success -> {                    teachers = it.data as ArrayList<Teacher>                    if (teachers.size > 0) {                        binding.apply {                            spinnerRegisterTeacherName.isEnabled = true                            aviRegisterTeacherSpinner.isVisible = false                            txtRegisterTeacherSpinner.isVisible = false                        }                        val adapter = TeacherSpinnerAdapter(requireContext(), teachers)                        binding.spinnerRegisterTeacherName.adapter = adapter                    } else {                        binding.apply {                            spinnerRegisterTeacherName.isEnabled = false                            aviRegisterTeacherSpinner.isVisible = false                            txtRegisterTeacherSpinner.isVisible = true                        }                    }                    viewModel.setStateEvent(RegisterTeacherStateEvent.None)                }                is DataState.Failure -> {                    teachers.clear()                    binding.apply {                        spinnerRegisterTeacherName.isEnabled = false                        aviRegisterTeacherSpinner.isVisible = false                        txtRegisterTeacherSpinner.isVisible = true                    }                    viewModel.setStateEvent(RegisterTeacherStateEvent.None)                }                is DataState.ExceptionState -> {                    teachers.clear()                    binding.apply {                        spinnerRegisterTeacherName.isEnabled = false                        aviRegisterTeacherSpinner.isVisible = false                        txtRegisterTeacherSpinner.isVisible = true                        txtRegisterTeacherSpinner.text = getString(R.string.strThereIsProblem)                    }                    viewModel.setStateEvent(RegisterTeacherStateEvent.None)                }            }        }    }    private fun observeShowPassword() {        viewModel.showPassword.observe(viewLifecycleOwner) {            binding.apply {                edtRegisterTeacherPassword.transformationMethod =                    if (it!!) HideReturnsTransformationMethod() else PasswordTransformationMethod()                imgRegisterTeacherShowPassword.setBackgroundResource(if (it) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off)            }        }    }    override fun onDestroyView() {        super.onDestroyView()        _binding = null    }}