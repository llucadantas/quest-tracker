package com.ads.quest_tracker.structures.avl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - AVLNode")
class AVLNodeTest {

    @Nested
    @DisplayName("Cenários de Inicialização")
    class InitializationTests {

        @Test
        @DisplayName("Deve inicializar um nó folha com altura 1 e filhos nulos")
        void shouldInitializeLeafNodeWithHeightOneAndNullChildren() {
            AVLNode<Integer> node = new AVLNode<>(42);

            assertEquals(42, node.getValue());
            assertEquals(1, node.getHeight());
            assertNull(node.getLeft());
            assertNull(node.getRight());
            assertTrue(node.isLeaf());
            assertFalse(node.hasLeft());
            assertFalse(node.hasRight());
        }

        @Test
        @DisplayName("Deve inicializar nó com filhos utilizando construtor sobrecarregado")
        void shouldInitializeWithChildren() {
            AVLNode<Integer> leftChild = new AVLNode<>(10);
            AVLNode<Integer> rightChild = new AVLNode<>(30);
            AVLNode<Integer> parent = new AVLNode<>(20, leftChild, rightChild);

            assertEquals(20, parent.getValue());
            assertEquals(leftChild, parent.getLeft());
            assertEquals(rightChild, parent.getRight());
            assertFalse(parent.isLeaf());
            assertTrue(parent.hasLeft());
            assertTrue(parent.hasRight());
        }

        @Test
        @DisplayName("Deve lançar NullPointerException ao instanciar nó com valor nulo")
        void shouldThrowExceptionWhenValueIsNull() {
            assertThrows(NullPointerException.class, () -> new AVLNode<>(null));
        }
    }

    @Nested
    @DisplayName("Cenários de Modificação e Encapsulamento")
    class ModificationTests {

        @Test
        @DisplayName("Deve alterar e recuperar valor corretamente")
        void shouldSetAndGetValue() {
            AVLNode<String> node = new AVLNode<>("inicial");
            node.setValue("atualizado");

            assertEquals("atualizado", node.getValue());
        }

        @Test
        @DisplayName("Deve lançar NullPointerException ao definir valor nulo via setter")
        void shouldThrowExceptionWhenSettingNullValue() {
            AVLNode<String> node = new AVLNode<>("valido");
            assertThrows(NullPointerException.class, () -> node.setValue(null));
        }

        @Test
        @DisplayName("Deve atualizar referências dos filhos esquerdo e direito")
        void shouldUpdateLeftAndRightReferences() {
            AVLNode<Integer> root = new AVLNode<>(50);
            AVLNode<Integer> left = new AVLNode<>(25);
            AVLNode<Integer> right = new AVLNode<>(75);

            root.setLeft(left);
            assertTrue(root.hasLeft());
            assertEquals(left, root.getLeft());

            root.setRight(right);
            assertTrue(root.hasRight());
            assertEquals(right, root.getRight());

            assertFalse(root.isLeaf());
        }

        @Test
        @DisplayName("Deve atualizar altura com valor válido")
        void shouldUpdateHeightCorrectly() {
            AVLNode<Integer> node = new AVLNode<>(10);
            node.setHeight(3);

            assertEquals(3, node.getHeight());
        }

        @Test
        @DisplayName("Deve lançar IllegalArgumentException para altura inválida (< 1)")
        void shouldThrowExceptionForInvalidHeight() {
            AVLNode<Integer> node = new AVLNode<>(10);

            assertThrows(IllegalArgumentException.class, () -> node.setHeight(0));
            assertThrows(IllegalArgumentException.class, () -> node.setHeight(-1));
        }
    }

    @Nested
    @DisplayName("Cenários de Representação em Texto")
    class RepresentationTests {

        @Test
        @DisplayName("toString deve conter informações essenciais do nó")
        void toStringShouldContainEssentialInfo() {
            AVLNode<String> node = new AVLNode<>("teste");
            String repr = node.toString();

            assertTrue(repr.contains("teste"));
            assertTrue(repr.contains("height=1"));
        }
    }
}
