package me.thejokerdev.creator.data.types;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.thejokerdev.creator.Main;
import me.thejokerdev.creator.data.Data;
import me.thejokerdev.creator.data.DataType;
import me.thejokerdev.creator.player.CPlayer;
import org.bson.Document;

public class MDBData extends Data {

    private boolean running = false;
    private MongoCollection<Document> playerCollection;
    private MongoCollection<Document> creatorsCollection;

    public MDBData(Main plugin) {
        super(plugin);
    }

    @Override
    public DataType getType() {
        return DataType.MONGODB;
    }

    @Override
    public void syncData(CPlayer var) {
        Document newDocument = new Document("uuid", var.getUniqueId().toString())
                .append("interacted", var.isInteracted())
                .append("date", var.getDateOfInteraction());

        Document document = this.playerCollection.find(Filters.eq("uuid", var.getUniqueId().toString())).first();

        if(document == null || document.isEmpty()) {
            this.playerCollection.insertOne(newDocument);
        } else {
            this.playerCollection.findOneAndReplace(Filters.eq("uuid", var.getUniqueId().toString()), newDocument);
        }
    }

    @Override
    public void getData(CPlayer var) {
        Document document = this.playerCollection.find(Filters.eq("uuid", var.getUniqueId().toString())).first();

        if(document == null) {
            var.setInteracted(false);
            var.setDateOfInteraction(null);
            return;
        }

        var.setInteracted(document.getBoolean("interacted"));
        var.setDateOfInteraction(document.getDate("date"));
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setup() {
        try {
            String mongoURI = plugin.getConfig().getString("settings.data.mongodb.uri");

            MongoClient client;

            if(mongoURI == null || mongoURI.equals("")) {
                client = new MongoClient();
            } else {
                client = new MongoClient(new MongoClientURI(mongoURI));
            }
            MongoDatabase database = client.getDatabase("TheJokerDev_"+plugin.getDescription().getName());

            this.playerCollection = database.getCollection("players");
            running = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
