package com.example.aleppocollage.repository.notification

import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.notification.domain.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.ResultSet

class NotificationRepository {

    fun getNotifications(
        flag: Int,
        studentID: Int,
        groupID: Int,
        teacherID: Int
    ): Flow<Notification?> {

        return flow {
            try {
                var isSuccess = false
                val connectDB = ConnectDB()
                val connection: Connection? = connectDB.getConncetion()
                if (connection == null) {
                } else {
                    val query = "Call AndroidNotificationsSelect(?,?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setInt("@Flag", flag)
                    stmt.setInt("@StudentID", studentID)
                    stmt.setInt("@TeacherID", teacherID)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet
                    var id: Int
                    var title: String
                    var content: String
                    var type: String
                    var info: String
                    var date: String?
                    var studentID: Int
                    var teacherID: Int
                    var groupID: Int
                    while (rs.next()) {
                        isSuccess = true
                        id = rs.getInt(1);
                        title = rs.getString(2);
                        content = rs.getString(3);
                        type = rs.getString(4);
                        info = rs.getString(5);
                        date = rs.getString(6);
                        studentID = rs.getInt(7);
                        teacherID = rs.getInt(8);
                        groupID = rs.getInt(9);
                        emit(
                            Notification(
                                id,
                                title,
                                content,
                                type,
                                info,
                                date,
                                studentID,
                                teacherID,
                                groupID
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