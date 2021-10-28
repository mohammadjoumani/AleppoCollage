package com.example.aleppocollage.util

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.aleppocollage.R
import com.example.aleppocollage.model.user.domain.Student
import com.example.aleppocollage.model.user.domain.Teacher
import com.google.android.material.snackbar.Snackbar
import io.paperdb.Paper
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Common {
    const val QR_CODE_REQUEST = 2000
    const val PHONE_NUMBER = "PhoneNumber"
    const val LOGIN_DATA = "LoginData"

    const val MANAGER_ROLE = "Manager"
    const val SUPERVISOR_ROLE = "Supervisor"
    const val GUEST_ROLE = "Guest"
    const val TEACHER_ROLE = "Teacher"
    const val STUDENT_ROLE = "Student"

    const val NotificationTopicApp = "App"
    const val NotificationTopicGuest = "Guest"
    const val NotificationTopicTeacher = "Teacher"
    const val NotificationTopicStudent = "Student"
    const val USER_TOPICS = "UserTopics"
    const val ALL_TOPIC = "all"

    // TODO: 28/04/2021 Clear it on newer version
    const val SHOULD_CLEAN_DATA = "ShouldCleanData"

    private const val APP_VERSION = "6_3_1"
    const val VERSION_FEATURES = "VersionFeatures"
    const val DB = "MosqueDb_$APP_VERSION"
    const val DB_VERSION = 4
    const val LATEST_UPDATE = "LatestUpdate"
    const val LATEST_UPDATE_VALUE = "2021-07-08 2:07:59 PM" // 2021-07-08%202%3A07%3A59%20PM

    const val CURRENT_PHONE = "CurrentPhone"
    const val CURRENT_PASSWORD = "CurrentPassword"
    const val ACCESS_TOKEN = "AccessToken"
    const val CURRENT_ROLE = "CurrentRole"
    const val FIRST_USING = "FirstUsing"
    const val CURRENT_STUDENT = "CurrentStudent"
    const val CURRENT_TEACHER = "CurrentTeacher"
    const val CURRENT_USER_TYPE = "CurrentUserType"

    const val APP_LANG = "AppLang"
    const val APP_COUNTRY = "AppCountry"
    const val ENGLISH_LOCALE_LANG = "en"
    const val ENGLISH_LOCALE_COUNTRY = "US"
    const val ARABIC_LOCALE_LANG = "ar"
    const val ARABIC_LOCALE_COUNTRY = "SY"

    const val DELETED_SELLING_ITEMS_IDS = "DeletedSellingItemsIds"
    const val DELETED_BILL_ITEMS_IDS = "DeletedBillItemsIds"
    const val DELETED_BILLS_IDS = "DeletedBillsIds"

    const val PRESENT_ATTENDANCE = "P"
    const val ABSCENT_ATTENDANCE = "A"

    /*
    * • = \u2022,   ● = \u25CF,   ○ = \u25CB,   ▪ = \u25AA,   ■ = \u25A0,   □ = \u25A1,   ► = \u25BA
    * */
    const val DOT_UNICODE = "\u25CB"

    ///region Permissions Requests

    const val EXTERNAL_REQUEST_PERMISSION = 500
    const val EXTERNAL_REQUEST_PERMISSION_PHOTO = 510
    const val EXTERNAL_REQUEST_PERMISSION_VIDEO = 520
    const val EXTERNAL_REQUEST_PERMISSION_AUDIO = 530
    const val EXTERNAL_REQUEST_PERMISSION_FILE = 540
    const val CAMERA_REQUEST_PERMISSION = 550
    const val LOCATION_REQUEST_PERMISSION = 570
    const val DOWNLOAD_FILES_REQUEST_PERMISSION = 560

    ///endregion

    ///region Requests Code

    const val GALLERY_REQUEST_CODE = 200

    ///endregion

    ///region Permissions

    fun isPermissionsExternalGranted(context: Context): Boolean {
        return !(ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
    }

    fun requestRuntimePermissionsExternal(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_REQUEST_PERMISSION)
    }

    fun requestRuntimePermissionsExternalForFiles(fragment: Fragment) {
        fragment.requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_REQUEST_PERMISSION_FILE)
    }

    fun requestRuntimePermissionsExternal(fragment: Fragment) {
        fragment.requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_REQUEST_PERMISSION)
    }

    ///endregion

    ///region Images

//    fun loadImage(glide: RequestManager, url: Any, placeHolder: Int, imageView: ImageView) {
//        glide
//            .load(url)
//            .centerCrop()
//            //.transition(DrawableTransitionOptions.withCrossFade())
//            .placeholder(placeHolder)
//            .error(placeHolder)
//            .into(imageView)
//    }

