package ru.min.coursework3.service;

import ru.min.coursework3.entity.Sock;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public interface SockService {

    /**
     * добавить носок на склад
     * @param sock - носок
     * @return добавляемый носок
     */
    Sock addSockToStock(Sock sock);

    /**
     * продать носок со склада
     * @param sock - носок
     * @return продаваемый носок
     */
    Sock sellSockFromStock(Sock sock);

    /**
     * получить количество носков, используя фильтры
     * @param color - цвет
     * @param size - размер
     * @param cottonMin - минимальное содержание хлопка
     * @param cottonMax - максимальное содержание хлопка
     * @return количество носков
     */
    int getTotalSockByParams(String color, int size, int cottonMin, int cottonMax);

    /**
     * удаление бракованных носков
     * @param sock - носок
     * @return - успешно или нет
     */
    boolean deleteSockFromStock(Sock sock);

    /**
     * получить список всех носков
     * @return
     */
    Set<Sock> showAllSocks();

    /**
     * создает файл
     * @return
     * @throws IOException
     */
    Path createInputOutputFile() throws IOException;

    /**
     * обновление данных на складе в зависимости от того, что хранится в файле прихода
     */
    void updateStockByInputData();

    /**
     * обновление данных на складе в зависимости от того, что хранится в файле продажи
     */
    void updateStockByOutputData();
}
