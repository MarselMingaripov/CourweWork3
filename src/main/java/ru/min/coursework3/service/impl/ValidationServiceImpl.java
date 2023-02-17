package ru.min.coursework3.service.impl;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import ru.min.coursework3.entity.Sock;
import ru.min.coursework3.service.ValidationService;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public boolean validateSock(Sock sock){
        return NumberUtils.compare(sock.getCottonPercentage(), -1) > 0
                && NumberUtils.compare(sock.getCottonPercentage(), 101) < 0
                && NumberUtils.compare(sock.getSize(), 29) > 0
                && NumberUtils.compare(sock.getSize(), 49) < 0
                && NumberUtils.compare(sock.getStockBalance(), 0) > 0;
    }
}
