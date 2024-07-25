//Nome: Nicola   Cognome: Tinari     matricola: 1078643       e_mail: nicola.tinari@studio.unibo.it

//questo codice si occuperà di smistare i vari n-file  nel minor numero di dischi possibile.
//Per fare ciò dobbiamo massimizzare lo spazio occupato all' interno di ogni disco.
//Il metodo smistamento() allocherà x-file nel primo disco in modo da manimizzare lo spazio libero in quest' ultimo,
// con i rimanenti (n-x)-file il metodo 
//smistamento() dovrà trovare di nuovo la miglior combinazione da allocare nel secondo disco(se serve), 
//poi anche nel terzo (se serve)e così via finchè tutti i file non saranno smistati senza essere smezzati,
// Ricordiamoci che  l' obbiettivo è utilizzare meno dischi possibili.
//Per risolvere questo problema utilizzeremo un procedimento simile al problema dello zaino, immaginando che il mio zaino sia un singolo disco,
// la capienza massima sia pari a 650(MB) e invece di trovare la combinazione che ci dà il valore massimo dobbiamo
//trovare la combinazione che ci darà il peso massimo(in MB) senza ovviamente eccedere dal vincolo di 650 MB.
//Andremo a ripetere il procedimento fino a non avere più file da allocare.


//Mi servirò di una lista in cui saranno riportati i nomi dei file ancora da allocare in memoria denominata files_memoria
//e una lista dove saranno riportati i pesi dei file ancora da allocare pesi_files.
//Nella posizione i-esima del vettore pesi_files corrisponderà il peso dell' i-esimo file del vettore files_memoria.

//La richiesta è di allocare tutti i file all' interno di un numero minimo di dischi, quindi 
//i miei sottoproblemi  P(i,j) sono massimizzare lo spazio occupato(quindi alla fine cercare di avere il meno
// spazio libero possibile) con i primi i-file disponibili e avendo una capacità massima 'j' MB.
//Per aiutarci ci serviremo di una matrice mat[n][651] che andremo a riempire con i pesi totali degli insiemi trovati
//con primi i-file(un insieme può essere vuoto così come può comprendere tutti gli i-file) 
//che massimizzano lo spazio occupato.

//la soluzione S(i,j) sarà determinare il peso della migliore combinazione dei primi i-file che massimizza lo spazio
// occupato, ma la somma dei pesi di tutti i k-file (k<=i) dev' essere minore o uguale dello spazio a disposizione j.
//Per tenere traccia della soluzione useremo una matrice booleana use[n][651] dove use[i][j] assumerà valore true solo
//nel caso in cui l' i-esimo elemento faccia parta della soluzione al problema P(i,j)


//casi Base
//(ho solo un file disponibile) se i==0 e j>=p[i] allora mat[i][j]=p[i]   altrimenti se j<p[i]   mat[i][j]=0
//(non ho spazio a disposizione) se j==0 allora mat[i][j]=0

//caso Generale
//(il peso dell' i-esimo elemento è troppo per la nostra capacità, allora useremo la soluzione trovata solo con gli elementi precedenti)
//  se  j<p[i]  mat[i][j]=mat[i-1][j]
//(il peso dell' i-esimo elemento è minore della nostra capacità massima, ma sarà inserito nell' insieme 
//solo se il suo peso massimizza lo spazio occupatao, rispettando sempre il vincolo dell capacità disponibile j)
// se j>=p[i] e se mat[i-1][j-p[i]]+p[i]>mat[i-1][j]  allora  mat[i][j]=mat[i-1][j-p[i]]+p[i]
//altrimenti   mat[i][j]=mat[i-1][j]

//per la tracciare i file(indicati dall' indice 'i') che fanno parte della mia soluzione S(i,j) userò una matrice use[][], tale che:
//se i==0 e j>=p[i] allora use[i][j]=true
//se j>=p[i] e se mat[i-1][j-p[i]]+p[i]>mat[i-1][j]  allora use[i][j]=true
//altrimenti use[i][j]=false

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

