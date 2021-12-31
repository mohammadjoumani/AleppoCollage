package com.example.aleppocollage.repository.notification

import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.notification.domain.Notification
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
class NotificationRepository @Inject constructor(
    private val connectDB: ConnectDB,
) {

    suspend fun getNotifications(flag: Int, studentID: Int, groupID: Int, teacherID: Int): Flow<DataState<List<Notification>>> = flow {
        val notifications: ArrayList<Notification> = ArrayList()
        emit(DataState.Loading)
        try {
            var response = false
            val connection: Connection? = connectDB.getConnection()
            if (connection == null) {
                emit(DataState.Connection)
            } else  {
                val query = "Call AndroidNotificationsSelect(?,?,?)"
                val stmt: CallableStatement = connection.prepareCall(query)
                stmt.setInt("@Flag", flag)
                stmt.setInt("@StudentID", studentID)
                stmt.setInt("@TeacherID", teacherID)
                stmt.execute()
                val rs: ResultSet = stmt.resultSet
                var id = 0
                var title = ""
                var content = ""
                var type = ""
                var info = ""
                var date = ""
                var studentID = 0
                var teacherID = 0
                var groupID = 0

                while (rs.next()) {
                    response = true
                    id = rs.getInt(1)
                    title = rs.getString(2)
                    content = rs.getString(3)
                    type = rs.getString(4)
                    info = rs.getString(5)
                    date = rs.getString(6)
                    studentID = rs.getInt(7)
                    teacherID = rs.getInt(8)
                    groupID = rs.getInt(9)

                    notifications.add(Notification(id, title, content, type, info, date, studentID, teacherID, groupID))
                }
                if (response) {

                    emit(DataState.Success(notifications))

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