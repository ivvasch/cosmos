package ru.cosmotask.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "t_patient")
public class Patient {
    @Id
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthDay;
    private String phone;
    private String email;


    // todo вынести в отдельную сущность тел и почту, процедуры тоже убрать

}

/**
 * 1. Выбираем клиента из базы findById(id) ++
 * 2. Открывается страница с клиентом и списком процедур, которые были сделаны findAllByPatientId(patientId) ++.
 *      У процедур, имеющих статус "необходимость следующей процедуры" есть чекбокс "завершить", после нажатия на который
 *      должна открыться новая процедура.
 * 3. У нас есть возможность:
 *      3.1. добавить новую завершенную процедуру (Post)
 *          3.1.1. отмечаем стоимость, дата проведения процедуры ставится автоматом (но ее можно сменить), если необходимо ставим галочку
 *            "необходимость следующей процедуры" и выбираем дату следующей процедуры.
 *            3.1.2. сохраняем процедуру (Post) addCompletedProcedure(CompletedProcedureDTO) ++
 *            3.1.3. одновременно создается Task (POST)  addTask(TaskDTO) ++  (добавить синхронизацию в гугл календарь) --
 *      3.2. выбрать процедуру из уже сделанных (getCompletedProcedureById(id)) ++
 *          3.2.1. ранее была выбрана дата следующей процедуры, ставим галочку "готово" (Patch) ++
 *          3.2.2. создается новая такая же процедура (форма)
 *          3.2.3. отмечаем стоимость, дата проведения процедуры ставится автоматом (но ее можно сменить), если необходимо ставим галочку
 *          "необходимость следующей процедуры" и выбираем дату следующей процедуры.
 *          3.2.4. сохраняем процедуру (Post) ++
 *          3.2.5. одновременно создается Task ++
 *      3.3. добавить/изменить телефон ++
 *      3.4. добавить/изменить ФИО ++
 *      3.5. добавить/изменить дату рождения ++
 *      3.6. добавить/изменить email ++
 *      3.7. показать доход (Income) от всех процедур за все время
 *      3.8. показать количество посещений
  */

