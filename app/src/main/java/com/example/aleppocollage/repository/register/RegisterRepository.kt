package com.example.aleppocollage.repository.register

import com.example.aleppocollage.connecter.ConnectDB
import com.example.aleppocollage.model.user.domain.Student
import com.example.aleppocollage.model.user.domain.Teacher
import com.example.aleppocollage.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.ResultSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepository @Inject constructor(
    private val connectDB: ConnectDB
) {

    suspend fun getOneStudent(name: String, password: String): Flow<DataState<Student>> = flow {
        emit(DataState.Loading)
        try {
            var response = false
            val connection: Connection? = connectDB.getConnection()
            if (connection == null) {
            } else {
                val query = "Call AndroidOneStudentSelect(?,?)"
                val stmt: CallableStatement = connection.prepareCall(query)
                stmt.setString("@Name", name)
                stmt.setString("@Password", password)
                stmt.execute()
                val rs: ResultSet = stmt.resultSet
                var id = 0
                var name = ""
                var password = ""
                var groupID = 0
                var groupLevel = ""
                var groupName = ""
                var groupGender = ""

                while (rs.next()) {
                    response = true
                    id = rs.getInt(1)
                    name = rs.getString(2)
                    password = rs.getString(3)
                    groupID = rs.getInt(4)
                    groupLevel = rs.getString(5)
                    groupName = rs.getString(6)
                    groupGender = rs.getString(7)
                }

                if (response) {
                    emit(DataState.Success(Student(id, name, password, groupID, groupLevel, groupName, groupGender)))
                } else if (!response) {
                    emit(DataState.Failure(""))
                }
                connection.close()
            }
        } catch (exception: Exception) {
            emit(DataState.ExceptionState(exception))
        }

    }.flowOn(Dispatchers.IO)

    suspend fun getAllTeachers(): Flow<DataState<List<Teacher>>> = flow {
            val teachers: ArrayList<Teacher> = ArrayList()
            emit(DataState.Loading)
            try {
                var response = false
                val connection: Connection? = connectDB.getConnection()
                if (connection == null) {
                } else {
                    val query = "Call AndroidTeachersSelect"
                    val stmt: CallableStatement = connection.prepareCall(query)
                    stmt.execute()
                    val rs: ResultSet = stmt.resultSet
                    var id = 0
                    var name = ""
                    var password = ""

                    while (rs.next()) {
                        response = true
                        id = rs.getInt(1)
                        name = rs.getString(2)
                        password = rs.getString(3)
                        teachers.add(Teacher(id, name, password))
                    }

                    if (response) {
                        emit(DataState.Success(teachers))
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