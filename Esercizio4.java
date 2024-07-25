//Nome: Nicola   Cognome: Tinari     matricola: 1078643       e_mail: nicola.tinari@studio.unibo.it
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;

public class Esercizio4{
    int n;      // numero di nodi nel grafo
    int m;      // numero di archi nel grafo   
    Vector< LinkedList<Edge> > adjList; // lista di archi adiacenti ad ogni nodo i, l' indice del vettore corrisponde l' id del nodo
    int source; // nodo sorgente
    int destination; //nodo destinazione
    LinkedList<LinkedList<Integer>> p;    // array di predecessori
    double[] d; // array di distanze dalla sorgente
    LinkedList<Edge> sp; //array di archi appartenenti all' albero dei cammini minimi
    int count_cammini; //conta il numero di cammini minimi da un nodo sorgente ad un nodo destinazione(aventi lo stesso costo)
    
    //ogni nodo è identificato da un numero id e una distanza dal nodo sorgente
    private class Node{
        int id;
        double d_sorgente;
        public Node(){
            this.id=0;
            this.d_sorgente=0;
        }
        public Node(int n, double d){
            this.id=n;
            this.d_sorgente=d;
        }
        public int getID(){
            return this.id;
        }
        public double getd(){
            return this.d_sorgente;
        }
        public void setd(double d_new){
            this.d_sorgente=d_new;
        }
    }
    //ogni arco è identificato da un nodo di partenza, un nodo di arrivo e un peso
    // e siccome l' algoritmo di Dijsktra vuole che i pesi degli archi siano positivi dobbiamo effetturare un piccolo controllo
    private class Edge {
        final int src;
        final int dst;
        final double w;

        public Edge(int src, int dst, double w)
        {
            
            assert(w >= 0.0);
            
            this.src = src;
            this.dst = dst;
            this.w = w;
        }
        public String toString(){
            return src+"\t"+dst+"peso:\t"+w+"\n";
        }
    }  
    //la prima cosa da fare è leggere il grafico da file con il metodo readGraph()
    public Esercizio4(String inputf){
        this.sp=new LinkedList<Edge>();
        readGraph(inputf);
    }
    //dovendo stampare tutti i cammini da un nodo sorgente a tutti gli altri, il metodo print_path() sarà
    //scritto in un ciclo for così da poter guardare tutti i nodi dst 
    //(solo per motivi estetici nella stampa l' id di ogni nodo sarà incrementato di un +1 dato che i nodi nel grafo
    //disegnato partono da 1 e non da 0, il funzionamento dell' algoritmo non cambia)
    public void print_paths()
    {
    
        System.out.println("Source = " + (source+1));
        System.out.println();
        System.out.println("   s    d         dist path");
        System.out.println("---- ---- ------------ -------------------");
        for (int dst=0; dst<n; dst++) {
            this.destination=dst;
            System.out.printf("%4d %4d %12.4f ", source+1, dst+1, d[dst]);
            this.count_cammini=0;
            print_path(this.destination);
            System.out.println();
            
        }
        
        
    }
    //il metodo readGraph serve per creare un grafo leggendo gli elementi del file in input
    //ed essendo un grafo non orientato un arco (u,v) è considerato sia entrante che uscente
    //cioè  dato un arco che collega il nodo 'u' al nodo 'v'
    //avremo sia un arco (u,v) che un arco (v,u) aventi lo stesso peso.
    //In altre parole questo metodo legge il grafo convertendolo anche in un grafo orientato
     public void readGraph(String inputf)
    {
        Locale.setDefault(Locale.US);
        
        try {
            Scanner f = new Scanner(new FileReader(inputf));
            n = f.nextInt();
            m = f.nextInt();
            f.nextLine();
            adjList = new Vector< LinkedList<Edge> >();
            for(int j=0; j<n; j++)
            adjList.add(j, new LinkedList<Edge>());
            for(int i=0; i<m;i++){
             int src=f.nextInt();
             int dst=f.nextInt();
             double w=f.nextDouble();
             Edge e=new Edge(src, dst, w);//essendo archi bidirezionali l' arco (u,v) e l' arco (v,u) hanno lo stesso peso, ma sono considerati archi diversi
             Edge e1=new Edge(dst,src,w);
             adjList.get(src).add(e);
             adjList.get(dst).add(e1);
            }
           
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }   
    }

