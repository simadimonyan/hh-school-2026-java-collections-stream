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
//    + Проверка не нужна, список и так будет пустым
//    if (persons.size() == 0) {
//      return Collections.emptyList();
//    }
//    + Добавлен скип для читаемости
//    persons.remove(0);
    return persons.stream().skip(1).map(Person::firstName).collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    // + Избыточный stream
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
//    String result = "";
//    if (person.secondName() != null) {
//      result += person.secondName();
//    }
//
//    if (person.firstName() != null) {
//      result += " " + person.firstName();
//    }
//
//    if (person.secondName() != null) {
//      result += " " + person.secondName();
//    }

//  + Добавлен stream для читаемости (сверху баг, пропущено middle name)
    return Stream.of(person.firstName(), person.middleName(), person.secondName())
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
//    Map<Integer, String> map = new HashMap<>(1);
//    for (Person person : persons) {
//      if (!map.containsKey(person.id())) {
//        map.put(person.id(), convertPersonToString(person));
//      }
//    }

//  + Добавлен stream для читаемости
    return persons.stream().collect(Collectors.toMap(Person::id, Person::firstName));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
//    boolean has = false;
//    for (Person person1 : persons1) {
//      for (Person person2 : persons2) {
//        if (person1.equals(person2)) {
//          has = true;
//        }
//      }
//    }

//  + Добавлен stream для читаемости
    return persons1.stream().anyMatch(persons2::contains);
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
//    count = 0;
//    numbers.filter(num -> num % 2 == 0).forEach(num -> count++);

//  + Использован встроенный метод count() у stream для читаемости
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    // + совпадение (хешкода чисел % количество элементов в хешсете) с бакетами хешсета по порядку :D
    assert snapshot.toString().equals(set.toString());
  }
}
