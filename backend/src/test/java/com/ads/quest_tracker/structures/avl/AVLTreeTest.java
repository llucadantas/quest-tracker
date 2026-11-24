package com.ads.quest_tracker.structures.avl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - AVLTree")
class AVLTreeTest {
    private AVLTree<Integer> tree;

    @BeforeEach
    void setUp() {
        tree = new AVLTree<>();
    }

    private <T> void assertAVLInvariants(AVLTree<T> avlTree) {
        assertNodeInvariants(avlTree.getRoot(), avlTree);
    }

    private <T> void assertNodeInvariants(AVLNode<T> node, AVLTree<T> avlTree) {
        if (node == null) {
            return;
        }

        int leftHeight = avlTree.getNodeHeight(node.getLeft());
        int rightHeight = avlTree.getNodeHeight(node.getRight());
        int expectedHeight = 1 + Math.max(leftHeight, rightHeight);

        assertEquals(expectedHeight, node.getHeight(),
                "Altura do nó " + node.getValue() + " inconsistente.");

        int balanceFactor = avlTree.getBalanceFactor(node);
        assertTrue(balanceFactor >= -1 && balanceFactor <= 1,
                "Fator de balanceamento do nó " + node.getValue() + " violado: " + balanceFactor);

        assertNodeInvariants(node.getLeft(), avlTree);
        assertNodeInvariants(node.getRight(), avlTree);
    }

    @Nested
    @DisplayName("Inicialização e Estado Inicial")
    class InitializationTests {
        @Test
        @DisplayName("Árvore recém-criada deve estar vazia com tamanho e altura zero")
        void shouldInitializeEmptyTree() {
            assertTrue(tree.isEmpty());
            assertEquals(0, tree.size());
            assertEquals(0, tree.height());
            assertNull(tree.getRoot());
        }

        @Test
        @DisplayName("Deve permitir inicialização com Comparator customizado")
        void shouldInitializeWithCustomComparator() {
            AVLTree<String> customTree = new AVLTree<>(Comparator.reverseOrder());
            assertTrue(customTree.isEmpty());

            customTree.insert("alpha");
            customTree.insert("beta");

            assertEquals("alpha", customTree.getRoot().getValue());
            assertEquals("beta", customTree.getRoot().getLeft().getValue());
            assertAVLInvariants(customTree);
        }

        @Test
        @DisplayName("Deve rejeitar inicialização com Comparator nulo")
        void shouldThrowExceptionWhenComparatorIsNull() {
            assertThrows(NullPointerException.class, () -> new AVLTree<Integer>(null));
        }

        @Test
        @DisplayName("Deve lançar ClassCastException se elemento não implementar Comparable e não houver Comparator")
        void shouldThrowExceptionWhenElementNotComparable() {
            class ObjetoNaoComparavel {}
            AVLTree<ObjetoNaoComparavel> invalidTree = new AVLTree<>();
            assertThrows(ClassCastException.class, () -> invalidTree.insert(new ObjetoNaoComparavel()));
        }
    }