public class Esercizio3{
    int n_file;  
    LinkedList<String> files_memoria; 
    LinkedList<Integer> pesi_files;  
    int n_dischi;
    
//una cd_rom avrà al suo interno una lista di file, un id e un numero intero che segna lo spazio ancora disponibile al suo interno
//e rappresnta un nostro generico disco   
private class cd_rom{  
        int id;
        LinkedList<String> files;
        int spazio_libero;
        LinkedList<Integer> pesi;

        public cd_rom(int id){
       this.id=id;
       this.spazio_libero=650;//ogni cd_rom avrà inizialmente 650 MB di spazio disponibile
       this.files=new LinkedList<String>();
       this.pesi=new LinkedList<Integer>();
        }
//questo metodo serve ad aggiungere il nome di un file e il suo rispettivo peso nelle liste nome_file e peso_file
//ovviamente al file dell' i-esima posizione della lista nome_file corrisponderà il peso dell' i-esima posizione di peso_file
        public void add(String nome_file, int peso_file ){
            if(peso_file<this.spazio_libero){
            this.files.add(nome_file);
            this.pesi.add(peso_file);
            this.spazio_libero=this.spazio_libero-peso_file;
            }
        }
       public String toString(){ //il metodo toString() serve solo per  la stampa
            String s="";
            for(int i=0; i<this.files.size(); i++){
             s=s+this.files.get(i)+"\t"+this.pesi.get(i)+"\n";
            }
            return "Disco\t"+this.id+"\n"+s+"Spazio Libero :\t"+this.spazio_libero+"\n";
        }
    }
    public Esercizio3(String inputf){
        read_input(inputf);
    
        }
        //questo metodo readInput(String inputf) leggendo il file passato in input
        // andrà ad inserire i valori nelle rispettive liste nome_file e peso_file, 
        //assicurandosi ovviamente che nessun file pesi più di 650 MB
    public void read_input(String inputf){ 
        Scanner scan;
        try {
           scan=new Scanner(new FileReader(inputf));
           this.n_file=scan.nextInt();
           assert(n_file>=1);
           
           scan.nextLine();
           this.files_memoria=new LinkedList<String>();
           this.pesi_files=new LinkedList<Integer>();
           for(int i=0; i<this.n_file; i++){
            String riga=scan.nextLine();
            
            String[] parole=riga.split(" ");
            this.files_memoria.addFirst(parole[0]);
        int peso=Integer.parseInt(parole[1]);
        assert(peso>650);
                 this.pesi_files.addFirst(peso);  
           }
           
           
        } catch (FileNotFoundException e) {
           
            e.printStackTrace();
        }
        n_dischi=1;
        
         smistamento();
    }

