//Nome: Nicola   Cognome: Tinari     matricola: 1078643       e_mail: nicola.tinari@studio.unibo.it

//Per risolvere questo problema ho usato un algoritmo di visita in ampiezza BFS, ho immaginato  le caselle
//come dei nodi e la mia schacchiera come un grafo rappresentato sotto forma di matrice. Le caselle indicate con il '.' sono i miei
//nodi da visitare, mentre quelle indicati con 'X' sono i nodi irraggiungibili. Le 8 mosse a 'L' del cavallo è come se fossero i miei 
//archi che collegano un nodo u ad un nodo v aventi tutti lo stesso peso w(u,v)=1 e biderezionali.
//La casella iniziale è come se fosse il nodo di partenza da cui iniziare la visita in ordine in base alla distanza crescente in numeri di archi
//dal nodo di partenza ad un generico nodo. Il mio obbiettivo è verificare che dalla casella di partenza tutte le altre siano raggiungibili.
//Verificare se tutte le caselle sono raggiungibili dal cavallo è un po' come verificare se esiste un albero di copertura,
// radicato nella casella di partenza, che racchiude tutti gli altri i nodi(caselle).


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
public class Esercizio2{
int n; //numero di righe
int m; //numero di colonne
char[][] scacchiera=new char[n][m]; 
list_caselle coda= new list_caselle(); 
int count_not_visited; //conta il numero di caselle che il cavallo non ha ancora visitato o dove non è presente nessun altro pezzo di scacchi
casella start; //casella di partenza

//item è il nome della classe che userò per rappresentare le mie caselle e ovviamnete saranno identificate un intero
// per il numero di riga e intero per il numero della colonna
private class casella {
        int row;
        int col;

