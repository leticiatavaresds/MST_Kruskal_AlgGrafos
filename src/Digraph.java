import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Digraph implements Serializable {
    protected HashMap<Integer,Vertex> vertex_set; //armazena os vértices de um grafo
    protected HashMap<Integer, Graph.Arestas> arestas_set; //armazena as arestas de um grafo
    private Boolean acyclic; //Varíavel que informa se o grafo é acíclico ou não


    public Digraph() {
        vertex_set = new HashMap<Integer,Vertex>();
        arestas_set = new HashMap<Integer,Graph.Arestas>();
    }

    //Método para adicionar um vértice ao grafo
    public void add_vertex( int id ) {
        if ( this.vertex_set.get( id ) == null ) { //se ainda não existe um vértice com o id passado, cria-se o vértice
            Vertex v = new Vertex( id );
            vertex_set.put( v.id, v );
        }
    }

    // Método para adicionar um vértice à árvore geradora.
    // Se diferencia do método acima pois retorna se de fato criou ou vértice ou não,
    // Informação usada para se caso a aresta adicionada em uma iteração do algoritmo de Kruskal formar um ciclo
    // Rrmover o vértice junto com a aresta somente se ele foi criado na iteração.
    public boolean add_vertex_mst( int id ) {
        Boolean adc = false;

        if ( this.vertex_set.get( id ) == null ) {
            Vertex v = new Vertex( id );
            vertex_set.put( v.id, v );
            adc = true;
        }
        return adc;

    }

    // Método para adicionar uma aresta ao grafo, a adicionando ao HashMap "arestas_set".
    public void add_aresta(Integer id_aresta, Integer id1, Integer id2, Integer peso) {
        this.add_vertex( id1 );
        Vertex v1 = vertex_set.get(id1);
        this.add_vertex( id2 );
        Vertex v2 = vertex_set.get(id2);
        v1.add_neighbor( v2 );

        v1.add_weight(id2,peso);
        v2.add_weight(id1,peso);

        Graph.Arestas a = new Graph.Arestas();
        arestas_set.put(id_aresta, a);
        a.v1 = v1;
        a.v2 = v2;
        a.peso = peso;

    }


    // Método para deletar uma aresta utilizado nas iterações no método Kruskal
    // pois em cada iteração é adicionada uma aresta na árvore e se ela causar um ciclo, deve ser retirada.
    public void del_arc( Integer id1, Integer id2, Integer id_aresta) {
        Vertex v1 = vertex_set.get(id1);
        Vertex v2 = vertex_set.get(id2);
        this.arestas_set.remove(id_aresta);
        v1.nbhood.remove( v2.id );
        v2.nbhood.remove( v1.id );
    }

    // Método para deletar um vértice em um grafo
    //  é utilizao em cada iteração do método Kruskal apenas se a aresta acrescentada formar um ciclo
    //  e o vértice tiver sido adicionado na própria iteração.
    //  Esse método primeiro procura todos os vértices que tem o
    //  vértice id como vizinho e o tira de cada vizinhança; e depois
    //  remove o próprio vértice.
    public void del_vertex( int id ) {

        for ( Vertex v1 : vertex_set.values()){
            v1.nbhood.remove( id );
        }
        vertex_set.remove(id);
    }

    // Método para calcular o grau máximo de um grafo.
    public int max_degree() {

        int max = -1;
        for( Vertex v1 : vertex_set.values() ) {
            if( v1.degree() > max )
                max = v1.degree();
        }
        return max;
    }


    // Método para verificar se um grafo é acíclico por meio de uma busca em profundidade.
    public boolean is_acyclic( ) {
        DFS( null );
        return acyclic;
    }


    // Método para realizar uma busca em profundidade em um grafo com a finalidade de identificar ciclo.
    // A ideia é percorrer tod o grafo marcando os vértices visitados,
    // Se visitar um mesmo vértice duas vezes, o grafo possui ciclos, sendo assim não acícliclo.
    // Utiliza o método "DFS_visit" para as visitas
    public void DFS( List<Vertex> ordering ) {

        ArrayList <String> arestas_marcadas = new ArrayList<String>();//Armazenar as arestas já percorridas


        if( ordering == null ) {
            ordering = new ArrayList<Vertex>( );
            ordering.addAll( vertex_set.values( ) );
        }
        for( Vertex v1 : vertex_set.values() )
            v1.d = null;

        acyclic = true;

        for( Vertex v1 : ordering )
            if( v1.d == null ) {
                v1.d = 0; //Marca o vértice como visitado
                DFS_visit(v1,arestas_marcadas);
            }
    }

    // Método utilizado pela "DFS" para as visitas aos vizinhos de cada vértice ainda não visitado.
    private void DFS_visit( Vertex v1, ArrayList <String> a_m ) {

        // Caso queira ver as visitas realizadas na busca em cada iteração de kruskal, retirar o comentário abaixo
        /*System.out.println("Visitando:" + v1.id);


        for (Vertex v: v1.nbhood.values()){
            System.out.println("vizinhos:" + v.id);
        }*/

        for( Vertex neig : v1.nbhood.values( ) ) {
            if( neig.d == null ) {
                a_m.add(Integer.toString(v1.id)+Integer.toString(neig.id));//Marca a aresta como percorrida
                v1.d = 0; // Marca o vértice como visitado
                DFS_visit( neig, a_m );
            }
            else if (!a_m.contains(Integer.toString(neig.id)+Integer.toString(v1.id)))
                // Verica-se se a aresta neig-id foi percorrida;
                // Pois se sim, neig ter sido visitado não implica em um ciclo
                // Por exemplo, começa-se pelo vértice 1 que tem como vizinho o 2
                //  Visita-se o 2 que tem como vizinho o 1, o 1 ter sido visitado
                //  não implica no grafo ser ciclíco.
                acyclic = false;
        }
    }

    // Método para printar um grafo na tela.
    public void print() {
        System.out.println("\n----------------------------------------------------");
        System.out.println("********************** Grafo **********************");
        System.out.println("----------------------------------------------------");

        if( this.vertex_set.size() == 0 ) {
            System.out.print("\n\n Conjunto de vértices vazio");
            System.out.println("----------------------------------------------------");
            return;
        }

        for( Vertex v : vertex_set.values())
            v.print();

        System.out.printf("\n\n Grafo, grau máximo %d", this.max_degree());

        System.out.println("\n----------------------------------------------------\n\n");
    }

    // Método para printar na tela a Árvore Geradora Mínima gerada pelo método Kruskal
    public void print_MST(Integer Soma) {
        System.out.println("\n----------------------------------------------------");
        System.out.println("*************** Árvore Geradora Mínima **************");
        System.out.println("----------------------------------------------------");

        for( Vertex v : vertex_set.values())
            v.print();

        System.out.printf("\n\nGrau máximo da MST: %d", this.max_degree());
        System.out.println("\nSoma dos pesos das arestas da MST: " + Soma );

        System.out.print("\n----------------------------------------------------\n\n");
    }

}