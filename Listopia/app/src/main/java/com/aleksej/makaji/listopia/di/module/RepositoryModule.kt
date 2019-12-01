package com.aleksej.makaji.listopia.di.module

import android.app.Application
import androidx.room.Room
import com.aleksej.makaji.listopia.data.api.ListopiaApi
import com.aleksej.makaji.listopia.data.repository.ProductDataSource
import com.aleksej.makaji.listopia.data.room.ListopiaDatabase
import com.aleksej.makaji.listopia.data.repository.ShoppingListDataSource
import com.aleksej.makaji.listopia.data.repository.UserDataSource
import com.aleksej.makaji.listopia.data.repository.local.ProductLocalDataSource
import com.aleksej.makaji.listopia.data.repository.local.ShoppingListLocalDataSource
import com.aleksej.makaji.listopia.data.repository.local.UserLocalDataSource
import com.aleksej.makaji.listopia.data.repository.remote.ProductRemoteDataSource
import com.aleksej.makaji.listopia.data.repository.remote.ShoppingListRemoteDataSource
import com.aleksej.makaji.listopia.data.repository.remote.UserRemoteDataSource
import com.aleksej.makaji.listopia.data.room.dao.ProductDao
import com.aleksej.makaji.listopia.data.room.dao.ShoppingListDao
import com.aleksej.makaji.listopia.data.room.dao.UserDao
import com.aleksej.makaji.listopia.di.annotation.Listopia
import com.aleksej.makaji.listopia.di.annotation.Local
import com.aleksej.makaji.listopia.di.annotation.Remote
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideChatDataBaseRoom(application: Application): ListopiaDatabase {
        return Room.databaseBuilder(application, ListopiaDatabase::class.java, "listopia.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    @Local
    fun bindShoppingListLocalDataSource(shoppingListDao: ShoppingListDao): ShoppingListDataSource {
        return ShoppingListLocalDataSource(shoppingListDao)
    }

    @Singleton
    @Provides
    @Remote
    fun bindShoppingListRemoteDataSource(listopiaApi: ListopiaApi, @Listopia retrofit: Retrofit): ShoppingListDataSource {
        return ShoppingListRemoteDataSource(listopiaApi, retrofit)
    }

    @Singleton
    @Provides
    fun provideShoppingListDao(listopiaDatabase: ListopiaDatabase): ShoppingListDao {
        return listopiaDatabase.shoppingListDao()
    }

    @Singleton
    @Provides
    @Local
    fun bindProductLocalDataSource(productDao: ProductDao): ProductDataSource {
        return ProductLocalDataSource(productDao)
    }

    @Singleton
    @Provides
    @Remote
    fun bindProductRemoteDataSource(listopiaApi: ListopiaApi, @Listopia retrofit: Retrofit): ProductDataSource {
        return ProductRemoteDataSource(listopiaApi, retrofit)
    }

    @Singleton
    @Provides
    fun provideProductDao(listopiaDatabase: ListopiaDatabase): ProductDao {
        return listopiaDatabase.productDao()
    }

    @Singleton
    @Provides
    @Local
    fun bindUserLocalDataSource(userDao: UserDao, sharedPreferenceManager: SharedPreferenceManager, listopiaDatabase: ListopiaDatabase): UserDataSource {
        return UserLocalDataSource(userDao, sharedPreferenceManager, listopiaDatabase)
    }

    @Singleton
    @Provides
    @Remote
    fun bindUserRemoteDataSource(listopiaApi: ListopiaApi, @Listopia retrofit: Retrofit, sharedPreferenceManager: SharedPreferenceManager): UserDataSource {
        return UserRemoteDataSource(listopiaApi, retrofit, sharedPreferenceManager)
    }

    @Singleton
    @Provides
    fun provideUserDao(listopiaDatabase: ListopiaDatabase): UserDao {
        return listopiaDatabase.userDao()
    }
}