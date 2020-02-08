package com.hexbit.additionandsubtraction.util.base

import android.annotation.SuppressLint
import android.app.Application
import com.hexbit.additionandsubtraction.R
import com.hexbit.additionandsubtraction.data.AppDatabase
import com.hexbit.additionandsubtraction.data.model.ExerciseType
import com.hexbit.additionandsubtraction.data.model.Operation
import com.hexbit.additionandsubtraction.data.model.Settings
import com.hexbit.additionandsubtraction.di.appModule
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin

/**
 * Main application object.
 */
class BaseApplication : Application() {

    private val database: AppDatabase by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin(
            this,
            listOf(appModule)
        )
        initDatabaseIfNeeded()
    }

    @SuppressLint("CheckResult")
    private fun initDatabaseIfNeeded() {
        database.settingsDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { databaseSettings ->
                if (databaseSettings.isEmpty()) {
                    database.settingsDao()
                        .insertAll(
                            arrayListOf(
                                Settings(
                                    maxNumberInBattleMode = 100,
                                    lastNickname1 = getString(R.string.player1),
                                    lastNickname2 = getString(R.string.player2)
                                )
                            )
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                } else {
                    Completable.complete()
                }
            }
            .subscribe()

        val exercises = database.exerciseTypeDao().getAll()
        exercises
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { list ->
                if (list.isEmpty()) {
                    initializeExerciseListInDatabase()
                } else {
                    Completable.complete()
                }
            }.subscribe()
    }

    private fun initializeExerciseListInDatabase(): Completable {
        val firstRange = 5..20 step 5
        val secondRange = 30..100 step 10
        val range = firstRange.plus(secondRange)
        val exercises = arrayListOf<ExerciseType>()

        range.forEach {
            exercises.add(
                ExerciseType(
                    Operation.PLUS,
                    it
                )
            )
            exercises.add(
                ExerciseType(
                    Operation.MINUS,
                    it
                )
            )
            exercises.add(
                ExerciseType(
                    Operation.PLUS_MINUS,
                    it
                )
            )
        }
        return database.exerciseTypeDao()
            .insertAll(exercises)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}