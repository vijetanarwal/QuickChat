package com.vijet.quickchat.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.vijet.quickchat.R
import com.vijet.quickchat.network.Api
import com.vijet.quickchat.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestoreInstance():FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseMessagingInstance():FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constant.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFcmApi() =
        Retrofit.Builder()
            .baseUrl(Constant.FCM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

    @Singleton
    @Provides
    fun provideRemoteMsgHeaders(@ApplicationContext context: Context) =
        HashMap<String,String>().apply {
            //put(Constant.REMOTE_MSG_AUTHORIZATION, String.format("key=%s",context.getString(R.string.firebase_server_key)))
            put(Constant.REMOTE_MSG_CONTENT_TYPE,"application/json")
        }

    /*  @Provides
      @Singleton
      fun provideSharedPreferencesEditor(preferences: SharedPreferences):SharedPreferences.Editor {
          return preferences.edit()
      }*/

}