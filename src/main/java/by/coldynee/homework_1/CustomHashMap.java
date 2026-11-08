package by.coldynee.homework_1;

import java.util.*;

/**
 * Кастомная реализация хэш-таблицы (HashMap).
 * <p>
 * Использует метод цепочек (separate chaining) для разрешения коллизий.
 * Поддерживает {@code null} ключи и значения.
 * </p>
 * <p>
 * <b>Сложность операций:</b> O(1) в среднем, O(n) в худшем случае.
 * </p>
 *
 * @param <K> тип ключей
 * @param <V> тип значений
 */
public class CustomHashMap<K, V> {
    private Node<K, V>[] buckets;
    private int size = 0;

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public CustomHashMap(int capacity) {
        buckets = new Node[capacity];
    }

    private int hash(K key){
        if (key == null) return 0;
        return (key.hashCode() & 0x7fffffff) % buckets.length;
    }

    private boolean keysEqual(K key1, K key2) {
        if (key1 == null && key2 == null) return true;
        if (key1 == null || key2 == null) return false;
        return key1.equals(key2);
    }

    /**
     * Добавляет пару ключ-значение или обновляет существующую.
     *
     * @return предыдущее значение, или {@code null}, если ключа не было
     */
    public V put(K key, V value){
        int index = hash(key);

        if (buckets[index] == null) {
            buckets[index] = new Node<>(key, value);
            size++;
            return null;
        }

        Node<K, V> current = buckets[index];
        Node<K, V> last = null;
        while (current != null) {
            if (keysEqual(current.key, key)){
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            last = current;
            current = current.next;
        }

        last.next = new Node<>(key, value);
        size++;
        return null;
    }

    /**
     * @return значение по ключу, или {@code null}, если ключ не найден
     */
    public V get(K key){
        int index = hash(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (keysEqual(current.key, key)) return current.value;
            current = current.next;
        }

        return null;
    }

    /**
     * @return удалённое значение, или {@code null}, если ключ не найден
     */
    public V remove(K key){
        int index = hash(key);
        Node<K, V> current = buckets[index];
        Node<K, V> prev = null;

        while (current != null) {
            if (keysEqual(current.key, key)) {
                if (prev == null) buckets[index] = current.next;
                else prev.next = current.next;
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }

        return null;
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public boolean containsKey(K key) {
        int index = hash(key);
        Node<K, V> current = buckets[index];

        while(current != null) {
            if (keysEqual(current.key, key)) return true;
            current = current.next;
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (Node<K, V> bucket : buckets) {
            Node<K, V> current = bucket;
            while (current != null) {
                if (Objects.equals(current.value, value)) { return true; }
                current = current.next;
            }
        }
        return false;
    }

    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Node<K, V> bucket : buckets) {
            Node<K, V> current = bucket;
            while (current != null) {
                keys.add(current.key);
                current = current.next;
            }
        }
        return keys;
    }

    public List<V> values() {
        List<V> values = new ArrayList<>();
        for (Node<K, V> bucket : buckets) {
            Node<K, V> current = bucket;
            while (current != null) {
                values.add(current.value);
                current = current.next;
            }
        }
        return values;
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Заменяет значение для существующего ключа.
     * В отличие от {@link #put}, не добавляет новый ключ, если его нет.
     *
     * @return предыдущее значение, или {@code null}, если ключ не найден
     */
    public V replace(K key, V value) {
        int index = hash(key);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            current = current.next;
        }

        return null;
    }

    public void clear() {
        Arrays.fill(buckets, null);
        size = 0;
    }
}
