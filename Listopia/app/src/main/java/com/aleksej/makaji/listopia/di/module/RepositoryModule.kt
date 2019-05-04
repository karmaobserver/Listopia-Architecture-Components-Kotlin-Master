package com.aleksej.makaji.listopia.di.module

import android.app.Application
import androidx.room.Room
import com.aleksej.makaji.listopia.data.api.ListopiaApi
import com.aleksej.makaji.listopia.data.repository.ProductDataSource
import com.aleksej.makaji.listopia.data.room.ListopiaDatabase
import com.aleksej.makaji.listopia.data.repository.ShoppingListDataSource
import com.aleksej.makaji.listopia.data.repository.local.ProductLocalDataSource
import com.aleksej.makaji.listopia.data.repository.local.ShoppingListLocalDataSource
import com.aleksej.makaji.listopia.data.repository.remote.ProductRemoteDataSource
import com.aleksej.makaji.listopia.data.repository.remote.ShoppingListRemoteDataSource
import com.aleksej.makaji.listopia.data.room.ProductDao
import com.aleksej.makaji.listopia.data.room.ShoppingListDao
import com.aleksej.makaji.listopia.di.annotation.Listopia
import com.aleksej.makaji.listopia.di.annotation.Local
import com.aleksej.makaji.listopia.di.annotation.Remote
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
    fun bindProductRemoteDataSource(): ProductDataSource {
        return ProductRemoteDataSource()
    }

    @Singleton
    @Provides
    fun provideProductDao(listopiaDatabase: ListopiaDatabase): ProductDao {
        return listopiaDatabase.productDao()
    }
}