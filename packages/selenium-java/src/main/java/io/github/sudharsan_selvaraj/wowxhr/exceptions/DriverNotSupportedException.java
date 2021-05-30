package io.github.sudharsan_selvaraj.wowxhr.exceptions;

public class DriverNotSupportedException extends Exception {

    public DriverNotSupportedException(String driverClassName) {
        super("Drivers doesn't support javascript execution. Provided driver : " + driverClassName);
    }

}