    //il metodo print_path serve a ripercorrere i nodi predecessori così da poter stampare e ricostruire il cammino 
//dal nodo source a dst, si fermerà nel momento in cui si incontra il nodo this.destination.
//Se un  nodo non ha nessun predecessore, e non ancora incontriamo il nodo this.destination, 
//vuol dire che da dal nostro nodo source il nodo destination non è raggiungibile
//analizzando tutti i predecessori(un nodo può averne più di uno) riusciamo a ricostruire tutti i cammini minimi e
//se abbiamo più di un predecessore allora vuol dire che abbiamo più di un cammino minimo.
//Il metodo print path sarà chiamato ad ogni iterazione passando in input però il predecessore
// del nodo corrente così da ricostruire tutto il cammino.
    public void print_path(int dst){
        if (dst == source && this.destination!=source) 
            System.out.print(dst+1);
        else if (dst == source && this.destination==source){ //si verifica solo nel caso un nodo tenti di raggiungere se stesso
            System.out.printf("%4d  CAMMINO MINIMO numero 1\n", source+1);
        }
         else if (p.get(dst).size()== 0)
            System.out.print("Irraggiungibile");
        else {
     
            for(int i=0; i<p.get(dst).size(); i++){ 
                if(i>=1)                         
                System.out.printf("%4d %4d %12.4f ", source+1, dst+1, d[dst]);
            print_path(p.get(dst).get(i));    
            System.out.print("->" + (dst+1));
           
            //Appena ricostruito un cammino completo il numero dei cammini sarà incrementato 
            //così da tenerne il conto nel caso ce ne fosse più di uno
            if(dst==this.destination){ 
                this.count_cammini=this.count_cammini+1;
            System.out.print("\tCAMMINO MINIMO numero "+"\t"+(this.count_cammini)+"\n");
            }
            }
            
        
        
        }
    }

//questo metodo andrà a trovare il nodo avente la distanza minima dal nodo sorgente e a toglierlo dalla lista
    public Node find_delete_Min(LinkedList<Node> l){
        Node min=l.get(0);                 
        for(int i=1; i<l.size(); i++){
        if(l.get(i).getd()<min.getd())
        min=l.get(i);
        }
        l.remove(min);
        return min;
    }

//dati l' id di un nodo, la lista dei nodi ancora da visitare e una nuova  distanza dal nodo sorgente(minore di quella precedente)
//questo metodo andrà a cambiare quest' ultima direttamente nella coda dove sono presenti i nodi non ancora visitati
    public void changePrio(LinkedList<Node> l,double d_new, int id){
    for(int i=0; i<l.size(); i++){           
            if(l.get(i).getID()==id)        
            l.get(i).setd(d_new);
        }
    }
    
   
 //questo metodo shortestPaths() serve a trovare il cammino minimo tra un nodo s e tutti gli altri nodi.
    //Ci occorrerranno un vettore per tenere traccia delle distanze d[]
    //una lista di liste per tenere traccia dei predecessori p=LinkedList<LinkedList<Integer>>
    //(se trovo un nuovo percorso che minimizza il cammino di costo minimo tra u e v allora i vecchi predecessori
    //inseriti per quel cammino andranno eliminati così da non avere più informazioni relative 
    //al/ai vecchio/i cammini di costo maggiore)
    //così da poter tenere traccia di piu'
    //di un predessore nel caso in cui ci fosse più di un cammino di minimo da un nodo u ad un nodo v
    //e una lista in cui andremo a inserire ed estrarre tutti i nodi raggiungibili q=LinkedList<Node>
    //Dijkstra per memorizzare gli archi di un cammino minimo usa un Minheap
    //nel nostro caso useremo invece una lista dove con deleteMin() sarà estratto il nodo con la minore distanza dal nodo sorgente
    // e con changePrio() andremo ad aggiornare il nodo specificato con il nuovo valore inserito
    public void shortestPaths( int s )
    {
        this.source=s;
        Edge[] sp_edges = new Edge[n]; 
        boolean[] visited = new boolean[n]; //vettore dove vengono marcati i nodi visitati


        LinkedList<Node> q = new LinkedList<Node>(); 
         
	  d = new double[n]; //vettore delle distanze degli n nodi dal nodo sorgente

       
        p = new LinkedList<LinkedList<Integer>>();
        
        
    //inizialmente le distanze sono settate a infinito per i nodi non ancora visitati
    //e ogni volta che troverò un percorso la cui sistanza è minore di quella trovata in precedenza andrò ad aggiornare 
    //i valori sia nella lista q con changeprio(...) che nel vettore d
    //riempiamo anche la nostra lista q tenendo traccia dei vari nodi e della loro distanza dal nodo sorgente
    //la distanza tra un nodo sorgente e se stesso sarà chiaramente 0
        Arrays.fill(d, Double.POSITIVE_INFINITY);
        Arrays.fill(visited, false);
     d[s] = 0.0;
      visited[s]=true;
        for (int v=n-1; v>=0; v--) {
           q.addFirst(new Node(v,d[v])); 
            p.addFirst(new LinkedList<Integer>()) ;//inizialmente non  avrò nessun predecessore
        }
 
//il ciclo terminerà quando non avrò più nodi da visitare, quindi quando la mia lista q sarà vuota.
//fin quando la lista contenente i nodi da visitare e le loro distanze dalla sorgente q non è vuota
//estraiamo ad ogni ciclo il nodo avente la distanza dal nodo sorgente minore marchiandolo nel vettore visited[], 
//adesso è come se facesse parte dell' albero di costo minimo.
//Ad ogni ciclo vado ad esaminare ogni nodo raggiungibile, con archi diretti, dal nodo che ho appena estratto (edge(u,v))
//se il nodo 'v' non è mai stato visitato e il cammino da sorgente a nodo'u'+arco(u,v) è minore della
//distanza registrata in precedenza d[v] allora:
//vado ad aggiornare la distanza tra il nodo sorgente e il nodo 'v' anche nella mia lista q e in d[v],
//cancello ogni traccia dei predeccosi del cammino precedente più costoso
//aggiornandolo con i nuovi valori trovati.
      
    while (!q.isEmpty()) { 
            final Node u = find_delete_Min(q); 
	    visited[u.getID()] = true;  
            if (sp_edges[u.getID()] != null) {
                sp.add(sp_edges[u.getID()]);
            }
            for (Edge e : adjList.get(u.getID())) {
		 int v = e.dst;
	        if (!visited[v] && (d[u.getID()] + e.w < d[v])) {
                    d[v] = d[u.getID()] + e.w;       
                    changePrio(q, d[v], v);
                    p.get(v).clear();
                    p.get(v).add(u.getID());
                    sp_edges[v] = e; 
                    
            }
             //se riesco a trovare un cammino dal nodo sorgente al nodo 'v'
    //diverso da quello che avevo già trovato in precedenza, ma uguale a livello di costo, significa che esiste un' altro cammino che minimizza
    //la distanza tra il nodo sorgente e il nodo 'v' 
    // il nodo u sarà inserito nella lista dei predecessori nella posizioe p.get(v)
            else  if (!visited[v] && (d[u.getID()] + e.w == d[v])) {
            p.get(v).add(u.getID());  
            sp_edges[v] = e; 
}
	    }
	}
    }

