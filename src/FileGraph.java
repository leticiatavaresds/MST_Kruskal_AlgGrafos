import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Scanner;


public class FileGraph {

    // Metódo para ler um grafo de um arquivo de texto
    public Graph open_text( ) {
        Scanner ler = new Scanner(System.in);
        String thisLine;
        Graph dg1 = new Graph( );
        String[] pieces;
        int cont = 0;

        System.out.print("Informe o nome do arquivo txt (arquivo deve estar na pasta myfiles):\n");
        String nome = ler.nextLine();

        try {
            FileReader file_in = new FileReader("myfiles/" + nome + ".txt");
            BufferedReader br1 = new BufferedReader( file_in );
            ArrayList<String> arestas_ja_adiciondas = new ArrayList<String>();
            while ( (thisLine = br1.readLine( )) != null) {

                // Retira excessos de espaços em branco
                thisLine = thisLine.replaceAll("\\s+", " ");
                pieces = thisLine.split(" ");

                int v1 = Integer.parseInt( pieces[0] );

                dg1.add_vertex( v1 );
                for( int i = 2; i < pieces.length; i++ ) {
                    int v2 = Integer.parseInt( pieces[ i ] );
                    i+=1;
                    int weight = Integer.parseInt( pieces[ i ] );
                    cont+=1;
                    dg1.vertex_set.get(v1).add_weight( v2, weight );

                    if(!arestas_ja_adiciondas.contains(Integer.toString(v2)+Integer.toString(v1))){
                        dg1.add_aresta(cont,v1,v2,weight);
                        arestas_ja_adiciondas.add(Integer.toString(v1)+Integer.toString(v2));
                    }

                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("Arquivo lido com sucesso. O que deseja fazer agora?\n");
        return dg1;
    }
}