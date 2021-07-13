package com.example.aleppocollage.repository.mark

import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.mark.domain.Mark
import com.example.aleppocollage.model.user.domain.Student
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
class MarkRepository() {

    fun getMarkStudent(studentID: Int, groupID: Int, month: Int, studyYear: String): Flow<Mark?> {

        return flow {
            try {
                var isSuccess = false
                val connectDB = ConnectDB()
                val connection: Connection? = connectDB.getConncetion()
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
                    var subjectName: String
                    var examName: String
                    var maxMark: Int
                    var date: String
                    var description: String
                    var grade: Int
                    while (rs.next()) {
                        isSuccess = true
                        subjectName = rs.getString(1)
                        examName = rs.getString(2)
                        maxMark = rs.getInt(3)
                        date = rs.getString(4)
                        description = rs.getString(5)
                        grade = rs.getInt(6)
                        emit(
                            Mark(subjectName, examName, maxMark, date, description, grade)
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
}