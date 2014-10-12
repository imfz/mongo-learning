package lv.k2611a;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class App {
    public static void main(String[] args) throws UnknownHostException {

        MongoClient mongoClient = getMongoClient();

        DB db = mongoClient.getDB("k2611a");

        DBCollection users = db.getCollection("users");

        addUsers(users);

        searchUserById(users);

        System.out.println(users.getCount());

        deleteUserById(users);
        System.out.println(users.getCount());

        printAllUsers(users);

        deleteAllUsers(users);

        mongoClient.close();
    }

    private static void printAllUsers(DBCollection users) {
        DBCursor cursor = users.find();
        try {
            while(cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

    private static void deleteAllUsers(DBCollection users) {
        users.drop();
    }

    private static void deleteUserById(DBCollection users) {
        BasicDBObject query = new BasicDBObject("id", 70);
        users.findAndRemove(query);
    }

    private static void searchUserById(DBCollection users) {
        BasicDBObject query = new BasicDBObject("id", 71);

        DBCursor cursor = users.find(query);
        try {
            while(cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

    private static void addUsers(DBCollection users) {
        for (int i=0; i < 100; i++) {
            BasicDBObject basicDBObject = new BasicDBObject("id", i)
                    .append("name", RandomStringUtils.random(10, true, false)
                    );
            users.insert(basicDBObject);
        }
    }

    private static MongoClient getMongoClient() throws UnknownHostException {
        MongoCredential credential = MongoCredential.createMongoCRCredential("usernamehere", "dbhere", "passwordhere".toCharArray());

        List<ServerAddress> serverAddresses = Arrays.asList(
                new ServerAddress("178.62.248.4", 27017),
                new ServerAddress("178.62.247.188", 27017),
                new ServerAddress("178.62.247.189", 27017)
        );
        return new MongoClient(
                serverAddresses,
                Arrays.asList(credential)
                );
    }
}