   //Il metodo smistamento() si occuperà di trovare le miglior combinazioni tra i file contenuti in this.memoria
   // da allocare nei vari dischi.
    //Dato che this.memoria raprresenta i file che non sono ancora stati allocati, il metodo
    //si fermerà nel memomento in cui tutti i file saranno allocati su un disco(cioè finchè il vettore
    //this.files_memoria non sarà svuotato).
    //Cominciamo con inizializziare la matrice con dimensioni 'j' pari alla capacità di un disco (650)
    // e 'i' invece il numero di file da allocare.
    //La matrice use[][] invece mi servirà per tenere traccia degli elementi che appartengono alla mia soluzione,
//quindi se l' i-esimo elemento appartiene alla soluzione del problema P(i,j) allora use[i][j]=true
     public void smistamento(){
     

        while(!this.files_memoria.isEmpty()){ 
        cd_rom cd=new cd_rom(n_dischi);
        int[][] mat=new int[this.files_memoria.size()][651];  

        boolean[][] use=new boolean[this.files_memoria.size()][651];
        
        for(int i=0; i<this.files_memoria.size(); i++){
            for(int j=0; j< mat[i].length; j++){
            if(j==0) //se non ho memoria disponibile allora nessun elemento può essere depositato al suo interno
            mat[i][j]=0;
            else if(i==0 && j<this.pesi_files.get(i))//non abbastanza memoria disponibile per inserire il primo elemento
            mat[i][j]=0;
            else if(i==0 && j>=this.pesi_files.get(i)){//ho abbastanza memoria per inserire il primo elemento ma solo il primo è disponibile nessun altro
            mat[i][j]=this.pesi_files.get(i);
            use[i][j]=true;
            }else if(j<this.pesi_files.get(i)){//l' elemento è troppo grande per entrare nella porzione di disco
            mat[i][j]=mat[i-1][j];            // quindi prenderò la soluzione di quando avevo un elemento in meno a dsiposizione, ma la stessa porzione di memoria
             
        } else if(j>=this.pesi_files.get(i) && mat[i-1][j]<=mat[i-1][j-this.pesi_files.get(i)]+this.pesi_files.get(i)){
//l' elemento i-esimo farà parte della soluzione solo se aggiungendolo al vecchio valore che comprendeva solo i primi 
//(i-1)-esimi elementi il suo peso va a massimizzare la quantità di MB presenti nel disco
             mat[i][j]=mat[i-1][j-this.pesi_files.get(i)]+this.pesi_files.get(i);
             use[i][j]=true;
              } else if(j>=this.pesi_files.get(i) && mat[i-1][j]>mat[i-1][j-this.pesi_files.get(i)]+this.pesi_files.get(i)){
                //cioè se aggiungo l' elemento i-esimo al mio insieme, data una quantità di spazio disponibile j, e il peso di quest' ultimo
                //non va a massimizzare la quantità di MB che posso inserire nel disco 
                //allora mi accontenterò del valore trovato quando avevo 'i-1' file a disposizione
            mat[i][j]=mat[i-1][j];
               
        }
            }
        }
        int i = mat.length-1;
        int j = 650;

//per capire da dove iniziare a leggere la matrice use[][], basti pensare al sotto-problem P(n,650),
//cioè la soluzione trovata avendo n-file a disposizione e 650MB di capienza massima che corrisponde proprio alla casella in basso
//a destra della mia matrice, è da lì che dobbiamo partire a leggere la matrice use[][] .  
//se use[i][j]==true allora l' i-esimo elemento può essere aggiunto nel disco perchè fa parte della mia soluzione
//quindi non mi resta che ripercorere la matrice 'use' nelle caselle dove use[i][j]==true
        while(i>=0){
            if(use[i][j]){ 
               cd.add(this.files_memoria.get(i),this.pesi_files.get(i));
                j=j-this.pesi_files.get(i); 
             }               
               i=i-1; 
            }

        //una volta creato l' insieme di elementi che massimizza lo spazio occupato nel disco andrò ad aggiungere
        // questi ultimi al mio cd-rom, identificato con un header pari al numero del disco, prima di eliminarli dalla lista di file
        //ancora da allocare così da poter ripetere il procedimento di massimizzazione dello spazio con i file 
        //rimaneti su un ulteriore disco
        this.files_memoria.removeAll(cd.files);
        this.pesi_files.removeAll(cd.pesi);
        n_dischi++;
        System.out.println(cd.toString());
    }  
      }
//per calcolare il costo asintotico di questo algoritmo analizziamo il caso peggiore in cui ho n-file grandi quanto un singolo cd rom,
//in questo caso avrò quindi bisogno di n-dischi da riempire e quindi dovrò trovare la combinazione giusta per ogni disco.
//Per trovare la combinazione giusta nella matrice dovrò fare n*650 iterazioni, quindi 650*n=O(n) 
//che sarà anche il costo per inizializzare e modificare use[][].
//e avrà un costo pari a 'i' trovare la soluzione nella matrice use[][], i=O(n).
//Avendo n-dischi,quindi dovendo fare questo procedimento al più n-volte, il costo finale dell' algoritmo sarà n*(O(n)+O(n))S
// il costo asintotico è quindi pari a O(n^2).
//Se contiamo la fase iniziale in cui il file input viene letto operazione, di costo costo sarà O(n)
// dato che dobbiamo leggere n+1 righe,
//anche per stampare il contenuto dei vari n-dischi il costo sarà lineare dato che stamperò ogni file al massimo una volta
//Alla fine il programma completato avrà un costo pari a:
//O(n^2)+O(n)+O(n)=O(n^2)
    
    public static void main(String args[]){
        Esercizio3 es=new Esercizio3(args[0]);
    }
}