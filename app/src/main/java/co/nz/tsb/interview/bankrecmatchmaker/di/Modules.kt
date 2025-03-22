package co.nz.tsb.interview.bankrecmatchmaker.di

import co.nz.tsb.interview.bankrecmatchmaker.repository.FakeRemoteRepository
import co.nz.tsb.interview.bankrecmatchmaker.core.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    fun provideApiService(): Repository {
        return FakeRemoteRepository()
    }

    @Provides
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.Default
}