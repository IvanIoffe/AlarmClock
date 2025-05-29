package com.ioffeivan.alarmclock

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.ioffeivan.alarmclock.core.data.source.local.dao.AlarmClockDao
import com.ioffeivan.alarmclock.core.data.source.local.db.AppRoomDatabase
import com.ioffeivan.alarmclock.core.data.source.local.model.AlarmClockLocal
import com.ioffeivan.alarmclock.sound_selection.data.model.SoundLocal
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
class AlarmClockDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: AppRoomDatabase

    private lateinit var dao: AlarmClockDao
    private lateinit var alarmClockLocal: AlarmClockLocal

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.alarmClockDao()
        alarmClockLocal = AlarmClockLocal(
            id = 1,
            hour = 0,
            minute = 0,
            isEnabled = true,
            sound = SoundLocal(
                name = "name",
                type = "device",
                uri = "uri",
            ),
            isVibrate = false,
            name = "alarmClockName",
            timeSnooze = 10,
        )
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun getAllAlarmClock_sortedTime() = runTest {
        val alarmClock1 = alarmClockLocal.copy(id = 1, hour = 12, minute = 45)
        val alarmClock2 = alarmClockLocal.copy(id = 2, hour = 1, minute = 34)
        val alarmClock3 = alarmClockLocal.copy(id = 3, hour = 12, minute = 12)
        val alarmClock4 = alarmClockLocal.copy(id = 4, hour = 23, minute = 34)

        dao.addAlarmClock(alarmClock1)
        dao.addAlarmClock(alarmClock2)
        dao.addAlarmClock(alarmClock3)
        dao.addAlarmClock(alarmClock4)

        val alarmClockList = dao.getAllAlarmClocks().first()

        assertThat(alarmClockList).containsExactly(
            alarmClock2, alarmClock3, alarmClock1, alarmClock4,
        ).inOrder()
    }
}