package ru.min.coursework3.service;

import ru.min.coursework3.entity.Sock;

public interface ValidationService {
    /**
     * валидация носка
     * @param sock
     * @return
     */
    boolean validateSock(Sock sock);
}
