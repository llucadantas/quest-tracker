package com.ads.quest_tracker.structures.linkedlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - LinkedList")
class LinkedListTest {

    private LinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new LinkedList<>();
    }

    @Nested
    @DisplayName("Estado Inicial")
    class InitialStateTests {

        @Test
        @DisplayName("Lista recém-criada deve estar vazia e com tamanho zero")
        void shouldBeEmptyInitially() {
            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
            assertEquals("[]", list.toString());
        }

        @Test
        @DisplayName("getFirst e getLast em lista vazia devem lançar NoSuchElementException")
        void shouldThrowNoSuchElementExceptionOnEmptyQueries() {
            assertThrows(NoSuchElementException.class, () -> list.getFirst());
            assertThrows(NoSuchElementException.class, () -> list.getLast());
            assertThrows(NoSuchElementException.class, () -> list.removeFirst());
            assertThrows(NoSuchElementException.class, () -> list.removeLast());
        }
    }

    @Nested
    @DisplayName("Operações de Inserção")
    class InsertionTests {

        @Test
        @DisplayName("add(T) deve adicionar elementos no final da lista")
        void shouldAddElementsToEnd() {
            list.add("A");
            list.add("B");
            list.add("C");

            assertEquals(3, list.size());
            assertFalse(list.isEmpty());
            assertEquals("A", list.getFirst());
            assertEquals("C", list.getLast());
            assertEquals("A", list.get(0));
            assertEquals("B", list.get(1));
            assertEquals("C", list.get(2));
            assertEquals("[A, B, C]", list.toString());
        }

        @Test
        @DisplayName("addFirst(T) deve adicionar elementos no início da lista")
        void shouldAddElementsToBeginning() {
            list.addFirst("C");
            list.addFirst("B");
            list.addFirst("A");

            assertEquals(3, list.size());
            assertEquals("A", list.getFirst());
            assertEquals("C", list.getLast());
            assertEquals("[A, B, C]", list.toString());
        }

        @Test
        @DisplayName("add(index, T) deve inserir no início, meio e fim corretamente")
        void shouldAddAtIndexCorrectly() {
            list.add(0, "B"); // [B] (início na lista vazia)
            list.add(0, "A"); // [A, B] (início com elementos)
            list.add(2, "D"); // [A, B, D] (fim)
            list.add(2, "C"); // [A, B, C, D] (meio)

            assertEquals(4, list.size());
            assertEquals("A", list.get(0));
            assertEquals("B", list.get(1));
            assertEquals("C", list.get(2));
            assertEquals("D", list.get(3));
            assertEquals("D", list.getLast());
        }

        @Test
        @DisplayName("add(index, T) deve lançar IndexOutOfBoundsException para índices inválidos")
        void shouldThrowExceptionForInvalidAddIndices() {
            assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "X"));
            assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, "X")); // size é 0, index 1 é inválido

            list.add("A");
            assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, "X")); // size é 1, index 2 é inválido
        }

        @Test
        @DisplayName("Deve permitir inserção e manipulação de elementos nulos")
        void shouldHandleNullElements() {
            list.add(null);
            list.addFirst("Inicio");
            list.add(null);

            assertEquals(3, list.size());
            assertNull(list.get(1));
            assertTrue(list.contains(null));
            assertEquals(1, list.indexOf(null));
        }
    }

    @Nested
    @DisplayName("Operações de Remoção")
    class DeletionTests {

        @Test
        @DisplayName("removeFirst() deve remover e retornar o primeiro elemento")
        void shouldRemoveFirstElement() {
            list.add("A");
            list.add("B");

            assertEquals("A", list.removeFirst());
            assertEquals(1, list.size());
            assertEquals("B", list.getFirst());
            assertEquals("B", list.getLast());

            assertEquals("B", list.removeFirst());
            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
        }

        @Test
        @DisplayName("removeLast() deve remover e retornar o último elemento")
        void shouldRemoveLastElement() {
            list.add("A");
            list.add("B");
            list.add("C");

            assertEquals("C", list.removeLast());
            assertEquals(2, list.size());
            assertEquals("B", list.getLast());

            assertEquals("B", list.removeLast());
            assertEquals(1, list.size());
            assertEquals("A", list.getFirst());
            assertEquals("A", list.getLast());

            assertEquals("A", list.removeLast());
            assertTrue(list.isEmpty());
        }

        @Test
        @DisplayName("remove(index) deve remover elemento no índice especificado")
        void shouldRemoveAtIndex() {
            list.add("A");
            list.add("B");
            list.add("C");
            list.add("D");

            // Remove do meio
            assertEquals("B", list.remove(1));
            assertEquals(3, list.size());
            assertEquals("[A, C, D]", list.toString());

            // Remove do início
            assertEquals("A", list.remove(0));
            assertEquals(2, list.size());

            // Remove do fim
            assertEquals("D", list.remove(1));
            assertEquals(1, list.size());
            assertEquals("C", list.getFirst());
            assertEquals("C", list.getLast());
        }

        @Test
        @DisplayName("remove(index) deve lançar IndexOutOfBoundsException para índices inválidos")
        void shouldThrowExceptionForInvalidRemoveIndices() {
            assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));

            list.add("A");
            assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        }

        @Test
        @DisplayName("remove(T element) deve remover por valor corretamente")
        void shouldRemoveByValue() {
            assertFalse(list.remove("Inexistente"));

            list.add("A");
            list.add("B");
            list.add("C");
            list.add("B");

            // Remove primeira ocorrência de B
            assertTrue(list.remove("B"));
            assertEquals(3, list.size());
            assertEquals("[A, C, B]", list.toString());

            // Remove primeiro elemento (A)
            assertTrue(list.remove("A"));
            assertEquals("C", list.getFirst());

            // Remove último elemento (B)
            assertTrue(list.remove("B"));
            assertEquals("C", list.getLast());
            assertEquals(1, list.size());

            // Tenta remover elemento já inexistente
            assertFalse(list.remove("Z"));
        }

        @Test
        @DisplayName("remove(T element) deve remover elemento nulo")
        void shouldRemoveNullElement() {
            list.add("A");
            list.add(null);
            list.add("B");

            assertTrue(list.remove(null));
            assertEquals(2, list.size());
            assertFalse(list.contains(null));
        }
    }

    @Nested
    @DisplayName("Operações de Consulta e Busca")
    class QueryTests {

        @BeforeEach
        void populate() {
            list.add("Primeiro");
            list.add("Segundo");
            list.add("Terceiro");
        }

        @Test
        @DisplayName("get(index) deve recuperar elementos em tempo O(n)")
        void shouldGetElementAtIndex() {
            assertEquals("Primeiro", list.get(0));
            assertEquals("Segundo", list.get(1));
            assertEquals("Terceiro", list.get(2));
        }

        @Test
        @DisplayName("get(index) deve lançar IndexOutOfBoundsException para índices inválidos")
        void shouldThrowExceptionOnInvalidGet() {
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
        }

        @Test
        @DisplayName("contains(T) e indexOf(T) devem localizar elementos corretamente")
        void shouldFindElementAndIndex() {
            assertTrue(list.contains("Segundo"));
            assertEquals(1, list.indexOf("Segundo"));

            assertFalse(list.contains("Inexistente"));
            assertEquals(-1, list.indexOf("Inexistente"));
        }
    }

    @Nested
    @DisplayName("Operações de Controle e Limpeza")
    class ControlTests {

        @Test
        @DisplayName("clear() deve esvaziar a lista e resetar ponteiros")
        void shouldClearList() {
            list.add("A");
            list.add("B");
            list.add("C");
            assertEquals(3, list.size());

            list.clear();

            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
            assertEquals("[]", list.toString());
            assertThrows(NoSuchElementException.class, () -> list.getFirst());
            assertThrows(NoSuchElementException.class, () -> list.getLast());
        }

        @Test
        @DisplayName("Deve permitir iteração completa via for-each (Iterable)")
        void shouldSupportForEachIteration() {
            list.add("1");
            list.add("2");
            list.add("3");

            List<String> collected = new ArrayList<>();
            for (String item : list) {
                collected.add(item);
            }

            assertEquals(List.of("1", "2", "3"), collected);
        }
    }

    @Nested
    @DisplayName("Exemplos de Utilização (Integer e Objeto Personalizado)")
    class UsageExamplesTests {

        @Test
        @DisplayName("Exemplo de utilização com Integer")
        void shouldWorkWithInteger() {
            LinkedList<Integer> intList = new LinkedList<>();
            intList.add(10);
            intList.add(20);
            intList.addFirst(5);
            intList.add(2, 15); // [5, 10, 15, 20]

            assertEquals(4, intList.size());
            assertEquals(5, intList.getFirst());
            assertEquals(20, intList.getLast());
            assertEquals(15, intList.get(2));

            assertTrue(intList.contains(10));
            assertEquals(1, intList.indexOf(10));

            intList.removeFirst(); // remove 5
            intList.removeLast();  // remove 20
            intList.remove(Integer.valueOf(15)); // remove 15

            assertEquals(1, intList.size());
            assertEquals(10, intList.getFirst());
        }

        static class RegistroPersonalizado {
            private final int id;
            private final String descricao;

            RegistroPersonalizado(int id, String descricao) {
                this.id = id;
                this.descricao = descricao;
            }

            public int getId() {
                return id;
            }

            public String getDescricao() {
                return descricao;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                RegistroPersonalizado that = (RegistroPersonalizado) o;
                return id == that.id && java.util.Objects.equals(descricao, that.descricao);
            }

            @Override
            public int hashCode() {
                return java.util.Objects.hash(id, descricao);
            }

            @Override
            public String toString() {
                return "Registro#" + id + "{" + descricao + "}";
            }
        }

        @Test
        @DisplayName("Exemplo de utilização com Objeto Personalizado")
        void shouldWorkWithCustomObject() {
            LinkedList<RegistroPersonalizado> customList = new LinkedList<>();
            RegistroPersonalizado r1 = new RegistroPersonalizado(1, "Configurar Ambiente");
            RegistroPersonalizado r2 = new RegistroPersonalizado(2, "Implementar Estrutura");
            RegistroPersonalizado r3 = new RegistroPersonalizado(3, "Executar Testes");

            customList.add(r1);
            customList.add(r2);
            customList.addFirst(r3); // [r3, r1, r2]

            assertEquals(3, customList.size());
            assertEquals(r3, customList.getFirst());
            assertEquals(r2, customList.getLast());

            // Busca e remoção por valor baseado em equals
            assertTrue(customList.contains(new RegistroPersonalizado(2, "Implementar Estrutura")));
            assertEquals(2, customList.indexOf(new RegistroPersonalizado(2, "Implementar Estrutura")));

            assertTrue(customList.remove(new RegistroPersonalizado(1, "Configurar Ambiente")));
            assertEquals(2, customList.size());
            assertEquals(r3, customList.get(0));
            assertEquals(r2, customList.get(1));
        }
    }
}
