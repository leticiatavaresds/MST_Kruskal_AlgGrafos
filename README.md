## 1.	Árvore Geradora Mínima
Dado um grafo não direcionado e ponderado, uma Árvore Geradora Mínima (Minimum Spanning Tree) é uma subárvore desse grafo que conecta todos os vértices e apresenta a menor soma dos pesos de todas as arestas possível, ou seja, é a árvore geradora do grafo com menor peso dentre todas as árvores geradoras possíveis onde o peso da árvore é a soma de todos os pesos de suas arestas.
Na imagem abaixo encontram-se um grafo, não direcionado e com pesos, à esquerda e sua árvore geradora mínima correspondente à direita.





## 2.	Algoritmo de Kruskal
O algoritmo de Kruskal é um algoritmo guloso para encontrar a Árvore Geradora Mínima que tem como entrada um grafo G(V,E) não direcionado e não ponderado. Seu funcionamento ocorre da seguinte maneira:
1.	Cria-se a Árvore Geradora Mínima vazia.
2.	Organiza-se todas as arestas do Grafo em ordem crescente de peso.
3.	Pega-se a aresta com o menor peso e a adiciona à Árvore Geradora. Se ao ser adicionada, a aresta gerar um ciclo, rejeita-se essa aresta.
4.	Continua-se adicionando as arestas em ordem crescente de peso até que todos os vértices sejam alcançados.
 

## 3.	Implementação do Algoritmo de Kruskal
Para esse trabalho, o algoritmo de Kruskal foi implementado de duas formas diferentes, que serão melhor descritas abaixo, para a etapa de verificar se uma aresta pode entrar na árvore ou deve ser retirada. 
Pois como essa etapa consiste em verificar se a árvore gerada pela adição da aresta permanece acíclica, há mais de uma maneira de fazer, sendo as atualizadas aqui a verificação por Union-Find e a verificação por uma Busca em Profundidade. 
Contudo, a etapa de ordenar as arestas em ordem crescente de peso é a mesma para ambos os pesos, dada pelo método abaixo.

``` 
public static HashMap<Integer, Arestas> sortByValue(HashMap<Integer, Arestas> hm) {

        // Cria uma lista com as arestas do HashMap passado.
        List<Map.Entry<Integer, Arestas>> list =
                new LinkedList<Map.Entry<Integer, Arestas>>(hm.entrySet());

        // Ordena a Lista
        list.sort(new Comparator<Map.Entry<Integer, Arestas>>() {
            public int compare(Map.Entry<Integer, Arestas> o1,
                               Map.Entry<Integer, Arestas> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // Coloca os dados da Lista em um HashMap.
        HashMap<Integer, Arestas> arestas_ordenadas = new LinkedHashMap<Integer, Arestas>();
        for (Map.Entry<Integer, Arestas> aa : list) {
            arestas_ordenadas.put(aa.getKey(), aa.getValue());
        }
        // Retorna o HashMap com as arestas ordenadas.
        return arestas_ordenadas;
    }
```

### 3.1	Algoritmo de Kruskal utilizando Union-Find
Com a utilização do Union-Find, a Árvore Geradora Mínima é dada por um processo de unificação onde em cada iteração pega-se uma aresta da lista de arestas ordenadas do grafo e aplica-se o método Find que verifica se as extremidades da aresta pertencem à subárvores diferentes (aresta não gerou um ciclo), se sim, essas subárvores são combinadas pelo método Union, e a aresta é adicionada à resposta. 
Depois de iterar sobre todas as arestas, todos os vértices pertencerão à mesma subárvore e a Árvore Geradora Mínima será dada. Os métodos utilizados para essa implementação se encontram abaixo.