        public casella(int start, int finish){
            this.row=start;
            this.col=finish;
        }
    }
    //la classe list_item rappresenta la coda(FIFO) che userò per visitare le varie caselle. Ho scelto una coda FIFO
    // perchè BFS come sappiamo visita i nodi partendo da quelli più vicini al nodo sorgente
    private class list_caselle{ 
        casella[] list;
        int size;
         
//per rappresentare la mia coda userò un array che ogni volta che si desidera aggiungere una casella andrà ad incrementarsi
        public list_caselle(){
            list=new casella[0];
            size=0;
           
        }
        public list_caselle(int n,int m){
            this.list=new casella[1];
            this.list[0]=new casella(n,m);
          this.size=1;
        }
        public boolean isEmpty(){
            return this.size==0;
        }
        //per aggiungere un elemento, in coda chiaramente, dovrò crearmi un array temporaneo di grandezza size+1
        // copiare tutti i primi n-elementi e aggiungere all' ultima posizione la mia nuova casella
        public void push(casella item){
            casella[] temp=new casella[size+1];
            for(int i=0; i<size; i++)
            temp[i]=this.list[i];
            temp[temp.length-1]=item;
            this.list=temp;
            size++;
        }
        //nel metodo pop, dato che l' elemento da estrarre si troverà in testa mi basterà creare un array temporaneo
        //di grandezzza size-1 e copiare tutti gli elementi eccetto il primo in questa nuova struttura, ovviamente
        //tutti nella posizione i-1(dove i rappresenta la vecchia posizione che occupavano)
        public casella pop(){
            casella item=this.list[0]; 
            casella[] temp=new casella[list.length-1];
            for(int i=1; i<list.length; i++)
            temp[i-1]=this.list[i];
            size--;
            this.list=temp;
           return item;
        }
       
    }
    public Esercizio2(String inputf){
    read_file(inputf);
    BFS();
  
    
    }
//con il metodo read_file(String inputf) la scacchiera sarà riempita in automatico con i caratteri specificati nel file
//e la scacchiera sarà rappresentata da una matrice di caratteri
 //la variabile count_not_visited è il contatore di caselle non visitate(e non occupate da un pezzo) andrà ad incrementarsi
 // ogni volta incontra un '.', al termine del procedimento mi basterà verificare se count_not_visited==0
 //per verificare se tutte le caselle sono state visitate.
 //Inizialmente il valore di questa variabile sarà pari al numero di caselle marchiate con il carattere '.'.
 //il nodo di partenza, cioè l' unico che inizialmente marchiato con 'C'
//sarà l' unico che inizialmente sarà inserito nella coda

public void read_file(String inputf){
       
        Scanner scan;
        try {
            scan = new Scanner(new FileReader(inputf));
       
     this.n=scan.nextInt();
     scan.nextLine();
     this.m=scan.nextInt();
     scan.nextLine();
     this.scacchiera=new char[n][m]; 
     this.count_not_visited=0;
     for(int i=0; i<n; i++){
        String s=scan.nextLine();
        for(int j=0; j<m; j++){
            char c=s.charAt(j);
             if(c=='.'){
            count_not_visited=count_not_visited+1;
            }else if(c=='C'){
            this.start=new casella(i, j);
            this.coda=new list_caselle(i, j);
           }
           this.scacchiera[i][j]=c;
        }
     }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    }
//In questa versione anzichè usare un vettore booleano per contrassegnare i nodi(o caselle) visitati come un algoritmo BFS
// userò direttamente una matrice di caratteri marchiando con il carattere 'C' i nodi scoperti dal mio cavallo.
//Il programma termina quando la mia coda è vuota, vuol dire che non ho più caselle (raggiungibili) ancora da visitare.
//La prima cosa da fare prima di muovere il cavallo è controllare se riesco a raggiungere una nuova casella 
//quindi controllo se le caselle di destinazione non sono mai state visitate, o non vi è presente nessun pezzo, e
//se effettivamente una di loro non è mai state esplorata sarà aggiunta in coda e marchiata con il carattere 'C'.
//Il che significa anallizzare tutte le possibili 8 mosse assicurandoci di non uscire dalla scacchiera
 //e di non andare a visitare una casella già visitata(contrassegnata con 'C') o occupata da un' altro pezzo (contrassegnato con 'X'). 
    public void BFS(){
        while(!this.coda.isEmpty()){
            casella temp=this.coda.pop();
            if( temp.row-2>=0 && temp.col-1>=0  ){
                casella new_item=new casella(temp.row-2, temp.col-1);
                if(this.scacchiera[temp.row-2][temp.col-1]=='.' ){
                this.coda.push(new_item); 
                this.scacchiera[new_item.row][new_item.col]='C';
                count_not_visited--;
                }
            }
            
            if(temp.row-2>=0 && temp.col+1<m){
                casella new_item=new casella(temp.row-2,temp.col+1);
                if(this.scacchiera[temp.row-2][temp.col+1]=='.' ){
                this.coda.push(new_item);
                this.scacchiera[new_item.row][new_item.col]='C';
                count_not_visited--;    
            }
               
            }
            if(temp.row-1>0 && temp.col+2<m){
                casella new_item=new casella(temp.row-1,temp.col+2);
                if(this.scacchiera[temp.row-1][temp.col+2]=='.' ){
                this.coda.push(new_item);
                this.scacchiera[new_item.row][new_item.col]='C';
                count_not_visited--;    
            }
                
            }
            if(temp.row+1<n && temp.col+2<m){
                casella new_item=new casella(temp.row+1,temp.col+2);
                if(this.scacchiera[temp.row+1][temp.col+2]=='.'){
                this.coda.push(new_item);
                this.scacchiera[new_item.row][new_item.col]='C';
                count_not_visited--;    
            }
            }  
            if(temp.row+2<n && temp.col+1<m){
                casella new_item=new casella(temp.row+2,temp.col+1);
                if(this.scacchiera[temp.row+2][temp.col+1]=='.' ){
                this.coda.push(new_item);
                this.scacchiera[new_item.row][new_item.col]='C';
                count_not_visited--;
                }
                
            }
            if(temp.row+2<n && temp.col-1>=0){
                casella new_item=new casella(temp.row+2,temp.col-1);
                if(this.scacchiera[temp.row+2][temp.col-1]=='.' ){
                this.coda.push(new_item);
                this.scacchiera[new_item.row][new_item.col]='C';
                count_not_visited--;
            }
                
            }
            if(temp.row+1<n && temp.col-2 >=0){
                casella new_item=new casella(temp.row+1,temp.col-2);
                if(this.scacchiera[temp.row+1][temp.col-2]=='.' ){
                this.coda.push(new_item);
                this.scacchiera[new_item.row][new_item.col]='C';
                count_not_visited--;
            }
                
            }
            if(temp.row-1>=0 && temp.col-2>=0){
                casella new_item=new casella(temp.row-1,temp.col-2);
                if(this.scacchiera[temp.row-1][temp.col-2]=='.' ){
                this.coda.push(new_item);
                this.scacchiera[new_item.row][new_item.col]='C';
                count_not_visited--;
                }
                
            }
          
        }
        //questo blocco mi serve solo per mandare a video la matrice dove sono presenti le caselle visitate
        //e mi offre una perifierica anche di quali sono le caselle libere(non occupate da un pezzo) che invece risultano
        // irraggiungibili dal mio cavallo, magari per raggiiungerle il mio cavallo si sarebbe dovuto spostare in una 
        // delle caselle occupate già da un pezzo.
        
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++)
            System.out.print(this.scacchiera[i][j]+"\t");
         System.out.println();
        }
        // Se il numero di caselle visitate è uguale a zero allora il cavallo è riuscito a visistare tutte le caselle
        //possibili
        System.out.println(count_not_visited==0);
    
       
        }
    
    
    
    public static void main(String args[]){
       Esercizio2 es=new Esercizio2(args[0]);

    }
}