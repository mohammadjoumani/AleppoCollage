package com.example.aleppocollage.ui.mainimport android.os.Bundleimport android.view.Viewimport androidx.activity.viewModelsimport androidx.appcompat.app.AppCompatDelegateimport androidx.core.view.isVisibleimport androidx.navigation.NavGraphimport androidx.navigation.NavInflaterimport androidx.navigation.findNavControllerimport androidx.navigation.fragment.NavHostFragmentimport com.example.aleppocollage.Rimport com.example.aleppocollage.ui.util.network.model.NetworkConnectionimport com.example.aleppocollage.databinding.ActivityMainBindingimport com.example.aleppocollage.ui.base.BaseActivityimport com.example.aleppocollage.ui.main.model.ProfileInfoimport com.example.aleppocollage.ui.util.network.ConnectionViewModelimport com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModelimport com.example.aleppocollage.util.Commonimport dagger.hilt.android.AndroidEntryPointimport io.paperdb.Paper@AndroidEntryPointclass MainActivity : BaseActivity() {    ///region Variables    private lateinit var inflater: NavInflater    private lateinit var graph: NavGraph    private lateinit var navHostFragment: NavHostFragment    //endregion    //region ViewModel & Binding    private val viewModel: MainViewModel by viewModels()    private val sharedViewModel: SharedViewModel by viewModels()    private val connectionStateViewModel: ConnectionViewModel by viewModels()    private var _binding: ActivityMainBinding? = null    val binding get() = _binding!!    //endregion    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        _binding = ActivityMainBinding.inflate(layoutInflater)        val view = binding.root        setContentView(view)        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)        observeVariables()        initNavigationMain()        init()        binding.apply {            viewMainBlur.setOnClickListener {                val state = !(sharedViewModel.showProfileInfo.value!!.state)                when(Common.getCurrentTypeUser()) {                    "Student" -> {                        sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), student = Common.getCurrentStudent()!!, state = state)                    }                    "Teacher" -> {                        sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = state)                    }                }            }        }    }    private fun init() {        when(Common.getCurrentTypeUser()) {            "NotYet" -> {                setHomeScreen(R.id.RegisterFragment)            }            "Student" -> {                setHomeScreen(R.id.homeStudentFragment)            }            "Teacher" -> {                setHomeScreen(R.id.HomeTeacherFragment)            }        }    }    private fun initNavigationMain() {        navHostFragment =            (supportFragmentManager.findFragmentById(R.id.fragmentMainNavHost) as NavHostFragment)        inflater = navHostFragment.navController.navInflater        graph = inflater.inflate(R.navigation.mobile_navigation)    }    private fun setHomeScreen(id: Int) {        graph.startDestination = id        navHostFragment.navController.graph = graph    }    private fun observeVariables() {        sharedViewModel.showProfileInfo.observe(this) {            binding.apply {                linearMainInfo.isVisible = it.state                viewMainBlur.isVisible = it.state            }            if (it.userType == "Student") {                binding.apply {                    txtMainActivityName.text = it.student!!.name                    txtMainActivityGroup.text = it.student.groupLevel                }            } else if (it.userType == "Teacher") {                binding.apply {                    txtMainActivityName.text = it.teacher!!.name                    txtMainActivityGroup.isVisible = false                }            }        }        NetworkConnection(this).observe(this) {            sharedViewModel.isDissconnect.value = !it.state            binding.root.isVisible = it.state            if (!it.state) {                findNavController(R.id.fragmentMainNavHost).navigate(R.id.action_global_connectionStateDialog)            }        }        sharedViewModel.stateStartApp.observe(this) {            if (it != 0) {              setHomeScreen(it)            }        }    }}