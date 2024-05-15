package com.webprog.documentFlow.models;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.Date;

/**
 * Класс, представляющий документ в системе.
 */
@Entity
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number; // Номер документа

    private String title; // Заголовок документа

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate date; // Дата документа

    @Enumerated(EnumType.STRING)
    private DocumentType type; // Тип документа

    private String fileName; // Имя файла документа

    @Lob
    private byte[] fileData; // Данные файла документа

    /**
     * Конструктор по умолчанию.
     */
    public Document() {
    }

    /**
     * Конструктор для создания документа с указанием всех полей.
     *
     * @param number номер документа
     * @param title  заголовок документа
     * @param date   дата документа
     * @param type   тип документа
     */
    public Document(String number, String title, LocalDate date, DocumentType type) {
        this.number = number;
        this.title = title;
        this.date = date;
        this.type = type;
    }
}
