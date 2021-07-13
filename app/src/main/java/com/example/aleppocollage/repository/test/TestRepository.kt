package com.example.aleppocollage.repository.test

import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.test.domain.DeservedGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.ResultSet

class TestRepository {

    fun getDeservedGroups(studyYear: String, teacherID: Int): Flow<DeservedGroup?> {

        return flow {
            try {
                var isSuccess = false
                val connectDB = ConnectDB()
                val connection: Connection? = connectDB.getConncetion()
                if (connection == null) {
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
                        isSuccess = true
                        id = rs.getInt(1)
                        studyYear = rs.getString(2)
                        studyLevel = rs.getString(3)
                        groupName = rs.getString(4)
                        subject = ""
                        subjectID = 0
                        emit(
                            DeservedGroup(
                                id,
                                studyYear,
                                studyLevel,
                                groupName,
                                subject,
                                subjectID
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
}