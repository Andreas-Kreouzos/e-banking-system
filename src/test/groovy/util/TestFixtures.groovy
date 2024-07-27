package util

import com.ebanking.system.dto.UserRequest
import com.ebanking.system.dto.UserResponse

class TestFixtures {

    static createUserRequest(String firstName = 'Andreas',
                             String lastName = 'Kreouzos',
                             String email = 'andreas.kreouzos@hotmail.com',
                             String password = 'password',
                             String afm = '123456789',
                             String birthDate = '1985-11-11',
                             String mobileNumber = '6931234567',
                             String cardNumber = '1234123412341234') {
        new UserRequest(
                firstName,
                lastName,
                email,
                password,
                afm,
                birthDate,
                mobileNumber,
                cardNumber)
    }

    static createUserResponse() {
        new UserResponse(1, 'andrekreou', 'Andreas', 'Kreouzos', 'andreas.kreouzos@hotmail.com')
    }
}
