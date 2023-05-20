package exeptions;

import Store.Register;

public class RegisterUnavailableException extends Exception {
    Register register;

    public RegisterUnavailableException(String message,  Register register) {
        super(message);
        this.register = register;
    }

    public Register getRegister() {
        return register;
    }
}
