package ru.min.coursework3.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.min.coursework3.service.FilesService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FilesServiceImpl implements FilesService {

    @Value("${path.to.file}")
    private String pathToFile;

    @Value("${name.of.file}")
    private String nameOfFile;

    @Value("${name.of.input.output.file}")
    private String inputOutputFile;

    @Value("${name.of.input.file}")
    private String nameOfInputFile;

    @Value("${name.of.output.file}")
    private String nameOfOutputFile;

    @Override
    public boolean saveSocksToFile(String json) {
        try {
            cleanSocksDataFile();
            Files.writeString(Path.of(pathToFile, nameOfFile), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveSocksToInputFile(String json) {
        try {
            cleanInputFile();
            Files.writeString(Path.of(pathToFile, nameOfInputFile), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveSocksToOutputFile(String json) {
        try {
            cleanOutputFile();
            Files.writeString(Path.of(pathToFile, nameOfOutputFile), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String readSocksFromFile() {
        try {
            return Files.readString(Path.of(pathToFile, nameOfFile));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось считать из файла");
        }
    }

    @Override
    public String readSocksFromInputFile() {
        try {
            return Files.readString(Path.of(pathToFile, nameOfInputFile));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось считать из файла");
        }
    }

    @Override
    public String readSocksFromOutputFile() {
        try {
            return Files.readString(Path.of(pathToFile, nameOfOutputFile));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось считать из файла");
        }
    }

    @Override
    public File getSocksDataFile() {
        return new File(pathToFile + "/" + nameOfFile);
    }

    @Override
    public File getInputDataFile() {
        return new File(pathToFile + "/" + nameOfInputFile);
    }

    @Override
    public File getOutputDataFile() {
        return new File(pathToFile + "/" + nameOfOutputFile);
    }

    @Override
    public Path returnInputOutputPath() {
        Path path = Path.of(pathToFile, inputOutputFile);
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    @Override
    public Path returnInputPath() {
        Path path = Path.of(pathToFile, nameOfInputFile);
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    @Override
    public Path returnOutputPath() {
        Path path = Path.of(pathToFile, nameOfOutputFile);
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    @Override
    public boolean cleanSocksDataFile() {
        try {
            Path path = Path.of(pathToFile, nameOfFile);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cleanInputFile() {
        try {
            Path path = Path.of(pathToFile, nameOfInputFile);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cleanOutputFile() {
        try {
            Path path = Path.of(pathToFile, nameOfOutputFile);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cleanInputOutputFile() {
        try {
            Path path = Path.of(pathToFile, inputOutputFile);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
