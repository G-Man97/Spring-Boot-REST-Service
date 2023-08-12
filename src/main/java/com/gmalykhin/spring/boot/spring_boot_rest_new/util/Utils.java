package com.gmalykhin.spring.boot.spring_boot_rest_new.util;

import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.Department;
import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.BaseEntity;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.IncorrectFieldData;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }

    // Метод превращает входную строку в строку со всеми прописными буквами,
    // а первую букву делает заглавной
    public static String initCap (String str) {
        return str.substring(0,1).toUpperCase()
                + str.substring(1).toLowerCase();
    }

    // Проверка возраста нового устраиваемого работника
    // Возраст должен быть больше 18 лет и меньше 60 лет
    public static void checkBirthday (LocalDate birthday) throws IncorrectFieldData {
        final var LOW_BOUND_DATE = LocalDate.now().minusYears(60);
        final var UP_BOUND_DATE = LocalDate.now().minusYears(18).plusDays(1);

        if(!(birthday.isBefore(UP_BOUND_DATE) && birthday.isAfter(LOW_BOUND_DATE))) {
            throw new IncorrectFieldData("The employee must be over 18 years old and under 60 years old");
        }
    }

    // Получение строки дефолтных сообщений об ошибках валидации полей
    public static String errorsToString(List<FieldError> fieldErrors) {
        var errorMessage = new StringBuilder();
        for (var fE: fieldErrors) {
            errorMessage.append(fE.getDefaultMessage()).append("\n");
        }
        return errorMessage.toString();
    }

    // Проверка попадания зарплаты работника в диапазон minSalary и maxSalary департамента, в котором он работает
    public static void checkEmployeesSalary (Double salary, Department department) throws IncorrectFieldData {
        if (salary < department.getMinSalary()) {
            throw new IncorrectFieldData("The salary must be > value of the minSalary field of employee's department");
        } else if (salary > department.getMaxSalary()) {
            throw new IncorrectFieldData("The salary must be < value of the maxSalary field of employee's department");
        }
    }

    // Проверка корректности ввода minSalary и maxSalary, а так же разность между ними должна быть >= 500 и <= 7000
    public static void checkDepartmentMinMaxSalary (Double minSalary, Double maxSalary) throws IncorrectFieldData {
        if ((minSalary >= maxSalary) || (maxSalary - minSalary) < 500 || (maxSalary - minSalary) > 7000) {
            throw new IncorrectFieldData("The value of the minSalary field must be > then the value of the " +
                    "maxSalary field. The range between these two values must be between 500 and 7000");
        }
    }

    // Проверка если при создании нового entity (PostMapping) какие-либо из полей были пропущены,
    // т.е. они null, тогда приводится список пропущенных полей, которые должны быть заполнены
    public static <T extends BaseEntity> void checkEntityFieldsIfNull(T entity) {

        var entityFields = entity.getClass().getDeclaredFields();
        var listOfNullFields = new StringBuilder();

        for (Field field : entityFields) {

            field.setAccessible(true);

            if (!field.getType().isPrimitive()) {
                try {
                    if (field.get(entity) == null
                            && !field.getName().equals("employee")) {

                        listOfNullFields.append(field.getName()).append(", ");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

        if (!listOfNullFields.isEmpty()) {
            throw new IncorrectFieldData("You missed the required field(s): "
                    + listOfNullFields.substring(0, listOfNullFields.length()-2));
        }
    }

    // Проверка если при изменении entity (PutMapping) какие-либо из полей были пропущены,
    // т.е. они null, тогда они заполняются прежним значением
    public static <T extends BaseEntity> void checkEntityFieldsIfNullThenFill(T entity, T repoEntity) {

        var entityFields = entity.getClass().getDeclaredFields();
        var repoEntityFields = repoEntity.getClass().getDeclaredFields();

        for (int i = 0; i < entityFields.length; i++) {
            entityFields[i].setAccessible(true);
            repoEntityFields[i].setAccessible(true);

            if (!entityFields[i].getType().isPrimitive()) {
                try {
                    if (entityFields[i].get(entity) == null) {
                        entityFields[i].set(entity, repoEntityFields[i].get(repoEntity));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }
}