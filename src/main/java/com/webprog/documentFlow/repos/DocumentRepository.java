package com.webprog.documentFlow.repos;

import com.webprog.documentFlow.models.Document;
import com.webprog.documentFlow.models.DocumentType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс репозитория для работы с документами в базе данных.
 */
public interface DocumentRepository extends CrudRepository<Document, Long> {

    /**
     * Поиск документов по всем полям.
     *
     * @param number Номер документа
     * @param title Заголовок документа
     * @param date Дата документа
     * @param type Тип документа
     * @return Список документов, соответствующих критериям поиска
     */
    @Query("SELECT d FROM Document d WHERE d.number = :number AND d.title = :title AND d.date = :date AND d.type = :type")
    List<Document> finByAll(String number, String title, LocalDate date, DocumentType type);

    /**
     * Поиск документов по их номеру (частичное совпадение).
     *
     * @param number Номер документа или его часть
     * @return Список документов, чей номер содержит указанную строку
     */
    List<Document> findByNumberContaining(String number);

    /**
     * Поиск документов по их типу.
     *
     * @param documentType Тип документа
     * @return Список документов указанного типа
     */
    List<Document> findByType(DocumentType documentType);
}
