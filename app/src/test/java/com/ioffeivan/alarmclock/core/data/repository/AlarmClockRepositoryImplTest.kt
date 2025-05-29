package com.ioffeivan.alarmclock.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.ioffeivan.alarmclock.core.data.source.local.AlarmClockLocalDataSource
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.domain.model.AlarmClocks
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class AlarmClockRepositoryImplTest {

    private val alarmClockLocalDataSource = mockk<AlarmClockLocalDataSource>()
    private lateinit var alarmClockRepositoryImpl: AlarmClockRepositoryImpl

    @Before
    fun setUp() {
        alarmClockRepositoryImpl = AlarmClockRepositoryImpl(alarmClockLocalDataSource)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return flow with alarmClocks`() = runTest {
        val alarmClocksFlow = flowOf(AlarmClocks(listOf()))
        every { alarmClockLocalDataSource.getAllAlarmClocks() } returns alarmClocksFlow

        val result = alarmClockRepositoryImpl.getAllAlarmClocks().first()

        assertThat(result).isEqualTo(AlarmClocks(listOf()))
    }

    @Test
    fun `should return AlarmClock`() = runTest {
        val alarmClockMock = AlarmClock(id = 1, name = "AlarmClock")
        coEvery { alarmClockLocalDataSource.getAlarmClockById(1) } returns alarmClockMock

        val result = alarmClockRepositoryImpl.getAlarmClockById(1)

        assertThat(alarmClockMock).isEqualTo(result)
    }

    @Test
    fun `should return id added alarmClock`() = runTest {
        val alarmClock = AlarmClock()
        coEvery { alarmClockLocalDataSource.addAlarmClock(alarmClock) } returns 1L

        val result = alarmClockRepositoryImpl.addAlarmClock(alarmClock)

        assertThat(1L).isEqualTo(result)
    }

    @Test
    fun `should call updateAlarmClock from localDataSource`() = runTest {
        val alarmClock = AlarmClock()
        coEvery { alarmClockLocalDataSource.updateAlarmClock(alarmClock) } just runs

        alarmClockRepositoryImpl.updateAlarmClock(alarmClock)

        coVerify(exactly = 1) { alarmClockLocalDataSource.updateAlarmClock(alarmClock) }
    }

    @Test
    fun `should call deleteAlarmClock from localDataSource`() = runTest {
        val alarmClock = AlarmClock(id = 1, name = "name")
        coEvery { alarmClockLocalDataSource.deleteAlarmClock(alarmClock) } just runs

        alarmClockRepositoryImpl.deleteAlarmClock(alarmClock)

        coVerify(exactly = 1) { alarmClockLocalDataSource.deleteAlarmClock(alarmClock) }
    }
}