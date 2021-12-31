package com.example.aleppocollage.repository.payment

import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.payment.domain.Payment
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
class PaymentRepository @Inject constructor(
    private val connectDB: ConnectDB,
) {

    suspend fun getPaymentStudent(studentID: Int, studyYear: String): Flow<DataState<List<Payment>>> = flow {
        val payments: ArrayList<Payment> = ArrayList()
        emit(DataState.Loading)
        try {
            var response = false
            val connection: Connection? = connectDB.getConnection()
            if (connection == null) {
                emit(DataState.Connection)
            } else {
                val query = "Call AndroidStudentPayments(?,?)"
                val stmt: CallableStatement = connection.prepareCall(query)
                stmt.setInt("@StudentID", studentID)
                stmt.setString("@StudyYear", studyYear)
                stmt.execute()
                val rs: ResultSet = stmt.resultSet
                var id = 0
                var payNo = 0
                var date = ""
                var dateForMonth = ""
                var pay = 0

                while (rs.next()) {
                    response = true
                    id = rs.getInt(1)
                    payNo = rs.getInt(2)
                    date = rs.getString(3)
                    dateForMonth = rs.getString(4)
                    pay = rs.getInt(5)
                    payments.add(Payment(id, payNo, date, dateForMonth, pay))
                }

                if (response) {
                    emit(DataState.Success(payments))
                } else if (!response) {
                    emit(DataState.Failure(""))
                }
                connection.close()
            }
        } catch (exception: Exception) {
            emit(DataState.ExceptionState(exception))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getPaymentTeacher(teacherID: Int, studyYear: String): Flow<DataState<List<Payment>>> = flow {
        val payments: ArrayList<Payment> = ArrayList()
        emit(DataState.Loading)
        try {
            var response = false
            val connection: Connection? = connectDB.getConnection()
            if (connection == null) {
                emit(DataState.Connection)
            } else {
                val query = "Call AndroidTeacherPayments(?,?)"
                val stmt: CallableStatement = connection.prepareCall(query)
                stmt.setInt("@TeacherID", teacherID)
                stmt.setString("@StudyYear", studyYear)
                stmt.execute()
                val rs: ResultSet = stmt.resultSet
                var id = 0
                var payNo = 0
                var date = ""
//              var dateForMonth: String
                var pay = 0
                while (rs.next()) {
                    response = true
                    id = rs.getInt(1)
                    payNo = rs.getInt(2)
                    date = rs.getString(3)
//                   dateForMonth = rs.getString(4)
                    pay = rs.getInt(4)
                    payments.add(Payment(id, payNo, date, "", pay))
                }
                if (response) {
                    emit(DataState.Success(payments))
                } else if (!response) {
                    emit(DataState.Failure(""))
                }
                connection.close()
            }
        } catch (exception: Exception) {
            emit(DataState.ExceptionState(exception))
        }

    }.flowOn(Dispatchers.IO)

    suspend fun getCostStudent(studentID: Int, groupID: Int): Flow<DataState<Int>> = flow {
        emit(DataState.Loading)
        try {
            var response = false
            val connection: Connection? = connectDB.getConnection()
            if (connection == null) {
                emit(DataState.Connection)
            } else {
                val query = "Call AndroidStudentCost(?,?)"
                val stmt: CallableStatement = connection.prepareCall(query)
                stmt.setInt("@StudentID", studentID)
                stmt.setInt("@GroupID", groupID)
                stmt.execute()
                val rs: ResultSet = stmt.resultSet
                var cost = 0
                while (rs.next()) {
                    response = true
                    cost = rs.getInt(1)
                }
                if (response) {
                    emit(DataState.Success(cost))
                } else if (!response) {
                    emit(DataState.Failure(""))
                }
                connection.close()
            }
        } catch (exception: Exception) {
            emit(DataState.ExceptionState(exception))
        }

    }.flowOn(Dispatchers.IO)

    suspend fun getCostTeacher(teacherID: Int, groupID: Int, studyYear: String): Flow<DataState<Int>> = flow {
        emit(DataState.Loading)
        try {
            var response = false
            val connection: Connection? = connectDB.getConnection()
            if (connection == null) {
                DataState.Connection
            } else {
                val query = "Call AndroidTeacherCost(?,?,?)"
                val stmt: CallableStatement = connection.prepareCall(query)
                stmt.setInt("@TeacherID", teacherID)
                stmt.setInt("@GroupID", groupID)
                stmt.setString("@StudyYear", studyYear)
                stmt.execute()
                val rs: ResultSet = stmt.resultSet
                var cost = 0
                while (rs.next()) {
                    response = true
                    cost = rs.getInt(1)
                }

                if (response) {
                    emit(DataState.Success(cost))

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