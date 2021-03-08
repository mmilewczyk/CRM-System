package com.agiklo.oracledatabase.security.email;

public interface EmailSender {

    void send(String to, String email);
}
