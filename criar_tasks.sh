#!/bin/bash

REPO="llucadantas/quest-tracker"

create_issue() {
  local title="$1"
  local body="$2"
  local label="$3"
  gh issue create --repo "$REPO" --title "$title" --body "$body" --label "$label" --project "kanban-questtracker"
}

create_issue "TASK-01: Setup dos Repositórios" "Criar projeto Spring Boot (Java 17/21, Spring Web, Lombok) e projeto React (Vite), configurando CORS no backend." "backend-core"
create_issue "TASK-02: Modelagem de Domínio e DTOs" "Criar classes Task, SubTask, enums (Status, Priority) e os DTOs de entrada/saída." "backend-core"
create_issue "TASK-03: Implementação da Lista Encadeada (SubTaskLinkedList)" "Criar nós, inserção, remoção, marcação de sub-etapa como concluída e método toList()." "backend-core"
create_issue "TASK-04: Implementação da Árvore AVL (AVLTree)" "Implementar nós com fator de balanceamento, rotações (simples e duplas), inserção, busca por ID em O(log n) e travessia in-order." "backend-core"
create_issue "TASK-05: Implementação da Fila de Prioridade (PriorityHeap)" "Implementar Max-Heap/Min-Heap com operações insert e extract baseadas na urgência da tarefa." "backend-core"

create_issue "TASK-06: Camada de Serviço (TaskService)" "Integrar AVLTree e PriorityHeap como singletons em memória com controle de concorrência (synchronized)." "backend-api"
create_issue "TASK-07: Persistência Snapshot (JSON)" "Implementar rotina com Jackson (@PostConstruct e salvamento após mutações) para gravar e restaurar o estado em arquivo local." "backend-api"
create_issue "TASK-08: Endpoints de Tarefas e Busca (TaskController)" "Criar rotas POST /api/tasks, GET /api/tasks/{id} (busca na AVL) e GET /api/tasks (filtros por status e prioridade)." "backend-api"
create_issue "TASK-09: Endpoints de Fila de Execução" "Criar rotas POST /api/tasks/{id}/enqueue e POST /api/tasks/execute-next." "backend-api"
create_issue "TASK-10: Endpoints de Sub-tarefas" "Criar rotas POST /api/tasks/{id}/subtasks e PATCH /api/tasks/{id}/subtasks/{subId}/complete." "backend-api"

create_issue "TASK-11: Camada de Integração API (Axios/Fetch)" "Configurar cliente HTTP centralizado mapeando todos os endpoints do Spring Boot." "frontend-react"
create_issue "TASK-12: Tela/Painel de Cadastro e Busca Rápida" "Criar formulário de nova tarefa e barra de pesquisa por ID conectada à busca O(log n) da Árvore AVL." "frontend-react"
create_issue "TASK-13: Visão Geral e Filtros (Backlog)" "Criar listagem de tarefas com abas ou filtros para exibir todas, apenas pendentes ou ordenadas por prioridade." "frontend-react"
create_issue "TASK-14: Painel de Fila de Execução (Heap)" "Criar componente visual destacando a fila ativa e botão Executar Próxima Tarefa." "frontend-react"
create_issue "TASK-15: Modal/Drawer de Detalhes e Sub-tarefas" "Criar visualização interna da tarefa exibindo o checklist dinâmico (Lista Encadeada) com checkboxes e formulário de novos passos." "frontend-react"

create_issue "TASK-16: Testes Unitários e de Estresse (JUnit 5)" "Criar testes automatizados inserindo milhares de registros para comprovar auto-balanceamento da AVL, ordenação do Heap e referências da Lista." "qa"
create_issue "TASK-17: Homologação Ponta a Ponta" "Testar fluxo completo: Cadastro -> Enfileiramento -> Execução -> Sub-tarefas -> Reinício do servidor." "qa"

echo "Todas as issues foram criadas com sucesso!"