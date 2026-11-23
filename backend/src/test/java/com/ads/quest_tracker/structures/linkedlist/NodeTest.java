package com.ads.quest_tracker.structures.linkedlist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - Node")
class NodeTest {

    @Test
    @DisplayName("Deve inicializar nó com valor e próximo nulo")
    void shouldInitializeNodeWithValueOnly() {
        Node<String> node = new Node<>("item");

        assertEquals("item", node.getValue());
        assertNull(node.getNext());
        assertFalse(node.hasNext());
    }

    @Test
    @DisplayName("Deve inicializar nó com valor e referência ao próximo nó")
    void shouldInitializeNodeWithValueAndNext() {
        Node<String> nextNode = new Node<>("proximo");
        Node<String> firstNode = new Node<>("primeiro", nextNode);

        assertEquals("primeiro", firstNode.getValue());
        assertEquals(nextNode, firstNode.getNext());
        assertTrue(firstNode.hasNext());
    }

    @Test
    @DisplayName("Deve alterar valor e próximo através de setters")
    void shouldUpdateValueAndNextViaSetters() {
        Node<Integer> node = new Node<>(1);
        node.setValue(2);
        Node<Integer> next = new Node<>(3);
        node.setNext(next);

        assertEquals(2, node.getValue());
        assertEquals(next, node.getNext());
        assertTrue(node.hasNext());
    }

    @Test
    @DisplayName("toString deve formatar dados do nó adequadamente")
    void toStringShouldFormatNodeData() {
        Node<String> node = new Node<>("teste");
        String repr = node.toString();

        assertTrue(repr.contains("teste"));
        assertTrue(repr.contains("hasNext=false"));
    }
}
