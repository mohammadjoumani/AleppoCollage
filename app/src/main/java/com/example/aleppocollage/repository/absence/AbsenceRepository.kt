package com.example.aleppocollage.repository.absence

import android.util.Log
import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.absence.domain.Absence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.ResultSet

class AbsenceRepository {

    //region AbsenceStudent
    fun getAbsenceStudent(studentID: Int, date: String): Flow<Absence?> {

        return flow {
            try {
                var isSuccess = false
                val connectDB = ConnectDB()
                val connection: Connection? = connectDB.getConncetion()
                if (connection == null) {
                } else {
                    val query = "Call AndroidStudentAttendenceSelect(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setInt("@StudentID", studentID)
                    stmt.setString("@Date", date)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet
                    var id: Int?=null
                    var studentID: Int?=null
                    var student: String?=null
                    var attendenceValue: String?=null
                    var date: String?=null
                    var status: String?=null
                    var note: String?=null

                    var session0: String?=null
                    var session1: String?=null
                    var session2: String?=null
                    var session3: String?=null
                    var session4: String?=null
                    var session5: String?=null
                    var session6: String?=null
                    var session7: String?=null
                    var session8: String?=null
                    var session9: String? =null

                    while (rs.next()) {
                        isSuccess = true
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
                        emit(
                            Absence(
                                id, studentID, student, attendenceValue, date, status, note,
                                session0, session1, session2, session3, session4, session5,
                                session6, session7, session8, session9
                            )
                        )
                    }
                    if (isSuccess == false) {
                        emit(null)
                    }
                    connection.close()
                }
            } catch (ex: Exception) {
            }
        }.flowOn(Dispatchers.IO)
    }
    //endregion

    ///region Teacher
    fun getGroupAbsenceSelect(groupID: Int, lastDate: String): Flow<Absence?> {

        return flow {
            try {
                var isSuccess = false
                val connectDB = ConnectDB()
                val connection: Connection? = connectDB.getConncetion()
                if (connection == null) {
                } else {
                    val query = "Call AndroidGroupAttendenceSelect(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setString("@Date", lastDate)
                    stmt.setInt("@GroupID", groupID)
                    stmt.execute()

                    val rs: ResultSet = stmt.resultSet
                    var id: Int?=null
                    var studentID: Int?=null
                    var student: String?=null
                    var attendenceValue: String?=null
                    var date: String?=null
                    var status: String?=null
                    var note: String?=null

                    var session0: String?=null
                    var session1: String?=null
                    var session2: String?=null
                    var session3: String?=null
                    var session4: String?=null
                    var session5: String?=null
                    var session6: String?=null
                    var session7: String?=null
                    var session8: String?=null
                    var session9: String? =null

                    while (rs.next()) {
                        isSuccess = true
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
                        emit(
                            Absence(
                                id, studentID, student, attendenceValue, date, status, note,
                                session0, session1, session2, session3, session4, session5,
                                session6, session7, session8, session9
                            )
                        )

                    }
                    if (!isSuccess) {
                        emit(null)
                    }
                    connection.close()
                }
            } catch (ex: Exception) {
            }
        }.flowOn(Dispatchers.IO)
    }

    /*  fun setStudentAbsence(groupID: Int, lastDate: String): Flow<String?> {

          return flow {
              try {
                  var isSuccess = false
                  val connectDB = ConnectDB()
                  val connection: Connection? = connectDB.getConncetion()
                  if (connection == null) {
                  } else {
                      val query = "Call AndroidStudentAttendence(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                      val stmt: CallableStatement = connection.prepareCall(query)

                      stmt.execute()

                      connection.close()
                  }
              } catch (ex: Exception) {
              }
          }.flowOn(Dispatchers.IO)
      }*/

    //endregion
}