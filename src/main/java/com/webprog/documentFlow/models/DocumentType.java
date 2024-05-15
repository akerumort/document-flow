package com.webprog.documentFlow.models;

/**
 * Перечисление, представляющее типы документов в системе.
 */
public enum DocumentType {
    PASSPORT, // Паспорт
    DRIVER_LICENSE, // Водительское удостоверение
    ID_CARD, // Удостоверение личности
    RESIDENCE_PERMIT, // Вид на жительство
    BIRTH_CERTIFICATE, // Свидетельство о рождении
    MARRIAGE_CERTIFICATE, // Свидетельство о браке
    DEATH_CERTIFICATE, // Свидетельство о смерти
    DIPLOMA, // Диплом
    EMPLOYMENT_CONTRACT, // Трудовой договор
    BANK_STATEMENT, // Выписка из банка
    UTILITY_BILL, // Квитанция за коммунальные услуги
    INSURANCE_POLICY, // Страховой полис
    MEDICAL_RECORD, // Медицинская карта
    VISA, // Виза
    RESUME, // Резюме
    LETTER_OF_RECOMMENDATION, // Рекомендательное письмо
    BUSINESS_LICENSE, // Лицензия на ведение бизнеса
    COPYRIGHT_CERTIFICATE, // Свидетельство о регистрации авторского права
    OTHER // Другой тип документа
}
