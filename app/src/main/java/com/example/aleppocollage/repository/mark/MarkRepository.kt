package com.example.aleppocollage.repository.mark

import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.mark.domain.Mark
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
class MarkRepository @Inject constructor(
    private val connectDB: ConnectDB,
) {

    suspend fun getMarkStudent(studentID: Int, groupID: Int, month: Int, studyYear: String): Flow<DataState<List<Mark>>> = flow {
        var marks: ArrayList<Mark> = ArrayList()
        emit(DataState.Loading)
        try {
            var response = false
            val connection: Connection? = connectDB.getConnection()
            if (connection == null) {
            } else {
                val query = "Call AndroidStudentsExamsMarks(?,?,?,?)"
                val stmt: CallableStatement = connection.prepareCall(query)
                stmt.setInt("@StudentID", studentID)
                stmt.setInt("@GroupID", groupID)
                stmt.setString("@StudyYear", studyYear)
                stmt.setInt("@Month", month)
                stmt.execute()
                val rs: ResultSet = stmt.resultSet

                var subjectName = ""
                var examName = ""
                var maxMark = 0
                var date = ""
                var description = ""
                var grade = 0

                while (rs.next()) {
                    response = true
                    subjectName = rs.getString(1)
                    examName = rs.getString(2)
                    maxMark = rs.getInt(3)
                    date = rs.getString(4)
                    description = rs.getString(5)
                    grade = rs.getInt(6)

                    marks.add(Mark(subjectName, examName, maxMark, date, description, grade))
                }
                if (response) {
                    emit(DataState.Success(marks))
                } else if (!response) {
                    emit(DataState.Failure(""))
                }
                connection.close()
            }
        } catch (exception: Exception) {
            emit(DataState.ExceptionState(exception))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun UpdateMark(id: Int, maxMark: Int, description: String): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading)
        try {
            var response = false
            val connection: Connection? = connectDB.getConnection()
            if (connection == null) {
            } else {
                val query = "Call AndroidMaxMarkUpdate(?,?,?)"
                val stmt: CallableStatement = connection.prepareCall(query)
                stmt.setInt("@ID", id)
                stmt.setInt("@MaxMark", maxMark)
                stmt.setString("@Description", description)

                stmt.executeUpdate()
                response = true

                if (response){
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

    suspend fun UpdateMaxMark(id: Int, maxMark: Int, description: String): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading)
        try {
            var response = false
            val connection: Connection? = connectDB.getConnection()
            if (connection == null) {
            } else {
                val query = "Call AndroidMaxMarkUpdate(?,?,?)"
                val stmt: CallableStatement = connection.prepareCall(query)
                stmt.setInt("@ID", id)
                stmt.setInt("@MaxMark", maxMark)
                stmt.setString("@Description", description)

                stmt.executeUpdate()
                response = true

                if (response){
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