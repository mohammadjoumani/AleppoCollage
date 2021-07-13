package com.example.aleppocollage.repository.payment

import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.payment.domain.Payment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.ResultSet
import kotlin.math.cos

class PaymentRepository {

    fun getPaymentStudent(studentID: Int, studyYear: String): Flow<Payment?> {

        return flow {
            try {
                var isSuccess = false
                val connectDB = ConnectDB()
                val connection: Connection? = connectDB.getConncetion()
                if (connection == null) {
                } else {
                    val query = "Call AndroidStudentPayments(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setInt("@StudentID", studentID)
                    stmt.setString("@StudyYear", studyYear)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet
                    var id: Int
                    var payNo: Int
                    var date: String
                    var dateForMonth: String
                    var pay: Int
                    while (rs.next()) {
                        isSuccess = true
                        id = rs.getInt(1)
                        payNo = rs.getInt(2)
                        date = rs.getString(3)
                        dateForMonth = rs.getString(4)
                        pay = rs.getInt(5)
                        emit(
                            Payment(id, payNo, date, dateForMonth, pay)
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

    fun getPaymentTeacher(teacherID: Int, studyYear: String): Flow<Payment?> {

        return flow {
            try {
                var isSuccess = false
                val connectDB = ConnectDB()
                val connection: Connection? = connectDB.getConncetion()
                if (connection == null) {
                } else {
                    val query = "Call AndroidTeacherPayments(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setInt("@TeacherID", teacherID)
                    stmt.setString("@StudyYear", studyYear)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet
                    var id: Int
                    var payNo: Int
                    var date: String
//                    var dateForMonth: String
                    var pay: Int
                    while (rs.next()) {
                        isSuccess = true
                        id = rs.getInt(1)
                        payNo = rs.getInt(2)
                        date = rs.getString(3)
//                        dateForMonth = rs.getString(4)
                        pay = rs.getInt(4)
                        emit(
                            Payment(id, payNo, date, "", pay)
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

    fun getCostStudent(studentID:Int,groupID:Int):Flow<Int>{
        return flow {
            try {
                var isSuccess = false
                val connectDB = ConnectDB()
                val connection: Connection? = connectDB.getConncetion()
                if (connection == null) {
                } else {
                    val query = "Call AndroidStudentCost(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setInt("@StudentID", studentID)
                    stmt.setInt("@GroupID", groupID)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet
                    var cost: Int
                    while (rs.next()) {
                        isSuccess = true
                        cost = rs.getInt(1)
                        emit(cost)
                    }
                    if (isSuccess == false) {
                        emit(-1)
                    }
                    connection.close()
                }
            } catch (ex: Exception) {
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getCostTeacher(teacherID:Int,groupID:Int,studyYear:String):Flow<Int>{
        return flow {
            try {
                var isSuccess = false
                val connectDB = ConnectDB()
                val connection: Connection? = connectDB.getConncetion()
                if (connection == null) {
                } else {
                    val query = "Call AndroidTeacherCost(?,?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setInt("@TeacherID", teacherID)
                    stmt.setInt("@GroupID", groupID)
                    stmt.setString("@StudyYear", studyYear)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet
                    var cost: Int
                    while (rs.next()) {
                        isSuccess = true
                        cost = rs.getInt(1)
                        emit(cost)
                    }
                    if (isSuccess == false) {
                        emit(-1)
                    }
                    connection.close()
                }
            } catch (ex: Exception) {
            }
        }.flowOn(Dispatchers.IO)
    }

}