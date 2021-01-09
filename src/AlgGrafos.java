import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class AlgGrafos {

    static Graph dg1;
    static boolean goon = true;


    public static void main(String[] args) {
        dg1 = new Graph();
        main_menu( );
    }

    //Menu Interativo
    private static void main_menu( ) {

        Scanner scan1 = new Scanner(System.in);


        String line1 = " \n 0 Sair \n 1 Entrada por arquivo de texto \n 2 Print do Grafo  ";
        String line2 = " \n 3 Escrever em Arquivo \n 4 Excluir vértice \n 5 Árvore Geradora Mínima \n\n";
        String menu = line1 + line2;

        System.out.print("\nSelecione o que deseja fazer:");
        while( goon ) {
            System.out.print( menu );
            int choice = scan1.nextInt();
            switch (choice) {
                case 0 -> goon = false;

                case 1 -> {
                    FileGraph fg1 = new FileGraph();
                    dg1 = fg1.open_text();
                }

                case 2 -> {
                    dg1.print();
                    seg_menu();
                }

                case 3 -> {
                    write(dg1);
                    seg_menu();
                }

                case 4 -> {
                    System.out.print("Vértice a excluir: ");
                    int id = scan1.nextInt();
                    dg1.del_vertex(id);
                    seg_menu();
                }
                case 5 -> {
                    dg1.MST_Kruskal_DFS();
                    dg1.MST_Kruskal_FindUnion();
                    seg_menu();
                }

                default ->
                    System.out.print("Opção Inválida. Selecione uma das opções abaixo: \n");
            }
        }
    }

    private static void seg_menu() {
        System.out.println(" Deseja fazer mais alguma coisa?\n");
        System.out.println(" 1 - Sim \n 0 - Não, Sair.\n");
        Scanner scan1 = new Scanner(System.in);
        int opcao = scan1.nextInt();
        switch (opcao){
            case 0 ->
                goon = false;

            case 1 -> System.out.println(" O que deseja fazer agora?");

            default -> System.out.print("Opção Inválida. Selecione uma das opções abaixo: \n");

        }
    }

    // Método para escrever grafo em um arquivo
    private static void write( Digraph g1 ) {
        try {
            FileOutputStream arquivoGrav = new FileOutputStream("myfiles/saida.dat");
            ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav);
            objGravar.writeObject( g1 );
            objGravar.flush();
            objGravar.close();
            arquivoGrav.flush();
            arquivoGrav.close();
            System.out.println("Objeto gravado com sucesso!");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}