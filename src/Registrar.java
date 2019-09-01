import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class Registrar {
    public MongoDatabase db;
    public MongoCollection collection;



    public Registrar() {

        String uri = "mongodb+srv://ramapitchala:ramki123@space-bjfmm.gcp.mongodb.net/test?retryWrites=true"; //connection to the database
        MongoClientURI mongoClientUri = new MongoClientURI(uri);
        MongoClient client = new MongoClient(mongoClientUri);
        db = client.getDatabase("Test");

    }
    public static void main(String [] args){
        Document d = new Document("test2", "test2"); //testing connection of database
        Registrar r = new Registrar();
        r.updateRegistrar(d);
    }
    public void updateRegistrar(Document d){
        collection.insertOne(d);
    }




    }





