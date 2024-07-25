//Nome: Nicola   Cognome: Tinari     matricola: 1078643       e_mail: nicola.tinari@studio.unibo.it

//per la realizzazione di questo algoritmo ho creato una mia struttura di grandezza k che corrisponde
//ad una tabella di hash, quindi il costo di ricerca sarà costante. Essa gestisce le collisioni con una memorizzazione esterna,
//quindi tramite liste di trabocco. Ogni lista di trabocco presenta al suo interno dei nodi
//indentificati da una stringa, cioè la nostra chiave/parola, e da un valore intero che corrisponde alla sua occorrenza
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Locale;

public class Esercizio1 {
    int k=200;
    Struttura struttura;
//il seguente metodo presa una stringa in input mi restituisce un numero intero che corrisponderà all' indice 
//occupato dalla stringa nella mia struttura 
    public int stringHash(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = (31 * hash + key.charAt(i)) % this.k;
        }
        return hash;
    }
    //la classe Node rappresenta un elemento della mia lista di trabocco e sarà identificata da una stringa(la mia chiave)
    //e da un valore(la sua occorrenza)
    private class Node{
        String key;
        int val;
        Node succ;
        public Node(String s,int n){
            this.key=s;
            this.succ=null;
             this.val=n;
        }
     
    }
    //la classe lista di trabocco rappresenta un singolo elemento della mia struttura e mi permette di memorizzare
    //più di un nodo nella stessa posizione nel caso in cui indice restituito dalla mia funzione stringHash(String s)
    //sia lo stesso per parole diverse(cioè nel caso in cui si verifica una collisione).
    //La mia classe Struttura sarà costituita da un unico nodo detto head che rappresenta il primo nodo della mia lista
    //e scorrendo i puntatori riesco ad accedere a tutti gli elementi che la compongono, come una linkedList
    private class Lista_trabocco{
        Node head;

        public Lista_trabocco(String s, int n){
            this.head=new Node(s,n);
        }
        //il metodo add(...) prende in input una stringa e un numero, crea con queste due variabili un nodo e lo aggiunge
        //in coda alla mia lista di trabocco
        public void add(String parola, int n_occorrenze){
            Node n=this.head;
            while(n.succ!=null)
            n=n.succ;
            n.succ=new Node(parola,n_occorrenze);
        }
        
    }
    //la struttura che userò per contenere le informazioni relative ad ogni parola presente nella pagina e il suo numero
    // totale di occorrenze è identificata dalla classe Struttura che corrisponde ad un array di liste di traboco
    //di grandezza pari a k. La struttura gestisce le collisioni semplicemente memorizzando in coda alla lista tutte le chiavi,
    //cioè le parole, che generano una collisione
    private class Struttura{
        Lista_trabocco[] s;

        public Struttura(int k){
        this.s=new Lista_trabocco[k];
        for(int i=0; i< k; i++)
        this.s[i]=null;
        }
//dopo aver torvato un indice con la mia funzione stringHash() 
//il metodo aggiungiOccorrenze si occuperà di inserire al posto giusto il 
//nodo creato con la stringa e il numero intero dati in input gestendo le 3 casistiche:
//1)la posizione identificata dall' indice nella struttutra è vuota(cioè null),
//e quindi il nodo può essere aggiunto senza problemi 
//2)nella posizione identificata dall' indice è presenta un' altra parola, quindi dovrò scorrere la lista e verificare
//che la parola non sia gia presente al suo interno. Se la risposta è affermativa basterà sommare le nuove occorrenze
//al vecchio valore contrassegnato
//3)se la stringa non è presente nella lista allora un nuovo nodo sarà aggiunto in coda alla lista
        public void aggiungiOccorrenze(String parola, int n_occorrenze){
            parola=parola.toLowerCase();
            int indice=stringHash(parola);
            
            if(this.s[indice]==null){
            this.s[indice]=new Lista_trabocco(parola,n_occorrenze);
            }else if(this.s[indice]!=null && this.s[indice].head.key.equals(parola))
            this.s[indice].head.val=this.s[indice].head.val+n_occorrenze;
            else{
                Node n=this.s[indice].head;
                while(n.succ!=null || n.key.equals(parola)) 
                n=n.succ;
               if(n.succ!=null)
               n.succ.val=n.val+n_occorrenze;
               else
                this.s[indice].add(parola, n_occorrenze);
               
            }
          
        }
    //questo metodo si occupa di cercare una chiave(stringa) nella struttura, una  volta restituito l' indice dalla
    //nostra funzione stringHash(String s) basterà cercare se la parola compare nella lista presente nella posizione
    //indicata dall' indice nella struttura.
    //E se compare ci sarà restituito il suo valore altrimenti sarà restituito il valore 0
       public int occorrenzeParola(String parola){
        parola=parola.toLowerCase();
        int indice=stringHash(parola);
        if(s[indice]==null)//cioè parola non compare
        return 0;
        else if(s[indice].head!=null && s[indice].head.key.equals(parola))
        return s[indice].head.val;
        else{
            Node n=s[indice].head;
            while(n.succ!=null && n.key.equals(parola)==false)
            n=n.succ;
            if(n==null)//cioè parola non compare
            return 0;
            else
            return n.val;
        }
    }
}
    public Esercizio1(String inputf, String inputf1){
       this.struttura=new Struttura(this.k);
       readFile(inputf, inputf1);
 
    }
    //questo metodo si occupa di riempire la nostra struttura usufruendo del metodo creato in precedenza 'aggiungiOccorrenze(...)';   
    //vado quindi a riempire la mia struttura con le parole e le occorrenze scritte nel 1° file in input
     //e mando a video le occorrenze delle parole richieste dal 2° file in input
    public void readFile(String inputf, String inputf1){
         try {
            Scanner f = new Scanner(new FileReader(inputf));
            while(f.hasNextLine()){
                String frase=f.nextLine();
                String[] item=frase.split(",");
                int n=Integer.parseInt(item[1].trim());
                this.struttura.aggiungiOccorrenze(item[0],n);   
            }
            f.close();
            Scanner g=new Scanner(new FileReader(inputf1));
            String chiave="";
           
            while(g.hasNextLine()){
            chiave=g.nextLine();
            System.out.println(chiave+",\t"+this.struttura.occorrenzeParola(chiave));
            }
           g.close();
          } catch(IOException e){
                System.out.println(e.getMessage());
            }


    }
    
   
    public static void main(String args[]){
        Locale.setDefault(Locale.US);
        Esercizio1 es=new Esercizio1(args[0],args[1]);
        
        

    }
}
