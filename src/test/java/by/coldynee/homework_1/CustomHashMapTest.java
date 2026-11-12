package by.coldynee.homework_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomHashMapTest {

    private CustomHashMap<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new CustomHashMap<>(16);
    }

    @Nested
    @DisplayName("Базовые операции put() и get()")
    class BasicOperations {

        @Test
        @DisplayName("put() добавляет новый элемент")
        void putAddsNewElement() {
            Integer result = map.put("apple", 42);
            assertNull(result, "put должен вернуть null для нового ключа");
            assertEquals(42, map.get("apple"));
        }

        @Test
        @DisplayName("get() возвращает null для несуществующего ключа")
        void getReturnsNullForMissingKey() {
            assertNull(map.get("nonexistent"));
        }

        @Test
        @DisplayName("put() обновляет существующий ключ и возвращает старое значение")
        void putUpdatesExistingKey() {
            map.put("apple", 42);
            Integer oldValue = map.put("apple", 100);

            assertEquals(42, oldValue, "put должен вернуть старое значение");
            assertEquals(100, map.get("apple"), "значение должно обновиться");
        }

        @Test
        @DisplayName("Множественные put() работают корректно")
        void multiplePuts() {
            map.put("a", 1);
            map.put("b", 2);
            map.put("c", 3);

            assertEquals(1, map.get("a"));
            assertEquals(2, map.get("b"));
            assertEquals(3, map.get("c"));
        }
    }

    @Nested
    @DisplayName("size() и isEmpty()")
    class SizeOperations {

        @Test
        @DisplayName("Пустая карта имеет размер 0")
        void emptyMapHasSizeZero() {
            assertEquals(0, map.size());
            assertTrue(map.isEmpty());
        }

        @Test
        @DisplayName("size() увеличивается при добавлении")
        void sizeIncreasesOnPut() {
            map.put("a", 1);
            assertEquals(1, map.size());
            assertFalse(map.isEmpty());

            map.put("b", 2);
            assertEquals(2, map.size());
        }

        @Test
        @DisplayName("size() не увеличивается при обновлении")
        void sizeDoesNotIncreaseOnUpdate() {
            map.put("a", 1);
            map.put("a", 2);
            assertEquals(1, map.size(), "размер не должен измениться");
        }

        @Test
        @DisplayName("size() уменьшается при удалении")
        void sizeDecreasesOnRemove() {
            map.put("a", 1);
            map.put("b", 2);
            assertEquals(2, map.size());

            map.remove("a");
            assertEquals(1, map.size());
        }
    }

    @Nested
    @DisplayName("remove()")
    class RemoveOperations {

        @Test
        @DisplayName("remove() удаляет существующий ключ и возвращает значение")
        void removeExistingKey() {
            map.put("apple", 42);
            Integer removed = map.remove("apple");

            assertEquals(42, removed);
            assertNull(map.get("apple"));
            assertEquals(0, map.size());
        }

        @Test
        @DisplayName("remove() возвращает null для несуществующего ключа")
        void removeNonExistentKey() {
            Integer removed = map.remove("nonexistent");
            assertNull(removed);
        }

        @Test
        @DisplayName("remove() не влияет на размер при удалении несуществующего ключа")
        void removeDoesNotAffectSizeForMissingKey() {
            map.put("a", 1);
            map.remove("b");
            assertEquals(1, map.size());
        }
    }

    @Nested
    @DisplayName("containsKey() и containsValue()")
    class ContainsOperations {

        @Test
        @DisplayName("containsKey() находит существующий ключ")
        void containsKeyFindsExisting() {
            map.put("apple", 42);
            assertTrue(map.containsKey("apple"));
        }

        @Test
        @DisplayName("containsKey() возвращает false для отсутствующего ключа")
        void containsKeyReturnsFalseForMissing() {
            assertFalse(map.containsKey("nonexistent"));
        }

        @Test
        @DisplayName("containsValue() находит существующее значение")
        void containsValueFindsExisting() {
            map.put("apple", 42);
            assertTrue(map.containsValue(42));
        }

        @Test
        @DisplayName("containsValue() возвращает false для отсутствующего значения")
        void containsValueReturnsFalseForMissing() {
            assertFalse(map.containsValue(999));
        }

        @Test
        @DisplayName("containsValue() находит значение среди нескольких")
        void containsValueFindsAmongMultiple() {
            map.put("a", 1);
            map.put("b", 2);
            map.put("c", 3);

            assertTrue(map.containsValue(2));
            assertFalse(map.containsValue(99));
        }
    }

    @Nested
    @DisplayName("keySet() и values()")
    class CollectionOperations {

        @Test
        @DisplayName("keySet() возвращает все ключи")
        void keySetReturnsAllKeys() {
            map.put("a", 1);
            map.put("b", 2);
            map.put("c", 3);

            Set<String> keys = map.keySet();
            assertEquals(3, keys.size());
            assertTrue(keys.contains("a"));
            assertTrue(keys.contains("b"));
            assertTrue(keys.contains("c"));
        }

        @Test
        @DisplayName("values() возвращает все значения")
        void valuesReturnsAllValues() {
            map.put("a", 1);
            map.put("b", 2);
            map.put("c", 3);

            List<Integer> values = map.values();
            assertEquals(3, values.size());
            assertTrue(values.contains(1));
            assertTrue(values.contains(2));
            assertTrue(values.contains(3));
        }

        @Test
        @DisplayName("keySet() для пустой карты возвращает пустой Set")
        void keySetForEmptyMap() {
            Set<String> keys = map.keySet();
            assertTrue(keys.isEmpty());
        }

        @Test
        @DisplayName("values() для пустой карты возвращает пустой List")
        void valuesForEmptyMap() {
            List<Integer> values = map.values();
            assertTrue(values.isEmpty());
        }
    }

    @Nested
    @DisplayName("clear()")
    class ClearOperations {

        @Test
        @DisplayName("clear() очищает карту")
        void clearEmptiesMap() {
            map.put("a", 1);
            map.put("b", 2);
            map.clear();

            assertEquals(0, map.size());
            assertTrue(map.isEmpty());
            assertNull(map.get("a"));
            assertNull(map.get("b"));
        }

        @Test
        @DisplayName("clear() на пустой карте не вызывает ошибок")
        void clearOnEmptyMap() {
            assertDoesNotThrow(() -> map.clear());
            assertEquals(0, map.size());
        }
    }

    @Nested
    @DisplayName("putAll()")
    class PutAllOperations {

        @Test
        @DisplayName("putAll() добавляет все элементы из другой карты")
        void putAllAddsAllElements() {
            Map<String, Integer> source = new HashMap<>();
            source.put("a", 1);
            source.put("b", 2);
            source.put("c", 3);

            map.putAll(source);

            assertEquals(3, map.size());
            assertEquals(1, map.get("a"));
            assertEquals(2, map.get("b"));
            assertEquals(3, map.get("c"));
        }

        @Test
        @DisplayName("putAll() обновляет существующие ключи")
        void putAllUpdatesExistingKeys() {
            map.put("a", 1);

            Map<String, Integer> source = new HashMap<>();
            source.put("a", 100);
            source.put("b", 2);

            map.putAll(source);

            assertEquals(2, map.size());
            assertEquals(100, map.get("a"));
            assertEquals(2, map.get("b"));
        }
    }

    @Nested
    @DisplayName("replace()")
    class ReplaceOperations {

        @Test
        @DisplayName("replace() обновляет существующий ключ")
        void replaceUpdatesExistingKey() {
            map.put("apple", 42);
            Integer oldValue = map.replace("apple", 100);

            assertEquals(42, oldValue);
            assertEquals(100, map.get("apple"));
            assertEquals(1, map.size());
        }

        @Test
        @DisplayName("replace() возвращает null для несуществующего ключа")
        void replaceReturnsNullForMissingKey() {
            Integer result = map.replace("nonexistent", 100);
            assertNull(result);
        }

        @Test
        @DisplayName("replace() не добавляет новый ключ")
        void replaceDoesNotAddNewKey() {
            map.replace("new", 100);
            assertEquals(0, map.size());
            assertNull(map.get("new"));
        }
    }

    @Nested
    @DisplayName("Null ключи")
    class NullKeyOperations {

        @Test
        @DisplayName("put() с null ключом работает")
        void putWithNullKey() {
            map.put(null, 999);
            assertEquals(999, map.get(null));
            assertEquals(1, map.size());
        }

        @Test
        @DisplayName("get() с null ключом работает")
        void getWithNullKey() {
            map.put(null, 999);
            assertEquals(999, map.get(null));
        }

        @Test
        @DisplayName("containsKey() с null ключом работает")
        void containsKeyWithNullKey() {
            map.put(null, 999);
            assertTrue(map.containsKey(null));
            assertFalse(map.containsKey("other"));
        }

        @Test
        @DisplayName("remove() с null ключом работает")
        void removeWithNullKey() {
            map.put(null, 999);
            Integer removed = map.remove(null);

            assertEquals(999, removed);
            assertNull(map.get(null));
            assertEquals(0, map.size());
        }

        @Test
        @DisplayName("Обновление null ключа")
        void updateNullKey() {
            map.put(null, 999);
            map.put(null, 888);

            assertEquals(888, map.get(null));
            assertEquals(1, map.size());
        }
    }

    @Nested
    @DisplayName("Коллизии")
    class CollisionOperations {

        @Test
        @DisplayName("Маленький capacity вызывает коллизии")
        void smallCapacityCausesCollisions() {
            CustomHashMap<Integer, String> smallMap = new CustomHashMap<>(2);

            for (int i = 0; i < 20; i++) {
                smallMap.put(i, "val" + i);
            }

            assertEquals(20, smallMap.size());

            for (int i = 0; i < 20; i++) {
                assertEquals("val" + i, smallMap.get(i));
            }
        }

        @Test
        @DisplayName("Удаление из цепочки коллизий")
        void removeFromCollisionChain() {
            CustomHashMap<Integer, String> smallMap = new CustomHashMap<>(2);

            smallMap.put(1, "one");
            smallMap.put(3, "three");
            smallMap.put(5, "five");

            smallMap.remove(3);

            assertEquals(2, smallMap.size());
            assertEquals("one", smallMap.get(1));
            assertNull(smallMap.get(3));
            assertEquals("five", smallMap.get(5));
        }
    }

    @Nested
    @DisplayName("Разные типы данных")
    class DifferentTypes {

        @Test
        @DisplayName("Работа с Integer ключами")
        void workWithIntegerKeys() {
            CustomHashMap<Integer, String> intMap = new CustomHashMap<>(16);
            intMap.put(1, "one");
            intMap.put(2, "two");

            assertEquals("one", intMap.get(1));
            assertEquals("two", intMap.get(2));
        }

        @Test
        @DisplayName("Работа с null значениями")
        void workWithNullValues() {
            map.put("key", null);
            assertNull(map.get("key"));
            assertTrue(map.containsKey("key"));
            assertEquals(1, map.size());
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Полный цикл: put -> get -> remove -> get")
        void fullCycle() {
            map.put("apple", 42);
            assertEquals(42, map.get("apple"));

            map.remove("apple");
            assertNull(map.get("apple"));
            assertEquals(0, map.size());
        }

        @Test
        @DisplayName("Множественные операции")
        void multipleOperations() {
            for (int i = 0; i < 100; i++) {
                map.put("key" + i, i);
            }

            assertEquals(100, map.size());

            for (int i = 0; i < 50; i++) {
                map.remove("key" + i);
            }

            assertEquals(50, map.size());

            for (int i = 50; i < 100; i++) {
                assertEquals(i, map.get("key" + i));
            }
        }
    }
}