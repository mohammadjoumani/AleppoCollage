@file:Suppress("DEPRECATION")package com.example.aleppocollage.ui.registerteacherimport android.app.Activityimport android.content.Contextimport android.os.Bundleimport android.util.Logimport android.view.Viewimport android.view.inputmethod.InputMethodManagerimport android.widget.AdapterViewimport android.widget.Toastimport androidx.fragment.app.Fragmentimport androidx.navigation.fragment.findNavControllerimport com.example.aleppocollage.Rimport com.example.aleppocollage.databinding.FragmentRegisterTeacherBindingimport io.paperdb.Paperimport androidx.lifecycle.Observerimport androidx.lifecycle.lifecycleScopeimport com.example.aleppocollage.MainActivityimport com.example.aleppocollage.model.user.domain.Teacherimport com.example.aleppocollage.ui.loading.LoadingDialogimport com.example.aleppocollage.ui.registerteacher.adapter.TeacherSpinnerAdapterimport com.example.aleppocollage.util.Statusimport es.dmoral.toasty.Toastyimport kotlinx.coroutines.delay@Suppress("DEPRECATION")class RegisterTeacherFragment : Fragment(R.layout.fragment_register_teacher) {    private lateinit var binding: FragmentRegisterTeacherBinding    companion object {        lateinit var teachers: ArrayList<Teacher>        private lateinit var teacher: Teacher        val loadingDialog = LoadingDialog()    }    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        super.onViewCreated(view, savedInstanceState)        binding = FragmentRegisterTeacherBinding.bind(view)        ///region spinner teacher Name        teachers = ArrayList()        getAllTeacher()        binding.spinnerRegisterTeacherName.onItemSelectedListener =            object : AdapterView.OnItemSelectedListener {                override fun onNothingSelected(adapterView: AdapterView<*>?) {                }                override fun onItemSelected(                    adapterView: AdapterView<*>?, view: View?, position: Int, id: Long                ) {                    teacher = adapterView?.getItemAtPosition(position) as Teacher                }            }        //endregion        binding.btnRegisterTeacherBack.setOnClickListener {            findNavController().popBackStack()        }        binding.btnRegisterTeacherLogin.setOnClickListener {            hideKeyboard(activity as MainActivity)            val password = binding.edtRegisterTeacherPassword.text.toString().trim()            if (password == "") {                binding.edtRegisterTeacherPassword.error = getString(R.string.please_enter_password)                return@setOnClickListener            }            if (password.equals(teacher.password)) {                lifecycleScope.launchWhenCreated {                    loadingDialog.show((activity as MainActivity).supportFragmentManager, "LoadingDialog")                    Paper.book().write("typeUser", 2)                    Paper.book().write("Teacher", teacher)                    Paper.book().write("oneTime", false)                    delay(3000)                    loadingDialog.dismiss()                    val action =                        RegisterTeacherFragmentDirections.actionRegisterTeacherFragmentToWorkStudentOrTeacherFragment()                    findNavController().navigate(action)                    Toasty.success(activity as MainActivity, "Success!", Toast.LENGTH_SHORT, true)                        .show()                    (activity as MainActivity).setHomeScreen(R.id.workStudentOrTeacherFragment)                }            } else {                lifecycleScope.launchWhenCreated {                    loadingDialog.show((activity as MainActivity).supportFragmentManager, "LoadingDialog")                    delay(3000)                    loadingDialog.dismiss()                    Toasty.error(activity as MainActivity, getString(R.string.wrong_password), Toast.LENGTH_SHORT,                        true).show()                }            }        }    }    private fun getAllTeacher() {        val registerTeacherViewModel = RegisterTeacherViewModel()        registerTeacherViewModel.getAllTeacher()        registerTeacherViewModel.teachersInfo.observe(viewLifecycleOwner, Observer {            when (it.status) {                Status.FAILURE -> {                    Log.d("state", "FAILURE")                }                Status.ERROR -> {                    Log.d("state", "ERROR")                }                Status.SUCCESS -> {                    Log.d("state", "SUCCESS")                    teachers = it._data!!                    val adapter = activity?.applicationContext?.let {                        TeacherSpinnerAdapter(activity as MainActivity, teachers)                    }                    binding.spinnerRegisterTeacherName.adapter = adapter                }                Status.LOADING -> {                    Log.d("state", "LOADING")                }            }        })    }    private fun hideKeyboard(activity: Activity) {        val inputManager = activity            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager        val currentFocusedView = activity.currentFocus        if (currentFocusedView != null) {            inputManager.hideSoftInputFromWindow(                currentFocusedView.windowToken,                InputMethodManager.HIDE_NOT_ALWAYS            )        }    }}