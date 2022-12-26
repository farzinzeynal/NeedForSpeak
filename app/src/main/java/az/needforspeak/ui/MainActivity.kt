package az.needforspeak.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import az.needforspeak.R
import az.needforspeak.base.BaseActivity
import az.needforspeak.databinding.ActivityMainBinding
import az.needforspeak.ui.register.ContainerNavFragment
import az.needforspeak.ui.register.post.AddPostActivity
import az.needforspeak.utils.*
import az.needforspeak.view_model.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var subCatId: String =""
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding = ActivityMainBinding::inflate
    val sharedPreferences by inject<SharedPreferences> { parametersOf("secure") }
    private val mainViewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val listFragment = mutableListOf<ContainerNavFragment>()
    private val listDest = mutableListOf<Int>()
    private val listDestLabel = mutableListOf<String>()
    private var bottomIndex = 0
    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)
        initApi()
//        views.buttonClick.setOnClickListener {
//            StaticValues.lang = "ru"
//            finish()
//            startActivity(intent)
//        }


        listFragment.add(ContainerNavFragment.newInstance(R.id.friendsFragment))
        listFragment.add(ContainerNavFragment.newInstance(R.id.chatsFragment))
        listFragment.add(ContainerNavFragment.newInstance(R.id.marketFragment))
        listFragment.add(ContainerNavFragment.newInstance(R.id.accountFragment))
        listDest.add(R.id.friendsFragment)
        listDest.add(R.id.chatsFragment)
        listDest.add(R.id.marketFragment)
        listDest.add(R.id.accountFragment)
        listDestLabel.add(resources.getString(R.string.friends))
        listDestLabel.add(resources.getString(R.string.chats))
        listDestLabel.add(resources.getString(R.string.market))
        listDestLabel.add(resources.getString(R.string.account))

        navContainerViewInit()
        bottomNavInit()

        views.backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    fun bottomNavHide(id: Int, index: Int) {
        if(bottomIndex == index) {
            when(id) {
                R.id.messagingActivity -> {
                    views.bottomNav.visibility = View.GONE
                }
                else -> {
                    views.bottomNav.visibility = View.VISIBLE
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun statusbarColor() {
        when(listDest[bottomIndex]) {
            R.id.friendsFragment -> {
                views.toolbarLayout.setBackgroundColor(resources.getColor(R.color.background_two_color))
                window.statusBarColor = resources.getColor(R.color.background_two_color)
            }
            R.id.marketFragment -> {
                views.toolbarLayout.setBackgroundColor(resources.getColor(R.color.background_two_color))
                window.statusBarColor = resources.getColor(R.color.background_two_color)
            }
            R.id.accountFragment -> {
                views.toolbarLayout.setBackgroundColor(resources.getColor(R.color.background_two_color))
                window.statusBarColor = resources.getColor(R.color.background_two_color)
            }
            else -> {
                views.toolbarLayout.setBackgroundColor(resources.getColor(R.color.background_color))
                window.statusBarColor = resources.getColor(R.color.background_color)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val username =  sharedPreferences.getString("username", "") ?: run { "" }
        val password = sharedPreferences.getString("password", "") ?: run { "" }
        if(XMPPController.getConnection()?.isConnected != true) {
            XMPPController.connect(username, password)
        }
    }

    override fun onPause() {
        super.onPause()
        XMPPController.disconnect()
    }

    private fun bottomNavInit() {
        views.bottomNav.setOnItemReselectedListener {
            when(it.itemId) {
                R.id.friendsNav -> {
                    val navCtr = listFragment[0].views.containerContent.findNavController()
                    while(navCtr.currentDestination?.id != R.id.friendsFragment) {
                        navCtr.navigateUp()
                    }
                }
                R.id.chatsNav -> {
                    val navCtr = listFragment[1].views.containerContent.findNavController()
                    while(navCtr.currentDestination?.id != R.id.chatsFragment) {
                        navCtr.navigateUp()
                    }
                }
                R.id.marketNav -> {
                    val navCtr = listFragment[2].views.containerContent.findNavController()
                    while(navCtr.currentDestination?.id != R.id.marketFragment) {
                        navCtr.navigateUp()
                    }
                }
                R.id.accountNav -> {
                    val navCtr = listFragment[3].views.containerContent.findNavController()
                    while(navCtr.currentDestination?.id != R.id.accountFragment) {
                        navCtr.navigateUp()
                    }
                }
            }
        }

        views.bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.friendsNav -> {
                    bottomIndex = 0
                    navContainerViewChange(0)
                    statusbarColor()
                    labelTrigger()
                    return@setOnItemSelectedListener true
                }
                R.id.chatsNav -> {
                    bottomIndex = 1
                    navContainerViewChange(1)
                    statusbarColor()
                    labelTrigger()
                    return@setOnItemSelectedListener true
                }
                R.id.marketNav -> {
                    bottomIndex = 2
                    navContainerViewChange(2)
                    statusbarColor()
                    labelTrigger()
                    return@setOnItemSelectedListener true
                }
                R.id.accountNav -> {
                    bottomIndex = 3
                    navContainerViewChange(3)
                    statusbarColor()
                    labelTrigger()
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

        views.addIcon.setOnClickListener {
            when(bottomIndex) {
                0 -> Navigation.findNavController(listFragment[0].views.containerContent).navigate(R.id.addFriendFragment, null, getNavOptions())
                2 -> {
                    startActivity(Intent(this, AddPostActivity::class.java).also {
                        it.putExtra(AddPostActivity.SELECTED_ROOM_JID, subCatId)
                    })
                }
            }
        }

        views.settingsIcon.setOnClickListener{
            Navigation.findNavController(listFragment[3].views.containerContent).navigate(R.id.userSettingsFragment, null, getNavOptions())
        }

       /* Handler(Looper.getMainLooper()).post {
            views.bottomNav.selectedItemId = R.id.chatsNav
        }*/
    }



    private fun navContainerViewInit() {
        supportFragmentManager.beginTransaction()
            .add(R.id.contentMain, listFragment[1], "Chats")
            .add(R.id.contentMain, listFragment[2], "Market")
            .add(R.id.contentMain, listFragment[3], "Account")
            .add(R.id.contentMain, listFragment[0], "Friends")
            .hide(listFragment[1])
            .hide(listFragment[2])
            .hide(listFragment[3])
            .show(listFragment[0])
            .commit()
        navContainerViewChange(0)
        statusbarColor()
        Handler().post{
            listFragment[0].views.containerContent.findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
                listDest[0] = destination.id
                listDestLabel[0] = controller.currentDestination?.label.toString()
                bottomNavHide(destination.id, 0)
                statusbarColor()
                labelTrigger()
            }
            listFragment[1].views.containerContent.findNavController().addOnDestinationChangedListener { controller, destination, arguments ->

                listDest[1] = destination.id
                listDestLabel[1] = controller.currentDestination?.label.toString()
                bottomNavHide(destination.id, 1)
                statusbarColor()
                labelTrigger()
            }
            listFragment[2].views.containerContent.findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
                listDest[2] = destination.id
                listDestLabel[2] = controller.currentDestination?.label.toString()
                bottomNavHide(destination.id, 2)
                statusbarColor()
                labelTrigger()
            }
            listFragment[3].views.containerContent.findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
                listDest[3] = destination.id
                listDestLabel[3] = controller.currentDestination?.label.toString()
                bottomNavHide(destination.id, 3)
                statusbarColor()
                labelTrigger()
            }
        }
    }

    fun labelTrigger() {
        views.toolbarTitle.text = listDestLabel[bottomIndex]
        backIcon()
        rightIcons()
    }

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        if (currentFocus != null) {
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    fun rightIcons() {
        if(bottomIndex == 0 && listDest[0] == R.id.friendsFragment) {
            views.addIcon.visibility = View.VISIBLE
        }else if(bottomIndex == 1 && listDest[1] == R.id.chatsFragment) {
            views.addIcon.visibility = View.VISIBLE
        }else if(bottomIndex == 2 && listDest[2] == R.id.postFragment) {
            views.addIcon.visibility = View.VISIBLE
        }else {
            views.addIcon.visibility = View.GONE
        }

        if(bottomIndex == 3 && listDest[3] == R.id.accountFragment) {
            views.settingsIcon.visibility = View.VISIBLE
        }else {
            views.settingsIcon.visibility = View.GONE
        }
    }

    fun backIcon() {
        if(bottomIndex == 0 && listDest[0] == R.id.friendsFragment) {
            views.backArrow.visibility = View.GONE
        }else if(bottomIndex == 1 && listDest[1] == R.id.chatsFragment) {
            views.backArrow.visibility = View.GONE
        }else if(bottomIndex == 2 && listDest[2] == R.id.marketFragment) {
            views.backArrow.visibility = View.GONE
        }else if(bottomIndex == 3 && listDest[3] == R.id.accountFragment) {
            views.backArrow.visibility = View.GONE
        }else {
            views.backArrow.visibility = View.VISIBLE
        }
    }

    fun navContainerViewChange(index: Int) {
        val fr = supportFragmentManager.beginTransaction()
        listFragment.forEachIndexed { i, f ->
            if(index == i) {
                fr.show(f)
                f.view?.visibility = View.VISIBLE
            }else {
                fr.hide(f)
                f.view?.visibility = View.GONE
            }
        }
        fr.commit()


    }

    fun getFragmentNow(): Fragment {
        return listFragment[bottomIndex]
    }

    fun selectNavIndex(index: Int) {
        when(index) {
            0 -> {
                listDest[0] = R.id.friendsFragment
                views.bottomNav.selectedItemId = R.id.friendsNav
            }
            1 -> {
                listDest[1] = R.id.chatsFragment
                views.bottomNav.selectedItemId = R.id.chatsNav
            }
            2 -> {
                listDest[2] = R.id.marketFragment
                views.bottomNav.selectedItemId = R.id.marketNav
            }
            3 -> {
                listDest[3] = R.id.accountFragment
                views.bottomNav.selectedItemId = R.id.accountNav
            }
        }
    }

    private fun initApi() {
        mainViewModel.loginResponse.observe(this) {
            Log.e("NetworkResult", it.toString())
            when(it) {
                is NetworkResult.Success -> {
                    it.data?.name
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
            }
        }
        mainViewModel.login()
    }

    @SuppressLint("RestrictedApi")
    override fun onBackPressed() {
        when(bottomIndex) {
            0 -> {
                val navCtrl = listFragment[1].views.containerContent.findNavController()
                navCtrl.backStack.count()
                if(listDest[0] == R.id.friendsFragment) {
                    super.onBackPressed()
                }else {
                    listFragment[0].views.containerContent.findNavController().popBackStack()
                }
            }
            1 -> {
                if(listDest[1] == R.id.chatsFragment) {
                    bottomIndex = 0
                    navContainerViewChange(0)
                    selectNavIndex(0)
                }else {
                    listFragment[1].views.containerContent.findNavController().popBackStack()
                }
            }
            2 -> {
                if(listDest[2] == R.id.marketFragment) {
                    bottomIndex = 0
                    navContainerViewChange(0)
                    selectNavIndex(0)
                }else {
                    listFragment[2].views.containerContent.findNavController().popBackStack()
                }
            }
            3 -> {
                if(listDest[3] == R.id.accountFragment) {
                    bottomIndex = 0
                    navContainerViewChange(0)
                    selectNavIndex(0)
                }else {
                    listFragment[3].views.containerContent.findNavController().popBackStack()
                }
            }
        }

        statusbarColor()
    }
}