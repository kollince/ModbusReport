## Приложение ModBus reports

### Описание

Десктопное приложение для чтения данные с ПЛК по modbus протоколу, с последующем сохранением полученного результата в файл формата *.docx.
Для работы приложения неообходим ip-адрес ПЛК с поддержкой modbus и файл шаблон *.docx. В файле необходимо указать заменяемые символы (пример: {12345}) 
и в приложении добавить алиас для каждого адреса (рис.1). 
![screen.png](src/main/resources/images/screen.png)

### Используемые инструменты:
1. [x] Java FX;
2. [x] Maven:
   * JLibModbus;
   * Jackson core;
   * Apache POI.




