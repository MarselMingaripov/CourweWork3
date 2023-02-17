package ru.min.coursework3.service;

import java.io.File;
import java.nio.file.Path;

public interface FilesService {
    boolean saveSocksToFile(String json);

    boolean saveSocksToInputFile(String json);

    boolean saveSocksToOutputFile(String json);

    String readSocksFromFile();

    String readSocksFromInputFile();

    String readSocksFromOutputFile();

    File getSocksDataFile();

    File getInputDataFile();

    File getOutputDataFile();

    Path returnInputOutputPath();

    Path returnInputPath();

    Path returnOutputPath();

    boolean cleanSocksDataFile();

    boolean cleanInputFile();

    boolean cleanOutputFile();

    boolean cleanInputOutputFile();
}
