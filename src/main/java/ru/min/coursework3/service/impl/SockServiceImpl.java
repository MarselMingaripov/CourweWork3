package ru.min.coursework3.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Service;
import ru.min.coursework3.entity.Sock;
import ru.min.coursework3.exception.ShortageException;
import ru.min.coursework3.exception.ValidationException;
import ru.min.coursework3.service.FilesService;
import ru.min.coursework3.service.SockService;
import ru.min.coursework3.service.ValidationService;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SockServiceImpl implements SockService {

    private final String formatter = "dd:MM:yyyy HH:mm:ss";

    private Set<Sock> socks = new HashSet<>();
    private Map<String, Sock> inputOutputSock = new HashMap<>();
    private List<Sock> input = new ArrayList<>();
    private List<Sock> output = new ArrayList<>();

    private final ValidationService validationService;
    private final FilesService filesService;

    public SockServiceImpl(ValidationService validationService, FilesService filesService) {
        this.validationService = validationService;
        this.filesService = filesService;
    }

    @PostConstruct
    public void init() {
        try {
            readFromFile();
            readFromInputFile();
            readFromOutputFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Sock addSockToStock(Sock sock) {
        if (!validationService.validateSock(sock)) {
            throw new ValidationException("Некорректные данные носка");
        } else {
            input.add(sock);
            saveToInputFile();
            inputOutputSock.put("поступление " + LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatter)), sock);
        }

        if (socks.isEmpty()) {
            socks.add(sock);
            saveToFile();
        } else {
            if (socks.contains(sock)) {
                for (Sock value : socks) {
                    if (value.equals(sock)) {
                        Sock tempSock = value.clone();
                        tempSock.setStockBalance(value.getStockBalance() + sock.getStockBalance());
                        socks.remove(value);
                        socks.add(tempSock);
                        saveToFile();
                    }
                }
            } else {
                socks.add(sock);
                saveToFile();
            }
        }
        return sock;
    }

    @Override
    public Sock sellSockFromStock(Sock sock) {
        if (!validationService.validateSock(sock)) {
            throw new ValidationException("Некорректные данные носка");
        } else {
            inputOutputSock.put("продажа " + LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatter)), sock);
            output.add(sock);
            saveToOutputFile();
        }
        if (socks.contains(sock)) {
            for (Sock value : socks) {
                if (value.equals(sock)) {
                    if (!(value.getStockBalance() < sock.getStockBalance())) {
                        Sock tempSock = value.clone();
                        tempSock.setStockBalance(value.getStockBalance() - sock.getStockBalance());
                        socks.remove(value);
                        socks.add(tempSock);
                        saveToFile();
                        return tempSock;
                    } else {
                        throw new ShortageException("Нехватает на складе!");
                    }
                }
            }
        } else {
            throw new ShortageException("Отсутствует на складе!");
        }
        return null;
    }

    @Override
    public int getTotalSockByParams(String color, int size, int cottonMin, int cottonMax) {
        List<Sock> sockList = new ArrayList<>();
        for (Sock sock : socks) {
            if (sock.getColor().getDescription().equals(color)
                    && sock.getSize() == size
                    && sock.getCottonPercentage() <= cottonMax
                    && sock.getCottonPercentage() >= cottonMin) {
                sockList.add(sock);
            }
        }
        int totalCount = 0;
        if (!sockList.isEmpty()) {
            for (Sock sock : sockList) {
                totalCount += sock.getStockBalance();
            }
        } else {
            throw new ShortageException("Носки по заданным параметрам не найдены!");
        }
        return totalCount;
    }

    @Override
    public boolean deleteSockFromStock(Sock sock) {
        if (!validationService.validateSock(sock)) {
            throw new ValidationException("Некорректные параметры носка");
        }
        if (socks.contains(sock)) {
            for (Sock value : socks) {
                if (value.equals(sock)) {
                    if (!(value.getStockBalance() < sock.getStockBalance())) {
                        value.setStockBalance(value.getStockBalance() - sock.getStockBalance());
                        saveToFile();
                        return true;
                    } else {
                        throw new ShortageException("Пытаетесь списать больше, чем есть на складе!");
                    }
                }
            }
        } else {
            throw new ShortageException("Носки по заданным параметрам отсутствуют на складе!");
        }
        return false;
    }

    @Override
    public Set<Sock> showAllSocks() {
        return socks;
    }

    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socks);
            filesService.saveSocksToFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void saveToInputFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(input);
            filesService.saveSocksToInputFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void saveToOutputFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(output);
            filesService.saveSocksToOutputFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile() {
        try {
            String json = filesService.readSocksFromFile();
            if (!json.isEmpty()){
                socks = new ObjectMapper().readValue(json, new TypeReference<>() {
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void readFromInputFile() {
        try {
            String json = filesService.readSocksFromInputFile();
            if (!json.isEmpty()){
                input = new ObjectMapper().readValue(json, new TypeReference<>() {
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void readFromOutputFile() {
        try {
            String json = filesService.readSocksFromOutputFile();
            if (!json.isEmpty()){
                output = new ObjectMapper().readValue(json, new TypeReference<>() {
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Path createInputOutputFile() throws IOException {
        Path path = filesService.returnInputOutputPath();
        try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            for (Map.Entry<String, Sock> stringSockEntry : inputOutputSock.entrySet()) {
                writer.append(stringSockEntry.getKey() + "\n" +
                        "Количество: " + stringSockEntry.getValue().getStockBalance() + "\n" +
                        "Размер: " + stringSockEntry.getValue().getSize() + "\n" +
                        "Содержание хлопка: " + stringSockEntry.getValue().getCottonPercentage() + "%" + "\n" +
                        "Цвет: " + stringSockEntry.getValue().getColor().getDescription() + "\n\n");
                writer.append("\n");
            }
        }
        return path;
    }

    @Override
    public void updateStockByInputData() {
        input.clear();
        readFromInputFile();
        List<Sock> mockList = List.copyOf(input);
        input.clear();
        for (Sock sock : mockList) {
            addSockToStock(sock);
        }
    }

    @Override
    public void updateStockByOutputData() {
        output.clear();
        readFromOutputFile();
        List<Sock> mockList = List.copyOf(output);
        output.clear();
        for (Sock sock : mockList) {
            sellSockFromStock(sock);
        }
    }
}
