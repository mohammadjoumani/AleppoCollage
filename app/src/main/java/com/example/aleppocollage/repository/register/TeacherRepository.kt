package com.example.aleppocollage.repository.register

import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.user.domain.Teacher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.ResultSet


class TeacherRepository {

    fun getAllTeacher(): Flow<Teacher> {

        return flow {
            try {
                val connectDB = ConnectDB()
                val connection: Connection? = connectDB.getConncetion()
                if (connection == null) {
                } else {
                    val query = "Call AndroidTeachersSelect"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.execute()
                    val rs: ResultSet = stmt.getResultSet()
                    var id: Int
                    var name: String
                    var password: String

                    while (rs.next()) {
                        id = rs.getInt(1)
                        name = rs.getString(2)
                        password = rs.getString(3)
                        emit(
                            Teacher(
                                id,
                                name,
                                password
                            )
                        )
                    }
                    connection.close()
                }
            } catch (ex: Exception) {
            }
        }.flowOn(Dispatchers.IO)
    }

}
