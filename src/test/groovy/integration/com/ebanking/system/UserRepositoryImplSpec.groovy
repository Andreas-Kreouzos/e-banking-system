package integration.com.ebanking.system

import com.ebanking.system.entity.User
import com.ebanking.system.exception.UserNotFoundException
import com.ebanking.system.repository.UserRepositoryImpl
import org.springframework.dao.DuplicateKeyException
import spock.lang.Subject
import util.TestContainersSpec

class UserRepositoryImplSpec extends TestContainersSpec {

    @Subject
    UserRepositoryImpl repository

    def username = 'testUser'
    def password = 'myPassword'
    def firstName = 'myName'
    def lastName = 'myLastName'
    def email = 'my.email@hotmail.com'

    def setup() {
        repository = new UserRepositoryImpl(dataSource)
    }

    def 'Successfully insert a new user in the database'() {
        given: 'a user'
        def user = new User(username, password, firstName, lastName, email)

        when: 'calling the insert method'
        def result = repository.insert(user)

        then: 'the user is saved'
        def dbUser = sql.firstRow("SELECT * FROM E_BANKING_SYSTEM.USERS WHERE USERNAME = ?", [user.username])
        dbUser.get('ID') == result
        dbUser.get('USERNAME') == user.username
        dbUser.get('PASSWORD') == user.password
        dbUser.get('FIRSTNAME') == user.firstName
        dbUser.get('LASTNAME') == user.lastName
        dbUser.get('EMAIL') == user.email
    }

    def 'Insert users with same username or email in the database fails'() {
        given: 'a first user'
        def firstUser = new User(firstUsername, password, firstName, lastName, firstEmail)

        and: 'a second user'
        def secondUser = new User(secondUsername, password, firstName, lastName, secondEmail)

        when: 'inserting the users'
        repository.insert(firstUser)
        repository.insert(secondUser)

        then: 'an exception is thrown with appropriate messages'
        def ex = thrown(DuplicateKeyException)
        ex.message.contains(errorMessage)

        where: 'duplicate data is being provided for username and email'
        firstUsername | secondUsername | firstEmail                   | secondEmail                   || errorMessage
        'testUser'    | 'testUser'     | 'my.first.email@hotmail.com' | 'my.second.email@hotmail.com' || duplicateUsernameErrorMessage()
        'testUser1'   | 'testUser2'    | 'my.email@hotmail.com'       | 'my.email@hotmail.com'        || duplicateEmailErrorMessage()
    }

    def 'Successfully select a user by his id from the database'() {
        given: 'a user'
        def user = new User(username, password, firstName, lastName, email)

        and: 'insert the user in the database'
        repository.insert(user)

        and: 'manually select the user'
        def dbUser = sql.firstRow("SELECT * FROM E_BANKING_SYSTEM.USERS WHERE USERNAME = ?", [username])

        when: 'trying to select that user'
        def selectedUser = repository.select(dbUser.get('ID'))

        then: 'the user is retrieved'
        selectedUser.id == dbUser.get('ID')
        selectedUser.username == user.username
        selectedUser.password == user.password
        selectedUser.firstName == user.firstName
        selectedUser.lastName == user.lastName
        selectedUser.email == user.email
    }

    def 'Exception thrown when select a user by id from the database'() {
        given: 'a non-existent user id'
        def nonExistentId = 9L

        when: 'calling the select method'
        repository.select(nonExistentId)

        then: 'an exception is thrown'
        def ex = thrown(UserNotFoundException)

        and: 'with an appropriate message'
        ex.message.contains("User not found with ID: ${nonExistentId}")
    }

    def 'Successfully update a user in the database'() {
        given: 'a user'
        def user = new User(username, password, firstName, lastName, email)

        and: 'insert that user in the database'
        repository.insert(user)

        and: 'provide new values for firstname and lastname'
        def updatedFirstname = 'newFirstname'
        def updatedLastname = 'newLastname'

        and: 'manually select the user'
        def dbUser = sql.firstRow("SELECT * FROM E_BANKING_SYSTEM.USERS WHERE USERNAME = ?", [username])

        when: 'trying to update the firstname and lastname'
        def result = repository.update(dbUser.get('ID'), updatedFirstname, updatedLastname)

        then: 'the user has been updated'
        def updatedUser = sql.firstRow("SELECT * FROM E_BANKING_SYSTEM.USERS WHERE USERNAME = ?", [user.username])
        updatedUser.get('ID') == result
        updatedUser.get('USERNAME') == user.username
        updatedUser.get('PASSWORD') == user.password
        updatedUser.get('FIRSTNAME') == updatedFirstname
        updatedUser.get('LASTNAME') == updatedLastname
        updatedUser.get('EMAIL') == user.email
    }

    def duplicateUsernameErrorMessage() {
        "Duplicate entry 'testUser' for key 'USERS.USERNAME'"
    }

    def duplicateEmailErrorMessage() {
        "Duplicate entry 'my.email@hotmail.com' for key 'USERS.EMAIL'"
    }
}
