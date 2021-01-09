import java.io.Serializable;
import java.lang.Comparable;
import java.util.HashMap;

public class Vertex implements Serializable, Comparable<Vertex> {
    protected Integer id;
    protected HashMap<Integer,Vertex> nbhood; // Utilizado para armazenar a vizinhança de cada vértice
    public HashMap<Integer,Integer> arc_weights; // Utilizado para armazenar os pesos das arestas de cada vértice
    protected Integer d, f;

    // Tod vértice possui as seguintes informações:
    //      Seu id, Sua vizinhança e o peso das suas arestas.
    public Vertex ( int id ) {
        this.id = id;
        nbhood = new HashMap<Integer,Vertex>();
        arc_weights = new HashMap<Integer,Integer>( );
    }

    @Override public int compareTo( Vertex otherVertex ) {
        if( otherVertex.f > this.f)
            return 1;
        else
            return -1;
    }

    // Método para adicionar vizinho ao vértice
    public void add_neighbor( Vertex viz ) {
        nbhood.put(viz.id, viz);
    }

    // Método para adicionar peso à aresta
    public void add_weight( Integer id_nb, Integer weight ) {
        arc_weights.put( id_nb, weight );
    }

    // Método para encontrar o grau do grafo
    public int degree() {
        // grau de saída se direcionado
        return nbhood.size();
    }


    public void print() {
        System.out.print("\nId do vértice: " + id + ", Vizinhança: " );
        for( Vertex v : nbhood.values()) {
            System.out.print(" " + v.id);
            System.out.print(" " + "peso:" + arc_weights.get(v.id));
        }

    }
}