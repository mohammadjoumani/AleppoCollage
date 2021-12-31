package com.example.aleppocollage.repository.attendance

import android.util.Log
import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.attendance.domain.Attendance
import com.example.aleppocollage.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.ResultSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepository @Inject constructor(
    private val connectDB: ConnectDB,
) {

    suspend fun getAttendanceStudent(studentID: Int, date: String): Flow<DataState<Attendance>> =
        flow {
            emit(DataState.Loading)
            try {
                var response = false
                val connection: Connection? = connectDB.getConnection()
                if (connection == null) {
                    emit(DataState.Connection)
                } else {
                    val query = "Call AndroidStudentAttendenceSelect(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setInt("@StudentID", studentID)
                    stmt.setString("@Date", date)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet

                    var id = 0
                    var studentID = 0
                    var student = ""
                    var attendenceValue: String? = null
                    var date: String? =  null
                    var status : String? = null
                    var note : String? = null

                    var session0: String? = null
                    var session1: String? = null
                    var session2: String? = null
                    var session3: String? = null
                    var session4: String? = null
                    var session5: String? = null
                    var session6: String? = null
                    var session7: String? = null
                    var session8: String? = null
                    var session9: String? = null

                    while (rs.next()) {
                        response = true
                        id = rs.getInt(1)
                        studentID = rs.getInt(2)
                        student = rs.getString(3)
                        attendenceValue = rs.getString(4)
                        date = rs.getString(5)
                        status = rs.getString(6)
                        note = rs.getString(7)

                        session0 = rs.getString(8)
                        session1 = rs.getString(9)
                        session2 = rs.getString(10)
                        session3 = rs.getString(11)
                        session4 = rs.getString(12)
                        session5 = rs.getString(13)
                        session6 = rs.getString(14)
                        session7 = rs.getString(15)
                        session8 = rs.getString(16)
                        session9 = rs.getString(17)
                    }
                    if (response) {
                        emit(
                            DataState.Success(
                                Attendance(
                                    id,
                                    studentID,
                                    student,
                                    attendenceValue,
                                    date,
                                    status,
                                    note,
                                    session0,
                                    session1,
                                    session2,
                                    session3,
                                    session4,
                                    session5,
                                    session6,
                                    session7,
                                    session8,
                                    session9,
                                )
                            )
                        )
                    } else if (!response) {
                    emit(DataState.Failure(""))
                    }
                    connection.close()
                }
            } catch (exception: Exception) {
                emit(DataState.ExceptionState(exception))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getGroupAttendanceSelect(groupID: Int, lastDate: String): Flow<DataState<List<Attendance>>> =
        flow {
            var attendances: ArrayList<Attendance> = ArrayList()
            emit(DataState.Loading)
            try {
                var response = false
                val connection: Connection? = connectDB.getConnection()
                if (connection == null) {
                    emit(DataState.Connection)
                } else {
                    val query = "Call AndroidGroupAttendenceSelect(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setString("@Date", lastDate)
                    stmt.setInt("@GroupID", groupID)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet

                    var id = 0
                    var studentID = 0
                    var student = ""
                    var attendenceValue: String? = null
                    var date: String? =  null
                    var status: String? = null
                    var note: String? = null

                    var session0: String? = null
                    var session1: String? = null
                    var session2: String? = null
                    var session3: String? = null
                    var session4: String? = null
                    var session5: String? = null
                    var session6: String? = null
                    var session7: String? = null
                    var session8: String? = null
                    var session9: String? = null

                    while (rs.next()) {
                        response = true
                        id = rs.getInt(1)
                        studentID = rs.getInt(2)
                        student = rs.getString(3)
                        attendenceValue = rs.getString(4)
                        date = rs.getString(5)
                        status = rs.getString(6)
                        note = rs.getString(7)

                        session0 = rs.getString(8)
                        session1 = rs.getString(9)
                        session2 = rs.getString(10)
                        session3 = rs.getString(11)
                        session4 = rs.getString(12)
                        session5 = rs.getString(13)
                        session6 = rs.getString(14)
                        session7 = rs.getString(15)
                        session8 = rs.getString(16)
                        session9 = rs.getString(17)
                        attendances.add(Attendance(
                            id,
                            studentID,
                            student,
                            attendenceValue,
                            date,
                            status,
                            note,
                            session0,
                            session1,
                            session2,
                            session3,
                            session4,
                            session5,
                            session6,
                            session7,
                            session8,
                            session9))
                    }
                    if (response) {
                        emit(DataState.Success(attendances))
                    } else if (!response) {
                        emit(DataState.Failure(""))
                    }
                    connection.close()
                }
            } catch (exception: Exception) {
                emit(DataState.ExceptionState(exception))
            }
        }.flowOn(Dispatchers.IO)


    suspend fun setAttendanceStudent(attendances: List<Attendance>, date: String): Flow<DataState<String>> =
        flow {
            emit(DataState.Loading)
            try {
                var response = false
                val connection: Connection? = connectDB.getConnection()
                if (connection == null) {
                    emit(DataState.Connection)
                } else {
                    for (attendance in attendances) {
                        val query = "Call AndroidStudentAttendence(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                        val stmt: CallableStatement = connection.prepareCall(query)
                        stmt.setInt("@ID", attendance.id)
                        stmt.setInt("@StudentID", attendance.studentID)
                        stmt.setString("@Date", date)
                        stmt.setString("@Note", attendance.note)
                        stmt.setString("@Session0", attendance.session0)
                        stmt.setString("@Session1", attendance.session1)
                        stmt.setString("@Session2", attendance.session2)
                        stmt.setString("@Session3", attendance.session3)
                        stmt.setString("@Session4", attendance.session4)
                        stmt.setString("@Session5", attendance.session5)
                        stmt.setString("@Session6", attendance.session6)
                        stmt.setString("@Session7", attendance.session7)
                        stmt.setString("@Session8", attendance.session8)
                        stmt.setString("@Session9", attendance.session9)
                        stmt.execute()
                    }
                    response = true

                    if (response){
                        emit(DataState.Success("تمت العلمية بنجاح"))
                    } else if (!response) {
                        emit(DataState.Failure(""))
                    }
                    connection.close()
                }
            } catch (exception: Exception) {
                emit(DataState.ExceptionState(exception))
            }
        }.flowOn(Dispatchers.IO)

}