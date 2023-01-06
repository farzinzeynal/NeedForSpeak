package az.needforspeak.repository

import az.needforspeak.base.BaseApiResponse
import az.needforspeak.data.MainService
import az.needforspeak.utils.MyAccount
import az.needforspeak.utils.XMPPController

class MainRepository(private val service: MainService) : BaseApiResponse() {

    fun searchUserXMPP(userName: String, calback: (ArrayList<MyAccount>?) -> Unit) {
        XMPPController.checkIfUserExists(userName) {
            it
            calback.invoke(it)
        }
    }


}