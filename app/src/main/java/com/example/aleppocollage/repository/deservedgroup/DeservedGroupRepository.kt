package com.example.aleppocollage.repository.deservedgroup

import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.deservedgroup.domain.DeservedGroup
import com.example.aleppocollage.model.groupstudent.domain.GroupStudent
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
class DeservedGroupRepository @Inject constructor(
    private val connectDB: ConnectDB,
) {

    suspend fun getDeservedGroups(studyYear: String, teacherID: Int): Flow<DataState<List<DeservedGroup>>> =
        flow {
            var deservedGroups: ArrayList<DeservedGroup> = ArrayList()
            emit(DataState.Loading)
            try {
                var response = false
                val connection: Connection? = connectDB.getConnection()
                if (connection == null) {
                    emit(DataState.Connection)
                } else {
                    val query = "Call AndroidTeacherDeservedGroupsSelect(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setString("@StudyYear", studyYear)
                    stmt.setInt("@TeacherID", teacherID)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet

                    var id: Int
                    var studyYear: String
                    var studyLevel: String
                    var groupName: String
                    var subject: String
                    var subjectID: Int

                    while (rs.next()) {

                        response = true

                        id = rs.getInt(1)
                        studyYear = rs.getString(2)
                        studyLevel = rs.getString(3)
                        groupName = rs.getString(4)
                        subject = rs.getString(5)
                        subjectID = rs.getInt(6)

                        deservedGroups.add(DeservedGroup(id, studyYear, studyLevel, groupName, subject, subjectID))

                    }

                    if (response) {
                        emit(DataState.Success(deservedGroups))
                    } else if (!response) {
                        emit(DataState.Failure(""))
                    }

                    connection.close()
                }
            } catch (exception: Exception) {
                emit(DataState.ExceptionState(exception))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getGroupStudentsSelect(groupId: Int, examTableId: Int): Flow<DataState<List<GroupStudent>>> =
        flow {
            var groupStudents: ArrayList<GroupStudent> = ArrayList()
            emit(DataState.Loading)
            try {
                var response = false
                val connection: Connection? = connectDB.getConnection()
                if (connection == null) {
                    emit(DataState.Connection)
                } else {
                    val query = "Call AndroidGroupStudentsSelect(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setInt("@GroupID", groupId)
                    stmt.setInt("@ExamTableID", examTableId)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet

                    var id: Int
                    var studentName: String
                    var grade: Int

                    while (rs.next()) {

                        response = true

                        id = rs.getInt(1)
                        studentName = rs.getString(2)
                        grade = rs.getInt(3)

                        groupStudents.add(GroupStudent(id, studentName, grade))

                    }

                    if (response) {
                        emit(DataState.Success(groupStudents))
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