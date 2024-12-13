# Array Pair Sum Project

## Опис завдання

Ця програма призначена для знаходження попарної суми всіх елементів одновимірного масиву великих розмірів. Сума обчислюється за формулою:

```
(a1 + a2) + (a2 + a3) + ... + (an-1 + an)
```

### Основна мета
- Ознайомлення з підходами балансування задач.
- Розробка рішень за допомогою:
  - **Fork/Join Framework** (Work Stealing).
  - **ExecutorService** (Work Dealing).

### Очікуваний результат
- Коректний код з використанням сучасних Java-конструкцій (ООП, Thread Pool).
- Програмно заміряний час виконання обох підходів.
- Порівняння результатів і висновок.

## Інструкція користувача

1. **Запуск програми**
   - Виконайте файл `ArrayPairSum.java`.
   - Програма запитає:
     - Кількість елементів у масиві.
     - Мінімальне та максимальне значення елементів масиву.

2. **Приклад вводу**:
   ```
   Введіть кількість елементів у масиві:
   100000
   Введіть мінімальне значення:
   1
   Введіть максимальне значення:
   100
   ```

3. **Результат**:
   - На екран буде виведено:
     - Згенерований масив (для невеликих розмірів).
     - Результат виконання обчислень для обох підходів.
     - Час виконання кожного підходу.

## Реалізація

### 1. Підхід Work Stealing
- Використовує `ForkJoinPool` для автоматичного розподілу підзадач між потоками.
- Масив розбивається на частини, і задачі виконуються паралельно.
- Реалізовано у класі `PairSumTask`.

### 2. Підхід Work Dealing
- Використовує `ExecutorService` для ручного розподілу задач між потоками.
- Масив ділиться на частини однакового розміру, які виконуються у пулі потоків.
- Реалізовано в методі `calculateWithWorkDealing`.


### Висновки
- Work Stealing підходить для нерівномірного розподілу задач, оскільки автоматично перенаправляє потоки до невиконаних задач.
- Work Dealing забезпечує хороший контроль над розподілом задач, але вимагає ручного налаштування.
- В реальних умовах результати можуть залежати від розміру задач і конфігурації апаратного забезпечення.


## Використані технології
- **Java 17**
- **Fork/Join Framework**
- **ExecutorService**
- **Thread Pool**