    public static void main(String[]args){
        
        long start_t = System.currentTimeMillis();
        Esercizio4 sp = new Esercizio4(args[0]);
        for(int s=0; s<sp.n; s++){
        sp.shortestPaths(s); //the source is the node whose id is zero
        sp.print_paths();
        }
        long end_t = System.currentTimeMillis();

//questo blocco di codice mi serve per calcolare e stampare il tempo impiegato per eseguire l' algoritmo, mi basterà fare la differenza tra il tempo
//di partenza e il tempo in cui è terminato
      long elapsed = (end_t - start_t);
      long min = elapsed / (60*1000);
      double sec = (elapsed -min*60)/1000.0;
      System.out.println("Tempo impiegato per trovare la soluzione: "+min+" min "+sec+" sec");

    }
        
    }
    //2)CALCOLO DEL COSTO, dato un numero di archi m e un numero di nodi n
    //per inizializzare il grafo con i vari archi e i vari nodi, ci vorrà un costo  O(m) per leggere tutti gli archi
    //e inizializzare tutti i nodi.
    //Per trovare il cammino minimo da un nodo sorgente ai vari nodo destinazione bisogna svolgere il metodo shortestPaths(int s)
    //inizio con l'  inserire tutti i nodo e gli attributi nelle opportune liste e vettori p,d,q e visited 
    //il  costo di inserimento di un nodo è  O(1) per d e visited 
    //per p e q, essendo linked List , l' inserimento in testa avrà un costo O(1), 
    // ed essendo ripetuti n-volte tutti questi inserimenti 
    //il costo di inizializzazione sarà pari a: O(n)+O(n)=O(n).
    //Il ciclo while sarà eseguito al più n-volte, dato che nella lista saranno inseriti al massimo n-nodi 
    // e  il metodo find_delete_Min ha costo lineare O(n), i suo costo sarà O(n^2).
    // Il metodo changePrio() di costo lineare O(n) sarà eseguito invece al più m-volte(dovendo analizzare m-archi),O(m*n).
   // il costo totale del metodo shortestPaths(Node source) sarà quindi O(m*n).
    //Per il metodo Print_Path(int dest) invece il costo sarà O(n).
    //Invece nel metodo Print_Paths il metodo Print_Path viene
    //richiamato n-volte e il costo di un singolo metodo Print_Paths sarà O(n^2)
    //in conclusione per trovare e per stampare tutti i cammini da un nodo source a tutti gli altri nodi 
    //sarà: O(m)+O(n)+O(n*m)+O(n^2)=O(n*m)
    //Dato che tutto questo procedimento (esclusa la lettura del grafo)
    //va eseguito per tutti gli n-nodi del grafo, il costo totale 
    //di quest' algoritmo sarà: 
    //n*O(n*m)=O(m*n^2)