    @Nested
    @DisplayName("Operações de Inserção e Política de Duplicados")
    class InsertionTests {
        @Test
        @DisplayName("Deve inserir o primeiro elemento como raiz com altura 1")
        void shouldInsertRootElement() {
            boolean inserted = tree.insert(50);

            assertTrue(inserted);
            assertFalse(tree.isEmpty());
            assertEquals(1, tree.size());
            assertEquals(1, tree.height());
            assertNotNull(tree.getRoot());
            assertEquals(50, tree.getRoot().getValue());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Deve inserir múltiplos elementos respeitando as propriedades de ordenação")
        void shouldInsertMultipleElements() {
            assertTrue(tree.insert(50));
            assertTrue(tree.insert(30));
            assertTrue(tree.insert(70));

            assertEquals(3, tree.size());
            assertEquals(50, tree.getRoot().getValue());
            assertEquals(30, tree.getRoot().getLeft().getValue());
            assertEquals(70, tree.getRoot().getRight().getValue());
            assertEquals(2, tree.height());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Deve rejeitar elementos duplicados retornando false sem alterar o tamanho")
        void shouldRejectDuplicates() {
            assertTrue(tree.insert(50));
            assertTrue(tree.insert(30));
            assertEquals(2, tree.size());

            assertFalse(tree.insert(50));
            assertFalse(tree.insert(30));

            assertEquals(2, tree.size());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Deve lançar NullPointerException ao tentar inserir valor nulo")
        void shouldThrowExceptionOnNullInsert() {
            assertThrows(NullPointerException.class, () -> tree.insert(null));
        }
    }

    @Nested
    @DisplayName("Operações de Busca e Verificação de Existência")
    class SearchTests {
        @BeforeEach
        void populateTree() {
            tree.insert(50);
            tree.insert(25);
            tree.insert(75);
            tree.insert(10);
            tree.insert(30);
        }

        @Test
        @DisplayName("Deve encontrar elemento existente usando search e contains")
        void shouldFindExistingElement() {
            assertEquals(25, tree.search(25));
            assertTrue(tree.contains(25));
            assertEquals(75, tree.search(75));
            assertTrue(tree.contains(75));
        }

        @Test
        @DisplayName("Deve retornar null e false para elemento inexistente")
        void shouldReturnNullForNonExistingElement() {
            assertNull(tree.search(999));
            assertFalse(tree.contains(999));
        }

        @Test
        @DisplayName("Deve lançar NullPointerException ao buscar elemento nulo")
        void shouldThrowExceptionOnNullSearch() {
            assertThrows(NullPointerException.class, () -> tree.search(null));
            assertThrows(NullPointerException.class, () -> tree.contains(null));
        }
    }

    @Nested
    @DisplayName("Operações de Remoção")
    class DeletionTests {
        @Test
        @DisplayName("Deve retornar false ao tentar remover de árvore vazia")
        void shouldReturnFalseWhenDeletingFromEmptyTree() {
            assertFalse(tree.delete(10));
            assertEquals(0, tree.size());
        }

        @Test
        @DisplayName("Deve retornar false ao tentar remover elemento inexistente")
        void shouldReturnFalseWhenElementNotFound() {
            tree.insert(50);
            tree.insert(30);

            assertFalse(tree.delete(100));
            assertEquals(2, tree.size());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Caso 1: Deve remover nó folha corretamente")
        void shouldDeleteLeafNode() {
            tree.insert(50);
            tree.insert(20);
            tree.insert(80);

            assertTrue(tree.delete(20));
            assertEquals(2, tree.size());
            assertFalse(tree.contains(20));
            assertNull(tree.getRoot().getLeft());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Caso 2: Deve remover nó com 1 filho à esquerda")
        void shouldDeleteNodeWithLeftChildOnly() {
            tree.insert(50);
            tree.insert(25);
            tree.insert(75);
            tree.insert(10);

            assertTrue(tree.delete(25));
            assertEquals(3, tree.size());
            assertFalse(tree.contains(25));
            assertEquals(10, tree.getRoot().getLeft().getValue());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Caso 2: Deve remover nó com 1 filho à direita")
        void shouldDeleteNodeWithRightChildOnly() {
            tree.insert(50);
            tree.insert(25);
            tree.insert(75);
            tree.insert(85);

            assertTrue(tree.delete(75));
            assertEquals(3, tree.size());
            assertFalse(tree.contains(75));
            assertEquals(85, tree.getRoot().getRight().getValue());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Caso 3: Deve remover nó com 2 filhos substituindo pelo sucessor in-order")
        void shouldDeleteNodeWithTwoChildrenUsingSuccessor() {
            tree.insert(50);
            tree.insert(25);
            tree.insert(75);
            tree.insert(60);
            tree.insert(85);
            tree.insert(65);

            assertTrue(tree.delete(75));
            assertEquals(5, tree.size());
            assertFalse(tree.contains(75));
            assertTrue(tree.contains(60));
            assertTrue(tree.contains(85));
            assertTrue(tree.contains(65));
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Deve remover a raiz com sucesso e atualizar estrutura")
        void shouldDeleteRootNode() {
            tree.insert(50);
            tree.insert(25);
            tree.insert(75);

            assertTrue(tree.delete(50));
            assertEquals(2, tree.size());
            assertFalse(tree.contains(50));
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Alias remove(T) deve funcionar de forma equivalente a delete(T)")
        void shouldSupportRemoveAlias() {
            tree.insert(100);
            assertTrue(tree.remove(100));
            assertTrue(tree.isEmpty());
        }

        @Test
        @DisplayName("Deve lançar NullPointerException ao tentar remover valor nulo")
        void shouldThrowExceptionOnNullDelete() {
            assertThrows(NullPointerException.class, () -> tree.delete(null));
        }
    }

    @Nested
    @DisplayName("ETAPA 4 - Rotações e Mecanismos de Balanceamento")
    class RotationAndBalancingTests {
        @Test
        @DisplayName("Caso LL: Rotação Simples à Direita na Inserção")
        void shouldExecuteRightRotationLLOnInsert() {
            tree.insert(30);
            tree.insert(20);
            tree.insert(10);

            assertEquals(20, tree.getRoot().getValue());
            assertEquals(10, tree.getRoot().getLeft().getValue());
            assertEquals(30, tree.getRoot().getRight().getValue());
            assertEquals(2, tree.height());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Caso RR: Rotação Simples à Esquerda na Inserção")
        void shouldExecuteLeftRotationRROnInsert() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            assertEquals(20, tree.getRoot().getValue());
            assertEquals(10, tree.getRoot().getLeft().getValue());
            assertEquals(30, tree.getRoot().getRight().getValue());
            assertEquals(2, tree.height());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Caso LR: Rotação Dupla Esquerda-Direita na Inserção")
        void shouldExecuteDoubleRotationLROnInsert() {
            tree.insert(30);
            tree.insert(10);
            tree.insert(20);

            assertEquals(20, tree.getRoot().getValue());
            assertEquals(10, tree.getRoot().getLeft().getValue());
            assertEquals(30, tree.getRoot().getRight().getValue());
            assertEquals(2, tree.height());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Caso RL: Rotação Dupla Direita-Esquerda na Inserção")
        void shouldExecuteDoubleRotationRLOnInsert() {
            tree.insert(10);
            tree.insert(30);
            tree.insert(20);

            assertEquals(20, tree.getRoot().getValue());
            assertEquals(10, tree.getRoot().getLeft().getValue());
            assertEquals(30, tree.getRoot().getRight().getValue());
            assertEquals(2, tree.height());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Balanceamento após Remoção gerando Rotação LL")
        void shouldRebalanceAfterDeletionTriggeringLL() {
            tree.insert(50);
            tree.insert(30);
            tree.insert(70);
            tree.insert(20);

            assertTrue(tree.delete(70));

            assertEquals(30, tree.getRoot().getValue());
            assertEquals(20, tree.getRoot().getLeft().getValue());
            assertEquals(50, tree.getRoot().getRight().getValue());
            assertEquals(2, tree.height());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Balanceamento após Remoção gerando Rotação RR")
        void shouldRebalanceAfterDeletionTriggeringRR() {
            tree.insert(50);
            tree.insert(30);
            tree.insert(70);
            tree.insert(80);

            assertTrue(tree.delete(30));

            assertEquals(70, tree.getRoot().getValue());
            assertEquals(50, tree.getRoot().getLeft().getValue());
            assertEquals(80, tree.getRoot().getRight().getValue());
            assertEquals(2, tree.height());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Balanceamento após Remoção gerando Rotação LR")
        void shouldRebalanceAfterDeletionTriggeringLR() {
            tree.insert(50);
            tree.insert(30);
            tree.insert(70);
            tree.insert(40);

            assertTrue(tree.delete(70));

            assertEquals(40, tree.getRoot().getValue());
            assertEquals(30, tree.getRoot().getLeft().getValue());
            assertEquals(50, tree.getRoot().getRight().getValue());
            assertEquals(2, tree.height());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Balanceamento após Remoção gerando Rotação RL")
        void shouldRebalanceAfterDeletionTriggeringRL() {
            tree.insert(50);
            tree.insert(30);
            tree.insert(70);
            tree.insert(60);

            assertTrue(tree.delete(30));

            assertEquals(60, tree.getRoot().getValue());
            assertEquals(50, tree.getRoot().getLeft().getValue());
            assertEquals(70, tree.getRoot().getRight().getValue());
            assertEquals(2, tree.height());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Deve manter a árvore balanceada após inserções massivas sequenciais")
        void shouldMaintainBalanceOnSequentialMassInsertions() {
            for (int i = 1; i <= 63; i++) {
                assertTrue(tree.insert(i));
            }

            assertEquals(63, tree.size());
            assertEquals(6, tree.height());
            assertAVLInvariants(tree);

            for (int i = 1; i <= 30; i++) {
                assertTrue(tree.delete(i));
                assertAVLInvariants(tree);
            }

            assertEquals(33, tree.size());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("Cálculo correto do fator de balanceamento para nós nulos e nós folha")
        void shouldCalculateBalanceFactorCorrectly() {
            assertEquals(0, tree.getBalanceFactor(null));

            tree.insert(50);
            assertEquals(0, tree.getBalanceFactor(tree.getRoot()));

            tree.insert(30);
            assertEquals(1, tree.getBalanceFactor(tree.getRoot()));

            tree.insert(70);
            assertEquals(0, tree.getBalanceFactor(tree.getRoot()));
        }
    }

    @Nested
    @DisplayName("Operações de Consulta de Altura e Limpeza")
    class HeightAndClearTests {
        @Test
        @DisplayName("Altura deve ser atualizada corretamente após inserções e remoções")
        void shouldUpdateHeightCorrectly() {
            assertEquals(0, tree.height());

            tree.insert(50);
            assertEquals(1, tree.height());

            tree.insert(30);
            assertEquals(2, tree.height());

            tree.insert(70);
            assertEquals(2, tree.height());

            tree.insert(20);
            assertEquals(3, tree.height());

            tree.delete(20);
            assertEquals(2, tree.height());
            assertAVLInvariants(tree);
        }

        @Test
        @DisplayName("clear() deve esvaziar a árvore completamente")
        void shouldClearTree() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);
            assertEquals(3, tree.size());

            tree.clear();

            assertTrue(tree.isEmpty());
            assertEquals(0, tree.size());
            assertEquals(0, tree.height());
            assertNull(tree.getRoot());
            assertFalse(tree.contains(10));
        }
    }

    @Nested
    @DisplayName("ETAPA 5 - Percursos e Operações Auxiliares")
    class TraversalsAndAuxiliaryTests {
        @Test
        @DisplayName("Percursos em árvore vazia devem retornar listas vazias imutáveis")
        void shouldReturnEmptyListsOnEmptyTree() {
            assertTrue(tree.inOrder().isEmpty());
            assertTrue(tree.preOrder().isEmpty());
            assertTrue(tree.postOrder().isEmpty());
            assertTrue(tree.levelOrder().isEmpty());
            assertTrue(tree.getSortedElements().isEmpty());
            assertNull(tree.min());
            assertNull(tree.max());
            assertNull(tree.getRootValue());
        }

        @Test
        @DisplayName("Deve executar percursos tradicionais com ordem matemática exata")
        void shouldExecuteAllTraversalsAccurately() {
            tree.insert(50);
            tree.insert(20);
            tree.insert(70);
            tree.insert(10);
            tree.insert(30);
            tree.insert(60);
            tree.insert(80);

            assertEquals(java.util.List.of(10, 20, 30, 50, 60, 70, 80), tree.inOrder());

            assertEquals(java.util.List.of(50, 20, 10, 30, 70, 60, 80), tree.preOrder());

            assertEquals(java.util.List.of(10, 30, 20, 60, 80, 70, 50), tree.postOrder());

            assertEquals(java.util.List.of(50, 20, 70, 10, 30, 60, 80), tree.levelOrder());

            assertEquals(tree.inOrder(), tree.getSortedElements());
        }

        @Test
        @DisplayName("Deve identificar corretamente os extremos (menor, maior e raiz)")
        void shouldIdentifyMinMaxAndRoot() {
            tree.insert(40);
            tree.insert(20);
            tree.insert(60);
            tree.insert(5);
            tree.insert(95);

            assertEquals(5, tree.min());
            assertEquals(95, tree.max());
            assertEquals(40, tree.getRootValue());
        }

        @Test
        @DisplayName("Listas retornadas pelos percursos devem ser imutáveis e proteger a árvore")
        void returnedListsShouldBeUnmodifiable() {
            tree.insert(10);
            java.util.List<Integer> list = tree.inOrder();

            assertThrows(UnsupportedOperationException.class, () -> list.add(999));
        }

        @Test
        @DisplayName("Consultas não devem alterar o tamanho, altura ou estrutura da árvore")
        void queriesShouldNotMutateState() {
            tree.insert(50);
            tree.insert(25);
            tree.insert(75);

            int sizeBefore = tree.size();
            int heightBefore = tree.height();

            tree.inOrder();
            tree.preOrder();
            tree.postOrder();
            tree.levelOrder();
            tree.min();
            tree.max();
            tree.getRootValue();
            tree.getSortedElements();

            assertEquals(sizeBefore, tree.size());
            assertEquals(heightBefore, tree.height());
            assertAVLInvariants(tree);
        }
    }
}
