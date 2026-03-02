package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    // + Добавлен скип для читаемости
    // + skip пропускает по условию, а remove удаляет
    return persons.stream().skip(1).map(Person::firstName).collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    // + Избыточный stream
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    // + Добавлен stream для читаемости (сверху баг, пропущено middle name)
    return Stream.of(person.firstName(), person.middleName(), person.secondName())
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    // + Добавлен stream для читаемости
    return persons.stream()
            .collect(Collectors.toMap(
                    Person::id,
                    this::convertPersonToString,
                    (existing, replacement) -> existing
            ));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    // + Добавлен stream для читаемости
    return persons1.stream().anyMatch(persons2::contains);
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    // + Использован встроенный метод count() у stream для читаемости
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    // + Хеш сет хранит элементы в бакетах на основе хеш-кода.
    // + У integer хеш-код равен самому числу, поэтому числа 1..10000 распределяются по бакетам равномерно.
    // + При обходе хеш сет проходит бакеты по порядку так как его ключи будут вычисляться так
    // + -> (хешкода чисел % количество элементов в хешсете), поэтому порядок случайно совпадает с исходным списком.
    // + Из за этого сет сортируется и assert == true.
    // + Но если поменять размер сета то все поломается
    assert snapshot.toString().equals(set.toString());
  }
}