//    fun getBytesFromImageView(imageView: ImageView): ByteArray {
//        val bitmap = (imageView.drawable as RoundedDrawable).toBitmap()
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//        return byteArrayOutputStream.toByteArray()
//    }

    fun openGallery(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activity.startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    fun openGallery(fragment: Fragment) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        fragment.startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

//    fun cropImageWithImageCropper(activity: Activity, imageUri: Uri) {
//        CropImage.activity(imageUri)
//            .setAspectRatio(1, 1)
//            .setMinCropWindowSize(500, 500)
//            .start(activity)
//    }

//    fun cropImageWithImageCropper(context: Context, fragment: Fragment, imageUri: Uri) {
//        CropImage.activity(imageUri)
//            .setAspectRatio(1, 1)
//            .setMinCropWindowSize(500, 500)
//            .start(context, fragment)
//    }

    ///endregion

    //region Files

    fun openFile(fragment: Fragment) {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        fragment.startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    //endregion

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showSnackBar(context: Context, view: View, message: String, withAction: Boolean = false) {
        val snackBar = Snackbar.make(
            view, message, if (withAction) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG)

        //snackBar.setAction("Action", null)
        //snackBar.setActionTextColor(Color.BLUE)

        val snackBarView = snackBar.view
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            snackBarView.setBackgroundColor(context.getColor(R.color.colorRed))
        }

        val param = snackBarView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(30, 0, 30, 30)
        snackBarView.layoutParams = param

        val textView =
            snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            textView.setTextColor(context.getColor(R.color.colorWhite))
            snackBar.setActionTextColor(context.getColor(R.color.colorWhite))
        }
        textView.textSize = 16f

        if (withAction) {
            snackBar.setAction(context.getString(R.string.strOk)) {
                snackBar.dismiss()
            }
        }

        snackBar.show()
    }

    fun showToolTip(context: Context, view: View, text: String) {

    }

    ///region Notifications


    ///endregion

    fun logout() {
        Paper.book().write(CURRENT_PHONE, "")
        Paper.book().write(CURRENT_PASSWORD, "")
        Paper.book().write(ACCESS_TOKEN, "")
        Paper.book().write(CURRENT_ROLE, "")
        Paper.book().write(FIRST_USING, true)
        Paper.book().delete(CURRENT_STUDENT)
        Paper.book().delete(CURRENT_TEACHER)
        Paper.book().delete(CURRENT_USER_TYPE)
        Paper.book().write(LATEST_UPDATE, "")
        Paper.book().write(SHOULD_CLEAN_DATA, true)

        val topicsList = Paper.book().read(USER_TOPICS, listOf<String>())
        for (topic in topicsList) {
//            unsubscribeTopic(topic)
        }
    }

    fun shareApp(activity: Activity) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name))
            var shareMessage = "\nأنصحك باستخدام هذا التطبيق\n\n"
            shareMessage += "https://play.google.com/store/apps/details?id=com.intigron.kepler"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            activity.startActivity(Intent.createChooser(shareIntent, "اختر للمشاركة"))
        } catch (e: Exception) {
            //e.toString();
        }
    }

    fun getVersionName(context: Context): String {
        return context.packageManager.getPackageInfo(context.packageName, 0).versionName
    }

    fun openPlayStore(context: Context) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse("market://details?id=com.intigron.kepler")))
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.intigron.kepler")))
        }
    }

    fun storeCurrentUser(student: Student? = null, teacher: Teacher? = null) {

        var role = GUEST_ROLE

        if (student != null) {

            Paper.book().write(CURRENT_PASSWORD, student.password)
            Paper.book().write(CURRENT_STUDENT, student)
            Paper.book().write(CURRENT_USER_TYPE, "Student")
            role = STUDENT_ROLE

        } else if (teacher != null) {

            Paper.book().write(CURRENT_PASSWORD, teacher.password)
            Paper.book().write(CURRENT_TEACHER, teacher)
            Paper.book().write(CURRENT_USER_TYPE, "Teacher")

            role = TEACHER_ROLE

        }

        Paper.book().write(CURRENT_ROLE, role)
        val topicsList = listOf(
            NotificationTopicApp, role, NotificationTopicApp)

        Paper.book().write(USER_TOPICS, topicsList)

        for (topic in topicsList) {
            //subscribeTopic(topic)
        }
    }


    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale(ENGLISH_LOCALE_LANG))
        return simpleDateFormat.format(calendar.time)
    }

    fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        val pattern = "yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale(ENGLISH_LOCALE_LANG))
        return simpleDateFormat.format(calendar.time)
    }

    fun getCurrentDateAndTime(): String {
        val calendar = Calendar.getInstance()
        //val pattern = "dd/MM/yyyy HH:mm:ss"
        val pattern = "dd/MM/yyyy HH:mm"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale(ENGLISH_LOCALE_LANG))
        return simpleDateFormat.format(calendar.time)
    }

    fun getCurrentTypeUser(): String {
        return Paper.book().read(CURRENT_USER_TYPE, "NotYet")
    }

    fun getCurrentStudent(): Student? {
        return Paper.book().read(CURRENT_STUDENT, null)
    }

    fun getCurrentTeacher(): Teacher? {
        return Paper.book().read(CURRENT_TEACHER, null)
    }

    fun createFolderApp(
        childOne: String = "", childTwo: String = ""): String {

        var file: File? = null

        file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                "${Environment.getExternalStoragePublicDirectory(null)}" + "/" + childOne, childTwo)
        } else {
            File("${Environment.getExternalStorageDirectory()}" + "/" + childOne, childTwo)
        }

        if (!file!!.exists()) {
            file.mkdirs()
        }

        return file.absolutePath + File.separator
    }

    fun convrtDate(date: String): String {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale(ENGLISH_LOCALE_LANG))
        val myDate: Date = simpleDateFormat.parse(date)
        return simpleDateFormat.format(myDate)
    }

    fun getDayFromDate(date: String): String {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val myDate: Date = simpleDateFormat.parse(date)
        val outFormat = SimpleDateFormat("EEEE")
        return outFormat.format(myDate)
    }

}