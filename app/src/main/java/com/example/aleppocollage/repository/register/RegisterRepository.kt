package com.example.aleppocollage.repository.register

import android.util.Log
import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.user.domain.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.ResultSet

@Suppress("NAME_SHADOWING")
class RegisterRepository {

    fun getOneStudent(name: String, password: String): Flow<Student?> {

        return flow {
            try {
                var isSuccess = false
                val connectDB = ConnectDB()
                val connection: Connection? = connectDB.getConncetion()
                if (connection == null) {
                } else {
                    val query = "Call AndroidOneStudentSelect(?,?)"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.setString("@Name", name)
                    stmt.setString("@Password", password)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet
                    var id: Int
                    var name: String
                    var password: String
                    var groupID: Int
                    var groupLevel: String
                    var groupName: String
                    var groupGender: String
                    while (rs.next()) {
                        isSuccess = true
                        id = rs.getInt(1)
                        name = rs.getString(2)
                        password = rs.getString(3)
                        groupID = rs.getInt(4)
                        groupLevel = rs.getString(5)
                        groupName = rs.getString(6)
                        groupGender = rs.getString(7)
                        emit(
                            Student(
                                id,
                                name,
                                password,
                                groupID,
                                groupLevel,
                                groupName,
                                groupGender
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