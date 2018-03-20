package ru.mamykin.foreignbooksreader.data.network

import retrofit2.http.GET
import ru.mamykin.foreignbooksreader.data.model.UpdateInfo
import rx.Observable

/**
 * Creation date: 5/29/2017
 * Creation time: 3:30 PM
 * @author Andrey Mamykin(mamykin_av)
 */
interface UpdateService {

    @get:GET("v2/592bfedb100000b414389889")
    val changelog: Observable<UpdateInfo>

    companion object {
        val BASE_URL = "http://www.mocky.io/"
    }
}