```
int find(HashMap<Integer,subset> subsets, int i)
    {
        // encontra a raiz e a torna pai do vértice i
        if (subsets.get(i).parent != i) // se o pai do vértice i não for ele mesmo
            subsets.get(i).parent = find(subsets, subsets.get(i).parent); // Procura o pai do pai di vértice i

        return subsets.get(i).parent;
    }

    // Método para juntar duas subárvores em uma única subárvore
    void Union(HashMap<Integer,subset> subsets, int x, int y)
    {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        // Anexa uma árvore de classificação/profundidade menor sob a raiz da árvore mais profunda.
        if (subsets.get(xroot).rank < subsets.get(yroot).rank)
            subsets.get(xroot).parent = yroot;
        else if (subsets.get(xroot).rank > subsets.get(yroot).rank)
            subsets.get(yroot).parent = xroot;

        // Se a classificação das raízes forem iguais
        // Faz com que uma delas seja a raiz da árvore unificada e tenha sua classificação aumentada
        else {
            subsets.get(yroot).parent = xroot;
            subsets.get(xroot).rank++;
        }
    }

    // Método para encontrar a Árvore Geradora Mínima pelo algoritmo de Kruskal
    // Utiliza FindUnion para determinar se a aresta entra na árvore a cada iteração
    void MST_Kruskal_FindUnion()
    {
        Graph mst = new Graph();// Árvore Geradora Mínima que se-inicia vazia.
        int i = 0, pesoSoma = 0;
        // HashMap para amazenar o subset de cada vértice:
        HashMap <Integer,subset> subsets = new HashMap<Integer,subset>();

        for(Vertex v: this.vertex_set.values()){
            subsets.put(v.id, new subset());
        }

        // Cada vértice recebe seu próprio id como pai e 0 como sua classificação
        for (Vertex v: this.vertex_set.values())
        {
            subsets.get(v.id).parent = v.id;
            subsets.get(v.id).rank = 0;
        }


        Map<Integer, Arestas> sorted = sortByValue(arestas_set); // ardena as arestas em ordem crescente de peso

        for(Arestas l : sorted.values())// Para cada aresta da de menor peso para maior
        {

            int x = find(subsets, l.v1.id);// Utiliza o método find para descobrir
                                           // a raiz da subárvore a qual v1 pertence
            int y = find(subsets, l.v2.id);// Utiliza o método find para descobrir
                                           // a raiz da subárvore a qual v2 pertence

            // Se as raízes encontradas forem diferentes, os vértices que formam a aresta
            //  não pertencem a mesma subárvore, dessa forma não há ciclos
            // Assim, as subárvores são unidas pelo método union e
            //  e a aresta entra na Árvore Geradora Mínima
            if (x != y) {
                mst.add_aresta(i, l.v1.id, l.v2.id, l.peso);// Aresta é adicionada ao hashmap de arestas da árvore
                mst.add_aresta(i, l.v2.id, l.v1.id, l.peso);
                i++;
                Union(subsets, x, y);// Une as árvores, acresscentando a aresta
            }


        }
        for (Arestas l: mst.arestas_set.values()) {
            pesoSoma += l.peso; //peso da árvore recebe a soma dos pesos de suas arestas
        }
        mst.print_MST(pesoSoma);


    }
```

### 3.2	Algoritmo de Kruskal utilizando Busca em Profundidade
Com a utilização da Busca e Profundidade, a Árvore Geradora Mínima é dada por um processo de adição e remoção de arestas. Em cada iteração pega-se uma aresta da lista de arestas ordenadas do grafo e a adiciona à Árvore Geradora Mínima, verifica-se então se a Árvore permanece acíclica através de uma busca em profundidade na árvore, se sim, a aresta permanece, se não, é removida. 
Abaixo é possivel observar a implementação para esse código.

```
public void MST_Kruskal_DFS() {

        Graph mst = new Graph();// Árvore Geradora Mínima que se-inicia vazia.
        // Graph avg_ofc = new Graph();
        Integer pesoSoma = 0; // Utilizada para obter a soma dos pesos das arestas da árvore.

        Map<Integer, Arestas> sorted = sortByValue(arestas_set);// Mapa com as arestas do grafo ordenadas.


        // Em cada iteração:
        //  Adiciona-se à árvore a aresta de menor peso das que ainda não foram adicionadas
        //  Verifica-se se o árvore permaneceu-se acícilica por meio de uma busca em profundidade na árvore
        //  Se não, a aresta é removida juntamente aos vértices que foram adicionadas na iteração
        //  Se sim, a aresta permanece na árvore e inicia-se uma nova iteração ou termina caso seja a última aresta
        for (Arestas l : sorted.values()) {
            int id_aresta = 0;
            Boolean addv1, addv2;//variáveis para indicar se os vértices foram adicionados na iteração ou não

            addv1 = add_vertex_mst(l.v1.id); //Verifica se os vértices foram adicionados nesse momento
            addv2 = add_vertex_mst(l.v2.id); // pois se sim, seão retirados caso a aresta gere um grafo cíclico.

            mst.add_aresta(id_aresta++,l.v1.id, l.v2.id,l.peso); // Aidiciona a aresta
            mst.add_aresta(id_aresta++,l.v2.id, l.v1.id,l.peso);

            // Se com a nova aresta o grafo permanecer acícilo, a aresta permance.
            //  E a soma dos pesos recebe o peso da aresta.
            if (mst.is_acyclic()) {
                pesoSoma += l.peso;

            } else {
                // System.out.print(" - O grafo contém ciclo!!\n");

                // A Aresta não entra na árvore, pois o grafo ficou cíclico
                if (addv1) mst.del_vertex(l.v1.id); //remove o vértice caso ele tenha entrado nessa iteraçao
                if (addv2) mst.del_vertex(l.v2.id); //remove o vértice caso ele tenha entrado nessa iteraçao
                mst.del_arc(l.v1.id, l.v2.id, id_aresta--);      //remove o arco
            }
        }

        mst.print_MST(pesoSoma);

    }
```





