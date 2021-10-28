package com.example.aleppocollage.repository.exam

import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.exam.domain.Exam
import com.example.aleppocollage.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.sql.CallableStatement
import java.sql.Connection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamRepository @Inject constructor(
    private val connectDB: ConnectDB,
) {

    suspend fun getExams(subjectID: Int, groupID: Int): Flow<DataState<List<Exam>>> =
        flow {

            var exams: ArrayList<Exam> = ArrayList()

            emit(DataState.Loading)

            try {
                var response = false
                val connection: Connection? = connectDB.getConnection()
                if (connection == null) {
                } else {
                    val query = "Call AndroidSubjectExamsSelect(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setInt("@SubjectID", subjectID)
                    stmt.setInt("@GroupID", groupID)

                    stmt.execute()
                    val rs = stmt.resultSet

                    var id: Int
                    var name: String
                    var description: String
                    var date: String
                    var deliveredDate: String
                    var maxMark: Int
                    var status: String

                    while (rs.next()) {

                        response = true

                        id = rs.getInt(1)
                        name = rs.getString(2)
                        description = rs.getString(3)
                        date = if (rs.getString(4) == null) "" else rs.getString(4)
                        deliveredDate = if (rs.getString(5) == null) "" else rs.getString(5)
                        maxMark = rs.getInt(6)
                        status = rs.getString(7)

                        exams.add(Exam(id, name, description, date, deliveredDate, maxMark, status))

                    }

                    if (response) {
                        emit(DataState.Success(exams))
                    } else if (!response) {
                        emit(DataState.Failure(""))
                    }

                    connection.close()
                }
            } catch (exception: Exception) {
                emit(DataState.ExceptionState(exception))
            }

        }.flowOn(Dispatchers.IO)

    suspend fun getExamsTableWithMarks(groupId: Int, studentId: Int): Flow<DataState<Unit>> =
        flow {

//            var exams: ArrayList<Exam> = ArrayList()

            emit(DataState.Loading)

            try {
                var response = false
                val connection: Connection? = connectDB.getConnection()
                if (connection == null) {
                } else {
                    val query = "Call AndroidExamsTableWithMarks(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setInt("@GroupID", groupId)
                    stmt.setInt("@StudentID", studentId)

                    stmt.execute()
                    val rs = stmt.resultSet

                    var examTableID: Int
                    var exam: String
                    var subject: String
                    var date: String?
                    var deliveredDate: String?
                    var maxMark = 0
                    var description: String?
                    var status: String?
                    var grade: Int

                    while (rs.next()) {

                        examTableID = rs.getInt(1)
                        exam = rs.getString(2)
                        subject = rs.getString(3)
                        date = rs.getString(4)
                        deliveredDate = rs.getString(5)
                        maxMark = rs.getInt(6)
                        description = rs.getString(7)
                        status = rs.getString(8)
                        grade = rs.getInt(9)

                    }

                    if (response) {
                        emit(DataState.Success(Unit))
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