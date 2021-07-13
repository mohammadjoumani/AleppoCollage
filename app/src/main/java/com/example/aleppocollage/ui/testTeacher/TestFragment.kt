package com.example.aleppocollage.ui.testTeacherimport android.os.Bundleimport android.util.Logimport androidx.fragment.app.Fragmentimport android.view.Viewimport androidx.lifecycle.Observerimport android.widget.AdapterViewimport android.widget.Toastimport com.example.aleppocollage.MainActivityimport com.example.aleppocollage.Rimport com.example.aleppocollage.databinding.FragmentTestBindingimport com.example.aleppocollage.model.test.domain.DeservedGroupimport com.example.aleppocollage.model.user.domain.Teacherimport com.example.aleppocollage.ui.loading.LoadingDialogimport com.example.aleppocollage.ui.markStudent.adapter.YearSpinnerAdapterimport com.example.aleppocollage.ui.testTeacher.adapter.DeservedGroupsSpinnerAdapterimport com.example.aleppocollage.util.Statusimport es.dmoral.toasty.Toastyimport io.paperdb.Paperimport java.text.SimpleDateFormatimport java.util.*import kotlin.collections.ArrayList@Suppress("DEPRECATION")class TestFragment : Fragment(R.layout.fragment_test) {    private lateinit var binding: FragmentTestBinding    private var years: ArrayList<String>? = null    companion object {        lateinit var deservedGroups: ArrayList<DeservedGroup>        val loadingDialog = LoadingDialog()    }    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        super.onViewCreated(view, savedInstanceState)        binding = FragmentTestBinding.bind(view)        ///region spinner groups        val date: Date = Calendar.getInstance().time        val df = SimpleDateFormat("yyyy", Locale.getDefault())        val formattedDate: String = df.format(date)        years = ArrayList()        setYear(formattedDate.toInt())        val adapter = activity?.applicationContext?.let {            YearSpinnerAdapter(it, years!!)        }        binding.spinnerTestFragmentYear.adapter = adapter        binding.spinnerTestFragmentYear.onItemSelectedListener =            object : AdapterView.OnItemSelectedListener {                override fun onNothingSelected(adapterView: AdapterView<*>?) {                }                override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long                ) {                    val year = adapterView?.getItemAtPosition(position).toString()                    val teacher = Paper.book().read<Teacher>("Teacher")                    getDeservedGroup("$year-${year.toInt() + 1}", teacher.id)                }            }    }    ///region getGroups    fun getDeservedGroup(studyYear: String, teacherID: Int) {        val testViewModel = TestViewModel()        testViewModel.getDeservedGroup(studyYear, teacherID)        testViewModel.deservedGroup.observe(viewLifecycleOwner, Observer {            when (it.status) {                Status.LOADING -> {                    Log.d("state", "LOADING")                    loadingDialog.show(                        (activity as MainActivity).supportFragmentManager,                        "LoadingDialog"                    )                    binding.spinnerTestFragmentDepartment.isEnabled = false                }                Status.SUCCESS -> {                    Log.d("state", "SUCCESS")                    loadingDialog.dismiss()                    deservedGroups = it._data!!                    val adapter = activity?.applicationContext?.let {                        DeservedGroupsSpinnerAdapter(activity as MainActivity, deservedGroups)                    }                    binding.spinnerTestFragmentDepartment.adapter = adapter                    binding.spinnerTestFragmentDepartment.isEnabled = true                    if (deservedGroups.size == 0) {                        binding.spinnerTestFragmentDepartment.isEnabled = false                        Toasty.info(                            activity as MainActivity,                            resources.getString(R.string.no_groups),                            Toast.LENGTH_SHORT                        ).show()                    }                }                Status.FAILURE -> {                    Log.d("state", "FAILURE")                }                Status.ERROR -> {                    Log.d("state", "ERROR")                }            }        })    }    //endregion    private fun setYear(nowYear: Int) {        for (i in 2019..nowYear) {            Log.d("year1", "$i")            years?.add("$i")        }    }}